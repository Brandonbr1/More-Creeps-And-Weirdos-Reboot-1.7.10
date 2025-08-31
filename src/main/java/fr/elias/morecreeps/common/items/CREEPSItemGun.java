package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.entity.proj.CREEPSEntityBullet;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CREEPSItemGun extends Item {

  public static Random rand = new Random();

  public CREEPSItemGun() {
    super();
    this.maxStackSize = 1;
    this.setMaxDamage(128);
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  @Override
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    world.playSoundAtEntity(
        entityplayer, "morecreeps:bullet", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

    if (!world.isRemote) {
      double d4 = 0.012999999999999999D;
      double d5 = 4D;
      CREEPSEntityBullet creepsentitybullet =
          new CREEPSEntityBullet(world, entityplayer, (float) (d4 + d5));

      if (creepsentitybullet != null) {
        itemstack.damageItem(2, entityplayer);
        world.spawnEntityInWorld(creepsentitybullet);
      }
    }

    return itemstack;
  }

  /** Returns True is the item is renderer in full 3D when hold. */
  @Override
  public boolean isFull3D() {
    return true;
  }
}
