package fr.elias.morecreeps.common.entity.hostile;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityFrisbee;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityRocket;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityManDog extends EntityMob {

  private boolean foundplayer;
  private PathEntity pathToEntity;

  /** The Entity this EntityCreature is set to attack. */
  protected Entity entityToAttack;

  /** returns true if a creature has attacked recently only used for creepers and skeletons */
  protected boolean hasAttacked;

  protected ItemStack stolengood;
  private float distance;
  public int frisbeetime;
  public boolean chase;
  protected Entity frisbeeent;
  protected ItemStack frisbeestack;
  public boolean fetch;
  public boolean tamed;
  public int tamedfood;
  public int attempts;
  public double dist;
  public double prevdist;
  public int facetime;
  public boolean frisbeehold;
  public boolean superdog;
  public int superstate;
  public int supertimer;
  public double superX;
  public double superY;
  public double superZ;
  public boolean flapswitch;
  public boolean superfly;
  public int superdistance;
  public int superdistancetimer;
  public float speed;
  public double wayX;
  public double wayY;
  public double wayZ;
  public int waypoint;
  public int wayvert;
  public double distcheck;
  public double prevdistcheck;
  public boolean superflag;
  public double wayXa;
  public double wayYa;
  public double wayZa;
  public double wayXb;
  public double wayYb;
  public double wayZb;
  public float modelsize;

  public String texture;

  public CREEPSEntityManDog(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/mandog.png";
    this.hasAttacked = false;
    this.foundplayer = false;
    this.frisbeetime = 0;
    this.chase = false;
    this.fetch = false;
    this.tamed = false;
    this.tamedfood = this.rand.nextInt(3) + 1;
    this.attempts = 0;
    this.dist = 0.0D;
    this.prevdist = 0.0D;
    this.facetime = 0;
    this.frisbeehold = false;
    this.superdog = false;
    this.superstate = 0;
    this.supertimer = 0;
    this.superdistance = this.rand.nextInt(10) + 5;
    this.superdistancetimer = this.rand.nextInt(100) + 50;
    this.frisbeestack = new ItemStack(Items.stick, 1);
    this.modelsize = 1.0F;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(4, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(2, new AIAttackEntity());
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(45D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.666D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("Attempts", this.attempts);
    nbttagcompound.setBoolean("Tamed", this.tamed);
    nbttagcompound.setBoolean("FrisbeeHold", this.frisbeehold);
    nbttagcompound.setBoolean("Chase", this.chase);
    nbttagcompound.setBoolean("Fetch", this.fetch);
    nbttagcompound.setBoolean("FoundPlayer", this.foundplayer);
    nbttagcompound.setInteger("TamedFood", this.tamedfood);
    nbttagcompound.setString("Texture", this.texture);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.attempts = nbttagcompound.getInteger("Attempts");
    this.tamed = nbttagcompound.getBoolean("Tamed");
    this.frisbeehold = nbttagcompound.getBoolean("FrisbeeHold");
    this.chase = nbttagcompound.getBoolean("Chase");
    this.fetch = nbttagcompound.getBoolean("Fetch");
    this.foundplayer = nbttagcompound.getBoolean("FoundPlayer");
    this.tamedfood = nbttagcompound.getInteger("TamedFood");
    this.texture = nbttagcompound.getString("Texture");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.superflag) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    super.onUpdate();

    if (this.tamed) {
      this.frisbeetime++;

      if (this.frisbeetime >= 20 && !this.isDead && !this.chase && !this.fetch) {
        this.frisbeeent = null;
        List<?> list =
            this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this, this.boundingBox.expand(25D, 25D, 25D));

        for (int i = 0; i < list.size(); i++) {
          Entity entity = (Entity) list.get(i);

          if (entity instanceof CREEPSEntityFrisbee) {
            this.frisbeeent = entity;
            this.faceEntity(this.frisbeeent, 360F, 0.0F);
            this.entityToAttack = this.frisbeeent;
            this.chase = true;
            this.attempts = 0;
          }
        }
      }

      if (this.chase && (this.frisbeeent == null || this.frisbeeent.isDead)) {
        this.chase = false;
        this.frisbeetime = 0;
      }

      if (this.chase) {
        if (this.frisbeeent != null) {
          this.entityToAttack = this.frisbeeent;

          if (Math.abs(this.posY - this.frisbeeent.posY) < 2D) {
            this.faceEntity(this.frisbeeent, 360F, 0.0F);
          }
        }

        this.moveEntityWithHeading((float) this.motionX, (float) this.motionZ);
        this.fallDistance = -25F;
        this.entityToAttack = this.frisbeeent;
        this.prevdist = this.dist;
        this.dist = this.frisbeeent.posX - this.posX;

        if (this.dist == this.prevdist) {
          if (this.rand.nextInt(2) == 0) {
            this.motionX += 0.75D;
            this.motionZ += 0.75D;
          } else {
            this.motionX -= 0.75D;
            this.motionZ -= 0.75D;
          }
        }

        if (Math.abs(this.frisbeeent.posX - this.posX) < 1.0D
            && Math.abs(this.frisbeeent.posY - this.posY) < 1.0D
            && Math.abs(this.frisbeeent.posZ - this.posZ) < 1.0D) {
          this.worldObj.playSoundAtEntity(
              this,
              "random.pop",
              0.2F,
              ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
          this.frisbeestack = new ItemStack(MoreCreepsAndWeirdos.frisbee, 1, 0);
          this.frisbeeent.setDead();
          this.chase = false;
          this.fetch = true;
          this.frisbeehold = true;
          EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 32D);

          if (entityplayer != null) {
            this.frisbeeent = entityplayer;
            this.faceEntity(entityplayer, 360F, 0.0F);
          }
        }

        double d = this.frisbeeent.posX - this.posX;
        double d2 = this.frisbeeent.posZ - this.posZ;
        float f = MathHelper.sqrt_double(d * d + d2 * d2);
        this.motionX =
            (d / f) * 0.40000000000000002D * 0.50000000192092897D
                + this.motionX * 0.18000000098023225D;
        this.motionZ =
            (d2 / f) * 0.40000000000000002D * 0.40000000192092894D
                + this.motionZ * 0.18000000098023225D;

        if (this.onGround) {
          double d4 = (this.frisbeeent.posY - this.posY) * 0.18000000098023225D;

          if (d4 > 0.5D) {
            d4 = 0.5D;
          }

          this.motionY = d4;
        }

        if (Math.abs(this.frisbeeent.posX - this.posX) < 5D
            && Math.abs(this.frisbeeent.posZ - this.posZ) < 5D
            && this.frisbeeent.motionX == 0.0D) {
          this.attempts++;

          if (this.attempts > 100) {
            this.chase = false;
            this.frisbeetime = 0;
            this.frisbeeent = null;
            this.fetch = true;
            this.frisbeehold = false;
            EntityPlayer entityplayer1 = this.worldObj.getClosestPlayerToEntity(this, 50D);

            if (entityplayer1 != null) {
              this.frisbeeent = entityplayer1;
              this.faceEntity(entityplayer1, 360F, 0.0F);
            }
          }
        }
      }

      if (this.fetch && this.frisbeeent != null) {
        if (Math.abs(this.frisbeeent.posX - this.posX) < 2D
            && Math.abs(this.frisbeeent.posY - this.posY) < 2D
            && Math.abs(this.frisbeeent.posZ - this.posZ) < 2D) {
          this.worldObj.playSoundAtEntity(
              this,
              "random.pop",
              0.2F,
              ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);

          if (this.frisbeehold) {
            this.dropItem(MoreCreepsAndWeirdos.frisbee, 1);
          }

          this.chase = false;
          this.fetch = false;
        }

        this.fallDistance = -25F;
        double d1 = this.frisbeeent.posX - this.posX;
        double d3 = this.frisbeeent.posZ - this.posZ;
        float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
        this.motionX =
            (d1 / f1) * 0.40000000000000002D * 0.50000000192092897D
                + this.motionX * 0.18000000098023225D;
        this.motionZ =
            (d3 / f1) * 0.40000000000000002D * 0.40000000192092894D
                + this.motionZ * 0.18000000098023225D;

        if (this.onGround) {
          double d5 = (this.frisbeeent.posY - this.posY) * 0.18000000098023225D;

          if (d5 > 0.5D) {
            d5 = 0.5D;
          }

          this.motionY = d5;
        }
      }
    }
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    double d = entity.posX - this.posX;
    double d1 = entity.posZ - this.posZ;
    float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
    this.motionX =
        (d / f1) * 0.40000000000000002D * 0.30000000192092896D
            + this.motionX * 0.18000000098023225D;
    this.motionZ =
        (d1 / f1) * 0.40000000000000002D * 0.44000000192092897D
            + this.motionZ * 0.18000000098023225D;
  }

  class AIAttackEntity extends EntityAIBase {

    public AIAttackEntity() {
      CREEPSEntityManDog.this.entityToAttack = CREEPSEntityManDog.this.getAttackTarget();
    }

    @Override
    public boolean shouldExecute() {
      return !(CREEPSEntityManDog.this.entityToAttack instanceof EntityPlayer)
          || !CREEPSEntityManDog.this.tamed;
    }

    @Override
    public void updateTask() {
      try {
        float f =
            CREEPSEntityManDog.this.getDistanceToEntity(CREEPSEntityManDog.this.getAttackTarget());
        if (f < 256F) {
          CREEPSEntityManDog.this.attackEntity(CREEPSEntityManDog.this.getAttackTarget(), f);
          CREEPSEntityManDog.this
              .getLookHelper()
              .setLookPositionWithEntity(CREEPSEntityManDog.this.entityToAttack, 10.0F, 10.0F);
          CREEPSEntityManDog.this.getNavigator().clearPathEntity();
          CREEPSEntityManDog.this
              .getMoveHelper()
              .setMoveTo(
                  CREEPSEntityManDog.this.entityToAttack.posX,
                  CREEPSEntityManDog.this.entityToAttack.posY,
                  CREEPSEntityManDog.this.entityToAttack.posZ,
                  0.5D);
        }
        if (f < 1F) {
          CREEPSEntityManDog.this.attackEntityAsMob(CREEPSEntityManDog.this.entityToAttack);
        }
      } catch (NullPointerException ex) {
        ex.printStackTrace();
      }
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Object obj = damagesource.getEntity();

    if (obj != null && (obj instanceof CREEPSEntityRocket)) {
      obj = this.worldObj.getClosestPlayerToEntity(this, 30D);
    }

    super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    return true;
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    if (this.tamed) {
      this.entityToAttack = null;
      return null;
    } else return null;
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    ItemStack itemstack = entityplayer.inventory.getCurrentItem();

    if (itemstack == null && this.tamed) {
      this.chase = false;
      this.fetch = false;
      this.frisbeeent = null;
    }

    if (itemstack != null) {
      if (itemstack.getItem() == Items.cooked_porkchop) {
        this.tamedfood--;
        this.smoke();

        if (this.tamedfood < 1) {
          this.smoke();
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:mandogtamed",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.tamed = true;
          this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(45D);
          this.texture = "morecreeps:textures/entity/mandogtamed.png";
        }
      }

      if (itemstack.getItem() == Items.bone) {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(45D);
      }
    }

    return true;
  }

  private void smoke() {
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
              + (1.0F - this.modelsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.superdog) return "morecreeps:superdogname";
    else return "morecreeps:mandog";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:mandoghurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:mandogdeath";
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getFullBlockLightValue(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j, k);
    return i1 != Blocks.sand
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(25) == 0
        && l > 10;
  }

  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    if (this.tamed) {
      this.isDead = false;
      this.deathTime = 0;
      return;
    } else {
      super.setDead();
      return;
    }
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (this.tamed) return;
    else {
      this.smoke();
      if (!this.worldObj.isRemote) {
        this.dropItem(Items.bone, this.rand.nextInt(5));
      }
      super.onDeath(damagesource);
      return;
    }
  }
}
