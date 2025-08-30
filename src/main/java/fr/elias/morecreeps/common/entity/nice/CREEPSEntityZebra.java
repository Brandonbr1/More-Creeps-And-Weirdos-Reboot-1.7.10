package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityZebra extends EntityAnimal {

  EntityPlayerMP playermp;
  EntityPlayer entityplayer;
  World world;
  public int galloptime;
  public boolean tamed;
  public EntityPlayer owner;
  public boolean used;
  public String basetexture;
  protected double attackrange;
  protected int attack;

  private final EntityAIControlledByPlayer aiControlledByPlayer;

  public int spittimer;
  public int tamedcookies;
  public int basehealth;
  public double floatcycle;
  public int floatdir;
  public double floatmaxcycle;
  public float modelsize;
  public String name;
  public String texture;
  public double moveSpeed;
  public float health;
  static final String[] Names = {
    "Stanley",
    "Cid",
    "Hunchy",
    "The Heat",
    "Herman the Hump",
    "Dr. Hump",
    "Little Lousie",
    "Spoony G",
    "Mixmaster C",
    "The Maestro",
    "Duncan the Dude",
    "Charlie Camel",
    "Chip",
    "Charles Angstrom III",
    "Mr. Charles",
    "Cranky Carl",
    "Carl the Rooster",
    "Tiny the Peach",
    "Desert Dan",
    "Dungby",
    "Doofus"
  };
  static final String[] camelTextures = {"morecreeps:textures/entity/zebra.png"};

  public CREEPSEntityZebra(World world) {
    super(world);
    this.basetexture = camelTextures[this.rand.nextInt(camelTextures.length)];
    this.texture = this.basetexture;
    this.health = 25;
    this.basehealth = (int) this.health;
    this.attack = 2;
    this.moveSpeed = 0.65F;
    this.floatdir = 1;
    this.floatcycle = 0.0D;
    this.floatmaxcycle = 0.10499999672174454D;
    this.name = "";
    this.tamed = false;
    this.tamedcookies = this.rand.nextInt(7) + 1;
    this.modelsize = 2.0F;
    this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
    this.setSize(this.width * this.modelsize, this.height * this.modelsize);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIBreakDoor(this));
    this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.061D));
    this.tasks.addTask(5, new EntityAIWander(this, 0.25D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
    this.tasks.addTask(7, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65f);
  }

  @Override
  public boolean canBeSteered() {
    return true;
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(
        fr.elias.morecreeps.client.config.CREEPSConfig.zebraTamedDWID, (byte) 0);
    this.dataWatcher.addObject(fr.elias.morecreeps.client.config.CREEPSConfig.zebraNameDWID, "");
  }

  /**
   * This function is used when two same-species animals in 'love mode' breed to generate the new
   * baby animal.
   */
  public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal) {
    return new CREEPSEntityZebra(this.worldObj);
  }

  /**
   * Takes a coordinate in and returns a weight to determine how likely this creature will try to
   * path to the block. Args: x, y, z
   */
  @Override
  public float getBlockPathWeight(int i, int j, int k) {
    if (this.worldObj.getBlock(i, j - 1, k) == Blocks.leaves
        || this.worldObj.getBlock(i, j - 1, k) == Blocks.grass) return 10F;
    else return -(float) j;
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof EntityPlayer) return this.getYOffset() + 1.1F;
    else return this.getYOffset();
  }

  @Override
  public void updateRiderPosition() {
    if (this.riddenByEntity == null) return;

    if (this.riddenByEntity instanceof EntityPlayer) {
      this.riddenByEntity.setPosition(
          this.posX,
          (this.posY + 3.0999999046325684D) - (2.0F - this.modelsize) * 1.1F - this.floatcycle,
          this.posZ);
      return;
    } else return;
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    if (this.worldObj.difficultySetting.getDifficultyId() > 0) {
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

  /** Called when the entity is attacked. */
  public boolean attackEntityFrom(DamageSource damagesource, int i) {
    Entity entity = damagesource.getEntity();

    if (entity != null && (entity instanceof EntityPlayer) && this.tamed) {
      this.setAttackTarget(null);
      return false;
    }

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this && this.worldObj.difficultySetting.getDifficultyId() > 0) {
        this.setAttackTarget((EntityLivingBase) entity);
      }

      return true;
    } else return false;
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return !this.tamed;
  }

  @Override
  protected void updateAITick() {
    this.ignoreFrustumCheck = true;
    this.moveSpeed = 0.45F;

    if (this.riddenByEntity != null && (this.riddenByEntity instanceof EntityPlayer)) {
      this.moveForward = 0.0F;
      this.moveStrafing = 0.0F;
      this.moveSpeed = 1.95F;
      this.riddenByEntity.lastTickPosY = 0.0D;
      this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
      this.prevRotationPitch = this.rotationPitch = 0.0F;
      EntityPlayer entityplayer = (EntityPlayer) this.riddenByEntity;
      float f = 1.0F;

      if (entityplayer.getAIMoveSpeed() > 0.01F && entityplayer.getAIMoveSpeed() < 10F) {
        f = entityplayer.getAIMoveSpeed();
      }

      this.moveStrafing = (float) ((entityplayer.moveStrafing / f) * this.moveSpeed * 4.95F);
      this.moveForward = (float) ((entityplayer.moveForward / f) * this.moveSpeed * 4.95F);

      if (this.onGround && (this.moveStrafing != 0.0F || this.moveForward != 0.0F)) {
        this.motionY += 0.06100039929151535D;
      }

      if (this.moveStrafing != 0.0F || this.moveForward != 0.0F) {
        if (this.floatdir > 0) {
          this.floatcycle += 0.035999998450279236D;

          if (this.floatcycle > this.floatmaxcycle) {
            this.floatdir = this.floatdir * -1;
            this.fallDistance += -1.5F;
          }
        } else {
          this.floatcycle -= 0.017999999225139618D;

          if (this.floatcycle < -this.floatmaxcycle) {
            this.floatdir = this.floatdir * -1;
            this.fallDistance += -1.5F;
          }
        }
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
        //  this.isJumping = Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed();

        this.isJumping = MoreCreepsAndWeirdos.proxy.isJumpKeyDown();
        if (this.isJumping) {
          this.motionY += 0.37000000476837158D;
        }
      }

      if (this.onGround && this.isJumping) {
        double d = Math.abs(Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ));

        if (d > 0.13D) {
          double d1 = 0.13D / d;
          this.motionX = this.motionX * d1;
          this.motionZ = this.motionZ * d1;
        }

        this.motionX *= 2.9500000000000002D;
        this.motionZ *= 2.9500000000000002D;
      }

      return;
    } else {
      super.updateEntityActionState();
      return;
    }
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    ItemStack itemstack = entityplayer.inventory.getCurrentItem();
    this.used = false;

    if (this.tamed && entityplayer.isSneaking()) {
      if (itemstack == null) {
        if (!this.worldObj.isRemote) {
          entityplayer.openGui(
              MoreCreepsAndWeirdos.INSTANCE,
              7,
              this.worldObj,
              (int) this.posX,
              (int) this.posY,
              (int) this.posZ);
        }
        return true;
      }
    }

    if (itemstack == null && this.tamed) {
      if (entityplayer.riddenByEntity == null && this.modelsize > 0.5F) {
        this.handleMounting(entityplayer);
      } else if (this.modelsize < 0.5F) {
        MoreCreepsAndWeirdos.proxy.addChatMessage("Your Zebra is too small to ride!");
      } else if (entityplayer.riddenByEntity != null) {
        MoreCreepsAndWeirdos.proxy.addChatMessage("Unmount all creatures before riding your Zebra");
      }
    }

    if (!this.tamed
        && itemstack != null
        && this.riddenByEntity == null
        && itemstack.getItem() == Items.cookie) {
      this.used = true;

      if (!this.worldObj.isRemote && !this.tamed) {
        this.tamedcookies--;
        String plural = this.tamedcookies > 1 ? "s" : "";

        if (this.tamedcookies > 0) {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              "You need \2476"
                  + this.tamedcookies
                  + " cookie"
                  + plural
                  + " \247fto tame this speedy Zebra.");
        }

        if (itemstack.stackSize - 1 == 0) {
          entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        } else {
          itemstack.stackSize--;
        }
        if (!this.tamed) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:hotdogeat",
              this.getSoundVolume(),
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
        if (this.tamedcookies < 1) {
          this.tamed = true;
          this.owner = entityplayer;
          if (this.name.length() < 1) {
            this.name = Names[this.rand.nextInt(Names.length)];
          }
          // Update DataWatcher for client sync
          this.dataWatcher.updateObject(
              fr.elias.morecreeps.client.config.CREEPSConfig.zebraTamedDWID, (byte) 1);
          this.dataWatcher.updateObject(
              fr.elias.morecreeps.client.config.CREEPSConfig.zebraNameDWID, this.name);
          this.confetti(entityplayer);
          this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
          entityplayer.addStat(MoreCreepsAndWeirdos.achievezebra, 1);
          MoreCreepsAndWeirdos.proxy.addChatMessage("");
          MoreCreepsAndWeirdos.proxy.addChatMessage("\2476" + this.name + " \247fhas been tamed!");
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpiglevelup",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.smoke();
        }
      } else if (this.worldObj.isRemote && !this.tamed) {
        this.smoke();
      }
    }

    if (!this.used && !this.tamed && !this.worldObj.isRemote) {
      String plural = this.tamedcookies > 1 ? "s" : "";
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "You need \2476"
              + this.tamedcookies
              + " cookie"
              + plural
              + " \247fto tame this speedy Zebra.");
    }

    return true;
  }

  private void unlockZebraAchievement(EntityPlayer entityplayer) {
    /**
     * // todo fix crash if (this.world.isRemote && (!Minecraft.getMinecraft() .thePlayer
     * .getStatFileWriter() .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievezebra))) {
     * this.confetti(entityplayer); this.worldObj.playSoundAtEntity(entityplayer,
     * "morecreeps:achievement", 1.0F, 1.0F);
     * entityplayer.addStat(MoreCreepsAndWeirdos.achievezebra, 1); }
     *
     * <p>if (!this.world.isRemote && (!this.playermp .func_147099_x()
     * .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievezebra))) { this.confetti(entityplayer);
     * this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
     * this.playermp.addStat(MoreCreepsAndWeirdos.achievezebra, 1); }
     */
  }

  private void handleMounting(EntityPlayer entityplayer) {
    this.rotationYaw = entityplayer.rotationYaw;
    this.rotationPitch = entityplayer.rotationPitch;
    entityplayer.fallDistance = -5F;
    entityplayer.mountEntity(this);
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (entity != null && entity.boundingBox != null && this.getBoundingBox() != null) {
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
          && entity.getBoundingBox().maxY > this.getBoundingBox().minY
          && entity.boundingBox.minY < this.boundingBox.maxY) {
        this.attackTime = 20;
        entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attack);
      }
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
    Block i1 = this.worldObj.getBlock(i, j - 1, k);
    int j1 = this.worldObj.countEntities(CREEPSEntityNonSwimmer.class);
    return (i1 == Blocks.grass || i1 == Blocks.dirt)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.checkBlockCollision(this.getBoundingBox())
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(5) == 0
        && l > 7
        && j1 < 15;
  }

  public void confetti(EntityPlayer player) {

    double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
    CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
    creepsentitytrophy.setLocationAndAngles(
        player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
    if (!this.worldObj.isRemote) {
      this.worldObj.spawnEntityInWorld(creepsentitytrophy);
    }
  }

  private void smoke() {
    for (int i = 0; i < 7; i++) {
      double d = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      double d4 = this.rand.nextGaussian() * 0.02D;
      this.worldObj.spawnParticle(
          EnumParticleTypes.HEART,
          (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
          this.posY + 0.5D + this.rand.nextFloat() * this.height,
          (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
          d,
          d2,
          d4);
    }

    for (int j = 0; j < 4; j++) {
      for (int k = 0; k < 10; k++) {
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d3 = this.rand.nextGaussian() * 0.02D;
        double d5 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(
            EnumParticleTypes.EXPLOSION_NORMAL,
            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            this.posY + this.rand.nextFloat() * this.height + j,
            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            d1,
            d3,
            d5);
      }
    }
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setString("BaseTexture", this.basetexture);
    nbttagcompound.setString("ZebraName", this.name);
    nbttagcompound.setBoolean("Tamed", this.tamed);
    nbttagcompound.setInteger("TamedCookies", this.tamedcookies);
    nbttagcompound.setInteger("BaseHealth", this.basehealth);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.basetexture = nbttagcompound.getString("BaseTexture");
    this.name = nbttagcompound.getString("ZebraName");
    this.tamed = nbttagcompound.getBoolean("Tamed");

    if (this.basetexture == null) {
      this.basetexture = "/textures/entity/zebra.png";
    }

    this.texture = this.basetexture;
    this.basehealth = nbttagcompound.getInteger("BaseHealth");
    this.tamedcookies = nbttagcompound.getInteger("TamedCookies");
    this.modelsize = nbttagcompound.getFloat("ModelSize");

    // Update DataWatcher with loaded data
    this.dataWatcher.updateObject(
        fr.elias.morecreeps.client.config.CREEPSConfig.zebraTamedDWID, (byte) (this.tamed ? 1 : 0));
    this.dataWatcher.updateObject(
        fr.elias.morecreeps.client.config.CREEPSConfig.zebraNameDWID,
        this.name != null ? this.name : "");
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
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (2.0F - this.modelsize));
    }
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
    if (this.rand.nextInt(10) == 0) {
      this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
    }

    if (this.rand.nextInt(2) == 0) {
      this.dropItem(MoreCreepsAndWeirdos.zebrahide, 1);
    }

    super.onDeath(damagesource);
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 5;
  }

  @Override
  public EntityAgeable createChild(EntityAgeable ageable) {
    // TODO Auto-generated method stub
    return null;
  }

  public EntityAIControlledByPlayer getAIControlledByPlayer() {
    return this.aiControlledByPlayer;
  }
}
