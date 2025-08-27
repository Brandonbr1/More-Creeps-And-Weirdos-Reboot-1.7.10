package fr.elias.morecreeps.common.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityRocket extends Entity {

    protected int hitX;
    protected int hitY;
    protected int hitZ;
    // Don't know how to fix; not in 1.7.10
    // protected IBlockState blockHit;
    // Will this work?
    // world.checkCollision(....);

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/west corner.
     */
    protected int aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
     */
    protected int aoLightValueScratchXYNN;
    protected int damage;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/east corner.
     */
    protected boolean aoLightValueScratchXYZNNN;
    protected boolean playerFire;
    protected Entity playerToAttack;
    public boolean playerAttack;
    public int explodedelay;
    public EntityLivingBase owner;
    public Entity shootingEntity;

    public CREEPSEntityRocket(World world) {
        super(world);
        this.hitX = -1;
        this.hitY = -1;
        this.hitZ = -1;
        // Don't know how to fix; not in 1.7.10
        // blockHit = false;
        this.aoLightValueZPos = false;
        this.aoLightValueScratchXYNN = 0;
        this.setSize(0.325F, 0.1425F);
        this.playerFire = false;
        this.explodedelay = 30;
    }

    public CREEPSEntityRocket(World world, double d, double d1, double d2) {
        this(world);
        this.setPosition(d, d1, d2);
        this.aoLightValueScratchXYZNNN = true;
    }

    public CREEPSEntityRocket(World world, EntityLivingBase entityliving, float f) {
        this(world);
        this.shootingEntity = entityliving;
        this.owner = entityliving;
        this.damage = 15;
        this.setLocationAndAngles(
                entityliving.posX,
                entityliving.posY + entityliving.getEyeHeight(),
                entityliving.posZ,
                entityliving.rotationYaw,
                entityliving.rotationPitch);
        this.posX -= MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;
        this.posY += 0.10000000149011612D;
        this.posZ -= MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI) * 0.66F;

        if (entityliving instanceof EntityPlayer) {
            this.posY -= 1.3999999761581421D;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        this.motionZ = MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        this.motionY = -MathHelper.sin((this.rotationPitch / 180F) * (float) Math.PI);
        float f1 = 1.0F;

        if (entityliving instanceof EntityPlayer) {
            this.playerFire = true;
            float f2 = 0.3333333F;
            float f3 = f2 / 0.1F;

            if (f3 > 0.0F) {
                f1 = (float) (f1 * (1.0D + 2D / f3));
            }
        }

        if (Math.abs(entityliving.motionX) > 0.10000000000000001D
                || Math.abs(entityliving.motionY) > 0.10000000000000001D
                || Math.abs(entityliving.motionZ) > 0.10000000000000001D) {
            f1 *= 2.0F;
        }

        if (entityliving.onGround) {
            ;
        }

        this.func_22374_a(
                this.motionX,
                this.motionY,
                this.motionZ,
                (float) (1.1499999761581421D + (this.worldObj.rand.nextFloat() - 0.5D)),
                f1);
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    @Override
    protected void entityInit() {}

    public void func_22374_a(double d, double d1, double d2, float f, float f1) {
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
        this.aoLightValueScratchXYZNNP = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    public boolean isInRangeToRenderDist(double d) {
        return true;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    @Override
    public boolean canBeCollidedWith() {
        return this.explodedelay <= 0;
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.explodedelay > 0) {
            this.explodedelay--;
        }
        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.rocketSmoke(this.worldObj, this, MoreCreepsAndWeirdos.partWhite); // or partBlack ?
        }
        double d = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(
                EnumParticleTypes.SMOKE_NORMAL,
                (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                this.posY + 0.5D + this.rand.nextFloat() * this.height,
                (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                d,
                d1,
                d2);

        if (this.aoLightValueScratchXYNN == 100) {
            this.setDead();
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(this.motionY, f) * 180D) / Math.PI);
        }

        if (this.aoLightValueZPos) {

            // TODO Check if works
            if (this.worldObj.checkBlockCollision(this.boundingBox)) {
                this.aoLightValueZPos = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.aoLightValueScratchXYZNNP = 0;
                this.aoLightValueScratchXYNN = 0;
            }

            this.aoLightValueScratchXYZNNP++;

            if (this.aoLightValueScratchXYZNNP == 100) {
                this.setDead();
            }

            return;

        } else {
            this.aoLightValueScratchXYNN++;
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
        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                this.getBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(1.0D, 1.0D, 1.0D));
        double d3 = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.canBeCollidedWith()
                    || (entity1 == this.shootingEntity || this.shootingEntity != null && entity1 == this.shootingEntity.ridingEntity)
                    && this.aoLightValueScratchXYNN < 5
                    || this.aoLightValueScratchXYZNNN) {
                if (this.motionZ != 0.0D || !((this.motionX == 0.0D) & (this.motionY == 0.0D))) {
                    continue;
                }

                this.setDead();
                break;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.getBoundingBox()
                    .expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null) {
                if (!this.worldObj.isRemote) {
                    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.0F, true);
                }
                this.checkSplashDamage();
                this.setDead();
                continue;
            }

            double d4 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d4 < d3 || d3 == 0.0D) {
                if (!this.worldObj.isRemote) {
                    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.0F, true);
                }
                this.checkSplashDamage();
                entity = entity1;
                d3 = d4;
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null) {
                if (movingobjectposition.entityHit instanceof EntityPlayer) {
                    int k = this.damage;

                    if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
                        k = 0;
                    }

                    if (this.worldObj.difficultySetting == EnumDifficulty.EASY) {
                        k = k / 3 + 1;
                    }

                    if (this.worldObj.difficultySetting == EnumDifficulty.HARD) {
                        k = (k * 3) / 2;
                    }
                }

                if ((movingobjectposition.entityHit instanceof EntityLiving)
                        && !(movingobjectposition.entityHit instanceof CREEPSEntityRocketGiraffe)) {
                    if (movingobjectposition.entityHit
                            .attackEntityFrom(DamageSource.causeThrownDamage(this, entity), this.damage)) {
                        ;
                    }

                    this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:rocketexplode",
                            1.0F,
                            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    if (!this.worldObj.isRemote) {
                        this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.5F, true);
                    }
                    this.setDead();
                } else {
                    this.setDead();
                }
            } else {
                this.hitX = movingobjectposition.blockX;
                this.hitY = movingobjectposition.blockY;
                this.hitZ = movingobjectposition.blockZ;
                // blockHit = worldObj.getBlockState(new BlockPos(hitX, hitY, hitZ));
                this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
                this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
                this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
                float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= (this.motionX / f1) * 0.05000000074505806D;
                this.posY -= (this.motionY / f1) * 0.05000000074505806D;
                this.posZ -= (this.motionZ / f1) * 0.05000000074505806D;
                this.aoLightValueZPos = true;
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:rocketexplode",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                if (!this.worldObj.isRemote) {
                    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.5F, true);
                }
                this.checkSplashDamage();
                this.setDead();
            }

            this.worldObj.playSoundAtEntity(this, "morecreeps:raygun", 0.2F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
            this.setDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);

        for (this.rotationPitch = (float) ((Math.atan2(this.motionY, f2) * 180D) / Math.PI); this.rotationPitch - this.prevRotationPitch
                < -180F; this.prevRotationPitch -= 360F) {}

        for (; this.rotationPitch - this.prevRotationPitch >= 180F; this.prevRotationPitch += 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw < -180F; this.prevRotationYaw -= 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw >= 180F; this.prevRotationYaw += 360F) {}

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f3 = 0.99F;
        float f5 = 0.0F;

        if (this.handleWaterMovement()) {
            for (int l = 0; l < 4; l++) {
                float f7 = 0.25F;
                this.worldObj.spawnParticle(
                        EnumParticleTypes.WATER_BUBBLE,
                        this.posX - this.motionX * f7,
                        this.posY - this.motionY * f7,
                        this.posZ - this.motionZ * f7,
                        this.motionX,
                        this.motionY,
                        this.motionZ);
            }

            f3 = 0.8F;
            float f6 = 0.03F;
            this.setDead();
        }

        this.motionX *= f3;
        this.motionZ *= f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    public void checkSplashDamage() {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 50D);
        List list = null;
        list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(5D, 5D, 5D));

        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);

            if (!((entity != null) & (entity instanceof EntityCreature))
                    || (entity instanceof CREEPSEntityRocketGiraffe)) {
                continue;
            }

            if (entityplayer != null && entity != null) {
                ((EntityCreature) entity).setRevengeTarget(entityplayer);
            }

            if (entity != null) {
                this.playerAttack = true;
                entity.velocityChanged = true;
                entity.motionY += 0.20000000298023224D;
                entity.attackEntityFrom(DamageSource.generic, 10);

                if (entityplayer != null) {
                    if (((EntityLivingBase) entity).getHealth() <= 0 && !entity.isDead
                            && !((EntityPlayerMP) entityplayer).func_147099_x()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieverocketrampage)
                            && MoreCreepsAndWeirdos.rocketcount >= 50) {
                        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
                        entityplayer.addStat(MoreCreepsAndWeirdos.achieverocketrampage, 1);
                        this.confetti(entityplayer);
                    }
                }

            }
        }
    }

    public void goBoom() {
        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.rocketGoBoom(this.worldObj, this, this.rand);
        }
    }

    public void confetti(EntityPlayer player) {
        double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
        CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
        creepsentitytrophy.setLocationAndAngles(
                player.posX + d * 3D,
                player.posY - 2D,
                player.posZ + d1 * 3D,
                player.rotationYaw,
                0.0F);
        this.worldObj.spawnEntityInWorld(creepsentitytrophy);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.hitX);
        nbttagcompound.setShort("yTile", (short) this.hitY);
        nbttagcompound.setShort("zTile", (short) this.hitZ);
        nbttagcompound.setByte("inGround", (byte) (this.aoLightValueZPos ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.hitX = nbttagcompound.getShort("xTile");
        this.hitY = nbttagcompound.getShort("yTile");
        this.hitZ = nbttagcompound.getShort("zTile");
        this.aoLightValueZPos = nbttagcompound.getByte("inGround") == 1;
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead() {
        super.setDead();
        this.goBoom();
        this.shootingEntity = null;
    }
}
