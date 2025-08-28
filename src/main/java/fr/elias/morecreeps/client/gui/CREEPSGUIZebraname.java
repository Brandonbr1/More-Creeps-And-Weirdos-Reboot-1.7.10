package fr.elias.morecreeps.client.gui;

import java.util.Random;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.entity.nice.CREEPSEntityZebra;

public class CREEPSGUIZebraname extends GuiScreen {

    private final CREEPSEntityZebra zebra;
    private GuiTextField namescreen;
    private boolean field_28217_m;
    protected int xSize = 256;
    protected int ySize = 180;

    public CREEPSGUIZebraname(CREEPSEntityZebra creepsentityzebra) {
        zebra = creepsentityzebra;
    }

    @Override
    public void updateScreen() {
        namescreen.updateCursorCounter();
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 62 + 12, I18n.format("gui.cancel")));
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 42, "Save"));
        namescreen = new GuiTextField(fontRendererObj, width / 2 - 100, height / 4 + 10, 200, 20);
        namescreen.setMaxStringLength(31);
        namescreen.setCanLoseFocus(true);
        namescreen.setText(zebra.name != null ? zebra.name : "");
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }

        if (guibutton.id == 1) {
            mc.displayGuiScreen(null);
            return;
        }

        if (guibutton.id == 0) {
            if (field_28217_m) {
                return;
            }

            field_28217_m = true;
            String s = namescreen.getText();
            zebra.name = s;
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        namescreen.textboxKeyTyped(c, i);

        if (c == '\r') {
            actionPerformed((GuiButton) buttonList.get(0));
        }

        if (i == 1) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        namescreen.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(new ResourceLocation("morecreeps:textures/gui/gui-screen.png"));
        int l = (width - xSize) / 2;
        int i1 = (height - (ySize + 16)) / 2;
        drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);
        drawCenteredString(fontRendererObj, "N A M E   Y O U R   Z E B R A", width / 2, (height / 4 - 40) + 30, 0xffffff);
        namescreen.drawTextBox();
        super.drawScreen(i, j, f);
    }
}
