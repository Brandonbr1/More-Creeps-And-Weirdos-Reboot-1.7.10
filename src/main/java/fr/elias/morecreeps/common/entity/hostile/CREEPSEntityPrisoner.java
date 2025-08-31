package fr.elias.morecreeps.common.entity.hostile;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityPrisoner extends EntityMob {

  static final String prisonerTextures[] = {
    "morecreeps:textures/entity/prisoner1.png",
    "morecreeps:textures/entity/prisoner2.png",
    "morecreeps:textures/entity/prisoner3.png",
    "morecreeps:textures/entity/prisoner4.png",
    "morecreeps:textures/entity/prisoner5.png"
  };
  protected double attackRange;
  // private int waittime; // TODO (unused)
  public float modelsize;
  public boolean saved;
  public int timeonland;
  public boolean evil;
  public String texture;

  public CREEPSEntityPrisoner(World world) {
    super(world);
    this.texture = prisonerTextures[this.rand.nextInt(prisonerTextures.length)];
    this.attackRange = 16D;
    this.timeonland = 0;
    this.saved = false;
    // this.waittime = this.rand.nextInt(1500) + 500; // TODO (unused)
    this.modelsize = 1.0F;

    if (this.rand.nextInt(2) == 0) {
      this.evil = true;
    } else {
      this.evil = false;
    }
    this.getNavigator().setBreakDoors(true);
    this.tasks.addTask(0, new EntityAISwimming(this));
    this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.4D, true));
    this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
    this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
    this.tasks.addTask(4, new EntityAILookIdle(this));
    this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    this.targetTasks.addTask(
        2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(25D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.45D);
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return false;
  }

  @Override
  public double getYOffset() {
    return this.inWater ? -1.4D : 0.0D;
  }

  @Override
  public void onLivingUpdate() {
    if (this.inWater) {
      this.getYOffset();
    } else {
      EntityPlayer entityplayersp = this.worldObj.getClosestPlayerToEntity(this, 2D);

      if (entityplayersp != null) {
        float f = entityplayersp.getDistanceToEntity(this);

        if (f < 3F
            && this.canEntityBeSeen(entityplayersp)
            && !this.saved
            && this.timeonland++ > 50
            && !this.evil) {
          this.giveReward(entityplayersp);
        }
      }
    }

    super.onLivingUpdate();
  }

  public void giveReward(EntityPlayer player) {
    MoreCreepsAndWeirdos.prisonercount++;

    if (!((EntityPlayerMP) player)
        .func_147099_x()
        .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieveprisoner)) {
      this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
      player.addStat(MoreCreepsAndWeirdos.achieveprisoner, 1);
      this.confetti(player);
    } else if (!((EntityPlayerMP) player)
            .func_147099_x()
            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve5prisoner)
        && MoreCreepsAndWeirdos.prisonercount == 5) {
      this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
      player.addStat(MoreCreepsAndWeirdos.achieve5prisoner, 1);
      this.confetti(player);
    } else if (!((EntityPlayerMP) player)
            .func_147099_x()
            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve10prisoner)
        && MoreCreepsAndWeirdos.prisonercount == 10) {
      this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
      player.addStat(MoreCreepsAndWeirdos.achieve10prisoner, 1);
      this.confetti(player);
    }

    if (this.rand.nextInt(4) == 0) {
      this.worldObj.playSoundAtEntity(this, "morecreeps:prisonersorry", 1.0F, 1.0F);
      return;
    }

    this.worldObj.playSoundAtEntity(this, "morecreeps:prisonerreward", 1.0F, 1.0F);
    this.saved = true;
    int i = this.rand.nextInt(4) + 1;
    this.faceEntity(player, 0.0F, 0.0F);

    if (player != null && !this.worldObj.isRemote) {
      EntityItem entityitem = null;

      switch (i) {
        case 1:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.lolly, this.rand.nextInt(2) + 1, 0), 1.0F);
          break;

        case 2:
          entityitem = this.entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
          break;

        case 3:
          entityitem = this.entityDropItem(new ItemStack(Items.cake, 1, 0), 1.0F);
          break;

        case 4:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(20) + 1, 0), 1.0F);
          break;

        default:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(5) + 1, 0), 1.0F);
          break;
      }

      double d = -MathHelper.sin(((player).rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos(((player).rotationYaw * (float) Math.PI) / 180F);
      entityitem.posX = (player).posX + d * 0.5D;
      entityitem.posY = (player).posY + 0.5D;
      entityitem.posZ = (player).posZ + d1 * 0.5D;
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (entity instanceof EntityPlayer) {
      this.evil = true;
    }

    super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    return true;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("modelsize", this.modelsize);
    nbttagcompound.setBoolean("saved", this.saved);
    nbttagcompound.setBoolean("evil", this.evil);
    nbttagcompound.setString("Texture", this.texture);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.modelsize = nbttagcompound.getFloat("modelsize");
    this.saved = nbttagcompound.getBoolean("saved");
    this.evil = nbttagcompound.getBoolean("evil");
    this.texture = nbttagcompound.getString("Texture");
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

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.rand.nextInt(5) == 0) return "morecreeps:prisoner";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:prisonerhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:prisonerdeath";
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
