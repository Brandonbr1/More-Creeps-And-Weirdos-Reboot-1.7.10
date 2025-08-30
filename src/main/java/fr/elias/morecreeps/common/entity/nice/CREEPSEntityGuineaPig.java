package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuy;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTombstone;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import java.util.List;
import net.minecraft.block.Block;
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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityGuineaPig extends EntityMob {

  EntityPlayer entityplayer;
  Entity entitymain;
  World world;
  public boolean rideable;
  public int interest;
  private boolean primed;
  public boolean tamed;
  public int basehealth;
  private float distance;
  public Item armor;
  public String basetexture;
  public boolean used;
  public boolean grab;
  public List<?> piglist;
  public int pigstack;
  public int level;
  public float totaldamage;
  public int alt;
  public boolean hotelbuilt;
  public int wanderstate;
  public int speedboost;
  public int totalexperience;
  public float baseSpeed;
  public float modelsize;
  public int unmounttimer;
  public int skillattack;
  public int skilldefend;
  public int skillhealing;
  public int skillspeed;
  public int firenum;
  public int firepower;
  public int healtimer;
  public EntityLivingBase owner;
  public int criticalHitCooldown;
  public String name;
  public String texture;
  public double health;
  public double attackStrength;
  public double moveSpeed;

  static final String Names[] = {
    "Sugar",
    "Clover",
    "CoCo",
    "Sprinkles",
    "Mr. Rabies",
    "Stinky",
    "The Incredible Mr. CoCoPants",
    "Butchie",
    "Lassie",
    "Fuzzy",
    "Nicholas",
    "Natalie",
    "Pierre",
    "Priscilla",
    "Mrs. McGillicutty",
    "Dr. Tom Jones",
    "Peter the Rat",
    "Wiskers",
    "Penelope",
    "Sparky",
    "Tinkles",
    "Ricardo",
    "Jimothy",
    "Captain Underpants",
    "CoCo Van Gough",
    "Chuck Norris",
    "PeeWee",
    "Quasimodo",
    "ZSA ZSA",
    "Yum Yum",
    "Deputy Dawg",
    "Henrietta Pussycat",
    "Bob Dog",
    "King Friday",
    "Jennifer",
    "The Situation",
    "Prince Charming",
    "Sid",
    "Sunshine",
    "Bubbles",
    "Carl",
    "Snowy",
    "Dorf",
    "Chilly Willy",
    "Angry Bob",
    "George W. Bush",
    "Ted Lange from The Love Boat",
    "Notch",
    "Frank",
    "A Very Young Pig",
    "Blaster",
    "Darwin",
    "Ruggles",
    "Chang",
    "Spaz",
    "Fluffy",
    "Fuzzy",
    "Charrlotte",
    "Tootsie",
    "Mary",
    "Caroline",
    "Michelle",
    "Sandy",
    "Peach",
    "Scrappy",
    "Roxanne",
    "James the Pest",
    "Lucifer",
    "Shaniqua",
    "Wendy",
    "Zippy",
    "Prescott Pig",
    "Pimpin' Pig",
    "Big Daddy",
    "Little Butchie",
    "The Force",
    "The Handler",
    "Little Louie",
    "Satin",
    "Sparkly Puff",
    "Dr. Chews",
    "Pickles",
    "Longtooth",
    "Jeffry",
    "Pedro the Paunchy",
    "Wee Willy Wiskers",
    "Tidy Smith",
    "Johnson",
    "Big Joe",
    "Tiny Mackeral",
    "Wonderpig",
    "Wee Wonderpig",
    "The Polish Baron",
    "Inconceivable",
    "Double Danny Dimples",
    "Jackie Jones",
    "Pistol",
    "Tiny Talker",
    "Strum",
    "Disco the Pig",
    "Banjo",
    "Fingers",
    "Clean Streak",
    "Little Sweet",
    "Fern",
    "Youngblood",
    "Lazy Cottonball",
    "Foxy",
    "SlyFoxHound"
  };
  static final String pigTextures[] = {
    "morecreeps:textures/entity/ggpig1.png",
    "morecreeps:textures/entity/ggpig2.png",
    "morecreeps:textures/entity/ggpig3.png",
    "morecreeps:textures/entity/ggpig4.png",
    "morecreeps:textures/entity/ggpig5.png",
    "morecreeps:textures/entity/ggpig6.png",
    "morecreeps:textures/entity/ggpig7.png",
    "morecreeps:textures/entity/ggpig8.png",
    "morecreeps:textures/entity/ggpig9.png",
    "morecreeps:textures/entity/ggpiga.png"
  };
  public static final int leveldamage[] = {
    0, 200, 600, 1000, 1500, 2000, 2700, 3500, 4400, 5400, 6600, 7900, 9300, 10800, 12400, 14100,
    15800, 17600, 19500, 21500, 25000, 30000
  };
  public static final String levelname[] = {
    "Guinea Pig",
    "A nothing pig",
    "An inexperienced pig",
    "Trainee",
    "Private",
    "Private First Class",
    "Corporal",
    "Sergeant",
    "Staff Sergeant",
    "Sergeant First Class",
    "Master Sergeant",
    "First Sergeant",
    "Sergeant Major",
    "Command Sergeant Major",
    "Second Lieutenant",
    "First Lieutenant",
    "Captain",
    "Major",
    "Lieutenant Colonel",
    "Colonel",
    "General of the Pig Army",
    "General of the Pig Army"
  };

  public CREEPSEntityGuineaPig(World world) {
    super(world);
    this.primed = false;
    this.basetexture = pigTextures[this.rand.nextInt(pigTextures.length)];
    this.texture = this.basetexture;
    this.setSize(0.6F, 0.6F);
    this.rideable = false;
    this.basehealth = this.rand.nextInt(5) + 5;
    this.health = this.basehealth;
    this.attackStrength = 1;
    this.tamed = false;
    this.name = "";
    this.pigstack = 0;
    this.level = 1;
    this.totaldamage = 0.0F;
    this.alt = 1;
    this.hotelbuilt = false;
    this.wanderstate = 0;
    this.baseSpeed = 0.6F;
    this.moveSpeed = this.baseSpeed;
    this.speedboost = 0;
    this.totalexperience = 0;
    this.fallDistance = -25F;
    this.modelsize = 1.0F;
    this.unmounttimer = 0;
    this.skillattack = 0;
    this.skilldefend = 0;
    this.skillhealing = 0;
    this.skillspeed = 0;
    this.firepower = 0;
    this.criticalHitCooldown = 5;
    this.getNavigator().setCanSwim(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(3, new EntityAILookIdle(this));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6f);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1);
  }

  @Override
  protected void updateAITick() {
    super.updateEntityActionState();

    if (!this.attackEntityAsMob(this.entitymain)
        && !this.hasPath()
        && this.tamed
        && this.ridingEntity == null
        && this.wanderstate != 2
        && (this.entityplayer != null)) {
      float f = this.entityplayer.getDistanceToEntity(this);

      if (f <= 5F) {;
      }
    }

    if (this.getAttackTarget() instanceof EntityPlayer
        && (this.getDistanceToEntity(this.entityplayer) < 6F)) {
      this.setAttackTarget(null);
    }

    if ((float) this.health < this.basehealth * (0.1F * this.skillhealing)
        && this.skillhealing > 0) {
      this.attackEntity(this.entityplayer, (float) this.attackStrength);
    }
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    Entity obj = null;

    if (this.tamed && this.wanderstate == 0) {
      List<?> list =
          this.worldObj.getEntitiesWithinAABBExcludingEntity(
              this, this.boundingBox.expand(16D, 16D, 16D));

      for (Object o : list) {
        Entity entity = (Entity) o;

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

        if (!(entity instanceof EntityPlayer) || this.wanderstate != 0) {
          continue;
        }

        EntityPlayer entityplayer = (EntityPlayer) entity;

        if (obj != null && !(obj instanceof EntityPlayer)) {
          continue;
        }

        this.distance = this.getDistanceToEntity(entityplayer);

        if (this.distance < 6F) {
          obj = null;
        } else {
          obj = entityplayer;
        }
      }
    }

    return obj;
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (!(this.getAttackTarget() instanceof EntityPlayer)
        && !(this.getAttackTarget() instanceof CREEPSEntityGuineaPig)
        && !(this.getAttackTarget() instanceof CREEPSEntityHotdog)
        && this.ridingEntity == null) {
      if (this.onGround && this.tamed) {
        double d = entity.posX - this.posX;
        double d2 = entity.posZ - this.posZ;
        float f2 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.motionX = (d / f2) * 0.5D * 0.80000001192092896D + this.motionX * 0.20000000298023224D;
        this.motionZ =
            (d2 / f2) * 0.5D * 0.80000001192092896D + this.motionZ * 0.20000000298023224D;
        this.motionY = 0.40000000596046448D;
      } else if (this.tamed && f < 2.5D) {
        if (this.rand.nextInt(5) == 0) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigangry",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        double d1 = 1.0D;
        d1 += this.level * 5 + this.skillattack * 4;

        if (d1 < 5D) {
          d1 = 5D;
        }

        super.attackEntityAsMob(entity);

        if (this.rand.nextInt(100) > 100D - d1) {
          if (CREEPSConfig.Blood && this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.guineaPigParticles(this.worldObj, this);
          }

          float f1 = (float) this.attackStrength * 0.25F;

          if (f1 < 1.0F) {
            f1 = 1.0F;
          }

          if (this.skillattack > 1
              && this.rand.nextInt(100) > 100 - this.skillattack * 2
              && this.criticalHitCooldown-- < 1) {
            this.totaldamage += 25F;
            this.totalexperience += 25;

            if (f1 < ((EntityLiving) entity).getHealth()) {
              f1 = ((EntityLiving) entity).getHealth();
            }

            ((EntityLiving) entity).setHealth(0);
            this.worldObj.playSoundAtEntity(entity, "morecreeps:guineapigcriticalhit", 1.0F, 1.0F);
            this.criticalHitCooldown = 50 - this.skillattack * 8;
          }

          if (((EntityLiving) entity).getHealth() - f1 <= 0.0F) {
            this.worldObj.playSoundAtEntity(entity, "morecreeps:ggpigangry", 1.0F, 1.0F);
          }

          ((EntityLiving) entity)
              .attackEntityFrom(DamageSource.causeThrownDamage(this, entity), (int) f1);
          this.totaldamage += (int) (f1 * 1.5D + this.skillattack);
          this.totalexperience += (int) (f1 * 1.5D + this.skillattack);
        }

        if (this.totaldamage > leveldamage[this.level] && this.level < 20) {
          this.level++;
          this.totaldamage = 0.0F;
          boolean flag = false;

          if (this.level == 5) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievepiglevel5, 1);
          }

          if (this.level == 10) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievepiglevel10, 1);
          }

          if (this.level == 20) {
            flag = true;
            this.confetti();
            this.entityplayer.addStat(MoreCreepsAndWeirdos.achievepiglevel20, 1);
          }

          if (flag) {
            this.worldObj.playSoundAtEntity(
                this.entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
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

        super.setAttackTarget((EntityLivingBase) entity);
      }
    }

    if (f < 16D && (this.getAttackTarget() instanceof EntityPlayer)) {
      this.setAttackTarget(null);
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (entity != this.getAttackTarget()) {
      this.setAttackTarget((EntityLivingBase) entity);
    }

    return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    if (this.ridingEntity != null || this.unmounttimer-- > 0) return false;
    else return super.isEntityInsideOpaqueBlock();
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.world == null) return;

    if (this.riddenByEntity != null && this.ridingEntity == null) {
      if (this.ridingEntity != null) {
        this.ridingEntity.mountEntity(null);
      }

      if (this.riddenByEntity != null) {
        this.riddenByEntity.mountEntity(null);
      }
      this.riddenByEntity.mountEntity(null);
      this.mountEntity(null);
      return;
    }

    if (this.ridingEntity != null
        && !(this.ridingEntity instanceof EntityPlayer)
        && !(this.ridingEntity instanceof CREEPSEntityGuineaPig)
        && !(this.ridingEntity instanceof CREEPSEntityHotdog)) {
      this.mountEntity(null);
      this.unmounttimer = 20;
    }

    if (this.speedboost-- == 0 && this.name.length() > 0) {
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:guineapigspeeddown",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          (new StringBuilder())
              .append("\247b")
              .append(this.name)
              .append("\2476 has run out of speedboost.")
              .toString());

      if (this.wanderstate != 1) {
        this.moveSpeed = this.baseSpeed;
      }
    }

    if (this.healtimer-- < 1 && this.health < this.basehealth && this.skillhealing > 0) {
      this.healtimer = (6 - this.skillhealing) * 200;
      this.health += this.skillhealing;

      if (this.health > this.basehealth) {
        this.health = this.basehealth;
        this.setHealth((float) this.health);
      }

      for (int i = 0; i < this.skillhealing; i++) {
        double d = this.rand.nextGaussian() * 0.02D;
        double d1 = this.rand.nextGaussian() * 0.02D;
        double d2 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(
            "HEART".toLowerCase(),
            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            this.posY + 0.5D + this.rand.nextFloat() * this.height,
            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            d,
            d1,
            d2);
      }
    }

    if (this.handleWaterMovement()) {
      this.motionY += 0.028799999505281448D;
    }
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
    nbttagcompound.setBoolean("HotelBuilt", this.hotelbuilt);
    nbttagcompound.setInteger("AttackStrength", (int) this.attackStrength);
    nbttagcompound.setInteger("WanderState", this.wanderstate);
    nbttagcompound.setInteger("SpeedBoost", this.speedboost);
    nbttagcompound.setInteger("TotalExperience", this.totalexperience);
    nbttagcompound.setFloat("BaseSpeed", this.baseSpeed);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
    nbttagcompound.setInteger("SkillAttack", this.skillattack);
    nbttagcompound.setInteger("SkillDefense", this.skilldefend);
    nbttagcompound.setInteger("SkillHealing", this.skillhealing);
    nbttagcompound.setInteger("SkillSpeed", this.skillspeed);
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
    this.hotelbuilt = nbttagcompound.getBoolean("HotelBuilt");
    this.attackStrength = nbttagcompound.getInteger("AttackStrength");
    this.wanderstate = nbttagcompound.getInteger("WanderState");
    this.speedboost = nbttagcompound.getInteger("SpeedBoost");
    this.totalexperience = nbttagcompound.getInteger("TotalExperience");
    this.baseSpeed = nbttagcompound.getFloat("BaseSpeed");
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    this.skillattack = nbttagcompound.getInteger("SkillAttack");
    this.skilldefend = nbttagcompound.getInteger("SkillDefense");
    this.skillhealing = nbttagcompound.getInteger("SkillHealing");
    this.skillspeed = nbttagcompound.getInteger("SkillSpeed");
    this.texture = this.basetexture;

    if (this.wanderstate == 1) {
      this.moveSpeed = 0.0F;
    } else {
      this.moveSpeed = this.speedboost <= 0 ? this.baseSpeed : this.baseSpeed + 0.75F;
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
        creepsentitytombstone.hotelbuilt = this.hotelbuilt;
        creepsentitytombstone.attackStrength = (int) this.attackStrength;
        creepsentitytombstone.wanderstate = this.wanderstate;
        creepsentitytombstone.speedboost = this.speedboost;
        creepsentitytombstone.totalexperience = this.totalexperience;
        creepsentitytombstone.baseSpeed = this.baseSpeed;
        creepsentitytombstone.modelsize = this.modelsize;
        creepsentitytombstone.skillattack = this.skillattack;
        creepsentitytombstone.skilldefend = this.skilldefend;
        creepsentitytombstone.skillhealing = this.skillhealing;
        creepsentitytombstone.skillspeed = this.skillspeed;
        creepsentitytombstone.deathtype = "GuineaPig";
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
            "EXPLODE".toLowerCase(),
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
            "EXPLODE".toLowerCase(),
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
    return i1 != Blocks.cobblestone
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
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
          CREEPSGuiHandler.GuiType.GUINEA_TRAIN.id,
          this.world,
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
              "morecreeps:ggpigmount",
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

          if ((Entity) obj instanceof CREEPSEntityGuineaPig) {
            ((CREEPSEntityGuineaPig) obj).unmounttimer = 20;
          }

          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:ggpigunmount",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      }
    }

    if (itemstack != null && this.health > 0) {
      if ((itemstack.getItem() == Items.book
              || itemstack.getItem() == Items.paper
              || itemstack.getItem() == MoreCreepsAndWeirdos.guineapigradio)
          && this.tamed) {
        entityplayer.openGui(
            MoreCreepsAndWeirdos.INSTANCE,
            CREEPSGuiHandler.GuiType.GUINEA.id,
            this.world,
            this.getEntityId(),
            0,
            0);
      }

      if (itemstack.getItem() == Items.diamond && this.tamed) {
        if (this.ridingEntity != null) {
          MoreCreepsAndWeirdos.proxy.addChatMessage(
              "Put your Guinea Pig down before building the Guinea Pig Hotel!");
        } else if (!this.hotelbuilt) {
          if (this.level >= 20) {

            this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            entityplayer.addStat(MoreCreepsAndWeirdos.achievepighotel, 1);

            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            int i = MathHelper.floor_double(entityplayer.posX);
            int i1 = MathHelper.floor_double(entityplayer.getBoundingBox().minY);
            int j1 = MathHelper.floor_double(entityplayer.posZ);
            this.createDisco(entityplayer, i + 2, i1, j1 + 2);
          } else {
            MoreCreepsAndWeirdos.proxy.addChatMessage(
                "Your Guinea Pig must be level 20 to build a Hotel.");
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
                  .append("\247f has already built a Hotel.")
                  .toString());
        }
      }

      if (itemstack.getItem() == Item.getItemFromBlock(Blocks.red_flower) && this.tamed
          || itemstack.getItem() == Item.getItemFromBlock(Blocks.yellow_flower) && this.tamed) {
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
              "EXPLODE".toLowerCase(),
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
        this.setHealth((float) this.health);
        this.setDead();
      } else {
        this.primed = false;
      }

      byte byte0 = 0;
      byte byte1 = 0;

      if (this.tamed && this.texture.length() == 23 + 14) {
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
        this.armor = itemstack.getItem();
        this.smoke();
        int k1 = 0;

        if (this.armor == Items.leather_boots
            || this.armor == Items.leather_chestplate
            || this.armor == Items.leather_helmet
            || this.armor == Items.leather_leggings) {
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

        if (this.armor == Items.golden_boots
            || this.armor == Items.golden_chestplate
            || this.armor == Items.golden_helmet
            || this.armor == Items.golden_leggings) {
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

        if (this.armor == Items.iron_boots
            || this.armor == Items.iron_chestplate
            || this.armor == Items.iron_helmet
            || this.armor == Items.iron_leggings) {
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

        if (this.armor == Items.diamond_boots
            || this.armor == Items.diamond_chestplate
            || this.armor == Items.diamond_helmet
            || this.armor == Items.diamond_leggings) {
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

      if (itemstack.getItem() == Items.wheat) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:ggpigeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 15;
        this.health += 10;
        this.isDead = false;
        this.smoke();
      }

      if (itemstack.getItem() == Items.cookie) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:ggpigeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 30;
        this.health += 15;
        this.isDead = false;
        this.smoke();
      }

      if (itemstack.getItem() == Items.apple) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:ggpigeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 55;
        this.health += 25;
        this.isDead = false;
        this.smoke();
      }

      if (itemstack.getItem() == Items.golden_apple) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:ggpigeat",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.used = true;
        this.interest += 111;
        this.health += 75;
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
        entityplayer.addStat(MoreCreepsAndWeirdos.achievepigtaming, 1);

        if (this.used) {
          this.smoke();
        }

        this.tamed = true;

        if (this.name.length() < 1) {
          this.name = Names[this.rand.nextInt(Names.length)];
        }

        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:ggpigfull",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        this.interest = 100;
      }

      return true;
    } else return false;
  }

  public void createDisco(EntityPlayer entityplayer, int i, int j, int k) {
    byte byte0 = 16;
    byte byte1 = 6;
    byte byte2 = 16;
    this.alt = 1;
    int l = 0;

    for (int i1 = 0; i1 < byte1 + 4; i1++) {
      for (int k3 = -2; k3 < byte0 + 2; k3++) {
        for (int i5 = -2; i5 < byte2 + 2; i5++) {

          if (this.worldObj.getBlock(i + k3, j + i1, k + i5) != Blocks.air) {
            l++;
          }
        }
      }
    }

    if (l < 900) {
      this.used = true;
      this.hotelbuilt = true;
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:guineapighotel",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      MoreCreepsAndWeirdos.proxy.addChatMessage("GUINEA PIG HOTEL BUILT!");

      for (int j1 = 0; j1 < byte1 + 4; j1++) {
        for (int l3 = -2; l3 < byte0 + 2; l3++) {
          for (int j5 = -2; j5 < byte2 + 2; j5++) {

            this.worldObj.setBlock(i + l3, j + j1, k + j5, Blocks.air);
          }
        }
      }

      for (int k1 = 0; k1 < byte1; k1++) {
        for (int i4 = 0; i4 < byte2; i4++) {
          this.alt *= -1;

          for (int k5 = 0; k5 < byte0; k5++) {
            this.worldObj.setBlock(i + i4, j + k1, k + 0, Blocks.wool);
            this.worldObj.setBlock(i + i4, j + k1, (k + byte0) - 1, Block.getBlockById(35));
            this.worldObj.setBlockMetadataWithNotify(
                i + 0, j + k1, k + k5, Block.getIdFromBlock(Blocks.wool), 1);
            this.worldObj.setBlockMetadataWithNotify(
                i + byte2, j + k1, k + k5, Block.getIdFromBlock(Blocks.wool), 1);

            this.alt *= -1;

            if (this.alt > 0) {
              this.worldObj.setBlockMetadataWithNotify(
                  i + i4, j, k + k5, Block.getIdFromBlock(Blocks.wool), 10);

            } else {
              this.worldObj.setBlockMetadataWithNotify(
                  i + i4, j, k + k5, Block.getIdFromBlock(Blocks.wool), 11);
            }

            this.worldObj.setBlock(i + i4, j + byte1, k + k5, Blocks.glass);
          }
        }
      }

      this.worldObj.setBlock(i + 7, j, k - 1, Block.getBlockById(43));
      this.worldObj.setBlock(i + 10, j, k - 1, Block.getBlockById(43));
      this.worldObj.setBlock(i + 7, j + 2, k - 1, Blocks.torch);
      this.worldObj.setBlock(i + 10, j + 2, k - 1, Blocks.torch);
      this.worldObj.setBlock(i + 8, j, k - 1, Block.getBlockById(44));
      this.worldObj.setBlock(i + 9, j, k - 1, Block.getBlockById(44));
      this.worldObj.setBlock(i + 8, j + 1, k, Blocks.wooden_door);
      this.worldObj.setBlock(i + 8, j + 1, k, Blocks.air);
      this.worldObj.setBlock(i + 8, j + 2, k, Blocks.wooden_door);
      this.worldObj.setBlock(i + 8, j + 2, k, Block.getBlockById(8));
      this.worldObj.setBlock(i + 9, j + 1, k, Blocks.wooden_door);
      this.worldObj.setBlock(i + 9, j + 1, k, Block.getBlockById(1));
      this.worldObj.setBlock(i + 9, j + 2, k, Blocks.wooden_door);
      this.worldObj.setBlock(i + 9, j + 2, k, Block.getBlockById(9));
      this.worldObj.setBlock(i + 8, j + 1, k + 5, Blocks.sandstone);
      this.worldObj.setBlock(i + 9, j + 1, k + 5, Blocks.sandstone);
      this.worldObj.setBlock(i + 8, j + 2, k + 5, Blocks.torch);
      this.worldObj.setBlock(i + 9, j + 2, k + 5, Blocks.torch);

      for (int l1 = 4; l1 < byte2 - 4; l1 += 3) {
        this.worldObj.setBlock(i + 1, j + 4, k + l1, Blocks.torch);
        this.worldObj.setBlock((i + byte2) - 1, j + 4, k + l1, Blocks.torch);
        this.worldObj.setBlock(i + l1 + 2, j + 4, (k + byte0) - 2, Blocks.torch);
      }

      for (int i2 = 0; i2 < 9; i2++) {
        for (int j4 = 1; j4 < byte2; j4++) {
          this.worldObj.setBlock(i + j4, j + 1, k + i2 + 6, Blocks.dirt);
        }
      }

      for (int j2 = 0; j2 < 5; j2++) {
        for (int k4 = 1; k4 < byte2; k4++) {
          // worldObj.setBlockWithNotify(i + k4, j + 2, k + j2 + 10, Block.dirt.blockID);
          this.world.setBlock(i + k4, j + 2, k + j2 + 10, Blocks.dirt);
        }
      }

      for (int k2 = 3; k2 < byte2 - 3; k2++) {
        // worldObj.setBlockWithNotify(i + k2, j + 1, k + 6, 0);
        this.world.setBlock(i + k2, j + 1, k + 6, Blocks.air);
      }

      for (int l2 = 7; l2 < byte2 - 4; l2++) {
        // worldObj.setBlockWithNotify(i + l2, j + 1, k + 7, 0);
        this.world.setBlock(i + l2, j + 1, k + 7, Blocks.air);
      }

      for (int i3 = 7; i3 < 12; i3++) {
        // worldObj.setBlockWithNotify(i + 1, j + 2, k + i3, 37);
        this.world.setBlock(i + 1, j + 2, k + i3, Blocks.yellow_flower);
        // worldObj.setBlockWithNotify(i + 2, j + 2, k + i3, 37);
        this.world.setBlock(i + 2, j + 2, k + i3, Blocks.yellow_flower);
        // worldObj.setBlockWithNotify(i + 14, j + 2, k + i3, 38);
        this.world.setBlock(i + 14, j + 2, k + i3, Blocks.red_flower);
        // worldObj.setBlockWithNotify(i + 15, j + 2, k + i3, 38);
        this.world.setBlock(i + 15, j + 2, k + i3, Blocks.red_flower);
      }

      for (int j3 = 0; j3 < 3; j3++) {
        for (int l4 = 6; l4 < byte2 - 3; l4++) {
          /// worldObj.setBlockWithNotify(i + l4, j + 2, k + j3 + 11, 8);
          this.world.setBlock(i + 9, j + 1, k + 5, Blocks.double_stone_slab);
          /// worldObj.setBlockWithNotify(i + l4, j + 1, k + j3 + 11, 8);
          this.world.setBlock(i + 9, j + 1, k + 5, Blocks.double_stone_slab);
        }
      }

      // worldObj.setBlock(i + 5, j + 2, k + 12, 8);
      this.world.setBlock(i + 5, j + 2, k + 12, Blocks.flowing_water);
      // worldObj.setBlock(i + 5, j + 2, k + 13, 8);
      this.world.setBlock(i + 5, j + 2, k + 13, Blocks.flowing_water);
      // worldObj.setBlockWithNotify(i + 9, j + 1, k + 8, 2);
      this.world.setBlock(i + 9, j + 1, k + 8, Blocks.grass);
      // worldObj.setBlockWithNotify(i + 5, j + 3, k, 20);
      this.world.setBlock(i + 5, j + 3, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 5, j + 2, k, 20);
      this.world.setBlock(i + 5, j + 2, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 4, j + 3, k, 20);
      this.world.setBlock(i + 4, j + 3, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 4, j + 2, k, 20);
      this.world.setBlock(i + 4, j + 2, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 13, j + 3, k, 20);
      this.world.setBlock(i + 13, j + 3, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 13, j + 2, k, 20);
      this.world.setBlock(i + 13, j + 2, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 12, j + 3, k, 20);
      this.world.setBlock(i + 12, j + 3, k, Blocks.glass);
      // worldObj.setBlockWithNotify(i + 12, j + 2, k, 20);
      this.world.setBlock(i + 12, j + 2, k, Blocks.glass);
      // worldObj.setBlock(i + 1, j + 1, k + 3, 54);
      this.world.setBlock(i + 1, j + 1, k + 3, Blocks.chest);
      TileEntityChest tileentitychest = new TileEntityChest();
      // setBlockTileEntity is about the same as setBlockState except is a tile entity, like a
      // chest's
      // worldObj.setBlockTileEntity(i + 1, j + 1, k + 3, tileentitychest);
      this.world.setTileEntity(i + 1, j + 1, k + 3, tileentitychest);
      // worldObj.setBlock(i + 1, j + 1, k + 4, 54);
      this.world.setBlock(i + 9, j + 1, k + 5, Blocks.double_stone_slab);
      TileEntityChest tileentitychest1 = new TileEntityChest();
      // worldObj.setBlockTileEntity(i + 1, j + 1, k + 4, tileentitychest1);
      this.world.setTileEntity(i + 1, j + 1, k + 4, tileentitychest1);

      for (int l5 = 0; l5 < tileentitychest.getSizeInventory(); l5++) {
        if (this.rand.nextInt(10) == 0) {
          tileentitychest.setInventorySlotContents(l5, new ItemStack(Items.golden_apple, 1, 0));
          tileentitychest1.setInventorySlotContents(l5, new ItemStack(Items.golden_apple, 1, 0));
        } else {
          tileentitychest.setInventorySlotContents(l5, new ItemStack(Items.apple, 1, 0));
          tileentitychest1.setInventorySlotContents(
              l5, new ItemStack(Items.wheat, this.rand.nextInt(16), 0));
        }
      }

      // worldObj.setBlock((i + byte2) - 1, j + 1, k + 3, 54);
      this.world.setBlock((i + byte2) - 1, j + 1, k + 3, Blocks.glass);
      TileEntityChest tileentitychest2 = new TileEntityChest();
      // worldObj.setBlockTileEntity((i + byte2) - 1, j + 1, k + 3, tileentitychest2);
      this.world.setTileEntity((i + byte2) - 1, j + 1, k + 3, tileentitychest2);
      // worldObj.setBlock((i + byte2) - 1, j + 1, k + 4, 54);
      this.world.setBlock((i + byte2) - 1, j + 1, k + 4, Blocks.glass);
      TileEntityChest tileentitychest3 = new TileEntityChest();
      // worldObj.setBlockTileEntity((i + byte2) - 1, j + 1, k + 4, tileentitychest3);
      this.world.setTileEntity((i + byte2) - 1, j + 1, k + 4, tileentitychest3);

      for (int i6 = 0; i6 < tileentitychest1.getSizeInventory(); i6++) {
        if (this.rand.nextInt(15) == 0) {
          tileentitychest2.setInventorySlotContents(i6, new ItemStack(Items.golden_apple, 1, 0));
          tileentitychest3.setInventorySlotContents(i6, new ItemStack(Items.apple, 1, 0));
        } else {
          tileentitychest2.setInventorySlotContents(i6, new ItemStack(Items.apple, 1, 0));
          tileentitychest3.setInventorySlotContents(
              i6, new ItemStack(Items.wheat, this.rand.nextInt(16), 0));
        }
      }
    } else {
      MoreCreepsAndWeirdos.proxy.addChatMessage("Too many obstructions, choose another spot!");
    }
  }

  public void confetti() {
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

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.ridingEntity == null) {
      if (this.rand.nextInt(5) == 0) return "morecreeps:ggpig";
      else return null;
    } else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:ggpigangry";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:ggpigdeath";
  }

  public void onDeath(Entity entity) {
    if (this.tamed) return;
    else {
      super.setDead();
      this.dropItem(Items.porkchop, 1);
      return;
    }
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return !this.tamed;
  }
}
