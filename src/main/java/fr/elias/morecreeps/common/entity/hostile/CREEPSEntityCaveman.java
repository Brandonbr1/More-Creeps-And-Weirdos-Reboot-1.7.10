package fr.elias.morecreeps.common.entity.hostile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityRocket;

public class CREEPSEntityCaveman extends EntityMob {

    protected double attackrange;
    protected int attack;

    public int eaten;
    public boolean hungry;
    public int hungrytime;
    public float hammerswing;
    public int frozen;
    public float modelsize;
    public float fat;
    public boolean cavegirl;
    public boolean evil;
    public float modelsizebase;
    public int wanderstate;
    public int houseX;
    public int houseY;
    public int houseZ;
    public int housechunk;
    public int area;
    public int talkdelay;

    public String texture;
    public int attackTime;

    public CREEPSEntityCaveman(World world) {
        super(world);
        this.scoreValue = 4;
        this.attack = 1;
        this.attackrange = 16D;
        this.hammerswing = 0.0F;
        this.hungry = false;
        this.hungrytime = this.rand.nextInt(100) + 10;

        if (this.rand.nextInt(100) > 50) {
            this.cavegirl = true;
        }

        this.evil = false;
        this.wanderstate = 0;
        this.frozen = 5;
        this.fat = this.rand.nextFloat() * 1.0F - this.rand.nextFloat() * 0.55F;
        this.modelsize = (1.25F + this.rand.nextFloat() * 1.0F) - this.rand.nextFloat() * 0.75F;
        this.modelsizebase = this.modelsize;
        this.setSize(this.width * 0.8F + this.fat, this.height * 1.3F + this.fat);
        this.setCaveTexture();
        this.targetTasks.addTask(0, new CREEPSEntityCaveman.AIFindPlayerToAttack());
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(25D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.45D);
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.frozen > 0 && this.worldObj.getBlock(
            MathHelper.floor_double(this.posX),
            MathHelper.floor_double(this.posY),
            MathHelper.floor_double(this.posZ)) == Blocks.air) {
            this.posY--;
        }

        if (this.isWet()) {
            this.frozen = 0;
        }
        // double moveSpeed = this.frozen >= 1 ? 0.0F : 0.45F; // TODO (unused)

        if (this.wanderstate == 0 && this.frozen < 1
            && !this.evil
            && !MoreCreepsAndWeirdos.cavemanbuilding
            && this.rand.nextInt(100) == 0) {
            this.wanderstate = 1;
        }

        if (this.wanderstate == 1 && this.rand.nextInt(201) == 200
            && !MoreCreepsAndWeirdos.cavemanbuilding
            && !this.evil
            && this.checkArea()) {
            this.wanderstate = 2;
            this.housechunk = 0;

            for (int i = 0; i < 4; i++) {
                if (this.worldObj.getBlock(this.houseX, this.houseY, this.houseZ) == Blocks.air
                    || this.worldObj.getBlock(this.houseX + 1, this.houseY, this.houseZ) == Blocks.air
                    || this.worldObj.getBlock(this.houseX + 2, this.houseY, this.houseZ + 4) == Blocks.air
                    || this.worldObj.getBlock(this.houseX, this.houseY, this.houseZ + 2) == Blocks.air) {
                    this.houseY--;
                }
            }

            MoreCreepsAndWeirdos.cavemanbuilding = true;
        }

        if (this.wanderstate == 2) {
            this.posX = this.houseX - 1;
            // moveSpeed = 0.0F; TODO
            this.setRotation(45F, this.rotationPitch);
        }

