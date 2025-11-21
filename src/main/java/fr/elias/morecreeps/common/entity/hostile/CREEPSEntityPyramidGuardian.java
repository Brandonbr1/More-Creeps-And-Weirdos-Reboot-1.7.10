package fr.elias.morecreeps.common.entity.hostile;

import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;

public class CREEPSEntityPyramidGuardian extends EntityMob {

    public static Random rand = new Random();
    public int rows;
    public int columns;
    public int maze[][];
    public static int backgroundCode;
    public static int wallCode;
    public static int pathCode;
    public static int emptyCode;
    public static int visitedCode;
    public TileEntityChest chest;
    public int alternate;
    public boolean found;
    public int bedrockcounter;
    public String texture;

    public CREEPSEntityPyramidGuardian(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/pyramidguardian.png";
        this.found = false;
        this.rotationYaw = 0.0F;
        this.setSize(0.4F, 0.4F);
        this.alternate = 1;
        this.bedrockcounter = 0;
        this.getNavigator()
            .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.4D, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(15D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
            .setBaseValue(0.0D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
            .setBaseValue(1D);
    }

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Found", this.found);
        nbttagcompound.setInteger("BedrockCounter", this.bedrockcounter);
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.found = nbttagcompound.getBoolean("Found");
        this.bedrockcounter = nbttagcompound.getInteger("BedrockCounter");
    }

    /**
     * Called frequently so the entity can update its every tick as required. For example, zombies and
     * skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        super.onLivingUpdate();
    }

    /** Called when the entity is attacked. */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();

        if (entity != null && (entity instanceof EntityPlayer))
            return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
        else return false;
    }

