package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityEvilPig extends EntityMob {

  public float modelsize;
  public String texture;

  public CREEPSEntityEvilPig(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/evilpig.png";
    this.setSize(this.width * 2.2F, this.height * 1.6F);
    this.isImmuneToFire = true;
    this.modelsize = 1.0F;
    this.fallDistance = -25F;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.45D, true));
    this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
  }

  /*
   * protected void attackEntity(Entity entity, float f)
   * {
   * double d = entity.posX - posX;
   * double d1 = entity.posZ - posZ;
   * float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
   * motionX = (d / (double)f1) * 0.40000000000000002D * 0.50000000192092897D + motionX * 0.18000000098023225D;
   * motionZ = (d1 / (double)f1) * 0.40000000000000002D * 0.34000000192092894D + motionZ * 0.18000000098023225D;
   * if ((double)f < 2.5D - (2D - (double)modelsize) && entity.boundingBox.maxY > boundingBox.minY &&
   * entity.boundingBox.minY < boundingBox.maxY)
   * {
   * attackTime = 20;
   * entity.attackEntityFrom(DamageSource.causeMobDamage(this), attackStrength);
   * }
   * super.attackEntity(entity, f);
   * }
   */

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    return true;
  }

  /** Called when the entity is attacked. */
  public boolean attackEntityFrom(DamageSource damagesource, int i) {
    Entity entity = damagesource.getEntity();
    EntityPlayer player = (EntityPlayer) entity;
    double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
    this.motionX += d * 2D;
    this.motionZ += d1 * 2D;

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
        this.setRevengeTarget((EntityLivingBase) entity);
      }

      return true;
    } else return false;
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
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
              + 1.0F
              + (1.0F - this.modelsize) * 2.0F);
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    return "mob.pig.say";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "mob.pig.say";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "mob.pig.death";
  }
}
