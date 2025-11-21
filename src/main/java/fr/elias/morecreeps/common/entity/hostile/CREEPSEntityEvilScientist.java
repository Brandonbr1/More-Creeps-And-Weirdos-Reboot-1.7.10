package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.entity.proj.CREEPSEntityEvilLight;

public class CREEPSEntityEvilScientist extends EntityMob {

    // public int basehealth;
    public int weapon;
    public boolean used;
    public int interest;
    public String ss;
    public float distance;
    public String basetexture;
    public int experimenttimer;
    public boolean experimentstart;
    public int stage;
    public int towerX;
    public int towerY;
    public int towerZ;
    public int towerHeight;
    public boolean water;
    public Block area;
    public int numexperiments;
    public boolean trulyevil;
    public boolean towerBuilt;
    public float modelsize;
    public String texture;

    public CREEPSEntityEvilScientist(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/evilscientist.png";
        this.basetexture = this.texture;
        this.experimenttimer = this.rand.nextInt(100) + 100;
        this.experimentstart = false;
        this.stage = 0;
        this.water = false;
        this.trulyevil = false;
        this.towerBuilt = false;
        this.numexperiments = this.rand.nextInt(3) + 1;
        this.isImmuneToFire = true;
        this.modelsize = 1.0F;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(4, new CREEPSEntityEvilScientist.AITargetingSystem());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        if (this.trulyevil) {
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 1, true));
        }
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.45D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(1D);
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("ExperimentTimer", this.experimenttimer);
        nbttagcompound.setBoolean("ExperimentStart", this.experimentstart);
        nbttagcompound.setInteger("Stage", this.stage);
        nbttagcompound.setBoolean("Water", this.water);
        nbttagcompound.setInteger("NumExperiments", this.numexperiments);
        nbttagcompound.setInteger("TowerX", this.towerX);
        nbttagcompound.setInteger("TowerY", this.towerY);
        nbttagcompound.setInteger("TowerZ", this.towerZ);
        nbttagcompound.setInteger("TowerHeight", this.towerHeight);
        nbttagcompound.setBoolean("TrulyEvil", this.trulyevil);
        nbttagcompound.setBoolean("TowerBuilt", this.towerBuilt);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.experimenttimer = nbttagcompound.getInteger("ExperimentTimer");
        this.experimentstart = nbttagcompound.getBoolean("ExperimentStart");
        this.stage = nbttagcompound.getInteger("Stage");
        this.water = nbttagcompound.getBoolean("Water");
        this.numexperiments = nbttagcompound.getInteger("NumExperiments");
        this.towerX = nbttagcompound.getInteger("TowerX");
        this.towerY = nbttagcompound.getInteger("TowerY");
        this.towerZ = nbttagcompound.getInteger("TowerZ");
        this.towerHeight = nbttagcompound.getInteger("TowerHeight");
        this.trulyevil = nbttagcompound.getBoolean("TrulyEvil");
        this.towerBuilt = nbttagcompound.getBoolean("TowerBuilt");
        this.modelsize = nbttagcompound.getFloat("ModelSize");

        if (this.trulyevil) {
            this.texture = "morecreeps:textures/entity/evilscientistblown.png";
        }
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.trulyevil) {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(0.75D);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(0.45D);
        }
        this.fallDistance = 0.0F;

        if (this.stage == 3 && this.posY + 3D < this.towerY + this.towerHeight) {
            this.stage = 2;
        }

        if (this.stage == 0) {
            if (this.experimenttimer > 0 && !this.experimentstart) {
                this.experimenttimer--;
            }

            if (this.experimenttimer == 0) {
                this.experimentstart = true;
                this.stage = 1;
                this.experimenttimer = this.rand.nextInt(5000) + 100;
            }
        }

        if (this.stage == 1 && this.onGround && this.experimentstart && this.posY > 63D) {
            this.towerX = MathHelper.floor_double(this.posX) + 2;
            this.towerY = MathHelper.floor_double(this.posY);
            this.towerZ = MathHelper.floor_double(this.posZ) + 2;
            this.towerHeight = this.rand.nextInt(20) + 10;
            this.area = Blocks.air;

            /*
             * for (int i = 0; i < towerHeight; i++)
             * {
             * for (int i2 = 0; i2 < 3; i2++)
             * {
             * for (int l3 = 0; l3 < 3; l3++)
             * {
             * area += worldObj.getBlock(towerX + l3, towerY + i, towerZ + i2 + 1);
             * area += worldObj.getBlock(towerX + i2 + 1, towerY + i, towerZ + l3);
             * }
             * }
             * }
             */

            for (int i = 0; i < this.towerHeight; i++) {
                for (int i2 = 0; i2 < 3; i2++) {
                    for (int l3 = 0; l3 < 3; l3++) {
                        this.area = this.worldObj.getBlock(this.towerX + l3, this.towerY + i, this.towerZ + i2 + 1);
                        this.area = this.worldObj.getBlock(this.towerX + i2 + 1, this.towerY + i, this.towerZ + l3);
                    }
                }
            }

            if (this.posY > 63D && this.area == Blocks.air
                && this.worldObj.getBlock(this.towerX + 2, this.towerY - 1, this.towerZ + 2) != Blocks.air
                && this.worldObj.getBlock(this.towerX + 2, this.towerY - 1, this.towerZ + 2) != Blocks.water
                && this.worldObj.getBlock(this.towerX + 2, this.towerY - 1, this.towerZ + 2) != Blocks.flowing_water) {
                this.towerBuilt = true;

                for (int j = 0; j < this.towerHeight; j++) {
                    for (int j2 = 0; j2 < 3; j2++) {
                        for (int i4 = 0; i4 < 3; i4++) {
                            // previously called "byte byte0"
                            Block byte0 = Blocks.cobblestone;

                            if (this.rand.nextInt(5) == 0) {
                                byte0 = Blocks.mossy_cobblestone;
                            }

                            this.worldObj.setBlock(this.towerX + i4, this.towerY + j, this.towerZ + j2 + 1, byte0);
                            byte0 = Blocks.cobblestone;

                            if (this.rand.nextInt(5) == 0) {
                                byte0 = Blocks.mossy_cobblestone;
                            }

                            this.worldObj.setBlock(this.towerX + j2 + 1, this.towerY + j, this.towerZ + i4, byte0);
                        }
                    }
                }

                this.worldObj
                    .setBlock(this.towerX + 2, this.towerY + this.towerHeight, this.towerZ + 2, Blocks.crafting_table);

                for (int k = 0; k < this.towerHeight; k++) {
                    this.worldObj.setBlock(this.towerX, this.towerY + k, this.towerZ, Blocks.ladder);
                    // TODO : Fix this !
                    /*
                     * Blocks.ladder.onBlockPlaced(worldObj, new BlockPos(towerX, towerY + k, towerZ), 65);
                     * onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
                     */
                    Blocks.ladder
                        .onBlockPlaced(this.worldObj, this.towerX, this.towerY + k, this.towerZ, 0, 0, 0, 0, 0);
                }

                this.stage = 2;
            } else {
                this.stage = 0;
                this.experimenttimer = this.rand.nextInt(100) + 50;
                this.experimentstart = false;
            }
        }

        if (this.stage == 2) {
            if (this.posX < this.towerX) {
                this.motionX = 0.20000000298023224D;
            }

            if (this.posZ < this.towerZ) {
                this.motionZ = 0.20000000298023224D;
            }

            if (Math.abs(this.posX - this.towerX) < 0.40000000596046448D
                && Math.abs(this.posZ - this.towerZ) < 0.40000000596046448D) {
                this.motionX = 0.0D;
                this.motionZ = 0.0D;
                this.motionY = 0.30000001192092896D;
                int l = MathHelper.floor_double(this.posX);
                int k2 = MathHelper.floor_double(this.posY);
                int j4 = MathHelper.floor_double(this.posZ);
                this.worldObj.setBlockToAir(l, k2 + 2, j4);

                if (this.posY > this.towerY + this.towerHeight) {
                    this.motionY = 0.0D;
                    this.posZ++;
                    this.posX++;
                    this.stage = 3;
                    this.experimenttimer = this.rand.nextInt(1000) + 500;
                    this.experimentstart = false;
                }
            }
        }

        if (this.stage == 3) {
            this.posY = this.towerY + this.towerHeight;
            this.posX = this.towerX + 2;
            this.posZ = this.towerZ + 2;
            this.setPosition(this.towerX + 2, this.towerY + this.towerHeight, this.towerZ + 2);
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
            this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(0.0D);

            if (this.experimenttimer > 0) {
                this.experimenttimer--;
            }

            if (this.rand.nextInt(200) == 0) {
                int i1 = MathHelper.floor_double(this.posX);
                int l2 = MathHelper.floor_double(this.posY);
                int k4 = MathHelper.floor_double(this.posZ);
                this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, i1, l2 + 3, k4));
            }

            if (this.rand.nextInt(150) == 0 && !this.water) {
                this.worldObj
                    .setBlock(this.towerX + 2, this.towerY + this.towerHeight, this.towerZ + 1, Blocks.flowing_water);
                this.worldObj
                    .setBlock(this.towerX + 3, this.towerY + this.towerHeight, this.towerZ + 2, Blocks.flowing_water);
                this.worldObj
                    .setBlock(this.towerX + 1, this.towerY + this.towerHeight, this.towerZ + 2, Blocks.flowing_water);
                this.worldObj
                    .setBlock(this.towerX + 2, this.towerY + this.towerHeight, this.towerZ + 3, Blocks.flowing_water);
                this.water = true;
            }

            if (this.rand.nextInt(8) == 0) {
                CREEPSEntityEvilLight creepsentityevillight = new CREEPSEntityEvilLight(this.worldObj);
                creepsentityevillight.setLocationAndAngles(
                    this.towerX,
                    this.towerY + this.towerHeight,
                    this.towerZ,
                    this.rotationYaw,
                    0.0F);
                creepsentityevillight.motionX = this.rand.nextFloat() * 2.0F - 1.0F;
                creepsentityevillight.motionZ = this.rand.nextFloat() * 2.0F - 1.0F;
                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentityevillight);
                }
            }

            if (this.rand.nextInt(10) == 0) {
                for (int j1 = 0; j1 < 4; j1++) {
                    for (int i3 = 0; i3 < 10; i3++) {
                        double d = this.rand.nextGaussian() * 0.02D;
                        double d2 = this.rand.nextGaussian() * 0.02D;
                        double d4 = this.rand.nextGaussian() * 0.02D;
                        this.worldObj.spawnParticle(
                            "largesmoke",
                            ((2.0F + this.towerX) + (double) (this.rand.nextFloat() * this.width * 2.0F)) - this.width,
                            (1.0F + this.towerY + this.towerHeight) + (double) (this.rand.nextFloat() * this.height)
                                + 2D,
                            (2D + (this.towerZ + (double) (this.rand.nextFloat() * this.width * 2.0F))) - this.width,
                            d,
                            d2,
                            d4);
                    }
                }
            }

            if (!this.experimentstart) {
                for (int k1 = 0; k1 < 4; k1++) {
                    for (int j3 = 0; j3 < 10; j3++) {
                        double d1 = this.rand.nextGaussian() * 0.02D;
                        double d3 = this.rand.nextGaussian() * 0.02D;
                        double d5 = this.rand.nextGaussian() * 0.02D;
                        this.worldObj.spawnParticle(
                            "largesmoke",
                            (this.towerX + (double) (this.rand.nextFloat() * this.width * 2.0F)) - this.width,
                            (this.towerY + this.towerHeight) + (double) (this.rand.nextFloat() * this.height) + 2D,
                            (this.towerZ + (double) (this.rand.nextFloat() * this.width * 2.0F)) - this.width,
                            d1,
                            d3,
                            d5);
                    }
                }

                CREEPSEntityEvilLight creepsentityevillight1 = new CREEPSEntityEvilLight(this.worldObj);
                creepsentityevillight1.setLocationAndAngles(
                    this.towerX,
                    this.towerY + this.towerHeight + 10,
                    this.towerZ,
                    this.rotationYaw,
                    0.0F);
                this.worldObj.spawnEntityInWorld(creepsentityevillight1);
                this.experimentstart = true;
            }

            if (this.experimenttimer == 0) {
                this.worldObj.createExplosion(
                    null,
                    this.towerX + 2,
                    this.towerY + this.towerHeight + 4,
                    this.towerZ + 2,
                    2.0F,
                    true);
                this.experimentstart = false;
                this.stage = 4;
            }
        }

        if (this.stage == 4) {
            int l1 = MathHelper.floor_double(this.posX);
            int k3 = MathHelper.floor_double(this.posY);
            int l4 = MathHelper.floor_double(this.posZ);

            for (int i5 = 0; i5 < this.rand.nextInt(5) + 1; i5++) {
                this.worldObj.addWeatherEffect(
                    new EntityLightningBolt(
                        this.worldObj,
                        (l1 + this.rand.nextInt(4)) - 2,
                        k3 + 6,
                        (l4 + this.rand.nextInt(4)) - 2));
            }

            this.worldObj.playSoundAtEntity(
                this,
                "morecreeps:evilexperiment",
                1.0F,
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.texture = "morecreeps:textures/entity/evilscientistblown.png";
            this.trulyevil = true;
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
                .setBaseValue(90D);

            for (int j5 = 0; j5 < this.rand.nextInt(4) + 1; j5++) {
                int k5 = this.rand.nextInt(4);

                if (k5 == 0) {
                    for (int l5 = 0; l5 < this.rand.nextInt(8) + 2; l5++) {
                        CREEPSEntityEvilSnowman creepsentityevilsnowman = new CREEPSEntityEvilSnowman(this.worldObj);
                        creepsentityevilsnowman.setLocationAndAngles(
                            this.towerX + this.rand.nextInt(3),
                            this.towerY + this.towerHeight + 1,
                            this.towerZ + this.rand.nextInt(3),
                            this.rotationYaw,
                            0.0F);
                        creepsentityevilsnowman.motionX = this.rand.nextFloat() * 0.3F;
                        creepsentityevilsnowman.motionY = this.rand.nextFloat() * 0.4F;
                        creepsentityevilsnowman.motionZ = this.rand.nextFloat() * 0.4F;
                        creepsentityevilsnowman.fallDistance = -35F;
                        if (!this.worldObj.isRemote) {
                            this.worldObj.spawnEntityInWorld(creepsentityevilsnowman);
                        }
                    }
                }

                if (k5 == 1) {
                    CREEPSEntityEvilScientist creepsentityevilscientist = new CREEPSEntityEvilScientist(this.worldObj);
                    creepsentityevilscientist.setLocationAndAngles(
                        this.towerX,
                        this.towerY + this.towerHeight + 1,
                        this.towerZ,
                        this.rotationYaw,
                        0.0F);
                    creepsentityevilscientist.fallDistance = -35F;
                    if (!this.worldObj.isRemote) {
                        this.worldObj.spawnEntityInWorld(creepsentityevilscientist);
                    }
                }

                if (k5 == 2) {
                    CREEPSEntityEvilPig creepsentityevilpig = new CREEPSEntityEvilPig(this.worldObj);
                    creepsentityevilpig.setLocationAndAngles(
                        this.towerX,
                        this.towerY + this.towerHeight + 1,
                        this.towerZ,
                        this.rotationYaw,
                        0.0F);
                    creepsentityevilpig.fallDistance = -35F;
                    if (!this.worldObj.isRemote) {
                        this.worldObj.spawnEntityInWorld(creepsentityevilpig);
                    }
                }

                if (k5 == 3) {
                    for (int i6 = 0; i6 < this.rand.nextInt(4) + 1; i6++) {
                        CREEPSEntityEvilChicken creepsentityevilchicken = new CREEPSEntityEvilChicken(this.worldObj);
                        creepsentityevilchicken.setLocationAndAngles(
                            this.towerX + this.rand.nextInt(3),
                            this.towerY + this.towerHeight + 1,
                            this.towerZ + this.rand.nextInt(3),
                            this.rotationYaw,
                            0.0F);
                        creepsentityevilchicken.motionX = this.rand.nextFloat() * 0.3F;
                        creepsentityevilchicken.motionY = this.rand.nextFloat() * 0.4F;
                        creepsentityevilchicken.motionZ = this.rand.nextFloat() * 0.4F;
                        creepsentityevilchicken.fallDistance = -35F;
                        if (!this.worldObj.isRemote) {
                            this.worldObj.spawnEntityInWorld(creepsentityevilchicken);
                        }
                    }
                }

                if (k5 != 4) {
                    continue;
                }

                for (int j6 = 0; j6 < this.rand.nextInt(8) + 2; j6++) {
                    CREEPSEntityEvilSnowman creepsentityevilsnowman1 = new CREEPSEntityEvilSnowman(this.worldObj);
                    creepsentityevilsnowman1.setLocationAndAngles(
                        this.towerX + this.rand.nextInt(3),
                        this.towerY + this.towerHeight + 1,
                        this.towerZ + this.rand.nextInt(3),
                        this.rotationYaw,
                        0.0F);
                    creepsentityevilsnowman1.motionX = this.rand.nextFloat() * 0.3F;
                    creepsentityevilsnowman1.motionY = this.rand.nextFloat() * 0.4F;
                    creepsentityevilsnowman1.motionZ = this.rand.nextFloat() * 0.4F;
                    creepsentityevilsnowman1.fallDistance = -35F;
                    if (!this.worldObj.isRemote) {
                        this.worldObj.spawnEntityInWorld(creepsentityevilsnowman1);
                    }
                }
            }

            this.numexperiments--;

            if (this.numexperiments <= 0) {
                this.numexperiments = this.rand.nextInt(4) + 1;
                this.stage = 5;
            } else {
                this.stage = 3;
                this.experimenttimer = this.rand.nextInt(500) + 500;
                this.experimentstart = false;
            }
        }

        if (this.stage == 5) {
            this.tearDownTower();
            this.stage = 6;
        }

        if (this.stage == 6) {
            this.experimenttimer = this.rand.nextInt(5000) + 100;
            this.stage = 0;
        }

        super.onLivingUpdate();
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
     * pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        return true;
    }

    public void tearDownTower() {
        if (this.towerBuilt) {
            for (int i = 0; i < this.towerHeight + 1; i++) {
                this.worldObj.setBlockToAir(this.towerX, this.towerY + i, this.towerZ);

                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 4; l++) {
                            for (int i1 = 0; i1 < 10; i1++) {
                                double d = this.rand.nextGaussian() * 0.02D;
                                double d1 = this.rand.nextGaussian() * 0.02D;
                                double d2 = this.rand.nextGaussian() * 0.02D;
                                this.worldObj.spawnParticle(
                                    "largesmoke",
                                    ((double) (2.0F + this.towerX)
                                        + (double) (this.rand.nextFloat() * this.width * 2.0F)) - this.width,
                                    (double) (1.0F + this.towerY + i) + (double) (this.rand.nextFloat() * this.height)
                                        + 2D,
                                    (2D + ((double) this.towerZ + (double) (this.rand.nextFloat() * this.width * 2.0F)))
                                        - this.width,
                                    d,
                                    d1,
                                    d2);
                            }
                        }

                        this.worldObj.setBlockToAir(this.towerX + k, this.towerY + i, this.towerZ + j + 1);
                        this.worldObj.setBlockToAir(this.towerX + j + 1, this.towerY + i, this.towerZ + k);
                    }
                }
            }
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.trulyevil) {
            double d = entity.posX - this.posX;
            double d1 = entity.posZ - this.posZ;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
            this.motionX = (d / f1) * 0.40000000000000002D * 0.20000000192092895D + this.motionX * 0.18000000098023225D;
            this.motionZ = (d1 / f1) * 0.40000000000000002D * 0.14000000192092896D
                + this.motionZ * 0.18000000098023225D;

            if (this.onGround) {
                this.motionY = 0.44000000196046446D;
            }

            if (f < 2D - (1.0D - this.modelsize) && entity.boundingBox.maxY > this.boundingBox.minY
                && entity.boundingBox.minY < this.boundingBox.maxY) {
                this.attackTime = 10;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1.0F);
            }
        }
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();

        if (entity != null) {
            double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
            double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
            this.motionX = d * 4D;
            this.motionZ = d1 * 4D;
        }

        if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
            if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

            if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                this.setRevengeTarget((EntityLivingBase) entity);
            }

            return true;
        } else return false;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
     * attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        if (this.trulyevil) {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16D);

            if (entityplayer != null && this.canEntityBeSeen(entityplayer)) return entityplayer;
            else return null;
        } else return null;
    }

    class AITargetingSystem extends EntityAIBase {

        public CREEPSEntityEvilScientist evilscientist = CREEPSEntityEvilScientist.this;
        public int attackTime;

        public AITargetingSystem() {}

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.evilscientist.getAttackTarget();
            return CREEPSEntityEvilScientist.this.trulyevil && entitylivingbase != null
                && entitylivingbase.isEntityAlive();
        }

        @Override
        public void updateTask() {
            try {
                --this.attackTime;
                EntityLivingBase entitylivingbase = this.evilscientist.getAttackTarget();
                double d0 = this.evilscientist.getDistanceSqToEntity(entitylivingbase);
                this.evilscientist.attackEntity(entitylivingbase, (float) d0);
                if (d0 < 4.0D) {
                    if (this.attackTime <= 0) {
                        this.attackTime = 20;
                        this.evilscientist.attackEntityAsMob(entitylivingbase);
                    }

                    this.evilscientist.getMoveHelper()
                        .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
                } else if (d0 < 256.0D) {
                    // ATTACK ENTITY GOES HERE
                    this.evilscientist.getLookHelper()
                        .setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
                } else {
                    this.evilscientist.getNavigator()
                        .clearPathEntity();
                    this.evilscientist.getMoveHelper()
                        .setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 0.5D);
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
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
        int j1 = this.worldObj.countEntities(CREEPSEntityEvilScientist.class);
        return i1 != Blocks.cobblestone && i1 != Blocks.log
            && i1 != Blocks.stone_slab
            && i1 != Blocks.double_stone_slab
            && i1 != Blocks.planks
            && i1 != Blocks.wool
            && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
            && this.worldObj.canBlockSeeTheSky(i, j, k)
            && this.rand.nextInt(45) == 0
            && l > 10
            && j1 < 3;
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
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
        if (this.stage < 3) return "morecreeps:evillaugh";
        else return "morecreeps:evilexplosion";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:evilhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:evilexperiment";
    }

    private void smoke() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                double d = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.worldObj.spawnParticle(
                    "EXPLODE".toLowerCase(),
                    (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                    this.posY + this.rand.nextFloat() * this.height + i,
                    (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                    d,
                    d1,
                    d2);
            }
        }
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        this.tearDownTower();

        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(5) == 0) {
                this.dropItem(Items.cooked_porkchop, this.rand.nextInt(3) + 1);
            } else {
                this.dropItem(Item.getItemFromBlock(Blocks.sand), this.rand.nextInt(3) + 1);
            }
        }
        super.onDeath(damagesource);
    }

    /** Will get destroyed next tick. */
    @Override
    public void setDead() {
        this.tearDownTower();
        super.setDead();
    }
}