        if (this.wanderstate == 2 && this.rand.nextInt(50) == 0) {
            if (this.housechunk == 0) {
                this.hammerswing = -2.8F;
                this.worldObj.setBlock(this.houseX + 1, this.houseY, this.houseZ, Blocks.snow);
                this.housechunk++;
                this.snowFX(this.houseX + 1, this.houseY, this.houseZ);
            } else if (this.housechunk == 1) {
                this.hammerswing = -2.8F;
                this.worldObj.setBlock(this.houseX + 1, this.houseY + 1, this.houseZ, Blocks.snow);
                this.housechunk++;
                this.snowFX(this.houseX + 1, this.houseY + 1, this.houseZ);
            } else if (this.housechunk == 2) {
                this.hammerswing = -2.8F;
                this.worldObj.setBlock(this.houseX + 3, this.houseY, this.houseZ, Blocks.snow);
                this.snowFX(this.houseX + 3, this.houseY, this.houseZ);
                this.housechunk++;
            } else if (this.housechunk == 3) {
                this.hammerswing = -2.8F;
                this.worldObj.setBlock(this.houseX + 3, this.houseY + 1, this.houseZ, Blocks.snow);
                this.snowFX(this.houseX + 3, this.houseY + 1, this.houseZ);
                this.housechunk++;
            } else if (this.housechunk == 4) {
                this.hammerswing = -2.8F;

                for (int j = 1; j < 4; j++) {
                    this.worldObj.setBlock(this.houseX, this.houseY, this.houseZ + j, Blocks.snow);
                    this.snowFX(this.houseX, this.houseY, this.houseZ + j);
                }

                this.housechunk++;
            } else if (this.housechunk == 5) {
                this.hammerswing = -2.8F;

                for (int k = 1; k < 4; k++) {
                    this.worldObj.setBlock(this.houseX, this.houseY + 1, this.houseZ + k, Blocks.snow);
                    this.snowFX(this.houseX, this.houseY + 1, this.houseZ + k);
                }

                this.housechunk++;
            } else if (this.housechunk == 6) {
                this.hammerswing = -2.8F;

                for (int l = 1; l < 4; l++) {
                    this.worldObj.setBlock(this.houseX + 4, this.houseY, this.houseZ + l, Blocks.snow);
                    this.snowFX(this.houseX + 4, this.houseY, this.houseZ + l);
                }

                this.housechunk++;
            } else if (this.housechunk == 7) {
                this.hammerswing = -2.8F;

                for (int i1 = 1; i1 < 4; i1++) {
                    this.worldObj.setBlock(this.houseX + 4, this.houseY + 1, this.houseZ + i1, Blocks.snow);
                    this.snowFX(this.houseX + 4, this.houseY + 1, this.houseZ + i1);
                }

                this.housechunk++;
            } else if (this.housechunk == 8) {
                this.hammerswing = -2.8F;

                for (int j1 = 1; j1 < 4; j1++) {
                    this.worldObj.setBlock(this.houseX + j1, this.houseY, this.houseZ + 4, Blocks.snow);
                    this.snowFX(this.houseX + j1, this.houseY, this.houseZ + 4);
                }

                this.housechunk++;
            } else if (this.housechunk == 9) {
                this.hammerswing = -2.8F;

                for (int k1 = 1; k1 < 4; k1++) {
                    this.worldObj.setBlock(this.houseX + k1, this.houseY + 1, this.houseZ + 4, Blocks.snow);
                    this.snowFX(this.houseX + k1, this.houseY + 1, this.houseZ + 4);
                }

                this.housechunk++;
            } else if (this.housechunk == 10) {
                this.hammerswing = -2.8F;

                for (int l1 = 1; l1 < 4; l1++) {
                    for (int j2 = 1; j2 < 4; j2++) {
                        this.worldObj.setBlock(this.houseX + j2, this.houseY + 2, this.houseZ + l1, Blocks.snow);
                        this.snowFX(this.houseX + j2, this.houseY + 2, this.houseZ + l1);
                    }
                }

                this.housechunk++;
            } else if (this.housechunk == 11) {
                this.hammerswing = -2.8F;
                this.worldObj.setBlock(this.houseX + 2, this.houseY + 3, this.houseZ + 2, Blocks.snow);
                this.snowFX(this.houseX + 2, this.houseY + 3, this.houseZ + 2);
                this.housechunk++;
            } else if (this.housechunk == 12) {
                Item i2;

                if (this.rand.nextInt(5) == 0) {
                    i2 = Items.fish;
                } else {
                    i2 = MoreCreepsAndWeirdos.popsicle;
                }

                if (!this.worldObj.isRemote) {
                    EntityItem entityitem = new EntityItem(
                        this.worldObj,
                        this.houseX + 3,
                        this.houseY,
                        this.houseZ + 3,
                        new ItemStack(i2, this.rand.nextInt(4) + 1, 0));
                    this.worldObj.spawnEntityInWorld(entityitem);
                }
                // moveSpeed = maxspeed;
                MoreCreepsAndWeirdos.cavemanbuilding = false;
                this.wanderstate = 3;
            }
        }

