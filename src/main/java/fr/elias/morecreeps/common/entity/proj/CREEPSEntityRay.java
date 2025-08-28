package fr.elias.morecreeps.common.entity.proj;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityFloob;

public class CREEPSEntityRay extends EntityThrowable {

    protected int damage;

    protected boolean playerFire;

    public CREEPSEntityRay(World world) {
        super(world);
        playerFire = false;
    }

    public CREEPSEntityRay(World world, double d, double d1, double d2) {
        super(world, d, d1, d2);
    }

    public CREEPSEntityRay(World world, EntityLivingBase entityliving) {
        super(world, entityliving);
        damage = 4;
    }

    public void onUpdate() {
        super.onUpdate();
        if (onGround) {
            if (rand.nextInt(3) == 0) {
                if (!worldObj.isRemote) {
                    if (CREEPSConfig.rayGunFire) {
                        worldObj.setBlock((int) posX, (int) posY, (int) posZ, Blocks.fire);
                    } else {
                        worldObj.setBlockToAir((int) posX, (int) posY - 1, (int) posZ);
                    }
                }
            }
        }
        if (!worldObj.isRemote) {
            checkNearBlock(Blocks.ice, Blocks.water, (int) posX, (int) posY, (int) posZ);
            checkNearBlock(Blocks.ice, Blocks.water, (int) posX + 1, (int) posY, (int) posZ);
            checkNearBlock(Blocks.ice, Blocks.water, (int) posX - 1, (int) posY, (int) posZ);
            checkNearBlock(Blocks.ice, Blocks.water, (int) posX, (int) posY, (int) posZ - 1);
            checkNearBlock(Blocks.ice, Blocks.water, (int) posX, (int) posY, (int) posZ + 1);

            if (rand.nextBoolean()) {
                checkNearBlock2(Blocks.air, Blocks.fire, (int) posX, (int) posY, (int) posZ);
                checkNearBlock2(Blocks.air, Blocks.fire, (int) posX + 1, (int) posY, (int) posZ);
                checkNearBlock2(Blocks.air, Blocks.fire, (int) posX - 1, (int) posY, (int) posZ);
                checkNearBlock2(Blocks.air, Blocks.fire, (int) posX, (int) posY, (int) posZ - 1);
                checkNearBlock2(Blocks.air, Blocks.fire, (int) posX, (int) posY, (int) posZ + 1);
            }
        }
    }

    public void checkNearBlock(Block blockToReplace, Block theNewBlock, int x, int y, int z) {
        if (worldObj.getBlock(x, y, z) == blockToReplace && CREEPSConfig.rayGunMelt) {
            worldObj.setBlock(x, y, z, theNewBlock);
        }
    }

    public void checkNearBlock2(Block blockToReplace, Block theNewBlock, int x, int y, int z) {
        if (worldObj.getBlock(x, y, z) == blockToReplace) {
            if (CREEPSConfig.rayGunFire) {
                worldObj.setBlock(x, y, z, theNewBlock);
            }

            else {
                worldObj.setBlockToAir(x, y, z);
            }
        }
    }

    public void blast() {
        if (worldObj.isRemote) {
            for (int i = 0; i < 8; i++) {
                byte byte0 = 7;

                if (rand.nextInt(4) == 0) {
                    byte0 = 11;
                }
                MoreCreepsAndWeirdos.proxy.smokeRay(worldObj, this, byte0);
            }
        }
    }

    @Override
    protected void onImpact(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entityHit != null) {
            if (movingobjectposition.entityHit instanceof EntityPlayer) {
                int k = damage;

                if (worldObj.difficultySetting == EnumDifficulty.PEACEFUL) {
                    k = 0;
                }

                if (worldObj.difficultySetting == EnumDifficulty.EASY) {
                    k = k / 3 + 1;
                }

                if (worldObj.difficultySetting == EnumDifficulty.HARD) {
                    k = (k * 3) / 2;
                }
            }

            if ((movingobjectposition.entityHit instanceof EntityLivingBase) && playerFire
                || !(movingobjectposition.entityHit instanceof CREEPSEntityFloob)
                || playerFire) {
                movingobjectposition.entityHit
                    .attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), damage);
            }

            if (!worldObj.isRemote) setDead();
        }
        if (worldObj.isRemote) blast();

        worldObj.playSoundAtEntity(this, "morecreeps:raygun", 0.2F, 1.0F / (rand.nextFloat() * 0.1F + 0.95F));
    }

    protected float getGravityVelocity() {
        return 0.0F;
    }
}
