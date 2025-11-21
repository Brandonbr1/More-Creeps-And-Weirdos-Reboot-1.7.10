package fr.elias.morecreeps.common.entity.nice;

import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityHippo extends EntityMob {

    protected double attackrange;
    public boolean bone;
    protected int attack;
    public float goatsize;

    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public int goatlevel;
    public double waterX;
    public double waterY;
    public double waterZ;
    public boolean findwater;
    public float modelsize;
    public boolean hippoHit;
    public String texture;

    public CREEPSEntityHippo(World world) {
        super(world);
        this.bone = false;
        this.texture = "morecreeps:textures/entity/hippo2.png";
        this.setSize(2.0F, 1.4F);
        this.attack = 2;
        this.attackrange = 16D;
        this.hungry = false;
        this.findwater = false;
        this.hungrytime = this.rand.nextInt(100) + 10;
        this.goatlevel = 1;
        this.modelsize = 2.0F;
        this.hippoHit = false;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.45D, true));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityMob.class, 0.45D, false));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityAnimal.class, 0.45D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new CREEPSEntityHippo.AIAttackEntity(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new CREEPSEntityHippo.AIAttackEntity(this, EntityMob.class, true));
        this.targetTasks.addTask(2, new CREEPSEntityHippo.AIAttackEntity(this, EntityAnimal.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(25);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.45D);
    }

    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (this.worldObj.getBlock(x, y, z) == Blocks.water || this.worldObj.getBlock(x, y, z) == Blocks.flowing_water)
            return 10F;
        else return -(float) y;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
     * attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected EntityLivingBase findPlayerToAttack() {
        if (!this.hippoHit) return null;

        if (this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
            float f = this.getBrightness(1.0F);

            if (f < 0.0F) {
                EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, this.attackrange);

                if (entityplayer != null) return entityplayer;
            }

            if (this.rand.nextInt(10) == 0) {
                EntityLivingBase entityliving = this.getClosestTarget(this, 15D);
                return entityliving;
            }
        }

        return null;
    }

    public EntityLivingBase getClosestTarget(Entity entity, double d) {
        double d1 = -1D;
        EntityLivingBase entityliving = null;

        for (int i = 0; i < this.worldObj.loadedEntityList.size(); i++) {
            Entity entity1 = (Entity) this.worldObj.loadedEntityList.get(i);

            if (!(entity1 instanceof EntityLiving) || entity1 == entity
                || entity1 == entity.riddenByEntity
                || entity1 == entity.ridingEntity
                || (entity1 instanceof EntityPlayer)
                || (entity1 instanceof EntityMob)
                || (entity1 instanceof EntityAnimal)) {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1)
                && ((EntityLivingBase) entity1).canEntityBeSeen(entity)) {
                d1 = d2;
                entityliving = (EntityLivingBase) entity1;
            }
        }

        return entityliving;
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.hungry = false;
        this.hippoHit = true;

        if (entity != null) {
            this.setRevengeTarget((EntityLivingBase) entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
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
            this.motionX = (d / f1) * 0.20000000000000001D * (0.850000011920929D + this.motionX * 0.20000000298023224D);
            this.motionZ = (d1 / f1) * 0.20000000000000001D
                * (0.80000001192092896D + this.motionZ * 0.20000000298023224D);
            this.motionY = 0.10000000596246449D;
            this.fallDistance = -25F;
        }
    }

    class AIAttackEntity extends EntityAINearestAttackableTarget {

        public AIAttackEntity(EntityCreature p_i45878_1_, Class<? extends net.minecraft.entity.Entity> p_i45878_2_,
            boolean p_i45878_3_) {
            super(p_i45878_1_, p_i45878_2_, 1, p_i45878_3_);
        }

        @Override
        public boolean shouldExecute() {
            return CREEPSEntityHippo.this.hippoHit && CREEPSEntityHippo.this.getAttackTarget() != null
                && super.shouldExecute();
        }

        @Override
        public void startExecuting() {
            CREEPSEntityHippo.this.setAttackTarget(CREEPSEntityHippo.this.findPlayerToAttack());
            super.startExecuting();
        }

        @Override
        public void updateTask() {
            try {
                double d0 = CREEPSEntityHippo.this.getDistanceSqToEntity(CREEPSEntityHippo.this.getAttackTarget());
                if (d0 < 256D) {
                    CREEPSEntityHippo.this.attackEntity(CREEPSEntityHippo.this.getAttackTarget(), (float) d0);
                }
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
        int l = this.worldObj.getFullBlockLightValue(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return (i1 == Blocks.grass || i1 == Blocks.dirt) && i1 != Blocks.cobblestone
            && i1 != Blocks.log
            && i1 != Blocks.stone_slab
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && this.rand.nextInt(35) == 0
            && l > 7;
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Hungry", this.hungry);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.hungry = nbttagcompound.getBoolean("Hungry");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
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
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (2.0F - this.modelsize) * 2.0F);
        }
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        return "morecreeps:hippo";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:hippohurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:hippodeath";
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(10) == 0) {
                this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
            }

            if (this.rand.nextInt(10) == 0) {
                this.dropItem(Items.wheat_seeds, this.rand.nextInt(3) + 1);
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
