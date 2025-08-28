package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBigBabyAI;

public class CREEPSEntityBigBaby extends EntityMob {

    public int skinDirection;
    public int skin;
    public int skinTimer;
    public float modelsize;
    public float hammerswing;
    public ResourceLocation texture;
    public int attackTime;

    public CREEPSEntityBigBaby(World world) {
        super(world);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABY0);
        this.setSize(this.width * 5.25F, this.height * 5.55F);
        this.skinDirection = 1;
        this.skinTimer = 0;
        this.skin = 0;
        this.modelsize = 6.5F;
        this.hammerswing = 0.0F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityBigBabyAI(this));
        this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(80D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(3D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.hammerswing < 0.0F) {
            this.hammerswing += 0.1000055F;
        } else {
            this.hammerswing = 0.0F;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.skinTimer++ > 60) {
            this.skinTimer = 0;
            this.skin += this.skinDirection;

            if (this.skin == 4 || this.skin == 0) {
                this.skinDirection *= -1;
            }

            if (this.getAttackTarget() != null) {
                this.skin = 0;
            }

            if (this.skin < 0 || this.skin > 4) {
                this.skin = 0;
            }

            this.texture = new ResourceLocation(
                    Reference.MOD_ID,
                    Reference.TEXTURE_PATH_ENTITES + (new StringBuilder()).append(Reference.TEXTURE_BIGBABY)
                    .append(String.valueOf(this.skin))
                    .append(".png")
                    .toString());
        }

        super.onLivingUpdate();
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    public void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.20000000000000001D * (0.850000011920929D + this.motionX * 0.20000000298023224D);
            this.motionZ = (d1 / f1) * 0.20000000000000001D
                    * (0.80000001192092896D + this.motionZ * 0.20000000298023224D);
            this.motionY = 0.14400000596246448D;
            this.fallDistance = -25F;

            if (f < 6D && f > 3D && this.rand.nextInt(5) == 0) {
                double d2 = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
                double d3 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
                this.motionX += d2 * 0.25D;
                this.motionZ += d3 * 0.25D;
                this.motionY += 0.15000000596046448D;
            }
        }

        if (f < this.modelsize * 0.69999999999999996D + 1.5D
                && entity.boundingBox.maxY > this.boundingBox.minY
                && entity.boundingBox.minY < this.boundingBox.maxY
                && this.rand.nextInt(10) == 0) {
            if (this.hammerswing == 0.0F) {
                this.hammerswing = -1.1F;
            }

            this.attackTime = 20;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
        }
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BIGBABYBOP);
        this.skinTimer = 45;

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
            if (this.riddenByEntity == entity || this.ridingEntity == entity)
                return true;

            if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                this.setRevengeTarget((EntityLivingBase) entity);
            }

            return true;
        } else
            return false;
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.getItem() == MoreCreepsAndWeirdos.babyjarempty) {
            if (this.modelsize < 1.0F) {
                this.setDead();
                entityplayer.inventory.setInventorySlotContents(
                        entityplayer.inventory.currentItem,
                        new ItemStack(MoreCreepsAndWeirdos.babyjarfull));
                MoreCreepsAndWeirdos.proxy.addChatMessage("Now turn that Baby into a Schlump on the floor");
                this.worldObj.playSoundAtEntity(this, "morecreeps:babytakehome", 1.0F, 1.0F);
            } else {
                MoreCreepsAndWeirdos.proxy.addChatMessage("That baby is too large");
                this.worldObj.playSoundAtEntity(this, "morecreeps:babyshrink", 1.0F, 1.0F);
            }
        }

        return true;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.15F;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && this.isValidLightLevel()
                && super.getCanSpawnHere();
        // Method used by Minecraft above, probably better to use it instead?
        /*
         * int i = MathHelper.floor_double(posX);
         * int j = MathHelper.floor_double(posY);
         * int k = MathHelper.floor_double(posZ);
         * int l = worldObj.getBlockLightOpacity(getPosition());
         * Block i1 = worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock();
         * return i1 != Blocks.cobblestone && i1 != Blocks.log && i1 != Blocks.double_stone_slab && i1 !=
         * Blocks.stone_slab && i1 != Blocks.planks && i1 != Blocks.wool && worldObj.getCollidingBoundingBoxes(this,
         * getBoundingBox()).size() == 0 && rand.nextInt(50) == 0 && worldObj.canSeeSky(new BlockPos(i, j, k)) && l > 6;
         */
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (6.5F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:bigbaby";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:bigbabyhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:bigbabyhurt";
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            this.dropItem(Items.porkchop, this.rand.nextInt(6) + 5);
        }
        super.onDeath(damagesource);
    }
}
