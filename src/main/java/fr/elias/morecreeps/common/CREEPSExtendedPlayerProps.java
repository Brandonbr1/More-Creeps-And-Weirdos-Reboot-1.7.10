package fr.elias.morecreeps.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CREEPSExtendedPlayerProps implements IExtendedEntityProperties {

    public CREEPSExtendedPlayerProps() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Handler());
        FMLCommonHandler.instance()
            .bus()
            .register(new Handler());
    }

    public static final String PROP_NAME = Reference.MOD_ID + "_PlayerData";

    public int currentfine;
    public static final String MONEY_KEY = "CREEPS_MONEY";

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound propertyData = new NBTTagCompound();

        propertyData.setInteger(MONEY_KEY, currentfine);

        compound.setTag(PROP_NAME, propertyData);

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey(PROP_NAME, Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound propertyData = compound.getCompoundTag(PROP_NAME);

            propertyData.getInteger(MONEY_KEY);
        }

    }

    @Override
    public void init(Entity entity, World world) {

    }

    public static CREEPSExtendedPlayerProps get(Entity p) {
        return (CREEPSExtendedPlayerProps) p.getExtendedProperties(PROP_NAME);
    }

    public static class Handler {

        @SubscribeEvent
        public void onEntityConstructing(EntityConstructing event) {
            if (event.entity instanceof EntityPlayer) {
                if (event.entity.getExtendedProperties(PROP_NAME) == null) {
                    event.entity.registerExtendedProperties(PROP_NAME, new CREEPSExtendedPlayerProps());
                }

            }
        }
    }

}
