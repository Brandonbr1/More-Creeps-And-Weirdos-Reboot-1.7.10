package fr.elias.morecreeps.common.entity.hostile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityEvilLight;

public class CREEPSEntityRobotTed extends EntityMob {

    public static Random rand = new Random();
    protected double attackRange;
    // private int angerLevel; // TODO (unused)
    public boolean jumping;
    public float robotsize;
    public int floattimer;
    public int floatdir;
    protected Entity playerToAttack;
    protected boolean hasAttacked;
    public float modelspeed;
    public double floatcycle;
    public double floatmaxcycle;

    public String texture;

    public CREEPSEntityRobotTed(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/robotted.png";
        // this.angerLevel = 0; // TODO (unused)
        this.attackRange = 16D;
        this.jumping = false;
        this.robotsize = 2.5F;
        this.setSize(this.width * (this.robotsize * 0.8F), this.height * (this.robotsize * 0.8F));
        this.modelspeed = 0.61F;
        this.floattimer = 0;
        this.floatdir = 1;
        this.floatcycle = 0.0D;
        this.floatmaxcycle = 0.10499999672174454D;

        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBreakDoor(this));

        this.tasks.addTask(2, new AIAttackEntity());

        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        this.tasks.addTask(5, new EntityAIWander(this, 0.25D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, CREEPSEntityRobotTodd.class, 8F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, CREEPSEntityRobotTodd.class, 0, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(45D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.061D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(1D);
    }

    @Override
    public float getEyeHeight() {
        return 2.0F;
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("RobotSize", this.robotsize);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.robotsize = nbttagcompound.getFloat("RobotSize");
    }

    /** Checks if the entity's current position is a valid location to spawn this entity. */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null) return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        int l = this.worldObj.getBlockLightOpacity(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.cobblestone && i1 != Blocks.log
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.stone_slab
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && rand.nextInt(10) == 0
            && l > 8;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.robotsize > 1.0F) {
            super.ignoreFrustumCheck = true;
        } else {
            super.ignoreFrustumCheck = false;
        }

        if (this.modelspeed < 0.05F) {
            this.modelspeed = 0.05F;
        }

        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(this.modelspeed);
        this.isJumping = false;

        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy
                .robotTedSmoke(this.worldObj, this.posX, this.posY, this.posZ, this.floattimer, this.modelspeed);
        }
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate() {
        if (this.isEntityInsideOpaqueBlock()) {
            this.posY += 2.5D;
            this.floatdir = 1;
            this.floatcycle = 0.0D;
        }

        if (this.floatdir > 0) {
            this.floatcycle += 0.017999999225139618D;

            if (this.floatcycle > this.floatmaxcycle) {
                this.floatdir = this.floatdir * -1;
                this.fallDistance += -1.5F;
            }
        } else {
            this.floatcycle -= 0.0094999996945261955D;

            if (this.floatcycle < -this.floatmaxcycle) {
                this.floatdir = this.floatdir * -1;
                this.fallDistance += -1.5F;
            }
        }

        super.onUpdate();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (entity instanceof EntityPlayer) {
            double d = this.getDistanceToEntity(entity);

            if (this.posY > entity.posY && d < 6D) {
                this.motionY = -0.02500000037252903D;
            }

            if (this.posY < entity.posY - 0.5D && d < 6D) {
                this.motionY = 0.043999999761581421D;
            }

            if (d < 3D) {
                double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
                double d4 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
                this.motionX = -(d2 * 0.20000000298023224D);
                this.motionZ = -(d4 * 0.20000000298023224D);

                if (this.posY > entity.posY) {
                    this.motionY = -0.070000000298023224D;
                }
            }
        }

        this.fallDistance = -25F;
        double d1 = entity.posX - this.posX;
        double d3 = entity.posZ - this.posZ;
        float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
        this.motionX = (d1 / f1) * 0.5D * 0.35000000192092895D + this.motionX * 0.20000000098023224D;
        this.motionZ = (d3 / f1) * 0.5D * 0.25000000192092897D + this.motionZ * 0.20000000098023224D;
        this.jumping = true;

        if (this.attackTime <= 0 && f < (double) (3.1F - (2.5F - this.robotsize))
            && entity.boundingBox.maxY > this.boundingBox.minY
            && entity.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            super.attackEntityAsMob(entity);
        }
    }

    // to make attackEntity works in 1.8
    public class AIAttackEntity extends EntityAIBase {

        public CREEPSEntityRobotTed robot = CREEPSEntityRobotTed.this;
        public int attackTime;

        public AIAttackEntity() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public void updateTask() {
            --this.attackTime;
            EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
            if (entitylivingbase == null) return;
            double d0 = this.robot.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D) {
                this.robot.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            } else if (d0 < 256.0D) {
                this.robot.getLookHelper()
                    .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            } else {
                this.robot.getNavigator()
                    .clearPathEntity();
                this.robot.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
            }
        }
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();

        if (entity != null) {
            double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
            this.motionX = d * 1.25D;
            this.motionZ = d1 * 1.25D;

            if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
                if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

                if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                    this.setRevengeTarget((EntityLivingBase) entity);
                }

                return true;
            } else return false;
        } else return false;
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
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
                (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (2.5F - this.robotsize) * 2.0F);
        }
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        return "morecreeps:tedinsult";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:robothurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:teddead";
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 4; i++) {
                CREEPSEntityEvilLight creepsentityevillight = new CREEPSEntityEvilLight(this.worldObj);
                creepsentityevillight.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);

                if (damagesource != null) {
                    creepsentityevillight.motionX = -(rand.nextFloat() * 2.0F);
                    creepsentityevillight.motionZ = -(rand.nextFloat() * 2.0F);
                    creepsentityevillight.lifespan = 15;
                }
                this.worldObj.spawnEntityInWorld(creepsentityevillight);
            }

            this.dropItem(MoreCreepsAndWeirdos.ram16k, rand.nextInt(2) + 1);
        }
        super.onDeath(damagesource);
    }
}
