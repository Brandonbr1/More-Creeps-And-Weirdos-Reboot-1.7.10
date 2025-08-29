package fr.elias.morecreeps.client.render;

import fr.elias.morecreeps.client.models.CREEPSModelCastleCritter;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleCritter;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CREEPSRenderCastleCritter extends RenderLiving {

  public static ResourceLocation eye_glow =
      new ResourceLocation("morecreeps:textures/entity/castlecritterglow.png");
  public CREEPSRenderCastleCritter castleCritterRender;

  protected CREEPSModelCastleCritter modelBipedMain;

  public CREEPSRenderCastleCritter(CREEPSModelCastleCritter creepsmodelcastlecritter, float f) {
    super(creepsmodelcastlecritter, f);
    this.modelBipedMain = creepsmodelcastlecritter;
  }

  protected int eyeGlow(CREEPSEntityCastleCritter creepsentitycastlecritter, int i, float f) {
    if (i != 0) return -1;

    if (i != 0) return -1;
    else {
      this.bindTexture(CREEPSRenderCastleCritter.eye_glow);
      float f1 = (1.0F - creepsentitycastlecritter.getBrightness(1.0F)) * 0.5F;
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glDisable(GL11.GL_ALPHA_TEST);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
      return 1;
    }
  }

  protected void fattenup(CREEPSEntityCastleCritter creepsentitycastlecritter, float f) {
    GL11.glScalef(
        creepsentitycastlecritter.modelsize,
        creepsentitycastlecritter.modelsize,
        creepsentitycastlecritter.modelsize);
  }

  @Override
  protected void preRenderCallback(EntityLivingBase entityliving, float f) {
    this.fattenup((CREEPSEntityCastleCritter) entityliving, f);
  }

  protected int shouldRenderPass(EntityLiving entityliving, int i, float f) {
    return this.eyeGlow((CREEPSEntityCastleCritter) entityliving, i, f);
  }

  protected ResourceLocation getEntityTexture(CREEPSEntityCastleCritter entity) {
    return new ResourceLocation(entity.texture);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {

    return this.getEntityTexture((CREEPSEntityCastleCritter) entity);
  }
}
