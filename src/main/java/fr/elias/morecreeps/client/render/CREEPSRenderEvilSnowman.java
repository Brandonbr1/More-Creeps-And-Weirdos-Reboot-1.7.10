package fr.elias.morecreeps.client.render;

import fr.elias.morecreeps.client.models.CREEPSModelEvilSnowman;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilSnowman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CREEPSRenderEvilSnowman extends RenderLiving {

  protected CREEPSModelEvilSnowman modelBipedMain;

  public CREEPSRenderEvilSnowman(CREEPSModelEvilSnowman creepsmodelevilsnowman, float f) {
    super(creepsmodelevilsnowman, f);
    this.modelBipedMain = creepsmodelevilsnowman;
  }

  protected void preRenderScale(CREEPSEntityEvilSnowman creepsentityevilsnowman, float f) {
    this.shadowSize = creepsentityevilsnowman.snowsize * 0.5F;
    GL11.glScalef(
        creepsentityevilsnowman.snowsize,
        creepsentityevilsnowman.snowsize,
        creepsentityevilsnowman.snowsize);
  }

  @Override
  protected void preRenderCallback(EntityLivingBase entityliving, float f) {
    this.preRenderScale((CREEPSEntityEvilSnowman) entityliving, f);
  }

  protected ResourceLocation getEntityTexture(CREEPSEntityEvilSnowman entity) {
    return new ResourceLocation(entity.texture);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {
    return this.getEntityTexture((CREEPSEntityEvilSnowman) entity);
  }
}
