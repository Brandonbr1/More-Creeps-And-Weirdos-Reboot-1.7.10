package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class CREEPSItemLimbs extends Item {

  public CREEPSItemLimbs() {
    super();
    maxStackSize = 24;
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    world.playSoundAtEntity(entityplayer, "morecreeps:barf", 1.0F, 1.0F);
    itemstack.stackSize--;
    entityplayer.attackEntityFrom(DamageSource.starve, 1F);
    if (world.isRemote) {
      MoreCreepsAndWeirdos.proxy.barf(world, entityplayer);
    }

    return itemstack;
  }
}
