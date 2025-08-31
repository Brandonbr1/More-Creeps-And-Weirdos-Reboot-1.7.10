package fr.elias.morecreeps.common.entity.hostile;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class CREEPSEntityBum extends EntityMob {

  ResourceLocation resource;
  public boolean rideable;
  protected double attackRange;
  private int angerLevel;
  private int value;
  private boolean bumgave;
  public int timetopee;
  public float bumrotation;
  public float modelsize;
  public ResourceLocation texture;

  public CREEPSEntityBum(World world) {

    super(world);
    // The texture reference
    this.texture =
        new ResourceLocation(
            Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BUM);
    this.angerLevel = 0;
    this.attackRange = 16D;
    this.bumgave = false;
    this.timetopee = this.rand.nextInt(900) + 500;
    this.bumrotation = 999F;
    this.modelsize = 1.0F;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
    this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5D);
  }

  public String pingText() {
    return (new StringBuilder()).append("angerLevel ").append(this.angerLevel).toString();
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();
    if (this.timetopee-- < 0 && !this.bumgave && CREEPSConfig.publicUrination) {
      this.isJumping = false;

      if (this.bumrotation == 999F) {
        this.bumrotation = this.rotationYaw;
      }

      this.rotationYaw = this.bumrotation;
      // moveSpeed = 0.0F; // TODO (unused)

      if (!this.onGround) {
        this.motionY -= 0.5D;
      }

      if (this.worldObj.isRemote) {
        MoreCreepsAndWeirdos.proxy.pee(
            this.worldObj, this.posX, this.posY, this.posZ, this.rotationYaw, this.modelsize);
      }

      if (this.timetopee < -200) {
        this.timetopee = this.rand.nextInt(600) + 600;
        this.bumrotation = 999F;
        int j = MathHelper.floor_double(this.posX);
        int k = MathHelper.floor_double(this.posY);
        int l = MathHelper.floor_double(this.posZ);

        for (int i1 = -1; i1 < 2; i1++) {
          for (int j1 = -1; j1 < 2; j1++) {
            if (this.rand.nextInt(3) != 0) {
              continue;
            }

            Block k1 = this.worldObj.getBlock(j + j1, k - 1, l - i1);
            Block l1 = this.worldObj.getBlock(j + j1, k, l - i1);

            if (this.rand.nextInt(2) == 0) {
              if ((k1 == Blocks.grass || k1 == Blocks.dirt) && l1 == Blocks.air) {
                this.worldObj.setBlock(j + j1, k, l - i1, Blocks.yellow_flower);
              }

              continue;
            }

            if ((k1 == Blocks.grass || k1 == Blocks.dirt) && l1 == Blocks.air) {
              this.worldObj.setBlock(j + j1, k, l - i1, Blocks.red_flower);
            }
          }
        }
      }
    }
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    Entity entity = damagesource.getEntity();

    if (entity instanceof EntityPlayer) {
      this.setRevengeTarget((EntityLivingBase) entity);
      this.becomeAngryAt(entity);
    }

    this.timetopee = this.rand.nextInt(900) + 500;
    this.bumrotation = 999F;
    super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    return true;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setShort("Anger", (short) this.angerLevel);
    nbttagcompound.setBoolean("BumGave", this.bumgave);
    nbttagcompound.setInteger("TimeToPee", this.timetopee);
    nbttagcompound.setFloat("BumRotation", this.bumrotation);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.angerLevel = nbttagcompound.getShort("Anger");
    this.bumgave = nbttagcompound.getBoolean("BumGave");
    this.timetopee = nbttagcompound.getInteger("TimeToPee");
    this.bumrotation = nbttagcompound.getFloat("BumRotation");
    this.modelsize = nbttagcompound.getFloat("ModelSize");

    if (this.bumgave) {
      this.texture =
          new ResourceLocation(
              Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BUM_DRESSED);
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
        && l > 8
        && super.getCanSpawnHere();
    // Method used by Minecraft above, probably better to use it instead?
    // Block i1 = worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
    // return i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.stone_slab && i1 !=
    // Blocks.double_stone_slab && i1 != Blocks.planks && i1 != Blocks.wool &&
    // worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0 &&
    // worldObj.canSeeSky(new BlockPos(i,
    // j, k)) && rand.nextInt(10) == 0 && l > 8;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  /**
   * TEMPORARILY REMOVED TO FIND AN ALTERNATIVE TO THESE FUNCTIONS Above null for now, testing to
   * see if it works in 1.7.10
   */
  /*
   * protected Entity findPlayerToAttack()
   * {
   * if (angerLevel == 0)
   * {
   * return null;EntityPigZombie
   * }
   * else
   * {
   * return super.findPlayerToAttack();
   * }
   * }
   * public boolean canAttackEntity(Entity entity, int i)
   * {
   * if (entity instanceof EntityPlayer)
   * {
   * List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().expand(32D, 32D, 32D));
   * for (int j = 0; j < list.size(); j++)
   * {
   * Entity entity1 = (Entity)list.get(j);
   * if (entity1 instanceof CREEPSEntityBum)
   * {
   * CREEPSEntityBum creepsentitybum = (CREEPSEntityBum)entity1;
   * creepsentitybum.becomeAngryAt(entity);
   * }
   * }
   * becomeAngryAt(entity);
   * }
   * return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
   * }
   * private void becomeAngryAt(Entity entity)
   * {
   * entityToAttack = entity;
   * angerLevel = 400 + rand.nextInt(400);
   * }
   */

  /** Simple try if it work or not * */
  private void becomeAngryAt(Entity p_70835_1_) {
    this.angerLevel = 400 + this.rand.nextInt(400);

    if (p_70835_1_ instanceof EntityLivingBase) {
      this.setRevengeTarget((EntityLivingBase) p_70835_1_);
    }
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    ItemStack itemstack = entityplayer.inventory.getCurrentItem();

    if (!this.bumgave && this.angerLevel == 0) {
      if (itemstack != null
          && (itemstack.getItem() == Items.diamond
              || itemstack.getItem() == Items.gold_ingot
              || itemstack.getItem() == Items.iron_ingot)) {
        if (itemstack.getItem() == Items.iron_ingot) {
          this.value = this.rand.nextInt(2) + 1;
        } else if (itemstack.getItem() == Items.gold_ingot) {
          this.value = this.rand.nextInt(5) + 1;
        } else if (itemstack.getItem() == Items.diamond) {
          this.value = this.rand.nextInt(10) + 1;
        }

        if (itemstack.stackSize - 1 == 0) {
          entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
        } else {
          itemstack.stackSize--;
        }

        for (int i = 0; i < 4; i++) {
          for (int i1 = 0; i1 < 10; i1++) {
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d3 = this.rand.nextGaussian() * 0.02D;
            double d6 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(
                "EXPLOSION".toLowerCase(),
                (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                this.posY + this.rand.nextFloat() * this.height + i,
                (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                d1,
                d3,
                d6);
          }
        }

        this.texture =
            new ResourceLocation(
                Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BUM_DRESSED);
        this.angerLevel = 0;
        this.findPlayerToAttack();

        if (this.rand.nextInt(5) == 0) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumsucker",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.bumgave = true;
        } else {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumthankyou",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.bumgave = true;

          for (int j = 0; j < 10; j++) {
            double d = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d5 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(
                "EXPLOSION".toLowerCase(),
                (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                this.posY + this.rand.nextFloat() * this.height,
                (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                d,
                d2,
                d5);
          }

          for (int k = 0; k < this.value; k++) {
            this.dropItem(Item.getItemById(this.rand.nextInt(95)), 1);
            this.dropItem(Items.iron_shovel, 1);
          }

          return true;
        }
      } else if (itemstack != null) {
        if (this.timetopee > 0) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumdontwant",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        } else if (itemstack != null
            && (itemstack.getItem() == Item.getItemFromBlock(Blocks.yellow_flower)
                || itemstack.getItem() == Item.getItemFromBlock(Blocks.red_flower))) {
          // func_147099_x() is the stat file, could of named it better :P
          if (!((EntityPlayerMP) entityplayer)
              .func_147099_x()
              .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievebumflower)) {
            this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            entityplayer.addStat(MoreCreepsAndWeirdos.achievebumflower, 1);
            this.confetti(entityplayer);
          }

          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumthanks",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.timetopee = this.rand.nextInt(1900) + 1500;

          if (itemstack.stackSize - 1 == 0) {
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, null);
          } else {
            itemstack.stackSize--;
          }
        } else if (itemstack != null && itemstack.getItem() == Items.bucket) {
          if (!((EntityPlayerMP) entityplayer)
                  .func_147099_x()
                  .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievebumpot)
              && ((EntityPlayerMP) entityplayer)
                  .func_147099_x()
                  .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievebumflower)) {
            this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            entityplayer.addStat(MoreCreepsAndWeirdos.achievebumpot, 1);
            this.confetti(entityplayer);
          }
          entityplayer.addStat(MoreCreepsAndWeirdos.achievebumpot, 1);
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumthanks",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.timetopee = this.rand.nextInt(1900) + 1500;

          if (itemstack.stackSize - 1 == 0) {
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, null);
          } else {
            itemstack.stackSize--;
          }
        } else if (itemstack != null && itemstack.getItem() == Items.lava_bucket) {
          if (!((EntityPlayerMP) entityplayer)
                  .func_147099_x()
                  .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievebumlava)
              && ((EntityPlayerMP) entityplayer)
                  .func_147099_x()
                  .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievebumpot)) {
            this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
            entityplayer.addStat(MoreCreepsAndWeirdos.achievebumlava, 1);
            this.confetti(entityplayer);
          }

          entityplayer.addStat(MoreCreepsAndWeirdos.achievebumpot, 1);
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumthanks",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
          this.timetopee = this.rand.nextInt(1900) + 1500;

          if (itemstack.stackSize - 1 == 0) {
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, null);
          } else {
            itemstack.stackSize--;
          }

          int l = (int) this.posX;
          int j1 = (int) this.posY;
          int k1 = (int) this.posZ;

          if (this.rand.nextInt(4) == 0) {
            for (int l1 = 0; l1 < this.rand.nextInt(3) + 1; l1++) {
              Blocks.obsidian.dropBlockAsItem(
                  this.worldObj, l, j1, k1, this.worldObj.getBlockMetadata(l, j1, k1), 0);
            }
          }

          for (int i2 = 0; i2 < 15; i2++) {
            double d4 = l + this.worldObj.rand.nextFloat();
            double d7 = j1 + this.worldObj.rand.nextFloat();
            double d8 = k1 + this.worldObj.rand.nextFloat();
            double d9 = d4 - this.posX;
            double d10 = d7 - this.posY;
            double d11 = d8 - this.posZ;
            double d12 = MathHelper.sqrt_double(d9 * d9 + d10 * d10 + d11 * d11);
            d9 /= d12;
            d10 /= d12;
            d11 /= d12;
            double d13 = 0.5D / (d12 / 10D + 0.10000000000000001D);
            d13 *= this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F;
            d9 *= d13;
            d10 *= d13;
            d11 *= d13;
            this.worldObj.spawnParticle(
                "SMOKE".toLowerCase(),
                (d4 + this.posX * 1.0D) / 2D,
                (d7 + this.posY * 1.0D) / 2D + 2D,
                (d8 + this.posZ * 1.0D) / 2D,
                d9,
                d10,
                d11);
            this.worldObj.spawnParticle("SMOKE".toLowerCase(), d4, d7, d8, d9, d10, d11);
          }

          if (this.rand.nextInt(4) == 0) {
            entityplayer.inventory.setInventorySlotContents(
                entityplayer.inventory.currentItem, new ItemStack(Items.bucket));
          }
        } else if (!this.bumgave) {
          this.worldObj.playSoundAtEntity(
              this,
              "morecreeps:bumpee",
              1.0F,
              (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }
      }
    } else {
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:bumleavemealone",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    return false;
  }

  public void confetti(EntityPlayer player) {
    MoreCreepsAndWeirdos.proxy.confettiA(player, this.worldObj);
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.timetopee > 0 || this.bumgave || !CREEPSConfig.publicUrination)
      return "morecreeps:bum";
    else return "morecreeps:bumlivingpee";
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:bumhurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:bumdeath";
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    super.onDeath(damagesource);
    if (!this.worldObj.isRemote) {
      this.dropItem(Items.cooked_porkchop, 1);
    }
  }
}
