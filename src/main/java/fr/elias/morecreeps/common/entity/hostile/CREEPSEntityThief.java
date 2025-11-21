package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityThief extends EntityMob {

    private boolean foundplayer;
    private boolean stolen;

    /** returns true if a creature has attacked recently only used for creepers and skeletons */
    protected boolean hasAttacked;

    protected ItemStack stolengood;
    private double goX;
    private double goZ;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public int tempDamage;
    public float modelsize;
    public String texture;

    public CREEPSEntityThief(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/thief.png";
        this.stolen = false;
        this.hasAttacked = false;
        this.foundplayer = false;
        this.tempDamage = 0;
        this.modelsize = 1.0F;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new AIThief());
        this.tasks.addTask(3, new EntityAIMoveTowardsRestriction(this, 0.35D));
        this.tasks.addTask(5, new EntityAIWander(this, 0.35D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 16F));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.65D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(1D);
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

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
     * attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected EntityPlayer findPlayerToAttack() {
        if (!this.stolen) {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16D);

            if (entityplayer != null && !entityplayer.worldObj.isRemote) {
                ItemStack aitemstack[] = entityplayer.inventory.mainInventory;
                this.itemnumber = -1;

                for (int i = 0; i < aitemstack.length; i++) {
                    ItemStack itemstack1 = aitemstack[i];

                    if (itemstack1 != null) {
                        this.itemnumber = i;
                    }
                }

                if (this.itemnumber >= 0) {
                    if (!this.foundplayer) {
                        if (this.rand.nextInt(2) == 0) {
                            this.worldObj.playSoundAtEntity(
                                entityplayer,
                                "morecreeps:thieffindplayer",
                                1.0F,
                                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
                        }

                        this.foundplayer = true;
                    }

                    return entityplayer;
                }
            }
        }

        return null;
    }

    /** Returns the item that this EntityLiving is holding, if any. */
    @Override
    public ItemStack getHeldItem() {
        return this.stolengood;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (this.handleWaterMovement()) {
            this.goX *= -1D;
            this.goZ *= -1D;
            this.motionX += this.goX * 0.10000000149011612D;
            this.motionZ += this.goZ * 0.10000000149011612D;
            this.motionY += 0.25D;
        }

        EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 25D);

        if (entityplayer != null && !this.stolen && !entityplayer.worldObj.isRemote) {
            this.distance = this.getDistanceToEntity(entityplayer);
            this.findPlayerToAttack();
        } else {
            this.distance = 999F;
            // entityToAttack = null;
            this.setAttackTarget(null);
        }

        if (this.stolen) {
            // entityToAttack = null;
            this.setAttackTarget(null);

            if (this.isJumping) {
                this.motionX += this.goX * 0.30000001192092896D;
                this.motionZ += this.goZ * 0.30000001192092896D;
            } else {
                this.motionX += this.goX;
                this.motionZ += this.goZ;
            }

            this.moveEntityWithHeading((float) this.motionX, (float) this.motionZ);

            if (this.prevPosY / this.posY == 1.0D) {
                if (this.rand.nextInt(25) == 0) {
                    this.motionX -= this.goX;
                } else {
                    this.motionX += this.goX;
                }

                if (this.rand.nextInt(25) == 0) {
                    this.motionZ -= this.goZ;
                } else {
                    this.motionZ += this.goZ;
                }
            }

            if (this.prevPosX == this.posX && this.rand.nextInt(50) == 0) {
                this.goX *= -1D;
            }

            if (this.prevPosZ == this.posZ && this.rand.nextInt(50) == 0) {
                this.goZ *= -1D;
            }

            if (this.rand.nextInt(500) == 0) {
                this.goX *= -1D;
            }

            if (this.rand.nextInt(700) == 0) {
                this.goZ *= -1D;
            }
        }

        if (entityplayer != null && !this.stolen
            && this.distance < 4F
            && this.canEntityBeSeen(entityplayer)
            && this.getHealth() > 0) {
            ItemStack itemstack = null;
            ItemStack aitemstack[] = entityplayer.inventory.mainInventory;
            this.itemnumber = -1;

            for (int i = 0; i < aitemstack.length; i++) {
                ItemStack itemstack1 = aitemstack[i];

                if (itemstack1 == null) {
                    continue;
                }

                itemstack = itemstack1;
                this.itemnumber = i;

                if (this.rand.nextInt(4) == 0) {
                    break;
                }
            }

            if (itemstack == null) {
                // entityToAttack = null;
                this.setAttackTarget(null);
            }

            if (itemstack != null && !this.stolen) {
                this.stolengood = itemstack.copy();
                this.worldObj.playSoundAtEntity(
                    this,
                    "random.pop",
                    this.getSoundVolume(),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 6.2F + 1.0F);
                this.stolenamount = this.rand.nextInt(itemstack.stackSize) + 1;

                if (this.stolenamount > itemstack.stackSize) {
                    this.stolenamount = itemstack.stackSize;
                }

                if (this.stolenamount == 1 && itemstack.isItemDamaged()) {
                    this.tempDamage = itemstack.getItemDamage();
                }

                if (this.stolenamount == itemstack.stackSize) {
                    entityplayer.inventory.mainInventory[this.itemnumber] = null;
                } else {
                    itemstack.stackSize -= this.stolenamount;
                }

                this.stolen = true;
                this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:thiefsteal",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                // entityToAttack = null;
                this.setAttackTarget(null);
                this.goX = 0.044999999999999998D;
                this.goZ = 0.044999999999999998D;

                if (this.rand.nextInt(5) == 0) {
                    this.goX *= -1D;
                }

                if (this.rand.nextInt(5) == 0) {
                    this.goZ *= -1D;
                }

                for (int j = 0; j < 10; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                        EnumParticleTypes.SMOKE_LARGE,
                        (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        this.posY + this.rand.nextFloat() * this.height,
                        (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
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
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.0F - this.modelsize) * 2.0F);
        }
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        return "morecreeps:thief";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:thiefhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:thiefdeath";
    }

    /** Checks if the entity's current position is a valid location to spawn this entity. */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null) return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        int l = this.worldObj.getBlockLightOpacity(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);

        if (j < 50) return true;
        else return i1 != Blocks.cobblestone && i1 != Blocks.log
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.stone_slab
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && this.rand.nextInt(10) == 0
            && l > 7;
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    public int getItemDamage() {
        return 25;
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            System.out.println(this.tempDamage);
            if (this.getHeldItem() != null) {
                ItemStack itemstack = this.getHeldItem()
                    .copy();
                if (this.tempDamage > 0) {
                    this.entityDropItem(itemstack, 0.0F);
                    this.stolengood = null;
                } else {
                    itemstack.stackSize = this.stolenamount;
                    this.entityDropItem(itemstack, 0.0F);
                }
            }
        }

        super.onDeath(damagesource);
    }

    class AIThief extends EntityAIBase {

        public CREEPSEntityThief thief = CREEPSEntityThief.this;

        public AIThief() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.thief.getAttackTarget();
            return entitylivingbase != null && !this.thief.stolen;
        }

        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.thief.getAttackTarget();
            if (entitylivingbase == null) return;
            double d0 = this.thief.getDistanceSqToEntity(entitylivingbase);

            if (d0 < 4.0D) {
                this.thief.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
            } else if (d0 < 256.0D) {
                this.thief.getLookHelper()
                    .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
            } else {
                this.thief.getNavigator()
                    .clearPathEntity();
                this.thief.getMoveHelper()
                    .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);

                this.thief.findPlayerToAttack();
            }
        }
    }
}
