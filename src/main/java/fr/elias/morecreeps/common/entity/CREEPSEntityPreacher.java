package fr.elias.morecreeps.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityPreacher extends EntityMob {

    public boolean rideable;
    protected double attackRange;
    private int angerLevel;
    private int value;
    private boolean ritual;
    private Entity targetedEntity;
    private Entity victimEntity;
    public int raise;
    public boolean getvictim;

    private float victimspeed;
    private int waittime;
    private int raiselevel;
    public int revenge;

    public String texture;

    public CREEPSEntityPreacher(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/preacher0.png";
        this.angerLevel = 0;
        this.attackRange = 16D;
        this.ritual = false;
        this.getvictim = false;
        this.raise = 0;
        this.victimspeed = 0.0F;
        this.waittime = this.rand.nextInt(500) + 500;
        this.raiselevel = 0;
        this.revenge = 0;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntitySheep.class, 0, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPig.class, 0, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(75D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(5D);
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(this.getAttackTarget() != null ? 0.6D : 0.4D);

        if (this.rand.nextInt(4) == 0) {
            this.texture = (new StringBuilder()).append("morecreeps:textures/entity/preacher")
                    .append(String.valueOf(this.rand.nextInt(3)))
                    .append(".png")
                    .toString();
        }

        super.onUpdate();

        if (this.handleLavaMovement()) {
            if (this.rand.nextInt(25) == 0) {
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:preacherburn",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.setOnFireFromLava();
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.inWater || this.isEntityInsideOpaqueBlock()) {
            int i = MathHelper.floor_double(this.posX);
            int l = MathHelper.floor_double(this.posY);
            int j1 = MathHelper.floor_double(this.posZ);
            Block l1 = this.worldObj.getBlock(i, l + 2, j1);

            if (l1 != Blocks.air) {
                this.worldObj.setBlockToAir(i, l + 2, j1);
                this.motionY += 0.5D;
            }
        }

        if (this.getvictim) {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;

            if (this.raise++ > this.raiselevel) {
                this.getvictim = false;
                this.ritual = false;
                this.victimEntity.motionY = -0.80000001192092896D;
                this.raise = 0;
                this.waittime = this.rand.nextInt(500) + 500;
            } else {
                int j = MathHelper.floor_double(this.victimEntity.posX);
                int i1 = MathHelper.floor_double(this.victimEntity.posY);
                int k1 = MathHelper.floor_double(this.victimEntity.posZ);
                Block i2 = this.worldObj.getBlock(j, i1 + 2, k1);

                if (i2 != Blocks.air && (this.victimEntity instanceof EntityPlayer)) {
                    this.worldObj.setBlockToAir(j, i1 + 2, k1);
                }

                this.victimEntity.motionY = 0.20000000298023224D;
                this.waittime = 1000;
                this.smokevictim(this.victimEntity);
                this.smoke();

                if (this.rand.nextInt(10) == 0) {
                    this.victimEntity.motionX = this.rand.nextFloat() * 0.85F - 0.5F;
                } else if (this.rand.nextInt(10) == 0) {
                    this.victimEntity.motionZ = this.rand.nextFloat() * 0.8F - 0.5F;
                }
            }
        }

        if (this.ritual && !this.getvictim) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.0D);
            this.targetedEntity = null;

            for (int k = 0; k < this.worldObj.loadedEntityList.size(); k++) {
                this.targetedEntity = (Entity) this.worldObj.loadedEntityList.get(k);

                if ((this.targetedEntity instanceof EntitySheep) || (this.targetedEntity instanceof EntityPig)) {
                    this.getvictim = true;
                    this.victimEntity = this.targetedEntity;
                    this.victimEntity.motionX = 0.0D;
                    this.victimEntity.motionY = 0.0D;
                    this.victimEntity.motionZ = 0.0D;
                    this.raiselevel = this.rand.nextInt(50) + 50;
                    this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:preacherraise",
                            1.0F,
                            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                }
            }

            if (this.targetedEntity == null) {
                this.ritual = false;
                this.getvictim = false;
                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(0.35D);
            }
        } else if (this.rand.nextInt(2) == 0 && this.waittime-- < 1) {
            this.ritual = true;
            this.waittime = 1000;
            this.getvictim = false;
        }

        super.onLivingUpdate();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        if (this.posX == 0 && this.posY == 0 && this.posZ == 0)
            return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        if (this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0 && this.worldObj.canBlockSeeTheSky(i, j, k)
                && this.rand.nextInt(25) == 0) {
            int l = this.worldObj.getFullBlockLightValue(i, j, k);
            Block i1 = this.worldObj.getBlock(i, j - 1, k);
            return i1 != null && i1 != Blocks.sand
                    && i1 != Blocks.cobblestone
                    && i1 != Blocks.log
                    && i1 != Blocks.double_stone_slab
                    && i1 != Blocks.stone_slab
                    && i1 != Blocks.planks
                    && i1 != Blocks.wool
                    && l > 10;
        }

        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity obj = damagesource.getEntity();

        if (!this.handleLavaMovement()) {
            this.worldObj.playSoundAtEntity(this, "morecreeps:preacherhurt", 1.0F, 1.0F);
        }

        if (this.getvictim && obj != null && !(obj instanceof CREEPSEntityRocket)) {
            obj.motionX += this.rand.nextFloat() * 1.98F;
            obj.motionY += this.rand.nextFloat() * 1.98F;
            obj.motionZ += this.rand.nextFloat() * 1.98F;
            return true;
        }

        if (obj != null && (obj instanceof CREEPSEntityRocket)) {
            obj = this.worldObj.getClosestPlayerToEntity(this, 30D);

            if (obj != null) {
                if ((obj instanceof EntityPlayer)) {
                    (obj).mountEntity(null);
                    this.getvictim = true;
                    this.victimEntity = ((obj));
                    this.victimEntity.motionX = 0.0D;
                    this.victimEntity.motionY = 0.0D;
                    this.victimEntity.motionZ = 0.0D;
                    this.raiselevel = this.rand.nextInt(50) + 50;
                    this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:preacherraise",
                            1.0F,
                            (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                }
            }
        }

        if (obj != null) {
            if (i < 1) {
                i = 1;
            }

            if ((obj instanceof CREEPSEntityGooDonut) || (obj instanceof CREEPSEntityRocket)
                    || (obj instanceof CREEPSEntityBullet)
                    || (obj instanceof CREEPSEntityRay)) {
                i = 2;
            }

            // wtf is that ?
            // health = (health - rand.nextInt(i)) + 1;
            this.raise = 1;
            this.waittime = 0;
            this.smoke();
            this.getvictim = true;
            this.victimEntity = ((obj));
            this.victimEntity.motionX = 0.0D;
            this.victimEntity.motionY = 0.0D;
            this.victimEntity.motionZ = 0.0D;
            this.raiselevel = this.rand.nextInt(50) + 50;
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:preacherraise",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
        }

        return true;
    }

    /**
     * knocks back this entity
     */
    @Override
    public void knockBack(Entity entity, float i, double d, double d1) {
        this.motionX *= 1.5D;
        this.motionZ *= 1.5D;
        this.motionY += 0.5D;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        return null;
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

    private void smokevictim(Entity entity) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                double d = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(
                        EnumParticleTypes.EXPLOSION_NORMAL,
                        (entity.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        (entity.posY + this.rand.nextFloat() * this.height + i) - 2D,
                        (entity.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        d,
                        d1,
                        d2);
            }
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:preacher";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:preacherhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:preacherdeath";
    }

    public void confetti(EntityPlayer player) {
        double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
        CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
        creepsentitytrophy.setLocationAndAngles(
                player.posX + d * 3D,
                player.posY - 2D,
                player.posZ + d1 * 3D,
                player.rotationYaw,
                0.0F);
        if (!this.worldObj.isRemote) {
            this.worldObj.spawnEntityInWorld(creepsentitytrophy);
        }
    }

    /**
     * Will get destroyed next tick.
     */

    @Override
    public void onDeath(DamageSource damagesource) {
        super.onDeath(damagesource);
        EntityPlayerMP player = (EntityPlayerMP) damagesource.getEntity();
        if (player != null) {
            if (!player.func_147099_x()
                    .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievegotohell)) {

                this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsAndWeirdos.achievegotohell, 1);
                this.confetti(player);
            }
        }

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(50) == 0) {
                this.dropItem(Items.diamond, this.rand.nextInt(2) + 1);
            }

            if (this.rand.nextInt(50) == 0) {
                this.entityDropItem(new ItemStack(Items.dye, 1, 4), 1.0F);
            }

            if (this.rand.nextInt(50) == 0) {
                this.entityDropItem(new ItemStack(Items.dye, 1, 3), 1.0F);
            }

            if (this.rand.nextInt(50) == 0) {
                this.entityDropItem(new ItemStack(Items.dye, 1, 1), 1.0F);
            }

            if (this.rand.nextInt(2) == 0) {
                this.dropItem(Items.gold_ingot, this.rand.nextInt(5) + 2);
            } else {
                this.dropItem(Items.book, 1);
                this.dropItem(Items.apple, 1);
            }
        }
    }
}
