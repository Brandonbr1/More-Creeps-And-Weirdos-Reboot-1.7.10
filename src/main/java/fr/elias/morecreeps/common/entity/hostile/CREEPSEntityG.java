package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.Reference;

public class CREEPSEntityG extends EntityMob {

    public float modelsize;
    public ResourceLocation texture;

    public CREEPSEntityG(World world) {
        super(world);
        this.texture = new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_G);
        this.setSize(this.width * 2.0F, this.height * 2.5F);
        this.modelsize = 2.0F;
        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        // this.tasks.addTask(2, new CREEPSEntityG.AIAttackEntity());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
        .setBaseValue(80);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
        .setBaseValue(0.45D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
        .setBaseValue(2D);
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
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        if (entity != null) {
            // according to wiki(https://morecreepsandweirdos.fandom.com/wiki/G), it gets flug back 40 blocks only by projectiles
            if (damagesource.isProjectile()) {
                double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
                double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
                this.motionX = d * 11D;
                this.motionZ = d1 * 11D;
            }
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.onGround) {
            double d = entity.posX - this.posX;
            double d2 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d2 * d2);
            this.motionX = (d / f1) * 0.40000000000000002D * 0.50000000192092897D + this.motionX * 0.18000000098023225D;
            this.motionZ = (d2 / f1) * 0.40000000000000002D * 0.37000000192092897D + this.motionZ * 0.18000000098023225D;
            this.motionY = 0.15000000019604645D;
            this.attackEntityAsMob(entity);
        }

        if (f < 6D) {
            double d1 = entity.posX - this.posX;
            double d3 = entity.posZ - this.posZ;
            float f2 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);
            this.motionX = (d1 / f2) * 0.40000000000000002D * 0.40000000192092894D + this.motionX * 0.18000000098023225D;
            this.motionZ = (d3 / f2) * 0.40000000000000002D * 0.27000000192092893D + this.motionZ * 0.18000000098023225D;
            this.rotationPitch = 90F;
            this.attackEntityAsMob(entity);
        }
    }
    /**
    class AIAttackEntity extends EntityAIBase {

        @Override
        public boolean shouldExecute() {
            return CREEPSEntityG.this.getAttackTarget() != null;
        }

        @Override
        public void updateTask() {
            try {
                float f = CREEPSEntityG.this.getDistanceToEntity(CREEPSEntityG.this.getAttackTarget());
                if (f < 256F) {
                    // lets not attack from that many block away......
                    CREEPSEntityG.this.attackEntity(CREEPSEntityG.this.getAttackTarget(), 1);
                    CREEPSEntityG.this.getLookHelper()
                    .setLookPositionWithEntity(CREEPSEntityG.this.getAttackTarget(), 10.0F, 10.0F);
                    CREEPSEntityG.this.getNavigator()
                    .clearPathEntity();
                    CREEPSEntityG.this.getMoveHelper()
                    .setMoveTo(
                            CREEPSEntityG.this.getAttackTarget().posX,
                            CREEPSEntityG.this.getAttackTarget().posY,
                            CREEPSEntityG.this.getAttackTarget().posZ,
                            0.5D);
                }
                if (f < 2F) {
                    CREEPSEntityG.this.attackEntity(CREEPSEntityG.this.getAttackTarget(), 1);
                    CREEPSEntityG.this.attackEntityAsMob(CREEPSEntityG.this.getAttackTarget());
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }
     **/

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
        int j1 = this.worldObj.countEntities(CREEPSEntityMummy.class);
        return i1 != Blocks.sand && i1 != Blocks.cobblestone
                && i1 != Blocks.log
                && i1 != Blocks.double_stone_slab
                && i1 != Blocks.stone_slab
                && i1 != Blocks.planks
                && i1 != Blocks.wool
                && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
                && this.rand.nextInt(15) == 0
                && j1 < 5;
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (2.0F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        return "morecreeps:g";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        return "morecreeps:ghurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        return "morecreeps:gdeath";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (!this.worldObj.isRemote) {
            int maxitems = 0;
            if (this.rand.nextInt(200) == 98) {
                this.dropItem(Item.getItemFromBlock(Blocks.gold_block), 1);
                maxitems++;
            }
            if (this.rand.nextInt(5) == 0) {
                this.dropItem(Items.gold_ingot, this.rand.nextInt(2) + 1);
                maxitems++;
            }
            if (this.rand.nextInt(150) > 145) {
                this.dropItem(Items.golden_sword, 1);
                maxitems++;
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_pickaxe, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_shovel, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_axe, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_helmet, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_chestplate, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.golden_boots, 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 80) {
                    this.dropItem(Items.wheat, this.rand.nextInt(6) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Item.getItemFromBlock(Blocks.glass), this.rand.nextInt(6) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(MoreCreepsAndWeirdos.goodonut, this.rand.nextInt(3) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 88) {
                    this.dropItem(Item.getItemFromBlock(Blocks.grass), this.rand.nextInt(6) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Item.getItemFromBlock(Blocks.glowstone), this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.glowstone_dust, this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.glass_bottle, this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.gold_nugget, this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) == 88) {
                    this.dropItem(Items.golden_apple, this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) > 98) {
                    this.dropItem(Items.gunpowder, this.rand.nextInt(2) + 1);
                    maxitems++;
                }
            }
            if (maxitems < 3) {
                if (this.rand.nextInt(100) == 88) {
                    this.dropItem(Items.ghast_tear, this.rand.nextInt(2) + 1);
                }
            }
        }
        super.onDeath(damagesource);
    }
}
