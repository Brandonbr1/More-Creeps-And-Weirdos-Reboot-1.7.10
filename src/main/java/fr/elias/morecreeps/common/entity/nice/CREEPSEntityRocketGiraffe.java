package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityRocket;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityRocketGiraffe extends CREEPSEntityTameable {
  // Client-only key edge tracking (not saved)
  private transient boolean __clientLastJump = false;
  private int jumpCooldown = 0;

  EntityPlayer entityplayer;
  World world;
  EntityPlayerMP playermp;
  protected Entity playerToAttack;

  /** returns true if a creature has attacked recently only used for creepers and skeletons */
  protected boolean hasAttacked;

  public double dist;
  public double prevdist;
  public int facetime;
  public int rockettime;
  public int rocketcount;
  public int galloptime;
  public float modelsize;
  public double floatcycle;
  public int floatdir;
  public double floatmaxcycle;
  public double moveSpeed;

  public CREEPSEntityRocketGiraffe(World world) {
    super(world);
    this.moveSpeed = 0.65F;
    this.hasAttacked = false;
    this.setSize(1.5F, 4F);
    this.rockettime = this.rand.nextInt(10) + 5;
    this.modelsize = 1.0F;
    this.floatdir = 1;
    this.floatcycle = 0.0D;
    this.floatmaxcycle = 0.10499999672174454D;
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
    this.tasks.addTask(5, new EntityAIWander(this, 0.25D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
    this.tasks.addTask(7, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(70);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65f);
  }

  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.modelsize = nbttagcompound.getFloat("ModelSize");
  }

  @Override
  public void onLivingUpdate() {
    if (this.riddenByEntity != null && this.onGround) {
      this.fallDistance = 0.0F;
    }

    if (this.riddenByEntity != null && this.getIsTamed()) {
      this.stepHeight = CREEPSConfig.rocketGiraffeStepHeight;
      this.isJumping = false;
    }

    if (!this.worldObj.isRemote && this.jumpCooldown > 0) {
      this.jumpCooldown--;
    }
    super.onLivingUpdate();
    if (this.riddenByEntity != null) {
      if (!this.worldObj.isRemote) {
        this.setAttackTarget(null);
      }
      if (!this.worldObj.isRemote && this.getNavigator() != null) {
        this.getNavigator().clearPathEntity();
      }
      this.rotationYawHead = this.rotationYaw;
      this.renderYawOffset = this.rotationYaw;
    }
    if (this.worldObj != null && this.worldObj.isRemote) {
      if (this.riddenByEntity instanceof EntityPlayer) {
        EntityPlayer rider = (EntityPlayer) this.riddenByEntity;
        EntityPlayer local = Minecraft.getMinecraft().thePlayer;
        if (rider == local) {
          // Pass raw inputs; server will scale using this entity's movement attributes
          float fwd = local.moveForward;
          float strafe = local.moveStrafing;
          boolean __jump_now = MoreCreepsAndWeirdos.proxy.isJumpKeyDown();
          boolean jump = __jump_now && !this.__clientLastJump;
          this.__clientLastJump = __jump_now;
          this.moveForward = fwd;
          this.moveStrafing = strafe;
          MoreCreepsAndWeirdos.packetHandler.sendToServer(
              new fr.elias.morecreeps.common.packets.MountInputPacket(
                  this.getEntityId(), fwd, strafe, jump));
        }
      }
    }
  }

  @Override
  protected void updateAITick() {
    // Keep big models rendered even when out of frustum a bit (old mod behavior)
    if (this.modelsize > 1.0F) {
      this.ignoreFrustumCheck = true;
    }
    // Base ground speed when not ridden; moved speed when ridden is applied in
    // moveEntityWithHeading
    this.moveSpeed = 0.35F;
    // If ridden, don't run AI tick logic or send any packets; steering comes from MountInputPacket
    if (this.riddenByEntity != null) return;
    super.updateAITick();
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    if (!super.attackEntityFrom(damagesource, i)) {
      return false;
    }
    Entity entity = damagesource.getEntity();

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this && !(entity instanceof CREEPSEntityRocket)) {
        this.setAttackTarget((EntityLivingBase) entity);
      }

      return true;
    } else return false;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    this.setAttackTarget(null);

    if (!(entity instanceof EntityPlayer)) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX =
          (d / f1) * 0.40000000000000002D * 0.10000000192092896D
              + this.motionX * 0.18000000098023225D;
      this.motionZ =
          (d1 / f1) * 0.40000000000000002D * 0.14000000192092896D
              + this.motionZ * 0.18000000098023225D;

      if (f < 2D - (2D - this.modelsize)
          && entity.getBoundingBox().maxY > this.getBoundingBox().minY
          && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
        this.attackTime = 10;
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
      }

      super.attackEntityAsMob(entity);
    }
  }

  @Override
  public void updateRiderPosition() {
    if (this.riddenByEntity == null) return;
    float f = 3.35F - (1.0F - this.modelsize) * 2.0F;
    if (this.modelsize > 1.0F) {
      f += (this.modelsize - 1.0F) * 1.75F;
    }
    this.riddenByEntity.setPosition(this.posX, (this.posY + f) - this.floatcycle, this.posZ);
  }

  @Override
  public float getAIMoveSpeed() {
    if (this.riddenByEntity instanceof EntityPlayer && this.getIsTamed())
      return (float) CREEPSConfig.rocketGiraffeRiddenSpeed;
    return super.getAIMoveSpeed();
  }

  @Override
  public void moveEntityWithHeading(float strafe, float forward) {
    if (this.riddenByEntity instanceof EntityPlayer && this.getIsTamed()) {
      EntityPlayer rider = (EntityPlayer) this.riddenByEntity;
      // Align body to rider
      this.prevRotationYaw = this.rotationYaw;
      this.rotationYaw = rider.rotationYaw;
      this.rotationPitch = rider.rotationPitch * 0.5F;
      this.renderYawOffset = this.rotationYaw;
      this.rotationYawHead = this.rotationYaw;
      // Step up smoothly
      this.stepHeight = CREEPSConfig.rocketGiraffeStepHeight;
      // Prevent any auto-jump
      this.isJumping = false;
      if (!this.worldObj.isRemote) {

        // Read inputs straight from the server-side rider
        float srvForward = rider.moveForward;
        float srvStrafe = rider.moveStrafing;
        // Deadzone to kill tiny drift
        if (srvForward > -0.02F && srvForward < 0.02F) {
          srvForward = 0F;
        }
        if (srvStrafe > -0.02F && srvStrafe < 0.02F) {
          srvStrafe = 0F;
        }
        // Apply configurable speed via movement attribute (this is what EntityLivingBase uses)
        this.setAIMoveSpeed((float) CREEPSConfig.rocketGiraffeRiddenSpeed);
        IAttributeInstance att = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (att != null) {
          double base = CREEPSConfig.rocketGiraffeRiddenSpeed;
          // clamp a bit for safety
          if (base < 0.05D) {
            base = 0.05D;
          }
          if (base > 1.50D) {
            base = 1.50D;
          }
          att.setBaseValue(base);
        }
        super.moveEntityWithHeading(srvStrafe, srvForward);

      } else {
        // Mirror server side locally so we don't "phase" into blocks while stepping up
        float clForward = rider.moveForward;
        float clStrafe = rider.moveStrafing;
        if (clForward > -0.02F && clForward < 0.02F) {
          clForward = 0F;
        }
        if (clStrafe > -0.02F && clStrafe < 0.02F) {
          clStrafe = 0F;
        }
        this.setAIMoveSpeed((float) CREEPSConfig.rocketGiraffeRiddenSpeed);
        super.moveEntityWithHeading(clStrafe, clForward);
      }
      return;
    }
    super.moveEntityWithHeading(strafe, forward);
  }

  @Override
  public EntityAgeable createChild(EntityAgeable entity) {
    return new CREEPSEntityRocketGiraffe(this.worldObj);
  }

  public void doMountJump() {
    if (!this.worldObj.isRemote && this.onGround && this.jumpCooldown == 0) {
      this.motionY = 0.6D;
      this.isAirBorne = true;
      this.jumpCooldown = 8;
    }
  }

  @Override
  protected void onTamingComplete(EntityPlayer player) {
    this.worldObj.playSoundAtEntity(
        this,
        "morecreeps:giraffetamed",
        1.0F,
        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
  }

  @Override
  protected int getFoodsToTame() {
    return this.rand.nextInt(8) + 5;
  }

  @Override
  protected int getBaseHealth() {
    return this.rand.nextInt(50) + 30;
  }

  @Override
  protected ItemStack getTamedItemStack() {
    return new ItemStack(Items.cookie);
  }

  @Override
  protected String[] getNames() {
    return new String[] {
      "Rory", "Stan", "Clarence", "FirePower", "Lightning", "Rocket Jockey", "Rocket Ralph", "Tim"
    };
  }

  @Override
  protected String getCreatureTypeName() {
    return "Rocket Giraffe";
  }

  @Override
  protected CREEPSGuiHandler.GuiType getNameGuiType() {
    return CREEPSGuiHandler.GuiType.GIRAFFE_NAME;
  }

  @Override
  protected net.minecraft.stats.StatBase getTamingAchievement() {
    return MoreCreepsAndWeirdos.achieverocketgiraffe;
  }

  @Override
  protected float getModelSize() {
    return this.modelsize;
  }

  @Override
  protected boolean canBeRidden() {
    return true;
  }

  @Override
  protected String getBaseTexture() {
    return "morecreeps:textures/entity/rocketgiraffe.png";
  }

  @Override
  protected String getTamedTexture() {
    return "morecreeps:textures/entity/rocketgiraffetamed.png";
  }

  @Override
  protected boolean shouldChangeTextureWhenTamed() {
    return true;
  }

  @Override
  protected boolean handleSpecificInteraction(EntityPlayer player, ItemStack itemstack) {
    if (itemstack != null
        && this.riddenByEntity != null
        && (this.riddenByEntity instanceof EntityLiving)
        && itemstack.getItem() == MoreCreepsAndWeirdos.rocket) {

      if (itemstack.stackSize - 1 == 0) {
        player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
      } else {
        itemstack.stackSize--;
      }

      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:rocketfire",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      CREEPSEntityRocket creepsentityrocket = new CREEPSEntityRocket(this.worldObj, player, 0.0F);

      if (creepsentityrocket != null) {
        this.worldObj.spawnEntityInWorld(creepsentityrocket);
      }
      return true;
    }
    return false;
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    return super.interact(entityplayer);
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof EntityPlayer) return this.getYOffset() - 1.1F;
    else return this.getYOffset();
  }

  protected void smoke() {
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
    if (this.rand.nextInt(10) == 0) return "morecreeps:giraffe";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:giraffehurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:giraffedead";
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.getBoundingBox().minY);
    int k = MathHelper.floor_double(this.posZ);
    if (this.worldObj == null) return false;
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    if (this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.checkBlockCollision(this.getBoundingBox())
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(15) == 0
        && l > 8) {
      Block i1 = this.worldObj.getBlock(i, j - 1, k);

      return i1 != null
          && i1 != Blocks.snow
          && i1 != Blocks.cobblestone
          && i1 != Blocks.planks
          && i1 != Blocks.wool;
    }
    return false;
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

  // Sync important fields to client on initial spawn (1.7.10)
  @Override
  public void writeSpawnData(ByteBuf buf) {
    super.writeSpawnData(buf);
    buf.writeFloat(this.modelsize);
  }

  @Override
  public void readSpawnData(ByteBuf buf) {
    super.readSpawnData(buf);
    this.modelsize = buf.readFloat();
  }

  @Override
  protected void fall(float distance) {
    // Ignore tiny steps and reduce small fall damage; giraffes are tall, 1-block drops shouldn't
    // hurt.
    if (distance <= 2.5F) return;
    // If ridden, give a little more forgiveness
    if (this.riddenByEntity != null && this.getIsTamed() && distance <= 3.5F) return;
    super.fall(distance);
  }
}
