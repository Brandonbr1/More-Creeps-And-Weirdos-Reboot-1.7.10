package fr.elias.morecreeps.client.render;

import fr.elias.morecreeps.client.models.CREEPSModelRocketGiraffe;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CREEPSRenderRocketGiraffe extends RenderLiving {

  public CREEPSRenderRocketGiraffe(CREEPSModelRocketGiraffe creepsmodelrocketgiraffe, float f) {
    super(creepsmodelrocketgiraffe, f);
  }

  protected void fattenup(CREEPSEntityRocketGiraffe creepsentityrocketgiraffe, float f) {
    GL11.glScalef(
        creepsentityrocketgiraffe.modelsize,
        creepsentityrocketgiraffe.modelsize,
        creepsentityrocketgiraffe.modelsize);
  }

  /**
   * Allows the render to do any OpenGL state modifications necessary before the model is rendered.
   * Args: entityLiving, partialTickTime
   */
  protected void preRenderCallback(EntityLivingBase entityliving, float f) {
    fattenup((CREEPSEntityRocketGiraffe) entityliving, f);
  }

  public void doRenderLiving(
      EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
    super.doRender(entityliving, d, d1, d2, f, f1);
    fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe e =
        (fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe) entityliving;
    String s = e.name;
    if (s != null && s.length() > 0 && e.tamed) {
      this.func_147906_a(entityliving, "\2476" + s, d, d1 + 1.1D, d2, 64);
    }
  }

  public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
    doRenderLiving((EntityLiving) entity, d, d1, d2, f, f1);
  }

  protected ResourceLocation getEntityTexture(CREEPSEntityRocketGiraffe entity) {
    return new ResourceLocation(entity.texture);
  }

  protected ResourceLocation getEntityTexture(Entity entity) {

    return getEntityTexture((CREEPSEntityRocketGiraffe) entity);
  }
}
