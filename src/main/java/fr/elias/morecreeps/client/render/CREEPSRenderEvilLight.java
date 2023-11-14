package fr.elias.morecreeps.client.render;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelEvilLight;
import fr.elias.morecreeps.common.entity.CREEPSEntityEvilLight;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class CREEPSRenderEvilLight extends RenderLiving
{
	
    public CREEPSRenderEvilLight(CREEPSModelEvilLight creepsmodelevillight, float f)
    {
        super(creepsmodelevillight, f);
        shadowSize = 0.0F;
    }
    public ResourceLocation evil_glow = new ResourceLocation("morecreeps:textures/entity/evillightglow.png");
    protected int glow(CREEPSEntityEvilLight creepsentityevillight, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }

        if (i != 0)
        {
            return -1;
        }
        else
        {
            bindTexture(this.evil_glow);
            float f1 = (1.0F - creepsentityevillight.getBrightness(1.0F)) * 0.5F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }
    protected int shouldRenderPass(EntityLiving entityliving, int i, float f)
    {
        return glow((CREEPSEntityEvilLight)entityliving, i, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityEvilLight entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((CREEPSEntityEvilLight) entity);
	}
}
