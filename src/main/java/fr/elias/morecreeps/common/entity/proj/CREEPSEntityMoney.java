package fr.elias.morecreeps.common.entity.proj;

import java.util.List;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityLawyerFromHell;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityPreacher;

public class CREEPSEntityMoney extends EntityThrowable {

    private int field_20056_b;
    private int field_20055_c;
    private int field_20054_d;
    private int field_20053_e;
    private boolean field_20052_f;
    public int field_20057_a;
    private EntityLivingBase field_20051_g;
    private int field_20050_h;
    private int field_20049_i;
    protected double initialVelocity;
    public EntityLivingBase owner;
    double bounceFactor;

    public CREEPSEntityMoney(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.initialVelocity = 1.0D;
        this.bounceFactor = 0.14999999999999999D;
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    @Override
    public boolean isInRangeToRenderDist(double d) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4D;
        d1 *= 64D;
        return d < d1 * d1;
    }

    public CREEPSEntityMoney(World world, EntityLivingBase entity) {
        this(world);
        this.owner = entity;
        this.setRotation(entity.rotationYaw, 0.0F);
        double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
        this.motionX = 0.59999999999999998D * d * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
        this.motionY = -0.69999999999999996D * MathHelper.sin((entity.rotationPitch / 180F) * (float) Math.PI);
        this.motionZ = 0.59999999999999998D * d1 * MathHelper.cos((entity.rotationPitch / 180F) * (float) Math.PI);
        this.setPosition(entity.posX + d * 0.80000000000000004D, entity.posY, entity.posZ + d1 * 0.80000000000000004D);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public CREEPSEntityMoney(World world, double d, double d1, double d2) {
        this(world);
        this.setPosition(d, d1, d2);
    }

    public void func_20048_a(double d, double d1, double d2, float f, float f1) {
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
        this.field_20050_h = 0;
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    @Override
    public void setVelocity(double d, double d1, double d2) {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float) ((Math.atan2(d, d2) * 180D) / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) ((Math.atan2(d1, f) * 180D) / Math.PI);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate() {
        double d = this.motionX;
        double d1 = this.motionY;
        double d2 = this.motionZ;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.handleWaterMovement()) {
            this.motionY += 0.0087999999523162842D;
            this.motionX *= 0.97999999999999998D;
            this.motionZ *= 0.68000000000000005D;
        }

        if (this.motionX != d) {
            this.motionX = -this.bounceFactor * d;
        }

        if (this.motionY != d1) {
            this.motionY = -this.bounceFactor * d1;
        }

        if (this.motionY != d1) {
            this.motionY = -this.bounceFactor * d1;
        } else if (!this.handleWaterMovement()) {
            this.motionY -= 0.0050000000000000001D;
        }

        this.motionX *= 0.97999999999999998D;
        this.motionY -= 0.0080000000000000002D;
        this.motionZ *= 0.97999999999999998D;

        if (this.isCollidedVertically) {
            this.motionX *= 0.14999999999999999D;
            this.motionZ *= 0.14999999999999999D;
        }

        if (!this.onGround) {
            Object obj = null;
            List<?> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(
                    this,
                    this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ)
                    .expand(0.5D, 0.5D, 0.5D));
            double d3 = 0.0D;

            for (int i = 0; i < list.size(); i++) {
                Entity entity = (Entity) list.get(i);

                if (entity.canBeCollidedWith() && (entity instanceof CREEPSEntityPreacher))
                {
                    EntityPlayer player = this.worldObj.getClosestPlayerToEntity(entity, 2D);

                    if (player != null)
                    {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(player), 10);
                    }

                    if (player != null) {
                        if (((EntityLiving) entity).getHealth() <= 0 && !((EntityPlayerMP) player).func_147099_x()
                                .hasAchievementUnlocked(MoreCreepsAndWeirdos.achievefalseidol)) {
                            player.addStat(MoreCreepsAndWeirdos.achievefalseidol, 1);
                            double d4 = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
                            double d5 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
                            CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
                            creepsentitytrophy.setLocationAndAngles(
                                    player.posX + d4 * 3D,
                                    player.posY - 2D,
                                    player.posZ + d5 * 3D,
                                    player.rotationYaw,
                                    0.0F);
                            this.worldObj.spawnEntityInWorld(creepsentitytrophy);
                        }

                        this.setDead();
                    }
                }

                if (!entity.canBeCollidedWith() || !(entity instanceof CREEPSEntityLawyerFromHell)) {
                    continue;
                }

                // Possible threading issues
                MoreCreepsAndWeirdos.INSTANCE.currentfine -= 50;

                if (MoreCreepsAndWeirdos.INSTANCE.currentfine < 0) {
                    MoreCreepsAndWeirdos.INSTANCE.currentfine = 0;
                }

                this.setDead();
            }
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    @Override
    public void onCollideWithPlayer(EntityPlayer entityplayer) {
        if (this.rand.nextInt(30) == 0) {
            if (entityplayer.inventory.addItemStackToInventory(new ItemStack(MoreCreepsAndWeirdos.money, 1, 0))) {
                this.worldObj.playSoundAtEntity(
                        this,
                        "random.pop",
                        0.2F,
                        ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.onItemPickup(this, 1);
                this.setDead();
            }

            Object obj = null;
            ItemStack aitemstack[] = entityplayer.inventory.mainInventory;
            int i = 0;

            for (ItemStack itemstack : aitemstack) {
                if (itemstack != null && itemstack.getItem() == MoreCreepsAndWeirdos.money) {
                    i += itemstack.stackSize;
                }
            }

            boolean flag = false;

            // TODO : Fix the achievement problem... (find an alternative to "hasAchievementUnlocked")...
            // 29/12/2015 : Packet Needed for achievement detector... -.-
            if (!this.worldObj.isRemote) {
                if (!(entityplayer instanceof EntityPlayerSP)) {
                    EntityPlayerMP playerMP = (EntityPlayerMP) entityplayer;
                    if (!playerMP.func_147099_x()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve100bucks) && i > 99) {
                        flag = true;
                        this.confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsAndWeirdos.achieve100bucks, 1);
                    }

                    if (!playerMP.func_147099_x()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve500bucks) && i > 499) {
                        flag = true;
                        this.confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsAndWeirdos.achieve500bucks, 1);
                    }

                    if (!playerMP.func_147099_x()
                            .hasAchievementUnlocked(MoreCreepsAndWeirdos.achieve1000bucks) && i > 999) {
                        flag = true;
                        this.confetti(entityplayer);
                        entityplayer.addStat(MoreCreepsAndWeirdos.achieve1000bucks, 1);
                    }
                } else {
                    FMLLog.getLogger().info( "[More Creeps Unofficial] Class cast failed when tried to trigger achievement in this class : "
                            + this.getClass()
                            + ".");
                    FMLLog.getLogger().info("[More Creeps Unofficial] Please contact elias54 or Astromojang for this!");
                }
            }

