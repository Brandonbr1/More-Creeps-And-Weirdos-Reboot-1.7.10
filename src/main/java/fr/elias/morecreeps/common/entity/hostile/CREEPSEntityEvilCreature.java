package fr.elias.morecreeps.common.entity.hostile;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityEvilCreature extends EntityMob {

    public boolean jumping;
    public float modelsize;
    public String texture;

    public CREEPSEntityEvilCreature(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/evilcreature.png";
        this.setSize(this.width * 3F, this.height * 3F);
        this.jumping = false;
        this.isImmuneToFire = true;
        this.modelsize = 3F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new CREEPSEntityEvilCreature.AIAttackEntity());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(110D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(3D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround && this.jumping) {
            this.jumping = false;
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:evilcreaturejump",
                    1.0F * (this.modelsize / 3F),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (3F - this.modelsize) * 2.0F);

            List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(18D, 18D, 18D));
            for (int i = 0; i < list.size(); i++) {
                Entity entity1 = (Entity) list.get(i);

                if ((entity1 instanceof EntityLiving) && !entity1.handleWaterMovement() && entity1.onGround) {
                    double d2 = this.getDistanceToEntity(entity1);
                    entity1.motionY += (17D - d2) * 0.067057996988296509D * (this.modelsize / 3F);
                }
            }
        }

        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.5D * 0.4D + this.motionX * 0.2D;
            this.motionZ = (d1 / f1) * 0.5D * 0.3D + this.motionZ * 0.2D;
            this.motionY = 0.35D;
            this.jumping = true;
        }
        if (this.attackTime-- < 0) {
            AxisAlignedBB attacked_abb = entity.boundingBox;
            AxisAlignedBB mob_abb = this.boundingBox;

            if (mob_abb == null) {
                FMLLog.getLogger().warn("The Entity Bounding Box was unable to be found");
                return;
            }

            if (attacked_abb == null) {
                FMLLog.getLogger().warn("Attacked Entity Bounding Box was unable to be found");
                return;
            }

            if (f < 3D - (2D - this.modelsize) && attacked_abb.maxY > mob_abb.minY && attacked_abb.minY < mob_abb.maxY ) {
                this.attackTime = 20;
                entity.motionY += 0.7D;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
            }
        }
    }

    class AIAttackEntity extends EntityAIBase {

        public CREEPSEntityEvilCreature evilcreature = CREEPSEntityEvilCreature.this;
        public int attackTime;

        public AIAttackEntity() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.evilcreature.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public void updateTask() {
            try {
                --this.attackTime;
                EntityLivingBase entitylivingbase = this.evilcreature.getAttackTarget();
                double d0 = this.evilcreature.getDistanceSqToEntity(entitylivingbase);
                this.evilcreature.attackEntity(entitylivingbase, (float) d0);
                this.evilcreature.getLookHelper()
                .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                /*
                 * if (d0 < 4.0D)
                 * {
                 * if (this.attackTime <= 0)
                 * {
                 * this.attackTime = 20;
                 * this.evilcreature.attackEntityAsMob(entitylivingbase);
                 * }
                 * this.evilcreature.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY,
                 * entitylivingbase.posZ, 1.0D);
                 * }
                 * else if (d0 < 256.0D)
                 * {
                 * // ATTACK ENTITY GOES HERE
                 * this.evilcreature.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                 * }
                 * else
                 */
                // {
                this.evilcreature.getNavigator()
                .clearPathEntity();
                this.evilcreature.getMoveHelper()
                .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
                // }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playLivingSound() {
        String s = this.getLivingSound();

        if (s != null) {
            this.worldObj.playSoundAtEntity(
                    this,
                    s,
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (3F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:evilcreature";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:evilcreaturehurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:evilcreaturedeath";
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    public void fall(float distance, float damageMultiplier) {}

    @Override
    public float getShadowSize() {
        return 2.9F;
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(5) == 0) {
                this.dropItem(Items.bread, this.rand.nextInt(3) + 1);
            } else {
                this.dropItem(Items.fish, this.rand.nextInt(3) + 1);
            }
        }
        super.onDeath(damagesource);
    }
}
