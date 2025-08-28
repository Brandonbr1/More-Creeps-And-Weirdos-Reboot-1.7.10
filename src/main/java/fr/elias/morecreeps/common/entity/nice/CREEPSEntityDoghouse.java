package fr.elias.morecreeps.common.entity.nice;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityDoghouse extends EntityAnimal {

    public float modelsize;
    public boolean houseoccupied;
    public String texture;

    public CREEPSEntityDoghouse(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/doghouse.png";
        this.modelsize = 2.5F;
        this.setSize(this.width * this.modelsize, this.height * this.modelsize);
        this.houseoccupied = false;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(20D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.0D);
    }

    /**
     * Returns the Y Offset of this entity.
     */
    @Override
    public double getYOffset() {
        if (this.ridingEntity instanceof CREEPSEntityHotdog)
            return -(double) 1.1F;
        else
            return super.getYOffset();
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity == null)
            return;

        if (this.riddenByEntity instanceof CREEPSEntityHotdog) {
            this.riddenByEntity.setPosition(this.posX, this.posY, this.posZ);
            return;
        } else
            return;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(10) == 0 && this.riddenByEntity != null) {
            if (this.worldObj.isRemote) {
                MoreCreepsAndWeirdos.proxy.bubbleDoghouse(this.worldObj, this);
            }
        }

        if (this.inWater) {
            this.setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.ignoreFrustumCheck = true;
        super.onUpdate();
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return this.getHealth() < 1;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (this.riddenByEntity == null) {
            List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(16D, 16D, 16D));
            int i = 0;

            do {
                if (i >= list.size()) {
                    break;
                }

                Entity entity = (Entity) list.get(i);

                if ((entity instanceof CREEPSEntityHotdog) && entity.ridingEntity == null) {
                    CREEPSEntityHotdog creepsentityhotdog = (CREEPSEntityHotdog) entity;

                    if (creepsentityhotdog.tamed) {
                        creepsentityhotdog.mountEntity(this);
                        this.houseoccupied = true;
                        this.worldObj.playSoundAtEntity(
                                this,
                                "morecreeps:hotdogpickup",
                                1.0F,
                                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        break;
                    }
                }

                i++;
            } while (true);
        } else {
            this.riddenByEntity.fallDistance = -10F;
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:hotdogputdown",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.houseoccupied = false;
            this.riddenByEntity.mountEntity(null);
        }

        return false;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, int i) {
        if (i < 1) {
            i = 1;
        }

        this.hurtTime = this.maxHurtTime = 10;
        this.smoke();

        if (this.getHealth() <= 0) {
            this.worldObj.playSoundAtEntity(
                    this,
                    this.getDeathSound(),
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.onDeath(damagesource);
        } else {
            this.worldObj.playSoundAtEntity(
                    this,
                    this.getHurtSound(),
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        super.attackEntityFrom(damagesource, i);
        return true;
    }

    public void loadHouse() {
        List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(16D, 16D, 16D));

        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);

            if (entity != null && (entity instanceof CREEPSEntityHotdog) && ((CREEPSEntityHotdog) entity).tamed) {
                entity.mountEntity(this);
                this.houseoccupied = true;
                return;
            }
        }

        this.houseoccupied = false;
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return this.getHealth() <= 0;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
        nbttagcompound.setBoolean("Occupied", this.houseoccupied);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.modelsize = nbttagcompound.getFloat("ModelSize");
        this.houseoccupied = nbttagcompound.getBoolean("Occupied");
        this.loadHouse();
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    private void smoke() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
                            d,
                            d1,
                            d2);
                }
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return null;
    }

    public void confetti() {
        List<?> list = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(8D, 4D, 8D));
        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);
            float f = this.getDistanceToEntity(entity);
            if (f < 6F) {
                if (!this.worldObj.isRemote) {
                    MoreCreepsAndWeirdos.proxy.confettiA((EntityPlayer) entity, this.worldObj);
                }
            }
        }
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead() {
        if (this.worldObj.isRemote) {
            this.smoke();
        }
        super.setDead();
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new CREEPSEntityDoghouse(this.worldObj);
    }
}
