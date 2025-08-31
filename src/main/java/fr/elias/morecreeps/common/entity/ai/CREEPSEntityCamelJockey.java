package fr.elias.morecreeps.common.entity.ai;

import fr.elias.morecreeps.common.entity.nice.CREEPSEntityCamel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityCamelJockey extends EntityMob {

  protected double attackrange;
  public boolean bone;
  protected int attack;
  public String texture;
  public float goatsize;

  /** Entity motion Y */
  public double motionY;

  public int eaten;
  public boolean hungry;
  public int hungrytime;
  public int goatlevel;
  public double waterX;
  public double waterY;
  public double waterZ;
  public boolean findwater;
  public int spittimer;
  public float modelsize;
  public double health;
  public double speed;
  public double strength;

  public CREEPSEntityCamelJockey(World world) {
    super(world);
    this.bone = false;
    this.texture = "morecreeps:textures/entity/jockey.png";
    this.setSize(this.width * 0.6F, this.height * 0.6F);
    this.attackrange = 16D;
    this.hungry = false;
    this.findwater = false;
    this.hungrytime = this.rand.nextInt(100) + 10;
    this.goatlevel = 1;
    this.spittimer = 50;
    this.modelsize = 0.6F;
    this.health = 25;
    this.speed = 0.55;
    this.strength = 1;
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.55);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    super.onUpdate();

    if (this.ridingEntity == null) {
      for (int i = 0; i < this.worldObj.loadedEntityList.size(); i++) {
        Entity entity = (Entity) this.worldObj.loadedEntityList.get(i);

        if (((entity instanceof CREEPSEntityCamel) || entity.riddenByEntity == null)
            && (entity instanceof CREEPSEntityCamel)
            && entity.riddenByEntity == null) {
          double d = entity.getDistance(this.posX, this.posY, this.posZ);
          CREEPSEntityCamel creepsentitycamel = (CREEPSEntityCamel) entity;

          if (d < 4D && entity.riddenByEntity == null) {
            this.mountEntity(entity);
          }

          if (!creepsentitycamel.getIsTamed()) {
            creepsentitycamel.interest = 0;
            creepsentitycamel.setTamedName("");
            creepsentitycamel.setIsTamed(false);
          }

          if (d < 16D && creepsentitycamel.canEntityBeSeen(this)) {
            this.attackEntity(creepsentitycamel, 0);
          }
        }
      }
    } else {
      this.rotationYaw = this.ridingEntity.rotationYaw;
    }
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof CREEPSEntityCamel) return this.getYOffset() + 1.5F;
    else return this.getYOffset();
  }

  @Override
  public void updateRiderPosition() {
    this.riddenByEntity.setPosition(
        this.posX,
        this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(),
        this.posZ);
  }

  /** Returns the Y offset from the entity's position for any entity riding this one. */
  @Override
  public double getMountedYOffset() {
    return 0.5D;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (this.handleWaterMovement()) {
      this.motionY = 0.15999999642372131D;
    }
  }

  /**
   * Takes a coordinate in and returns a weight to determine how likely this creature will try to
   * path to the block. Args: x, y, z
   */
  @Override
  public float getBlockPathWeight(int x, int y, int z) {
    if (this.worldObj.getBlock(x, y, z) == Blocks.sand
        || this.worldObj.getBlock(x, y, z) == Blocks.gravel) return 10F;
    else return -(float) y;
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();
    this.hungry = false;

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      /*if (entity != this && worldObj.difficultySetting.getDifficultyId() > 0) {
          this.attackEntityAsMob(entity);
      }

       */

      return true;
    } else return false;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
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

    if (f < 2D
        && entity.boundingBox != null
        && this.boundingBox != null
        && entity.boundingBox.maxY > this.boundingBox.minY
        && entity.boundingBox.minY < this.boundingBox.maxY
        && !(entity instanceof CREEPSEntityCamel)) {
      this.swingItem();
      this.attackEntityAsMob(entity);
    }
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.getBoundingBox().minY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    // Block i1 = worldObj.getBlockState(new BlockPos(getPosition())).getBlock();

    if (j < 50) return super.getCanSpawnHere();
    else
      return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL
          && l > 6
          && super.getCanSpawnHere();
    // return (i1 == Blocks.sand || i1 == Blocks.dirt || i1 == Blocks.gravel) && i1 !=
    // Blocks.cobblestone &&
    // worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&
    // worldObj.checkBlockCollision(getBoundingBox()) && worldObj.canBlockSeeSky(getPosition()) && l
    // > 6;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setBoolean("Hungry", this.hungry);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.hungry = nbttagcompound.getBoolean("Hungry");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
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
              + (0.6F - this.modelsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.ridingEntity != null) return "morecreeps:cameljockeyget";
    else return "morecreeps:cameljockey";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:cameljockeyhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:cameljockeydeath";
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (this.rand.nextInt(10) == 0) {
      this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
    }

    if (this.rand.nextInt(10) == 0) {
      this.dropItem(Items.reeds, this.rand.nextInt(3) + 1);
    }

    super.onDeath(damagesource);
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 2;
  }
}
