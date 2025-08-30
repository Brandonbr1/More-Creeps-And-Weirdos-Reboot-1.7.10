package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuy;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTombstone;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityHotdog extends EntityMob {

  EntityPlayer entityplayer;
  public boolean rideable;
  public String texture;
  public int interest;
  private boolean primed;
  public boolean tamed;
  public int basehealth;
  private float distance;
  public int armor;
  public String basetexture;
  public boolean used;
  public boolean grab;
  public List<?> piglist;
  public int pigstack;
  public int level;
  public float totaldamage;
  public boolean alt;
  public boolean heavenbuilt;
  public int wanderstate;
  public int speedboost;
  public int totalexperience;
  public float baseSpeed;
  public Entity preventity;
  public boolean angrydog;
  public int skillattack;
  public int skilldefend;
  public int skillhealing;
  public int skillspeed;
  public int firenum;
  public int firepower;
  public int healtimer;
  public EntityPlayer owner;
  public int health;
  public double moveSpeed;

  /** How much damage this mob's attacks deal */
  public int attackStrength;

  public float dogsize;
  public int unmounttimer;
  public boolean chunky;
  public String name;
  static final String Names[] = {
    "Pogo",
    "Spot",
    "King",
    "Prince",
    "Bosco",
    "Ralph",
    "Wendy",
    "Trixie",
    "Bowser",
    "The Heat",
    "Weiner",
    "Wendon the Weiner",
    "Wallace the Weiner",
    "William the Weiner",
    "Terrance",
    "Elijah",
    "Good Boy",
    "Boy",
    "Girl",
    "Tennis Shoe",
    "Rusty",
    "Mean Joe Green",
    "Lawrence",
    "Foxy",
    "SlyFoxHound",
    "Leroy Brown"
  };
  static final int firechance[] = {0, 20, 30, 40, 60, 90};
  static final int firedamage[] = {0, 15, 25, 50, 100, 200};
  static final int firenumber[] = {0, 1, 1, 2, 3, 4};
  static final String dogTextures[] = {
    "morecreeps:textures/entity/hotdg1.png",
    "morecreeps:textures/entity/hotdg2.png",
    "morecreeps:textures/entity/hotdg3.png"
  };
  public static final int leveldamage[] = {
    0, 50, 100, 250, 500, 800, 1200, 1700, 2200, 2700, 3300, 3900, 4700, 5400, 6200, 7000, 7900,
    8800, 9750, 10750, 12500, 17500, 22500, 30000, 40000, 50000, 60000
  };
  public static final String levelname[] = {
    "Just A Pup",
    "Hotdog",
    "A Dirty Dog",
    "An Alley Dog",
    "Scrapyard Puppy",
    "Army Dog",
    "Private",
    "Private First Class",
    "Corporal",
    "Sergeant",
    "Staff Sergeant",
    "Sergeant First Class",
    "Master Segeant",
    "First Sergeant",
    "Sergeant Major",
    "Command Sergeant Major",
    "Second Lieutenant",
    "First Lieutenant",
    "Captain",
    "Major",
    "Lieutenant Colonel",
    "Colonel",
    "General of the Hotdog Army",
    "General of the Hotdog Army",
    "Sparky the Wonder Pooch",
    "Sparky the Wonder Pooch"
  };

  public CREEPSEntityHotdog(World world) {
    super(world);
    this.primed = false;
    this.basetexture = dogTextures[this.rand.nextInt(dogTextures.length)];
    this.texture = this.basetexture;
    this.setSize(0.5F, 0.75F);
    this.rideable = false;
    this.basehealth = this.rand.nextInt(15) + 5;
    this.health = this.basehealth;
    this.attackStrength = 1;
    this.tamed = false;
    this.name = "";
    this.level = 1;
    this.totaldamage = 0.0F;
    this.alt = false;
    this.heavenbuilt = false;
    this.wanderstate = 0;
    this.baseSpeed = 0.76F;
    this.moveSpeed = this.baseSpeed;
    this.speedboost = 0;
    this.totalexperience = 0;
    this.fallDistance = -25F;
    this.isImmuneToFire = true;
    this.angrydog = false;
    this.unmounttimer = 0;
    this.skillattack = 0;
    this.skilldefend = 0;
    this.skillhealing = 0;
    this.skillspeed = 0;
    this.firepower = 0;
    this.healtimer = 600;
    this.dogsize = 0.6F;
    this.chunky = false;
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(3, new EntityAILookIdle(this));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.76f);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return !this.tamed;
  }

  @Override
  protected void updateAITick() {
    super.updateEntityActionState();
    this.setFire(0);

    if (this.tamed && this.wanderstate == 0) {
      this.firenum = 0;
      List<?> list =
          this.worldObj.getEntitiesWithinAABBExcludingEntity(
              this, this.boundingBox.expand(16D, 16D, 16D));

      for (int i = 0; i < list.size(); i++) {
        Entity entity = (Entity) list.get(i);

        if (!(entity instanceof EntityCreature)) {
          continue;
        }

        EntityCreature entitycreature = (EntityCreature) entity;

        if (!(entitycreature.getAttackTarget() instanceof EntityPlayer)
            || (entitycreature instanceof CREEPSEntityHotdog)
            || (entitycreature instanceof CREEPSEntityHunchback)
            || (entitycreature instanceof CREEPSEntityGuineaPig)
            || (entitycreature instanceof CREEPSEntityArmyGuy)
                && ((CREEPSEntityArmyGuy) entitycreature).loyal) {
          continue;
        }

        if (this.rand.nextInt(100) < firechance[this.skillattack]
            && this.firenum < firenumber[this.skillattack]
            && this.firepower >= 25) {
          float f1 = this.getDistanceToEntity(entitycreature);

          if (f1 < this.skillattack + 1
              && this.rand.nextInt(100) < firechance[this.skillattack]
              && entitycreature.isBurning()) {
            this.firepower -= 25;

            if (this.firepower < 0) {
              this.firepower = 0;
            }

            entitycreature.setFire(firedamage[this.skillattack]);
            this.firenum++;
          }
        }

        this.getAttackTarget();
      }
    }

    if (!this.hasPath() && this.tamed && this.ridingEntity == null && this.wanderstate != 2) {

      if (this.entityplayer != null) {
        float f = this.entityplayer.getDistanceToEntity(this);

        if (f <= 5F) {;
        }
      }
    }

    if (this.getAttackTarget() instanceof EntityPlayer) {

      if (this.getDistanceToEntity(this.entityplayer) < 6F) {
        this.setAttackTarget(null);
      }
    }

    if (this.health < this.basehealth * (0.1F * this.skillhealing) && this.skillhealing > 0) {
      this.setAttackTarget(this.entityplayer);
    }
  }

  @Override
  public float getEyeHeight() {
    return this.height * 0.5F;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (this.riddenByEntity != null && this.ridingEntity == null) {
      if (this.ridingEntity != null) {
        this.ridingEntity.mountEntity(null);
      }

      if (this.riddenByEntity != null) {
        this.riddenByEntity.mountEntity(null);
      }

      this.mountEntity(null);
    }

    if (this.ridingEntity != null
        && !(this.ridingEntity instanceof EntityPlayer)
        && !(this.ridingEntity instanceof CREEPSEntityGuineaPig)
        && !(this.ridingEntity instanceof CREEPSEntityHotdog)
        && !(this.ridingEntity instanceof CREEPSEntityDoghouse)) {
      this.mountEntity(null);
      this.unmounttimer = 20;
    }

    if (this.healtimer-- < 1 && this.health < this.basehealth && this.skillhealing > 0) {
      this.healtimer = (6 - this.skillhealing) * 200;
      this.health += this.skillhealing;

      if (this.health > this.basehealth) {
        this.health = this.basehealth;
      }

      for (int i = 0; i < this.skillhealing; i++) {
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d3 = this.rand.nextGaussian() * 0.02D;
        double d5 = this.rand.nextGaussian() * 0.02D;
        if (this.worldObj.isRemote) {
          this.worldObj.spawnParticle(
              "heart",
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + 0.5D + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d1,
              d3,
              d5);
        }
      }
    }

    if (this.handleWaterMovement()) {
      this.motionY += 0.22879999876022339D;
      double d = this.skillspeed / 2.5D;

      if (d < 1.0D) {
        d = 1.0D;
      }

      this.motionX *= d;
      this.motionZ *= d;
    }

    if (this.firepower >= 25 && this.tamed && this.prevPosX != this.posX) {
      int j = this.rand.nextInt(4);
      double d2 = (float) this.posX + 0.125F;
      double d4 = (float) this.posY + 0.4F;
      double d6 = (float) this.posZ + 0.5F;
      double d7 = 0.2199999988079071D;
      double d8 = 0.27000001072883606D;

      if (j == 1) {
        if (this.worldObj.isRemote) {
          this.worldObj.spawnParticle("smoke", d2 - d8, d4 + d7, d6, 0.0D, 0.0D, 0.0D);
          this.worldObj.spawnParticle("flame", d2 - d8, d4 + d7, d6, 0.0D, 0.0D, 0.0D);
        }
      } else if (j != 2 && j == 3) {
        if (this.worldObj.isRemote) {
          this.worldObj.spawnParticle("smoke", d2, d4 + d7, d6 - d8, 0.0D, 0.0D, 0.0D);
          this.worldObj.spawnParticle("flame", d2, d4 + d7, d6 - d8, 0.0D, 0.0D, 0.0D);
        }
      }
    }

    if (this.wanderstate == 0 && !this.hasPath() && this.angrydog) {
      EntityLivingBase entityliving = this.getAITarget();

      if (entityliving instanceof EntityPlayer) {
        EntityPlayer entityplayer = (EntityPlayer) entityliving;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && this.tamed) {
          if (itemstack.getItem() != Item.getItemFromBlock(Blocks.torch)) {;
          }
        }
      }
    }
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    Object obj = null;

    if (this.tamed && this.wanderstate == 0) {
      List<?> list =
          this.worldObj.getEntitiesWithinAABBExcludingEntity(
              this, this.getBoundingBox().expand(16D, 16D, 16D));

      for (int i = 0; i < list.size(); i++) {
        Entity entity = (Entity) list.get(i);

        if (entity instanceof EntityCreature) {
          EntityCreature entitycreature = (EntityCreature) entity;

          if ((entitycreature.getAttackTarget() instanceof EntityPlayer)
              && !(entitycreature instanceof CREEPSEntityHotdog)
              && !(entitycreature instanceof CREEPSEntityHunchback)
              && !(entitycreature instanceof CREEPSEntityGuineaPig)
              && (!(entitycreature instanceof CREEPSEntityArmyGuy)
                  || !((CREEPSEntityArmyGuy) entitycreature).loyal)) {
            obj = entitycreature;
          }
        }

        if (!(entity instanceof EntityPlayer)
            || this.wanderstate != 0
            || (this.getAttackTarget() instanceof EntityPlayer)) {
          continue;
        }

        EntityPlayer entityplayer = (EntityPlayer) entity;

        if (entityplayer == null || obj != null && !(obj instanceof EntityPlayer)) {
          continue;
        }

        this.distance = this.getDistanceToEntity(entityplayer);

        if (this.distance < 8F) {
          obj = null;
        } else {
          obj = entityplayer;
        }
      }
    }

    return ((Entity) (obj));
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.wanderstate == 0
        && !(this.getAttackTarget() instanceof EntityPlayer)
        && !(this.getAttackTarget() instanceof CREEPSEntityGuineaPig)
        && this.ridingEntity == null) {
      if (this.onGround && this.tamed) {
        double d = entity.posX - this.posX;
        double d2 = entity.posZ - this.posZ;
        float f2 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.motionX = (d / f2) * 0.5D * 0.88000001192092892D + this.motionX * 0.20000000298023224D;
        this.motionZ =
            (d2 / f2) * 0.5D * 0.88000001192092892D + this.motionZ * 0.20000000298023224D;
        this.motionY = 0.4200000059604645D;
      } else if (this.tamed && f < 3.3999999999999999D) {
        if (this.rand.nextInt(5) == 0) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:hotdogattack",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        double d1 = 1.0D;
        d1 += this.level * 5 + this.skillattack * 4;

        if (d1 < 5D) {
          d1 = 5D;
        }

        super.attackEntityAsMob(entity);

        if (this.rand.nextInt(100) < d1) {
          if (CREEPSConfig.Blood && this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.guineaPigParticles(this.worldObj, this);
          }

          float f1 = this.attackStrength * 0.25F;

          if (f1 < 1.0F) {
            f1 = 1.0F;
          }

          if (((EntityLiving) entity).getHealth() - f1 <= 0.0F) {
            this.worldObj.playSoundAtEntity(entity, "morecreeps:hotdogkill", 1.0F, 1.0F);
          }

          ((EntityLiving) entity)
              .attackEntityFrom(DamageSource.causeThrownDamage(this, entity), (int) f1);
          this.totaldamage += (int) (f1 * 1.5D + this.skillattack);
          this.totalexperience += (int) (f1 * 1.5D + this.skillattack);
        }

        if (this.totaldamage > leveldamage[this.level] && this.level < 25) {
          this.level++;
          this.totaldamage = 0.0F;
          this.dogsize += 0.05F;

          if (this.dogsize > 1.5D) {
            this.dogsize = 1.5F;
          }

          boolean flag = false;

          if (this.level == 5) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievehotdoglevel5, 1);
          }

          if (this.level == 10) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievehotdoglevel10, 1);
          }

          if (this.level == 20) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievehotdoglevel25, 1);
          }

          if (flag) {
            this.worldObj.playSoundAtEntity(
                this.entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            double d4 = -MathHelper.sin(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
            double d6 = MathHelper.cos(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
            CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
            creepsentitytrophy.setLocationAndAngles(
                (this.entityplayer).posX + d4 * 3D,
                (this.entityplayer).posY - 2D,
                (this.entityplayer).posZ + d6 * 3D,
                (this.entityplayer).rotationYaw,
                0.0F);
            this.worldObj.spawnEntityInWorld(creepsentitytrophy);
          }

          MoreCreepsAndWeirdos.proxy.addChatMessage(
              (new StringBuilder())
                  .append("\247b")
                  .append(this.name)
                  .append(" \247fincreased to level \2476")
                  .append(String.valueOf(this.level))
                  .append("!")
                  .toString());
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpiglevelup",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.baseSpeed += 0.025F;
          this.basehealth += this.rand.nextInt(4) + 1;
          this.attackStrength++;
        }

        double d3 = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
        double d5 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
        entity.motionX = d3 * 0.15000000596046448D;
        entity.motionZ = d5 * 0.15000000596046448D;
      }
    }
  }

  /** Called when the entity is attacked. */
  public boolean attackEntityFrom(DamageSource damagesource, int i) {
    Entity entity = damagesource.getEntity();

    if (entity != this.getAttackTarget()) {
      this.setAttackTarget((EntityLivingBase) entity);
    }

    if (this.rand.nextInt(100) < this.skilldefend * 10) {
      i--;

      if (this.skilldefend == 5) {
        i--;
      }

      this.worldObj.playSoundAtEntity(this.entityplayer, "morecreeps:hotdogwhoosh", 1.0F, 1.0F);
    }

    if (i < 1) {
      i = 1;
    }

    if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
      i = (i + 1) / 2;
    }

    if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
      if (!this.tamed && !this.angrydog) {
        if (entity instanceof EntityPlayer) {
          this.angrydog = true;
          this.setAttackTarget((EntityLivingBase) entity);
        }

        if ((entity instanceof EntityArrow) && ((EntityArrow) entity).shootingEntity != null) {
          entity = ((EntityArrow) entity).shootingEntity;
        }

        if (entity instanceof EntityLiving) {
          List<?> list =
              this.worldObj.getEntitiesWithinAABB(
                  CREEPSEntityHotdog.class,
                  AxisAlignedBB.getBoundingBox(
                          this.posX,
                          this.posY,
                          this.posZ,
                          this.posX + 1.0D,
                          this.posY + 1.0D,
                          this.posZ + 1.0D)
                      .expand(16D, 4D, 16D));
          Iterator<?> iterator = list.iterator();

          do {
            if (!iterator.hasNext()) {
              break;
            }

            Entity entity1 = (Entity) iterator.next();
            CREEPSEntityHotdog creepsentityhotdog = (CREEPSEntityHotdog) entity1;

            if (!creepsentityhotdog.tamed && creepsentityhotdog.getAttackTarget() == null) {
              entity = creepsentityhotdog.getAttackTarget();

              if (entity instanceof EntityPlayer) {
                creepsentityhotdog.angrydog = true;
              }
            }
          } while (true);
        }
      } else if (entity != this && entity != null) {
        if (this.tamed && (entity instanceof EntityPlayer)) return true;

        entity = this.getAttackTarget();
      }

      return true;
    } else return false;
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.ridingEntity != null || this.unmounttimer-- > 0) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setInteger("Interest", this.interest);
    nbttagcompound.setBoolean("Tamed", this.tamed);
    nbttagcompound.setString("Name", this.name);
    nbttagcompound.setInteger("BaseHealth", this.basehealth);
    nbttagcompound.setInteger("Level", this.level);
    nbttagcompound.setString("BaseTexture", this.basetexture);
    nbttagcompound.setFloat("TotalDamage", this.totaldamage);
    nbttagcompound.setBoolean("heavenbuilt", this.heavenbuilt);
    nbttagcompound.setInteger("AttackStrength", this.attackStrength);
    nbttagcompound.setInteger("WanderState", this.wanderstate);
    nbttagcompound.setInteger("SpeedBoost", this.speedboost);
    nbttagcompound.setInteger("TotalExperience", this.totalexperience);
    nbttagcompound.setFloat("BaseSpeed", this.baseSpeed);
    nbttagcompound.setInteger("SkillAttack", this.skillattack);
    nbttagcompound.setInteger("SkillDefense", this.skilldefend);
    nbttagcompound.setInteger("SkillHealing", this.skillhealing);
    nbttagcompound.setInteger("SkillSpeed", this.skillspeed);
    nbttagcompound.setInteger("FirePower", this.firepower);
    nbttagcompound.setFloat("DogSize", this.dogsize);
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
    this.level = nbttagcompound.getInteger("Level");
    this.totaldamage = nbttagcompound.getFloat("TotalDamage");
    this.heavenbuilt = nbttagcompound.getBoolean("heavenbuilt");
    this.attackStrength = nbttagcompound.getInteger("AttackStrength");
    this.wanderstate = nbttagcompound.getInteger("WanderState");
    this.speedboost = nbttagcompound.getInteger("SpeedBoost");
    this.totalexperience = nbttagcompound.getInteger("TotalExperience");
    this.baseSpeed = nbttagcompound.getFloat("BaseSpeed");
    this.skillattack = nbttagcompound.getInteger("SkillAttack");
    this.skilldefend = nbttagcompound.getInteger("SkillDefense");
    this.skillhealing = nbttagcompound.getInteger("SkillHealing");
    this.skillspeed = nbttagcompound.getInteger("SkillSpeed");
    this.firepower = nbttagcompound.getInteger("FirePower");
    this.dogsize = nbttagcompound.getFloat("DogSize");

    if (this.dogsize < 0.7F) {
      this.dogsize = 0.7F;
    }

    this.texture = this.basetexture;

    if (this.wanderstate == 1) {
      this.moveSpeed = 0.0F;
    } else {
      this.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.5F;
    }
  }

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    if (this.interest == 0 || this.health <= 0) {
      if (this.tamed) {
        CREEPSEntityTombstone creepsentitytombstone = new CREEPSEntityTombstone(this.worldObj);
        creepsentitytombstone.setLocationAndAngles(
            this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
        creepsentitytombstone.interest = this.interest;
        creepsentitytombstone.tamed = this.tamed;
        creepsentitytombstone.name = this.name;
        creepsentitytombstone.basehealth = this.basehealth;

        if (this.level > 1) {
          this.level--;
        }

        creepsentitytombstone.level = this.level;
        creepsentitytombstone.basetexture = this.basetexture;
        creepsentitytombstone.totaldamage = 0.0F;
        creepsentitytombstone.heavenbuilt = this.heavenbuilt;
        creepsentitytombstone.attackStrength = this.attackStrength;
        creepsentitytombstone.wanderstate = this.wanderstate;
        creepsentitytombstone.speedboost = this.speedboost;
        creepsentitytombstone.totalexperience = this.totalexperience;
        creepsentitytombstone.baseSpeed = this.baseSpeed;
        creepsentitytombstone.skillattack = this.skillattack;
        creepsentitytombstone.skilldefend = this.skilldefend;
        creepsentitytombstone.skillhealing = this.skillhealing;
        creepsentitytombstone.skillspeed = this.skillspeed;
        creepsentitytombstone.firepower = this.firepower;
        creepsentitytombstone.dogsize = this.dogsize;
        creepsentitytombstone.deathtype = "Hotdog";
        this.worldObj.spawnEntityInWorld(creepsentitytombstone);
      }

      super.setDead();
    } else {
      this.isDead = false;
      this.deathTime = 0;
      return;
    }
  }

  private void explode() {
    float f = 2.0F;
    this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, f, true);
  }

  private void smoke() {
    for (int i = 0; i < 7; i++) {
      double d = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      double d4 = this.rand.nextGaussian() * 0.02D;
      this.worldObj.spawnParticle(
          "heart",
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
            "explode",
            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            this.posY + this.rand.nextFloat() * this.height + j,
            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            d1,
            d3,
            d5);
      }
    }
  }

  private void smokePlain() {
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

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.getBoundingBox().minY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j - 1, k);
    return i1 != Blocks.sand
        && i1 != Blocks.cobblestone
        && this.worldObj.checkBlockCollision(this.getBoundingBox())
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(5) == 0
        && l > 8;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 2;
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof EntityPlayer) return this.getYOffset() - 1.15F;
    else return this.getYOffset();
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
      entityplayer.openGui(
          MoreCreepsAndWeirdos.INSTANCE,
          CREEPSGuiHandler.GuiType.HOTDOG.id,
          this.worldObj,
          this.getEntityId(),
          0,
          0);
      return true;
    }

    if (itemstack == null && this.tamed && this.health > 0) {
      this.rotationYaw = entityplayer.rotationYaw;
      Object obj = entityplayer;

      if (this.ridingEntity != obj) {
        int k;

        for (k = 0; ((Entity) obj).riddenByEntity != null && k < 20; k++) {
          obj = ((Entity) obj).riddenByEntity;
        }

        if (k < 20) {
          this.rotationYaw = ((Entity) obj).rotationYaw;
          this.mountEntity((Entity) obj);
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:hotdogpickup",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      } else {
        int l;

        for (l = 0; ((Entity) obj).riddenByEntity != null && l < 20; l++) {
          obj = ((Entity) obj).riddenByEntity;
        }

        if (l < 20) {
          this.rotationYaw = ((Entity) obj).rotationYaw;
          ((Entity) obj).fallDistance = -25F;
          ((Entity) obj).mountEntity(null);

          if ((Entity) obj instanceof CREEPSEntityHotdog) {
            ((CREEPSEntityHotdog) obj).unmounttimer = 20;
          }

          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:hotdogputdown",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      }
    }

    if (itemstack != null && this.health > 0) {
      if (itemstack.getItem() == Items.redstone && this.tamed) {
        this.firepower += 250;
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogredstone",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }

      if ((itemstack.getItem() == Items.book
              || itemstack.getItem() == Items.paper
              || itemstack.getItem() == MoreCreepsAndWeirdos.guineapigradio)
          && this.tamed) {
        entityplayer.openGui(
            MoreCreepsAndWeirdos.INSTANCE,
            CREEPSGuiHandler.GuiType.GUINEA_TRAIN.id,
            this.worldObj,
            this.getEntityId(),
            0,
            0);
      }

      if (itemstack.getItem() == Items.diamond && this.tamed) {
        if (!this.heavenbuilt) {
          if (this.level >= 25) {
            int i = MathHelper.floor_double(entityplayer.posX);
            int i1 = MathHelper.floor_double(entityplayer.getBoundingBox().minY);
            int j1 = MathHelper.floor_double(entityplayer.posZ);
            this.buildHeaven(entityplayer, i + 1, i1, j1 + 1);

            if (this.heavenbuilt) {
              this.confetti();
              this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
              entityplayer.addStat(MoreCreepsAndWeirdos.achievehotdogheaven, 1);
            }
          } else {
            this.worldObj.playSoundAtEntity(
                this,
                "morecreeps:hotdoglevelneeded",
                1.0F,
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            MoreCreepsAndWeirdos.proxy.addChatMessage(
                "Your Hotdog must be level 25 to build Hot Dog Heaven.");
            MoreCreepsAndWeirdos.proxy.addChatMessage(
                (new StringBuilder())
                    .append("\247b")
                    .append(this.name)
                    .append(" is only level \247f")
                    .append(String.valueOf(this.level))
                    .toString());
          }
        } else {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              (new StringBuilder())
                  .append("\247b")
                  .append(this.name)
                  .append("\247f has already built Hot Dog Heaven.")
                  .toString());
        }
      }

      if (Item.getIdFromItem(itemstack.getItem()) == 37 && this.tamed
          || Item.getIdFromItem(itemstack.getItem()) == 38 && this.tamed) {
        this.smokePlain();

        if (this.wanderstate == 0) {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              (new StringBuilder())
                  .append("\2473")
                  .append(this.name)
                  .append("\2476 will \2474STAY\2476 right here.")
                  .toString());
          this.wanderstate = 1;
          this.moveSpeed = 0.0F;
        } else if (this.wanderstate == 1) {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              (new StringBuilder())
                  .append("\2473")
                  .append(this.name)
                  .append("\2476 will \247dWANDER\2476 around and have fun.")
                  .toString());
          this.wanderstate = 2;
          this.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.5F;
        } else if (this.wanderstate == 2) {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              (new StringBuilder())
                  .append("\2473")
                  .append(this.name)
                  .append("\2476 will \2472FIGHT\2476 and follow you!")
                  .toString());
          this.wanderstate = 0;
          this.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.5F;
        }
      }

      if (itemstack.getItem() == Items.reeds && this.tamed) {
        this.smokePlain();
        this.used = true;

        if (this.speedboost < 0) {
          this.speedboost = 0;
        }

        this.speedboost += 13000;

        if (this.wanderstate != 1) {
          this.moveSpeed = this.baseSpeed + 0.5F;
        }

        int j = this.speedboost / 21;
        j /= 60;
        String s = "";

        if (j < 0) {
          j = 0;
        }

        if (j > 1) {
          s = "s";
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:guineapigspeedup",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        MoreCreepsAndWeirdos.proxy.addChatMessage(
            (new StringBuilder())
                .append("\2473")
                .append(this.name)
                .append("\247f ")
                .append(String.valueOf(j))
                .append("\2476 minute")
                .append(s)
                .append(" of speedboost left.")
                .toString());
      }

      if (itemstack.getItem() == Items.egg) {
        this.used = true;
        this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
        this.setLocationAndAngles(
            entityplayer.posX,
            entityplayer.posY + entityplayer.getEyeHeight(),
            entityplayer.posZ,
            entityplayer.rotationYaw,
            entityplayer.rotationPitch);
        this.motionX =
            -MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        this.motionZ =
            MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI)
                * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        double d = this.motionX / 100D;
        double d1 = this.motionZ / 100D;

        for (int l1 = 0; l1 < 2000; l1++) {
          this.moveEntity(d, 0.0D, d1);
          double d2 = this.rand.nextGaussian() * 0.02D;
          double d3 = this.rand.nextGaussian() * 0.02D;
          double d4 = this.rand.nextGaussian() * 0.02D;
          this.worldObj.spawnParticle(
              "explode",
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d2,
              d3,
              d4);
        }

        this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, 1.1F, true);
        this.interest = 0;
        this.health = 0;
        this.setDead();
      } else {
        this.primed = false;
      }

      byte byte0 = 0;
      byte byte1 = 0;

      if (this.tamed && this.texture.length() == 37) {
        // Add 14 because texture string is longer
        String s1 = this.basetexture.substring(18 + 14, 19 + 14);
        char c = s1.charAt(0);

        if (c == 'L') {
          byte0 = 5;
          byte1 = 1;
        }

        if (c == 'I') {
          byte0 = 9;
          byte1 = 2;
        }

        if (c == 'G') {
          byte0 = 15;
          byte1 = 3;
        }

        if (c == 'D') {
          byte0 = 22;
          byte1 = 6;
        }
      }

      if (this.tamed) {
        this.armor = Item.getIdFromItem(itemstack.getItem());
        this.smoke();
        int k1 = 0;

        if (this.armor > 297 + k1 && this.armor < 302 + k1) {
          this.used = true;
          this.basehealth += 5 - byte0;
          this.attackStrength += 1 - byte1;
          this.health = this.basehealth;
          String s2 = this.basetexture.substring(0, 18 + 14);
          s2 = (new StringBuilder()).append(s2).append("L.png").toString();
          this.texture = s2;
          this.basetexture = s2;
          this.smoke();
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigarmor",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        if (this.armor > 313 + k1 && this.armor < 318 + k1) {
          this.used = true;
          this.basehealth += 15 - byte0;
          this.attackStrength += 3 - byte1;
          this.health = this.basehealth;
          String s3 = this.basetexture.substring(0, 18 + 14);
          s3 = (new StringBuilder()).append(s3).append("G.png").toString();
          this.texture = s3;
          this.basetexture = s3;
          this.smoke();
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigarmor",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        if (this.armor > 305 + k1 && this.armor < 310 + k1) {
          this.used = true;
          this.basehealth += 9 - byte0;
          this.attackStrength += 2 - byte1;
          this.health = this.basehealth;
          String s4 = this.basetexture.substring(0, 18 + 14);
          s4 = (new StringBuilder()).append(s4).append("I.png").toString();
          this.texture = s4;
          this.basetexture = s4;
          this.smoke();
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigarmor",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        if (this.armor > 309 + k1 && this.armor < 314 + k1) {
          this.used = true;
          this.basehealth += 22 - byte0;
          this.attackStrength += 6 - byte1;
          this.health = this.basehealth;
          String s5 = this.basetexture.substring(0, 18 + 14);
          s5 = (new StringBuilder()).append(s5).append("D.png").toString();
          this.texture = s5;
          this.basetexture = s5;
          this.smoke();
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigarmor",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      }

      if (itemstack.getItem() == Items.bone) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 15;
        this.health += 10;
        this.isDead = false;
        this.smoke();
      }

      if (itemstack.getItem() == Items.porkchop) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 30;
        this.health += 15;
        this.isDead = false;
        this.smoke();
      }

      if (itemstack.getItem() == Items.cooked_porkchop) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 55;
        this.health += 25;
        this.isDead = false;
        this.smoke();
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

      if (!this.primed && this.interest > 100) {

        this.confetti();
        this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        entityplayer.addStat(MoreCreepsAndWeirdos.achievehotdogtaming, 1);

        if (this.used) {
          this.smoke();
        }

        this.tamed = true;

        this.owner = entityplayer;

        if (this.name.length() < 1) {
          this.name = Names[this.rand.nextInt(Names.length)];
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:hotdogtamed",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.interest = 100;
      }

      return true;
    } else return false;
  }

  public void buildHeaven(EntityPlayer entityplayer, int i, int j, int k) {
    if (j > 95) {
      MoreCreepsAndWeirdos.proxy.addChatMessage("You are too far up to build Hotdog Heaven!");
      return;
    }

    byte byte0 = 40;
    byte byte1 = 40;
    boolean flag = false;
    int l = (105 - j) / 2;
    int i1 = 0;

    for (int j1 = 0; j1 < l * 2; j1++) {
      for (int i5 = -2; i5 < byte0 + 2; i5++) {
        for (int j7 = -2; j7 < byte1 + 2; j7++) {
          if (Block.getIdFromBlock(this.worldObj.getBlock(i + i5, j + j1, k + j7)) != 0) {
            i1++;
          }
        }
      }
    }

    if (this.worldObj.blockExists(i - byte0 / 2, j, k - byte1 / 2)
        && this.worldObj.blockExists(i + byte0, j, k)
        && this.worldObj.blockExists(i + byte0, j, k + byte1)
        && this.worldObj.blockExists(i, j, k - byte1)) {
      this.chunky = true;
    }

    if (i1 < 3000 && this.chunky) {
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:hotdogheaven",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.used = true;
      this.heavenbuilt = true;
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:guineapighotel",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      MoreCreepsAndWeirdos.proxy.addChatMessage("HOT DOG HEAVEN HAS BEEN BUILT!");
      // worldObj.setBlockWithNotify(i, j, k, Block.planks.blockID);
      this.worldObj.setBlock(i, j, k, Blocks.planks);
      // worldObj.setBlockWithNotify(i, j + 1, k, Block.torchWood.blockID);
      this.worldObj.setBlock(i, j + 1, k, Blocks.torch);
      // worldObj.setBlockWithNotify(i + 5, j, k, Block.planks.blockID);
      this.worldObj.setBlock(i + 5, j, k, Blocks.planks);
      // worldObj.setBlockWithNotify(i + 5, j + 1, k, Block.torchWood.blockID);
      this.worldObj.setBlock(i + 5, j + 1, k, Blocks.planks);

      for (int k1 = 0; k1 < l; k1++) {
        for (int j5 = 0; j5 < 4; j5++) {
          // worldObj.setBlockWithNotify(i + j5 + 1, j + k1, k + k1,
          // Block.stairCompactPlanks.blockID);
          this.worldObj.setBlock(i + j5 + 1, j + k1, k + k1, Blocks.oak_stairs);
          // worldObj.setBlockMetadataWithNotify(i + j5 + 1, j + k1, k + k1, 2);
          this.worldObj.setBlock(i + j5 + 1, j + k1, k + k1, Blocks.grass);
        }
      }

      for (int l1 = 0; l1 < l - 1; l1++) {
        for (int k5 = 0; k5 < 4; k5++) {
          // worldObj.setBlockWithNotify(i - k5, j + l + l1, (k + l) - l1,
          // Block.stairCompactPlanks.blockID);
          this.worldObj.setBlock(i - k5, j + l + l1, (k + l) - l1, Blocks.oak_stairs);
          // worldObj.setBlockMetadataWithNotify(i - k5, j + l + l1, (k + l) - l1, 3);
          this.worldObj.setBlock(i - k5, j + l + l1, (k + l) - l1, Blocks.dirt);
        }
      }

      for (int i2 = 0; i2 < 10; i2++) {
        // worldObj.setBlockWithNotify((i - i2) + 5, j + l, k + l + 6, Block.fence.blockID);
        this.worldObj.setBlock((i - i2) + 5, j + l, k + l + 6, Blocks.fence);

        for (int l5 = 0; l5 < 7; l5++) {
          // worldObj.setBlockWithNotify(i + 5, j + l, k + l + l5, Block.fence.blockID);
          this.worldObj.setBlock(i + 5, j + l, k + l + l5, Blocks.fence);
          // worldObj.setBlockWithNotify(i - 4, j + l, k + l + l5, Block.fence.blockID);
          this.worldObj.setBlock(i - 4, j + l, k + l + l5, Blocks.fence);
          flag = !flag;

          if (flag) {
            // worldObj.setBlockWithNotify(i + 5, j + l + 1, k + l + l5, Block.torchWood.blockID);
            this.worldObj.setBlock(i + 5, j + l + 1, k + l + l5, Blocks.torch);
          }

          if (flag) {
            // worldObj.setBlockWithNotify(i - 4, j + l + 1, k + l + l5, Block.torchWood.blockID);
            this.worldObj.setBlock(i - 4, j + l + 1, k + l + l5, Blocks.torch);
          }

          // worldObj.setBlockWithNotify((i - i2) + 5, (j + l) - 1, k + l + l5,
          // Block.planks.blockID);
          this.worldObj.setBlock((i - i2) + 5, (j + l) - 1, k + l + l5, Blocks.planks);
        }
      }

      for (int j2 = 0; j2 < byte0; j2++) {
        for (int i6 = 0; i6 < byte1; i6++) {
          for (int k7 = 0 - this.rand.nextInt(3) - 2; k7 < 1; k7++) {
            if (k7 < 0) {
              // worldObj.setBlockWithNotify((i + j2) - byte0 / 2, (j + l * 2 + k7) - 2, ((k + i6) -
              // byte1) + 2, Block.dirt.blockID);
              this.worldObj.setBlock(
                  (i + j2) - byte0 / 2, (j + l * 2 + k7) - 2, ((k + i6) - byte1), Blocks.dirt);
            } else {
              // worldObj.setBlockWithNotify((i + j2) - byte0 / 2, (j + l * 2 + k7) - 2, ((k + i6) -
              // byte1) + 2, Block.grass.blockID);
              this.worldObj.setBlock(
                  (i + j2) - byte0 / 2, (j + l * 2 + k7) - 2, ((k + i6) - byte1) + 2, Blocks.grass);
            }
          }
        }
      }

      for (int k2 = 0; k2 < this.rand.nextInt(10) + 2; k2++) {
        // worldObj.setBlockWithNotify((i + rand.nextInt(byte0 - 10)) - byte0 / 2, (j + l * 2) - 1,
        // (k +
        // rand.nextInt(byte1 - 6)) - byte1, 32);
        this.worldObj.setBlock(
            (i + this.rand.nextInt(byte0 - 10)) - byte0 / 2,
            (j + l * 2) - 1,
            (k + this.rand.nextInt(byte1 - 6)),
            Blocks.deadbush);
      }

      for (int l2 = 0; l2 < this.rand.nextInt(10) + 2; l2++) {
        // worldObj.setBlockWithNotify((i + rand.nextInt(byte0 - 10)) - byte0 / 2, (j + l * 2) - 1,
        // (k +
        // rand.nextInt(byte1 - 6)) - byte1, 37);
        this.worldObj.setBlock(
            (i + this.rand.nextInt(byte0 - 10)) - byte0 / 2,
            (j + l * 2) - 1,
            (k + this.rand.nextInt(byte1 - 6)),
            Blocks.yellow_flower);
      }

      for (int i3 = 0; i3 < this.rand.nextInt(10) + 2; i3++) {
        // worldObj.setBlockWithNotify((i + rand.nextInt(byte0 - 10)) - byte0 / 2, (j + l * 2) - 1,
        // (k +
        // rand.nextInt(byte1 - 6)) - byte1, 38);
        this.worldObj.setBlock(
            (i + this.rand.nextInt(byte0 - 10)) - byte0 / 2,
            (j + l * 2) - 1,
            (k + this.rand.nextInt(byte1 - 6)) - byte1,
            Blocks.red_flower);
      }

      for (int j3 = 0; j3 < this.rand.nextInt(30) + 2; j3++) {
        int j6 = this.rand.nextInt(byte0 - 12);
        int l7 = this.rand.nextInt(byte1 - 8);

        if (Block.getIdFromBlock(
                this.worldObj.getBlock((i + j6) - byte0 / 2, (j + l * 2) - 1, (k + l7) - byte1))
            == 0) {
          // worldObj.setBlockWithNotify((i + j6) - byte0 / 2, (j + l * 2) - 1, (k + l7) - byte1,
          // 31);
          this.worldObj.setBlock(
              (i + j6) - byte0 / 2, (j + l * 2) - 1, (k + l7) - byte1, Blocks.deadbush);
          // worldObj.setBlockMetadataWithNotify((i + j6) - byte0 / 2, (j + l * 2) - 1, (k + l7) -
          // byte1, 2);
          this.worldObj.setBlock(
              (i + j6) - byte0 / 2, (j + l * 2) - 1, (k + l7) - byte1, Blocks.grass);
        }
      }

      for (int k3 = 0; k3 < this.rand.nextInt(50) + 2; k3++) {
        int k6 = this.rand.nextInt(byte0 - 12);
        int i8 = this.rand.nextInt(byte1 - 8);

        if (Block.getIdFromBlock(
                this.worldObj.getBlock((i + k6) - byte0 / 2, (j + l * 2) - 1, (k + i8) - byte1))
            == 0) {
          // worldObj.setBlockWithNotify((i + k6) - byte0 / 2, (j + l * 2) - 1, (k + i8) - byte1,
          // 31);
          this.worldObj.setBlock(
              (i + k6) - byte0 / 2, (j + l * 2) - 1, (k + i8) - byte1, Blocks.deadbush);
          // worldObj.setBlockMetadataWithNotify((i + k6) - byte0 / 2, (j + l * 2) - 1, (k + i8) -
          // byte1, 1);
          this.worldObj.setBlock(
              (i + k6) - byte0 / 2, (j + l * 2) - 1, (k + i8) - byte1, Blocks.stone);
        }
      }

      for (int l3 = 1; l3 < byte0 - 1; l3++) {
        // worldObj.setBlockWithNotify((i + l3) - byte0 / 2, (j + l * 2) - 1, (k - byte1) + 3,
        // Block.fence.blockID);
        this.worldObj.setBlock(
            (i + l3) - byte0 / 2, (j + l * 2) - 1, (k - byte1) + 3, Blocks.fence);
        // worldObj.setBlockWithNotify((i + l3) - byte0 / 2, (j + l * 2) - 1, k,
        // Block.fence.blockID);
        this.worldObj.setBlock((i + l3) - byte0 / 2, (j + l * 2) - 1, k, Blocks.fence);
        flag = !flag;

        if (flag) {
          // worldObj.setBlockWithNotify((i + l3) - byte0 / 2, j + l * 2, (k - byte1) + 3,
          // Block.torchWood.blockID);
          this.worldObj.setBlock((i + l3) - byte0 / 2, j + l * 2, (k - byte1) + 3, Blocks.torch);
        }

        if (flag) {
          // worldObj.setBlockWithNotify((i + l3) - byte0 / 2, j + l * 2, k,
          // Block.torchWood.blockID);
          this.worldObj.setBlock((i + l3) - byte0 / 2, j + l * 2, k, Blocks.torch);
        }
      }

      for (int i4 = 4; i4 < byte1; i4++) {
        // worldObj.setBlockWithNotify((i - byte0 / 2) + 1, (j + l * 2) - 1, (k + i4) - byte1,
        // Block.fence.blockID);
        this.worldObj.setBlock(
            (i - byte0 / 2) + 1, (j + l * 2) - 1, (k + i4) - byte1, Blocks.fence);
        // worldObj.setBlockWithNotify((i + byte0) - byte0 / 2 - 2, (j + l * 2) - 1, (k + i4) -
        // byte1,
        // Block.fence.blockID);
        this.worldObj.setBlock(
            (i + byte0) - byte0 / 2 - 2, (j + l * 2) - 1, (k + i4) - byte1, Blocks.fence);
        flag = !flag;

        if (flag) {
          // worldObj.setBlockWithNotify((i - byte0 / 2) + 1, j + l * 2, (k + i4) - byte1,
          // Block.torchWood.blockID);
          this.worldObj.setBlock((i - byte0 / 2) + 1, j + l * 2, (k + i4) - byte1, Blocks.torch);
        }

        if (flag) {
          // worldObj.setBlockWithNotify((i + byte0) - byte0 / 2 - 2, j + l * 2, (k + i4) - byte1,
          // Block.torchWood.blockID);
          this.worldObj.setBlock(
              (i + byte0) - byte0 / 2 - 2, j + l * 2, (k + i4) - byte1, Blocks.torch);
        }
      }

      // worldObj.setBlockWithNotify(i - 1, (j + l * 2) - 1, k, 107);
      this.worldObj.setBlock(i - 1, (j + l * 2) - 1, k, Blocks.fence_gate);
      // worldObj.setBlockWithNotify(i - 2, (j + l * 2) - 1, k, 107);
      this.worldObj.setBlock(i - 2, (j + l * 2) - 1, k, Blocks.fence_gate);

      for (int j4 = 0; j4 < 6; j4++) {
        CREEPSEntityDoghouse creepsentitydoghouse = new CREEPSEntityDoghouse(this.worldObj);
        creepsentitydoghouse.setLocationAndAngles(
            i + 15, (j + l * 2) - 1, k - 7 - j4 * 5, 90F, 0.0F);
        this.worldObj.spawnEntityInWorld(creepsentitydoghouse);
      }

      for (int k4 = 0; k4 < this.rand.nextInt(15) + 5; k4++) {
        int l6 = this.rand.nextInt(byte0 - 10) + 3;
        int j8 = this.rand.nextInt(byte1 - 6) + 3;
        // worldObj.setBlockWithNotify((i + l6) - byte0 / 2, (j + l * 2) - 1, (k + j8) - byte1,
        // Block.sapling.blockID);
        this.worldObj.setBlock(
            (i + l6) - byte0 / 2, (j + l * 2) - 1, (k + j8) - byte1, Blocks.sapling);
        // TODO Below may not grow the tree (I think), but there seems to be no other way :(
        ((BlockSapling) Blocks.sapling)
            .func_149879_c(
                this.worldObj, (i + l6) - byte0 / 2, (j + l * 2) - 1, (k + j8) - byte1, this.rand);
      }

      for (int l4 = (byte0 / 2 + this.rand.nextInt(8)) - 8;
          l4 < ((byte0 / 2 + this.rand.nextInt(10)) - 5) + 8;
          l4++) {
        for (int i7 = (byte1 / 2 + this.rand.nextInt(8)) - 8;
            i7 < ((byte1 / 2 + this.rand.nextInt(10)) - 5) + 8;
            i7++) {
          // worldObj.setBlockWithNotify((i + l4) - byte0 / 2, (j + l * 2) - 2, (k + i7) - byte1,
          // Block.waterStill.blockID);
          this.worldObj.setBlock(
              (i + l4) - byte0 / 2, (j + l * 2) - 2, (k + i7) - byte1, Blocks.water);
          // worldObj.setBlockWithNotify((i + l4) - byte0 / 2, (j + l * 2) - 3, (k + i7) - byte1,
          // Block.waterStill.blockID);
          this.worldObj.setBlock(
              (i + l4) - byte0 / 2, (j + l * 2) - 3, (k + i7) - byte1, Blocks.water);
        }
      }

      // worldObj.setBlock(i + 7, (j + l * 2) - 1, k - 5, 54);
      this.worldObj.setBlock(i, j, k, Blocks.chest);
      TileEntityChest tileentitychest = new TileEntityChest();
      // worldObj.setBlockTileEntity(i + 7, (j + l * 2) - 1, k - 5, tileentitychest);
      this.worldObj.setTileEntity(i + 7, (j + l * 2) - 1, k - 5, tileentitychest);
      // worldObj.setBlockMetadataWithNotify(i + 7, (j + l * 2) - 1, k - 5, 4);
      this.worldObj.setBlock(i + 7, (j + l * 2) - 1, k - 5, Blocks.cobblestone);
      // worldObj.setBlock(i + 7, (j + l * 2) - 1, k - 6, 54);
      this.worldObj.setBlock(i + 7, (j + l * 2) - 1, k - 6, Blocks.chest);
      TileEntityChest tileentitychest1 = new TileEntityChest();
      // worldObj.setBlockTileEntity(i + 7, (j + l * 2) - 1, k - 6, tileentitychest1);
      this.worldObj.setTileEntity(i + 7, (j + l * 2) - 1, k - 6, tileentitychest1);
      // worldObj.setBlockMetadataWithNotify(i + 7, (j + l * 2) - 1, k - 6, 4);
      this.worldObj.setBlock(i + 7, (j + l * 2) - 1, k - 6, Blocks.cobblestone);

      for (int k8 = 0; k8 < tileentitychest.getSizeInventory() - 9; k8++) {
        tileentitychest.setInventorySlotContents(k8, new ItemStack(Items.bone, 32, 0));
        tileentitychest1.setInventorySlotContents(k8, new ItemStack(Items.redstone, 32, 0));
      }

      for (int l8 = tileentitychest.getSizeInventory() - 9;
          l8 < tileentitychest.getSizeInventory();
          l8++) {
        tileentitychest.setInventorySlotContents(l8, new ItemStack(Items.golden_helmet, 1, 0));
        tileentitychest1.setInventorySlotContents(l8, new ItemStack(Items.gold_ingot, 1, 0));
      }

      // worldObj.setBlock(i - 7, (j + l * 2) - 1, k - 5, 54);
      this.worldObj.setBlock(i - 7, (j + l * 2) - 1, k - 5, Blocks.chest);
      TileEntityChest tileentitychest2 = new TileEntityChest();
      // worldObj.setBlockTileEntity(i - 7, (j + l * 2) - 1, k - 5, tileentitychest2);
      this.worldObj.setTileEntity(i - 7, (j + l * 2) - 1, k - 5, tileentitychest2);
      // worldObj.setBlock(i - 7, (j + l * 2) - 1, k - 6, 54);
      this.worldObj.setBlock(i - 7, (j + l * 2) - 1, k - 6, Blocks.chest);
      TileEntityChest tileentitychest3 = new TileEntityChest();
      /// worldObj.setBlockTileEntity(i - 7, (j + l * 2) - 1, k - 6, tileentitychest3);
      this.worldObj.setTileEntity(i - 7, (j + l * 2) - 1, k - 6, tileentitychest3);

      for (int i9 = 0; i9 < tileentitychest2.getSizeInventory() - 9; i9++) {
        tileentitychest2.setInventorySlotContents(i9, new ItemStack(Items.bone, 32, 0));
        tileentitychest3.setInventorySlotContents(i9, new ItemStack(Items.redstone, 32, 0));
      }

      for (int j9 = tileentitychest2.getSizeInventory() - 9;
          j9 < tileentitychest2.getSizeInventory();
          j9++) {
        tileentitychest2.setInventorySlotContents(j9, new ItemStack(Items.diamond_helmet, 1, 0));
        tileentitychest3.setInventorySlotContents(j9, new ItemStack(Items.diamond, 1, 0));
      }
    } else {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "Hotdog Heaven cannot be built here, choose another spot!");
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.ridingEntity == null) {
      if (this.rand.nextInt(5) == 0) return "morecreeps:hotdog";
      else return null;
    }

    if (this.angrydog) return "mob.wolf.growl";

    if (this.rand.nextInt(10) == 0) return "mob.wolf.panting";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:hotdoghurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:hotdogdeath";
  }

  public void confetti() {
    World world = this.worldObj;
    double d = -MathHelper.sin(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos(((this.entityplayer).rotationYaw * (float) Math.PI) / 180F);
    CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(world);
    creepsentitytrophy.setLocationAndAngles(
        (this.entityplayer).posX + d * 3D,
        (this.entityplayer).posY - 2D,
        (this.entityplayer).posZ + d1 * 3D,
        (this.entityplayer).rotationYaw,
        0.0F);
    world.spawnEntityInWorld(creepsentitytrophy);
  }

  public void onDeath(Entity entity) {
    if (this.tamed) return;
    else {
      super.setDead();
      this.dropItem(Items.porkchop, 1);
      return;
    }
  }
}
