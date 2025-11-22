package fr.elias.morecreeps.client.render;

import java.util.Random;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelGooGoat;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityGooGoat;

public class CREEPSRenderGooGoat extends RenderLiving {

    private ModelBase scaleAmount;
    protected CREEPSModelGooGoat modelBipedMain;
    public static Random rand = new Random();

    public CREEPSRenderGooGoat(CREEPSModelGooGoat creepsmodelgoogoat, float f) {
        super(creepsmodelgoogoat, f);
        this.modelBipedMain = creepsmodelgoogoat;
        this.scaleAmount = creepsmodelgoogoat;
    }

    protected int shouldRenderPass(CREEPSEntityGooGoat creepsentitygoogoat, int i, float f) {
        if (i == 0) {
            this.setRenderPassModel(this.scaleAmount);
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        } else {
            if (i == 1) {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        return -1;
    }

    /** sets the scale for the slime based on getSlimeSize in EntitySlime */
    protected void scaleSlime(CREEPSEntityGooGoat creepsentitygoogoat, float f) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_BLEND);
        // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glScalef(creepsentitygoogoat.getSlimeSize(), creepsentitygoogoat.getSlimeSize(), creepsentitygoogoat.getSlimeSize() + 0.5F);
        GL11.glPopMatrix();
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered.
     * Args: entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        this.scaleSlime((CREEPSEntityGooGoat) entityliving, f);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f) {
        return this.shouldRenderPass((CREEPSEntityGooGoat) entityliving, i, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityGooGoat entity) {
        return new ResourceLocation(entity.texture);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return this.getEntityTexture((CREEPSEntityGooGoat) entity);
    }
}
