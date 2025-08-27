package fr.elias.morecreeps.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityCastleGuard extends EntityMob {

    private int angerLevel;
    private int randomSoundDelay;
    public String basetexture;
    public int attackdamage;
    public boolean isSwinging;
    public boolean swingArm;
    public int swingTick;
    public String texture;
    public boolean attacked;
    public float hammerswing;
    public float modelsize;
    static final String guardTextures[] = { "/textures/entity/castleguard1.png", "/textures/entity/castleguard2.png",
            "/textures/entity/castleguard3.png", "/textures/entity/castleguard4.png", "/textures/entity/castleguard5.png" };

    public CREEPSEntityCastleGuard(World world) {
        super(world);
        this.angerLevel = 0;
        this.randomSoundDelay = 0;
        this.basetexture = guardTextures[this.rand.nextInt(guardTextures.length)];
        this.texture = this.basetexture;
        this.attacked = false;
        this.hammerswing = 0.0F;
        this.modelsize = 1.0F;
        this.attackdamage = 1;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(1D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        if (this.hammerswing < 0.0F) {
            this.hammerswing += 0.45F;
        } else {
            this.hammerswing = 0.0F;
        }

        super.onUpdate();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        AxisAlignedBB x = this.getBoundingBox();

        return this.worldObj.difficultySetting.getDifficultyId() > 0 && this.worldObj.checkNoEntityCollision(this.getBoundingBox())
                && this.worldObj.getCollidingBoundingBoxes(this, x)
                .size() == 0;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
        nbttagcompound.setBoolean("Attacked", this.attacked);
        nbttagcompound.setString("BaseTexture", this.basetexture);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        nbttagcompound.setBoolean("Attacked", this.attacked);
        nbttagcompound.setString("BaseTexture", this.basetexture);
        this.modelsize = nbttagcompound.getFloat("ModelSize");
        this.texture = this.basetexture;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        if (entity instanceof EntityPlayer) {
            this.attacked = true;
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d2 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d2 * d2);
            this.motionX = (d / f1) * 0.20000000000000001D
                    * (0.58000001192092898D + this.motionX * 0.20000000298023224D);
            this.motionZ = (d2 / f1) * 0.20000000000000001D
                    * (0.52200000119209289D + this.motionZ * 0.20000000298023224D);
            this.motionY = 0.19500000596246447D;
            this.fallDistance = -25F;
        }

        if (f > 3D && this.rand.nextInt(10) == 0) {
            double d1 = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
            double d3 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
            this.motionX += d1 * 0.10999999940395355D;
            this.motionZ += d3 * 0.10999999940395355D;
            this.motionY += 0.023000000044703484D;
        }

        if (f < 2.2999999999999998D - (1.0D - this.modelsize)
                && entity.boundingBox.maxY > entity.boundingBox.minY
                && entity.boundingBox.minY < entity.boundingBox.maxY
                && !(entity instanceof CREEPSEntityCastleGuard)) {
            if (this.hammerswing == 0.0F) {
                this.hammerswing = -2.6F;
            }

            // attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackdamage);
        }
    }

    private void becomeAngryAt(Entity entity) {
        this.attackEntity(entity, 1);
        this.angerLevel = 400 + this.rand.nextInt(400);
        this.randomSoundDelay = this.rand.nextInt(40);
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.0F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (this.attacked && this.rand.nextInt(5) == 0)
            return "morecreeps:castleguardmad";

        if (this.rand.nextInt(12) == 0)
            return "morecreeps:castleguard";
        else
            return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:castleguardhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:castleguarddeath";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
                this.dropItem(MoreCreepsAndWeirdos.donut, this.rand.nextInt(2) + 1);
            }
        }
    }
}
