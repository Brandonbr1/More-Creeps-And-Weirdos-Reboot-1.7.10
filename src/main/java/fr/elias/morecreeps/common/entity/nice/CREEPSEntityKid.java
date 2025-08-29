package fr.elias.morecreeps.common.entity.nice;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityKid extends EntityAnimal {

  protected final double attackrange;
  protected int attack;
  public float modelsize;
  public int checktimer;
  public final String texture;

  public CREEPSEntityKid(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/kid.png";
    this.attack = 1;
    this.attackrange = 16D;
    this.checktimer = 0;
    this.modelsize = 0.6F;
    this.setEntitySize(this.width * this.modelsize, this.height * this.modelsize);
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new CREEPSEntityKid.AIAttackEntity());
    this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(4, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
  }

  public void setEntitySize(float width, float height) {
    this.setSize(width, height);
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.55D);
  }

  @Override
  public float getShadowSize() {
    return 0.6F - this.modelsize;
  }

  /** Checks if this entity is inside an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.ridingEntity != null) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof EntityPlayer) {
      float f = 0.6F - this.modelsize;
      return (double) -1.6F + f;
    }

    if (this.ridingEntity instanceof CREEPSEntityLolliman)
      return (2.6F + (0.6F - this.modelsize))
          - (2.0F - ((CREEPSEntityLolliman) this.ridingEntity).modelsize) * 2.75F;
    else return 1.0;
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
    return 1.75D;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.worldObj == null) return;
    if (this.modelsize > 1.0F) {
      this.ignoreFrustumCheck = true;
    }
    if (this.handleWaterMovement()) {
      this.motionY = 0.15999999642372131D;
    }
    if (this.boundingBox != null) {
      AxisAlignedBB aabb = this.boundingBox.expand(8D, 8D, 8D);
      for (Object object : this.worldObj.getEntitiesWithinAABB(Entity.class, aabb)) {
        Entity entity = (Entity) object;
        if (entity instanceof CREEPSEntityLolliman) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:kidfind",
              1.0F,
              (this.rand.nextFloat()) * 0.2F + 1.0F + (0.6F - this.modelsize) * 2.0F);
        }
      }
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

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    if (!(this.getAttackTarget() instanceof EntityPlayer)) {
      Entity obj = entityplayer;

      if (this.ridingEntity != obj) {
        this.rotationYaw = obj.rotationYaw;
        this.mountEntity(obj);
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:kidup",
            1.0F,
            (this.rand.nextFloat()) * 0.2F + 1.0F + (0.6F - this.modelsize) * 2.0F);
      } else {
        obj = obj.riddenByEntity;
        this.rotationYaw = obj.rotationYaw;
        obj.fallDistance = -25F;
        obj.mountEntity(null);
        double d = -MathHelper.sin((entityplayer.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((entityplayer.rotationYaw * (float) Math.PI) / 180F);
        obj.motionX = d * 0.60000002384185791D;
        obj.motionZ = d1 * 0.60000002384185791D;
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:kiddown",
            1.0F,
            (this.rand.nextFloat()) * 0.2F + 1.0F + (0.6F - this.modelsize) * 2.0F);
      }
    } else {
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:kidnontpickup",
          1.0F,
          (this.rand.nextFloat()) * 0.2F + 1.0F + (0.6F - this.modelsize) * 2.0F);
    }

    return true;
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();
    this.setRevengeTarget((EntityLivingBase) entity);
    return super.attackEntityFrom(damagesource, i);
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
  }

  class AIAttackEntity extends EntityAIBase {

    @Override
    public boolean shouldExecute() {
      return CREEPSEntityKid.this.getAttackTarget() != null;
    }

    @Override
    public void updateTask() {
      if (CREEPSEntityKid.this.getAttackTarget() != null) {
        float f = CREEPSEntityKid.this.getDistanceToEntity(CREEPSEntityKid.this.getAttackTarget());
        if (f < 256F) {
          CREEPSEntityKid.this.attackEntity(CREEPSEntityKid.this.getAttackTarget(), f);
          CREEPSEntityKid.this
              .getLookHelper()
              .setLookPositionWithEntity(CREEPSEntityKid.this.getAttackTarget(), 10.0F, 10.0F);
          CREEPSEntityKid.this.getNavigator().clearPathEntity();
          CREEPSEntityKid.this
              .getMoveHelper()
              .setMoveTo(
                  CREEPSEntityKid.this.getAttackTarget().posX,
                  CREEPSEntityKid.this.getAttackTarget().posY,
                  CREEPSEntityKid.this.getAttackTarget().posZ,
                  0.5D);
        }
        if (f < 1F) {
          CREEPSEntityKid.this.attackEntityAsMob(CREEPSEntityKid.this.getAttackTarget());
        }
      }
    }
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getFullBlockLightValue(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j - 1, k);
    return (i1 == Blocks.grass || i1 == Blocks.sand || i1 == Blocks.dirt || i1 == Blocks.gravel)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).isEmpty()
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(15) == 0
        && l > 6;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
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
          (this.rand.nextFloat()) * 0.2F + 1.0F + (0.6F - this.modelsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.getAttackTarget() instanceof EntityPlayer) return null;

    if (this.rand.nextInt(5) == 0) {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      Block l = this.worldObj.getBlock(i, j - 1, k);
      Block i1 = this.worldObj.getBlock(i, j, k);

      if (i1 == Blocks.snow || l == Blocks.snow || l == Blocks.ice) return "morecreeps:kidcold";
    }

    if (this.ridingEntity != null) return "morecreeps:kidride";
    else return "morecreeps:kid";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:kidhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:kiddeath";
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

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  @Override
  public EntityAgeable createChild(EntityAgeable ageable) {
    return new CREEPSEntityKid(this.worldObj);
  }
}
