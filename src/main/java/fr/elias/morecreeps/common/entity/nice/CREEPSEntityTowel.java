package fr.elias.morecreeps.common.entity.nice;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CREEPSEntityTowel extends EntityCreature {

    public String basetexture;
    public String texture;
    public int textureindex;
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    public CREEPSEntityTowel(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/towel0.png";
        this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        this.basetexture = this.texture;
        this.textureindex = world.rand.nextInt(6);

    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(2D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.0D);
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("Texture", this.textureindex);
        //  nbttagcompound.setString("BaseTexture", this.basetexture);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.textureindex = nbttagcompound.getInteger("Texture");
        //  this.basetexture = nbttagcompound.getString("BaseTexture");
        //  this.texture = this.basetexture;
    }

    @Override
    public boolean interact(EntityPlayer p_70085_1_) {
        if (super.interact(p_70085_1_))
            return true;
        else if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == p_70085_1_)) {
            p_70085_1_.mountEntity(this);
            return true;
        } else
            return false;
    }

    @Override
    public double getYOffset() {
        return super.ridingEntity instanceof EntityPlayer ? (double)(super.yOffset - 1.1F) : (double)super.yOffset;
    }

    @Override
    public void updateRiderPosition() {
        if (super.riddenByEntity != null && super.riddenByEntity instanceof EntityPlayer) {
            super.riddenByEntity.setPosition(super.posX, super.posY + 1.100000023841858D, super.posZ);
        } else if (super.riddenByEntity != null && super.riddenByEntity instanceof CREEPSEntityNonSwimmer) {
            super.updateRiderPosition();
        }
    }


    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
    }
    @Override
    protected void fall(float p_70069_1_) {

    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return null;
    }

    /**
     * Return the AI task for player control.
     */
    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }
}
