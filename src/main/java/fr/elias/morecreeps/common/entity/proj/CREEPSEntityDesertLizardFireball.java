package fr.elias.morecreeps.common.entity.proj;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class CREEPSEntityDesertLizardFireball extends Entity {

    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private boolean inGround;
    public int field_9406_a;
    private int ticksAlive;
    private int ticksInAir;
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public Entity shootingEntity;

    public CREEPSEntityDesertLizardFireball(World world) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = null;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        this.setSize(0.5F, 0.5F);
    }

    @Override
    protected void entityInit() {}

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public CREEPSEntityDesertLizardFireball(World world, EntityLivingBase entityliving, double d, double d1,
            double d2) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = null;
        this.inGround = false;
        this.field_9406_a = 0;
        this.ticksInAir = 0;
        this.shootingEntity = entityliving;
        this.setSize(0.1F, 0.1F);
        this.setLocationAndAngles(
                entityliving.posX,
                entityliving.posY,
                entityliving.posZ,
                entityliving.rotationYaw,
                entityliving.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = this.motionY = this.motionZ = 0.0D;
        d += this.rand.nextGaussian() * 0.40000000000000002D;
        d1 += this.rand.nextGaussian() * 0.40000000000000002D;
        d2 += this.rand.nextGaussian() * 0.40000000000000002D;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        this.accelerationX = (d / d3) * 0.10000000000000001D;
        this.accelerationY = (d1 / d3) * 0.10000000000000001D;
        this.accelerationZ = (d2 / d3) * 0.10000000000000001D;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.setFire(10);

        if (this.field_9406_a > 0) {
            this.field_9406_a--;
        }

        if (this.inGround) {
            Block i = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

            if (i != this.inTile) {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.1F;
                this.motionY *= this.rand.nextFloat() * 0.1F;
                this.motionZ *= this.rand.nextFloat() * 0.08F;
                this.ticksAlive = 0;
                this.ticksInAir = 0;
            } else {
                this.ticksAlive++;

                if (this.ticksAlive == 1200) {
                    this.setDead();
                }

                return;
            }
        } else {
            this.ticksInAir++;
        }

        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec3d1 = Vec3.createVectorHelper(
                    movingobjectposition.hitVec.xCoord,
                    movingobjectposition.hitVec.yCoord,
                    movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.canBeCollidedWith() || entity1 == this.shootingEntity && this.ticksInAir < 25) {
                continue;
            }

            float f2 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox
                    .expand(f2, f2, f2);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null) {
                continue;
            }

            double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d1 < d || d == 0.0D) {
                entity = entity1;
                d = d1;
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null) {
                if (movingobjectposition.entityHit
                        .attackEntityFrom(DamageSource.causeThrownDamage(this, entity), 2)) {
                    ;
                }
            }

            if (!this.worldObj.isRemote) {
                this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, true, true);
            }
            this.setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);

        for (this.rotationPitch = (float) ((Math.atan2(this.motionY, f) * 180D) / Math.PI); this.rotationPitch - this.prevRotationPitch
                < -180F; this.prevRotationPitch -= 360F) {}

        for (; this.rotationPitch - this.prevRotationPitch >= 180F; this.prevRotationPitch += 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw < -180F; this.prevRotationYaw -= 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw >= 180F; this.prevRotationYaw += 360F) {}

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f1 = 0.95F;

        if (this.handleWaterMovement()) {
            for (int k = 0; k < 4; k++) {
                float f3 = 0.25F;
                this.worldObj.spawnParticle(
                        "BUBBLE".toLowerCase(),
                        this.posX - this.motionX * f3,
                        this.posY - this.motionY * f3,
                        this.posZ - this.motionZ * f3,
                        this.motionX,
                        this.motionY,
                        this.motionZ);
            }

            f1 = 0.8F;
        }

        this.motionX += this.accelerationX;
        this.motionY += this.accelerationY;
        this.motionZ += this.accelerationZ;
        this.motionX *= f1;
        this.motionY *= f1;
        this.motionZ *= f1;
        this.worldObj.spawnParticle("SMOKE".toLowerCase(), this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.worldObj.spawnParticle("SMOKE".toLowerCase(), this.posX, this.posY + 0.10000000000000001D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.xTile);
        nbttagcompound.setShort("yTile", (short) this.yTile);
        nbttagcompound.setShort("zTile", (short) this.zTile);
        nbttagcompound.setByte("shake", (byte) this.field_9406_a);
        nbttagcompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.field_9406_a = nbttagcompound.getByte("shake") & 0xff;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public float getCollisionBorderSize() {
        return 1.0F;
    }

    public static DamageSource causeLizardFireballDamage(
            CREEPSEntityDesertLizardFireball creepsentitydesertlizardfireball, Entity entity) {
        return (new EntityDamageSourceIndirect("lizardfireball", creepsentitydesertlizardfireball, entity))
                .setFireDamage()
                .setProjectile();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.setBeenAttacked();

        if (entity != null) {
            Vec3 vec3d = entity.getLookVec();

            if (vec3d != null) {
                this.motionX = vec3d.xCoord;
                this.motionY = vec3d.yCoord;
                this.motionZ = vec3d.zCoord;
                this.accelerationX = this.motionX * 0.10000000000000001D;
                this.accelerationY = this.motionY * 0.10000000000000001D;
                this.accelerationZ = this.motionZ * 0.10000000000000001D;
            }

            return true;
        } else
            return false;
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }
}
