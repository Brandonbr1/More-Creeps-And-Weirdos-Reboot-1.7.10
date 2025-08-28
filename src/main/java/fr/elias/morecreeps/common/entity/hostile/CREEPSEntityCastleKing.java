package fr.elias.morecreeps.common.entity.hostile;

import java.util.Random;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.client.particles.CREEPSFxSmoke;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;

public class CREEPSEntityCastleKing extends EntityMob {

    private boolean foundplayer;
    private PathEntity pathToEntity;
    protected Entity playerToAttack;
    EntityPlayer entityplayer;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    public int intrudercheck;
    public ItemStack gem;
    public String texture;
    public double moveSpeed;
    public float attackStrength;
    public double health;
    public static Random random = new Random();
    public float hammerswing;

    public CREEPSEntityCastleKing(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/castleking.png";
        this.moveSpeed = 0.0;
        this.attackStrength = 4;
        // this.health = 90;
        this.health = 1;
        this.health = this.rand.nextInt(60) + 60;
        this.setSize(2.0F, 1.6F);
        this.foundplayer = false;
        this.intrudercheck = 25;
        this.hammerswing = 0.0F;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(60D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.4D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(4D);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        // Go away like a normal agressive mob
        if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.hammerswing < 0.0F) {
            this.hammerswing += 0.45F;
        } else {
            this.hammerswing = 0.0F;
        }

        double d = -MathHelper.sin((this.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((this.rotationYaw * (float) Math.PI) / 180F);
        if (this.worldObj.isRemote) {
            CREEPSFxSmoke creepsfxsmoke = new CREEPSFxSmoke(
                    this.worldObj,
                    (this.posX + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
                    ((this.posY - 1.0D) + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
                    (this.posZ + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
                    0.55F,
                    0);
            creepsfxsmoke.renderDistanceWeight = 20D;
            Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }

        if (this.intrudercheck-- < 0) {
            this.intrudercheck = 25;
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 10D);

            if (entityplayer != null && this.canEntityBeSeen(entityplayer)) {
                this.moveSpeed = 0.222F;
            }
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        //TODO: ADD BACK HAMMER SWING
        if (f < 3.1F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY) {

            this.attackTime = 10;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attackStrength);
        }


    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("FoundPlayer", this.foundplayer);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.foundplayer = nbttagcompound.getBoolean("FoundPlayer");
        this.attackStrength = 8;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:castleking";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:castlekinghurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:castlekingdeath";
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
        return i1 != Blocks.cobblestone && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
                && this.worldObj.checkBlockCollision(this.getBoundingBox())
                && this.worldObj.canBlockSeeTheSky(j, k, l)
                && this.rand.nextInt(5) == 0
                && l > 10;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
    protected boolean canDespawn() {
        return false;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    private void smoke() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i * 0.5F - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i * 0.5F - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            "EXPLOSION".toLowerCase(),
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i * 0.5F - this.width,
                            d,
                            d1,
                            d2);
                }
            }
        }
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    @Override
    public ItemStack getHeldItem()
    {
        return new ItemStack(MoreCreepsAndWeirdos.gemsword, 1);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            int i = 0;
            this.gem = new ItemStack(MoreCreepsAndWeirdos.skygem, 1);

            if (this.checkGem(this.gem)) {
                i++;
            }

            this.gem = new ItemStack(MoreCreepsAndWeirdos.earthgem, 1);

            if (this.checkGem(this.gem)) {
                i++;
            }

            this.gem = new ItemStack(MoreCreepsAndWeirdos.firegem, 1);

            if (this.checkGem(this.gem)) {
                i++;
            }

            this.gem = new ItemStack(MoreCreepsAndWeirdos.healinggem, 1);

            if (this.checkGem(this.gem)) {
                i++;
            }

            this.gem = new ItemStack(MoreCreepsAndWeirdos.mininggem, 1);

            if (this.checkGem(this.gem)) {
                i++;
            }

            if (i == 5) {
                this.smoke();
                this.smoke();
                this.dropItem(MoreCreepsAndWeirdos.gemsword, 1);
                this.dropItem(MoreCreepsAndWeirdos.money, this.rand.nextInt(100) + 50);
            } else {
                this.dropItem(Items.iron_sword, 1);
                this.dropItem(Items.book, 1);
            }
        }

        this.smoke();
        super.onDeath(damagesource);

    }

    public boolean checkGem(ItemStack itemstack) {
        if (this.entityplayer == null)
            return false;

        Object obj = null;
        ItemStack aitemstack[] = (this.entityplayer).inventory.mainInventory;
        boolean flag = false;
        int i = 0;

        do {
            if (i >= aitemstack.length) {
                break;
            }

            ItemStack itemstack1 = aitemstack[i];

            if (itemstack1 != null && itemstack1 == itemstack) {
                flag = true;
                break;
            }

            i++;
        } while (true);

        return flag;
    }
}