        if (this.hammerswing < 0.0F) {
            this.hammerswing += 0.4F;
        } else {
            this.hammerswing = 0.0F;
        }

        // EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        // taken from findPlayerToAttack() method.
        EntityPlayer entityplayer = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0D);

        if (entityplayer != null) {
            float f = this.getDistanceToEntity(entityplayer);
            this.ignoreFrustumCheck = f < 16F;
        }
    }

    @Override
    public boolean isMovementBlocked() {
        return this.frozen >= 1 || this.wanderstate == 2 ? true : false;
    }

    public boolean checkArea() {
        this.houseX = MathHelper.floor_double(this.posX);
        this.houseY = MathHelper.floor_double(this.posY);
        this.houseZ = MathHelper.floor_double(this.posZ);

        if (this.worldObj.getBlock(this.houseX, this.houseY - 1, this.houseZ) == Blocks.air) {
            this.houseY--;
        }

        this.area = 0;

        for (int i = -3; i < 7; i++) {
            for (int k = -3; k < 7; k++) {
                for (int i1 = 0; i1 < 3; i1++) {
                    if (this.worldObj.getBlock(this.houseX + k, this.houseY + i1, this.houseZ + i) == Blocks.air) {
                        this.area++;
                    }
                }
            }
        }

        if (this.area < 220) return false;

        for (int j = -2; j < 7; j++) {
            for (int l = -2; l < 7; l++) {
                Block j1 = this.worldObj.getBlock(this.houseX + l, this.houseY, this.houseZ + j);
                Block k1 = this.worldObj.getBlock(this.houseX + l, this.houseY - 1, this.houseZ + j);

                if (j1 == Blocks.snow || j1 == Blocks.ice) {
                    this.area++;
                }

                if (k1 == Blocks.snow || k1 == Blocks.ice) {
                    this.area++;
                }
            }
        }

        return this.area > 75;
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in
     * attacking (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        if (!this.evil || this.frozen > 0) return null;
        else return super.findPlayerToAttack();
    }

    class AIFindPlayerToAttack extends EntityAINearestAttackableTarget {

        public AIFindPlayerToAttack() {
            super(CREEPSEntityCaveman.this, EntityPlayer.class, 0, true);
        }

        @Override
        public void updateTask() {
            try {
                EntityLivingBase target = CREEPSEntityCaveman.this.getAttackTarget();
                float f = CREEPSEntityCaveman.this.getDistanceToEntity(target);
                CREEPSEntityCaveman.this.attackEntity(target, f);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public boolean shouldExecute() {
            return !CREEPSEntityCaveman.this.evil || CREEPSEntityCaveman.this.frozen > 0 && super.shouldExecute();
        }
    }

    /** knocks back this entity */
    public void knockBack(Entity entity, int i, double d, double d1) {
        if (this.frozen < 1) {
            super.knockBack(entity, i, d, d1);
        }
    }

    @Override
    public void updateRiderPosition() {
        this.riddenByEntity
            .setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
    }

    /** Returns the Y offset from the entity's position for any entity riding this one. */
    @Override
    public double getMountedYOffset() {
        return 0.5D;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example,
     * zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        if (this.frozen < 1) {
            super.onLivingUpdate();
        }

        if (this.handleWaterMovement()) {
            this.frozen = 0;
        }

        if (this.isWet()) {
            this.frozen = 0;
        }
    }

    /**
     * Takes a coordinate in and returns a weight to determine how likely this creature will try to
     * path to the block. Args: x, y, z
     */
    @Override
    public float getBlockPathWeight(int x, int y, int z) {
        if (this.worldObj.getBlock(x, y, z) == Blocks.gravel || this.worldObj.getBlock(x, y, z) == Blocks.stone)
            return 10F;
        else return -(float) y;
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();
        this.hungry = false;

        if (this.frozen < 1) {
            this.evil = true;
            this.setCaveTexture();

            if (super.attackEntityFrom(DamageSource.causeMobDamage(this), i)) {
                if (this.riddenByEntity == entity || this.ridingEntity == entity) return true;

                if (entity != this && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL) {
                    this.setRevengeTarget((EntityLivingBase) entity);
                }

                return true;
            } else return false;
        }

        if (entity instanceof EntityPlayer) {
            this.worldObj.playSoundAtEntity(
                this,
                "morecreeps:cavemanice",
                0.5F,
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

            if (this.rand.nextInt(100) > 65) {
                this.frozen--;
            }

            for (int j = 0; j < 35; j++) {
                this.worldObj
                    .spawnParticle("SNOWBALL".toLowerCase(), this.posX, this.posY + 1.0D, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }

        if (this.frozen > 0) {
            this.hurtTime = 0;
        }

        return false;
    }

    public void snowFX(int i, int j, int k) {
        for (int l = 0; l < 40; l++) {
            this.worldObj.spawnParticle("SNOWBALLPOOF".toLowerCase(), i, j + 0.5D, k, 1.0D, 1.0D, 1.0D);
        }
        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.foam3(this.worldObj, this, i, j, k);
        }
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define
     * their attack.
     */
    @Override
    protected void attackEntity(Entity entity, float f) {
        if (this.frozen < 1) {
            if (this.onGround) {
                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                this.motionX = (d / f1) * 0.20000000000000001D
                    * (0.45000001192092898D + this.motionX * 0.20000000298023224D);
                this.motionZ = (d1 / f1) * 0.20000000000000001D
                    * (0.40000001192092893D + this.motionZ * 0.20000000298023224D);
                this.motionY = 0.46000000596246449D;
                this.fallDistance = -25F;
            }

            if (f < 2.8999999999999999D && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                if (this.hammerswing == 0.0F) {
                    this.hammerswing = -2.8F;
                }

                if (this.talkdelay-- < 0) {
                    this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:cavemanevil",
                        0.5F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                    this.talkdelay = 2;
                }
            }

            if (f < 2.3500000000000001D && entity.getBoundingBox().maxY > this.getBoundingBox().minY
                && entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                this.attackTime = 20;
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), this.attack);
            }
        }
    }

    /** Checks if the entity's current position is a valid location to spawn this entity. */
    @Override
    public boolean getCanSpawnHere() {
        if (this.worldObj == null || this.getBoundingBox() == null) return false;
        if (this.getBoundingBox() == null) return false;
        if (this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL) return false;
        return super.getCanSpawnHere();
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Hungry", this.hungry);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
        nbttagcompound.setFloat("ModelSizeBase", this.modelsizebase);
        nbttagcompound.setFloat("Fat", this.fat);
        nbttagcompound.setInteger("Frozen", this.frozen);
        nbttagcompound.setBoolean("Cavegirl", this.cavegirl);
        nbttagcompound.setBoolean("Evil", this.evil);
        nbttagcompound.setInteger("WanderState", this.wanderstate);
        nbttagcompound.setInteger("HouseX", this.houseX);
        nbttagcompound.setInteger("HouseY", this.houseY);
        nbttagcompound.setInteger("HouseZ", this.houseZ);
        nbttagcompound.setInteger("HouseChunk", this.housechunk);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.hungry = nbttagcompound.getBoolean("Hungry");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
        this.modelsizebase = nbttagcompound.getFloat("ModelSizeBase");
        this.fat = nbttagcompound.getFloat("Fat");
        this.frozen = nbttagcompound.getInteger("Frozen");
        this.cavegirl = nbttagcompound.getBoolean("Cavegirl");
        this.evil = nbttagcompound.getBoolean("Evil");
        this.wanderstate = nbttagcompound.getInteger("WanderState");
        this.houseX = nbttagcompound.getInteger("HouseX");
        this.houseY = nbttagcompound.getInteger("HouseY");
        this.houseZ = nbttagcompound.getInteger("HouseZ");
        this.housechunk = nbttagcompound.getInteger("HouseChunk");

        if (this.wanderstate == 2) {
            MoreCreepsAndWeirdos.cavemanbuilding = true;
        }

        this.setCaveTexture();
    }

    public void setCaveTexture() {
        if (this.evil) {
            if (this.cavegirl) {
                this.texture = "morecreeps:textures/entity/cavemanladyevil.png";
            } else {
                this.texture = "morecreeps:textures/entity/cavemanevil.png";
            }
        } else if (this.cavegirl) {
            this.texture = "morecreeps:textures/entity/cavemanlady.png";
        } else {
            this.texture = "morecreeps:textures/entity/caveman.png";
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
                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F
                    + (1.0F - (this.modelsizebase - this.modelsize) * 2.0F));
        }
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        if (this.evil) return "morecreeps:cavemanevil";

        EntityLivingBase entityToAttack = this.getAttackTarget();

        if (entityToAttack != null) {
            if (this.cavegirl) return "morecreeps:cavewomanfree";
            else return "morecreeps:cavemanfree";
        }

        if (this.cavegirl) {
            if (this.frozen < 1) return "morecreeps:cavewomanfree";
            else return "morecreeps:cavewomanfrozen";
        }

        if (this.frozen < 1) return "morecreeps:cavemanfree";
        else return "morecreeps:cavemanfrozen";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        if (this.frozen > 0) return null;

        if (this.cavegirl) return "morecreeps:cavewomanhurt";
        else return "morecreeps:cavemanhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        if (this.cavegirl) return "morecreeps:cavewomandead";
        else return "morecreeps:cavemandead";
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        Object obj = damagesource.getEntity();

        if ((obj instanceof CREEPSEntityRocket) && ((CREEPSEntityRocket) obj).owner != null) {
            obj = ((CREEPSEntityRocket) obj).owner;
        }
        if (!(obj instanceof EntityPlayer)) return;

        EntityPlayer player = (EntityPlayer) obj;
        if (player != null) {
            MoreCreepsAndWeirdos.cavemancount++;
            if (!((EntityPlayerMP) player).func_147099_x()
                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve1caveman)) {
                this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsAndWeirdos.achieve1caveman, 1);
                this.confetti();
            }

            if (!((EntityPlayerMP) player).func_147099_x()
                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve1caveman)
                && MoreCreepsAndWeirdos.cavemancount >= 10) {
                this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsAndWeirdos.achieve10caveman, 1);
                this.confetti();
            }

            if (!((EntityPlayerMP) player).func_147099_x()
                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve10caveman)
                && MoreCreepsAndWeirdos.cavemancount >= 50) {
                this.worldObj.playSoundAtEntity(player, "morecreeps:achievement", 1.0F, 1.0F);
                player.addStat(MoreCreepsAndWeirdos.achieve50caveman, 1);
                this.confetti();
            }
        }
        if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(10) == 0) {
                this.dropItem(Items.porkchop, this.rand.nextInt(3) + 1);
            }

            if (this.rand.nextInt(10) == 0) {
                this.dropItem(MoreCreepsAndWeirdos.popsicle, this.rand.nextInt(3) + 1);
            }

            if (this.rand.nextInt(8) == 0) {
                this.dropItem(MoreCreepsAndWeirdos.cavemanclub, 1);
            }
        }

        super.onDeath(damagesource);
    }

    public void confetti() {
        MoreCreepsAndWeirdos.proxy.confettiA(this, this.worldObj);
    }

    /** Get number of ticks, at least during which the living entity will be silent. */
    @Override
    public int getTalkInterval() {
        return !this.evil ? 180 : 120;
    }

    /** Will return how many at most can spawn in a chunk at once. */
    @Override
    public int getMaxSpawnedInChunk() {
        return 2;
    }
}
