package fr.elias.morecreeps.common.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityHunchbackSkeleton;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;

public class CREEPSEntityHunchback extends EntityMob {

    EntityPlayer entityplayer;
    World world;
    public boolean tamed;
    public int basehealth;
    public int weapon;
    public boolean used;
    public int interest;
    public String ss;
    public float distance;
    public int caketimer;
    public String basetexture;
    public float modelsize;
    public String texture;
    public double moveSpeed;
    public double attackStrength;
    public double health;

    public CREEPSEntityHunchback(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/hunchback.png";
        this.basetexture = this.texture;
        this.moveSpeed = 0.51F;
        this.attackStrength = 1;
        this.health = this.rand.nextInt(15) + 5;
        this.tamed = false;
        this.basehealth = this.rand.nextInt(30) + 10;
        this.health = this.basehealth;
        this.caketimer = 0;
        this.modelsize = 1.0F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.45D, true));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(30);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.51f);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.getAttackTarget() instanceof CREEPSEntityHunchbackSkeleton) {
            this.setAttackTarget(null);
        }

        if (this.basetexture != this.texture) {
            this.texture = this.basetexture;
        }

        if (this.tamed && this.caketimer > 0 && this.rand.nextInt(10) == 0) {
            this.caketimer--;

            if (this.caketimer == 0) {
                this.tamed = false;
                this.texture = "/textures/entity/hunchback.png";
                this.basetexture = this.texture;
                this.caketimer = this.rand.nextInt(700) + 300;
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        if (this.tamed) {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16D);

            if (entityplayer != null && this.canEntityBeSeen(entityplayer)) {
                this.distance = this.getDistanceToEntity(entityplayer);

                if (this.distance < 5F) {
                    this.setAttackTarget(null);
                    return null;
                } else
                    return entityplayer;
            } else
                return null;
        } else
            return null;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.hurtTime = this.maxHurtTime = 10;

        if (entity != null) {
            double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);

            if ((entity instanceof EntityPlayer) && this.tamed) {
                this.motionY = this.rand.nextFloat() * 0.9F;
                this.motionZ = d1 * 0.40000000596046448D;
                this.motionX = d * 0.5D;
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:hunchbackthanks",
                        2.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                super.attackEntityFrom(damagesource, i / 6);
            } else if (i > 0 && entity != null) {
                this.motionY = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.3F;
                this.motionZ = d1 * 0.40000000596046448D;
                this.motionX = d * 0.5D;
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:hunchhurt",
                        2.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.setBeenAttacked();
                this.setAttackTarget((EntityLivingBase) entity);
                super.attackEntityFrom(damagesource, i);
            }
        }

        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (!(this.getAttackTarget() instanceof CREEPSEntityHunchbackSkeleton)
                && !(this.getAttackTarget() instanceof CREEPSEntityGuineaPig)
                && !(this.getAttackTarget() instanceof CREEPSEntityHotdog)) {
            if (this.onGround) {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                this.motionX = (d / f1) * 0.5D * 0.80000001192092896D + this.motionX * 0.20000000298023224D;
                this.motionZ = (d1 / f1) * 0.5D * 0.80000001192092896D + this.motionZ * 0.20000000298023224D;
                this.motionY = 0.20000000596046447D;
                super.attackEntityAsMob(entity);
            } else if (this.tamed) {
                super.attackEntityAsMob(entity);
            }
        }

        if (f < 16D && (this.getAttackTarget() instanceof EntityPlayer) && this.tamed) {
            this.setAttackTarget(null);
        } else {
            super.attackEntityAsMob(entity);
        }
    }

    public EntityLiving getClosestTarget(Entity entity, double d) {
        double d1 = -1D;
        EntityLiving entityliving = null;

        for (int i = 0; i < this.worldObj.loadedEntityList.size(); i++) {
            Entity entity1 = (Entity) this.worldObj.loadedEntityList.get(i);

            if (!(entity1 instanceof EntityLiving) || entity1 == entity
                    || entity1 == entity.riddenByEntity
                    || entity1 == entity.ridingEntity
                    || (entity1 instanceof EntityPlayer)
                    || (entity1 instanceof EntityMob)
                    || (entity1 instanceof CREEPSEntityHunchbackSkeleton)) {
                continue;
            }

            double d2 = entity1.getDistanceSq(entity.posX, entity.posY, entity.posZ);

            if ((d < 0.0D || d2 < d * d) && (d1 == -1D || d2 < d1)
                    && ((EntityLiving) entity1).canEntityBeSeen(entity)) {
                d1 = d2;
                entityliving = (EntityLiving) entity1;
            }
        }

        return entityliving;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        this.used = false;

        if (itemstack != null) {
            if (this.tamed && itemstack.getItem() == Items.bone) {
                this.smoke();
                this.used = true;
                this.smoke();
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:ggpigarmor",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:huncharmy",
                        2.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

                for (int i = 0; i < 5; i++) {
                    CREEPSEntityHunchbackSkeleton creepsentityhunchbackskeleton = new CREEPSEntityHunchbackSkeleton(
                            this.worldObj);
                    creepsentityhunchbackskeleton
                    .setLocationAndAngles(this.posX + 3D, this.posY, this.posZ + i, this.rotationYaw, 0.0F);
                    creepsentityhunchbackskeleton.modelsize = this.modelsize;
                    this.worldObj.spawnEntityInWorld(creepsentityhunchbackskeleton);
                }
            }

            if (itemstack.getItem() == Items.cake || Item.getIdFromItem(itemstack.getItem()) == 92) {

                this.worldObj.playSoundAtEntity(entityplayer, "morecreeps:achievement", 1.0F, 1.0F);
                entityplayer.addStat(MoreCreepsAndWeirdos.achievehunchback, 1);
                this.confetti();

                this.texture = "/textures/entity/hunchbackcake.png";
                this.basetexture = this.texture;
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:hunchthankyou",
                        2.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.used = true;
                this.tamed = true;
                this.interest += 15;
                this.health += 2;
                this.setHealth((float) this.health);
                this.smoke();
                this.getLivingSound();

                if (this.caketimer < 4000) {
                    this.caketimer += this.rand.nextInt(500) + 250;
                }
            }

            if (this.health > this.basehealth) {
                this.health = this.basehealth;
                this.setHealth((float) this.health);
            }

            if (this.used) {
                if (itemstack.stackSize - 1 == 0) {
                    entityplayer.inventory.setInventorySlotContents(entityplayer.inventory.currentItem, null);
                } else {
                    itemstack.stackSize--;
                }
            }
        }

        return false;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Tamed", this.tamed);
        nbttagcompound.setInteger("CakeTimer", this.caketimer);
        nbttagcompound.setString("BaseTexture", this.basetexture);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.tamed = nbttagcompound.getBoolean("Tamed");
        this.basetexture = nbttagcompound.getString("BaseTexture");
        this.caketimer = nbttagcompound.getInteger("CakeTimer");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.getBoundingBox().minY);
        int k = MathHelper.floor_double(this.posZ);
        int l = this.worldObj.getBlockLightOpacity(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.cobblestone && i1 != Blocks.wool
                && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
                && this.worldObj.checkBlockCollision(this.getBoundingBox())
                && this.worldObj.canBlockSeeTheSky(i, j, k)
                && this.rand.nextInt(10) == 0
                && l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
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

    /**
     * Plays living's sound at its position
     */
    @Override
    public void playLivingSound() {
        String s = this.getLivingSound();

        if (s != null) {
            this.worldObj.playSoundAtEntity(
                    this,
                    s,
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.0F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (this.tamed)
            return "morecreeps:hunchquiet";

        if (this.rand.nextInt(3) == 0)
            return "morecreeps:hunchcake";
        else
            return "morecreeps:hunchquiet";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:hunchhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:hunchdeath";
    }

    private void smoke() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 10; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            "explode",
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height + i,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (this.tamed && this.health > 0)
            return;

        if (this.rand.nextInt(5) == 0) {
            this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
        } else {
            this.dropItem(Item.getItemFromBlock(Blocks.sand), this.rand.nextInt(3) + 1);
        }

        super.onDeath(damagesource);
    }

    /**
     * Will get destroyed next tick.
     */
    @Override
    public void setDead() {
        if (this.worldObj.isRemote) {
            if (this.tamed && this.health > 0) {
                this.isDead = false;
                this.deathTime = 0;
                return;
            } else {
                super.setDead();
                return;
            }
        }
    }
}
