package fr.elias.morecreeps.common.items;

import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBigBaby;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CREEPSItemBabyJarEmpty extends Item {

  private int healAmount;
  private boolean messagegiven;

  public CREEPSItemBabyJarEmpty() {
    super();
    this.healAmount = 10;
    this.maxStackSize = 1;
    this.messagegiven = false;
  }

  /**
   * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack,
   * world, entityPlayer
   */
  @Override
  public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
    if (!this.messagegiven) {
      boolean flag = false;
      List<?> list =
          world.getEntitiesWithinAABBExcludingEntity(
              entityplayer, entityplayer.boundingBox.expand(8D, 8D, 8D));

      for (int i = 0; i < list.size(); i++) {
        Entity entity = (Entity) list.get(i);

        if (entity instanceof CREEPSEntityBigBaby) {
          flag = true;
        }
      }

      if (!flag) {
        world.playSoundAtEntity(entityplayer, "morecreeps:babyjarempty", 1.0F, 1.0F);
        this.messagegiven = true;
      }
    }

    return itemstack;
  }
}
