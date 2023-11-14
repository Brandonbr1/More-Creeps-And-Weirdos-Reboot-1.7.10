package fr.elias.morecreeps.client.render;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelLawyerFromHell;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.CREEPSEntityLawyerFromHell;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class CREEPSRenderLawyerFromHell extends RenderLiving
{
    protected CREEPSModelLawyerFromHell modelBipedMain;

    public CREEPSRenderLawyerFromHell(CREEPSModelLawyerFromHell creepsmodellawyerfromhell, float f)
    {
        super(creepsmodellawyerfromhell, f);
        modelBipedMain = creepsmodellawyerfromhell;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        CREEPSEntityLawyerFromHell creepsentitylawyerfromhell = (CREEPSEntityLawyerFromHell)entityliving;
        modelBipedMain.modelsize = creepsentitylawyerfromhell.modelsize;
        fattenup((CREEPSEntityLawyerFromHell)entityliving, f);
    }

    protected void fattenup(CREEPSEntityLawyerFromHell creepsentitylawyerfromhell, float f)
    {
        GL11.glScalef(creepsentitylawyerfromhell.modelsize, creepsentitylawyerfromhell.modelsize, creepsentitylawyerfromhell.modelsize);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1)
    {
        super.doRender(entityliving, d, d1, d2, f, f1);
        float f2 = 1.6F;
        float f3 = 0.01666667F * f2;
        float f4 = entityliving.getDistanceToEntity(renderManager.livingPlayer);
        String s = "";
        int i = MoreCreepsAndWeirdos.instance.currentfine;

        if (i > 0)
        {
            s = (new StringBuilder()).append("\247cFINE: \2472$\247f").append(String.valueOf(i)).toString();
        }

        if (i >= 2500)
        {
            s = "\247cJAIL TIME!";
        }

        if (((CREEPSEntityLawyerFromHell)entityliving).undead)
        {
            s = "";
        }

        if (f4 < 20F && s.length() > 0)
        {
            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef((float)d + 0.0F, (float)d1 + 1.1F, (float)d2);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f3, -f3, f3);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.instance;
            float f5 = (1.0F - ((CREEPSEntityLawyerFromHell)entityliving).modelsize) * 9F;
            int j = -60 + (int)f5;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int k = fontrenderer.getStringWidth(s) / 2;
            tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            tessellator.addVertex(-k - 1, -1 + j, 0.0D);
            tessellator.addVertex(-k - 1, 8 + j, 0.0D);
            tessellator.addVertex(k + 1, 8 + j, 0.0D);
            tessellator.addVertex(k + 1, -1 + j, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j, 0x20ffffff);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        doRenderLiving((EntityLiving)entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityLawyerFromHell entity)
    {
		return new ResourceLocation(entity.texture);
	}

	protected ResourceLocation getEntityTexture(Entity entity) {

		return getEntityTexture((CREEPSEntityLawyerFromHell) entity);
	}
}
