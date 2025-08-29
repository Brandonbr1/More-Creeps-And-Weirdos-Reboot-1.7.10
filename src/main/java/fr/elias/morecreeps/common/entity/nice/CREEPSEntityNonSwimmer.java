package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityNonSwimmer extends EntityAnimal {

  protected double attackRange;
  private int waittime;
  public boolean swimming;
  public float modelsize;
  public boolean saved;
  public int timeonland;
  public boolean towel;
  public boolean wet;
  public String texture;

  public CREEPSEntityNonSwimmer(World world) {
    super(world);
    this.texture = "morecreeps:textures/entity/nonswimmer.png";
    this.attackRange = 16D;
    this.timeonland = 0;
    this.wet = false;
    this.swimming = false;
    this.saved = false;
    this.towel = false;
    this.waittime = this.rand.nextInt(1500) + 500;
    this.modelsize = 1.0F;
    this.setSize(this.width * 1, this.height * 2);
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
    this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
    this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
    this.tasks.addTask(8, new EntityAILookIdle(this));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
  }

  /**
   * This function is used when two same-species animals in 'love mode' breed to generate the new
   * baby animal.
   */
  @Override
  public EntityAnimal createChild(EntityAgeable entityanimal) {
    return new CREEPSEntityNonSwimmer(this.worldObj);
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    IAttributeInstance movementSpeedAttribute =
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    double waterMovementSpeed = 0.05D;
    double defaultMovementSpeed = 0.5D;

    if (this.inWater) {
      movementSpeedAttribute.setBaseValue(waterMovementSpeed);
      this.swimming = true;
      this.wet = true;
    } else {
      movementSpeedAttribute.setBaseValue(defaultMovementSpeed);

      int posXFloor = MathHelper.floor_double(this.posX);
      int posYFloor = MathHelper.floor_double(this.posY);
      int posZFloor = MathHelper.floor_double(this.posZ);

      Block block = this.getBlockAt(posXFloor, posYFloor, posZFloor);

      if (!this.isWaterBlock(block)) {
        this.swimming = false;
        EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, 3F);

        if (closestPlayer != null) {
          float distance = closestPlayer.getDistanceToEntity(this);

          if (distance < 4F && !this.saved && this.timeonland++ > 155 && this.wet) {
            this.giveReward((EntityPlayerMP) closestPlayer);
          }
        }
      }
    }

    if (this.saved && this.rand.nextInt(100) == 0 && !this.towel && this.onGround) {
      int towelPosX = MathHelper.floor_double(this.posX);
      int towelPosY = MathHelper.floor_double(this.posY);
      int towelPosZ = MathHelper.floor_double(this.posZ);

      Block towelBlock = this.getBlockAt(towelPosX, towelPosY, towelPosZ);

      if (!this.isWaterBlock(towelBlock)) {
        this.towel = true;
        CREEPSEntityTowel towelEntity = this.createTowelEntity();
        this.worldObj.spawnEntityInWorld(towelEntity);
        this.mountEntity(towelEntity);
      }
    }

    super.onLivingUpdate();
  }

  private Block getBlockAt(int x, int y, int z) {
    return this.worldObj.getBlock(x, y, z);
  }

  private boolean isWaterBlock(Block block) {
    return block == Blocks.water || block == Blocks.flowing_water;
  }

  private CREEPSEntityTowel createTowelEntity() {
    int textureIndex = this.rand.nextInt(6);
    String texturePath = "morecreeps:textures/entity/towel" + textureIndex + ".png";
    String baseTexturePath = "/textures/entity/towel" + textureIndex + ".png";

    CREEPSEntityTowel towelEntity = new CREEPSEntityTowel(this.worldObj);
    towelEntity.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
    towelEntity.texture = texturePath;
    towelEntity.basetexture = baseTexturePath;

    return towelEntity;
  }

  public void giveReward(EntityPlayerMP entityplayersp) {
    if (!entityplayersp
        .func_147099_x()
        .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievenonswimmer)) {
      this.confetti(entityplayersp);
      this.worldObj.playSoundAtEntity(entityplayersp, "morecreeps:achievement", 1.0F, 1.0F);
      entityplayersp.addStat(MoreCreepsAndWeirdos.achievenonswimmer, 1);
    }

    if (this.rand.nextInt(5) == 0) {
      this.worldObj.playSoundAtEntity(this, "morecreeps:nonswimmersorry", 1.0F, 1.0F);
      return;
    }

    this.worldObj.playSoundAtEntity(this, "morecreeps:nonswimmerreward", 1.0F, 1.0F);
    this.saved = true;
    int i = this.rand.nextInt(5) + 1;
    this.faceEntity(entityplayersp, 0.0F, 0.0F);

    if (!this.worldObj.isRemote) {
      EntityItem entityitem;

      switch (i) {
        case 1:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.lolly, this.rand.nextInt(2) + 1, 0), 1.0F);
          break;

        case 2:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.lolly, 1, 0), 1.0F);
          break;

        case 3:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(10) + 1, 0), 1.0F);
          break;

        case 4:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(30) + 1, 0), 1.0F);
          break;

        case 5:
          entityitem = this.entityDropItem(new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
          break;

        default:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(3) + 1, 0), 1.0F);
          break;
      }

      double d = -MathHelper.sin((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
      entityitem.posX = entityplayersp.posX + d * 0.5D;
      entityitem.posY = entityplayersp.posY + 0.5D;
      entityitem.posZ = entityplayersp.posZ + d1 * 0.5D;
    }
  }

  public int[] findTree(Entity entity, Double double1) {
    AxisAlignedBB axisalignedbb = entity.boundingBox.expand(double1, double1, double1);
    int i = MathHelper.floor_double(axisalignedbb.minX);
    int j = MathHelper.floor_double(axisalignedbb.maxX + 1.0D);
    int k = MathHelper.floor_double(axisalignedbb.minY);
    int l = MathHelper.floor_double(axisalignedbb.maxY + 1.0D);
    int i1 = MathHelper.floor_double(axisalignedbb.minZ);
    int j1 = MathHelper.floor_double(axisalignedbb.maxZ + 1.0D);

    for (int k1 = i; k1 < j; k1++) {
      for (int l1 = k; l1 < l; l1++) {
        for (int i2 = i1; i2 < j1; i2++) {
          Block j2 = this.worldObj.getBlock(k1, l1, i2);

          if (j2 != Blocks.air && (j2 == Blocks.water || j2 == Blocks.flowing_water))
            return (new int[] {k1, l1, i2});
        }
      }
    }

    return (new int[] {-1, 0, 0});
  }

  /**
   * Takes a coordinate in and returns a weight to determine how likely this creature will try to
   * path to the block. Args: x, y, z
   */
  @Override
  public float getBlockPathWeight(int x, int y, int z) {
    if (this.worldObj.getBlock(x, y, z) == Blocks.water
        || this.worldObj.getBlock(x, y, z) == Blocks.flowing_water) return 10F;
    else return -(float) y;
  }

  /** Returns the Y Offset of this entity. */
  @Override
  public double getYOffset() {
    if (this.ridingEntity instanceof CREEPSEntityTowel) return -1.85D;
    else return 0.0D;
  }

  @Override
  public void updateRiderPosition() {
    this.riddenByEntity.setPosition(
        this.posX,
        this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(),
        this.posZ);
  }

  /** Returns the Y offset from the entity's position for any entity riding this one. */
  @Override
  public double getMountedYOffset() {
    return 0.5D;
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (entity instanceof EntityPlayer) {
      this.motionY = 0.25D;

      if (i == 1) {
        i = 0;
      }
    }

    this.mountEntity(null);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.55D);
    super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    return true;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("modelsize", this.modelsize);
    nbttagcompound.setBoolean("saved", this.saved);
    nbttagcompound.setBoolean("towel", this.towel);
    nbttagcompound.setBoolean("wet", this.wet);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.modelsize = nbttagcompound.getFloat("modelsize");
    this.saved = nbttagcompound.getBoolean("saved");
    this.towel = nbttagcompound.getBoolean("towel");
    this.wet = nbttagcompound.getBoolean("wet");
  }

  /** knocks back this entity */
  @Override
  public void knockBack(Entity entity, float i, double d, double d1) {
    if (entity instanceof EntityPlayer) {
      double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
      double d3 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
      this.motionX = d2 * 0.5D;
      this.motionZ = d3 * 0.5D;
    }
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getFullBlockLightValue(i, j, k);
    Block i1 = this.worldObj.getBlock(i, j, k);
    int j1 = this.worldObj.countEntities(CREEPSEntityNonSwimmer.class);
    return (i1 == Blocks.flowing_water || i1 == Blocks.water)
        && i1 != Blocks.cobblestone
        && i1 != Blocks.log
        && i1 != Blocks.double_stone_slab
        && i1 != Blocks.stone_slab
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox()).size() == 0
        && this.worldObj.canBlockSeeTheSky(i, j, k)
        && this.rand.nextInt(25) == 0
        && l > 9
        && j1 < 4;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  private void smoke() {
    if (this.worldObj.isRemote) {
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 2; j++) {
          double d = this.rand.nextGaussian() * 0.02D;
          double d1 = this.rand.nextGaussian() * 0.02D;
          double d2 = this.rand.nextGaussian() * 0.02D;
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width) + i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i) - this.width,
              d,
              d1,
              d2);
          this.worldObj.spawnParticle(
              EnumParticleTypes.EXPLOSION_NORMAL,
              (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i,
              this.posY + this.rand.nextFloat() * this.height,
              (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i - this.width,
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
    if (this.swimming) return "morecreeps:nonswimmer";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:nonswimmerhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:nonswimmerdeath";
  }

  public void confetti(EntityPlayer player) {
    double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
    CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
    creepsentitytrophy.setLocationAndAngles(
        player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
    this.worldObj.spawnEntityInWorld(creepsentitytrophy);
  }
}
