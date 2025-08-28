package fr.elias.morecreeps.common.entity.proj;

import java.util.List;

import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilChicken;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilCreature;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilPig;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilScientist;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class CREEPSEntityEvilLight extends EntityMob {

    public int lifespan;
    public String texture;

    public CREEPSEntityEvilLight(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/evillight1.png";
        this.lifespan = 200;
        this.motionZ = this.rand.nextFloat() * 2.0F - 1.0F;
        this.motionX = this.rand.nextFloat() * 2.0F - 1.0F;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(1D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3D);
    }


    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        float health = this.getHealth();

        if (this.lifespan-- < 1 || this.handleWaterMovement()) {
            health = 0;
            this.setDead();
        }

        if (this.getBoundingBox() != null) {
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));

            for (Entity entity : list) {
                if (entity.canBeCollidedWith() && (entity instanceof EntityLivingBase)
                        && !(entity instanceof CREEPSEntityEvilLight)
                        && !(entity instanceof CREEPSEntityEvilScientist)
                        && !(entity instanceof CREEPSEntityEvilChicken)
                        && !(entity instanceof CREEPSEntityEvilCreature)
                        && !(entity instanceof CREEPSEntityEvilPig)) {

                    entity.setFire(3);
                    entity.motionX = this.rand.nextFloat() * 0.7F;
                    entity.motionY = this.rand.nextFloat() * 0.4F;
                    entity.motionZ = this.rand.nextFloat() * 0.7F;

                    this.worldObj.playSoundAtEntity(this, "morecreeps:evillight", 0.2F,
                            1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
                }
            }
        }

        super.onLivingUpdate();
    }

    public void damageEntity(int i) {
        float health = this.getHealth();
        health -= i;
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer p_70100_1_) {
        if (!this.worldObj.isRemote) {
            p_70100_1_.attackEntityFrom(DamageSource.anvil, 3);
        }
        super.onCollideWithPlayer(p_70100_1_);
    }


    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:evillight";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:evillight";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:evillight";
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        return true;
    }
}
