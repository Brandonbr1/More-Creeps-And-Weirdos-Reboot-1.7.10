package fr.elias.morecreeps.common.entity.nice;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntitySchlump extends EntityAnimal {

  World world;
  EntityPlayer entityplayer;
  protected double attackRange;
  private int waittime;
  public float modelsize;
  public boolean saved;
  public int age;
  public int agetimer;
  public int payouttimer;
  public boolean placed;
  public int deathtimer;
  public String texture;
  public double moveSpeed;
  public double health;

  public CREEPSEntitySchlump(World world) {
    super(world);
    this.texture =
        Reference.MOD_ID + ":" + Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_SCHLUMP;
    this.moveSpeed = 0.0F;
    // this.health = this.rand.nextInt(10) + 10;
    this.saved = false;
    this.waittime = this.rand.nextInt(1500) + 500;
    this.modelsize = 0.4F;
    this.setSize(this.width * this.modelsize, this.height * this.modelsize);
    this.age = 0;
    this.agetimer = 0;
    this.placed = false;
    this.deathtimer = -1;
  }

  @Override
  public IEntityLivingData onSpawnWithEgg(IEntityLivingData data) {

    this.health = this.rand.nextInt(10) + 10;
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(this.health);

    return super.onSpawnWithEgg(data);
  }

  @Override
  public void applyEntityAttributes() {
    super.applyEntityAttributes();
    this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1D);
    this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.1D);
  }

  /**
   * This function is used when two same-species animals in 'love mode' breed to generate the new
   * baby animal.
   */
  public EntityAnimal spawnBabyAnimal(EntityAnimal entityanimal) {
    return new CREEPSEntitySchlump(this.worldObj);
  }

  /**
   * Called frequently so the entity can update its state every tick as required. For example,
   * zombies and skeletons use this to react to sunlight and start to burn.
   */
  @Override
  public void onLivingUpdate() {
    if (this.inWater) {
      this.setDead();
    }
    super.onLivingUpdate();
  }

  /** Called to update the entity's position/logic. */
  @Override
  public void onUpdate() {
    this.ignoreFrustumCheck = true;

    if (this.agetimer++ > 50) {
      if (this.age < 22000) {
        this.age++;
      }

      if (this.age > 20000) {
        this.setDead();
      }

      if (this.age > 6000) {

        this.confetti();
        this.worldObj.playSoundAtEntity(this.entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
        this.entityplayer.addStat(MoreCreepsAndWeirdos.achieveschlump, 1);
      }

      if (this.modelsize < 3.5F) {
        this.modelsize += 0.001F;
      }

      this.agetimer = 0;
      int i = (this.age / 100) * 2;

      if (i > 150) {
        i = 150;
      }

      if (this.age > 200 && this.rand.nextInt(200 - i) == 0) {
        this.giveReward();
      }
    }

    if (!this.placed) {
      this.placed = true;

      if (!this.checkHouse()) {
        this.deathtimer = 200;
      }
    } else if (this.deathtimer-- == 0) {
      this.setDead();
    }

    super.onUpdate();
  }

  /** Determines if an entity can be despawned, used on idle far away entities */
  @Override
  protected boolean canDespawn() {
    return this.health < 1;
  }

  public boolean checkHouse() {
    boolean flag = false;
    List<?> list =
        this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this, this.boundingBox.expand(16D, 16D, 16D));
    int i = 0;

    do {
      if (i >= list.size()) {
        break;
      }

      Entity entity = (Entity) list.get(i);

      if (entity instanceof CREEPSEntitySchlump) {
        flag = true;
        break;
      }

      i++;
    } while (true);

    if (flag) {
      MoreCreepsAndWeirdos.proxy.addChatMessage("Too close to another Schlump. SCHLUMP OVERLOAD!");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-overload",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      return false;
    }

    i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.boundingBox.minY);
    int k = MathHelper.floor_double(this.posZ);

    if (this.worldObj.canBlockSeeTheSky(i, j, k)) {
      MoreCreepsAndWeirdos.proxy.addChatMessage("Your Schlump needs to be indoors or it will die!");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-indoors",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      return false;
    }

    if (this.worldObj.getBlockLightOpacity(i, j, k) > 11) {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "It is too bright in here for your little Schlump!");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-bright",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      return false;
    }

    int l = 0;

    for (int i1 = -2; i1 < 2; i1++) {
      for (int k1 = -2; k1 < 2; k1++) {
        for (int i2 = 0; i2 < 5; i2++) {
          if (this.worldObj.getBlock(
                  (int) this.posX + i1, (int) this.posY + i2, (int) this.posZ + k1)
              == Blocks.air) {
            l++;
          }
        }
      }
    }

    if (l < 60) {
      MoreCreepsAndWeirdos.proxy.addChatMessage("Your Schlump doesn't have enough room to grow!");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-room",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      return false;
    }

    int j1 = 0;

    for (int l1 = -5; l1 < 5; l1++) {
      for (int j2 = -5; j2 < 5; j2++) {
        for (int k2 = -5; k2 < 5; k2++) {
          Block l2 =
              this.worldObj.getBlock(
                  (int) this.posX + l1, (int) this.posY + k2, (int) this.posZ + j2);

          if (l2 == Blocks.wooden_door) {
            j1 += 10;
          }

          if (l2 == Blocks.iron_door) {
            j1 += 20;
          }

          if (l2 == Blocks.glass) {
            j1 += 5;
          }

          if (l2 == Blocks.chest) {
            j1 += 15;
          }

          if (l2 == Blocks.bed) {
            j1 += 20;
          }

          if (l2 == Blocks.bookshelf) {
            j1 += 15;
          }

          if (l2 == Blocks.brick_block) {
            j1 += 3;
          }

          if (l2 == Blocks.planks) {
            j1 += 3;
          }

          if (l2 == Blocks.wool) {
            j1 += 2;
          }

          if (l2 == Blocks.cake) {
            j1 += 10;
          }

          if (l2 == Blocks.furnace) {
            j1 += 15;
          }

          if (l2 == Blocks.lit_furnace) {
            j1 += 10;
          }

          if (l2 == Blocks.red_flower) {
            j1 += 5;
          }

          if (l2 == Blocks.red_flower) {
            j1 += 5;
          }

          if (l2 == Blocks.crafting_table) {
            j1 += 10;
          }
        }
      }
    }

    if (j1 > 275) {
      if (this.age < 10) {
        MoreCreepsAndWeirdos.proxy.addChatMessage(
            "This location is great! Your Schlump will love it here! ");
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:schlump-ok",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }

      return true;
    } else {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "This is not a good location for your Schlump. It will die here! ");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-sucks",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      return false;
    }
  }

  /**
   * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
   * pig.
   */
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    ItemStack itemstack = entityplayer.inventory.getCurrentItem();

    if (itemstack != null && itemstack.getItem() == MoreCreepsAndWeirdos.babyjarempty) {
      if (this.modelsize > 0.5F) {
        MoreCreepsAndWeirdos.proxy.addChatMessage("That Schlump is too big to fit in a jar! ");
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:schlump-big",
            1.0F,
            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        return true;
      }

      this.setDead();
      entityplayer.inventory.setInventorySlotContents(
          entityplayer.inventory.currentItem, new ItemStack(MoreCreepsAndWeirdos.babyjarfull));
    }

    return false;
  }

  /** Called when the entity is attacked. */
  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float i) {
    if (i < 1) {
      i = 1;
    }

    this.hurtTime = this.maxHurtTime = 10;
    this.smoke();

    if (this.health <= 0) {
      this.worldObj.playSoundAtEntity(
          this,
          this.getDeathSound(),
          this.getSoundVolume(),
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.onDeath(damagesource);
    } else {
      this.worldObj.playSoundAtEntity(
          this,
          this.getHurtSound(),
          this.getSoundVolume(),
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    super.attackEntityFrom(damagesource, i);
    return true;
  }

  public boolean checkItems() {
    int i = 0;
    Object obj = null;
    List<?> list =
        this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this, this.boundingBox.expand(6D, 6D, 6D));

    for (int j = 0; j < list.size(); j++) {
      Entity entity = (Entity) list.get(j);

      if (entity instanceof EntityItem) {
        i++;
      }
    }

    return i > 25;
  }

  public void giveReward() {
    if (!this.checkHouse()) {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "This is not a good location for your Schlump. It will die here!");
      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:schlump-sucks",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.deathtimer = 200;
      return;
    }

    if (this.checkItems()) return;

    this.worldObj.playSoundAtEntity(this, "morecreeps:schlump-reward", 1.0F, 1.0F);
    this.smallconfetti();
    int i = this.rand.nextInt(this.age / 100) + 1;

    if (i > 42) {
      i = 42;
    }

    if (this.entityplayer != null) {
      EntityItem entityitem = null;

      switch (i) {
        case 1:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.lolly, this.rand.nextInt(2) + 1, 0), 1.0F);
          break;

        case 2:
          entityitem = this.entityDropItem(new ItemStack(Items.wheat, 1, 0), 1.0F);
          break;

        case 3:
          entityitem = this.entityDropItem(new ItemStack(Items.wheat, 1, 0), 1.0F);
          break;

        case 4:
          entityitem = this.entityDropItem(new ItemStack(Items.wheat, 1, 0), 1.0F);
          break;

        case 5:
          entityitem = this.entityDropItem(new ItemStack(Items.wheat, 1, 0), 1.0F);
          break;

        case 6:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.bandaid, 1, 0), 1.0F);
          break;

        case 7:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.bandaid, 1, 0), 1.0F);
          break;

        case 8:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.bandaid, 1, 0), 1.0F);
          break;

        case 9:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.bandaid, 1, 0), 1.0F);
          break;

        case 10:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.bandaid, 1, 0), 1.0F);
          break;

        case 11:
          entityitem = this.entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
          break;

        case 12:
          entityitem = this.entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
          break;

        case 13:
          entityitem = this.entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
          break;

        case 14:
          entityitem = this.entityDropItem(new ItemStack(Items.bread, 1, 0), 1.0F);
          break;

        case 15:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(4) + 1, 0), 1.0F);
          break;

        case 16:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(4) + 1, 0), 1.0F);
          break;

        case 17:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(4) + 1, 0), 1.0F);
          break;

        case 18:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.lolly, this.rand.nextInt(2) + 1, 0), 1.0F);
          break;

        case 19:
          entityitem = this.entityDropItem(new ItemStack(Items.apple, 1, 0), 1.0F);
          break;

        case 20:
          entityitem = this.entityDropItem(new ItemStack(Items.apple, 1, 0), 1.0F);
          break;

        case 21:
          entityitem = this.entityDropItem(new ItemStack(Items.porkchop, 1, 0), 1.0F);
          break;

        case 22:
          entityitem = this.entityDropItem(new ItemStack(Items.coal, 1, 0), 1.0F);
          break;

        case 23:
          entityitem = this.entityDropItem(new ItemStack(Items.coal, 1, 0), 1.0F);
          break;

        case 24:
          entityitem = this.entityDropItem(new ItemStack(Items.melon_seeds, 1, 0), 1.0F);
          break;

        case 25:
          entityitem = this.entityDropItem(new ItemStack(Items.porkchop, 1, 0), 1.0F);
          break;

        case 26:
          entityitem = this.entityDropItem(new ItemStack(Items.porkchop, 1, 0), 1.0F);
          break;

        case 27:
          entityitem = this.entityDropItem(new ItemStack(Items.iron_ingot, 1, 0), 1.0F);
          break;

        case 28:
          entityitem = this.entityDropItem(new ItemStack(Items.fish, 1, 0), 1.0F);
          break;

        case 29:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.evilegg, this.rand.nextInt(5) + 1, 0), 1.0F);
          break;

        case 30:
          entityitem = this.entityDropItem(new ItemStack(Items.cooked_fished, 1, 0), 1.0F);
          break;

        case 31:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.gun, 1, 0), 1.0F);
          break;

        case 32:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.extinguisher, this.rand.nextInt(2) + 1, 0),
                  1.0F);
          break;

        case 33:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.rocket, 1, 0), 1.0F);
          break;

        case 34:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.atompacket, this.rand.nextInt(7) + 1, 0),
                  1.0F);
          break;

        case 35:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.armygem, 1, 0), 1.0F);
          break;

        case 36:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(24) + 1, 0), 1.0F);
          break;

        case 37:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.armygem, 1, 0), 1.0F);
          break;

        case 38:
          entityitem =
              this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.horseheadgem, 1, 0), 1.0F);
          break;

        case 39:
          entityitem = this.entityDropItem(new ItemStack(Items.gold_ingot, 1, 0), 1.0F);
          break;

        case 40:
          entityitem = this.entityDropItem(new ItemStack(Items.diamond, 1, 0), 1.0F);
          break;

        case 41:
          entityitem = this.entityDropItem(new ItemStack(MoreCreepsAndWeirdos.raygun, 1, 0), 1.0F);
          break;

        case 42:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(49) + 1, 0), 1.0F);
          break;

        default:
          entityitem =
              this.entityDropItem(
                  new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(3) + 1, 0), 1.0F);
          break;
      }

      double d = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
      entityitem.posX = (this.entityplayer).posX + d * 0.5D;
      entityitem.posZ = (this.entityplayer).posZ + d1 * 0.5D;
      entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;
      entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;
    }
  }

  /** Checks if this entity is inside of an opaque block */
  @Override
  public boolean isEntityInsideOpaqueBlock() {
    return this.health <= 0;
  }

  /** (abstract) Protected helper method to write subclass entity data to NBT. */
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setFloat("ModelSize", this.modelsize);
    nbttagcompound.setInteger("Age", this.age);
    nbttagcompound.setInteger("DeathTimer", this.deathtimer);
  }

  /** (abstract) Protected helper method to read subclass entity data from NBT. */
  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.modelsize = nbttagcompound.getFloat("ModelSize");
    this.age = nbttagcompound.getInteger("Age");
    this.deathtimer = nbttagcompound.getInteger("DeathTimer");
  }

  /** Checks if the entity's current position is a valid location to spawn this entity. */
  @Override
  public boolean getCanSpawnHere() {
    if (this.worldObj == null || this.getBoundingBox() == null) return false;
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.getBoundingBox().minY);
    int k = MathHelper.floor_double(this.posZ);
    int l = this.worldObj.getBlockLightOpacity(i, j, k);
    int i1 = Block.getIdFromBlock((this.worldObj.getBlock(i, j - 1, k)));
    return true;
  }

  /** Will return how many at most can spawn in a chunk at once. */
  @Override
  public int getMaxSpawnedInChunk() {
    return 1;
  }

  private void smoke() {
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

  public void playLivingSound2() {
    String s = this.getLivingSound();

    if (s != null) {
      this.worldObj.playSoundAtEntity(
          this,
          s,
          this.getSoundVolume(),
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (3F - this.modelsize));
    }
  }

  /** Returns the sound this mob makes while it's alive. */
  @Override
  protected String getLivingSound() {
    if (this.rand.nextInt(5) == 0) return "morecreeps:schlump";
    else return null;
  }

  /** Returns the sound this mob makes when it is hurt. */
  @Override
  protected String getHurtSound() {
    return "morecreeps:schlumphurt";
  }

  /** Returns the sound this mob makes on death. */
  @Override
  protected String getDeathSound() {
    return "morecreeps:schlumpdeath";
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

  public void smallconfetti() {
    if (!this.worldObj.isRemote) return;
    MoreCreepsAndWeirdos.proxy.spawnSchlumpParticles(this.worldObj, this, this.rand, 20, 10);
  }

  /** Will get destroyed next tick. */
  @Override
  public void setDead() {
    this.smoke();
    this.worldObj.playSoundAtEntity(
        this,
        this.getDeathSound(),
        this.getSoundVolume(),
        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    super.setDead();
  }

  /** Called when the mob's health reaches 0. */
  @Override
  public void onDeath(DamageSource damagesource) {
    this.giveReward();
    super.onDeath(damagesource);
  }

  @Override
  public EntityAgeable createChild(EntityAgeable ageable) {
    // TODO Auto-generated method stub
    return null;
  }
}
