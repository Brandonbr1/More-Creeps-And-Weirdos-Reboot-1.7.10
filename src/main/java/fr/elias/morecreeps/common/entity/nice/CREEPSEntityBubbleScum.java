package fr.elias.morecreeps.common.entity.nice;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.client.particles.CREEPSFxBubbles;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityBubbleScum extends EntityCreature {

    public boolean rideable;
    public int interest;
    private boolean primed;
    public boolean tamed;
    private float distance;
    public int armor;
    public boolean used;
    public boolean grab;
    public List<?> piglist;
    public int pigstack;
    public int level;
    public float totaldamage;
    public int alt;
    public boolean hotelbuilt;
    public int wanderstate;
    public int speedboost;
    public int totalexperience;
    public int part;
    public boolean tossed;
    public float modelsize;
    public int attackTime;
    public String texture;
    static final int leveldamage[] = { 0, 200, 600, 1000, 1500, 2000, 2700, 3500, 4400, 5400, 6600, 7900, 9300, 10800,
            12400, 14100, 15800, 17600, 19500, 21500, 25000 };
    static final String levelname[] = { "Guinea Pig", "A nothing pig", "An inexperienced pig", "Trainee", "Private",
            "Private First Class", "Corporal", "Sergeant", "Staff Sergeant", "Sergeant First Class", "Master Segeant",
            "First Sergeant", "Sergeant Major", "Command Sergeant Major", "Second Lieutenant", "First Lieutenant",
            "Captain", "Major", "Lieutenant Colonel", "Colonel", "General of the Pig Army", "General of the Pig Army" };

    public CREEPSEntityBubbleScum(World world) {
        super(world);
        this.primed = false;
        this.texture = "morecreeps:textures/entity/bubblescum.png";
        this.setSize(0.6F, 0.6F);
        this.rideable = false;
        this.pigstack = 0;
        this.level = 1;
        this.totaldamage = 0.0F;
        this.alt = 1;
        this.hotelbuilt = false;
        this.wanderstate = 0;
        this.speedboost = 0;
        this.totalexperience = 0;
        this.fallDistance = -5F;
        this.tossed = false;
        this.modelsize = 1.0F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(5, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.6D);
    }

    // TODO The below was here for 1.8, does it apply for 1.7.10?
    // "Don't need this anymore..."
    @Override
    protected void attackEntity(Entity entity, float f) {
        double d = entity.posX - this.posX;
        double d1 = entity.posZ - this.posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        this.motionX = (d / f1) * 0.40000000000000002D * 0.16000000192092895D + this.motionX * 0.18000000098023225D;
        this.motionZ = (d1 / f1) * 0.40000000000000002D * 0.12000000192092895D + this.motionZ * 0.18000000098023225D;

        if (f < 2D && entity.boundingBox.maxY > this.boundingBox.minY
                && entity.boundingBox.minY < this.boundingBox.maxY) {
            this.attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.knockBack(this, i, 3D, 5D);

        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityToAttack = this.getAttackTarget();
            entityToAttack = (EntityLivingBase) entity;
        }
        return super.attackEntityFrom(damagesource, i);
    }

    /** finally, i will not use this.. **/
    /*
     * public EntityPlayer getPlayer()
     * {
     * EntityPlayer entityplayer = null;
     * for (int i = 0; i < worldObj.playerEntities.size(); ++i)
     * {
     * EntityPlayer entityplayer1 = (EntityPlayer)worldObj.playerEntities.get(i);
     * if (IEntitySelector.NOT_SPECTATING.apply(entityplayer1))
     * {
     * entityplayer = entityplayer1;
     * }
     * }
     * return entityplayer;
     * }
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.modelsize > 1.0F) {
            this.ignoreFrustumCheck = true;
        }
        EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 1);
        float f = this.fallDistance;

        // TODO find another player instance...
        if (f > 10F && this.tossed) {
            this.confetti();
            this.tossed = false;
            this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsAndWeirdos.achieve10bubble, 1);
        }

        if (f > 25F && this.tossed) {
            this.confetti();
            this.tossed = false;
            this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsAndWeirdos.achieve25bubble, 1);
        }

        if (f > 50F && this.tossed) {
            this.confetti();
            this.tossed = false;
            this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsAndWeirdos.achieve50bubble, 1);
        }

        if (f > 100F && this.tossed) {
            this.confetti();
            this.tossed = false;
            this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
            player.addStat(MoreCreepsAndWeirdos.achieve100bubble, 1);
        }

        if (this.rand.nextInt(3) == 0) {
            if (this.worldObj.isRemote) {
                MoreCreepsAndWeirdos.proxy.bubble(this.worldObj, this);
            }
        }

        if (this.onGround) {
            this.tossed = false;
        }
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

    private void smoke() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 7; i++) {
                double d = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d4 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(
                        "HEART".toLowerCase(),
                        (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        this.posY + 0.5D + this.rand.nextFloat() * this.height,
                        (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        d,
                        d2,
                        d4);
            }

            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 10; k++) {
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d3 = this.rand.nextGaussian() * 0.02D;
                    double d5 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height + j,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d1,
                            d3,
                            d5);
                }
            }
        }
    }

    private void smokePlain() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 10; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height + i,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                }
            }
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        // Method used by Minecraft below, probably better to leave it?
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        return this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0 && this.rand.nextInt(5) == 0;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 12;
    }

    @Override
    public double getYOffset() {
        if (this.ridingEntity instanceof EntityPlayer) {
            float f = 1.0F - this.modelsize;

            if (this.modelsize > 1.0F) {
                f *= 0.55F;
            }

            return (this.getMountedYOffset() - 1.5F) + f * 0.6F;
        } else
            return this.getMountedYOffset();
    }

    @Override
    public void updateRiderPosition() {
        this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    @Override
    public double getMountedYOffset() {
        return 0.5D;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.used = false;

        if (itemstack == null && !(this.getAttackTarget() instanceof EntityPlayer)) {
            this.rotationYaw = entityplayer.rotationYaw;
            Object obj = entityplayer;

            if (this.ridingEntity != obj) {
                int i;

                for (i = 0; ((Entity) obj).riddenByEntity != null && i < 20; i++) {
                    obj = ((Entity) obj).riddenByEntity;
                }

                if (i < 20) {
                    this.rotationYaw = ((Entity) obj).rotationYaw;
                    this.mountEntity((Entity) obj);
                    this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:bubblescumpickup",
                            1.0F,
                            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }
            } else {
                int j;

                for (j = 0; ((Entity) obj).riddenByEntity != null && j < 20; j++) {
                    obj = ((Entity) obj).riddenByEntity;
                }

                if (j < 20) {
                    this.rotationYaw = ((Entity) obj).rotationYaw;
                    ((Entity) obj).fallDistance = -5F;
                    ((Entity) obj).mountEntity(null);
                    double d = -MathHelper.sin((entityplayer.rotationYaw * (float) Math.PI) / 180F);
                    double d1 = MathHelper.cos((entityplayer.rotationYaw * (float) Math.PI) / 180F);
                    double d2 = -MathHelper.sin((entityplayer.rotationPitch / 180F) * (float) Math.PI);
                    ((Entity) obj).motionX = 1.0D * d;
                    ((Entity) obj).motionZ = 1.0D * d1;
                    ((Entity) obj).motionY = 1.0D * d2;
                    this.tossed = true;
                    this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:bubblescumputdown",
                            1.0F,
                            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }
            }
        }

        return true;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (this.ridingEntity == null) {
            if (this.rand.nextInt(1) == 0)
                return "morecreeps:bubblescum";
            else
                return null;
        } else
            return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:bubblescumhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:bubblescumdeath";
    }

    @Override
    public void setDead() {
        this.smokePlain();
        super.setDead();

        for (int i = 0; i < 25; i++) {
            // fixed error and decommeted (Is there a better word?) it
            double d = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
            double d1 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
            CREEPSFxBubbles creepsfxbubbles = new CREEPSFxBubbles(
                    this.worldObj,
                    this.posX + d * 0.5D,
                    this.posY,
                    this.posZ + d1 * 0.5D,
                    MoreCreepsAndWeirdos.partRed,
                    0.5F);
            creepsfxbubbles.renderDistanceWeight = 10D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
        }

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(25) == 0) {
                this.dropItem(Items.cookie, 1);
            }
        }
    }

    public void confetti() {
        MoreCreepsAndWeirdos.proxy.confettiA(this, this.worldObj);
    }
}
