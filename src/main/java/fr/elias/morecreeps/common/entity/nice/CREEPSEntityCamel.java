package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityCamelJockey;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityCamel extends EntityMob {

  public int galloptime;
  public boolean tamed;
  public String texture;
  public EntityPlayer owner;
  public boolean used;
  public int interest;
  public String basetexture;
  protected double attackrange;
  protected int attack;

  public int spittimer;
  public int tamedcookies;
  public int basehealth;
  public float modelsize;
  public String name;
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
  static final String[] camelTextures = {
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camelwhite.png",
    "morecreeps:textures/entity/camelblack.png",
    "morecreeps:textures/entity/camelbrown.png",
    "morecreeps:textures/entity/camelgrey.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camel.png",
    "morecreeps:textures/entity/camelwhite.png"
  };

  public CREEPSEntityCamel(World world) {
    super(world);
    this.basetexture = camelTextures[this.rand.nextInt(camelTextures.length)];
    this.texture = this.basetexture;
    this.setSize(this.width * 1.5F, this.height * 4F);
    this.attack = 2;
    this.basehealth = 30;
    this.attackrange = 16D;
    this.interest = 0;
    this.spittimer = 30;
    this.name = "";
    this.tamed = false;
    this.tamedcookies = this.rand.nextInt(7) + 1;
    this.modelsize = 1.75F;
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D);
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.riddenByEntity != null) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  /**
   * Takes a coordinate in and returns a weight to determine how likely this creature will try to
   * path to the block. Args: x, y, z
   */
  // getBlockPathHeight in 1.8
  public float getBlockPathHeight(int x, int y, int z) {
    // TODO: SEEMS LIKE THIS IS INCORRECT??? super.getBlockPathWeight(p_70783_1_, p_70783_2_,
    // p_70783_3_)
    if (this.worldObj.getBlock(x, y, z) == Blocks.sand
        || this.worldObj.getBlock(x, y, z) == Blocks.gravel) return 10F;
    else return -(float) y;
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof EntityPlayer) return this.getYOffset() + 1.1F;
    else return this.getYOffset();
  }

  @Override
  public void updateRiderPosition() {
    if (this.riddenByEntity == null && this.worldObj == null) return;

    if (this.riddenByEntity instanceof EntityPlayer) {
      this.riddenByEntity.setPosition(
          this.posX, (this.posY + 4.5D) - (1.75F - this.modelsize) * 2.0F, this.posZ);
      return;
    }

    if (this.riddenByEntity instanceof CREEPSEntityCamelJockey) {
      this.riddenByEntity.setPosition(
          this.posX,
          (this.posY + 3.1500000953674316D) - (1.75F - this.modelsize) * 2.0F,
          this.posZ);
      return;
    } else return;
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return !this.tamed;
  }

  @Override
  public void onLivingUpdate() {
    // keep loaded via canDespawn override when tamed
    super.onLivingUpdate();
    //  System.out.println(this.tamed);
    // System.out.println(this.tamedcookies);

    if (this.modelsize > 1.75F) {
      this.ignoreFrustumCheck = true;
    }
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(CREEPSConfig.camelDWID, this.tamedcookies);
    this.dataWatcher.addObject(CREEPSConfig.camelTamedDWID, (byte) 0);
    this.dataWatcher.addObject(CREEPSConfig.camelNameDWID, "");
  }

  @Override
  public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
    if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase) {
      EntityLivingBase rider = (EntityLivingBase) this.riddenByEntity;
      this.rotationYaw = rider.rotationYaw;
      this.rotationPitch = rider.rotationPitch * 0.5F;
      this.setRotation(this.rotationYaw, this.rotationPitch);
      this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
      p_70612_1_ = rider.moveStrafing * 0.5F;
      p_70612_2_ = rider.moveForward;

      if (p_70612_2_ <= 0.0F) {
        p_70612_2_ *= 0.25F;
      }

      if (this.onGround) {
        p_70612_1_ = 0.0F;
        p_70612_2_ = 0.0F;
      }

      if (this.onGround) {
        this.motionY = 0.43;

        if (this.isPotionActive(Potion.jump)) {
          this.motionY += (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        this.setJumping(true);
        this.isAirBorne = true;

        if (p_70612_2_ > 0.0F) {
          float f2 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
          float f3 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
          this.motionX += -0.4F * f2;
          this.motionZ += 0.4F * f3;
        }

        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
      }

      this.stepHeight = 1.0F;
      this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1F;

      if (!this.worldObj.isRemote) {
        this.setAIMoveSpeed(
            (float)
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
        super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
      }

      this.prevLimbSwingAmount = this.limbSwingAmount;
      double d1 = this.posX - this.prevPosX;
      double d0 = this.posZ - this.prevPosZ;
      float f4 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;

      if (f4 > 1.0F) {
        f4 = 1.0F;
      }

      this.limbSwingAmount += (f4 - this.limbSwingAmount) * 0.4F;
      this.limbSwing += this.limbSwingAmount;
    } else {
      super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    }
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    if (this.worldObj.difficultySetting.getDifficultyId() > 0
        && (this.riddenByEntity instanceof CREEPSEntityCamelJockey)) {
      EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, this.attackrange);

      if (entityplayer != null) return entityplayer;

      if (this.rand.nextInt(10) == 0) return this.func_21018_getClosestTarget(this, 15D);
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
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this && this.worldObj.difficultySetting.getDifficultyId() > 0) {
        this.attackEntity(entity, 1);
      }

      return true;
    } else return false;
  }

  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.onGround) {
      double d = entity.posX - this.posX;
      double d2 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d2 * d2);
      this.motionX =
          (d / f1)
              * 0.20000000000000001D
              * (0.850000011920929D + this.motionX * 0.20000000298023224D);
      this.motionZ =
          (d2 / f1)
              * 0.20000000000000001D
              * (0.80000001192092896D + this.motionZ * 0.20000000298023224D);
      this.motionY = 0.10000000596246449D;
      this.fallDistance = -25F;
    }

    if (f > 2D
        && f < 7D
        && (entity instanceof EntityPlayer)
        && this.spittimer-- < 0
        && (this.riddenByEntity instanceof CREEPSEntityCamelJockey)) {
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:camelspits",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.spittimer = 30;
      this.faceEntity(entity, 360F, 0.0F);
      MoreCreepsAndWeirdos.proxy.spawnSpit(this.worldObj, this, 40);

      super.attackEntityAsMob(entity);
    }

    if (f < 3.2999999999999998D - (2D - this.modelsize)
        && entity.boundingBox != null
        && entity.boundingBox.maxY > entity.boundingBox.minY
        && entity.boundingBox.minY < entity.boundingBox.maxY) {
      super.attackEntityAsMob(entity);
      // entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attack);
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
      if (!this.worldObj.isRemote) {
        entityplayer.openGui(
            MoreCreepsAndWeirdos.INSTANCE,
            1,
            this.worldObj,
            (int) this.posX,
            (int) this.posY,
            (int) this.posZ);
      }
      return true;
    }

    if (itemstack != null
        && this.riddenByEntity == null
        && itemstack.getItem() == Items.cookie
        && !this.tamed) {
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
                  + " \247fto tame this lovely camel.");
        }

        if (itemstack.stackSize - 1 == 0) {
          entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        } else {
          itemstack.stackSize--;
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

        int health = this.basehealth;
        health += 10;
        this.setHealth(health);

        if (health > this.basehealth) {
          health = this.basehealth;
        }

        this.dataWatcher.updateObject(CREEPSConfig.camelDWID, this.tamedcookies);

        if (this.tamedcookies < 1) {
          this.tamed = true;
          this.owner = entityplayer;
          if (this.name.length() < 1) {
            this.name = Names[this.rand.nextInt(Names.length)];
          }
          // Update DataWatcher for client sync
          this.dataWatcher.updateObject(CREEPSConfig.camelTamedDWID, (byte) 1);
          this.dataWatcher.updateObject(CREEPSConfig.camelNameDWID, this.name);
          this.confetti(entityplayer);
          this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
          entityplayer.addStat(MoreCreepsAndWeirdos.achievecamel, 1);
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
              + " \247fto tame this lovely camel.");
    }

    if (itemstack == null && this.tamed && this.getHealth() > 0) {
      if (entityplayer.riddenByEntity == null && this.modelsize > 1.0F) {
        this.rotationYaw = entityplayer.rotationYaw;
        this.rotationPitch = entityplayer.rotationPitch;
        entityplayer.fallDistance = -5F;
        entityplayer.mountEntity(this);

        /**
         * Null comparison always yields false: this expression cannot be null if (this == null) {
         * double d = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
         * entityplayer.motionX += 1.5D * d; entityplayer.motionZ -= 0.5D; }
         */
      } else if (this.modelsize < 1.0F && this.tamed) {
        MoreCreepsAndWeirdos.proxy.addChatMessage("Your Camel is too small to ride!");
      } else {
        MoreCreepsAndWeirdos.proxy.addChatMessage("Unmount all creatures before riding your Camel");
      }
    }

    return true;
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
    if (!this.worldObj.isRemote) {
      for (int i = 0; i < 7; i++) {
        double d = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        double d4 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(
            "HEART".toLowerCase(),
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
              "EXPLOSION".toLowerCase(),
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + this.rand.nextFloat() * this.height + j,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d1,
              d3,
              d5);
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
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL
        && l > 6
        && super.getCanSpawnHere();
    // Don't know what's going on below...
    /*
     * int l = worldObj.getLight(getPosition());
     * int j1 = worldObj.countEntities(CREEPSEntityCamel.class);
     * Block i1 = worldObj.getBlockState(new BlockPos(getPosition())).getBlock();
     * return (i1 == Blocks.sand || i1 == Blocks.dirt || i1 == Blocks.gravel) && i1 != Blocks.cobblestone && i1 !=
     * Blocks.planks && i1 != Blocks.carpet && worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() ==
     * 0 &&worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&
     * worldObj.checkBlockCollision(getBoundingBox()) && worldObj.canBlockSeeSky(getPosition()) && l > 6 &&
     * rand.nextInt(40) == 0 && j1 < 25;
     */
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("SpitTimer", this.spittimer);
    nbttagcompound.setString("BaseTexture", this.basetexture);
    nbttagcompound.setString("CamelName", this.name);
    nbttagcompound.setBoolean("Tamed", this.tamed);
    nbttagcompound.setInteger("TamedCookies", this.tamedcookies);
    nbttagcompound.setInteger("BaseHealth", this.basehealth);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
    if (this.tamed && this.owner != null) {
      nbttagcompound.setString("OwnerName", this.owner.getCommandSenderName());
    }
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.spittimer = nbttagcompound.getInteger("SpitTimer");
    this.basetexture = nbttagcompound.getString("BaseTexture");
    this.name = nbttagcompound.getString("CamelName");
    this.tamed = nbttagcompound.getBoolean("Tamed");

    if (this.basetexture == null) {
      this.basetexture = "/textures/entity/camel.png";
    }

    this.texture = this.basetexture;

    this.basehealth = nbttagcompound.getInteger("BaseHealth");
    this.tamedcookies = nbttagcompound.getInteger("TamedCookies");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    if (this.tamed && nbttagcompound.hasKey("OwnerName")) {
      String ownerName = nbttagcompound.getString("OwnerName");
      this.owner = this.worldObj.getPlayerEntityByName(ownerName);
    }

    // Update DataWatcher with loaded data
    this.dataWatcher.updateObject(CREEPSConfig.camelTamedDWID, (byte) (this.tamed ? 1 : 0));
    this.dataWatcher.updateObject(CREEPSConfig.camelNameDWID, this.name != null ? this.name : "");
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
    return "morecreeps:camel";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:camelhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:cameldeath";
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (this.tamed) return;

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
    return 4;
  }
}
