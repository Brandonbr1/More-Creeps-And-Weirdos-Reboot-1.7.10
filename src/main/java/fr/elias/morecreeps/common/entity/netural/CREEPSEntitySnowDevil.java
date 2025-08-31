package fr.elias.morecreeps.common.entity.netural;

import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntitySnowDevil extends EntityMob {

  EntityPlayer entityplayer;
  EntityPlayerMP playermp;
  World world;
  public boolean rideable;
  public int interest;
  // private boolean primed; // TODO (unused)
  public boolean tamed;
  public int basehealth;
  public int armor;
  public String basetexture;
  public boolean used;
  public float modelsize;
  public String name;
  public double moveSpeed;
  public double attackStrength;
  public float health;
  static final String Names[] = {
    "Satan",
    "The Butcher",
    "Killer",
    "Tad",
    "Death Spanker",
    "Death Toll",
    "Bruiser",
    "Bones",
    "The Devil",
    "Little Devil",
    "Skinny",
    "Death to All",
    "I Will Hurt You",
    "Pierre",
    "Bonecruncher",
    "Bone Breaker",
    "Blood 'N Guts",
    "Kill Kill",
    "Murder",
    "The Juicer",
    "Scream",
    "Bloody Buddy",
    "Sawblade",
    "Ripper",
    "Razor",
    "Valley Strangler",
    "Choppy Joe",
    "Wiconsin Shredder",
    "Urinal",
    "Johnny Choke",
    "Annihilation",
    "Bloodshed",
    "Destructo",
    "Rub Out",
    "Massacre",
    "Felony",
    "The Mangler",
    "Destroyer",
    "The Marauder",
    "Wreck",
    "Vaporizer",
    "Wasteland",
    "Demolition Duo",
    "Two Knocks",
    "Double Trouble",
    "Thing One & Thing Two",
    "Wipeout",
    "Devil Duo",
    "Two Shot",
    "Misunderstood",
    "Twice As Nice"
  };
  static final String snowTextures[] = {
    "morecreeps:textures/entity/snowdevil1.png", "morecreeps:textures/entity/snowdevil2.png"
  };

  public String texture;

  public CREEPSEntitySnowDevil(World world) {
    super(world);
    // this.primed = false; // TODO (unused)
    this.basetexture = snowTextures[this.rand.nextInt(snowTextures.length)];
    this.texture = this.basetexture;
    this.setSize(this.width * 1.6F, this.height * 1.6F);
    this.height = 2.0F;
    this.width = 2.0F;
    this.moveSpeed = 0.6F;
    this.rideable = false;
    this.basehealth = this.rand.nextInt(50) + 15;
    this.health = this.basehealth;
    this.attackStrength = 3;
    this.tamed = false;
    this.name = "";
    this.modelsize = 1.0F;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
    this.tasks.addTask(5, new EntityAIWander(this, 0.35D));
    this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 16F));
    this.tasks.addTask(7, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(65);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6f);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3);
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 15D);

    if (entityplayer != null && (!this.tamed || this.rand.nextInt(10) == 0)) return entityplayer;

    if (this.rand.nextInt(6) == 0) {
      EntityLiving entityliving = this.getClosestTarget(this, 10D);
      return entityliving;
    }

    return null;
  }

  public EntityLiving getClosestTarget(Entity entity, double d) {
    AxisAlignedBB boundingBox = this.getBoundingBox();
    if (boundingBox == null) return null;

    List<Entity> list =
        this.worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(20D, 20D, 20D));

    for (Entity entity1 : list) {
      if (entity1 instanceof EntityCreature) {
        EntityCreature entitycreature = (EntityCreature) entity1;
        EntityLivingBase attackTarget = entitycreature.getAttackTarget();

        if (attackTarget instanceof EntityPlayer) return entitycreature;
      }
    }

    return null;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.onGround && !this.tamed) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX = (d / f1) * 0.5D * 0.80000001192092896D + this.motionX * 0.20000000298023224D;
      this.motionZ = (d1 / f1) * 0.5D * 0.80000001192092896D + this.motionZ * 0.20000000298023224D;
      this.motionY = 0.40000000596046448D;
    } else if (this.tamed) {
      super.attackEntityAsMob(entity);
    }

    if ((this.getAttackTarget() instanceof EntityPlayer) && this.tamed) {
      this.setAttackTarget(null);
      super.attackEntityAsMob(entity);
    } else if ((this.getAttackTarget() instanceof CREEPSEntitySnowDevil) && this.tamed) {
      this.setAttackTarget(null);
    } else {
      super.attackEntityAsMob(entity);
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

      if (entity != this) {
        this.setAttackTarget((EntityLivingBase) entity);
      }

      return true;
    } else return false;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("Interest", this.interest);
    nbttagcompound.setBoolean("Tamed", this.tamed);
    nbttagcompound.setString("Name", this.name);
    nbttagcompound.setInteger("BaseHealth", this.basehealth);
    nbttagcompound.setString("BaseTexture", this.basetexture);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.interest = nbttagcompound.getInteger("Interest");
    this.tamed = nbttagcompound.getBoolean("Tamed");
    this.name = nbttagcompound.getString("Name");
    this.basetexture = nbttagcompound.getString("BaseTexture");
    this.basehealth = nbttagcompound.getInteger("BaseHealth");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    this.texture = this.basetexture;
  }

  private void explode() {
    float f = 2.0F;
    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, f, true);
  }

  private void smoke() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 10; j++) {
        double d = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(
            EnumParticleTypes.EXPLOSION_NORMAL,
            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            this.posY + this.rand.nextFloat() * this.height + i,
            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            d,
            d1,
            d2);
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
    Block j1 = this.worldObj.getBlock(i, j, k);
    return (i1 == Blocks.snow || j1 == Blocks.snow)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.checkBlockCollision(this.getBoundingBox())
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(5) == 0
        && l > 6;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 2;
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
      MoreCreepsAndWeirdos.guiHandler.tryOpenGui(
          CREEPSGuiHandler.GuiType.ZEBRA_NAME.id, entityplayer, this.worldObj, this.getEntityId());
    }

    if (itemstack != null) {
      if (this.tamed && this.texture.length() == 2222) {
        this.armor = Item.getIdFromItem(itemstack.getItem());
        this.smoke();

        if (this.armor > 297 && this.armor < 302) {
          this.used = true;
          this.basehealth += 5;
          this.attackStrength++;
          this.health = this.basehealth;
          this.setHealth(this.health);
          String s = this.basetexture.substring(0, 18);
          s = (new StringBuilder()).append(s).append("L.png").toString();
          this.texture = s;
          this.smoke();
        }

        if (this.armor > 313 && this.armor < 318) {
          this.used = true;
          this.basehealth += 10;
          this.attackStrength += 2;
          this.health = this.basehealth;
          this.setHealth(this.health);
          String s1 = this.basetexture.substring(0, 18);
          s1 = (new StringBuilder()).append(s1).append("G.png").toString();
          this.texture = s1;
          this.smoke();
        }

        if (this.armor > 305 && this.armor < 310) {
          this.used = true;
          this.basehealth += 20;
          this.health = this.basehealth;
          this.setHealth(this.health);
          this.attackStrength += 4;
          String s2 = this.basetexture.substring(0, 18);
          s2 = (new StringBuilder()).append(s2).append("I.png").toString();
          this.texture = s2;
          this.smoke();
        }

        if (this.armor > 309 && this.armor < 314) {
          this.smoke();
          this.used = true;
          this.basehealth += 30;
          this.health = this.basehealth;
          this.setHealth(this.health);
          this.attackStrength += 10;
          String s3 = this.basetexture.substring(0, 18);
          s3 = (new StringBuilder()).append(s3).append("D.png").toString();
          this.texture = s3;
          this.smoke();
        }
      }

      if (itemstack.getItem() == Items.snowball) {

        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        entityplayer.addStat(MoreCreepsAndWeirdos.achievesnowdevil, 1);
        // this.playermp.addStat(MoreCreepsAndWeirdos.achievesnowdevil, 1);
        this.confetti();

        this.used = true;
        this.health += 2;
        this.smoke();
        this.smoke();
        this.tamed = true;
        this.health = this.basehealth;
        this.setHealth(this.health);

        if (this.name.length() < 1) {
          this.name = Names[this.rand.nextInt(Names.length)];
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:snowdeviltamed",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }

      if (this.health > this.basehealth) {
        this.health = this.basehealth;
      }

      if (this.used) {
        if (itemstack.stackSize - 1 == 0) {
          entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        } else {
          itemstack.stackSize--;
        }
      }

      return true;
    } else return false;
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
    return "morecreeps:snowdevil";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:snowdevilhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:snowdevildeath";
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    // if (this.tamed && this.health > 0) return;

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

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    if (this.tamed && this.health > 0) {
      this.isDead = false;
      this.deathTime = 0;
      return;
    } else {
      super.setDead();
      return;
    }
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
}
