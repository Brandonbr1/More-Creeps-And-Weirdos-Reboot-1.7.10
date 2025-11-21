package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityCastleCritter extends EntityMob {

    protected double attackrange;
    protected int attack;
    public float modelsize;
    public String texture;
    public int attackTime;

    public CREEPSEntityCastleCritter(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/castlecritter.png";
        this.setSize(0.6F, 0.6F);
        this.attack = 1;
        this.attackrange = 16D;
        this.modelsize = 1.6F;
        this.targetTasks.addTask(0, new CREEPSEntityCastleCritter.AIAttackEntity());
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(6D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.4D);
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to
     * path to the block. Args: x, y, z
     */
    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (this.worldObj.getBlock(x, y, z) == Blocks.double_stone_slab
            || this.worldObj.getBlock(x, y, z) == Blocks.stone_slab) return 10F;
        else return -(float) y;
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
            if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

            if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                this.setRevengeTarget((EntityLivingBase) entity);
            }

            return true;
        } else return false;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.20000000000000001D
                * (0.80000001192092896D + this.motionX * 0.20000000298023224D);
            this.motionZ = (d1 / f1) * 0.20000000000000001D
                * (0.75000001192092891D + this.motionZ * 0.20000000298023224D);
            this.motionY = 0.10000000596246449D;
            this.fallDistance = -25F;

            if (this.rand.nextInt(5) == 0) {
                double d2 = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
                double d3 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
                this.motionX += d2 * 0.64999997615814209D;
                this.motionZ += d3 * 0.64999997615814209D;
            }
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
        }
    }

    class AIAttackEntity extends EntityAINearestAttackableTarget {

        public AIAttackEntity() {
            super(CREEPSEntityCastleCritter.this, EntityPlayer.class, 2, true);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase target = CREEPSEntityCastleCritter.this.getAttackTarget();
            return target != null && super.shouldExecute();
        }

        @Override
        public void updateTask() {
            try {
                float f = CREEPSEntityCastleCritter.this
                    .getDistanceToEntity(CREEPSEntityCastleCritter.this.getAttackTarget());
                CREEPSEntityCastleCritter.this.attackEntity(CREEPSEntityCastleCritter.this.getAttackTarget(), f);

            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    /** Checks if the entity's current position is a valid location to spawn this entity. */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null) return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.cobblestone && i1 != Blocks.log
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0;
    }

    /** Plays living's sound at its position */
    @Override
    public void playLivingSound() {
        String s = this.getLivingSound();

        if (s != null) {
            this.worldObj.playSoundAtEntity(
                this,
                s,
                this.getSoundVolume(),
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.6F - this.modelsize) * 2.0F);
        }
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        if (this.rand.nextInt(5) == 0) return "morecreeps:castlecritter";
        else return null;
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:castlecritterhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:castlecritterdeath";
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(10) == 0) {
                this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
            }

            if (this.rand.nextInt(10) == 0) {
                this.dropItem(Items.bone, this.rand.nextInt(3) + 1);
            }
        }

        super.onDeath(damagesource);
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }
}
