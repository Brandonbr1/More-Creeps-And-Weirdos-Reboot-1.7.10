package fr.elias.morecreeps.common.entity.nice;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityHorseHead extends EntityAnimal {

  public int galloptime;
  public double floatcycle;
  public int floatdir;
  public double floatmaxcycle;
  public int blastoff;
  public int blastofftimer;
  public String texture;

  public CREEPSEntityHorseHead(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/horsehead.png";
    this.setSize(0.6F, 2.0F);
    this.floatdir = 1;
    this.floatcycle = 0.0D;
    this.floatmaxcycle = 0.10499999672174454D;
    this.blastoff = this.rand.nextInt(500) + 400;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(3, new EntityAILookIdle(this));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 5; i++) {
        MoreCreepsAndWeirdos.proxy.smokeHorseHead(this.worldObj, this, this.rand);
      }
    }

    if (this.isEntityInsideOpaqueBlock()) {
      this.posY += 2.5D;
      this.floatdir = 1;
      this.floatcycle = 0.0D;
    }

    this.fallDistance = -100F;

    if (this.riddenByEntity == null && this.blastoff-- < 0) {
      this.motionY += 0.15049999952316284D;
      double d = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
      this.motionX += d * 0.10999999940395355D;
      this.motionZ += d1 * 0.10999999940395355D;

      if (this.worldObj.isRemote) {
        for (int j = 0; j < 25; j++) {
          MoreCreepsAndWeirdos.proxy.smokeHorseHead(this.worldObj, this, this.rand);
        }
      }

      if (this.posY > 100D) {
        this.setDead();
      }
    }

    if (this.riddenByEntity == null
        && this.blastoff > 0
        && this.worldObj.getBlock((int) this.posX, (int) this.posY - 1, (int) this.posZ)
            == Blocks.air) {
      this.posY -= 0.25D;
    }

    this.ignoreFrustumCheck = true;

    if (this.floatdir > 0) {
      this.floatcycle += 0.017999999225139618D;

      if (this.floatcycle > this.floatmaxcycle) {
        this.floatdir = this.floatdir * -1;
        this.fallDistance += -1.5F;
      }
    } else {
      this.floatcycle -= 0.0094999996945261955D;

      if (this.floatcycle < -this.floatmaxcycle) {
        this.floatdir = this.floatdir * -1;
        this.fallDistance += -1.5F;
      }
    }

    if (this.riddenByEntity != null && (this.riddenByEntity instanceof EntityPlayer)) {
      this.blastoff++;

      if (this.blastoff > 50000) {
        this.blastoff = 50000;
      }
    }

    super.onUpdate();
  }

  @Override
  protected void updateAITasks() {
    this.motionY *= 0.80000001192092896D;
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.95D);

    if (this.riddenByEntity != null && (this.riddenByEntity instanceof EntityPlayer)) {
      this.moveForward = 0.0F;
      this.moveStrafing = 0.0F;
      this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(1.95D);
      this.riddenByEntity.lastTickPosY = 0.0D;
      this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
      this.prevRotationPitch = this.rotationPitch = 0.0F;
      EntityPlayer entityplayer = (EntityPlayer) this.riddenByEntity;
      float f = 1.0F;

      if (entityplayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
              > 0.01D
          && entityplayer.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
              < 10D) {
        f = (float) this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue();
      }

      this.moveStrafing =
          (entityplayer.moveStrafing / f)
              * (float)
                  this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
              * 4.95F;
      this.moveForward =
          (entityplayer.moveForward / f)
              * (float)
                  this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue()
              * 4.95F;

      if (this.onGround && (this.moveStrafing != 0.0F || this.moveForward != 0.0F)) {
        this.motionY += 0.16100040078163147D;
      }

      if (this.moveStrafing == 0.0F && this.moveForward == 0.0F) {
        this.isJumping = false;
        this.galloptime = 0;
      }

      if (this.moveForward != 0.0F && this.galloptime++ > 10) {
        this.galloptime = 0;

        if (this.handleWaterMovement()) {
          this.worldObj.playSoundAtEntity(
              this, "morecreeps:giraffesplash", this.getSoundVolume(), 1.2F);
        } else {
          this.worldObj.playSoundAtEntity(
              this, "morecreeps:giraffegallop", this.getSoundVolume(), 1.2F);
        }
      }

      if (this.onGround && !this.isJumping) {
        this.isJumping =
            ObfuscationReflectionHelper.getPrivateValue(EntityLivingBase.class, entityplayer, 40);
        if (this.isJumping) {
          this.motionY += 0.37000000476837158D;
        }
      }

      if (this.onGround && this.isJumping) {
        double d = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

        if (d > 0.13D) {
          double d2 = 0.13D / d;
          this.motionX = this.motionX * d2;
          this.motionZ = this.motionZ * d2;
        }

        this.motionX *= 6.9500000000000002D;
        this.motionZ *= 6.9500000000000002D;
      }

      if (MoreCreepsAndWeirdos.proxy.isJumpKeyDown() && this.posY < 120D) {
        this.motionY += 0.15049999952316284D;
        double d1 = -MathHelper.sin((entityplayer.rotationYaw * (float) Math.PI) / 180F);
        double d3 = MathHelper.cos((entityplayer.rotationYaw * (float) Math.PI) / 180F);
        this.motionX += d1 * 0.15999999642372131D;
        this.motionZ += d3 * 0.15999999642372131D;

        if (this.blastofftimer-- < 0) {
          this.worldObj.playSoundAtEntity(this, "morecreeps:horseheadblastoff", 1.0F, 1.0F);
          this.blastofftimer = 10;
        }

        if (this.worldObj.isRemote) {
          for (int i = 0; i < 25; i++) {
            MoreCreepsAndWeirdos.proxy.smokeHorseHead(this.worldObj, this, this.rand);
          }
        }
      }

      return;
    } else
      // super.updateEntityActionState();
      return;
  }

  @Override
  public void updateRiderPosition() {
    if (this.riddenByEntity == null) return;

    if (this.riddenByEntity instanceof EntityPlayer) {
      this.riddenByEntity.setPosition(this.posX, (this.posY + 2.5D) - this.floatcycle, this.posZ);
      return;
    } else return;
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    if (entityplayer.riddenByEntity == null) {
      this.rotationYaw = entityplayer.rotationYaw;
      this.rotationPitch = entityplayer.rotationPitch;
      entityplayer.fallDistance = -15F;
      entityplayer.mountEntity(this);
      this.blastoff += this.rand.nextInt(500) + 200;
      this.motionX = 0.0D;
      this.motionY = 0.0D;
      this.motionZ = 0.0D;

    } else {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "Unmount all creatures before riding your Horse Head");
    }

    return false;
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j - 1, k);
    return (i1 == Blocks.sand || i1 == Blocks.grass || i1 == Blocks.dirt)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.stone_slab
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(25) == 0
        && l > 7;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("Blastoff", this.blastoff);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.blastoff = nbttagcompound.getInteger("Blastoff");
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    return "morecreeps:horsehead";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:hippohurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:hippodeath";
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
    return new CREEPSEntityHorseHead(this.worldObj);
  }
}
