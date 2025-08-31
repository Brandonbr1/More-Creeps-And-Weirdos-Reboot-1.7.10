package fr.elias.morecreeps.common.entity.nice;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class CREEPSEntityTameable extends EntityAnimal
    implements IEntityAdditionalSpawnData {

  // -----------------------------
  // Fields
  // -----------------------------
  private boolean isTamed;

  private int foodsToTame;
  private int basehealth;

  private String tamedName;
  private String basetexture;
  private String texture;

  private EntityPlayer owner;
  private String ownerUUID;

  private boolean itemUsed;

  // -----------------------------
  // Constructor
  // -----------------------------
  public CREEPSEntityTameable(World world) {
    super(world);
    this.foodsToTame = getFoodsToTame();
    String baseTexture = this.getBaseTexture();
    this.setBaseTexture(baseTexture);
  }

  @Override
  public void onLivingUpdate() {
    super.onLivingUpdate();

    if (this.worldObj.isRemote) {
      String syncedBaseTexture = this.getDataWatcher().getWatchableObjectString(18);
      if (syncedBaseTexture != null
          && !syncedBaseTexture.isEmpty()
          && !syncedBaseTexture.equals(this.basetexture)) {
        this.basetexture = syncedBaseTexture;
        if (this.texture == null || this.texture.isEmpty()) {
          this.texture = syncedBaseTexture;
        }
      }

      String syncedCurrentTexture = this.getDataWatcher().getWatchableObjectString(19);
      if (syncedCurrentTexture != null
          && !syncedCurrentTexture.isEmpty()
          && !syncedCurrentTexture.equals(this.texture)) {
        this.texture = syncedCurrentTexture;
      }
    }
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    this.getDataWatcher().addObject(16, ""); // Tamed name
    this.getDataWatcher().addObject(17, (byte) 0); // Is tamed flag
    this.getDataWatcher().addObject(18, ""); // Base texture sync
    this.getDataWatcher().addObject(19, ""); // Current texture sync
  }

  // -----------------------------
  // Getters and Setters
  // -----------------------------
  public boolean getIsTamed() {
    if (this.worldObj.isRemote) {
      return this.getDataWatcher().getWatchableObjectByte(17) == 1;
    }
    return this.isTamed;
  }

  public void setIsTamed(boolean tamed) {
    this.isTamed = tamed;
    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(17, (byte) (tamed ? 1 : 0));
    }
  }

  public String getTamedName() {
    if (this.worldObj.isRemote) {
      String name = this.getDataWatcher().getWatchableObjectString(16);
      return name != null && name.length() > 0 ? name : null;
    }
    return this.tamedName;
  }

  public void setTamedName(String name) {
    this.tamedName = name;
    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(16, name != null ? name : "");
    }
  }

  public EntityPlayer getOwner() {
    return this.owner;
  }

  public void setOwner(EntityPlayer owner) {
    this.owner = owner;
    if (owner != null) {
      this.ownerUUID = owner.getUniqueID().toString();
    }
  }

  private void setBaseTexture(String texture) {
    this.basetexture = texture;
    this.texture = texture;
    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(18, texture != null ? texture : "");
      this.getDataWatcher().updateObject(19, texture != null ? texture : "");
    }
  }

  public void setTexture(String texture) {
    this.texture = texture;
    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(19, texture != null ? texture : "");
    }
  }

  public String getTexture() {
    return this.texture;
  }

  public String getOwnerUUID() {
    return this.ownerUUID;
  }

  // -----------------------------
  // Abstract hooks
  // -----------------------------
  protected abstract int getFoodsToTame();

  protected abstract int getBaseHealth();

  protected abstract ItemStack getTamedItemStack();

  protected abstract String[] getNames();

  protected abstract String getCreatureTypeName();

  protected abstract CREEPSGuiHandler.GuiType getNameGuiType();

  protected abstract StatBase getTamingAchievement();

  protected abstract float getModelSize();

  protected abstract boolean canBeRidden();

  protected abstract String getBaseTexture();

  protected abstract String getTamedTexture();

  protected abstract boolean shouldChangeTextureWhenTamed();

  protected abstract void onTamingComplete(EntityPlayer player);

  // -----------------------------
  // Interaction logic
  // -----------------------------
  @Override
  public boolean interact(EntityPlayer entityplayer) {
    ItemStack itemstack = entityplayer.inventory.getCurrentItem();
    this.itemUsed = false;

    if (handleSpecificInteraction(entityplayer, itemstack)) {
      return true;
    }

    if (this.isTamed && entityplayer.isSneaking()) {
      if (this.ownerUUID != null && entityplayer.getUniqueID().toString().equals(this.ownerUUID)) {
        this.owner = entityplayer;
        if (!this.worldObj.isRemote) {
          MoreCreepsAndWeirdos.guiHandler.tryOpenGui(
              this.getNameGuiType().id, entityplayer, this.worldObj, this.getEntityId());
        }
        return true;
      }
    }

    if (itemstack != null
        && this.riddenByEntity == null
        && itemstack.getItem() == this.getTamedItemStack().getItem()
        && !this.isTamed) {
      return handleTaming(entityplayer, itemstack);
    }

    if (!this.itemUsed && !this.isTamed && !this.worldObj.isRemote) {
      String plural = this.foodsToTame > 1 ? "s" : "";
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "You need \2476"
              + this.foodsToTame
              + " "
              + this.getTamedItemStack().getDisplayName()
              + plural
              + " \247fto tame this lovely "
              + this.getCreatureTypeName()
              + ".");
    }

    if (itemstack == null && this.isTamed && this.getHealth() > 0) {
      return handleMounting(entityplayer);
    }

    return true;
  }

  protected boolean handleSpecificInteraction(EntityPlayer player, ItemStack itemstack) {
    return false;
  }

  @Override
  public boolean attackEntityFrom(DamageSource damagesource, float damage) {
    if (this.riddenByEntity != null && damagesource.getEntity() instanceof EntityPlayer) {
      return false;
    }
    return super.attackEntityFrom(damagesource, damage);
  }

  // -----------------------------
  // Taming logic
  // -----------------------------
  private boolean handleTaming(EntityPlayer entityplayer, ItemStack itemstack) {
    this.itemUsed = true;

    if (!this.worldObj.isRemote && !this.isTamed) {
      this.foodsToTame--;
      String plural = this.foodsToTame > 1 ? "s" : "";

      if (this.foodsToTame > 0) {
        MoreCreepsAndWeirdos.proxy.addChatMessage(
            "You need \2476"
                + this.foodsToTame
                + " "
                + this.getTamedItemStack().getDisplayName()
                + plural
                + " \247fto tame this lovely "
                + this.getCreatureTypeName()
                + ".");
      }

      if (itemstack.stackSize - 1 == 0) {
        entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
      } else {
        itemstack.stackSize--;
      }

      this.worldObj.playSoundAtEntity(
          this,
          "morecreeps:hotdogeat",
          1.0F,
          (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

      int health = (int) this.getHealth() + 10;
      if (health > this.getBaseHealth()) {
        health = this.getBaseHealth();
      }
      this.setHealth(health);

      if (this.foodsToTame < 1) {
        completeTaming(entityplayer);
      }
    } else if (this.worldObj.isRemote && !this.isTamed) {
      this.smoke();
    }
    return true;
  }

  // TODO FIX ACHIEVEMENTS
  private void completeTaming(EntityPlayer entityplayer) {
    this.isTamed = true;
    this.owner = entityplayer;
    this.ownerUUID = entityplayer.getUniqueID().toString();
    if (this.tamedName == null || this.tamedName.length() < 1) {
      this.tamedName = this.getNames()[this.rand.nextInt(this.getNames().length)];
    }

    if (shouldChangeTextureWhenTamed()) {
      String tamedTexture = getTamedTexture();
      if (tamedTexture != null && !tamedTexture.isEmpty()) {
        setBaseTexture(tamedTexture);
      }
    }

    this.confetti(entityplayer);
    this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);

    StatBase achievement = this.getTamingAchievement();
    if (achievement != null) {
      entityplayer.addStat(achievement, 1);
    }

    MoreCreepsAndWeirdos.proxy.addChatMessage("");
    MoreCreepsAndWeirdos.proxy.addChatMessage("\2476" + this.tamedName + " \247fhas been tamed!");
    this.worldObj.playSoundAtEntity(
        this,
        "morecreeps:ggpiglevelup",
        1.0F,
        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    this.smoke();

    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(16, this.tamedName != null ? this.tamedName : "");
      this.getDataWatcher().updateObject(17, (byte) (this.isTamed ? 1 : 0));
    }

    onTamingComplete(entityplayer);
  }

  // -----------------------------
  // Visual FX
  // -----------------------------
  protected void confetti(EntityPlayer player) {
    double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
    CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
    creepsentitytrophy.setLocationAndAngles(
        player.posX + d * 3D, player.posY - 2D, player.posZ + d1 * 3D, player.rotationYaw, 0.0F);
    if (!this.worldObj.isRemote) {
      this.worldObj.spawnEntityInWorld(creepsentitytrophy);
    }
  }

  protected void smoke() {
    for (int i = 0; i < 7; i++) {
      double d = this.rand.nextGaussian() * 0.02D;
      double d2 = this.rand.nextGaussian() * 0.02D;
      double d4 = this.rand.nextGaussian() * 0.02D;
      this.worldObj.spawnParticle(
          EnumParticleTypes.HEART,
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
            EnumParticleTypes.EXPLOSION_NORMAL,
            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            this.posY + this.rand.nextFloat() * this.height + j,
            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
            d1,
            d3,
            d5);
      }
    }
  }

  private boolean handleMounting(EntityPlayer entityplayer) {
    if (!canBeRidden()) {
      return true;
    }

    if (entityplayer.ridingEntity == null && this.getModelSize() >= 1.0F) {
      this.rotationYaw = entityplayer.rotationYaw;
      this.rotationPitch = entityplayer.rotationPitch;
      entityplayer.fallDistance = -5F;
      entityplayer.mountEntity(this);
    } else if (this.getModelSize() < 1.0F && this.isTamed) {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "Your " + this.getCreatureTypeName() + " is too small to ride!");
    } else if (entityplayer.ridingEntity != null) {
      MoreCreepsAndWeirdos.proxy.addChatMessage(
          "Unmount all creatures before riding your " + getCreatureTypeName());
    }
    return true;
  }

  // -----------------------------
  // Persistence
  // -----------------------------
  @Override
  public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
    super.writeEntityToNBT(nbttagcompound);
    nbttagcompound.setBoolean("Tamed", this.isTamed);
    nbttagcompound.setInteger("TamedFood", this.foodsToTame);
    if (this.tamedName != null && !this.tamedName.isEmpty()) {
      nbttagcompound.setString("Name", this.tamedName);
    }
    if (this.isTamed && this.ownerUUID != null) {
      nbttagcompound.setString("OwnerUUID", this.ownerUUID);
    }
    nbttagcompound.setInteger("BaseHealth", this.basehealth);
    if (this.basetexture != null && !this.basetexture.isEmpty()) {
      nbttagcompound.setString("BaseTexture", this.basetexture);
    }
  }

  @Override
  public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
    super.readEntityFromNBT(nbttagcompound);
    this.isTamed = nbttagcompound.getBoolean("Tamed");
    this.foodsToTame = nbttagcompound.getInteger("TamedFood");
    if (nbttagcompound.hasKey("Name")) {
      this.tamedName = nbttagcompound.getString("Name");
    }
    if (this.isTamed && nbttagcompound.hasKey("OwnerUUID")) {
      this.ownerUUID = nbttagcompound.getString("OwnerUUID");
      this.owner = null;
    }
    this.basehealth = nbttagcompound.getInteger("BaseHealth");
    if (nbttagcompound.hasKey("BaseTexture")) {
      this.basetexture = nbttagcompound.getString("BaseTexture");
      this.texture = this.basetexture;
    }

    if (!this.worldObj.isRemote) {
      this.getDataWatcher().updateObject(16, this.tamedName != null ? this.tamedName : "");
      this.getDataWatcher().updateObject(17, (byte) (this.isTamed ? 1 : 0));
      this.getDataWatcher().updateObject(18, this.basetexture != null ? this.basetexture : "");
      this.getDataWatcher().updateObject(19, this.texture != null ? this.texture : "");
    }
  }

  @Override
  public void writeSpawnData(ByteBuf buf) {
    buf.writeBoolean(this.isTamed);
    ByteBufUtils.writeUTF8String(buf, this.tamedName != null ? this.tamedName : "");
    buf.writeInt(this.foodsToTame);
    ByteBufUtils.writeUTF8String(buf, this.basetexture != null ? this.basetexture : "");
    ByteBufUtils.writeUTF8String(buf, this.texture != null ? this.texture : "");
  }

  @Override
  public void readSpawnData(ByteBuf buf) {
    this.isTamed = buf.readBoolean();
    this.tamedName = ByteBufUtils.readUTF8String(buf);
    this.foodsToTame = buf.readInt();
    this.basetexture = ByteBufUtils.readUTF8String(buf);
    this.texture = ByteBufUtils.readUTF8String(buf);
    // Fallback if texture is empty
    if (this.texture == null || this.texture.isEmpty()) {
      this.texture = this.basetexture;
    }
  }

  // -----------------------------
  // Despawn rules
  // -----------------------------
  @Override
  protected boolean canDespawn() {
    return !this.isTamed;
  }

  @Override
  public boolean isNoDespawnRequired() {
    return this.isTamed || super.isNoDespawnRequired();
  }

  // -----------------------------
  // Command Sender
  // -----------------------------
  @Override
  public String getCommandSenderName() {
    if (this.isTamed && this.tamedName != null && this.tamedName.length() > 0) {
      return this.tamedName;
    }
    return super.getCommandSenderName();
  }
}
