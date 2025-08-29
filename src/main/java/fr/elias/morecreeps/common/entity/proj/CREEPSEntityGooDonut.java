package fr.elias.morecreeps.common.entity.proj;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CREEPSEntityGooDonut extends EntityThrowable {

  protected double initialVelocity;
  double bounceFactor;
  int fuse;
  boolean exploded;

  public CREEPSEntityGooDonut(World world) {
    super(world);
    this.setSize(0.25F, 0.25F);
    this.initialVelocity = 1.0D;
    this.bounceFactor = 0.84999999999999998D;
    this.exploded = false;
    this.fuse = 30;
  }

  /**
   * Checks if the entity is in range to render by using the past in distance and comparing it to
   * its average edge length * 64 * renderDistanceWeight Args: distance
   */
  @Override
  public boolean isInRangeToRenderDist(double d) {
    double d1 = this.boundingBox.getAverageEdgeLength() * 4D;
    d1 *= 64D;
    return d < d1 * d1;
  }

  public CREEPSEntityGooDonut(World world, Entity entity) {
    this(world);
    this.setRotation(entity.rotationYaw, 0.0F);
    double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
    this.motionX =
        0.69999999999999996D * d * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
    this.motionY =
        -0.80000000000000004D * MathHelper.sin((entity.rotationPitch / 180F) * (float) Math.PI);
    this.motionZ =
        0.69999999999999996D * d1 * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
    this.setPosition(
        entity.posX + d * 0.80000000000000004D,
        entity.posY,
        entity.posZ + d1 * 0.80000000000000004D);
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.fuse = 30;
  }

  public CREEPSEntityGooDonut(World world, double d, double d1, double d2) {
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

  /** Sets the velocity to the args. Args: x, y, z */
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

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    if (this.worldObj == null || this.isDead) return;

    double d = this.motionX;
    double d1 = this.motionY;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.moveEntity(this.motionX, this.motionY, this.motionZ);

    if (this.motionX != d) {
      this.motionX = -this.bounceFactor * d;
    }

    if (this.motionY != d1) {
      this.motionY = -this.bounceFactor * d1;
    } else {
      this.motionY -= 0.04D;
    }

    this.motionX *= 0.98D;
    this.motionY *= 0.995D;
    this.motionZ *= 0.98D;

    if (this.fuse-- <= 0) {
      this.explode();
    }

    if (this.handleWaterMovement()) {
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 10; j++) {
          float f = 0.85F;
          this.worldObj.spawnParticle(
              "BUBBLE",
              this.posX - this.motionX - 0.25D * f,
              this.posY - this.motionY - 0.25D * f,
              this.posZ - this.motionZ - 0.25D * f,
              this.motionX,
              this.motionY,
              this.motionZ);
        }
      }

      this.setDead();
      if (!this.worldObj.isRemote) {
        this.dropItem(MoreCreepsAndWeirdos.goodonut, 1);
      }
    }
    this.worldObj.getEntitiesWithinAABBExcludingEntity(
        this,
        this.boundingBox
            .addCoord(this.motionX, this.motionY, this.motionZ)
            .expand(1.0D, 1.0D, 1.0D));
  }

  private void explode() {
    if (!this.exploded) {
      this.exploded = true;
      if (!this.worldObj.isRemote) {
        this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 3.85F, true);
      }
      this.setDead();
    }
  }

  @Override
  protected void onImpact(MovingObjectPosition p_70184_1_) {
    // TODO Auto-generated method stub
  }
}
