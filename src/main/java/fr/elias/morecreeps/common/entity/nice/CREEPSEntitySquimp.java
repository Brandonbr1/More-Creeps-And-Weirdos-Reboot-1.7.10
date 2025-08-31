package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.common.entity.proj.CREEPSEntityDesertLizardFireball;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CREEPSEntitySquimp extends EntityWaterMob {

  // private boolean foundplayer; // TODO (unused)
  // private boolean stolen; // TODO (unused)
  // private PathEntity pathToEntity; // TODO (unused)
  protected Entity playerToAttack;

  /** returns true if a creature has attacked recently only used for creepers and skeletons */
  protected boolean hasAttacked;

  protected ItemStack stolengood;
  private float distance;
  public int itemnumber;
  public int stolenamount;
  public String texture;
  public double moveSpeed;

  public CREEPSEntitySquimp(World world) {
    super(world);
    texture = "morecreeps:textures/entity/squimp.png";
    moveSpeed = 0.0F;
    this.setHealth(rand.nextInt(20) + 10);
    // stolen = false;
    hasAttacked = false; // TODO (unused)
    // foundplayer = false;  // TODO (unused)
    tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8F));
  }

  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(moveSpeed);
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
  }

  /**
   * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
   * attacking (Animals, Spiders at day, peaceful PigZombies).
   */
  protected Entity findPlayerToAttack() {
    if (worldObj == null) {
      return null;
    }
    EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 20D);

    if (entityplayer != null) {
      distance = getDistanceToEntity(entityplayer);

      if (distance < 16F && inWater) {
        motionY += 2D;

        for (int i = 0; i < 4; i++) {
          float f = 0.25F;
          worldObj.spawnParticle(
              EnumParticleTypes.WATER_BUBBLE,
              posX - motionX * (double) f,
              posY - motionY * (double) f,
              posZ - motionZ * (double) f,
              motionX,
              motionY,
              motionZ);
        }

        for (int j = 0; j < 3; j++) {
          double d = entityplayer.posX - posX;
          double d1 =
              (entityplayer.posY + (double) (entityplayer.height / 2.0F))
                  - (posY + (double) (height / 2.0F));
          double d2 = (entityplayer.posZ - posZ) + 0.5D;
          renderYawOffset = rotationYaw = (-(float) Math.atan2(d, d2) * 180F) / (float) Math.PI;
          worldObj.playSoundAtEntity(
              this,
              "morecreeps:desertlizardfireball",
              getSoundVolume(),
              (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
          CREEPSEntityDesertLizardFireball creepsentitydesertlizardfireball =
              new CREEPSEntityDesertLizardFireball(worldObj, this, d, d1, d2);
          double d3 = 4D;
          Vec3 vec3d = getLook(1.0F);
          creepsentitydesertlizardfireball.posX = posX + vec3d.xCoord * d3;
          creepsentitydesertlizardfireball.posY = posY + (double) (height / 2.0F) + 0.5D + 1.0D;
          creepsentitydesertlizardfireball.posZ = posZ + vec3d.zCoord * d3 + (double) (1 - j);
          worldObj.spawnEntityInWorld(creepsentitydesertlizardfireball);
        }
      }
    }

    return null;
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  public void onLivingUpdate() {
    super.onLivingUpdate();
    motionY *= 0.87999999523162842D;
  }

  /** Returns the sound this mob makes while it's alive. */
  protected String getLivingSound() {
    return "morecreeps:thief";
  }

  /** Returns the sound this mob makes when it is hurt. */
  protected String getHurtSound() {
    return "morecreeps:thiefhurt";
  }

  /** Returns the sound this mob makes on death. */
  protected String getDeathSound() {
    return "morecreeps:thiefdeath";
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  public boolean getCanSpawnHere() {
    if (worldObj == null) {
      return false;
    }
    AxisAlignedBB box = getBoundingBox();
    if (box == null) {
      return false;
    }
    int i = MathHelper.floor_double(posX);
    int j = MathHelper.floor_double(box.minY);
    int k = MathHelper.floor_double(posZ);
    int l = worldObj.getBlockLightOpacity(i, j, k);
    Block i1 = worldObj.getBlock(i, j - 1, k);
    return i1 != Blocks.snow
        && i1 != Blocks.cobblestone
        && i1 != Blocks.planks
        && i1 != Blocks.wool
        && worldObj.getCollidingBoundingBoxes(this, box).size() == 0
        && worldObj.checkBlockCollision(box)
        && worldObj.canBlockSeeTheSky(i, j, k)
        && rand.nextInt(15) == 0
        && l > 8;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  /** Called when the mob's health reaches 0. */
  public void onDeath(DamageSource damagesource) {
    dropItem(Items.fish, rand.nextInt(2) + 1);
    super.onDeath(damagesource);
  }
}
