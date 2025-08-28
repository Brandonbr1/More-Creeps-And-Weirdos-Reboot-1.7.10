package fr.elias.morecreeps.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelAtom;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityAtom;

public class CREEPSRenderAtom extends RenderLiving {

    private ModelBase scaleAmount;
    public float sparkle;
    protected CREEPSModelAtom modelBipedMain;

    public CREEPSRenderAtom(CREEPSModelAtom creepsmodelatom, float f) {
        super(creepsmodelatom, f);
        modelBipedMain = creepsmodelatom;
        scaleAmount = creepsmodelatom;
    }

    public void doRenderLayer(CREEPSEntityAtom p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_,
        float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        func_179_a(p_177141_1_, 2, 0F);
    }

    protected int func_179_a(CREEPSEntityAtom creepsentityatom, int i, float f) {
        if (i == 1) {
            Random random = new Random(10842L);
            sparkle += random.nextFloat();

            if (sparkle > 30F) {
                sparkle = 0.0F;
            }

            sparkle = random.nextInt(30);
            float f1 = sparkle;
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(new ResourceLocation("armor/power.png"));
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            float f2 = f1 * 0.01F;
            float f3 = f1 * 0.01F;
            GL11.glTranslatef(f2, f3, 0.0F);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glEnable(GL11.GL_BLEND);
            float f4 = 0.5F;
            GL11.glColor4f(f4, f4, f4, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            return 1;
        }

        if (i == 2) {
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
        }

        return -1;
    }

    protected int func_179_aaaaaaaaaaa(CREEPSEntityAtom creepsentityatom, int i, float f) {
        if (i == 0) {
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        }

        if (i == 1) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        return -1;
    }

    /**
     * sets the scale for the slime based on getSlimeSize in EntitySlime
     */
    protected void scaleSlime(CREEPSEntityAtom creepsentityatom, float f) {
        GL11.glScalef(
            creepsentityatom.atomsize * 0.3F,
            creepsentityatom.atomsize * 0.3F,
            creepsentityatom.atomsize * 0.3F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        scaleSlime((CREEPSEntityAtom) entityliving, f);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        shadowSize = 0.0F;
        doRender((EntityLivingBase) entity, d, d1, d2, f, f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return new ResourceLocation(Reference.MOD_ID, Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ATOM);
    }
}
