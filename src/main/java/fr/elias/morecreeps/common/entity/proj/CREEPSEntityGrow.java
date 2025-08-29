package fr.elias.morecreeps.common.entity.proj;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class CREEPSEntityGrow extends EntityThrowable {

  public int hurtTime;
  public int maxHurtTime;
  protected int hitX;
  protected int hitY;
  protected int hitZ;
  protected Block blockHit;

  /** Light value one block more in z axis */
  protected boolean aoLightValueZPos;

  /** Light value of the block itself */
  public EntityLivingBase lightValueOwn;

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
  protected float shrinksize;
  protected int vibrate;
  public EntityPlayer playerOwnTheProjectile;

  public CREEPSEntityGrow(World world) {
    super(world);
    this.hitX = -1;
    this.hitY = -1;
    this.hitZ = -1;
    this.blockHit = Blocks.air;
    this.aoLightValueZPos = false;
    this.aoLightValueScratchXYNN = 0;
    this.setSize(0.0325F, 0.01125F);
    this.playerFire = false;
    this.shrinksize = 1.0F;
    this.vibrate = 1;
  }

  public CREEPSEntityGrow(World world, double d, double d1, double d2) {
    this(world);
    this.setPosition(d, d1, d2);
    this.aoLightValueScratchXYZNNN = true;
  }

  public CREEPSEntityGrow(World world, EntityLivingBase entityliving, float f) {
    this(world);
    this.playerOwnTheProjectile = (EntityPlayer) entityliving;
    this.lightValueOwn = entityliving;
    this.damage = 4;
    this.setLocationAndAngles(
        entityliving.posX,
        entityliving.posY + entityliving.getEyeHeight(),
        entityliving.posZ,
        entityliving.rotationYaw,
        entityliving.rotationPitch);
    this.posX -= MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;
    this.posY += 0.20000000149011612D;
    this.posZ -= MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;

    if (entityliving instanceof EntityPlayer) {
      this.posY -= 0.40000000596046448D;
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
        (float) (2.5D + (this.worldObj.rand.nextFloat() - 0.5D)),
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
  @SideOnly(Side.CLIENT)
  public boolean isInRangeToRenderDist(double d) {
    return true;
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    super.onUpdate();
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

  public void blast() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.shrinkParticle(this.worldObj, this);
    }
  }

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    this.blast();
    super.setDead();
    this.lightValueOwn = null;
  }

  private void smoke() {
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 7; i++) {
        for (int j = 0; j < 4; j++) {
          for (int k = 0; k < 5; k++) {
            double d = this.rand.nextGaussian() * 0.12D;
            double d1 = this.rand.nextGaussian() * 0.12D;
            double d2 = this.rand.nextGaussian() * 0.12D;
            this.worldObj.spawnParticle(
                "EXPLODE".toLowerCase(),
                (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                this.posY + this.rand.nextFloat() * this.height,
                (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                d,
                d1,
                d2);
          }
        }
      }
    }
  }

  @Override
  protected void onImpact(MovingObjectPosition p_70184_1_) {
    this.setDead();
  }
}
