package fr.elias.morecreeps.common.entity.nice;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
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

public class CREEPSEntityDigBug extends EntityMob {

    private static final Item dropItems[];
    protected double attackRange;
    private int angerLevel;
    public int digstage;
    public int digtimer;
    public double holeX;
    public double holeY;
    public double holeZ;
    public double xx;
    public double yy;
    public double zz;
    public int holedepth;
    public int skinframe;
    public int lifespan;
    public int hunger;
    public int waittimer;
    public float modelsize;
    public String texture;

    public CREEPSEntityDigBug(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/digbug0.png";
        this.angerLevel = 0;
        this.attackRange = 16D;
        this.setSize(0.5F, 1.2F);
        this.hunger = this.rand.nextInt(3) + 1;
        this.digstage = 0;
        this.digtimer = this.rand.nextInt(500) + 500;
        this.lifespan = 5000;
        this.holedepth = 0;
        this.modelsize = 1.0F;
        this.setSize(1.0F, 1.0F);
        this.targetTasks.addTask(0, new CREEPSEntityDigBug.AIFindPlayerToAttack());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(60D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.3D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(4D);
    }

    @Override
    public float getShadowSize() {
        return 0.4F;
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate() {
        if (this.lifespan >= 0 && this.holedepth > 0) {
            this.lifespan--;

            if (this.lifespan <= 0) {
                this.digtimer = this.rand.nextInt(20);
                this.xx = -1D;
                this.yy = this.holedepth;
                this.zz = -1D;
                this.digstage = 4;
            }
        }
        super.onUpdate();
    }

    /** Determines if an entity can be despawned, used on idle far away entities */
    @Override
    protected boolean canDespawn() {
        return this.lifespan < 0;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj == null) return;
        if (this.prevPosX != this.posX || this.prevPosY != this.posY) {
            this.texture = "morecreeps:textures/entity/digbug" + this.skinframe + ".png";
            this.skinframe++;

            if (this.skinframe > 3) {
                this.skinframe = 0;
            }
        }

        if (this.digstage == 0 && this.posY < 90D && this.digtimer-- < 1) {
            int i = MathHelper.floor_double(this.posX);
            int i1 = MathHelper.floor_double(this.posY);
            int i2 = MathHelper.floor_double(this.posZ);
            Block l2 = this.worldObj.getBlock(i, i1 - 1, i2);
            this.holedepth = this.rand.nextInt(2) + 3;

            if (l2 == Blocks.grass) {
                if (this.checkHole(i, i1, i2, this.holedepth)) {
                    this.digstage = 1;
                    this.holeX = i;
                    this.holeY = i1;
                    this.holeZ = i2;
                    this.xx = 0.0D;
                    this.yy = 1.0D;
                    this.zz = 0.0D;
                } else {
                    this.digtimer = this.rand.nextInt(200);
                }
            }
        }

        if (this.digstage == 1) {
            int j = MathHelper.floor_double(this.posX);
            int j1 = MathHelper.floor_double(this.posY);
            int j2 = MathHelper.floor_double(this.posZ);
            this.worldObj.setBlockToAir(j, j1, j2);
            this.worldObj.setBlockToAir(j, j1 + 1, j2);

            if (this.posX < this.holeX + this.xx) {
                this.motionX += 0.20000000298023224D;
            } else {
                this.motionX -= 0.20000000298023224D;
            }

            if (this.posZ < this.holeZ + this.zz) {
                this.motionZ += 0.20000000298023224D;
            } else {
                this.motionZ -= 0.20000000298023224D;
            }
            if (this.worldObj.isRemote) {
                MoreCreepsAndWeirdos.proxy.dirtDigBug(this.worldObj, this, this.rand, 1);
            }

            if (this.digtimer-- < 1) {
                this.digtimer = this.rand.nextInt(20);
                this.setPosition(
                    (int) (this.holeX + this.xx),
                    (int) (this.holeY - this.yy),
                    (int) (this.holeZ + this.zz));
                Block i3 = this.worldObj
                    .getBlock((int) (this.holeX + this.xx), (int) (this.holeY - this.yy), (int) (this.holeZ + this.zz));

                if (this.rand.nextInt(50) == 0) {
                    i3 = Blocks.coal_ore;
                }

                if (i3 != Blocks.sand && i3 != Blocks.log) {
                    for (int j3 = 0; j3 < this.rand.nextInt(2) + 1; j3++) {
                        EntityItem entityitem1 = new EntityItem(
                            this.worldObj,
                            (int) (this.holeX + this.xx),
                            (int) ((this.holeY - this.yy) + 1.0D),
                            (int) (this.holeZ + this.zz),
                            new ItemStack(i3, 1, 1));
                        this.worldObj.spawnEntityInWorld(entityitem1);
                    }
                }

                this.worldObj.setBlockToAir(
                    (int) (this.holeX + this.xx),
                    (int) (this.holeY - this.yy),
                    (int) (this.holeZ + this.zz));

                if (this.zz++ > 1.0D) {
                    this.zz = 0.0D;
                    this.setPosition(
                        (int) (this.holeX + this.xx),
                        (int) (this.holeY - this.yy),
                        (int) (this.holeZ + this.zz));

                    if (this.xx++ > 1.0D) {
                        this.xx = 0.0D;
                        this.setPosition(
                            (int) (this.holeX + this.xx),
                            (int) (this.holeY - this.yy),
                            (int) (this.holeZ + this.zz));

                        if (this.yy++ > this.holedepth) {
                            for (int k3 = 0; k3 < this.rand.nextInt(8) + 5; k3++) {
                                int l3 = this.rand.nextInt(40) + 40;
                                int i4 = this.rand.nextInt(40) + 40;

                                if (this.rand.nextInt(1) == 0) {
                                    l3 *= -1;
                                }

                                if (this.rand.nextInt(1) == 0) {
                                    i4 *= -1;
                                }

                                CREEPSEntityBubbleScum creepsentitybubblescum = new CREEPSEntityBubbleScum(
                                    this.worldObj);
                                creepsentitybubblescum.setLocationAndAngles(
                                    this.posX + l3,
                                    this.posY + this.holedepth + 2D,
                                    this.posZ + i4,
                                    this.rotationYaw,
                                    0.0F);
                                creepsentitybubblescum.motionX = this.rand.nextFloat() * 1.5F;
                                creepsentitybubblescum.motionY = this.rand.nextFloat() * 2.0F;
                                creepsentitybubblescum.motionZ = this.rand.nextFloat() * 1.5F;
                                creepsentitybubblescum.fallDistance = -25F;
                                if (!this.worldObj.isRemote) {
                                    this.worldObj.spawnEntityInWorld(creepsentitybubblescum);
                                }
                            }

                            this.digstage = 2;
                            // double moveSpeed = 0.0D; // TODO (unused)
                            this.lifespan = 5000;
                            this.motionY = 0.44999998807907104D;
                            this.setPosition(
                                (int) (this.holeX + 1.0D),
                                (int) (this.holeY - this.yy),
                                (int) (this.holeZ + 1.0D));
                            this.digtimer = this.rand.nextInt(5) + 5;
                        }
                    }
                }
            }
        }

        if (this.digstage == 2 && this.digtimer-- < 1) {
            this.digtimer = this.rand.nextInt(20);

            for (int k = 0; k < 20 + this.digtimer; k++) {
                MoreCreepsAndWeirdos.proxy.bubble(this.worldObj, this);
            }

            this.digtimer = 50;
            // List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
            // getBoundingBox().expand(5D, 1.0D, 5D));

            List<?> list = this.worldObj
                .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(5.0D, 1.0D, 5.0D));
            for (Object o : list) {
                Entity entity = (Entity) o;

                if ((entity instanceof CREEPSEntityBubbleScum)) {
                    if (entity != null) {
                        entity.setDead();
                        // double moveSpeed = 0.4F; // TODO (unused)
                        this.motionY = 0.60000002384185791D;
                        this.digstage = 3;
                        this.digtimer = 50;
                    }
                }
            }
        }

        if (this.digstage == 3) {
            int l = this.rand.nextInt(25) + 15;
            this.worldObj.playSoundAtEntity(
                this,
                "morecreeps:digbugeat",
                1.0F,
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            for (int l1 = 0; l1 < l; l1++) {
                if (this.worldObj.isRemote) {
                    for (int k2 = 0; k2 < 45; k2++) {
                        MoreCreepsAndWeirdos.proxy.dirtDigBug(this.worldObj, this, this.rand, k2);
                    }
                }

                EntityItem entityitem = this.entityDropItem(new ItemStack(Items.cookie, 1, 0), 1.0F);
                entityitem.motionY += this.rand.nextFloat() * 2.0F + 3F;
                entityitem.motionX += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.33F;
                entityitem.motionZ += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.33F;
            }

            if (this.hunger-- < 1) {
                this.digtimer = this.rand.nextInt(20);
                this.xx = -1D;
                this.yy = this.holedepth;
                this.zz = -1D;
                this.digstage = 4;
                this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:digbugfull",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            } else {
                this.digstage = 2;
                this.digtimer = 50;
            }
        }

        if (this.digstage == 4) {
            if (this.posX < this.holeX + this.xx) {
                this.motionX += 0.20000000298023224D;
            } else {
                this.motionX -= 0.20000000298023224D;
            }

            if (this.posZ < this.holeZ + this.zz) {
                this.motionZ += 0.20000000298023224D;
            } else {
                this.motionZ -= 0.20000000298023224D;
            }

            if (this.worldObj.isRemote) {
                MoreCreepsAndWeirdos.proxy.dirtDigBug(this.worldObj, this, this.rand, 1);
            }

            if (this.digtimer-- < 1) {
                this.digtimer = this.rand.nextInt(10);

                if (this.worldObj
                    .getBlock((int) (this.holeX + this.xx), (int) (this.holeY - this.yy), (int) (this.holeZ + this.zz))
                    == Blocks.air) {
                    this.worldObj.setBlock(
                        (int) (this.holeX + this.xx),
                        (int) (this.holeY - this.yy),
                        (int) (this.holeZ + this.zz),
                        Blocks.dirt);
                }

                if (this.zz++ > 2D) {
                    this.zz = -1D;
                    this.setPosition(
                        (int) (this.holeX + this.xx),
                        (int) (this.holeY - this.yy) + 1,
                        (int) (this.holeZ + this.zz));

                    if (this.xx++ > 2D) {
                        this.xx = -1D;
                        this.setPosition(
                            (int) (this.holeX + this.xx),
                            (int) (this.holeY - this.yy) + 1,
                            (int) (this.holeZ + this.zz));

                        if (this.yy-- == 1.0D) {
                            this.digstage = 0;
                            this.digtimer = this.rand.nextInt(8000) + 1000;
                            this.setPosition(
                                (int) (this.holeX + 1.0D),
                                (int) (this.holeY + this.yy + 1.0D),
                                (int) (this.holeZ + 1.0D));
                        }
                    }
                }
            }
        }
    }

    /** Checks if this entity is inside of an opaque block */
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.digstage == 1 || this.digstage == 4) return false;
        else return super.isEntityInsideOpaqueBlock();
    }

    public boolean checkHole(int i, int j, int k, int l) {
        for (int i1 = 0; i1 < l; i1++) {
            for (int j1 = 0; j1 < 3; j1++) {
                for (int k1 = 0; k1 < 3; k1++) {
                    Block l1 = this.worldObj.getBlock(i + j1, j - i1 - 1, k + k1);

                    if (l1 == Blocks.air) return false;
                }
            }
        }

        return true;
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (entity.getBoundingBox() != null && this.getBoundingBox() != null) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.40000000000000002D * 0.10000000192092896D + this.motionX * 0.18000000098023225D;
            this.motionZ = (d1 / f1) * 0.40000000000000002D * 0.070000001920928964D
                + this.motionZ * 0.18000000098023225D;
            if (f < 2D - (1.0D - this.modelsize) && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                this.attackTime = 10;
                entity.motionX = -(this.motionX * 3D);
                entity.motionY = this.rand.nextFloat() * 2.133F;
                entity.motionZ = -(this.motionZ * 3D);
                this.attackEntityAsMob(entity);
            }
        }
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setShort("Anger", (short) this.angerLevel);
        nbttagcompound.setInteger("DigStage", this.digstage);
        nbttagcompound.setInteger("DigTimer", this.digtimer);
        nbttagcompound.setInteger("LifeSpan", this.lifespan);
        nbttagcompound.setInteger("HoleDepth", this.holedepth);
        nbttagcompound.setDouble("XX", this.xx);
        nbttagcompound.setDouble("YY", this.yy);
        nbttagcompound.setDouble("ZZ", this.zz);
        nbttagcompound.setDouble("holeX", this.holeX);
        nbttagcompound.setDouble("holeY", this.holeY);
        nbttagcompound.setDouble("holeZ", this.holeZ);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.angerLevel = nbttagcompound.getShort("Anger");
        this.digstage = nbttagcompound.getInteger("DigStage");
        this.digtimer = nbttagcompound.getInteger("DigTimer");
        this.lifespan = nbttagcompound.getInteger("LifeSpan");
        this.holedepth = nbttagcompound.getInteger("HoleDepth");
        this.xx = nbttagcompound.getDouble("XX");
        this.yy = nbttagcompound.getDouble("YY");
        this.zz = nbttagcompound.getDouble("ZZ");
        this.holeX = nbttagcompound.getDouble("holeX");
        this.holeY = nbttagcompound.getDouble("holeY");
        this.holeZ = nbttagcompound.getDouble("holeZ");
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
        int j1 = this.worldObj.countEntities(CREEPSEntityDigBug.class);
        return (i1 == Blocks.grass || i1 == Blocks.dirt) && i1 != Blocks.cobblestone
            && i1 != Blocks.log
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.stone_slab
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && this.rand.nextInt(25) == 0
            && l > 10
            && j1 < 10;
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        if (damagesource.getEntity() instanceof EntityPlayer) {
            List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                this,
                this.getBoundingBox()
                    .expand(32D, 32D, 32D));

            for (Object o : list) {
                Entity entity1 = (Entity) o;

                if (entity1 instanceof CREEPSEntityDigBug) {
                    CREEPSEntityDigBug creepsentitydigbug = (CREEPSEntityDigBug) entity1;
                    creepsentitydigbug.becomeAngryAt(damagesource.getEntity());
                }
            }

            this.becomeAngryAt(damagesource.getEntity());
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    class AIFindPlayerToAttack extends EntityAINearestAttackableTarget {

        public AIFindPlayerToAttack() {
            super(CREEPSEntityDigBug.this, EntityPlayer.class, 1, true);
        }

        @Override
        public void updateTask() {
            try {
                EntityLivingBase target = CREEPSEntityDigBug.this.getAttackTarget();
                float f = CREEPSEntityDigBug.this.getDistanceToEntity(target);
                CREEPSEntityDigBug.this.attackEntity(target, f);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean shouldExecute() {
            if (CREEPSEntityDigBug.this.angerLevel > 0) {
                CREEPSEntityDigBug.this.angerLevel--;
            }

            if (CREEPSEntityDigBug.this.angerLevel == 0) return false;
            else return super.shouldExecute();
        }
    }

    private void becomeAngryAt(Entity entity) {
        this.setRevengeTarget((EntityLivingBase) entity);
        this.angerLevel = 400 + this.rand.nextInt(400);
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        if (this.digstage == 0) return "morecreeps:digbug";

        if (this.digstage == 1 || this.digstage == 4) return "morecreeps:digbugdig";

        if (this.digstage == 2) return "morecreeps:digbugcall";
        else return null;
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:digbughurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:digbugdeath";
    }

    /** Returns the item ID for the item the mob drops on death. */
    protected Item getDropItemId() {
        return dropItems[this.rand.nextInt(dropItems.length)];
    }

    static {
        dropItems = (new Item[] { Item.getItemFromBlock(Blocks.cobblestone), Item.getItemFromBlock(Blocks.gravel),
            Item.getItemFromBlock(Blocks.cobblestone), Item.getItemFromBlock(Blocks.gravel),
            Item.getItemFromBlock(Blocks.iron_ore), Item.getItemFromBlock(Blocks.mossy_cobblestone) });
    }
}
