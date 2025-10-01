package fr.elias.morecreeps.common.entity.hostile;

import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityBullet;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntitySneakySal extends EntityMob {

  World world;
  EntityPlayer entityplayer;
  public int salslots[];
  public static final Item[] salitems;
  public static final int salprices[] = {
    10, 200, 100, 20, 175, 150, 225, 50, 350, 100, 150, 10, 200, 150, 250
  };
  public static final String saldescriptions[] = {
    "BLORP COLA",
    "ARMY GEM",
    "HORSE HEAD GEM",
    "BAND AID",
    "SHRINK RAY",
    "EXTINGUISHER",
    "GROW RAY",
    "FRISBEE",
    "LIFE GEM",
    "GUN",
    "RAYGUN",
    "POPSICLE",
    "EARTH GEM",
    "FIRE GEM",
    "SKY GEM"
  };
  public static final ItemStack itemstack[];
  // private boolean foundplayer; // TODO (unused)
  protected Entity playerToAttack;

  /** returns true if a creature has attacked recently only used for creepers and skeletons */
  protected boolean hasAttacked;

  public boolean tamed;
  public int basehealth;
  public int tamedfood;
  public int attempts;
  public double dist;
  public double prevdist;
  public int facetime;
  public String basetexture;
  public int rockettime;
  public int rocketcount;
  public int galloptime;
  public String name;
  public int sale;
  public float saleprice;
  public int dissedmax;
  public ItemStack defaultHeldItem;
  private Entity targetedEntity;
  public int bulletTime;
  public float modelsize;
  public boolean shooting;
  public int shootingdelay;
  public int itemused;
  public int itemnew;
  public String texture;
  public double moveSpeed;
  public double attackStrength;
  public float health;

  public CREEPSEntitySneakySal(World world) {
    super(world);
    this.salslots = new int[30];
    this.basetexture = "morecreeps:textures/entity/sneakysal.png";
    this.texture = this.basetexture;
    this.moveSpeed = 0.65F;
    this.attackStrength = 3;
    this.basehealth = this.rand.nextInt(50) + 50;
    this.health = this.basehealth;
    this.hasAttacked = false;
    // this.foundplayer = false; // TODO (unused)
    this.setSize(1.5F, 4F);
    this.dissedmax = this.rand.nextInt(4) + 1;
    this.defaultHeldItem = new ItemStack(MoreCreepsAndWeirdos.gun, 1);
    this.sale = this.rand.nextInt(2000) + 100;
    this.saleprice = 0.0F;
    this.shooting = false;
    this.shootingdelay = 20;
    this.modelsize = 1.5F;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
    this.tasks.addTask(5, new EntityAIWander(this, 0.35D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 16F));
    this.tasks.addTask(7, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("Sale", this.sale);
    nbttagcompound.setFloat("SalePrice", this.saleprice);
    nbttagcompound.setInteger("DissedMax", this.dissedmax);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.sale = nbttagcompound.getInteger("Sale");
    this.saleprice = nbttagcompound.getFloat("SalePrice");
    this.dissedmax = nbttagcompound.getInteger("DissedMax");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    this.saleprice = 0.0F;
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    if (this.playerToAttack instanceof CREEPSEntitySneakySal) {
      this.playerToAttack = null;
    }

    super.onUpdate();
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  protected boolean interact(EntityPlayer entityplayer) {
    if (this.dissedmax > 0) {
      if (this.saleprice == 0.0F || this.sale < 1) {
        this.restockSal();
      }

      try {
        if (this.dissedmax > 0 && !(this.playerToAttack instanceof EntityPlayer)) {
          MoreCreepsAndWeirdos.guiHandler.tryOpenGui(
              CREEPSGuiHandler.GuiType.SNEAKY_SAL.id,
              entityplayer,
              this.worldObj,
              this.getEntityId());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return false;
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    if (this.dissedmax < 1) {
      EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16D);

      if (entityplayer != null && this.canEntityBeSeen(entityplayer)) return entityplayer;
      else return null;
    } else return null;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (entity == null) return;

    if (this.dissedmax < 1) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX =
          (d / f1) * 0.40000000000000002D * 0.20000000192092895D
              + this.motionX * 0.18000000098023225D;
      this.motionZ =
          (d1 / f1) * 0.40000000000000002D * 0.14000000192092896D
              + this.motionZ * 0.18000000098023225D;

      if (f < 2.7999999999999998D
          && entity.boundingBox.maxY > this.boundingBox.minY
          && entity.boundingBox.minY < this.boundingBox.maxY) {
        this.attackTime = 10;
        super.attackEntityAsMob(entity);
      }

      // super.attackEntityAsMob(entity);
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
    if (this.isEntityInvulnerable()) return false;
    else if (super.attackEntityFrom(p_70097_1_, p_70097_2_)) {
      Entity entity = p_70097_1_.getEntity();

      if (entity != null && entity instanceof EntityPlayer) {
        this.dissedmax = 0;
      }
    } else return false;
    return true;
  }

  /**
   * public boolean attackEntityFrom(DamageSource damagesource, float i) { Entity entity =
   * damagesource.getEntity();
   *
   * <p>if (entity != null && entity instanceof EntityPlayer) { this.dissedmax = 0; }
   *
   * <p>return super.attackEntityFrom(DamageSource.causeMobDamage(this), i); }
   */

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    if (this.boundingBox == null) return;

    if (this.shootingdelay-- < 1) {
      this.shooting = false;
    }

    this.targetedEntity = this.worldObj.getClosestPlayerToEntity(this, 3D);

    if (this.targetedEntity != null
        && (this.targetedEntity instanceof EntityPlayer)
        && this.canEntityBeSeen(this.targetedEntity)) {

      for (int i = 0; i < 360; i++) {
        this.rotationYaw = i;
      }

      if (this.rand.nextInt(4) == 0) {
        this.attackEntity(this.targetedEntity, 1.0F);
      }
    }

    if (this.bulletTime-- < 1 && this.dissedmax < 1) {
      this.bulletTime = this.rand.nextInt(50) + 25;
      double d = 64D;
      this.targetedEntity = this.worldObj.getClosestPlayerToEntity(this, 30D);

      if (this.targetedEntity != null
          && this.canEntityBeSeen(this.targetedEntity)
          && (this.targetedEntity instanceof EntityPlayer)
          && !this.isDead
          && !(this.targetedEntity instanceof CREEPSEntitySneakySal)
          && !(this.targetedEntity instanceof CREEPSEntityRatMan)) {
        double d2 = this.targetedEntity.getDistanceSqToEntity(this);

        if (d2 < d * d && d2 > 3D) {
          double d4 = this.targetedEntity.posX - this.posX;
          double d6 = this.targetedEntity.posZ - this.posZ;
          this.renderYawOffset =
              this.rotationYaw = (-(float) Math.atan2(d4, d6) * 180F) / (float) Math.PI;
          this.worldObj.playSoundAtEntity(
              this, "morecreeps:bullet", 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
          this.shooting = true;
          this.shootingdelay = 10;
          CREEPSEntityBullet creepsentitybullet = new CREEPSEntityBullet(this.worldObj, this, 0.0F);

          if (creepsentitybullet != null) {
            this.worldObj.spawnEntityInWorld(creepsentitybullet);
          }
        }
      }
    }

    this.sale--;

    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.spawnSalparticles(this.worldObj, this, this.rand);
    }

    if (this.dissedmax < 1 && this.playerToAttack == null) {
      this.findPlayerToAttack();
    }

    super.onLivingUpdate();
  }

  public void restockSal() {
    this.sale = this.rand.nextInt(2000) + 100;
    this.saleprice = 1.0F - (this.rand.nextFloat() * 0.25F - this.rand.nextFloat() * 0.25F);
    this.itemnew = this.rand.nextInt(salitems.length);
    this.itemused = 0;

    for (int i = 0; i < salitems.length; i++) {
      this.salslots[i] = i;
    }

    for (int j = 0; j < salitems.length; j++) {
      int k = this.rand.nextInt(salitems.length);
      int l = this.salslots[j];
      this.salslots[j] = this.salslots[k];
      this.salslots[k] = l;
    }
  }

  /** Returns the item that this EntityLiving is holding, if any. */
  @Override
  public ItemStack getHeldItem() {
    return this.defaultHeldItem;
  }

  private void smoke() {
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 10; j++) {
          double d = this.rand.nextGaussian() * 0.059999999999999998D;
          double d1 = this.rand.nextGaussian() * 0.059999999999999998D;
          double d2 = this.rand.nextGaussian() * 0.059999999999999998D;
          this.worldObj.spawnParticle(
              EnumParticleTypes.SMOKE_LARGE,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + this.rand.nextFloat() * this.height + i,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d,
              d1,
              d2);
        }
      }
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.rand.nextInt(10) == 0) return "morecreeps:giraffe";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:salhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:saldead";
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null) return false;

    if (this.boundingBox == null) {
      this.setSize(this.width <= 0 ? 0.6F : this.width, this.height <= 0 ? 1.8F : this.height);
      this.setPosition(this.posX, this.posY, this.posZ);
    }

    final int i = MathHelper.floor_double(this.posX);
    final int j = MathHelper.floor_double(this.boundingBox.minY);
    final int k = MathHelper.floor_double(this.posZ);

    // Don’t spawn in Peaceful
    if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) return false;

    // Basic light & ground checks
    final int light = this.worldObj.getBlockLightOpacity(i, j, k);
    final Block blockBelow = this.worldObj.getBlock(i, j - 1, k);
    if (blockBelow == null || blockBelow.getMaterial().isLiquid()) return false;

    // 1.7.10 uses boundingBox (field), not getBoundingBox()
    if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) return false;
    if (this.worldObj.checkBlockCollision(this.boundingBox)) return false;
    if (this.worldObj.isAnyLiquid(this.boundingBox)) return false;

    return light > 6 && super.getCanSpawnHere();
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    this.smoke();

    if (this.rand.nextInt(10) == 0) {
      this.dropItem(MoreCreepsAndWeirdos.rocket, this.rand.nextInt(5) + 1);
    }

    super.onDeath(damagesource);
  }

  public void confetti() {
    if (this.entityplayer == null || this.worldObj == null) return;
    double d = -MathHelper.sin(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
    CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.world);
    creepsentitytrophy.setLocationAndAngles(
        (this.entityplayer).posX + d * 3D,
        (this.entityplayer).posY - 2D,
        (this.entityplayer).posZ + d1 * 3D,
        (this.entityplayer).rotationYaw,
        0.0F);
    this.world.spawnEntityInWorld(creepsentitytrophy);
  }

  static {
    salitems =
        (new Item[] {
          MoreCreepsAndWeirdos.blorpcola,
          MoreCreepsAndWeirdos.armygem,
          MoreCreepsAndWeirdos.horseheadgem,
          MoreCreepsAndWeirdos.bandaid,
          MoreCreepsAndWeirdos.shrinkray,
          MoreCreepsAndWeirdos.extinguisher,
          MoreCreepsAndWeirdos.growray,
          MoreCreepsAndWeirdos.frisbee,
          MoreCreepsAndWeirdos.lifegem,
          MoreCreepsAndWeirdos.gun,
          MoreCreepsAndWeirdos.raygun,
          MoreCreepsAndWeirdos.popsicle,
          MoreCreepsAndWeirdos.earthgem,
          MoreCreepsAndWeirdos.firegem,
          MoreCreepsAndWeirdos.skygem
        });
    itemstack =
        (new ItemStack[] {
          new ItemStack(MoreCreepsAndWeirdos.blorpcola),
          new ItemStack(MoreCreepsAndWeirdos.armygem),
          new ItemStack(MoreCreepsAndWeirdos.horseheadgem),
          new ItemStack(MoreCreepsAndWeirdos.bandaid),
          new ItemStack(MoreCreepsAndWeirdos.shrinkray),
          new ItemStack(MoreCreepsAndWeirdos.extinguisher),
          new ItemStack(MoreCreepsAndWeirdos.growray),
          new ItemStack(MoreCreepsAndWeirdos.frisbee),
          new ItemStack(MoreCreepsAndWeirdos.lifegem),
          new ItemStack(MoreCreepsAndWeirdos.gun),
          new ItemStack(MoreCreepsAndWeirdos.raygun),
          new ItemStack(MoreCreepsAndWeirdos.popsicle),
          new ItemStack(MoreCreepsAndWeirdos.earthgem),
          new ItemStack(MoreCreepsAndWeirdos.firegem),
          new ItemStack(MoreCreepsAndWeirdos.skygem)
        });
  }
}
