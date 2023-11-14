package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.CREEPSEntitySchlump;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CREEPSItemBabyJarFull extends Item
{
    private int healAmount;
    private int placedelay;

    public CREEPSItemBabyJarFull()
    {
        super();
        healAmount = 20;
        maxStackSize = 1;
        placedelay = 30;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.isSneaking())
        {
            world.playSoundAtEntity(entityplayer, "morecreeps:barf", 1.0F, 1.0F);
            itemstack.stackSize--;

            if(world.isRemote)
            {
            	MoreCreepsAndWeirdos.proxy.barf(world, entityplayer);
            }

            placedelay = 30;
            entityplayer.heal(healAmount);
        }
        else if (placedelay <= 0)
        {
            float f = 1.0F;
            float f1 = entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * f;
            float f2 = entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * f;
            double d2 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double)f;
            double d3 = (entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double)f + 1.6200000000000001D) - entityplayer.getYOffset();
            double d4 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double)f;
            Vec3 vec3d = Vec3.createVectorHelper(d2, d3, d4);
            float f3 = MathHelper.cos(-f2 * 0.01745329F - (float)Math.PI);
            float f4 = MathHelper.sin(-f2 * 0.01745329F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.01745329F);
            float f6 = MathHelper.sin(-f1 * 0.01745329F);
            float f7 = f4 * f5;
            float f8 = f6;
            float f9 = f3 * f5;
            double d5 = 5D;
            Vec3 vec3d1 = vec3d.addVector((double)f7 * d5, (double)f8 * d5, (double)f9 * d5);
            MovingObjectPosition movingobjectposition = world.rayTraceBlocks(vec3d, vec3d1, true);

            if (movingobjectposition == null)
            {
                return itemstack;
            }

            if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                int k = movingobjectposition.blockX;
                int l = movingobjectposition.blockY + 1;
                int i1 = movingobjectposition.blockZ;
                Block j1 = world.getBlock(k, l, i1);
                CREEPSEntitySchlump creepsentityschlump = new CREEPSEntitySchlump(world);
                creepsentityschlump.setLocationAndAngles(k, l, i1, entityplayer.rotationYaw, 0.0F);
                world.spawnEntityInWorld(creepsentityschlump);
                placedelay = 30;
                return new ItemStack(MoreCreepsAndWeirdos.babyjarempty);
            }
        }

        return itemstack;
    }

    /**
     * Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
     * update it's contents.
     */
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        super.onUpdate(itemstack, world, entity, i, flag);

        if (flag && placedelay > 0)
        {
            placedelay--;
        }
    }
}
