package fr.elias.morecreeps.common.entity.hostile;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityEvilEgg extends Entity  {

    protected double initialVelocity;
    double bounceFactor;
    int fuse;
    boolean exploded;
    public Entity owner;

    public CREEPSEntityEvilEgg(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.initialVelocity = 1.0D;
        this.bounceFactor = 0.84999999999999998D;
        this.exploded = false;
        // delayBeforeCanPickup;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double p_70112_1_) {
        double d1 = this.boundingBox.getAverageEdgeLength();
        d1 *= 64.0D * this.renderDistanceWeight;
        return p_70112_1_ < d1 * d1;
    }

    public CREEPSEntityEvilEgg(World world, Entity entity) {
        this(world);
        this.owner = entity;
        this.setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        this.motionX = 1.1000000000000001D * d * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
        this.motionY = -1.1000000000000001D * MathHelper.sin((entity.rotationPitch / 180F) * (float) Math.PI);
        this.motionZ = 1.1000000000000001D * d1 * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
        this.setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public CREEPSEntityEvilEgg(World world, double d, double d1, double d2) {
        this(world);
        this.setPosition(d, d1, d2);
    }

    public void func_20048_a(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d1 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d2 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / Math.PI);
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @Override
    public void setVelocity(double d, double d1, double d2) {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        double d = this.motionX;
        double d1 = this.motionY;
        double d2 = this.motionZ;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.motionX != d) {
            if (this.rand.nextInt(40) == 0) {
                CREEPSEntityEvilChicken creepsentityevilchicken = new CREEPSEntityEvilChicken(this.worldObj);
                creepsentityevilchicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                creepsentityevilchicken.motionX = this.rand.nextFloat() * 0.3F;
                creepsentityevilchicken.motionY = this.rand.nextFloat() * 0.4F;
                creepsentityevilchicken.motionZ = this.rand.nextFloat() * 0.4F;
                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentityevilchicken);
                }
                this.setDead();
            } else {
                this.explode();
            }
        }

        if (this.motionY != d1) {
            if (this.rand.nextInt(40) == 0) {
                CREEPSEntityEvilChicken creepsentityevilchicken1 = new CREEPSEntityEvilChicken(this.worldObj);
                creepsentityevilchicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                creepsentityevilchicken1.motionX = this.rand.nextFloat() * 0.3F;
                creepsentityevilchicken1.motionY = this.rand.nextFloat() * 0.4F;
                creepsentityevilchicken1.motionZ = this.rand.nextFloat() * 0.4F;
                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentityevilchicken1);
                }
                this.setDead();
            } else {
                this.explode();
            }
        }

        if (this.motionY != d1) {
            if (this.rand.nextInt(40) == 0) {
                CREEPSEntityEvilChicken creepsentityevilchicken2 = new CREEPSEntityEvilChicken(this.worldObj);
                creepsentityevilchicken2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                creepsentityevilchicken2.motionX = this.rand.nextFloat() * 0.3F;
                creepsentityevilchicken2.motionY = this.rand.nextFloat() * 0.4F;
                creepsentityevilchicken2.motionZ = this.rand.nextFloat() * 0.4F;
                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentityevilchicken2);
                }
                this.setDead();
            } else {
                this.explode();
            }
        } else {
            this.motionY -= 0.040000000000000001D;
        }

        this.motionX *= 0.97999999999999998D;
        this.motionY *= 0.995D;
        this.motionZ *= 0.97999999999999998D;

        if (this.handleWaterMovement()) {
            if (this.worldObj.isRemote) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 10; j++) {
                        float f = 0.85F;
                        this.worldObj.spawnParticle(
                                "BUBBLE".toLowerCase(),
                                this.posX - this.motionX - 0.25D * f,
                                this.posY - this.motionY - 0.25D * f,
                                this.posZ - this.motionZ - 0.25D * f,
                                this.motionX,
                                this.motionY,
                                this.motionZ);
                    }
                }
            }

            this.setDead();
        }

        List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));

        /**
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            getBoundingBox().addCoord(motionX, motionY, motionZ)
                .expand(1.0D, 1.0D, 1.0D));
         **/

        for (int k = 0; k < list.size(); k++) {
            Entity entity = (Entity) list.get(k);

            if (entity != null && entity.canBeCollidedWith() && !(entity instanceof EntityPlayer)) {
                this.explode();
            }
        }
    }

    private void explode() {
        if (!this.exploded) {
            this.exploded = true;
            if (!this.worldObj.isRemote) {
                this.worldObj.createExplosion(this.owner, this.posX, this.posY, this.posZ, 2.0F, true);
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}


    @Override
    protected void entityInit() {
        // TODO Auto-generated method stub

    }
}
