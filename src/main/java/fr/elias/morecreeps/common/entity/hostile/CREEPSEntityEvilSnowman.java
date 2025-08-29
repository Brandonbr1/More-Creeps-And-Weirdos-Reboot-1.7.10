package fr.elias.morecreeps.common.entity.hostile;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityEvilSnowman extends EntityMob {

  public float snowsize;
  public String texture;

  public CREEPSEntityEvilSnowman(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/evilsnowman.png";
    this.setSize(0.7F, 1.5F);
    this.snowsize = 1.0F;
    this.isImmuneToFire = true;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(4, new CREEPSEntityEvilSnowman.AIAttackTarget());
    this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(
        2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (!this.onGround && !this.isJumping) {
      this.motionY -= 0.0020000000949949026D;
    }

    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    Block l = this.worldObj.getBlock(i, j - 1, k);
    Block i1 = this.worldObj.getBlock(i, j, k);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(this.snowsize + 1);

    if (
    /* l > 77 && l < 81 || */ i1 == Blocks.snow) {
      this.snowsize += 0.001F;
    } else {
      this.snowsize -= 0.002F;
    }

    if (this.inWater) {
      this.snowsize -= 0.02F;
    }

    if (this.snowsize > 6F) {
      this.snowsize = 6F;
    }

    this.setSize(this.snowsize * 0.45F, this.snowsize * 2.0F);

    if (this.snowsize < 0.050000000000000003D) {
      this.setDead();
    }
  }

  @Override
  public float getShadowSize() {
    return this.snowsize * 0.4F;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.onGround) {
      if (super.worldObj.isRemote) {
        for (int i = 0; i < 8; i++) {
          this.worldObj.playSoundAtEntity(
              this, "morecreeps:snowmanbounce", 1.0F, 2.0F - this.snowsize);
          this.worldObj.spawnParticle(
              "SNOWBALL".toLowerCase(), this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
      }

      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX = (d / f1) * 0.5D * 0.30000000192092896D + this.motionX * 0.20000000098023224D;
      this.motionZ = (d1 / f1) * 0.5D * 0.25000000192092897D + this.motionZ * 0.20000000098023224D;
      this.motionY = 0.35000000196046449D;

      if (this.rand.nextInt(20) == 0) {

        double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d5 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        this.motionX -= d2 * 0.40000000596046448D;
        this.motionZ -= d5 * 0.40000000596046448D;
      }

      if (this.rand.nextInt(20) == 0) {
        double d3 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d6 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        this.motionX -= d3 * 1.0D;
        this.motionY += 0.16599999368190765D;
      }

      if (this.rand.nextInt(40) == 0) {
        double d4 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d7 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        this.motionX -= d4 * 0.30000001192092896D;
        this.motionZ -= d7 * 0.30000001192092896D;
        this.motionY += 0.76599997282028198D;
      }
    }
  }

  public class AIAttackTarget extends EntityAIBase {

    public CREEPSEntityEvilSnowman evilsnowman = CREEPSEntityEvilSnowman.this;
    public int attackTime;

    public AIAttackTarget() {}

    @Override
    public boolean shouldExecute() {
      EntityLivingBase entitylivingbase = this.evilsnowman.getAttackTarget();
      return entitylivingbase != null && entitylivingbase.isEntityAlive();
    }

    @Override
    public void updateTask() {
      try {
        --this.attackTime;
        EntityLivingBase entitylivingbase = this.evilsnowman.getAttackTarget();
        double d0 = this.evilsnowman.getDistanceSqToEntity(entitylivingbase);

        if (d0 < 4.0D) {
          if (this.attackTime <= 0) {
            this.attackTime = 20;
            this.evilsnowman.attackEntityAsMob(entitylivingbase);
          }

          this.evilsnowman
              .getMoveHelper()
              .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
        } else if (d0 < 256.0D) {
          // ATTACK ENTITY GOES HERE
          this.evilsnowman.attackEntity(entitylivingbase, (float) d0);
          this.evilsnowman
              .getLookHelper()
              .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
        } else {
          this.evilsnowman.getNavigator().clearPathEntity();
          this.evilsnowman
              .getMoveHelper()
              .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
        }
      } catch (NullPointerException ex) {
        ex.printStackTrace();
      }
    }
  }

  /** knocks back this entity */
  public void knockBack(Entity entity, int i, double d, double d1) {
    if (this.worldObj.isRemote) {
      for (int j = 0; j < 8 + (int) (this.snowsize * 20F); j++) {
        this.worldObj.spawnParticle(
            "SNOWBALL".toLowerCase(),
            this.posX,
            this.posY + this.snowsize,
            this.posZ,
            0.0D,
            0.0D,
            0.0D);
      }
    }

    i *= i;
    this.motionY += 0.33300000429153442D;
    d *= 8.1999998092651367D;
    d1 *= 8.3000001907348633D;
    super.knockBack(entity, i, d, d1);
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (entity instanceof EntityPlayer) {
      EntityPlayer entityplayersp = (EntityPlayer) entity;
      double d = -MathHelper.sin((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
      this.motionX += d * 2D;
      this.motionZ += d1 * 2D;
      this.snowsize -= 0.02F;
    }

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
        this.setRevengeTarget((EntityLivingBase) entity);
      }

      return true;
    } else return false;
  }

  /** Called when the mob is falling. Calculates and applies fall damage. */
  public void fall(float distance, float damageMultiplier) {}

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
  @Override
  public boolean getCanSpawnHere() {
    return true;
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    Entity getentity = damagesource.getEntity();

    if (getentity == null) return;

    if (getentity instanceof EntityPlayer) {
      EntityPlayer entityplayer = (EntityPlayer) damagesource.getEntity();
      boolean flag = false;

      if (!((EntityPlayerMP) entityplayer)
              .func_147099_x()
              .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievesnowtiny)
          && this.snowsize < 0.1F) {
        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        entityplayer.addStat(MoreCreepsAndWeirdos.achievesnowtiny, 1);
        this.confetti();
        flag = true;
      }

      if (!((EntityPlayerMP) entityplayer)
              .func_147099_x()
              .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievesnowtall)
          && this.snowsize > 5F) {
        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        entityplayer.addStat(MoreCreepsAndWeirdos.achievesnowtall, 1);
        this.confetti();
        flag = true;
      }

      if (!((EntityPlayerMP) entityplayer)
              .func_147099_x()
              .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievesnow)
          && !flag) {
        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        entityplayer.addStat(MoreCreepsAndWeirdos.achievesnow, 1);
        this.confetti();
      }
    }

    if (!this.worldObj.isRemote) {
      if (this.rand.nextInt(10) == 0) {
        this.dropItem(Item.getItemFromBlock(Blocks.ice), this.rand.nextInt(3) + 1);
        this.dropItem(Item.getItemFromBlock(Blocks.snow), this.rand.nextInt(10) + 1);
      } else {
        this.dropItem(Item.getItemFromBlock(Blocks.snow), this.rand.nextInt(5) + 2);
      }
    }

    super.onDeath(damagesource);
  }

  public void confetti() {
    if (this.worldObj.isRemote) {
      MoreCreepsAndWeirdos.proxy.confettiA(this, this.worldObj);
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
              + (1.0F - this.snowsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    return "morecreeps:snowman";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:snowmanhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:snowmandeath";
  }
}
