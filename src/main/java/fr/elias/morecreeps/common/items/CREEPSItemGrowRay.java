package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.entity.proj.CREEPSEntityGrow;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CREEPSItemGrowRay extends Item {

  public static Random rand = new Random();

  public CREEPSItemGrowRay() {
    super();
    maxStackSize = 1;
    setMaxDamage(64);
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    world.playSoundAtEntity(
        entityplayer, "morecreeps:growray", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (!world.isRemote) {
      CREEPSEntityGrow creepsentitygrow = new CREEPSEntityGrow(world, entityplayer, 0.0F);

      if (creepsentitygrow != null) {
        itemstack.damageItem(1, entityplayer);
        world.spawnEntityInWorld(creepsentitygrow);
      }
    }

    return itemstack;
  }
}