            if (flag) {
                this.worldObj.playSoundAtEntity(
                        this,
                        "morecreeps:achievement",
                        1.0F,
                        (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    public void confetti(EntityPlayer entityplayersp) {
        double d = -MathHelper.sin((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
        double d1 = MathHelper.cos((entityplayersp.rotationYaw * (float) Math.PI) / 180F);
        CREEPSEntityTrophy creepsentitytrophy = new CREEPSEntityTrophy(this.worldObj);
        creepsentitytrophy.setLocationAndAngles(
                entityplayersp.posX + d * 3D,
                entityplayersp.posY - 2D,
                (entityplayersp).posZ + d1 * 3D,
                (entityplayersp).rotationYaw,
                0.0F);
        this.worldObj.spawnEntityInWorld(creepsentitytrophy);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short) this.field_20056_b);
        nbttagcompound.setShort("yTile", (short) this.field_20055_c);
        nbttagcompound.setShort("zTile", (short) this.field_20054_d);
        nbttagcompound.setByte("inTile", (byte) this.field_20053_e);
        nbttagcompound.setByte("shake", (byte) this.field_20057_a);
        nbttagcompound.setByte("inGround", (byte) (this.field_20052_f ? 1 : 0));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        this.field_20056_b = nbttagcompound.getShort("xTile");
        this.field_20055_c = nbttagcompound.getShort("yTile");
        this.field_20054_d = nbttagcompound.getShort("zTile");
        this.field_20053_e = nbttagcompound.getByte("inTile") & 0xff;
        this.field_20057_a = nbttagcompound.getByte("shake") & 0xff;
        this.field_20052_f = nbttagcompound.getByte("inGround") == 1;
    }

    @Override
    public float getShadowSize() {
        return 0.2F;
    }

    public ItemStack getEntityItem() {
        return new ItemStack(MoreCreepsAndWeirdos.money, 1, 0);
    }

    @Override
    protected void onImpact(MovingObjectPosition p_70184_1_) {

    }
}
