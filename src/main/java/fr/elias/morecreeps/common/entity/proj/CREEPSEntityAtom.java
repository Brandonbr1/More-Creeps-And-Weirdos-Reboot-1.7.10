package fr.elias.morecreeps.common.entity.proj;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import java.util.List;
import java.util.Random;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityAtom extends EntityFlying {

  private Random random;
  public static Random rand = new Random();
  protected double attackRange;
  private int angerLevel;
  public boolean jumping;
  public float robotsize;
  public int floattimer;
  public int floatdir;
  public float atomsize;
  public int lifespan;

  public CREEPSEntityAtom(World world) {
    super(world);
    this.renderDistanceWeight = 10D;
    this.angerLevel = 0;
    this.attackRange = 16D;
    this.jumping = false;
    this.floattimer = 0;
    this.floatdir = 1;
    this.atomsize = 1.0F;
    this.lifespan = rand.nextInt(300) + 200;
    this.setSize(0.5F, 0.5F);
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.61D);
  }

  @Override
  public float getShadowSize() {
    return 0.0F;
  }

  /**
   * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for
   * spiders and wolves to prevent them from trampling crops
   */
  @Override
  protected boolean canTriggerWalking() {
    return false;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  /*
   * public boolean getCanSpawnHere()
   * {
   * int i = MathHelper.floor_double(posX);
   * int j = MathHelper.floor_double(posY);
   * int k = MathHelper.floor_double(posZ);
   * int l = worldObj.getFullBlockLightValue(i, j, k);
   * int i1 = worldObj.getBlockId(i, j - 1, k);
   * return i1 != Block.cobblestone.blockID && i1 != Block.wood.blockID && i1 != Block.stairDouble.blockID && i1 !=
   * Block.stairSingle.blockID && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 &&
   * worldObj.checkIfAABBIsClear(boundingBox) && worldObj.canBlockSeeTheSky(i, j, k) && rand.nextInt(10) == 0 && l >
   * 8;
   * }
   */

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    int i = (int) this.posX;
    int j = (int) this.posY;
    int k = (int) this.posZ;

    for (int l = 0; l < this.atomsize; l++) {
      double d = i + this.worldObj.rand.nextFloat();
      double d1 = j + this.worldObj.rand.nextFloat();
      double d2 = k + this.worldObj.rand.nextFloat();
      double d3 = d - this.posX;
      double d4 = d1 - this.posY;
      double d5 = d2 - this.posZ;
      double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
      d3 /= d6;
      d4 /= d6;
      d5 /= d6;
      double d7 = 0.5D / (d6 / this.atomsize + 0.10000000000000001D);
      d7 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F;
      d3 *= d7;
      d4 *= d7;
      d5 *= d7;
      this.worldObj.spawnParticle(
          "portal",
          (d + this.posX * 1.0D) / 2D,
          (d1 + this.posY * 1.0D) / 2D + (int) (this.atomsize / 4F),
          (d2 + this.posZ * 1.0D) / 2D,
          d3,
          d4,
          d5);
    }

    if (rand.nextInt(6) == 0) {
      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.foam2(this.worldObj, this);
      }
    }

    if (this.lifespan-- == 0) {
      if (!this.worldObj.isRemote) {
        this.worldObj.createExplosion(
            this, this.posX, this.posY + this.atomsize, this.posZ, 1.0F, true);
      }
      this.setDead();
    }

    // TODO: NOTE, THIS IS HOW THIS ERROR IS FIXED
    //  AxisAlignedBB bb = this.getBoundingBox();
    AxisAlignedBB bb = this.boundingBox;

    if (bb == null) return;

    List<?> list =
        this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this, bb.expand(this.atomsize * 2.0F, this.atomsize * 2.0F, this.atomsize * 2.0F));

    for (int j1 = 0; j1 < list.size(); j1++) {
      Entity entity = (Entity) list.get(j1);
      float f = this.getDistanceToEntity(entity);

      if ((entity instanceof CREEPSEntityAtom) && f < 4F) {
        this.worldObj.playSoundAtEntity(
            this, "morecreeps:atomsuck", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        this.lifespan += ((CREEPSEntityAtom) entity).lifespan;
        entity.setDead();
        this.atomsize += ((CREEPSEntityAtom) entity).atomsize - 0.5F;
        this.setSize(this.atomsize * 0.6F, this.atomsize * 0.6F);
        continue;
      }

      if (!(entity instanceof EntityCreature) && !(entity instanceof EntityItem)) {
        continue;
      }

      if (entity instanceof EntityLivingBase) {
        double targetMoveSpeed =
            ((EntityLivingBase) entity)
                .getAttributeMap()
                .getAttributeInstance(SharedMonsterAttributes.movementSpeed)
                .getAttributeValue();
        targetMoveSpeed *= 0.8D;
      }

      float f1 = (0.15F / f) * this.atomsize;

      if (entity instanceof CREEPSEntityAtom) {
        f1++;
      }

      if (entity.posX > this.posX) {
        entity.motionX -= f1;
      }

      if (entity.posX < this.posX) {
        entity.motionX += f1;
      }

      if (entity.posY > this.posY) {
        entity.motionY -= f1;
      }

      if (entity.posY < this.posY) {
        entity.motionY += f1;
      }

      if (entity.posZ > this.posZ) {
        entity.motionZ -= f1;
      }

      if (entity.posZ < this.posZ) {
        entity.motionZ += f1;
      }

      if (rand.nextInt(50) == 0) {
        this.worldObj.playSoundAtEntity(
            this, "morecreeps:atomblow", 1.0F, (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        int k1 = (int) (this.atomsize / 3F) + 1;
        entity.motionX += rand.nextInt(k1);
        entity.motionY += rand.nextInt(k1);
        entity.motionZ += rand.nextInt(k1);
      }
    }
    super.onLivingUpdate();
    if (rand.nextInt(70) == 0) {
      this.motionX += rand.nextFloat() * 1.0F - 0.5F;
    }

    if (rand.nextInt(50) == 0) {
      this.motionY += rand.nextFloat() * 2.0F - 0.5F;
    }

    if (rand.nextInt(70) == 0) {
      this.motionZ += rand.nextFloat() * 1.0F - 0.5F;
    }

    if (rand.nextInt(10) == 0) {
      this.motionY = -(this.atomsize * 0.015F);
    }
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    return "morecreeps:atom";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:atomhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:atomdead";
  }

  public void onDeath() {
    Item[] dropItems =
        (new Item[] {
          Item.getItemFromBlock(Blocks.cobblestone),
          Item.getItemFromBlock(Blocks.gravel),
          Item.getItemFromBlock(Blocks.cobblestone),
          Item.getItemFromBlock(Blocks.gravel),
          Item.getItemFromBlock(Blocks.iron_ore),
          Item.getItemFromBlock(Blocks.mossy_cobblestone)
        });

    // Selects random item by getting a random number, itemchooser, and applying it to item array.
    Random rand = new Random();
    int itemchooser = rand.nextInt(6) + 1;

    this.dropItem(dropItems[itemchooser], (int) this.getYOffset());
  }
}
