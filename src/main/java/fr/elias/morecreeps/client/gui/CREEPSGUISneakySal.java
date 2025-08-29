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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRatMan;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;

@SideOnly(Side.CLIENT)
public class CREEPSGUISneakySal extends GuiScreen {

    private CREEPSEntitySneakySal sneakysal;
    private GuiTextField namescreen;
    private boolean field_28217_m;
    private float xSize_lo;
    private float ySize_lo;
    public int playercash;
    public float saleprice;
    public static Random rand = new Random();
    protected int xSize;
    protected int ySize;
    // private RenderItem itemRender;

    public CREEPSGUISneakySal(CREEPSEntitySneakySal creepsentitysneakysal) {
        this.sneakysal = creepsentitysneakysal;
        this.xSize = 512;
        this.ySize = 512;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        byte byte0 = -18;
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;
        world.playSoundAtEntity(entityplayersp, "morecreeps:salgreeting", 1.0F, 1.0F);

        this.saleprice = this.sneakysal.saleprice;
        assert this.sneakysal != null;
        this.buttonList.add(
                new GuiButton(
                        2,
                        this.width / 2 - 170,
                        this.height / 4 + 8 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[0]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[0]]));
        this.buttonList.add(
                new GuiButton(
                        3,
                        this.width / 2 + 2,
                        this.height / 4 + 8 + byte0,
                        155,
                        20,
                        "   \2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[1]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[1]]));
        this.buttonList.add(
                new GuiButton(
                        4,
                        this.width / 2 - 170,
                        this.height / 4 + 35 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[2]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[2]]));
        this.buttonList.add(
                new GuiButton(
                        5,
                        this.width / 2 + 2,
                        this.height / 4 + 35 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[3]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[3]]));
        this.buttonList.add(
                new GuiButton(
                        6,
                        this.width / 2 - 170,
                        this.height / 4 + 65 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[4]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[4]]));
        this.buttonList.add(
                new GuiButton(
                        7,
                        this.width / 2 + 2,
                        this.height / 4 + 65 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[5]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[5]]));
        this.buttonList.add(
                new GuiButton(
                        8,
                        this.width / 2 - 170,
                        this.height / 4 + 95 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[6]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[6]]));
        this.buttonList.add(
                new GuiButton(
                        9,
                        this.width / 2 + 2,
                        this.height / 4 + 95 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[7]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[7]]));
        this.buttonList.add(
                new GuiButton(
                        10,
                        this.width / 2 - 170,
                        this.height / 4 + 125 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[8]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[8]]));
        this.buttonList.add(
                new GuiButton(
                        11,
                        this.width / 2 + 2,
                        this.height / 4 + 125 + byte0,
                        155,
                        20,
                        "\2472    $\2476" +
                                Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[9]] * this.saleprice) +
                                "\247f " +
                                CREEPSEntitySneakySal.saldescriptions[this.sneakysal.salslots[9]]));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 158 + byte0, 98, 20, "RIPOFF SAL"));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 2, this.height / 4 + 158 + byte0, 98, 20, "DONE"));
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        char c = '\260';
        char c1 = '\246';
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
        int j = (this.width - c) / 2;
        int k = (this.height - c1) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, (int) this.xSize_lo, (int) this.ySize_lo);
        drawEntityOnScreen(
                j + 51,
                k + 75,
                30,
                (float) (j + 51) - mouseX,
                (float) (k + 75 - 50) - mouseY,
                this.mc.thePlayer);
    }

    public static void drawEntityOnScreen(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_,
            float p_147046_4_, EntityLivingBase p_147046_5_) {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef(p_147046_0_, p_147046_1_, 50.0F);
        GL11.glScalef((-p_147046_2_), p_147046_2_, p_147046_2_);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p_147046_5_.renderYawOffset;
        float f3 = p_147046_5_.rotationYaw;
        float f4 = p_147046_5_.rotationPitch;
        float f5 = p_147046_5_.prevRotationYawHead;
        float f6 = p_147046_5_.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float) Math.atan(p_147046_3_ / 40.0F) * 20.0F;
        p_147046_5_.rotationYaw = (float) Math.atan(p_147046_3_ / 40.0F) * 40.0F;
        p_147046_5_.rotationPitch = -((float) Math.atan(p_147046_4_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = RenderManager.instance;
        // Unknown for 1.7.10
        /*
         * rendermanager.setPlayerViewY(180.0F);
         * rendermanager.setRenderShadow(false);
         */
        rendermanager.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        // rendermanager.setRenderShadow(true);
        p_147046_5_.renderYawOffset = f2;
        p_147046_5_.rotationYaw = f3;
        p_147046_5_.rotationPitch = f4;
        p_147046_5_.prevRotationYawHead = f5;
        p_147046_5_.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        // TODO Check if above is appropriate for below
        // GlStateManager.disableRescaleNormal();
        GL13.glActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_2D);
        GL13.glActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;

        if (!guibutton.enabled)
            return;

        if (guibutton.id == 1) {
            this.mc.displayGuiScreen(null);
            return;
        }

        if (guibutton.id == 0) {
            this.sneakysal.dissedmax--;

            if (rand.nextInt(9) == 0) {
                world.playSoundAtEntity(
                        entityplayersp,
                        "mob.chickenplop",
                        1.0F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
                int i = rand.nextInt(15) + 1;

                switch (i) {
                    case 1:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.armygem, 1);
                        break;

                    case 2:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.horseheadgem, 1);
                        break;

                    case 3:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.bandaid, 1);
                        break;

                    case 4:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.shrinkray, 1);
                        break;

                    case 5:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.extinguisher, 1);
                        break;

                    case 6:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.growray, 1);
                        break;

                    case 7:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.frisbee, 1);
                        break;

                    case 8:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.lifegem, 1);
                        break;

                    case 9:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.gun, 1);
                        break;

                    case 10:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.raygun, 1);
                        break;

                    default:
                        this.sneakysal.dropItem(MoreCreepsAndWeirdos.bandaid, 1);
                        break;
                }

                this.mc.displayGuiScreen(null);
                return;
            }

            for (int j = 0; j < rand.nextInt(15) + 5; j++) {
                double d = -MathHelper.sin((this.sneakysal.rotationYaw * (float) Math.PI) / 180F);
                double d1 = MathHelper.cos((this.sneakysal.rotationYaw * (float) Math.PI) / 180F);
                CREEPSEntityRatMan creepsentityratman = new CREEPSEntityRatMan(world);
                creepsentityratman.setLocationAndAngles(
                        (this.sneakysal.posX + d * 1.0D + rand.nextInt(4)) - 2D,
                        this.sneakysal.posY - 1.0D,
                        (this.sneakysal.posZ + d1 * 1.0D + rand.nextInt(4)) - 2D,
                        this.sneakysal.rotationYaw,
                        0.0F);
                creepsentityratman.motionY = 1.0D;
                world.spawnEntityInWorld(creepsentityratman);
            }

            world.playSoundAtEntity(entityplayersp, "morecreeps:salrats", 1.0F, 1.0F);
            this.mc.displayGuiScreen(null);
            return;
        }

        int k = guibutton.id;

        if (k > 1 && k < 12) {
            k -= 2;
            CREEPSEntitySneakySal _tmp = this.sneakysal;
            int l = Math.round(CREEPSEntitySneakySal.salprices[this.sneakysal.salslots[k]] * this.saleprice);
            this.playercash = this.checkCash();

            if (this.playercash < l) {
                world.playSoundAtEntity(entityplayersp, "morecreeps:salnomoney", 1.0F, 1.0F);
            } else {
                this.removeCash(l);
                CREEPSEntitySneakySal _tmp1 = this.sneakysal;
                this.sneakysal.dropItem(CREEPSEntitySneakySal.salitems[this.sneakysal.salslots[k]], 1);
                world.playSoundAtEntity(entityplayersp, "morecreeps:salsale", 1.0F, 1.0F);
            }
        }
    }

    public boolean removeCash(int i) {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        Object obj = null;
        ItemStack aitemstack[] = ((EntityPlayer) (entityplayersp)).inventory.mainInventory;
        boolean flag = false;
        label0:

            for (int j = 0; j < aitemstack.length; j++) {
                ItemStack itemstack = aitemstack[j];

                if (itemstack == null || itemstack.getItem() != MoreCreepsAndWeirdos.money) {
                    continue;
                }

                do {
                    if (itemstack.stackSize <= 0 || i <= 0) {
                        continue label0;
                    }

                    i--;

                    if (itemstack.stackSize - 1 == 0) {
                        ((EntityPlayer) (entityplayersp)).inventory.mainInventory[j] = null;
                        continue label0;
                    }

                    itemstack.stackSize--;
                } while (true);
            }

        return true;
    }

    public int checkCash() {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;
        Object obj = null;
        ItemStack aitemstack[] = ((EntityPlayer) (entityplayersp)).inventory.mainInventory;
        int i = 0;

        for (ItemStack itemstack : aitemstack) {
            if (itemstack != null && itemstack.getItem() == MoreCreepsAndWeirdos.money) {
                i += itemstack.stackSize;
            }
        }

        return i;
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawWorldBackground(0);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(new ResourceLocation("morecreeps:textures/gui/gui-screensal.png"));
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - (this.ySize + 16)) / 2;
        this.drawTexturedModalRect(20, 20, 0, 0, this.xSize + 400, this.ySize);
        byte byte0 = -18;
        boolean flag = false;
        this.playercash = this.checkCash();
        this.drawCenteredString(
                this.fontRendererObj,
                "\2475******* \247fWELCOME TO SAL'S SHOP \2475*******",
                this.width / 2,
                this.height / 4 - 40,
                0xffffff);
        this.drawCenteredString(
                this.fontRendererObj,
                (new StringBuilder()).append("\247eYour cash : \2472$\2476 ")
                .append(String.valueOf(this.playercash))
                .toString(),
                this.width / 2,
                this.height / 4 - 25,
                0xffffff);

        for (int j1 = 0; j1 < 5; j1++) {
            this.zLevel = 200F;
            super.itemRender.zLevel = 200F;
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            CREEPSEntitySneakySal _tmp = this.sneakysal;
            GuiContainer.itemRender.renderItemIntoGUI(
                    this.fontRendererObj,
                    this.mc.getTextureManager(),
                    CREEPSEntitySneakySal.itemstack[this.sneakysal.salslots[j1 * 2]],
                    this.width / 2 - 160,
                    this.height / 4 + 8 + byte0 + j1 * 30);
            CREEPSEntitySneakySal _tmp1 = this.sneakysal;
            GuiContainer.itemRender.renderItemIntoGUI(
                    this.fontRendererObj,
                    this.mc.getTextureManager(),
                    CREEPSEntitySneakySal.itemstack[this.sneakysal.salslots[j1 * 2 + 1]],
                    this.width / 2 + 12,
                    this.height / 4 + 8 + byte0 + j1 * 30);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
        }

        super.drawScreen(i, j, f);
        this.xSize_lo = i;
        this.ySize_lo = j;
    }

}