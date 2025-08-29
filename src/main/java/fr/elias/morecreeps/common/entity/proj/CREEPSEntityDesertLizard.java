package fr.elias.morecreeps.common.entity.proj;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityDesertLizard extends EntityMob {

  public int fireballTime;
  private Entity targetedEntity;
  public float modelsize;
  static final String lizardTextures[] = {
    "morecreeps:textures/entity/desertlizard1.png",
    "morecreeps:textures/entity/desertlizard2.png",
    "morecreeps:textures/entity/desertlizard3.png",
    "morecreeps:textures/entity/desertlizard4.png",
    "morecreeps:textures/entity/desertlizard5.png"
  };
  public String texture;

  public CREEPSEntityDesertLizard(World world) {
    super(world);
    this.texture = lizardTextures[this.rand.nextInt(lizardTextures.length)];
    this.setSize(2.0F, 1.5F);
    this.fireballTime = this.rand.nextInt(300) + 250;
    this.modelsize = 1.0F;
    this.targetTasks.addTask(0, new CREEPSEntityDesertLizard.AIAttackEntity());
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.55D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.modelsize = nbttagcompound.getFloat("ModelSize");
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
        && l > 10
        && super.getCanSpawnHere();
    // return (i1 == Blocks.sand || i1 == Blocks.gravel) && i1 != Blocks.cobblestone && i1 !=
    // Blocks.log && i1 !=
    // Blocks.double_stone_slab && i1 != Blocks.stone_slab && i1 != Blocks.planks && i1 !=
    // Blocks.wool &&
    // worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&
    // worldObj.canSeeSky(new BlockPos(i,
    // j, k)) && rand.nextInt(5) == 0; //&& l > 10;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 3;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    if (this.fireballTime-- < 1) {
      this.fireballTime = this.rand.nextInt(300) + 250;
      double d = 64D;
      this.targetedEntity = this.worldObj.getClosestPlayerToEntity(this, 30D);

      if (this.targetedEntity != null
          && this.canEntityBeSeen(this.targetedEntity)
          && (this.targetedEntity instanceof EntityPlayer)) {
        double d1 = this.targetedEntity.getDistanceSqToEntity(this);

        if (d1 < d * d && d1 > 10D) {
          double d2 = this.targetedEntity.posX - this.posX;
          double d3 =
              (this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F)
                  - (this.posY + this.height / 2.0F);
          double d4 = (this.targetedEntity.posZ - this.posZ) + 0.5D;
          this.renderYawOffset =
              this.rotationYaw = (-(float) Math.atan2(d2, d4) * 180F) / (float) Math.PI;
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:desertlizardfireball",
              this.getSoundVolume(),
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          CREEPSEntityDesertLizardFireball creepsentitydesertlizardfireball =
              new CREEPSEntityDesertLizardFireball(this.worldObj, this, d2, d3, d4);
          double d5 = 4D;
          Vec3 vec3d = this.getLook(1.0F);
          creepsentitydesertlizardfireball.posX = this.posX + vec3d.xCoord * d5;
          creepsentitydesertlizardfireball.posY = this.posY + this.height / 2.0F + 0.5D;
          creepsentitydesertlizardfireball.posZ = this.posZ + vec3d.zCoord * d5;
          this.worldObj.spawnEntityInWorld(creepsentitydesertlizardfireball);
        }
      }
    }

    super.onLivingUpdate();
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
    return "morecreeps:desertlizard";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:desertlizardhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:desertlizarddeath";
  }

  @Override
  protected void attackEntity(Entity entity, float f) {
    if (f > 2.0F && f < 4F && this.rand.nextInt(10) == 0 && this.onGround) {
      double d = entity.posX - this.posX;
      double d1 = entity.posZ - this.posZ;
      float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
      this.motionX = (d / f2) * 0.7D * 0.8000000019209289D + this.motionX * 0.20000000098023224D;
      this.motionZ = (d1 / f2) * 0.7D * 0.70000000192092893D + this.motionZ * 0.20000000098023224D;
      this.motionY = 0.40000000196046448D;
      this.attackEntityAsMob(entity);
    }
  }

  class AIAttackEntity extends EntityAINearestAttackableTarget {

    public AIAttackEntity() {
      super(CREEPSEntityDesertLizard.this, EntityPlayer.class, 0, true);
    }

    @Override
    public void updateTask() {
      EntityLivingBase target = CREEPSEntityDesertLizard.this.getAttackTarget();
      if (target == null) return;
      float f = CREEPSEntityDesertLizard.this.getDistanceToEntity(target);
      CREEPSEntityDesertLizard.this.attackEntity(target, f);
    }

    @Override
    public boolean shouldExecute() {
      EntityLivingBase target = CREEPSEntityDesertLizard.this.getAttackTarget();
      return target != null && super.shouldExecute();
    }
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    if (!this.worldObj.isRemote) {
      if (this.rand.nextInt(5) == 0) {
        this.dropItem(Items.cooked_porkchop, this.rand.nextInt(3) + 1);
      } else {
        this.dropItem(Item.getItemFromBlock(Blocks.sand), this.rand.nextInt(3) + 1);
      }
    }

    super.onDeath(damagesource);
  }
}
