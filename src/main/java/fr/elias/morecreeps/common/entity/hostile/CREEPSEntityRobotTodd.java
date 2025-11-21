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
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityRobotTodd extends EntityMob {

    public static Random rand = new Random();
    protected double attackRange;
    // private int angerLevel; // TODO (unused)
    public boolean jumping;
    public float robotsize;
    public int texswitch;
    public int texnumber;
    public float modelspeed;
    public String texture;

    public CREEPSEntityRobotTodd(World world) {
        super(world);
        this.texnumber = 0;
        this.texture = "morecreeps:textures/entity/robottodd1.png";
        // this.angerLevel = 0; // TODO (unused)
        this.attackRange = 16D;
        this.jumping = false;
        this.robotsize = 2.5F;
        this.yOffset *= 1.5F;
        this.setSize(1.5F, 2.5F);
        this.modelspeed = 0.4F;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIBreakDoor(this));

        this.tasks.addTask(2, new AIAttackEntity());

        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
        this.tasks.addTask(5, new EntityAIWander(this, 0.25D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, CREEPSEntityRobotTed.class, 8F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, CREEPSEntityRobotTed.class, 0, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(45D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(2D);
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("RobotSize", this.robotsize);
        nbttagcompound.setFloat("ModelSpeed", this.modelspeed);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.robotsize = nbttagcompound.getFloat("RobotSize");
        this.modelspeed = nbttagcompound.getFloat("ModelSpeed");
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
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(this.modelspeed);
        super.onLivingUpdate();

        if (this.texswitch++ > 40) {
            if (this.texnumber++ > 1) {
                this.texnumber = 0;
            }

            if (this.texnumber == 0) {
                this.texture = "morecreeps:textures/entity/robottodd1.png";
            }

            if (this.texnumber == 1) {
                this.texture = "morecreeps:textures/entity/robottodd2.png";
            }
        }
    }

    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.5D * 0.40000000192092894D + this.motionX * 0.20000000098023224D;
            this.motionZ = (d1 / f1) * 0.5D * 0.30000000192092896D + this.motionZ * 0.20000000098023224D;
            this.motionY = 0.35000000196046449D;
            this.jumping = true;

            if (this.attackTime <= 0 && f < 2.0F
                && entity.boundingBox.maxY > this.boundingBox.minY
                && entity.boundingBox.minY < this.boundingBox.maxY) {
                super.attackEntityAsMob(entity);
            }
        }
    }

    // to make attackEntity works in 1.8
    public class AIAttackEntity extends EntityAIBase {

        public CREEPSEntityRobotTodd robot = CREEPSEntityRobotTodd.this;
        public int attackTime;

        public AIAttackEntity() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public void updateTask() {
            try {
                --this.attackTime;
                EntityLivingBase entitylivingbase = this.robot.getAttackTarget();
                double d0 = this.robot.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 40;
                        this.robot.attackEntityAsMob(entitylivingbase); // or entitylivingbase.attackEntityFrom
                        // blablabla...
                    }

                    this.robot.getMoveHelper()
                        .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
                } else if (d0 < 256.0D) {
                    // ATTACK ENTITY JUST CALLED HERE :D
                    this.robot.attackEntity(entitylivingbase, (float) d0);
                    this.robot.getLookHelper()
                        .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                } else {
                    this.robot.getNavigator()
                        .clearPathEntity();
                    this.robot.getMoveHelper()
                        .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

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
        return "morecreeps:toddinsult";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:robothurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:todddead";
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            this.dropItem(MoreCreepsAndWeirdos.battery, rand.nextInt(2) + 1);
        }
        super.onDeath(damagesource);
    }
}
