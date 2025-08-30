package fr.elias.morecreeps.client.gui;

import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import fr.elias.morecreeps.common.packets.TameableNamePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class CREEPSGUIGiraffename extends GuiScreen {

  private CREEPSEntityRocketGiraffe giraffe;
  private GuiTextField namescreen;
  private boolean field_28217_m;
  protected int xSize;
  protected int ySize;
  public static final ResourceLocation guiTexture =
      new ResourceLocation("morecreeps:textures/gui/gui-screen.png");

  public CREEPSGUIGiraffename(CREEPSEntityRocketGiraffe giraffe) {
    this.giraffe = giraffe;
    this.xSize = 256;
    this.ySize = 180;
  }

  @Override
  public void updateScreen() {
    if (this.namescreen != null) {
      this.namescreen.updateCursorCounter();
    }
  }

  @Override
  public void initGui() {
    Keyboard.enableRepeatEvents(true);
    this.buttonList.clear();
    this.buttonList.add(
        new GuiButton(
            1,
            this.width / 2 - 100,
            this.height / 4 + 52 + 12,
            I18n.format("gui.cancel", new Object[0])));
    this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 32, "Save"));
    this.namescreen =
        new GuiTextField(this.fontRendererObj, this.width / 2 - 100, this.height / 4, 200, 20);
    this.namescreen.setMaxStringLength(31);
    this.namescreen.setCanLoseFocus(true);
    this.namescreen.setText(this.giraffe != null ? this.giraffe.getTamedName() : "");
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void actionPerformed(GuiButton guibutton) {
    if (guibutton == null || !guibutton.enabled) return;

    if (guibutton.id == 1) { // cancel
      this.mc.displayGuiScreen(null);
      return;
    }

    if (guibutton.id == 0) {
      if (this.field_28217_m) return;

      String s = this.namescreen.getText();
      if (s == null || s.trim().isEmpty()) {
        return;
      }

      this.field_28217_m = true;
      if (this.giraffe == null) return;
      MoreCreepsAndWeirdos.packetHandler.sendToServer(
          new TameableNamePacket(this.giraffe.getEntityId(), s));
      this.mc.displayGuiScreen(null);
    }
  }

  @Override
  protected void keyTyped(char c, int i) {
    if (this.namescreen != null) {
      this.namescreen.textboxKeyTyped(c, i);
    }
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
    if (this.namescreen != null) {
      this.namescreen.mouseClicked(i, j, k);
    }
  }

  @Override
  public void drawScreen(int i, int j, float f) {
    this.drawDefaultBackground();
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.renderEngine.bindTexture(guiTexture);
    int l = (this.width - this.xSize) / 2;
    int i1 = (this.height - (this.ySize + 16)) / 2;
    this.drawTexturedModalRect(l, i1, 0, 0, this.xSize, this.ySize);
    this.drawCenteredString(
        this.fontRendererObj,
        "NAME YOUR ROCKET GIRAFFE",
        this.width / 2,
        (this.height / 4 - 40) + 20,
        0xffffff);
    if (this.namescreen != null) {
      this.namescreen.drawTextBox();
    }
    super.drawScreen(i, j, f);
  }
}
