package fr.elias.morecreeps.common.entity.proj;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityGooGoat extends EntityAnimal {

  protected double attackrange;
  public boolean bone;
  protected int attack;
  public float goatsize;

  /** Entity motion Y */
  public int eaten;

  public boolean hungry;
  public int hungrytime;
  public int goatlevel;
  public float modelspeed;
  public boolean angry;
  private int angerLevel;
  private int randomSoundDelay;
  public String texture;

  public CREEPSEntityGooGoat(World world) {
    super(world);
    this.bone = false;
    this.texture = "morecreeps:textures/entity/googoat1.png";
    this.attack = 1;
    this.attackrange = 16D;
    this.goatsize = 0.7F;
    this.hungry = false;
    this.angry = false;
    this.hungrytime = this.rand.nextInt(100) + 10;
    this.goatlevel = 1;
    this.modelspeed = 0.45F;
    this.setEntitySize(this.width * this.goatsize, this.height * this.goatsize);
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.45D, true));
    this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(
        2, new CREEPSEntityGooGoat.AIAttackEntity(this, EntityPlayer.class, true));
  }

  public void setEntitySize(float width, float height) {
    this.setSize(width, height);
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
    this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2D);
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    if (this.worldObj == null) return;
    if (this.modelspeed < 0.05F) {
      this.modelspeed = 0.05F;
    }

    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.modelspeed);
    super.onLivingUpdate();

    if (this.hungry) {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      Block l = this.worldObj.getBlock(i, j - 1, k);

      if (l == Blocks.grass && this.rand.nextInt(10) == 0) {
        this.worldObj.setBlock(i, j - 1, k, Blocks.dirt);
        this.hungrytime = this.hungrytime + this.rand.nextInt(100) + 25;

        if (this.hungrytime > 300 && this.goatlevel < 5) {
          this.hungry = false;
          this.hungrytime = 0;
          this.goatsize = this.goatsize + 0.2F;
          this.goatlevel++;
          this.attack++;
          this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
              .setBaseValue(15 * this.goatlevel + 25);
          this.texture = "morecreeps:textures/entity/googoat" + this.goatlevel + ".png";
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:googoatstretch",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      }
    } else {
      this.hungrytime--;

      if (this.hungrytime < 1) {
        this.hungry = true;
        this.hungrytime = 1;
      }
    }
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  @Override
  protected Entity findPlayerToAttack() {
    float f = this.getBrightness(1.0F);

    if (f < 0.0F || this.angry) {
      EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, this.attackrange);

      if (entityplayer != null) return entityplayer;
    }

    if (this.rand.nextInt(10) == 0) {
      EntityLivingBase entityliving = this.getClosestTarget(this, 6D);
      return entityliving;
    } else return null;
  }

  public EntityLivingBase getClosestTarget(Entity entity, double d) {
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

  class AIAttackEntity extends EntityAINearestAttackableTarget {

    private EntityLivingBase targetEntity;

    public AIAttackEntity(EntityCreature p_i45878_1_, Class<?> p_i45878_2_, boolean p_i45878_3_) {
      super(p_i45878_1_, p_i45878_2_, 1, p_i45878_3_);
    }

    @Override
    public boolean shouldExecute() {
      return CREEPSEntityGooGoat.this.angry
          && CREEPSEntityGooGoat.this.getAttackTarget() != null
          && super.shouldExecute();
    }

    @Override
    public void startExecuting() {
      if (CREEPSEntityGooGoat.this.onGround) // TODO move this on updateTask() if isn't working
      {
        double d = this.targetEntity.posX - CREEPSEntityGooGoat.this.posX;
        double d1 = this.targetEntity.posZ - CREEPSEntityGooGoat.this.posZ;
        float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
        CREEPSEntityGooGoat.this.motionX =
            (d / f1)
                    * 0.20000000000000001D
                    * (0.850000011920929D
                        + CREEPSEntityGooGoat.this.goatlevel * 0.10000000000000001D)
                + CREEPSEntityGooGoat.this.motionX * 0.20000000298023224D;
        CREEPSEntityGooGoat.this.motionZ =
            (d1 / f1)
                    * 0.20000000000000001D
                    * (0.80000001192092896D
                        + CREEPSEntityGooGoat.this.goatlevel * 0.10000000000000001D)
                + CREEPSEntityGooGoat.this.motionZ * 0.20000000298023224D;
        CREEPSEntityGooGoat.this.motionY =
            0.10000000596246449D + CREEPSEntityGooGoat.this.goatlevel * 0.070000002559000005D;
        CREEPSEntityGooGoat.this.fallDistance = -25F;
      }
      super.startExecuting();
    }
    /*
     * public void updateTask()
     * {
     * }
     */

  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();
    this.hungry = false;

    if (entity instanceof EntityPlayer) {
      this.angry = true;
    }
    return super.attackEntityFrom(damagesource, i);
  }

  private void becomeAngryAt(Entity entity) {
    this.setRevengeTarget((EntityLivingBase) entity);
    this.angerLevel = 400 + this.rand.nextInt(400);
    this.randomSoundDelay = this.rand.nextInt(40);
  }

  /**
   * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
   * their attack.
   */
  @Override
  protected void attackEntity(Entity entity, float f) {
    if (this.onGround) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX =
          (d / f1)
                  * 0.20000000000000001D
                  * (0.850000011920929D + this.goatlevel * 0.10000000000000001D)
              + this.motionX * 0.20000000298023224D;
      this.motionZ =
          (d1 / f1)
                  * 0.20000000000000001D
                  * (0.80000001192092896D + this.goatlevel * 0.10000000000000001D)
              + this.motionZ * 0.20000000298023224D;
      this.motionY = 0.10000000596246449D + this.goatlevel * 0.070000002559000005D;
      this.fallDistance = -25F;
    }

    if (f < 2D + this.goatlevel * 0.10000000000000001D
        && entity.boundingBox.maxY > this.boundingBox.minY
        && entity.boundingBox.minY < this.boundingBox.maxY) {
      this.attackTime = 20;
      entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attack);
    }
  }

  public int[] findTree(Entity entity, Material material, Double double1) {
    AxisAlignedBB axisalignedbb =
        entity.boundingBox.expand(
            double1.doubleValue(), double1.doubleValue(), double1.doubleValue());
    int i = MathHelper.floor_double(axisalignedbb.minX);
    int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
    int k = MathHelper.floor_double(axisalignedbb.minY);
    int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
    int i1 = MathHelper.floor_double(axisalignedbb.minZ);
    int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);

    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = k; l1 < l; l1++) {
        for (int i2 = i1; i2 < j1; i2++) {
          int j2 = this.worldObj.getBlockMetadata(k1, l1, i2);

          if (j2 != 0 && this.worldObj.getBlock(k1, l1, i2).getMaterial() == material)
            return (new int[] {k1, l1, i2});
        }
      }
    }

    return (new int[] {-1, 0, 0});
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
    return (i1 == Blocks.grass || i1 == Blocks.dirt)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(40) == 0
        && l > 7;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setBoolean("Hungry", this.hungry);
    nbttagcompound.setInteger("GoatLevel", this.goatlevel);
    nbttagcompound.setFloat("GoatSize", this.goatsize);
    nbttagcompound.setFloat("ModelSpeed", this.modelspeed);
    nbttagcompound.setBoolean("Angry", this.angry);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.hungry = nbttagcompound.getBoolean("Hungry");
    this.goatlevel = nbttagcompound.getInteger("GoatLevel");
    this.goatsize = nbttagcompound.getFloat("GoatSize");
    this.modelspeed = nbttagcompound.getFloat("ModelSpeed");
    this.angry = nbttagcompound.getBoolean("Angry");
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    return "morecreeps:googoat";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:googoathurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:googoatdead";
  }

  public void confetti() {
    MoreCreepsAndWeirdos.proxy.confettiA(this, this.worldObj);
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    Object obj = damagesource.getEntity();

    if ((obj instanceof CREEPSEntityRocket) && ((CREEPSEntityRocket) obj).owner != null) {
      obj = ((CREEPSEntityRocket) obj).owner;
    }

    if (obj instanceof EntityPlayer) {
      MoreCreepsAndWeirdos.goatcount++;
      boolean flag = false;
      EntityPlayerMP player = (EntityPlayerMP) obj;

      if (!player.func_147099_x().hasAchievementUnlocked(MoreCreepsAndWeirdos.achievegookill)) {
        flag = true;
        player.addStat(MoreCreepsAndWeirdos.achievegookill, 1);
        this.confetti();
      }

      if (!player.func_147099_x().hasAchievementUnlocked(MoreCreepsAndWeirdos.achievegookill10)
          && MoreCreepsAndWeirdos.goatcount >= 10) {
        flag = true;
        player.addStat(MoreCreepsAndWeirdos.achievegookill10, 1);
        this.confetti();
      }

      if (!player.func_147099_x().hasAchievementUnlocked(MoreCreepsAndWeirdos.achievegookill25)
          && MoreCreepsAndWeirdos.goatcount >= 25) {
        flag = true;
        player.addStat(MoreCreepsAndWeirdos.achievegookill25, 1);
        this.confetti();
      }

      if (flag) {
        this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
      }
    }

    int i = (this.goatlevel - 1) + this.rand.nextInt(2);

    if (i > 0) {
      if (!this.worldObj.isRemote) {
        this.dropItem(MoreCreepsAndWeirdos.goodonut, i);
      }
    }

    super.onDeath(damagesource);
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 2;
  }

  @Override
  public EntityAgeable createChild(EntityAgeable ageable) {
    return new CREEPSEntityGooGoat(this.worldObj);
  }
}
