package fr.elias.morecreeps.client.gui.handler;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import fr.elias.morecreeps.client.gui.*;
import fr.elias.morecreeps.client.gui.container.GenericContainer;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityCamel;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityZebra;

public class CREEPSGuiHandler implements IGuiHandler {

    public int guiCooldown = 0; // Avoid gui get reopened after gui is closed

    public enum GuiType {

        CAMEL_NAME(1),
        HOTDOG(2),
        GUINEA_TRAIN(3),
        GUINEA(4),
        GIRAFFE_NAME(5),
        SNEAKY_SAL(6),
        ZEBRA_NAME(7);

        public final int id;

        GuiType(int id) {
            this.id = id;
        }

        public static GuiType fromId(int id) {
            for (GuiType t : values()) {
                if (t.id == id) return t;
            }
            return null;
        }
    }

    public boolean tryOpenGui(int ID, EntityPlayer player, World world, int x) {
        if (world != null && !world.isRemote && this.guiCooldown == 0) {
            player.openGui(MoreCreepsAndWeirdos.INSTANCE, ID, world, x, 0, 0);
            this.guiCooldown = 10;
            return true;
        }
        return false;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiType type = GuiType.fromId(ID);
        if (type == null) return null;
        switch (type) {
            case CAMEL_NAME:
            case HOTDOG:
            case GUINEA_TRAIN:
            case GUINEA:
            case GIRAFFE_NAME:
            case SNEAKY_SAL:
            case ZEBRA_NAME:
                return new GenericContainer();
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiType type = GuiType.fromId(ID);
        if (type == null) return null;

        // x Containing the entity id
        Entity entity = world.getEntityByID(x);
        if (entity == null) return null;

        switch (type) {
            case CAMEL_NAME:
                if (entity instanceof CREEPSEntityCamel) {
                    return new CREEPSGUICamelname((CREEPSEntityCamel) entity);
                }
                break;
            case HOTDOG:
                if (entity instanceof CREEPSEntityHotdog) {
                    return new CREEPSGUIHotdog((CREEPSEntityHotdog) entity);
                }
                break;
            case GUINEA_TRAIN:
                if (entity instanceof CREEPSEntityGuineaPig) {
                    return new CREEPSGUIGuineaPigTraining((CREEPSEntityGuineaPig) entity);
                }
                break;
            case GUINEA:
                if (entity instanceof CREEPSEntityGuineaPig) {
                    return new CREEPSGUIGuineaPig((CREEPSEntityGuineaPig) entity);
                }
                break;
            case GIRAFFE_NAME:
                if (entity instanceof CREEPSEntityRocketGiraffe) {
                    return new CREEPSGUIGiraffename((CREEPSEntityRocketGiraffe) entity);
                }
                break;
            case SNEAKY_SAL:
                if (entity instanceof CREEPSEntitySneakySal) {
                    return new CREEPSGUISneakySal((CREEPSEntitySneakySal) entity);
                }
                break;
            case ZEBRA_NAME:
                if (entity instanceof CREEPSEntityZebra) {
                    return new CREEPSGUIZebraname((CREEPSEntityZebra) entity);
                }
                break;
            default:
                return null;
        }
        return null;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        if (this.guiCooldown > 0) {
            this.guiCooldown--;
        }
    }
}
