package fr.elias.morecreeps.client.gui.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import cpw.mods.fml.common.network.IGuiHandler;
import fr.elias.morecreeps.client.gui.*;
import fr.elias.morecreeps.client.gui.container.GenericContainer;
import fr.elias.morecreeps.common.entity.*;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityZebra;

public class CREEPSGuiHandler implements IGuiHandler {

    private CREEPSEntityHotdog hotdog;
    private CREEPSEntityGuineaPig guineapig;
    private CREEPSEntityRocketGiraffe rocketgiraffe;
    private CREEPSEntitySneakySal sneakysal;
    private CREEPSEntityZebra zebra;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == 1)
            return new GenericContainer();

        if (ID == 2)
            // name hotdog
            return new GenericContainer();

        if (ID == 3)
            return new GenericContainer();

        if (ID == 4)
            return new GenericContainer();

        if (ID == 5)
            return new GenericContainer();

        if (ID == 6)
            return new GenericContainer();

        if (ID == 7)
            return new GenericContainer();

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (ID == 1)
            return new CREEPSGUICamelname();

        if (ID == 2)
            // name hotdog
            return new CREEPSGUIHotdog(this.hotdog);

        if (ID == 3)
            // gpig stats
            return new CREEPSGUIGuineaPigTraining(this.guineapig);

        if (ID == 4)
            // name gpig
            return new CREEPSGUIGuineaPig(this.guineapig);

        if (ID == 5)
            // name giraffe
            return new CREEPSGUIGiraffename(this.rocketgiraffe);

        if (ID == 6)
            return new CREEPSGUISneakySal(this.sneakysal);

        if (ID == 7)
            return new CREEPSGUIZebraname(this.zebra);

        return null;
    }
}
