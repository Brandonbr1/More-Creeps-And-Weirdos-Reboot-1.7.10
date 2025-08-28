package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.ai.EntityBabyMummyAI;

public class CREEPSEntityBabyMummy extends EntityMob {

    public ResourceLocation basetexture;
    public float babysize;
    static final ResourceLocation mummyTextures[] = {
            new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY1),
            new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY2),
            new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_MUMMY3) };
    public ResourceLocation texture;
    public int attackTime;

    public CREEPSEntityBabyMummy(World world) {
        super(world);
        this.basetexture = mummyTextures[this.rand.nextInt(mummyTextures.length)];
        this.texture = this.basetexture;
        this.babysize = this.rand.nextFloat() * 0.45F + 0.25F;
        this.setSize(0.6F, 0.6F);
        this.attackTime = 20;
        this.getNavigator().setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityBabyMummyAI(this));
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
        .setBaseValue(15D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.35D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(1D);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setFloat("BabySize", this.babysize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.babysize = nbttagcompound.getFloat("BabySize");
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (0.7F - this.babysize) * 2.0F);
        }
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null)
            return false;
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.posY);
        int k = MathHelper.floor_double(this.posZ);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        int l = this.worldObj.getFullBlockLightValue(i, j, k);
        return this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL && l > 10 && super.getCanSpawnHere();
        // Method used by Minecraft above, probably better to use it instead?
        // return (i1 == Blocks.sand || i1 == Blocks.bedrock) && i1 != Blocks.cobblestone && i1 != Blocks.log && i1 !=
        // Blocks.planks && i1 != Blocks.wool && worldObj.getCollidingBoundingBoxes(this, getBoundingBox()).size() == 0
        // && rand.nextInt(15) == 0 && l > 10;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:babymummy";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:babymummyhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:babymummydeath";
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    @Override
    public void attackEntity(Entity entity, float f) {

        float f1 = this.getBrightness(1.0F);

        if (f1 < 0.5F && this.rand.nextInt(100) == 0)
            // entityToAttack = null;
            return;

        if (f > 2.0F && f < 6F && this.rand.nextInt(10) == 0) {
            if (this.onGround) {
                int i = MathHelper.floor_double(entity.posX);
                int j = MathHelper.floor_double(entity.posY);
                int k = MathHelper.floor_double(entity.posZ);

                if (this.worldObj.getBlock(i, j - 2, k) == Blocks.sand) {
                    if (this.rand.nextInt(5) == 0) {
                        for (int l = 0; l < this.rand.nextInt(4) + 1; l++) {
                            this.worldObj.setBlockToAir(i, j - (l + 2), k);

                            if (this.rand.nextInt(5) == 0) {
                                this.worldObj.setBlockToAir(i + l, j - 2, k);
                            }

                            if (this.rand.nextInt(5) == 0) {
                                this.worldObj.setBlockToAir(i, j - 2, k + l);
                            }
                        }
                    }

                    if (this.rand.nextInt(5) == 0) {
                        if (this.rand.nextInt(2) == 0) {
                            int i1 = this.rand.nextInt(5);

                            for (int k1 = -3; k1 < 3; k1++) {
                                this.worldObj.setBlock(i + k1, j + i1, k + 2, Blocks.sand);
                                this.worldObj.setBlock(i + k1, j + i1, k - 2, Blocks.sand);
                            }
                        }

                        if (this.rand.nextInt(2) == 0) {
                            int j1 = this.rand.nextInt(5);

                            for (int l1 = -3; l1 < 3; l1++) {
                                this.worldObj.setBlock(i + 2, j + j1, k + l1, Blocks.sand);
                                this.worldObj.setBlock(i - 2, j + j1, k + l1, Blocks.sand);
                            }
                        }
                    }

                    double d = entity.posX - this.posX;
                    double d1 = entity.posZ - this.posZ;
                    float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                    this.motionX = (d / f2) * 0.5D * 0.8000000019209289D + this.motionX * 0.18000000098023225D;
                    this.motionZ = (d1 / f2) * 0.5D * 0.70000000192092893D + this.motionZ * 0.18000000098023225D;
                }
            }
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(5) == 0) {
                this.dropItem(Item.getItemFromBlock(Blocks.wool), this.rand.nextInt(6) + 1);
            } else {
                this.dropItem(Item.getItemFromBlock(Blocks.sand), this.rand.nextInt(3) + 1);
            }
        }

        super.onDeath(damagesource);
    }
}
