package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityLolliman extends EntityAnimal {

  protected double attackrange;
  protected int attack;
  public int kidcheck;
  public boolean kidmounted;
  public int rockettime;
  public float modelsize;
  public int treats;
  public String texture;

  public CREEPSEntityLolliman(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/lolliman.png";
    this.setSize(0.9F, 3F);
    this.attack = 2;
    this.attackrange = 16D;
    this.kidcheck = 0;
    this.kidmounted = false;
    this.rockettime = 0;
    this.modelsize = 2.0F;
    this.treats = 0;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(4, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
  }

  /**
   * This function is used when two same-species animals in 'love mode' breed to generate the new
   * baby animal.
   */
  @Override
  public EntityAnimal createChild(EntityAgeable entityanimal) {
    return new CREEPSEntityLolliman(this.worldObj);
  }

  @Override
  protected void updateAITasks() {
    if (this.kidcheck++ > 25 && !this.kidmounted) {
      this.kidcheck = 0;
      // List list = null;
      AxisAlignedBB bb = this.boundingBox;
      if (bb == null) return;
      List<?> list =
          this.worldObj.getEntitiesWithinAABBExcludingEntity(this, bb.expand(2D, 2D, 2D));

      for (int k = 0; k < list.size(); k++) {
        Entity entity = (Entity) list.get(k);

        if (!((entity != null) && (entity instanceof CREEPSEntityKid))
            || (entity.ridingEntity == null || !(entity.ridingEntity instanceof EntityPlayer))) {
          continue;
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:lollimantakeoff",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.smallconfetti();

        // Actually not stable
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 4D);
        if (entityplayer instanceof EntityPlayer && entityplayer != null) {
          if (!((EntityPlayerMP) entityplayer)
              .func_147099_x()
              .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievelolliman)) {
            this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            entityplayer.addStat(MoreCreepsAndWeirdos.achievelolliman, 1);
            this.confetti();
          }
        }

        entity.mountEntity(null);
        entity.mountEntity(this);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
        this.motionY = 0.60000002384185791D;
        this.kidmounted = true;
        this.kidcheck = 255;
        return;
      }
    }

    if (this.kidcheck > 250 && this.kidmounted) {
      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.smoke3(this.worldObj, this, this.rand);
      }

      this.motionY = 0.25D;

      if (this.rockettime++ > 5) {
        this.rockettime = 0;

        for (int j = 0; j < this.rand.nextInt(2) + 1; j++) {
          Object obj = null;
          int l = this.rand.nextInt(4);
          this.treats++;

          if (this.treats == 30) {
            this.worldObj.playSoundAtEntity(
                this,
                "morecreeps:lollimanexplode",
                1.0F,
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          }

          if (this.rand.nextInt(3) == 0) {
            EntityItem entityitem;

            switch (l) {
              case 1:
                entityitem = this.entityDropItem(new ItemStack(Items.cookie, 1, 0), 1.0F);
                break;

              case 2:
                entityitem = this.entityDropItem(new ItemStack(Items.cake, 1, 0), 1.0F);
                break;

              case 3:
                entityitem =
                    this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.lolly, 1, 0), 1.0F);
                break;

              default:
                entityitem =
                    this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.lolly, 1, 0), 1.0F);
                break;
            }

            entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
          }

          if (this.posY <= 100D && this.treats <= 55) {
            continue;
          }

          if (this.riddenByEntity != null) {
            this.riddenByEntity.setDead();
          }

          this.setDead();

          if (this.treats > 50) {
            this.worldObj.createExplosion(this, this.posX, this.posY + 2D, this.posZ, 2.5F, true);
          }
        }
      }
    }

    super.updateEntityActionState();
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.ridingEntity != null && this.kidmounted) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  // this function doesn't exist anymore
  /* really?  protected void updateFallState(double p_70064_1_, boolean p_70064_3_) {
   * protected void updateFallState(double d, boolean flag)
   * {
   * if (!kidmounted)
   * {
   * super.updateFallState(d, flag);
   * }
   * }
   */

  /**
   * Takes a coordinate in and returns a weight to determine how likely this creature will try to
   * path to the block. Args: x, y, z
   */
  @Override
  public float getBlockPathWeight(int x, int y, int z) {
    if (this.worldObj.getBlock(x, y, z) == Blocks.water
        || this.worldObj.getBlock(x, y, z) == Blocks.flowing_water) return 10F;
    else return -(float) y;
  }

  @Override
  protected Entity findPlayerToAttack() {
    if (this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
      float f = this.getBrightness(1.0F);

      if (f < 0.0F) {
        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, this.attackrange);

        if (entityplayer != null) return entityplayer;
      }

      if (this.rand.nextInt(10) == 0) {
        EntityLiving entityliving = this.func_21018_getClosestTarget(this, 15D);
        return entityliving;
      }
    }

    return null;
  }

  public EntityLiving func_21018_getClosestTarget(Entity entity, double d) {
    double d1 = -1D;
    EntityLiving entityliving = null;

    for (int i = 0; i < this.worldObj.loadedEntityList.size(); i++) {
      Entity entity1 = (Entity) this.worldObj.loadedEntityList.get(i);

      if (!(entity1 instanceof EntityLiving)
          || entity1 == entity
          || entity1 == entity.riddenByEntity
          || entity1 == entity.ridingEntity
          || (entity1 instanceof EntityPlayer)
          || (entity1 instanceof EntityMob)
          || (entity1 instanceof EntityAnimal)) {
        continue;
      }

      double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

      if ((d < 0.0D || d2 < d * d)
          && (d1 == -1D || d2 < d1)
          && ((EntityLiving) entity1).canEntityBeSeen(entity)) {
        d1 = d2;
        entityliving = (EntityLiving) entity1;
      }
    }

    return entityliving;
  }

  public boolean attackEntityFrom(DamageSource damagesource, int i) {
    Entity entity = damagesource.getEntity();
    this.entityToAttack = entity;
    return super.attackEntityFrom(damagesource, i);
  }

  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.onGround) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX =
          (d / f1)
              * 0.20000000000000001D
              * (0.850000011920929D + this.motionX * 0.20000000298023224D);
      this.motionZ =
          (d1 / f1)
              * 0.20000000000000001D
              * (0.80000001192092896D + this.motionZ * 0.20000000298023224D);
      this.motionY = 0.10000000596246449D;
      this.fallDistance = -25F;
    }

    if (f < 3.1000000000000001D
        && entity.boundingBox.maxY > this.boundingBox.minY
        && entity.boundingBox.minY < this.boundingBox.maxY) {
      this.attackTime = 20;
      entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attack);
    }
  }

  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getFullBlockLightValue(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j - 1, k);
    return (i1 == Blocks.grass || i1 == Blocks.dirt)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(15) == 0
        && l > 7;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("KidCheck", this.kidcheck);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.kidcheck = nbttagcompound.getInteger("KidCheck");
    this.kidmounted = false;

    if (this.riddenByEntity != null) {
      this.riddenByEntity.setDead();
    }
  }

  /** Plays living's sound at its position */
  @Override
  public void playLivingSound() {
    String s = this.getLivingSound();

    if (s != null) {
      this.worldObj.playSoundAtEntity(
          this,
          s,
          this.getSoundVolume(),
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
              + 1.0F
              + (2.0F - this.modelsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (!this.kidmounted) return "morecreeps:lolliman";
    else return null;
  }

  @Override
  public boolean interact(EntityPlayer p_70085_1_) {
    if (super.interact(p_70085_1_)) return true;
    else if (this.worldObj.isRemote
        && (this.riddenByEntity == null || this.riddenByEntity == p_70085_1_)) {
      p_70085_1_.mountEntity(this);
      return true;
    } else return false;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:lollimanhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:lollimandeath";
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (!this.worldObj.isRemote) {
      if (this.rand.nextInt(10) == 0) {
        this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
      }

      if (this.rand.nextInt(10) == 0) {
        this.dropItem(Items.wheat_seeds, this.rand.nextInt(3) + 1);
      }
    }

    super.onDeath(damagesource);
  }

  public void smallconfetti() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.spawnLollimanConfetti(this.worldObj, this, this.rand, 20, 20);
    }
  }

  public void confetti() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.confettiA(this, this.worldObj);
    }
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 2;
  }
}