    /** Called when the mob's health reaches 0. */
    @Override
    public void onDeath(DamageSource damagesource) {
        if (damagesource.getEntity() != null && damagesource.getEntity() instanceof EntityPlayer) {
            EntityPlayerMP playerMPAchievement = (EntityPlayerMP) damagesource.getEntity();
            if (!playerMPAchievement.func_147099_x()
                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievepyramid)) {
                this.worldObj.playSoundAtEntity(playerMPAchievement, "morecreeps:achievement", 1.0F, 1.0F);
                playerMPAchievement.addStat(MoreCreepsAndWeirdos.achievepyramid, 1);
                this.confetti(playerMPAchievement);
            }
            if (!this.worldObj.isRemote) {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.posY);
                int k = MathHelper.floor_double(this.posZ);

                if (this.posY > 60D) {
                    for (int l = -4; l < 6; l++) {
                        for (int k2 = -40; k2 < 40; k2++) {
                            for (int j3 = -40; j3 < 40; j3++) {
                                if (this.worldObj.getBlock(i + k2, j + l, k + j3) == Blocks.bedrock) {
                                    this.bedrockcounter++;
                                    this.worldObj.setBlock(i + k2, j + l, k + j3, Blocks.sandstone);
                                }
                            }
                        }
                    }
                }

                if (this.bedrockcounter > 50) {
                    for (int i1 = 3; i1 < 11; i1++) {
                        for (int l2 = 9; l2 < 24; l2++) {
                            for (int k3 = 9; k3 < 25; k3++) {
                                this.worldObj.setBlockToAir(i - l2, j + i1, k - k3);
                            }
                        }
                    }

                    this.worldObj.setBlock(i - 2, j, k - 2, Blocks.sandstone);
                    this.worldObj.setBlockToAir(i - 2, j + 1, k - 1);
                    this.worldObj.setBlockToAir(i - 2, j + 1, k - 2);
                    this.worldObj.setBlockToAir(i - 2, j + 2, k - 1);
                    this.worldObj.setBlockToAir(i - 2, j + 2, k - 2);
                    this.worldObj.setBlock(i - 2, j + 1, k - 3, Blocks.sandstone);
                    this.worldObj.setBlockToAir(i - 2, j + 2, k - 3);
                    this.worldObj.setBlock(i - 2, j + 2, k - 4, Blocks.sandstone);
                    this.worldObj.setBlockToAir(i - 2, j + 3, k - 4);

                    for (int j1 = 2; j1 < 18; j1++) {
                        this.worldObj.setBlockToAir(i - 2, j + 3, k - j1);
                        this.worldObj.setBlockToAir(i - 2, j + 4, k - j1);
                        this.alternate *= -1;

                        if (this.alternate > 0) {
                            this.worldObj.setBlock(i - 2, j + 4, k - j1, Blocks.torch);
                        }
                    }

                    for (int k1 = 2; k1 < 20; k1++) {
                        this.worldObj.setBlockToAir(i - k1, j + 3, k - 17);
                        this.worldObj.setBlockToAir(i - k1, j + 4, k - 17);
                    }

                    for (int l1 = 9; l1 < 24; l1++) {
                        this.alternate *= -1;

                        if (this.alternate > 0) {
                            this.worldObj.setBlock(i - 8, j + 8, k - l1, Blocks.torch);
                            this.worldObj.setBlock(i - 24, j + 8, k - l1, Blocks.torch);
                        }

                        this.worldObj.setBlock(i - l1, j + 8, k - 9, Blocks.torch);
                        this.worldObj.setBlock(i - l1, j + 8, k - 24, Blocks.torch);
                    }

                    for (int i2 = 0; i2 < rand.nextInt(2) + 2; i2++) {
                        CREEPSEntityEvilCreature creepsentityevilcreature = new CREEPSEntityEvilCreature(this.worldObj);
                        creepsentityevilcreature
                            .setLocationAndAngles(i - 15, j + 8, k - 10 - i2, this.rotationYaw, 0.0F);
                        this.worldObj.spawnEntityInWorld(creepsentityevilcreature);
                    }

                    for (int j2 = 0; j2 < rand.nextInt(7) + 2; j2++) {
                        CREEPSEntityMummy creepsentitymummy = new CREEPSEntityMummy(this.worldObj);
                        creepsentitymummy.setLocationAndAngles(i - 15, j + 8, k - 13 - j2, this.rotationYaw, 0.0F);
                        this.worldObj.spawnEntityInWorld(creepsentitymummy);
                    }

                    this.worldObj.setBlock(i - 14, j + 3, k - 15, Blocks.glowstone);
                    this.worldObj.setBlock(i - 16, j + 3, k - 15, Blocks.glowstone);
                    BlockBed blockbed = (BlockBed) Blocks.bed;
                    int i3 = 0;
                    int l3 = 1;
                    this.worldObj.setBlock(i - 15, j + 3, k - 15, blockbed);
                    this.worldObj.setBlock((i - 15) + i3, j + 3, (k + l3) - 15, blockbed);
                }
            }
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a
     * pig.
     */
    @Override
    public boolean interact(EntityPlayer entityplayer) {
        this.worldObj.playSoundAtEntity(
            this,
            "morecreeps:pyramidcurse",
            1.0F,
            (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
        return true;
    }

    public void makeMaze() {
        int l1 = 0;
        int i2 = 0;
        int ai[] = new int[(this.rows * this.columns) / 2];
        int ai1[] = new int[(this.rows * this.columns) / 2];

        for (int i = 0; i < this.rows; i++) {
            for (int i1 = 0; i1 < this.columns; i1++) {
                this.maze[i][i1] = wallCode;
            }
        }

        for (int j = 1; j < this.rows - 1; j += 2) {
            for (int j1 = 1; j1 < this.columns - 1; j1 += 2) {
                l1++;
                this.maze[j][j1] = -l1;

                if (j < this.rows - 2) {
                    ai[i2] = j + 1;
                    ai1[i2] = j1;
                    i2++;
                }

                if (j1 < this.columns - 2) {
                    ai[i2] = j;
                    ai1[i2] = j1 + 1;
                    i2++;
                }
            }
        }

        for (int k = i2 - 1; k > 0; k--) {
            int j2 = (int) (Math.random() * k);

            if (ai[j2] % 2 == 1 && this.maze[ai[j2]][ai1[j2] - 1] != this.maze[ai[j2]][ai1[j2] + 1]) {
                this.fill(ai[j2], ai1[j2] - 1, this.maze[ai[j2]][ai1[j2] - 1], this.maze[ai[j2]][ai1[j2] + 1]);
                this.maze[ai[j2]][ai1[j2]] = this.maze[ai[j2]][ai1[j2] + 1];
            } else if (ai[j2] % 2 == 0 && this.maze[ai[j2] - 1][ai1[j2]] != this.maze[ai[j2] + 1][ai1[j2]]) {
                this.fill(ai[j2] - 1, ai1[j2], this.maze[ai[j2] - 1][ai1[j2]], this.maze[ai[j2] + 1][ai1[j2]]);
                this.maze[ai[j2]][ai1[j2]] = this.maze[ai[j2] + 1][ai1[j2]];
            }

            ai[j2] = ai[k];
            ai1[j2] = ai1[k];
        }

        for (int l = 1; l < this.rows - 1; l++) {
            for (int k1 = 1; k1 < this.columns - 1; k1++) {
                if (this.maze[l][k1] < 0) {
                    this.maze[l][k1] = emptyCode;
                }
            }
        }
    }

    public void tearDown(int i, int j) {
        if (i % 2 == 1 && this.maze[i][j - 1] != this.maze[i][j + 1]) {
            this.fill(i, j - 1, this.maze[i][j - 1], this.maze[i][j + 1]);
            this.maze[i][j] = this.maze[i][j + 1];
        } else if (i % 2 == 0 && this.maze[i - 1][j] != this.maze[i + 1][j]) {
            this.fill(i - 1, j, this.maze[i - 1][j], this.maze[i + 1][j]);
            this.maze[i][j] = this.maze[i + 1][j];
        }
    }

    public void fill(int i, int j, int k, int l) {
        if (i < 0) {
            i = 0;
        }

        if (j < 0) {
            i = 0;
        }

        if (i > this.rows) {
            i = this.rows;
        }

        if (j > this.columns) {
            j = this.columns;
        }

        if (this.maze[i][j] == k) {
            this.maze[i][j] = l;
            this.fill(i + 1, j, k, l);
            this.fill(i - 1, j, k, l);
            this.fill(i, j + 1, k, l);
            this.fill(i, j - 1, k, l);
        }
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
        this.worldObj.spawnEntityInWorld(creepsentitytrophy);
    }

    /** Returns the sound this mob makes while it's alive. */
    @Override
    protected String getLivingSound() {
        return "morecreeps:pyramid";
    }

    /** Returns the sound this mob makes when it is hurt. */
    @Override
    protected String getHurtSound() {
        return "morecreeps:pyramidhurt";
    }

    /** Returns the sound this mob makes on death. */
    @Override
    protected String getDeathSound() {
        return "morecreeps:pyramiddeath";
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }
}
