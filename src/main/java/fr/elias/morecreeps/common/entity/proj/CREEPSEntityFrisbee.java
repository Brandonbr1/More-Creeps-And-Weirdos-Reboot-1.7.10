package fr.elias.morecreeps.common.entity.proj;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CREEPSEntityFrisbee extends EntityThrowable {

  private int field_20056_b;
  private int field_20055_c;
  private int field_20054_d;
  private int field_20053_e;
  public int field_20057_a;
  // private int field_20050_h; // TODO (unused)
  protected double initialVelocity;
  double bounceFactor;
  public int lifespan;

  public CREEPSEntityFrisbee(World world) {
    super(world);
    this.setSize(0.25F, 0.25F);
    this.initialVelocity = 1.0D;
    this.bounceFactor = 0.14999999999999999D;
    this.lifespan = 120;
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

  public CREEPSEntityFrisbee(World world, Entity entity) {
    this(world);
    this.setRotation(entity.rotationYaw, 0.0F);
    double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
    this.motionX =
        0.59999999999999998D * d * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
    this.motionY =
        -0.69999999999999996D * MathHelper.sin((entity.rotationPitch / 180F) * (float) Math.PI);
    this.motionZ =
        0.59999999999999998D * d1 * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
    this.setPosition(
        entity.posX + d * 0.80000000000000004D,
        entity.posY,
        entity.posZ + d1 * 0.80000000000000004D + (3D * d1 + d));
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
  }

  public CREEPSEntityFrisbee(World world, double d, double d1, double d2) {
    this(world);
    this.setPosition(d, d1, d2);
  }

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
    // this.field_20050_h = 0; // TODO (unused)
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
    double d = this.motionX;
    double d1 = this.motionY;
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    this.moveEntity(this.motionX, this.motionY, this.motionZ);

    if (this.handleWaterMovement()) {
      this.motionY += 0.0087999999523162842D;
      this.motionX *= 0.97999999999999998D;
      this.motionZ *= 0.68000000000000005D;
    }

    if (this.motionX != d) {
      this.motionX = -this.bounceFactor * d;
    }

    if (this.motionY != d1) {
      this.motionY = -this.bounceFactor * d1;
    }

    if (this.motionY != d1) {
      this.motionY = -this.bounceFactor * d1;
    } else if (!this.handleWaterMovement()) {
      this.motionY -= 0.0050000000000000001D;
    }

    this.motionX *= 0.97999999999999998D;
    this.motionY *= 0.999D;
    this.motionZ *= 0.97999999999999998D;

    if (this.isCollidedVertically) {
      this.motionX *= 0.25D;
      this.motionZ *= 0.25D;
    }
    if (this.onGround && this.lifespan-- < 0) {
      if (!this.worldObj.isRemote) {
        this.dropItem(MoreCreepsAndWeirdos.frisbee, 1);
      }
      this.setDead();
    }
  }

  /*
   * public void onCollideWithPlayer(EntityPlayer entityplayer)
   * {
   * if (onGround && field_20051_g == entityplayer && field_20057_a <= 0 && !worldObj.isRemote)
   * {
   * worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) *
   * 2.0F);
   * setDead();
   * entityplayer.onItemPickup(this, 1);
   * entityplayer.inventory.addItemStackToInventory(new ItemStack(MoreCreepsAndWeirdos.frisbee, 1, 0));
   * }
   * }
   */

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    nbttagcompound.setShort("xTile", (short) this.field_20056_b);
    nbttagcompound.setShort("yTile", (short) this.field_20055_c);
    nbttagcompound.setShort("zTile", (short) this.field_20054_d);
    nbttagcompound.setByte("inTile", (byte) this.field_20053_e);
    nbttagcompound.setByte("shake", (byte) this.field_20057_a);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    this.field_20056_b = nbttagcompound.getShort("xTile");
    this.field_20055_c = nbttagcompound.getShort("yTile");
    this.field_20054_d = nbttagcompound.getShort("zTile");
    this.field_20053_e = nbttagcompound.getByte("inTile") & 0xff;
    this.field_20057_a = nbttagcompound.getByte("shake") & 0xff;
  }

  @Override
  protected void onImpact(MovingObjectPosition p_70184_1_) {}
}
