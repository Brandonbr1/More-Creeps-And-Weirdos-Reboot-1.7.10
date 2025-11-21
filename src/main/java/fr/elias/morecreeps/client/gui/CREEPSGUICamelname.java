package fr.elias.morecreeps.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityCamel;
import fr.elias.morecreeps.common.packets.TameableNamePacket;

public class CREEPSGUICamelname extends GuiScreen {

    // TODO: IMPLEMENT PACKETS
    private CREEPSEntityCamel camel;
    private GuiTextField namescreen;
    private boolean field_28217_m;
    protected int xSize;
    protected int ySize;

    public static ResourceLocation guiTexture = new ResourceLocation("morecreeps:textures/gui/gui-camelname.png");

    public CREEPSGUICamelname(CREEPSEntityCamel creepsentitycamel) {
        this.camel = creepsentitycamel;
        this.xSize = 256;
        this.ySize = 180;
    }

    /** Called from the main game loop to update the screen. */
    @Override
    public void updateScreen() {
        this.namescreen.updateCursorCounter();
    }

    /** Adds the buttons (and other controls) to the screen in question. */
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(
            new GuiButton(
                1,
                this.width / 2 - 100,
                this.height / 4 + 62 + 12,
                I18n.format("gui.cancel", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 42, "Save"));
        this.namescreen = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 10, 200, 20);
        this.namescreen.setMaxStringLength(31);
        this.namescreen.setCanLoseFocus(true);
        this.namescreen.setText(this.camel.getTamedName() != null ? this.camel.getTamedName() : "");
    }

    /** Called when the screen is unloaded. Used to disable keyboard repeat events */
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of
     * ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton guibutton) {

        if (!guibutton.enabled) return;

        if (guibutton.id == 1) {
            this.mc.displayGuiScreen(null);
            return;
        }

        if (guibutton.id == 0) {
            if (this.field_28217_m) return;

            String s = this.namescreen.getText();
            if (s == null || s.trim()
                .isEmpty()) {
                return;
            }

            this.field_28217_m = true;
            if (this.camel == null) return;
            MoreCreepsAndWeirdos.packetHandler.sendToServer(new TameableNamePacket(this.camel.getEntityId(), s));
            this.mc.displayGuiScreen(null);
        }
    }

    /** Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e). */
    @Override
    protected void keyTyped(char c, int i) {
        this.namescreen.textboxKeyTyped(c, i);

        if (c == '\r') {
            this.actionPerformed((GuiButton) this.buttonList.get(0));
        }

        if (i == 1) {
            this.mc.displayGuiScreen(null);
            return;
        } else return;
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.namescreen.mouseClicked(i, j, k);
    }

    /** Draws the screen and all the components in it. */
    @Override
    public void drawScreen(int i, int j, float f) {
        this.drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(guiTexture);
        int l = (this.width - this.xSize) / 2;
        int i1 = (this.height - (this.ySize + 16)) / 2;
        this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
        this.namescreen.drawTextBox();
        super.drawScreen(i, j, f);
    }
}
