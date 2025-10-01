package fr.elias.morecreeps.common;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;

public class CREEPSExtendedPlayerProps implements IExtendedEntityProperties {

  public CREEPSExtendedPlayerProps() {}

  public static void register() {
    MinecraftForge.EVENT_BUS.register(new Handler());
    FMLCommonHandler.instance().bus().register(new Handler());
  }

  public int currentfine;

  public static final String PROP_NAME = Reference.MOD_ID + "_PlayerData";

  @Override
  public void saveNBTData(NBTTagCompound compound) {
    // TODO Auto-generated method stub

  }

  @Override
  public void loadNBTData(NBTTagCompound compound) {
    // TODO Auto-generated method stub

  }

  @Override
  public void init(Entity entity, World world) {
    // TODO Auto-generated method stub

  }

  public static class Handler {}
}
