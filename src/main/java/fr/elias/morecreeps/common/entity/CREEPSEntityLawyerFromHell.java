package fr.elias.morecreeps.common.entity;

import java.util.List;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.port.EnumParticleTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class CREEPSEntityLawyerFromHell extends EntityMob {

    private boolean foundplayer;
    private boolean stolen;
    private PathEntity pathToEntity;

    /**
     * returns true if a creature has attacked recently only used for creepers and skeletons
     */
    protected boolean hasAttacked;
    protected ItemStack stolengood;
    private double goX;
    private double goZ;
    private float distance;
    public int itemnumber;
    public int stolenamount;
    public boolean undead;
    public int jailX;
    public int jailY;
    public int jailZ;
    public int area;
    public int lawyerstate;
    public int lawyertimer;
    private static ItemStack defaultHeldItem;
    public float modelsize;
    public int maxObstruct;
    public String texture;

    public CREEPSEntityLawyerFromHell(World world) {
        super(world);
        this.texture = "morecreeps:textures/entity/lawyerfromhell.png";

        if (this.undead) {
            this.texture = "morecreeps:textures/entity/lawyerfromhellundead.png";
        }

        this.stolen = false;
        this.hasAttacked = false;
        this.foundplayer = false;
        this.lawyerstate = 0;
        this.lawyertimer = 0;

        if (!this.undead) {
            defaultHeldItem = null;
        }

        this.modelsize = 1.0F;
        this.maxObstruct = 20;

        this.getNavigator()
        .setBreakDoors(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new CREEPSEntityLawyerFromHell.AIAttackEntity());
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 0.5D));
        this.tasks.addTask(3, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.44D);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1D);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    @Override
    protected Entity findPlayerToAttack() {
        if (this.lawyerstate == 0 && !this.undead)
            return null;

        if (MoreCreepsAndWeirdos.instance.currentfine <= 0 && !this.undead) {
            this.lawyerstate = 0;
            this.pathToEntity = null;
            return null;
        }

        if (this.lawyerstate > 0) {
            EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 16D);

            if (entityplayer != null && this.canEntityBeSeen(entityplayer))
                return entityplayer;
            else
                return null;
        } else
            return null;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate() {
        this.texture = this.undead ? "morecreeps:textures/entity/lawyerfromhellundead.png"
                : "morecreeps:textures/entity/lawyerfromhell.png";
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(this.undead ? 0.24D : 0.44D);

        if (this.undead && defaultHeldItem == null) {
            defaultHeldItem = new ItemStack(Items.bone, 1);
        }

        super.onLivingUpdate();
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        System.out.println("laywerstate" + this.lawyerstate);
        System.out.println("timer" + this.lawyertimer);
        System.out.println("laywerstate" + this.lawyertimer);
        System.out.println("fine" + MoreCreepsAndWeirdos.instance.currentfine);
        System.out.println("jail built" + MoreCreepsAndWeirdos.instance.jailBuilt);
        if (MoreCreepsAndWeirdos.instance.currentfine > 0 && this.lawyerstate == 0 && !this.undead) {
            this.lawyerstate = 1;
        }

        if (MoreCreepsAndWeirdos.instance.currentfine > 2500 && this.lawyerstate < 5 && !this.undead) {
            this.lawyerstate = 5;
        }

        if (this.undead) {
            this.lawyerstate = 1;
        }

        super.onUpdate();
    }

    /**
     * Checks if this entity is inside of an opaque block
     */
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (this.undead && this.isCollided)
            return false;
        else
            return super.isEntityInsideOpaqueBlock();
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource damagesource, float i) {
        Entity entity = damagesource.getEntity();

        if (!this.undead && (entity instanceof EntityPlayer)
                || (entity instanceof CREEPSEntityGuineaPig) && ((CREEPSEntityGuineaPig) entity).tamed
                || (entity instanceof CREEPSEntityHotdog) && ((CREEPSEntityHotdog) entity).tamed
                || (entity instanceof CREEPSEntityArmyGuy) && ((CREEPSEntityArmyGuy) entity).loyal) {
            MoreCreepsAndWeirdos.instance.currentfine += 50;
        }

        if (!this.undead) {
            if ((entity instanceof EntityPlayer)
                    || (entity instanceof CREEPSEntityHotdog) && ((CREEPSEntityHotdog) entity).tamed
                    || (entity instanceof CREEPSEntityGuineaPig) && ((CREEPSEntityGuineaPig) entity).tamed
                    || (entity instanceof CREEPSEntityArmyGuy) && ((CREEPSEntityArmyGuy) entity).loyal) {
                if (this.lawyerstate == 0) {
                    this.lawyerstate = 1;
                }

                this.setRevengeTarget((EntityLivingBase) entity);
            }

            if (entity instanceof EntityPlayer) {
                if (this.lawyerstate == 0) {
                    this.lawyerstate = 1;
                }

                if (this.rand.nextInt(5) == 0) {
                    for (int j = 0; j < this.rand.nextInt(20) + 5; j++) {
                        MoreCreepsAndWeirdos.instance.currentfine += 25;
                        this.worldObj.playSoundAtEntity(
                                this,
                                "morecreeps:lawyermoneyhit",
                                1.0F,
                                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        if (!this.worldObj.isRemote) {
                            this.dropItem(MoreCreepsAndWeirdos.money, 1);
                        }
                    }
                }

                if (this.rand.nextInt(5) == 0) {
                    for (int k = 0; k < this.rand.nextInt(3) + 1; k++) {
                        MoreCreepsAndWeirdos.instance.currentfine += 10;
                        this.worldObj.playSoundAtEntity(
                                this,
                                "morecreeps:lawyermoneyhit",
                                1.0F,
                                (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                        if (!this.worldObj.isRemote) {
                            this.dropItem(Items.paper, 1);
                        }
                    }
                }
            }
        }

        return super.attackEntityFrom(DamageSource.causeMobDamage(this), i);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    class AIAttackEntity extends EntityAIBase {

        EntityPlayer entityplayer;

        @Override
        public boolean shouldExecute() {
            return (this.entityplayer == CREEPSEntityLawyerFromHell.this.findPlayerToAttack());
        }

        @Override
        public void updateTask() {
            try {

                if (!CREEPSEntityLawyerFromHell.this.undead && CREEPSEntityLawyerFromHell.this.lawyerstate == 0) {
                    CREEPSEntityLawyerFromHell.this.entityToAttack = null;
                    return;
                }

                float f = CREEPSEntityLawyerFromHell.this.getDistanceToEntity(CREEPSEntityLawyerFromHell.this.getAttackTarget());
                if (f < 256F) {
                    CREEPSEntityLawyerFromHell.this.attackEntity(CREEPSEntityLawyerFromHell.this.getAttackTarget(), f);
                    CREEPSEntityLawyerFromHell.this.getLookHelper().setLookPositionWithEntity(CREEPSEntityLawyerFromHell.this.getAttackTarget(), 10.0F, 10.0F);
                    CREEPSEntityLawyerFromHell.this.getNavigator()
                    .clearPathEntity();
                    CREEPSEntityLawyerFromHell.this.getMoveHelper()
                    .setMoveTo(
                            CREEPSEntityLawyerFromHell.this.getAttackTarget().posX,
                            CREEPSEntityLawyerFromHell.this.getAttackTarget().posY,
                            CREEPSEntityLawyerFromHell.this.getAttackTarget().posZ,
                            0.5D);
                }
                if (f < 1F) {
                    CREEPSEntityLawyerFromHell.this.attackEntityAsMob(CREEPSEntityLawyerFromHell.this.getAttackTarget());
                }
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void attackEntity(Entity entity, float f) {
        super.attackEntity(entity, f);


        if (this.getAttackTarget() instanceof EntityPlayer) {
            if (MoreCreepsAndWeirdos.instance.currentfine <= 0 && !this.undead) {
                this.pathToEntity = null;
                return;
            }

            if (this.onGround) {
                float f1 = 1.0F;

                if (this.undead) {
                    f1 = 0.5F;
                }

                double d = entity.posX - this.posX;
                double d1 = entity.posZ - this.posZ;
                float f2 = MathHelper.sqrt_double(d * d + d1 * d1);
                this.motionX = ((d / f2) * 0.5D * 0.40000001192092893D + this.motionX * 0.20000000298023224D)
                        * f1;
                this.motionZ = ((d1 / f2) * 0.5D * 0.30000001192092896D + this.motionZ * 0.20000000298023224D)
                        * f1;
                this.motionY = 0.40000000596046448D;
            } else if (f < 2.6000000000000001D) {
                if (this.rand.nextInt(50) == 0 && (entity instanceof EntityPlayer)) {
                    this.suckMoney((EntityPlayer) entity);
                }

                if (this.undead) {
                    this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
                    .setBaseValue(2D);
                }

                if ((entity instanceof EntityPlayer) && this.lawyerstate == 5
                        && !this.undead
                        && this.rand.nextInt(10) == 0
                        && CREEPSConfig.jailActive
                        && MoreCreepsAndWeirdos.instance.currentfine >= 2500) {
                    for (int i = 0; i < 21; i++) {
                        EntityPlayer entityplayer = (EntityPlayer) entity;
                        Object obj = entityplayer;
                        int k;

                        for (k = 0; ((Entity) obj).riddenByEntity != null && k < 20; k++) {
                            obj = ((Entity) obj).riddenByEntity;
                        }

                        if (k < 20) {
                            ((Entity) obj).fallDistance = -25F;
                            ((Entity) obj).mountEntity(null);
                        }
                    }

                    this.buildJail((EntityPlayer) entity);

                }

                super.attackEntity(entity, f);
            } else if (f < 5D && (entity instanceof EntityPlayer)
                    && this.rand.nextInt(25) == 0
                    && !this.undead
                    && CREEPSConfig.jailActive
                    && this.lawyerstate == 5
                    && MoreCreepsAndWeirdos.instance.currentfine >= 2500) {
                for (int j = 0; j < 21; j++) {
                    EntityPlayer entityplayer1 = (EntityPlayer) entity;
                    Object obj1 = entityplayer1;
                    int l;

                    for (l = 0; ((Entity) obj1).riddenByEntity != null && l < 20; l++) {
                        obj1 = ((Entity) obj1).riddenByEntity;
                    }

                    if (l < 20) {
                        ((Entity) obj1).fallDistance = -25F;
                        ((Entity) obj1).mountEntity(null);
                    }
                }
                // test
                super.attackEntity(entity, f);

                MoreCreepsAndWeirdos.proxy.addChatMessage(" ");
                MoreCreepsAndWeirdos.proxy.addChatMessage("\2474  BUSTED! \2476Sending guilty player to Jail");
                MoreCreepsAndWeirdos.proxy
                .addChatMessage(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . .");
                this.buildJail((EntityPlayer) entity);
            }

            super.attackEntity(entity, f);
        }

    }

    public boolean buildJail(EntityPlayer entityplayersp)
    {

        int i = this.rand.nextInt(200) - 100;

        if (this.rand.nextInt(2) == 0) {
            i *= -1;
        }

        this.jailX = (int) ((entityplayersp).posX + i);
        this.jailY = this.rand.nextInt(20) + 25;
        this.jailZ = (int) ((entityplayersp).posZ + i);
        this.maxObstruct = 0x1869f;

        if (MoreCreepsAndWeirdos.instance.jailBuilt) {
            this.jailX = MoreCreepsAndWeirdos.instance.currentJailX;
            this.jailY = MoreCreepsAndWeirdos.instance.currentJailY;
            this.jailZ = MoreCreepsAndWeirdos.instance.currentJailZ;
        } else {
            if (!blockExists(this.worldObj, this.jailX, this.jailY, this.jailZ - 31)
                    || !blockExists(this.worldObj, this.jailX + 14, this.jailY, this.jailZ - 31)
                    || !blockExists(this.worldObj, this.jailX, this.jailY, this.jailZ + 45)
                    || !blockExists(this.worldObj, this.jailX + 14, this.jailY, this.jailZ + 45))
                return false;

            if (!MoreCreepsAndWeirdos.instance.jailBuilt) {
                this.area = 0;
                int j = -1;
                label0:

                    do {
                        if (j >= 6) {
                            break;
                        }

                        for (int k1 = -1; k1 < 14; k1++) {
                            for (int l2 = -1; l2 < 14; l2++) {
                                if (this.worldObj.getBlock(this.jailX + k1, this.jailY + j, this.jailZ + l2) == Blocks.air) {
                                    this.area++;
                                }

                                if (this.area > this.maxObstruct) {
                                    break label0;
                                }
                            }
                        }

                        j++;
                    } while (true);
            }

            if (this.worldObj.getBlock(this.jailX + 15 + 1, this.jailY + 20, this.jailZ + 7) == Blocks.flowing_water
                    || this.worldObj.getBlock(this.jailX + 15 + 1, this.jailY + 20, this.jailZ + 7) == Blocks.water) {
                this.area++;
            }

            if (this.area <= this.maxObstruct) {
                for (int k = -1; k < 6; k++) {
                    for (int l1 = -41; l1 < 55; l1++) {
                        for (int i3 = -1; i3 < 16; i3++) {
                            int j4 = this.rand.nextInt(100);

                            if (j4 < 1) {
                                this.worldObj.setBlock(this.jailX + i3, this.jailY + k, this.jailZ + l1, Blocks.gravel);
                                continue;
                            }

                            if (j4 < 15) {
                                this.worldObj.setBlock(this.jailX + i3, this.jailY + k, this.jailZ + l1, Blocks.mossy_cobblestone);
                            } else {
                                this.worldObj.setBlock(this.jailX + i3, this.jailY + k, this.jailZ + l1, Blocks.stone);
                            }
                        }
                    }
                }

                for (int l = 0; l < 5; l++) {
                    for (int i2 = 0; i2 < 13; i2++) {
                        for (int j3 = 0; j3 < 13; j3++) {
                            this.worldObj.setBlock(this.jailX + j3, this.jailY + l, this.jailZ + i2 + 1, Blocks.air);
                            this.worldObj.setBlock(this.jailX + j3, this.jailY + l, this.jailZ + i2 + 1, Blocks.air);
                        }
                    }
                }

                for (int i1 = 0; i1 < 5; i1++) {
                    for (int j2 = 3; j2 < 11; j2++) {
                        for (int k3 = 3; k3 < 11; k3++) {
                            this.worldObj.setBlock(this.jailX + k3, this.jailY + i1, this.jailZ + j2 + 1, Blocks.stone);
                            this.worldObj.setBlock(this.jailX + k3, this.jailY + i1, this.jailZ + j2 + 1, Blocks.stone);
                        }
                    }
                }

                for (int j1 = 0; j1 < 5; j1++) {
                    for (int k2 = 5; k2 < 9; k2++) {
                        for (int l3 = 5; l3 < 9; l3++) {
                            this.worldObj.setBlock(this.jailX + l3, this.jailY + j1, this.jailZ + k2 + 1, Blocks.air);
                            this.worldObj.setBlock(this.jailX + l3, this.jailY + j1, this.jailZ + k2 + 1, Blocks.air);
                        }
                    }
                }

                this.worldObj.setBlock(this.jailX + 7, this.jailY + 1, this.jailZ + 4, Blocks.air);
                this.worldObj.setBlock(this.jailX + 7, this.jailY + 1, this.jailZ + 5, Blocks.iron_bars);
                this.worldObj.setBlock(this.jailX + 3, this.jailY + 1, this.jailZ + 7, Blocks.glass);
                this.worldObj.setBlock(this.jailX + 4, this.jailY + 1, this.jailZ + 7, Blocks.glass);
                this.worldObj.setBlock(this.jailX + 10, this.jailY + 1, this.jailZ + 7, Blocks.air);
                this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ + 7, Blocks.iron_bars);
                this.worldObj.setBlock(this.jailX + 7, this.jailY + 1, this.jailZ + 11, Blocks.air);
                this.worldObj.setBlock(this.jailX + 7, this.jailY + 1, this.jailZ + 10, Blocks.iron_bars);
                this.worldObj.setBlock(this.jailX + 4, this.jailY, this.jailZ + 8, Blocks.air);
                this.worldObj.setBlock(this.jailX + 3, this.jailY, this.jailZ + 8, Blocks.air);
                this.worldObj.setBlock(this.jailX + 4, this.jailY + 1, this.jailZ + 8, Blocks.air);
                this.worldObj.setBlock(this.jailX + 3, this.jailY + 1, this.jailZ + 8, Blocks.air);
                this.worldObj.setBlock(this.jailX + 3, this.jailY, this.jailZ + 8, Blocks.wooden_door);
                this.worldObj.setBlock(this.jailX + 3, this.jailY, this.jailZ + 8, Blocks.wooden_door);
                this.worldObj.setBlock(this.jailX + 3, this.jailY + 1, this.jailZ + 8, Blocks.wooden_door);
                this.worldObj.setBlock(this.jailX + 3, this.jailY + 1, this.jailZ + 8, Blocks.wooden_door);
                byte byte0 = 15;
                byte byte1 = 7;
                int i4;

                for (i4 = 80; this.worldObj.getBlock(this.jailX + byte0, i4, this.jailZ + byte1) == Blocks.air
                        || this.worldObj.getBlock(this.jailX + byte0, i4, this.jailZ + byte1) == Blocks.leaves
                        || this.worldObj.getBlock(this.jailX + byte0, i4, this.jailZ + byte1) == Blocks.log; i4--) {}

                for (int k4 = 0; k4 < i4 - this.jailY; k4++) {
                    for (int i5 = 0; i5 < 2; i5++) {
                        for (int i7 = 0; i7 < 2; i7++) {
                            this.worldObj.setBlock(this.jailX + i5 + byte0, this.jailY + k4, this.jailZ + byte1 + i7, Blocks.air);
                        }
                    }
                }

                int l4 = 0;

                for (int j5 = 0; j5 < i4 - this.jailY; j5++) {
                    if (l4 == 0) {
                        this.worldObj.setBlock(this.jailX + byte0, this.jailY + j5, this.jailZ + byte1, Blocks.stone_stairs);
                        this.worldObj.setBlock(this.jailX + byte0, this.jailY + j5, this.jailZ + byte1, Blocks.stone_stairs);
                    }

                    if (l4 == 1) {
                        this.worldObj.setBlock(this.jailX + byte0 + 1, this.jailY + j5, this.jailZ + byte1, Blocks.stone_stairs);
                        this.worldObj.setBlock(this.jailX + byte0 + 1, this.jailY + j5, this.jailZ + byte1, Blocks.stone_stairs);
                    }

                    if (l4 == 2) {
                        this.worldObj.setBlock(this.jailX + byte0 + 1, this.jailY + j5, this.jailZ + byte1 + 1, Blocks.stone_stairs);
                        this.worldObj.setBlock(this.jailX + byte0 + 1, this.jailY + j5, this.jailZ + byte1 + 1, Blocks.stone_stairs);
                    }

                    if (l4 == 3) {
                        this.worldObj.setBlock(this.jailX + byte0, this.jailY + j5, this.jailZ + byte1 + 1, Blocks.stone_stairs);
                        this.worldObj.setBlock(this.jailX + byte0, this.jailY + j5, this.jailZ + byte1 + 1, Blocks.stone_stairs);
                    }

                    if (l4++ == 3) {
                        l4 = 0;
                    }
                }

                for (int k5 = 0; k5 < 3; k5++) {
                    this.worldObj.setBlock(this.jailX + 13 + k5, this.jailY, this.jailZ + 7, Blocks.air);
                    this.worldObj.setBlock(this.jailX + 13 + k5, this.jailY + 1, this.jailZ + 7, Blocks.air);
                }

                this.worldObj.setBlock(this.jailX + 13, this.jailY, this.jailZ + byte1, Blocks.iron_door);
                this.worldObj.setBlock(this.jailX + 13, this.jailY, this.jailZ + byte1, Blocks.iron_door);
                this.worldObj.setBlock(this.jailX + 13, this.jailY + 1, this.jailZ + byte1, Blocks.iron_door);
                this.worldObj.setBlock(this.jailX + 13, this.jailY + 1, this.jailZ + byte1, Blocks.iron_door);
                this.worldObj.setBlock(this.jailX + 15, this.jailY, this.jailZ + byte1, Blocks.stone_stairs);
                this.worldObj.setBlock(this.jailX + 15, this.jailY, this.jailZ + byte1, Blocks.stone_stairs);
                this.worldObj.setBlock(this.jailX + 14, this.jailY + 2, this.jailZ + byte1, Blocks.air);

                for (int l5 = 0; l5 < 32; l5++) {
                    for (int j7 = 6; j7 < 9; j7++) {
                        for (int l7 = 0; l7 < 4; l7++) {
                            this.worldObj.setBlockToAir(this.jailX + j7, this.jailY + l7, this.jailZ - l5 - 1);
                            this.worldObj.setBlockToAir(this.jailX + j7, this.jailY + l7, this.jailZ + l5 + 15);
                        }
                    }
                }

                for (int i6 = 1; i6 < 5; i6++) {
                    for (int k7 = 0; k7 < 3; k7++) {
                        for (int i8 = 0; i8 < 3; i8++) {
                            for (int l8 = 0; l8 < 4; l8++) {
                                this.worldObj.setBlockToAir(this.jailX + 10 + i8, this.jailY + l8, (this.jailZ - i6 * 7) + k7);
                                this.worldObj.setBlockToAir(this.jailX + 2 + i8, this.jailY + l8, (this.jailZ - i6 * 7) + k7);
                                this.worldObj.setBlockToAir(this.jailX + 10 + i8, this.jailY + l8, this.jailZ + i6 * 7 + 12 + k7);
                                this.worldObj.setBlockToAir(this.jailX + 2 + i8, this.jailY + l8, this.jailZ + i6 * 7 + 12 + k7);
                            }
                        }
                    }
                }

                this.worldObj.setBlockToAir(this.jailX + 7, this.jailY, this.jailZ);
                this.worldObj.setBlockToAir(this.jailX + 7, this.jailY + 1, this.jailZ);
                this.worldObj.setBlockToAir(this.jailX + 7, this.jailY, this.jailZ + 14);
                this.worldObj.setBlockToAir(this.jailX + 7, this.jailY + 1, this.jailZ + 14);

                for (int j6 = 0; j6 < 4; j6++) {
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ - j6 * 7 - 5 - 2, Blocks.iron_bars);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ - j6 * 7 - 5 - 2, Blocks.iron_bars);
                    this.worldObj.setBlockToAir(this.jailX + 9, this.jailY, this.jailZ - j6 * 7 - 5);
                    this.worldObj.setBlockToAir(this.jailX + 9, this.jailY + 1, this.jailZ - j6 * 7 - 5);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ - j6 * 7 - 5, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ + j6 * 7 + 19 + 2, Blocks.iron_bars);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 5, this.jailY + 1, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ + j6 * 7 + 19 + 2, Blocks.iron_bars);
                    this.worldObj.setBlockToAir(this.jailX + 9, this.jailY, this.jailZ + j6 * 7 + 19);
                    this.worldObj.setBlockToAir(this.jailX + 9, this.jailY + 1, this.jailZ + j6 * 7 + 19);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);
                    this.worldObj.setBlock(this.jailX + 9, this.jailY + 1, this.jailZ + j6 * 7 + 19, Blocks.wooden_door);

                    if (this.rand.nextInt(1) == 0) {
                        this.worldObj.setBlock(this.jailX + 12, this.jailY + 2, this.jailZ - j6 * 7 - 5, Blocks.torch);
                    }

                    if (this.rand.nextInt(1) == 0) {
                        this.worldObj.setBlock(this.jailX + 2, this.jailY + 2, this.jailZ - j6 * 7 - 5, Blocks.torch);
                    }

                    if (this.rand.nextInt(1) == 0) {
                        this.worldObj.setBlock(this.jailX + 12, this.jailY + 2, this.jailZ + j6 * 7 + 19, Blocks.torch);
                    }

                    if (this.rand.nextInt(1) == 0) {
                        this.worldObj.setBlock(this.jailX + 2, this.jailY + 2, this.jailZ + j6 * 7 + 19, Blocks.torch);
                    }
                }

                for (int k6 = 0; k6 < 9; k6++) {
                    if (this.rand.nextInt(2) == 0) {
                        this.worldObj.setBlock(this.jailX + 6, this.jailY + 2, this.jailZ - k6 * 4 - 2, Blocks.torch);
                    }

                    if (this.rand.nextInt(2) == 0) {
                        this.worldObj.setBlock(this.jailX + 8, this.jailY + 2, this.jailZ - k6 * 4 - 2, Blocks.torch);
                    }

                    if (this.rand.nextInt(2) == 0) {
                        this.worldObj.setBlock(this.jailX + 6, this.jailY + 2, this.jailZ + k6 * 4 + 18, Blocks.torch);
                    }

                    if (this.rand.nextInt(2) == 0) {
                        this.worldObj.setBlock(this.jailX + 8, this.jailY + 2, this.jailZ + k6 * 4 + 18, Blocks.torch);
                    }
                }
            } else
                return false;
        }

        this.worldObj.setBlock(this.jailX + 12, this.jailY, this.jailZ + 13, Blocks.chest);
        TileEntityChest tileentitychest = new TileEntityChest();
        this.worldObj.setTileEntity(this.jailX + 12, this.jailY, this.jailZ + 13, tileentitychest);
        this.worldObj.setBlock(this.jailX + 12, this.jailY, this.jailZ + 1, Blocks.chest);
        TileEntityChest tileentitychest1 = new TileEntityChest();
        this.worldObj.setTileEntity(this.jailX + 12, this.jailY, this.jailZ + 1, tileentitychest1);
        this.worldObj.setBlock(this.jailX, this.jailY, this.jailZ + 13, Blocks.chest);
        TileEntityChest tileentitychest2 = new TileEntityChest();
        this.worldObj.setTileEntity(this.jailX, this.jailY, this.jailZ + 13, tileentitychest2);
        this.worldObj.setBlock(this.jailX, this.jailY, this.jailZ + 1, Blocks.chest);
        TileEntityChest tileentitychest3 = new TileEntityChest();
        this.worldObj.setTileEntity(this.jailX, this.jailY, this.jailZ + 1, tileentitychest3);

        for (int l6 = 1; l6 < tileentitychest.getSizeInventory(); l6++) {
            tileentitychest.setInventorySlotContents(l6, null);
            tileentitychest1.setInventorySlotContents(l6, null);
            tileentitychest2.setInventorySlotContents(l6, null);
        }

        Object obj = null;
        ItemStack aitemstack[] = (entityplayersp).inventory.mainInventory;

        for (int j8 = 0; j8 < aitemstack.length; j8++) {
            ItemStack itemstack = aitemstack[j8];

            if (itemstack != null) {
                tileentitychest.setInventorySlotContents(j8, itemstack);
                (entityplayersp).inventory.mainInventory[j8] = null;
                (entityplayersp).inventoryContainer.detectAndSendChanges();
            }
        }

        for (int k8 = 1; k8 < tileentitychest3.getSizeInventory(); k8++) {
            int i9 = this.rand.nextInt(10);

            if (i9 == 1) {
                tileentitychest3
                .setInventorySlotContents(k8, new ItemStack(MoreCreepsAndWeirdos.bandaid, this.rand.nextInt(2) + 1, 0));
            }

            if (i9 == 2) {
                tileentitychest3
                .setInventorySlotContents(k8, new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(24) + 1, 0));
            }
        }

        this.worldObj.setBlock(this.jailX + 11, this.jailY, this.jailZ + 13, Blocks.mob_spawner);
        TileEntityMobSpawner tileentitymobspawner = new TileEntityMobSpawner();
        this.worldObj.setTileEntity(this.jailX + 11, this.jailY, this.jailZ + 13, tileentitymobspawner);
        // Thank you MCPBot for the knowledge that getSpawnerBaseLogic used to be func_145881_a()
        tileentitymobspawner.func_145881_a()
        .setEntityName("Skeleton");
        tileentitychest1.setInventorySlotContents(this.rand.nextInt(5), new ItemStack(Items.stone_pickaxe, 1, 0));
        tileentitychest1.setInventorySlotContents(this.rand.nextInt(5) + 5, new ItemStack(Items.apple, 1, 0));
        tileentitychest2
        .setInventorySlotContents(this.rand.nextInt(5) + 5, new ItemStack(Blocks.torch, this.rand.nextInt(16), 0));
        tileentitychest2.setInventorySlotContents(this.rand.nextInt(5), new ItemStack(Items.apple, 1, 0));
        this.worldObj.setBlock(this.jailX + 6, this.jailY + 2, this.jailZ + 9, Blocks.torch);
        int j9 = this.rand.nextInt(11);

        for (int k9 = 0; k9 < 4; k9++) {
            for (int j10 = 0; j10 < 4; j10++) {
                int l10 = 0;
                int i11 = 0;

                switch (j10 + 1) {
                    case 1:
                        l10 = this.jailX + 12;
                        i11 = this.jailZ - k9 * 7 - 5;
                        break;

                    case 2:
                        l10 = this.jailX + 2;
                        i11 = this.jailZ - k9 * 7 - 5;
                        break;

                    case 3:
                        l10 = this.jailX + 12;
                        i11 = this.jailZ + k9 * 7 + 19;
                        break;

                    case 4:
                        l10 = this.jailX + 2;
                        i11 = this.jailZ + k9 * 7 + 19;
                        break;

                    default:
                        l10 = this.jailX + 12;
                        i11 = this.jailZ - k9 * 7 - 5;
                        break;
                }

                if (k9 * 4 + j10 == j9) {
                    this.populateCell(l10, i11, this.worldObj, entityplayersp, true);
                } else {
                    this.populateCell(l10, i11, this.worldObj, entityplayersp, false);
                }

                if (this.rand.nextInt(3) == 0) {
                    this.dropTreasure(this.worldObj, this.jailX + 12, this.jailY + 2, this.jailZ - k9 * 7 - 5 - 1);
                }

                if (this.rand.nextInt(3) == 0) {
                    this.dropTreasure(this.worldObj, this.jailX + 2, this.jailY + 2, this.jailZ - k9 * 7 - 5 - 1);
                }

                if (this.rand.nextInt(3) == 0) {
                    this.dropTreasure(this.worldObj, this.jailX + 12, this.jailY + 2, this.jailZ + k9 * 7 + 19 + 1);
                }

                if (this.rand.nextInt(3) == 0) {
                    this.dropTreasure(this.worldObj, this.jailX + 2, this.jailY + 2, this.jailZ + k9 * 7 + 19 + 1);
                }
            }
        }


        for (int iter1 = 1; iter1 < this.rand.nextInt(5) + 3; iter1++) {
            CREEPSEntityLawyerFromHell creepsentitylawyerfromhell = new CREEPSEntityLawyerFromHell(this.worldObj);
            creepsentitylawyerfromhell.setLocationAndAngles(
                    this.jailX + 8,
                    this.jailY + 1,
                    this.jailZ - 11 - 1,
                    (entityplayersp).rotationYaw,
                    0.0F);
            creepsentitylawyerfromhell.undead = true;
            creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10D);
            this.worldObj.spawnEntityInWorld(creepsentitylawyerfromhell);
            CREEPSEntityLawyerFromHell creepsentitylawyerfromhell2 = new CREEPSEntityLawyerFromHell(this.worldObj);
            creepsentitylawyerfromhell2.setLocationAndAngles(
                    this.jailX + 8,
                    this.jailY + 1,
                    this.jailZ + 11 + 15,
                    (entityplayersp).rotationYaw,
                    0.0F);
            creepsentitylawyerfromhell2.undead = true;
            creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(20D);
            this.worldObj.spawnEntityInWorld(creepsentitylawyerfromhell2);
        }

        for (int iter2 = 2; iter2 < this.rand.nextInt(3) + 3; iter2++) {
            CREEPSEntityLawyerFromHell creepsentitylawyerfromhell1 = new CREEPSEntityLawyerFromHell(this.worldObj);
            creepsentitylawyerfromhell1.setLocationAndAngles(
                    this.jailX + iter2,
                    this.jailY + 2,
                    this.jailZ + 2,
                    (entityplayersp).rotationYaw,
                    0.0F);
            creepsentitylawyerfromhell1.undead = true;
            creepsentitylawyerfromhell1.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10D);
            this.worldObj.spawnEntityInWorld(creepsentitylawyerfromhell1);
            CREEPSEntityLawyerFromHell creepsentitylawyerfromhell3 = new CREEPSEntityLawyerFromHell(this.worldObj);
            creepsentitylawyerfromhell3.setLocationAndAngles(
                    this.jailX + 2,
                    this.jailY + 2,
                    this.jailZ + iter2,
                    (entityplayersp).rotationYaw,
                    0.0F);
            creepsentitylawyerfromhell3.undead = true;
            creepsentitylawyerfromhell1.getEntityAttribute(SharedMonsterAttributes.maxHealth)
            .setBaseValue(10D);
            this.worldObj.spawnEntityInWorld(creepsentitylawyerfromhell3);
        }




        entityplayersp.setPosition(this.jailX + 7, this.jailY + 2, this.jailZ + 7);
        entityplayersp.heal(20F);

        if (this.rand.nextInt(5) == 0) {
            this.dropTreasure(this.worldObj, this.jailX + 8, this.jailY + 2, this.jailZ + 8);
        }

        List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                entityplayersp,
                (entityplayersp).boundingBox
                .expand(4D, 4D, 4D));

        for (int k10 = 0; k10 < list.size(); k10++) {
            Entity entity = (Entity) list.get(k10);

            if (entity != null && !(entity instanceof EntityPlayer)) {
                entity.setDead();
            }
        }

        MoreCreepsAndWeirdos.instance.currentfine = 0;
        boolean flag = false;

        if (MoreCreepsAndWeirdos.instance.currentfine < 0) {
            MoreCreepsAndWeirdos.instance.currentfine = 0;
        }

        MoreCreepsAndWeirdos.instance.currentJailX = this.jailX;
        MoreCreepsAndWeirdos.instance.currentJailY = this.jailY;
        MoreCreepsAndWeirdos.instance.currentJailZ = this.jailZ;
        MoreCreepsAndWeirdos.instance.jailBuilt = true;
        return true;
    }

    public void dropTreasure(World world, int i, int j, int k) {
        int l = this.rand.nextInt(12);
        ItemStack itemstack = null;

        switch (l) {
            case 1:
                itemstack = new ItemStack(Items.wheat, this.rand.nextInt(2) + 1);
                break;

            case 2:
                itemstack = new ItemStack(Items.cookie, this.rand.nextInt(3) + 3);
                break;

            case 3:
                itemstack = new ItemStack(Items.paper, 1);
                break;

            case 4:
                itemstack = new ItemStack(MoreCreepsAndWeirdos.blorpcola, this.rand.nextInt(3) + 1);
                break;

            case 5:
                itemstack = new ItemStack(Items.bread, 1);
                break;

            case 6:
                itemstack = new ItemStack(MoreCreepsAndWeirdos.evilegg, this.rand.nextInt(2) + 1);
                break;

            case 7:
                itemstack = new ItemStack(Items.water_bucket, 1);
                break;

            case 8:
                itemstack = new ItemStack(Items.cake, 1);
                break;

            case 9:
                itemstack = new ItemStack(MoreCreepsAndWeirdos.money, this.rand.nextInt(5) + 5);
                break;

            case 10:
                itemstack = new ItemStack(MoreCreepsAndWeirdos.lolly, this.rand.nextInt(2) + 1);
                break;

            case 11:
                itemstack = new ItemStack(Items.cake, 1);
                break;

            case 12:
                itemstack = new ItemStack(MoreCreepsAndWeirdos.goodonut, this.rand.nextInt(2) + 1);
                break;

            default:
                itemstack = new ItemStack(Items.cookie, this.rand.nextInt(2) + 1);
                break;
        }

        EntityItem entityitem = new EntityItem(world, i, j, k, itemstack);
        // entityitem.delayBeforeCanPickup = 10;
        if (!this.worldObj.isRemote) {
            world.spawnEntityInWorld(entityitem);
        }
    }

    public void populateCell(int i, int j, World world, EntityPlayer entityplayer, boolean flag) {
        if (flag) {
            List list = world.getEntitiesWithinAABBExcludingEntity(
                    entityplayer,
                    entityplayer.boundingBox
                    .expand(26D, 26D, 26D));

            for (int l = 0; l < list.size(); l++) {
                Entity entity = (Entity) list.get(l);

                if ((entity instanceof CREEPSEntityHotdog) && ((CREEPSEntityHotdog) entity).tamed) {
                    ((CREEPSEntityHotdog) entity).wanderstate = 1;
                    ((CREEPSEntityHotdog) entity).getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                    .setBaseValue(0.0D);

                    if (((CREEPSEntityHotdog) entity).dogsize > 1.0F) {
                        ((CREEPSEntityHotdog) entity).dogsize = 1.0F;
                    }

                    ((CREEPSEntityHotdog) entity).setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                    continue;
                }

                if (!(entity instanceof CREEPSEntityGuineaPig) || !((CREEPSEntityGuineaPig) entity).tamed) {
                    continue;
                }

                ((CREEPSEntityGuineaPig) entity).wanderstate = 1;
                ((CREEPSEntityGuineaPig) entity).getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                .setBaseValue(0.0D);

                if (((CREEPSEntityGuineaPig) entity).modelsize > 1.0F) {
                    ((CREEPSEntityGuineaPig) entity).modelsize = 1.0F;
                }

                ((CREEPSEntityGuineaPig) entity).setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
            }

            return;
        }

        int k = this.rand.nextInt(5);

        switch (k + 1) {
            case 1:
                CREEPSEntityRatMan creepsentityratman = new CREEPSEntityRatMan(world);
                creepsentityratman.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityratman);
                break;

            case 2:
                CREEPSEntityPrisoner creepsentityprisoner = new CREEPSEntityPrisoner(world);
                creepsentityprisoner.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner);
                break;

            case 3:
                CREEPSEntityCamelJockey creepsentitycameljockey = new CREEPSEntityCamelJockey(world);
                creepsentitycameljockey.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentitycameljockey);
                break;

            case 4:
                CREEPSEntityMummy creepsentitymummy = new CREEPSEntityMummy(world);
                creepsentitymummy.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentitymummy);
                break;

            case 5:
                CREEPSEntityPrisoner creepsentityprisoner1 = new CREEPSEntityPrisoner(world);
                creepsentityprisoner1.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner1);
                break;

            default:
                CREEPSEntityPrisoner creepsentityprisoner2 = new CREEPSEntityPrisoner(world);
                creepsentityprisoner2.setLocationAndAngles(i, this.jailY, j, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityprisoner2);
                break;
        }
    }

    public boolean suckMoney(EntityPlayer player) {
        Object obj = null;
        ItemStack aitemstack[] = player.inventory.mainInventory;
        int i = 0;

        for (int j = 0; j < aitemstack.length; j++) {
            ItemStack itemstack = aitemstack[j];

            if (itemstack == null || itemstack.getItem() != MoreCreepsAndWeirdos.money) {
                continue;
            }

            if (!this.undead) {
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:lawyersuck",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            i = this.rand.nextInt(itemstack.stackSize) + 1;

            if (i > itemstack.stackSize) {
                i = itemstack.stackSize;
            }

            if (i == itemstack.stackSize) {
                player.inventory.mainInventory[j] = null;
            } else {
                itemstack.stackSize -= i;
            }
        }

        if (i > 0 && !this.undead) {
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:lawyertake",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
        }

        return true;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setInteger("LawyerState", this.lawyerstate);
        nbttagcompound.setInteger("LawyerTimer", this.lawyertimer);
        nbttagcompound.setInteger("JailX", this.jailX);
        nbttagcompound.setInteger("JailY", this.jailY);
        nbttagcompound.setInteger("JailZ", this.jailZ);
        nbttagcompound.setBoolean("Undead", this.undead);
        nbttagcompound.setFloat("ModelSize", this.modelsize);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        super.readEntityFromNBT(nbttagcompound);
        this.lawyerstate = nbttagcompound.getInteger("LawyerState");
        this.lawyertimer = nbttagcompound.getInteger("LawyerTimer");
        this.jailX = nbttagcompound.getInteger("JailX");
        this.jailY = nbttagcompound.getInteger("JailY");
        this.jailZ = nbttagcompound.getInteger("JailZ");
        this.undead = nbttagcompound.getBoolean("Undead");
        this.modelsize = nbttagcompound.getFloat("ModelSize");
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
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F + (1.0F - this.modelsize) * 2.0F);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
    protected String getLivingSound() {
        if (!this.undead)
            return "morecreeps:lawyer";
        else
            return "morecreeps:lawyerundead";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
    protected String getHurtSound() {
        if (!this.undead)
            return "morecreeps:lawyerhurt";
        else
            return "morecreeps:lawyerundeadhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
    protected String getDeathSound() {
        if (!this.undead)
            return "morecreeps:lawyerdeath";
        else
            return "morecreeps:lawyerundeaddeath";
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
        int l = this.worldObj.getFullBlockLightValue(i, j, k);
        Block i1 = this.worldObj.getBlock(i, j - 1, k);
        return i1 != Blocks.cobblestone && i1 != Blocks.log
                && i1 != Blocks.double_stone_slab
                && i1 != Blocks.stone_slab
                && i1 != Blocks.planks
                && i1 != Blocks.wool
                && this.worldObj.getCollidingBoundingBoxes(this, this.getBoundingBox())
                .size() == 0
                && this.worldObj.canBlockSeeTheSky(i, j, k)
                && this.rand.nextInt(5) == 0
                && l > 10;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    private void smoke() {
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 2; j++) {
                    double d = this.rand.nextGaussian() * 0.02D;
                    double d1 = this.rand.nextGaussian() * 0.02D;
                    double d2 = this.rand.nextGaussian() * 0.02D;
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i * 0.5F - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width - i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - i * 0.5F - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
                            ((this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width)
                            + i * 0.5F,
                            this.posY + this.rand.nextFloat() * this.height,
                            (this.posZ + this.rand.nextFloat() * this.width * 2.0F + i * 0.5F) - this.width,
                            d,
                            d1,
                            d2);
                    this.worldObj.spawnParticle(
                            EnumParticleTypes.EXPLOSION_NORMAL,
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
    public ItemStack getHeldItem() {
        return defaultHeldItem;
    }

    @Override
    public void onDeath(DamageSource damagesource) {
        Entity entity = damagesource.getEntity();
        if (this.rand.nextInt(3) == 0 && !this.undead && (entity instanceof EntityPlayer)) {

            for (int i = 0; i < this.rand.nextInt(4) + 3; i++) {
                CREEPSEntityLawyerFromHell creepsentitylawyerfromhell = new CREEPSEntityLawyerFromHell(this.worldObj);
                this.smoke();
                creepsentitylawyerfromhell.setLocationAndAngles(
                        entity.posX + (this.rand.nextInt(4) - this.rand.nextInt(4)),
                        entity.posY - 1.5D,
                        entity.posZ + (this.rand.nextInt(4) - this.rand.nextInt(4)),
                        this.rotationYaw,
                        0.0F);
                creepsentitylawyerfromhell.undead = true;
                creepsentitylawyerfromhell.getEntityAttribute(SharedMonsterAttributes.maxHealth)
                .setBaseValue(20D);
                if (!this.worldObj.isRemote) {
                    this.worldObj.spawnEntityInWorld(creepsentitylawyerfromhell);
                }
            }
        } else if (this.rand.nextInt(5) == 0 && !this.undead) {
            this.worldObj.playSoundAtEntity(
                    this,
                    "morecreeps:lawyerbum",
                    1.0F,
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            CREEPSEntityBum creepsentitybum = new CREEPSEntityBum(this.worldObj);
            this.smoke();
            creepsentitybum.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            this.worldObj.spawnEntityInWorld(creepsentitybum);
        }

        this.smoke();
        super.onDeath(damagesource);
    }

    public static boolean blockExists(World parWorld, int x, int y, int z) {
        Block state = parWorld.getBlock(x, y, z);
        if (state != null) return true;
        else return false;
    }

    static {
        defaultHeldItem = new ItemStack(Items.bone, 1);
    }
}
