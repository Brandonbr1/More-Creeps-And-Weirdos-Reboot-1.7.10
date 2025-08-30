package fr.elias.morecreeps.common.entity.hostile;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityBullet;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityArmyGuy extends EntityMob
    implements IRangedAttackMob, IEntityAdditionalSpawnData {

  public ItemStack defaultHeldItem;
  public int weapon;
  public int timeleft;
  public String ss;
  public boolean armright;
  public boolean armleft;
  public boolean legright;
  public boolean legleft;
  public boolean shrunk;
  public boolean helmet;
  public boolean head;
  public float modelsize;
  private Entity targetedEntity;
  public boolean loyal;
  public float distance;
  public int attack;
  public int attackTime;

  public CREEPSEntityArmyGuy(World world) {
    super(world);
    this.armright = false;
    this.armleft = false;
    this.legright = false;
    this.legleft = false;
    this.shrunk = false;
    this.helmet = false;
    this.head = false;
    this.defaultHeldItem = new ItemStack(MoreCreepsAndWeirdos.gun, 1);
    this.modelsize = 1.0F;
    this.loyal = false;
    this.attack = 1;
    this.attackTime = 20;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityCreature.class, 1.0D, false));
    this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F));
    this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 1.0D));
    this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(5, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(
        2,
        new CREEPSEntityArmyGuy.AINearestAttackableTarget(
            this, EntityCreature.class, 3, false, false, IMob.mobSelector));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(70D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return !this.loyal;
  }

  public CREEPSEntityArmyGuy(
      World world, Entity entity, double d, double d1, double d2, boolean flag) {
    this(world);
    this.setPosition(d, d1, d2);
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
    if (this.rand.nextInt(7) == 0) return "morecreeps:army";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:armyhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:armydeath";
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
    float health = this.getHealth();
    if (health < 60 && health > 50 && !this.helmet) {
      this.helmet = true;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armyhelmet", 1.0F, 0.95F);
      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(
            this.worldObj, this.posX, this.posY + 2.5D, this.posZ, false);
      }
    } else if (health < 50 && health > 40 && !this.armleft) {
      this.helmet = true;
      this.armleft = true;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armyarm", 1.0F, 0.95F);
      CREEPSEntityArmyGuyArm creepsentityarmyguyarm = new CREEPSEntityArmyGuyArm(this.worldObj);
      creepsentityarmyguyarm.setLocationAndAngles(
          this.posX, this.posY + 1.0D, this.posZ, this.rotationYaw, 0.0F);
      creepsentityarmyguyarm.motionX = 0.25D;
      creepsentityarmyguyarm.motionY = -0.25D;
      creepsentityarmyguyarm.modelsize = this.modelsize;
      if (!this.worldObj.isRemote) {
        this.worldObj.spawnEntityInWorld(creepsentityarmyguyarm);
      }
      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(
            this.worldObj, this.posX - 0.5D, this.posY + 1.0D, this.posZ, true);
      }
    } else if (health < 40 && health > 30 && !this.legright) {
      this.helmet = true;
      this.armleft = true;
      this.legright = true;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armyleg", 1.0F, 0.95F);
      CREEPSEntityArmyGuyArm creepsentityarmyguyarm1 = new CREEPSEntityArmyGuyArm(this.worldObj);
      creepsentityarmyguyarm1.setLocationAndAngles(
          this.posX, this.posY + 1.0D, this.posZ, this.rotationYaw, 0.0F);
      creepsentityarmyguyarm1.motionX = 0.25D;
      creepsentityarmyguyarm1.motionY = 0.25D;
      creepsentityarmyguyarm1.texture =
          new ResourceLocation(
              Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_LEG);
      creepsentityarmyguyarm1.modelsize = this.modelsize;
      if (!this.worldObj.isRemote) {
        this.worldObj.spawnEntityInWorld(creepsentityarmyguyarm1);
      }

      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(
            this.worldObj, this.posX - 0.5D, this.posY, this.posZ, true);
      }
    } else if (health < 30 && health > 20 && !this.legleft) {
      this.helmet = true;
      this.armleft = true;
      this.legright = true;
      this.legleft = true;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armybothlegs", 1.0F, 0.95F);
      CREEPSEntityArmyGuyArm creepsentityarmyguyarm2 = new CREEPSEntityArmyGuyArm(this.worldObj);
      creepsentityarmyguyarm2.setLocationAndAngles(
          this.posX, this.posY + 1.0D, this.posZ, this.rotationYaw, 0.0F);
      creepsentityarmyguyarm2.motionX = 0.25D;
      creepsentityarmyguyarm2.motionY = -0.25D;
      creepsentityarmyguyarm2.texture =
          new ResourceLocation(
              Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_LEG);
      creepsentityarmyguyarm2.modelsize = this.modelsize;
      if (!this.worldObj.isRemote) {
        this.worldObj.spawnEntityInWorld(creepsentityarmyguyarm2);
      }
      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(
            this.worldObj, this.posX + 0.5D, this.posY, this.posZ, true);
      }
    } else if (health < 20 && health > 10 && !this.armright) {
      this.helmet = true;
      this.armleft = true;
      this.legright = true;
      this.legleft = true;
      this.armright = true;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armyarm", 1.0F, 0.95F);
      CREEPSEntityArmyGuyArm creepsentityarmyguyarm3 = new CREEPSEntityArmyGuyArm(this.worldObj);
      creepsentityarmyguyarm3.setLocationAndAngles(
          this.posX, this.posY + 1.0D, this.posZ, this.rotationYaw, 0.0F);
      creepsentityarmyguyarm3.motionX = 0.25D;
      creepsentityarmyguyarm3.motionY = 0.25D;
      creepsentityarmyguyarm3.modelsize = this.modelsize;
      if (!this.worldObj.isRemote) {
        this.worldObj.spawnEntityInWorld(creepsentityarmyguyarm3);
        if (this.rand.nextInt(10) == 0) {
          this.dropItem(MoreCreepsAndWeirdos.gun, 1);
        }
      }
      this.defaultHeldItem = null;

      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(
            this.worldObj, this.posX, this.posY + 1.0D, this.posZ, true);
      }
    } else if (health < 10 && health > 0 && !this.head) {
      this.helmet = true;
      this.armleft = true;
      this.legright = true;
      this.legleft = true;
      this.armright = true;
      this.head = true;
      this.defaultHeldItem = null;
      this.worldObj.playSoundAtEntity(this, "morecreeps:armyhead", 1.0F, 0.95F);

      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.blood(this.worldObj, this.posX, this.posY, this.posZ, true);
      }
    }
  }

  @Override
  public double getYOffset() {
    if (this.legleft && this.legright && this.head) return -1.4D + (1.0D - this.modelsize);
    else if (this.legleft && this.legright) return -0.75D + (1.0D - this.modelsize);
    else return 0.0D;
  }

  @Override
  public void onUpdate() {
    if (this.getAttackTarget() instanceof CREEPSEntityArmyGuyArm) {
      this.setAttackTarget(null);
    }
    if (this.loyal
        && this.getAttackTarget() != null
        && ((this.getAttackTarget() instanceof CREEPSEntityArmyGuy)
            || (this.getAttackTarget() instanceof CREEPSEntityGuineaPig)
            || (this.getAttackTarget() instanceof CREEPSEntityHunchback))
        && ((CREEPSEntityArmyGuy) this.getAttackTarget()).loyal) {
      this.setAttackTarget(null);
    }
    EntityLivingBase livingbase = this.getAttackTarget();
    if (livingbase != null && (livingbase.getAITarget() instanceof EntityPlayer)) {
      this.setAttackTarget(livingbase);
    }
    if (!this.loyal
        && this.getAttackTarget() != null
        && (this.getAttackTarget() instanceof CREEPSEntityArmyGuy)) {
      this.setAttackTarget(null);
    }
    if (this.legright) {
      this.setJumping(true);
    }

    super.onUpdate();
  }

  private void smoke() {
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 10; j++) {
          double d = this.rand.nextGaussian() * 0.02D;
          double d1 = this.rand.nextGaussian() * 0.02D;
          double d2 = this.rand.nextGaussian() * 0.02D;
          this.worldObj.spawnParticle(
              "explode",
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

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    // Block i1 = worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
    return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL
        && l > 8
        && super.getCanSpawnHere();
    // Method used by Minecraft above, probably better to use it instead?
    // return i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.double_stone_slab && i1
    // !=
    // Blocks.stone_slab && i1 != Blocks.planks && i1 != Blocks.wool &&
    // worldObj.getCollidingBoundingBoxes(this,
    // getBoundingBox()).size() == 0 && rand.nextInt(10) == 0 && l > 8;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setBoolean("ArmRight", this.armright);
    nbttagcompound.setBoolean("ArmLeft", this.armleft);
    nbttagcompound.setBoolean("LegRight", this.legright);
    nbttagcompound.setBoolean("LegLeft", this.legleft);
    nbttagcompound.setBoolean("Helmet", this.helmet);
    nbttagcompound.setBoolean("Head", this.head);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
    nbttagcompound.setBoolean("Loyal", this.loyal);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.armright = nbttagcompound.getBoolean("ArmRight");
    this.armleft = nbttagcompound.getBoolean("ArmLeft");
    this.legright = nbttagcompound.getBoolean("LegRight");
    this.legleft = nbttagcompound.getBoolean("LegLeft");
    this.helmet = nbttagcompound.getBoolean("Helmet");
    this.head = nbttagcompound.getBoolean("Head");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    this.loyal = nbttagcompound.getBoolean("Loyal");
    float health = this.getHealth();

    if (this.helmet) {
      health = 60;
    }

    if (this.armleft) {
      health = 50;
    }

    if (this.legright) {
      health = 40;
    }

    if (this.armright) {
      health = 30;
      this.defaultHeldItem = null;
    }

    if (this.legleft) {
      health = 20;
    }

    if (this.head) {
      health = 10;
    }
  }

  /** Returns the item ID for the item the mob drops on death. */
  @Override
  protected Item getDropItem() {
    return Items.arrow;
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (!this.worldObj.isRemote) {
      if (this.rand.nextInt(5) == 0) {
        this.dropItem(MoreCreepsAndWeirdos.gun, 1);
      } else {
        this.dropItem(Items.apple, this.rand.nextInt(2));
      }
    }
  }

  /** Returns the item that this EntityLiving is holding, if any. */
  @Override
  public ItemStack getHeldItem() {
    return this.defaultHeldItem;
  }

  @Override
  public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_) {
    double d2 = this.targetedEntity.posX - this.posX;
    double d3 =
        (this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F)
            - (this.posY + this.height / 2.0F);
    double d4 = this.targetedEntity.posZ - this.posZ;
    this.renderYawOffset =
        this.rotationYaw = (-(float) Math.atan2(d2, d4) * 180F) / (float) Math.PI;
    this.worldObj.playSoundAtEntity(
        this, "morecreeps:bullet", 0.5F, 0.4F / (this.rand.nextFloat() * 0.4F + 0.8F));
    CREEPSEntityBullet creepsentitybullet = new CREEPSEntityBullet(this.worldObj, this, 0.0F);
    if (!this.worldObj.isRemote && !this.armright) {
      this.worldObj.spawnEntityInWorld(creepsentitybullet);
    }
  }

  static class AINearestAttackableTarget extends EntityAINearestAttackableTarget {

    public CREEPSEntityArmyGuy armyGuy;

    public AINearestAttackableTarget(
        final CREEPSEntityArmyGuy p_i45858_1_,
        Class<?> p_i45858_2_,
        int p_i45858_3_,
        boolean p_i45858_4_,
        boolean p_i45858_5_,
        final IEntitySelector mobselector) {
      super(p_i45858_1_, p_i45858_2_, p_i45858_3_, p_i45858_4_, p_i45858_5_, mobselector);
      this.armyGuy = p_i45858_1_;
    }

    @Override
    public boolean shouldExecute() {
      EntityLivingBase baseEntity = this.armyGuy.getAttackTarget();
      if ((baseEntity instanceof EntityPlayer)
          || (baseEntity instanceof CREEPSEntityArmyGuy)
          || (baseEntity instanceof CREEPSEntityHunchback)
          || (baseEntity instanceof CREEPSEntityGuineaPig)) return false;
      else return super.shouldExecute();
    }
  }

  @Override
  public void writeSpawnData(ByteBuf buffer) {
    buffer.writeBoolean(this.loyal);
  }

  @Override
  public void readSpawnData(ByteBuf additionalData) {
    this.loyal = additionalData.readBoolean();
  }
}
