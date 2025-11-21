package fr.elias.morecreeps.common.entity.proj;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityCamelJockey;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuy;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBabyMummy;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBigBaby;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBlackSoul;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBum;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleCritter;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleGuard;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCaveman;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilChicken;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilCreature;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilPig;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilScientist;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilSnowman;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityFloob;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityG;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityHunchbackSkeleton;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityLawyerFromHell;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityManDog;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRatMan;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRobotTed;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRobotTodd;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityThief;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityInvisibleMan;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityRockMonster;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntitySnowDevil;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityBlorp;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityBubbleScum;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityCamel;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityDigBug;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHippo;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityKid;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityLolliman;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityNonSwimmer;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityZebra;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSEntityShrink extends EntityThrowable {

    protected int hitX;
    protected int hitY;
    protected int hitZ;
    // protected IBlockState blockHit;

    /** Light value one block more in z axis */
    protected boolean aoLightValueZPos;

    /** Light value of the block itself */
    public EntityLivingBase lightValueOwn;

    /** Used as a scratch variable for ambient occlusion on the north/bottom/west corner. */
    protected int aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
     */
    protected int aoLightValueScratchXYNN;

    protected int damage;

    /** Used as a scratch variable for ambient occlusion on the north/bottom/east corner. */
    protected boolean aoLightValueScratchXYZNNN;

    protected boolean playerFire;
    protected float shrinksize;
    protected int vibrate;

    public CREEPSEntityShrink(World world) {
        super(world);
        this.hitX = -1;
        this.hitY = -1;
        this.hitZ = -1;
        // blockHit = null;
        this.aoLightValueZPos = false;
        this.aoLightValueScratchXYNN = 0;
        this.setSize(0.0325F, 0.01125F);
        this.playerFire = false;
        this.shrinksize = 1.0F;
        this.vibrate = 1;
    }

    public CREEPSEntityShrink(World world, double d, double d1, double d2) {
        this(world);
        this.setPosition(d, d1, d2);
        this.aoLightValueScratchXYZNNN = true;
    }

    public CREEPSEntityShrink(World world, EntityLivingBase entityliving, float f) {
        this(world);
        this.lightValueOwn = entityliving;
        this.damage = 4;
        this.setLocationAndAngles(
            entityliving.posX,
            entityliving.posY + entityliving.getEyeHeight(),
            entityliving.posZ,
            entityliving.rotationYaw,
            entityliving.rotationPitch);
        this.posX -= MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;
        this.posY += 0.20000000149011612D;
        this.posZ -= MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI) * 0.16F;

        if (entityliving instanceof EntityPlayer) {
            this.posY -= 0.40000000596046448D;
        }

        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin((this.rotationYaw / 180F) * (float) Math.PI)
            * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        this.motionZ = MathHelper.cos((this.rotationYaw / 180F) * (float) Math.PI)
            * MathHelper.cos((this.rotationPitch / 180F) * (float) Math.PI);
        this.motionY = -MathHelper.sin((this.rotationPitch / 180F) * (float) Math.PI);
        float f1 = 1.0F;

        if (entityliving instanceof EntityPlayer) {
            this.playerFire = true;
            float f2 = 0.3333333F;
            float f3 = f2 / 0.1F;

            if (f3 > 0.0F) {
                f1 = (float) (f1 * (1.0D + 2D / f3));
            }
        }

        if (Math.abs(entityliving.motionX) > 0.10000000000000001D
            || Math.abs(entityliving.motionY) > 0.10000000000000001D
            || Math.abs(entityliving.motionZ) > 0.10000000000000001D) {
            f1 *= 2.0F;
        }

        if (entityliving.onGround) {
            ;
        }

        this.func_22374_a(
            this.motionX,
            this.motionY,
            this.motionZ,
            (float) (2.5D + (this.worldObj.rand.nextFloat() - 0.5D)),
            f1);
    }

    /** Called by a player entity when they collide with an entity */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {}

    @Override
    protected void entityInit() {}

    public void func_22374_a(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d1 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d2 += this.rand.nextGaussian() * 0.0074999998323619366D * f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(d1, f3) * 180D) / Math.PI);
        this.aoLightValueScratchXYZNNP = 0;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to
     * its average edge length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    public boolean isInRangeToRenderDist(double d) {
        return true;
    }

    /** Called to update the entity's position/logic. */
    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.aoLightValueScratchXYNN == 5) {
            this.setDead();
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D)
                / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(this.motionY, f) * 180D) / Math.PI);
        }

        if (this.aoLightValueZPos) {
            /*
             * if (i != blockHit)
             * {
             * aoLightValueZPos = false;
             * motionX *= rand.nextFloat() * 0.2F;
             * motionZ *= rand.nextFloat() * 0.2F;
             * aoLightValueScratchXYZNNP = 0;
             * aoLightValueScratchXYNN = 0;
             * }
             */

            this.aoLightValueScratchXYZNNP++;

            if (this.aoLightValueScratchXYZNNP == 5) {
                this.setDead();
            }

            return;
        } else {
            this.aoLightValueScratchXYNN++;
        }

        Vec3 vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        Vec3 vec3d1 = Vec3
            .createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

        if (movingobjectposition != null) {
            vec3d1 = Vec3.createVectorHelper(
                movingobjectposition.hitVec.xCoord,
                movingobjectposition.hitVec.yCoord,
                movingobjectposition.hitVec.zCoord);
        }

        Entity entity = null;
        List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
            this,
            this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                .expand(1.0D, 1.0D, 1.0D));
        double d = 0.0D;

        for (int j = 0; j < list.size(); j++) {
            Entity entity1 = (Entity) list.get(j);

            if (!entity1.canBeCollidedWith()
                || (entity1 == this.lightValueOwn
                    || this.lightValueOwn != null && entity1 == this.lightValueOwn.ridingEntity)
                    && this.aoLightValueScratchXYNN < 5
                || this.aoLightValueScratchXYZNNN) {
                if (this.motionZ != 0.0D || !((this.motionX == 0.0D) & (this.motionY == 0.0D))) {
                    continue;
                }

                this.setDead();
                break;
            }

            float f4 = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

            if (movingobjectposition1 == null) {
                continue;
            }

            double d2 = vec3d.distanceTo(movingobjectposition1.hitVec);

            if (d2 < d || d == 0.0D) {
                entity = entity1;
                d = d2;
            }
        }

        if (entity != null) {
            movingobjectposition = new MovingObjectPosition(entity);
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null) {
                if (movingobjectposition.entityHit instanceof EntityLiving) {
                    boolean flag = false;

                    if (movingobjectposition.entityHit instanceof CREEPSEntityKid) {
                        if (((CREEPSEntityKid) movingobjectposition.entityHit).modelsize > 0.25F) {
                            ((CREEPSEntityKid) movingobjectposition.entityHit).modelsize -= 0.15F;
                        } else {
                            ((CREEPSEntityKid) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBigBaby) {
                        if (((CREEPSEntityBigBaby) movingobjectposition.entityHit).modelsize > 0.5F) {
                            ((CREEPSEntityBigBaby) movingobjectposition.entityHit).modelsize -= 0.25F;
                        } else {
                            ((CREEPSEntityBigBaby) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityRatMan) {
                        if (((CREEPSEntityRatMan) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityRatMan) movingobjectposition.entityHit).modelsize -= 0.2F;
                            ((CREEPSEntityRatMan) movingobjectposition.entityHit).modelspeed -= 0.15F;
                        } else {
                            ((CREEPSEntityRatMan) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityGuineaPig) {
                        if (((CREEPSEntityGuineaPig) movingobjectposition.entityHit).modelsize > 0.35F) {
                            ((CREEPSEntityGuineaPig) movingobjectposition.entityHit).modelsize -= 0.15F;
                        } else {
                            ((CREEPSEntityGuineaPig) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityHotdog) {
                        if (((CREEPSEntityHotdog) movingobjectposition.entityHit).dogsize > 0.35F) {
                            ((CREEPSEntityHotdog) movingobjectposition.entityHit).dogsize -= 0.15F;
                        } else {
                            ((CREEPSEntityHotdog) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityRobotTed) {
                        if (((CREEPSEntityRobotTed) movingobjectposition.entityHit).robotsize > 0.6F) {
                            ((CREEPSEntityRobotTed) movingobjectposition.entityHit).robotsize -= 0.5F;
                            ((CREEPSEntityRobotTed) movingobjectposition.entityHit).modelspeed -= 0.25F;
                        } else {
                            ((CREEPSEntityRobotTed) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityRobotTodd) {
                        if (((CREEPSEntityRobotTodd) movingobjectposition.entityHit).robotsize > 0.4F) {
                            ((CREEPSEntityRobotTodd) movingobjectposition.entityHit).robotsize -= 0.4F;
                            ((CREEPSEntityRobotTodd) movingobjectposition.entityHit).modelspeed -= 0.15F;
                        } else {
                            ((CREEPSEntityRobotTodd) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityGooGoat) {
                        if (((CREEPSEntityGooGoat) movingobjectposition.entityHit).goatsize > 0.4F) {
                            ((CREEPSEntityGooGoat) movingobjectposition.entityHit).goatsize -= 0.4F;
                            ((CREEPSEntityGooGoat) movingobjectposition.entityHit).modelspeed -= 0.15F;
                        } else {
                            ((CREEPSEntityGooGoat) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityLolliman) {
                        if (((CREEPSEntityLolliman) movingobjectposition.entityHit).modelsize > 0.6F) {
                            ((CREEPSEntityLolliman) movingobjectposition.entityHit).modelsize -= 0.4F;
                        } else {
                            ((CREEPSEntityLolliman) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityCastleCritter) {
                        if (((CREEPSEntityCastleCritter) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityCastleCritter) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityCastleCritter) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntitySneakySal) {
                        if (((CREEPSEntitySneakySal) movingobjectposition.entityHit).modelsize > 0.6F) {
                            ((CREEPSEntitySneakySal) movingobjectposition.entityHit).modelsize -= 0.2F;
                            ((CREEPSEntitySneakySal) movingobjectposition.entityHit).dissedmax = 0;

                            if (this.rand.nextInt(2) == 0) {
                                this.worldObj.playSoundAtEntity(this, "morecreeps:salnobodyshrinks", 0.5F, 1.0F);
                            }
                        } else {
                            ((CREEPSEntitySneakySal) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityArmyGuy) {
                        if (((CREEPSEntityArmyGuy) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityArmyGuy) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityArmyGuy) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityEvilChicken) {
                        if (((CREEPSEntityEvilChicken) movingobjectposition.entityHit).modelsize > 0.5F) {
                            ((CREEPSEntityEvilChicken) movingobjectposition.entityHit).modelsize -= 0.4F;
                            ((CREEPSEntityEvilChicken) movingobjectposition.entityHit)
                                .getEntityAttribute(SharedMonsterAttributes.movementSpeed)
                                .setBaseValue(0.015D);
                        } else {
                            ((CREEPSEntityEvilChicken) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBum) {
                        if (((CREEPSEntityBum) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityBum) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityBum) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBubbleScum) {
                        if (((CREEPSEntityBubbleScum) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityBubbleScum) movingobjectposition.entityHit).modelsize -= 0.15F;
                        } else {
                            ((CREEPSEntityBubbleScum) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityLawyerFromHell) {
                        if (((CREEPSEntityLawyerFromHell) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityLawyerFromHell) movingobjectposition.entityHit).modelsize -= 0.2F;
                            MoreCreepsAndWeirdos.INSTANCE.currentfine += 50;
                        } else {
                            ((CREEPSEntityLawyerFromHell) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityG) {
                        if (((CREEPSEntityG) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityG) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityG) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityRockMonster) {
                        if (((CREEPSEntityRockMonster) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityRockMonster) movingobjectposition.entityHit).modelsize -= 0.4F;
                        } else {
                            ((CREEPSEntityRockMonster) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBabyMummy) {
                        if (((CREEPSEntityBabyMummy) movingobjectposition.entityHit).babysize > 0.2F) {
                            ((CREEPSEntityBabyMummy) movingobjectposition.entityHit).babysize -= 0.2F;
                        } else {
                            ((CREEPSEntityBabyMummy) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBlackSoul) {
                        if (((CREEPSEntityBlackSoul) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityBlackSoul) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityBlackSoul) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityBlorp) {
                        if (((CREEPSEntityBlorp) movingobjectposition.entityHit).blorpsize > 0.3F) {
                            ((CREEPSEntityBlorp) movingobjectposition.entityHit).blorpsize -= 0.3F;
                        } else {
                            ((CREEPSEntityBlorp) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityCamel) {
                        if (((CREEPSEntityCamel) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityCamel) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityCamel) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityCamelJockey) {
                        if (((CREEPSEntityCamelJockey) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityCamelJockey) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityCamelJockey) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityCastleGuard) {
                        if (((CREEPSEntityCastleGuard) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityCastleGuard) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityCastleGuard) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityCaveman) {
                        if (((CREEPSEntityCaveman) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityCaveman) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityCaveman) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityDesertLizard) {
                        if (((CREEPSEntityDesertLizard) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityDesertLizard) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityDesertLizard) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityDigBug) {
                        if (((CREEPSEntityDigBug) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityDigBug) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityDigBug) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityEvilCreature) {
                        if (((CREEPSEntityEvilCreature) movingobjectposition.entityHit).modelsize > 0.5F) {
                            ((CREEPSEntityEvilCreature) movingobjectposition.entityHit).modelsize -= 0.4F;
                        } else {
                            ((CREEPSEntityEvilCreature) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityEvilPig) {
                        if (((CREEPSEntityEvilPig) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityEvilPig) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityEvilPig) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityEvilScientist) {
                        if (((CREEPSEntityEvilScientist) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityEvilScientist) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityEvilScientist) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityEvilSnowman) {
                        if (((CREEPSEntityEvilSnowman) movingobjectposition.entityHit).snowsize > 0.3F) {
                            ((CREEPSEntityEvilSnowman) movingobjectposition.entityHit).snowsize -= 0.2F;
                        } else {
                            ((CREEPSEntityEvilSnowman) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityFloob) {
                        if (((CREEPSEntityFloob) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityFloob) movingobjectposition.entityHit).modelsize -= 0.4F;
                        } else {
                            ((CREEPSEntityFloob) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityHippo) {
                        if (((CREEPSEntityHippo) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntityHippo) movingobjectposition.entityHit).modelsize -= 0.3F;
                        } else {
                            ((CREEPSEntityHippo) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityHunchback) {
                        if (((CREEPSEntityHunchback) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityHunchback) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityHunchback) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityHunchbackSkeleton) {
                        if (((CREEPSEntityHunchbackSkeleton) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityHunchbackSkeleton) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityHunchbackSkeleton) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityInvisibleMan) {
                        if (((CREEPSEntityInvisibleMan) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityInvisibleMan) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityInvisibleMan) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityManDog) {
                        if (((CREEPSEntityManDog) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityManDog) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityManDog) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityNonSwimmer) {
                        if (((CREEPSEntityNonSwimmer) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityNonSwimmer) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityNonSwimmer) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityRocketGiraffe) {
                        if (((CREEPSEntityRocketGiraffe) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityRocketGiraffe) movingobjectposition.entityHit).modelsize -= 0.15F;
                        } else {
                            ((CREEPSEntityRocketGiraffe) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntitySnowDevil) {
                        if (((CREEPSEntitySnowDevil) movingobjectposition.entityHit).modelsize > 0.4F) {
                            ((CREEPSEntitySnowDevil) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntitySnowDevil) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityThief) {
                        if (((CREEPSEntityThief) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityThief) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityThief) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (movingobjectposition.entityHit instanceof CREEPSEntityZebra) {
                        if (((CREEPSEntityZebra) movingobjectposition.entityHit).modelsize > 0.3F) {
                            ((CREEPSEntityZebra) movingobjectposition.entityHit).modelsize -= 0.2F;
                        } else {
                            ((CREEPSEntityZebra) movingobjectposition.entityHit).setDead();
                            flag = true;
                        }
                    }

                    if (flag) {
                        this.smoke();
                        this.worldObj.playSoundAtEntity(
                            this,
                            "morecreeps:shrinkkill",
                            1.0F,
                            1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
                        this.setDead();
                    }
                } else {
                    this.setDead();
                }
            } else {
                this.hitX = movingobjectposition.blockX;
                this.hitY = movingobjectposition.blockY;
                this.hitZ = movingobjectposition.blockZ;
                // blockHit = worldObj.getBlockState(new BlockPos(hitX, hitY, hitZ));
                this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
                this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
                this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
                float f1 = MathHelper.sqrt_double(
                    this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                this.posX -= (this.motionX / f1) * 0.05000000074505806D;
                this.posY -= (this.motionY / f1) * 0.05000000074505806D;
                this.posZ -= (this.motionZ / f1) * 0.05000000074505806D;
                this.aoLightValueZPos = true;
                this.setDead();

                /*
                 * if (blockHit.getBlock() == Blocks.ice)
                 * {
                 * worldObj.setBlockState(new BlockPos(hitX, hitY, hitZ), Blocks.flowing_water.getDefaultState());
                 * }
                 */

                this.setDead();
            }

            this.worldObj
                .playSoundAtEntity(this, "morecreeps:raygun", 0.2F, 1.0F / (this.rand.nextFloat() * 0.1F + 0.95F));
            this.setDead();
        }

        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.shrinkSmoke(this.worldObj, this);
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) ((Math.atan2(this.motionX, this.motionZ) * 180D) / Math.PI);

        for (this.rotationPitch = (float) ((Math.atan2(this.motionY, f2) * 180D) / Math.PI); this.rotationPitch
            - this.prevRotationPitch < -180F; this.prevRotationPitch -= 360F) {}

        for (; this.rotationPitch - this.prevRotationPitch >= 180F; this.prevRotationPitch += 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw < -180F; this.prevRotationYaw -= 360F) {}

        for (; this.rotationYaw - this.prevRotationYaw >= 180F; this.prevRotationYaw += 360F) {}

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
        float f3 = 0.99F;
        // this.vibratePlayer((EntityPlayer)this.getThrower());
        if (this.handleWaterMovement()) {
            for (int l = 0; l < 4; l++) {
                float f7 = 0.25F;
                this.worldObj.spawnParticle(
                    EnumParticleTypes.WATER_BUBBLE,
                    this.posX - this.motionX * f7,
                    this.posY - this.motionY * f7,
                    this.posZ - this.motionZ * f7,
                    this.motionX,
                    this.motionY,
                    this.motionZ);
            }

            f3 = 0.8F;
            this.setDead();
        }

        this.motionX *= f3;
        this.motionZ *= f3;
        this.setPosition(this.posX, this.posY, this.posZ);
    }
    /*
     * public void vibratePlayer(EntityPlayer player)
     * {
     * player.posY += rand.nextGaussian() * 0.34999999999999998D * (double)vibrate;
     * vibrate *= -1;
     * double d1 = -MathHelper.sin((player.rotationYaw * (float)Math.PI) / 180F);
     * double d3 = MathHelper.cos((player.rotationYaw * (float)Math.PI) / 180F);
     * player.posX += (double)((float)vibrate * 0.25F) * d1;
     * player.posZ += (double)((float)vibrate * 0.25F) * d3;
     * }
     */

    /** (abstract) Protected helper method to write subclass entity data to NBT. */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.hitX);
        nbttagcompound.setShort("yTile", (short) this.hitY);
        nbttagcompound.setShort("zTile", (short) this.hitZ);
        nbttagcompound.setByte("inGround", (byte) (this.aoLightValueZPos ? 1 : 0));
    }

    /** (abstract) Protected helper method to read subclass entity data from NBT. */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.hitX = nbttagcompound.getShort("xTile");
        this.hitY = nbttagcompound.getShort("yTile");
        this.hitZ = nbttagcompound.getShort("zTile");
        this.aoLightValueZPos = nbttagcompound.getByte("inGround") == 1;
    }

    @Override
    public float getShadowSize() {
        return 0.0F;
    }

    public void blast() {
        if (this.worldObj.isRemote) {
            MoreCreepsAndWeirdos.proxy.shrinkBlast(this.worldObj, this, this.rand);
        }
    }

    /** Will get destroyed next tick. */
    @Override
    public void setDead() {
        this.blast();
        super.setDead();
        this.lightValueOwn = null;
    }

    private void smoke() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 5; k++) {
                    double d = this.rand.nextGaussian() * 0.12D;
                    double d1 = this.rand.nextGaussian() * 0.12D;
                    double d2 = this.rand.nextGaussian() * 0.12D;
                    this.worldObj.spawnParticle(
                        EnumParticleTypes.EXPLOSION_NORMAL,
                        (this.posX + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        this.posY + this.rand.nextFloat() * this.height,
                        (this.posZ + this.rand.nextFloat() * this.width * 2.0F) - this.width,
                        d,
                        d1,
                        d2);
                }
            }
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {
        // TODO Auto-generated method stub

    }
}
