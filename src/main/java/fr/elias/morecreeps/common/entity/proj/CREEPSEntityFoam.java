package fr.elias.morecreeps.common.entity.proj;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class CREEPSEntityFoam extends Entity implements IProjectile {

  protected int hitX;
  protected int hitY;
  protected int hitZ;
  protected Block blockHit;

  /** Light value one block more in z axis */
  protected boolean aoLightValueZPos;

  /** Light value of the block itself */
  public EntityLiving lightValueOwn;

  /** Used as a scratch variable for ambient occlusion on the north/bottom/west corner. */
  protected int aoLightValueScratchXYZNNP;

  /**
   * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
   */
  protected int aoLightValueScratchXYNN;

  protected int damage;

  /** Used as a scratch variable for ambient occlusion on the north/bottom/east corner. */
  protected boolean aoLightValueScratchXYZNNN;

  protected boolean playerFire;
  public Entity shootingEntity;

  public CREEPSEntityFoam(World world) {
    super(world);
    this.hitX = -1;
    this.hitY = -1;
    this.hitZ = -1;
    this.blockHit = Blocks.air;
    this.aoLightValueZPos = false;
    this.aoLightValueScratchXYNN = 0;
    this.setSize(0.00325F, 0.001125F);
    this.playerFire = false;
  }

  public CREEPSEntityFoam(World world, double d, double d1, double d2) {
    this(world);
    this.setPosition(d, d1, d2);
    this.aoLightValueScratchXYZNNN = true;
  }

  public CREEPSEntityFoam(World world, EntityLivingBase entityliving, float f) {
    this(world);
    this.shootingEntity = entityliving;
    this.damage = 1;
    double d = -MathHelper.sin((entityliving.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entityliving.rotationYaw * (float) Math.PI) / 180F);
    this.setLocationAndAngles(
        entityliving.posX + d * 0.40000000596046448D,
        (entityliving.posY + entityliving.getEyeHeight()) - 0.25D,
        entityliving.posZ + d1 * 0.80000001192092896D,
        entityliving.rotationYaw,
        entityliving.rotationPitch);
    this.posX -= MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;
    this.posY -= 0.10000000149011612D;
    this.posZ -= MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;

    if (entityliving instanceof EntityPlayer) {
      this.posY += 0.20000000298023224D;
    }

    this.setPosition(this.posX, this.posY, this.posZ);
    this.motionX =
        -MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI)
            * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
    this.motionZ =
        MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI)
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

    if (entityliving.onGround) {;
    }

    this.setThrowableHeading(
        this.motionX,
        this.motionY,
        this.motionZ,
        (float) (2.380000114440918D + (this.worldObj.rand.nextFloat() - 0.5D)),
        f1);
  }

  /** Called by a player entity when they collide with an entity */
  @Override
  public void onCollideWithPlayer(EntityPlayer entityplayer) {}

  @Override
  protected void entityInit() {}

  @Override
  public void setThrowableHeading(double d, double d1, double d2, float f, float f1) {
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
   * Checks if the entity is in range to render by using the past in distance and comparing it to
   * its average edge length * 64 * renderDistanceWeight Args: distance
   */
  @Override
  public boolean isInRangeToRenderDist(double d) {
    return true;
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    super.onUpdate();

    for (int i = 0; i < 15; i++) {
      double d = this.rand.nextGaussian() * 0.10000000000000001D;
      double d1 = this.rand.nextGaussian() * 0.10000000000000001D;
      double d2 = this.rand.nextGaussian() * 0.10000000000000001D;
      this.worldObj.spawnParticle(
          "EXPLODE".toLowerCase(),
          this.posX + this.rand.nextFloat() * 2.0F,
          this.posY,
          this.posZ + this.rand.nextFloat() * 2.0F,
          d,
          d1,
          d2);
    }

    if (this.aoLightValueScratchXYNN == 100) {
      this.setDead();
    }

    if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
      float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.prevRotationYaw =
          this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);
      this.prevRotationPitch =
          this.rotationPitch = (float) ((Math.atan2(this.motionY, f) * 180D) / Math.PI);
    }

    if (this.aoLightValueZPos) {
      Block j = this.worldObj.getBlock(this.hitX, this.hitY, this.hitZ);

      if (j != this.blockHit) {
        this.aoLightValueZPos = false;
        this.motionX *= this.rand.nextFloat() * 0.2F;
        this.motionY *= this.rand.nextFloat() * 0.2F;
        this.motionZ *= this.rand.nextFloat() * 0.2F;
        this.aoLightValueScratchXYZNNP = 0;
        this.aoLightValueScratchXYNN = 0;
      } else {
        this.aoLightValueScratchXYZNNP++;

        if (this.aoLightValueScratchXYZNNP == 100) {
          this.setDead();
        }

        return;
      }
    } else {
      this.aoLightValueScratchXYNN++;
    }

    Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
    Vec3 vec3d1 =
        Vec3.createVectorHelper(
            this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
    MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
    vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
    vec3d1 =
        Vec3.createVectorHelper(
            this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

    if (movingobjectposition != null) {
      vec3d1 =
          Vec3.createVectorHelper(
              movingobjectposition.hitVec.xCoord,
              movingobjectposition.hitVec.yCoord,
              movingobjectposition.hitVec.zCoord);
    }

    Entity entity = null;
    List<?> list =
        this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            this.boundingBox
                .addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(1.0D, 1.0D, 1.0D));
    double d3 = 0.0D;

    for (int k = 0; k < list.size(); k++) {
      Entity entity1 = (Entity) list.get(k);

      if (!entity1.canBeCollidedWith()
          || (entity1 == this.shootingEntity
                  || this.shootingEntity != null && entity1 == this.shootingEntity.ridingEntity)
              && this.aoLightValueScratchXYNN < 5
          || this.aoLightValueScratchXYZNNN) {
        if (this.motionZ != 0.0D || !((this.motionX == 0.0D) & (this.motionY == 0.0D))) {
          continue;
        }

        this.setDead();
        break;
      }

      float f4 = 0.3F;
      AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
      MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

      if (movingobjectposition1 == null) {
        continue;
      }

      double d4 = vec3d.distanceTo(movingobjectposition1.hitVec);

      if (d4 < d3 || d3 == 0.0D) {
        entity = entity1;
        d3 = d4;
      }
    }

    if (entity != null) {
      movingobjectposition = new MovingObjectPosition(entity);
    }

    if (movingobjectposition != null) {
      if (movingobjectposition.entityHit != null) {
        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
          this.splash();
          movingobjectposition.entityHit.setFire(0);

          movingobjectposition.entityHit.attackEntityFrom(
              DamageSource.causeThrownDamage(this, entity), 0);

          this.setDead();
        } else {
          this.setDead();
        }
      } else {
        this.hitX = movingobjectposition.blockX;
        this.hitY = movingobjectposition.blockY;
        this.hitZ = movingobjectposition.blockZ;
        this.blockHit = this.worldObj.getBlock(this.hitX, this.hitY, this.hitZ);
        this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
        this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
        this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
        float f1 =
            MathHelper.sqrt_double(
                this.motionX * this.motionX
                    + this.motionY * this.motionY
                    + this.motionZ * this.motionZ);
        this.posX -= (this.motionX / f1) * 0.05000000074505806D;
        this.posY -= (this.motionY / f1) * 0.05000000074505806D;
        this.posZ -= (this.motionZ / f1) * 0.05000000074505806D;
        this.aoLightValueZPos = true;

        for (int l = -2; l < 2; l++) {
          for (int i1 = -2; i1 < 2; i1++) {
            for (int j1 = -2; j1 < 2; j1++) {
              if (this.worldObj.getBlock(this.hitX + l, this.hitY + j1, this.hitZ + i1)
                  == Blocks.fire) {
                this.worldObj.setBlockToAir(this.hitX + l, this.hitY + j1, this.hitZ + i1);
              }
            }
          }
        }

        this.setDead();
      }

      this.worldObj.playSoundAtEntity(
          this, "morecreeps:raygun", 0.2F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
      this.setDead();
    }

    this.posX += this.motionX;
    this.posY += this.motionY;
    this.posZ += this.motionZ;
    float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
    this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);

    for (this.rotationPitch = (float) ((Math.atan2(this.motionY, f2) * 180D) / Math.PI);
        this.rotationPitch - this.prevRotationPitch < -180F;
        this.prevRotationPitch -= 360F) {}

    for (; this.rotationPitch - this.prevRotationPitch >= 180F; this.prevRotationPitch += 360F) {}

    for (; this.rotationYaw - this.prevRotationYaw < -180F; this.prevRotationYaw -= 360F) {}

    for (; this.rotationYaw - this.prevRotationYaw >= 180F; this.prevRotationYaw += 360F) {}

    this.rotationPitch =
        this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
    this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    float f3 = 0.99F;

    if (this.handleWaterMovement()) {
      for (int k1 = 0; k1 < 4; k1++) {
        float f7 = 0.25F;
        this.worldObj.spawnParticle(
            "BUBBLE".toLowerCase(),
            this.posX - this.motionX * f7,
            this.posY - this.motionY * f7,
            this.posZ - this.motionZ * f7,
            this.motionX,
            this.motionY,
            this.motionZ);
      }

      f3 = 0.8F;
      this.setDead();
    }

    this.motionX *= f3;
    this.motionZ *= f3;
    this.setPosition(this.posX, this.posY, this.posZ);
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    nbttagcompound.setShort("xTile", (short) this.hitX);
    nbttagcompound.setShort("yTile", (short) this.hitY);
    nbttagcompound.setShort("zTile", (short) this.hitZ);
    nbttagcompound.setByte("inGround", (byte) (this.aoLightValueZPos ? 1 : 0));
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    this.hitX = nbttagcompound.getShort("xTile");
    this.hitY = nbttagcompound.getShort("yTile");
    this.hitZ = nbttagcompound.getShort("zTile");
    this.aoLightValueZPos = nbttagcompound.getByte("inGround") == 1;
  }

  public static DamageSource causeFoamDamage(CREEPSEntityFoam creepsentityfoam, Entity entity) {
    return (new EntityDamageSourceIndirect("foam", creepsentityfoam, entity))
        .setFireDamage()
        .setProjectile();
  }

  public void blast() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.smoke2(this.worldObj, this, this.rand);
    }
  }

  public void splash() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.blood(this.worldObj, this.posX, this.posY, this.posZ, true);
    }
  }

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    this.blast();
    super.setDead();
    this.shootingEntity = null;
  }
}
