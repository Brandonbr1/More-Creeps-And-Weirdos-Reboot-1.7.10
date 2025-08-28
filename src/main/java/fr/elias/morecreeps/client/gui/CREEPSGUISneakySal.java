package fr.elias.morecreeps.client.gui;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRatMan;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;

public class CREEPSGUISneakySal extends GuiScreen {

    private CREEPSEntitySneakySal sneakysal;
    private GuiTextField namescreen;
    private boolean field_28217_m;
    private float xSize_lo;
    private float ySize_lo;
    public int playercash;
    public static float saleprice;
    public static Random rand = new Random();
    protected int xSize;
    protected int ySize;
    private RenderItem itemRender;

    public CREEPSGUISneakySal(CREEPSEntitySneakySal creepsentitysneakysal) {
        this.sneakysal = creepsentitysneakysal;
        this.xSize = 512;
        this.ySize = 512;
    }


}
