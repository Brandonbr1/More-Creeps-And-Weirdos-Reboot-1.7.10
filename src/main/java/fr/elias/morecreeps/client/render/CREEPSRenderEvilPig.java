package fr.elias.morecreeps.client.render;

import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilPig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CREEPSRenderEvilPig extends RenderLiving {
  private static final ResourceLocation TEXTURE =
      new ResourceLocation("morecreeps:textures/entity/evilpig.png");

  public CREEPSRenderEvilPig(ModelBase model, float shadow) {
    super(model, shadow);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {
    return TEXTURE;
  }

  @Override
  protected void preRenderCallback(EntityLivingBase entity, float partialTick) {
    if (entity instanceof CREEPSEntityEvilPig) {
      float s = ((CREEPSEntityEvilPig) entity).modelsize;
      GL11.glScalef(s, s, s);
    }
  }
}
