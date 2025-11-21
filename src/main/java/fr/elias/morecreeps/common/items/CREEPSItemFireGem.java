package fr.elias.morecreeps.common.items;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityPreacher;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.port.EnumParticleTypes;

public class CREEPSItemFireGem extends Item {

    public static Random random = new Random();

    public CREEPSItemFireGem() {
        super();
        this.maxStackSize = 1;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
     * world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        world.playSoundAtEntity(entityplayer, "morecreeps:firegem", 1.0F, 1.0F);
        itemstack.damageItem(1, entityplayer);
        entityplayer.swingItem();
        List<?> list = world.getEntitiesWithinAABB(
            EntityLivingBase.class,
            AxisAlignedBB
                .getBoundingBox(
                    entityplayer.posX,
                    entityplayer.posY,
                    entityplayer.posZ,
                    entityplayer.posX + 1.0D,
                    entityplayer.posY + 1.0D,
                    entityplayer.posZ + 1.0D)
                .expand(10D, 10D, 10D));

        for (int i = 0; i < list.size(); i++) {
            Entity entity = (Entity) list.get(i);

            if (!(entity instanceof EntityLivingBase)) {
                continue;
            }

            EntityLivingBase entityliving = (EntityLivingBase) entity;

            if ((entityliving instanceof CREEPSEntityHotdog) || (entityliving instanceof CREEPSEntityHunchback)
                || (entityliving instanceof EntityPlayer)
                || (entityliving instanceof CREEPSEntityGuineaPig)
                || (entityliving instanceof CREEPSEntityPreacher)) {
                continue;
            }

            if (world.isRemote) {
                for (int j = 0; j < 10; j++) {
                    double d = random.nextGaussian() * 0.02D;
                    double d1 = random.nextGaussian() * 0.02D;
                    double d2 = random.nextGaussian() * 0.02D;
                    world.spawnParticle(
                        EnumParticleTypes.SMOKE_NORMAL,
                        entityliving.posX + random.nextFloat() * 1.5F,
                        entityliving.posY + 0.5D + random.nextFloat() * 2.5F,
                        entityliving.posZ + random.nextFloat() * 1.5F,
                        d,
                        d1,
                        d2);
                }
            }

            entityliving.attackEntityFrom(DamageSource.inFire, 2F);
            entityliving.motionY += 0.5D;
            entityliving.setFire(15);
        }

        return itemstack;
    }

    /** Returns the maximum damage an item can take. */
    @Override
    public int getMaxDamage() {
        return 64;
    }
}
