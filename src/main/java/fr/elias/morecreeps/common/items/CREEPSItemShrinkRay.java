package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.entity.proj.CREEPSEntityShrink;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CREEPSItemShrinkRay extends Item {

  public static Random rand = new Random();

  public CREEPSItemShrinkRay() {
    super();
    maxStackSize = 1;
    setMaxDamage(128);
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    world.playSoundAtEntity(
        entityplayer, "morecreeps:shrinkray", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (!world.isRemote) {
      CREEPSEntityShrink creepsentityshrink = new CREEPSEntityShrink(world, entityplayer, 0.0F);

      if (creepsentityshrink != null) {
        itemstack.damageItem(1, entityplayer);
        world.spawnEntityInWorld(creepsentityshrink);
      }
    }

    return itemstack;
  }
}
