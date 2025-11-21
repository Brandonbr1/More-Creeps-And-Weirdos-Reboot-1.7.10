package fr.elias.morecreeps.common.entity.netural;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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

public class CREEPSEntityInvisibleMan extends EntityMob {

    private static final ItemStack defaultHeldItem;
    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    public float modelsize;
    public String texture;

    public CREEPSEntityInvisibleMan(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/invisibleman.png";
        this.angerLevel = 0;
        this.modelsize = 1.0F;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new CREEPSEntityInvisibleMan.AIAttackEntity());
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (this.angerLevel > 0) {
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(30D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(2D);
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate() {
        if ((this.getAttackTarget() instanceof EntityPlayer) && this.angerLevel == 0) {
            this.texture = "morecreeps:textures/entity/invisiblemanmad.png";
            this.angerLevel = this.rand.nextInt(15) + 5;
        }
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(this.getAttackTarget() != null ? 0.75D : 0.5D);

        super.onUpdate();

        if (this.rand.nextInt(30) == 0 && this.angerLevel > 0) {
            this.angerLevel--;

            if (this.angerLevel == 0) {
                this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:invisiblemanforgetit",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                this.setAttackTarget(null);
                this.texture = "morecreeps:textures/entity/invisibleman.png";
            }
        }
    }

    class AIAttackEntity extends EntityAIBase {

        EntityPlayer entityplayer;

        @Override
        public boolean shouldExecute() {
            return (this.entityplayer == CREEPSEntityInvisibleMan.this.findPlayerToAttack());
        }

        @Override
        public void updateTask() {
            try {
                float f = CREEPSEntityInvisibleMan.this
                    .getDistanceToEntity(CREEPSEntityInvisibleMan.this.getAttackTarget());
                if (f < 256F) {
                    CREEPSEntityInvisibleMan.this.attackEntity(CREEPSEntityInvisibleMan.this.getAttackTarget(), f);
                    CREEPSEntityInvisibleMan.this.getLookHelper()
                        .setLookPositionWithEntity(CREEPSEntityInvisibleMan.this.getAttackTarget(), 10.0F, 10.0F);
                    CREEPSEntityInvisibleMan.this.getNavigator()
                        .clearPathEntity();
                    CREEPSEntityInvisibleMan.this.getMoveHelper()
                        .setMoveTo(
                            CREEPSEntityInvisibleMan.this.getAttackTarget().posX,
                            CREEPSEntityInvisibleMan.this.getAttackTarget().posY,
                            CREEPSEntityInvisibleMan.this.getAttackTarget().posZ,
                            0.5D);
                }
                if (f < 1F) {
                    CREEPSEntityInvisibleMan.this.attackEntityAsMob(CREEPSEntityInvisibleMan.this.getAttackTarget());
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    /** Checks if the entity's current position is a valid location to spawn this entity. */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null) return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        int l = this.worldObj.getFullBlockLightValue(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.sand && i1 != Blocks.cobblestone
            && i1 != Blocks.log
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.stone_slab
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && this.rand.nextInt(15) == 0
            && l > 7;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
     * attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        if (this.angerLevel == 0) return null;
        else {
            this.texture = "morecreeps:textures/entity/invisiblemanmad.png";
            return this.getLastAttacker();
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.20000000000000001D * 0.80000001192092896D + this.motionX * 0.20000000298023224D;
            this.motionZ = (d1 / f1) * 0.20000000000000001D * 0.80000001192092896D
                + this.motionZ * 0.20000000298023224D;
            this.motionY = 0.20000000596246448D;
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    public boolean canAttackEntity22(Entity entity, float i) {
        if (entity instanceof EntityPlayer) {
            List<?> list = this.worldObj
                .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(25D, 25D, 25D));

            for (int j = 0; j < list.size(); j++) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1 instanceof CREEPSEntityInvisibleMan) {
                    CREEPSEntityInvisibleMan creepsentityinvisibleman = (CREEPSEntityInvisibleMan) entity1;
                    creepsentityinvisibleman.becomeAngryAt(entity);
                }
            }

            this.becomeAngryAt(entity);
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    private void becomeAngryAt(Entity entity) {
        this.setRevengeTarget((EntityLivingBase) entity);
        this.angerLevel += this.rand.nextInt(40);
        this.texture = "morecreeps:textures/entity/invisiblemanmad.png";
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
        if (this.angerLevel == 0) return "morecreeps:invisibleman";
        else return "morecreeps:invisiblemanangry";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:invisiblemanhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:invisiblemandeath";
    }

    /** Returns the item that this EntityLiving is holding, if any. */
    @Override
    public ItemStack getHeldItem() {
        return defaultHeldItem;
    }

    /** Returns the item ID for the item the mob drops on death. */
    @Override
    protected Item getDropItem() {
        return dropItems[this.rand.nextInt(dropItems.length)];
    }

    static {
        defaultHeldItem = new ItemStack(Items.stick, 1);
        dropItems = (new Item[] { Items.stick, Items.stick, Items.stick, Items.stick, Items.apple, Items.bread,
            Items.bread, Items.cake, Items.stick, Items.cake, Items.stick, Items.stick, Items.stick, Items.stick,
            Items.stick, Items.stick, Items.stick, Items.stick, Items.stick, Items.stick, Items.stick, Items.stick,
            Items.stick, Items.stick, Items.gold_ingot, Items.stick, Items.stick, Items.stick, Items.apple, Items.apple,
            Items.stick });
    }
}
