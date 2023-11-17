package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.ai.EntityBlorpAI;

public class CREEPSEntityBlorp extends EntityAnimal {

    public double attackrange;
    public boolean bone;
    protected int attack;
    public float blorpsize;

    /** Entity motion Y */
    // public String motionY;
    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public int blorplevel;
    public boolean angry;
    public String texture;
    public int attackTime;

    public CREEPSEntityBlorp(World world) {
        super(world);
        bone = false;
        texture = "morecreeps:textures/entity/blorp.png";
        setSize(width * 1.5F, height * 2.5F);
        attack = 2;
        attackrange = 16D;
        blorpsize = 1.0F;
        hungry = false;
        hungrytime = rand.nextInt(20) + 20;
        blorplevel = 1;
        angry = false;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityBlorpAI(this));
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
    }

    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(25D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.5D);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        handleBlorpSize();

        super.onLivingUpdate();

        handleAttackTarget();

        if (hungry) {
            int[] treeCoords = findTree(this, 2D);

            if (treeCoords[1] != 0) {
                int treeX = treeCoords[0];
                int treeY = treeCoords[1];
                int treeZ = treeCoords[2];

                if (treeX == -1) {
                    return;
                }

                playBlorpEatSound();
                worldObj.setBlockToAir(treeX, treeY, treeZ);

                hungrytime += rand.nextInt(100) + 25;
                handleHungrytime();

                faceTreeTop(treeX, treeY, treeZ, 10F);

                int motionAdjustmentX = calculateMotionAdjustment(treeX, posX);
                int motionAdjustmentZ = calculateMotionAdjustment(treeZ, posZ);

                motionX += motionAdjustmentX * 0.050000000000000003D;
                motionZ += motionAdjustmentZ * 0.050000000000000003D;
            }
        } else {
            handleHungrytime();
        }
    }

    private void handleBlorpSize() {
        if (blorpsize > 2.0F) {
            ignoreFrustumCheck = true;
        }
    }

    private void handleAttackTarget() {
        if (getAttackTarget() != null) {
            hungry = false;
            hungrytime = 100;
        }
    }

    private void handleHungrytime() {
        hungrytime--;

        if (hungrytime < 1) {
            hungry = true;
            hungrytime = 1;
        }
    }

    private void playBlorpEatSound() {
        worldObj.playSoundAtEntity(this, "morecreeps:blorpeat", 1.0F,
            (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
    }

    private int calculateMotionAdjustment(int treeCoord, double position) {
        int adjustment;

        if (position < treeCoord) {
            adjustment = treeCoord - MathHelper.floor_double(position);
        } else {
            adjustment = MathHelper.floor_double(position) - treeCoord;
        }

        return adjustment;
    }

    public float getBlockPathWeight(int iPosX, int iPosY, int iPosZ) {
        if (worldObj.getBlock(iPosX, iPosY, iPosZ) == Blocks.leaves
            || worldObj.getBlock(iPosX, iPosY, iPosZ) == Blocks.log) {
            return 10F;
        } else {
            return -(float) iPosY;
        }
    }

    public void faceTreeTop(int i, int j, int k, float f) {
        double d = (double) i - posX;
        double d1 = (double) k - posZ;
        double d2 = (double) j - posY;
        double d3 = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = (float) ((Math.atan2(d1, d) * 180D) / Math.PI);
        float f2 = (float) ((Math.atan2(d2, d3) * 180D) / Math.PI);
        rotationYaw = (float) (Math.atan2(motionX, motionZ) / Math.PI);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource damagesource, float f) {
        Entity entity = damagesource.getEntity();

        if (entity instanceof EntityPlayer) {
            angry = true;
        }

        hungry = false;
        hungrytime = 100;
        EntityLivingBase entityToAttack = getAttackTarget();
        entityToAttack = (EntityLivingBase) entity;
        return super.attackEntityFrom(damagesource, f);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    public void attackEntity(Entity entity, float f) {
        if (onGround) {
            worldObj.playSoundAtEntity(
                this,
                "morecreeps:blorpbounce",
                1.0F,
                (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            double d = entity.posX - posX;
            double d2 = entity.posZ - posZ;
            float f1 = MathHelper.sqrt_double(d * d + d2 * d2);
            motionX = (d / f1) * 0.20000000000000001D * 0.80000001192092896D + motionX * 0.20000000298023224D;
            motionZ = (d2 / f1) * 0.20000000000000001D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
            motionY = 0.70000000596246448D + blorpsize * 0.050000004559D;
            fallDistance = -(25F + blorpsize * 5F);
        } else {
            double d1 = 2.5D + (blorpsize - 1.5D) * 0.80000000000000004D;

            if (d1 > 3.5D) {
                d1 = 3.5D;
            }

            if (f < d1 && entity.getBoundingBox().maxY > getBoundingBox().minY
                && entity.getBoundingBox().minY < getBoundingBox().maxY) {
                attackTime = 20;

                if (entity instanceof CREEPSEntityBlorp) {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack + blorplevel);
                } else {
                    entity.attackEntityFrom(DamageSource.causeMobDamage(this), attack);
                }
            }
        }
    }

    public int[] findTree(Entity entity, double double1) {
        if (entity == null || worldObj == null) {
            return new int[]{-1, 0, 0};
        }
        if (entity.getBoundingBox() == null) {
            return new int[]{-1, 0, 0};
        }
        AxisAlignedBB boundingBox = entity.getBoundingBox().expand(double1, double1, double1);
        int minX = MathHelper.floor_double(boundingBox.minX);
        int maxX = MathHelper.floor_double(boundingBox.maxX + 1.0D);
        int minY = MathHelper.floor_double(boundingBox.minY);
        int maxY = MathHelper.floor_double(boundingBox.maxY + 1.0D);
        int minZ = MathHelper.floor_double(boundingBox.minZ);
        int maxZ = MathHelper.floor_double(boundingBox.maxZ + 1.0D);

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    Block block = worldObj.getBlock(x, y, z);

                    if (block != Blocks.air && block == Blocks.leaves) {
                        return new int[]{x, y, z};
                    }

                    if (block != Blocks.air && blorplevel > 3 && block == Blocks.log) {
                        return new int[]{x, y, z};
                    }
                }
            }
        }

        return new int[]{-1, 0, 0};
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    public boolean isEntityInsideOpaqueBlock() {
        return false;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere() {
        if (worldObj == null || getBoundingBox() == null) {
            return false;
        }
        // Method used by Minecraft below, probably better to leave it?
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(posY);
        int k = MathHelper.floor_double(posZ);
        int l = worldObj.getFullBlockLightValue(i, j, k);
        Block i1 = worldObj.getBlock(i, j - 1, k);
        return (i1 == Blocks.grass || i1 == Blocks.dirt) && i1 != Blocks.cobblestone
            && i1 != Blocks.log
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.stone_slab
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && worldObj.getCollidingBoundingBoxes(this, getBoundingBox())
                .size() == 0
            && worldObj.canBlockSeeTheSky(i, j, k)
            && rand.nextInt(25) == 0
            && l > 10;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Hungry", hungry);
        nbttagcompound.setInteger("BlorpLevel", blorplevel);
        nbttagcompound.setFloat("BlorpSize", blorpsize);
        nbttagcompound.setBoolean("Angry", angry);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        hungry = nbttagcompound.getBoolean("Hungry");
        blorplevel = nbttagcompound.getInteger("BlorpLevel");
        blorpsize = nbttagcompound.getFloat("BlorpSize");
        angry = nbttagcompound.getBoolean("Angry");
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound() {
        String s = getLivingSound();

        if (s != null) {
            worldObj.playSoundAtEntity(
                this,
                s,
                getSoundVolume(),
                (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F + (1.0F - blorpsize));
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return "morecreeps:blorp";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "morecreeps:blorphurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "morecreeps:blorpdeath";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource damagesource) {
        if (!worldObj.isRemote) {
            dropItem(MoreCreepsAndWeirdos.blorpcola, blorplevel);
        }
        super.onDeath(damagesource);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk() {
        return 3;
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new CREEPSEntityBlorp(worldObj);
    }
}
