package fr.elias.morecreeps.client.render;

import java.util.Random;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.elias.morecreeps.client.models.CREEPSModelRobotTed;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRobotTed;

public class CREEPSRenderRobotTed extends RenderLiving {

    public static Random rand = new Random();
    protected CREEPSModelRobotTed modelBipedMain;
    public float sparkle;

    public CREEPSRenderRobotTed(CREEPSModelRobotTed creepsmodelrobotted, float f) {
        super(creepsmodelrobotted, f);
        this.modelBipedMain = creepsmodelrobotted;
    }

    protected int func_179_a(CREEPSEntityRobotTed creepsentityrobotted, int i, float f) {
        if (creepsentityrobotted.hurtTime > 0) {
            if (i == 1) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glColor4f(1.0F, 0.5F, 0.5F, 0.5F);
                return 1;
            }

            if (i == 2) {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        return -1;
    }

    /** sets the scale for the slime based on getSlimeSize in EntitySlime */
    protected void scaleSlime(CREEPSEntityRobotTed creepsentityrobotted, float f) {
        GL11.glScalef(creepsentityrobotted.robotsize, creepsentityrobotted.robotsize, creepsentityrobotted.robotsize);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered.
     * Args: entityLiving, partialTickTime
     */
    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        this.scaleSlime((CREEPSEntityRobotTed) entityliving, f);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f) {
        return this.func_179_a((CREEPSEntityRobotTed) entityliving, i, f);
    }

    public void doRenderTed(CREEPSEntityRobotTed creepsentityrobotted, double d, double d1, double d2, float f,
        float f1) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        // mainModel.swingProgress = getSwingProgress(creepsentityrobotted, f1);
        this.mainModel.isRiding = creepsentityrobotted.isRiding();

        try {
            float f2 = creepsentityrobotted.prevRenderYawOffset
                + (creepsentityrobotted.renderYawOffset - creepsentityrobotted.prevRenderYawOffset) * f1;
            float f3 = creepsentityrobotted.prevRotationYaw
                + (creepsentityrobotted.rotationYaw - creepsentityrobotted.prevRotationYaw) * f1;
            float f4 = creepsentityrobotted.prevRotationPitch
                + (creepsentityrobotted.rotationPitch - creepsentityrobotted.prevRotationPitch) * f1;
            this.renderLivingAt(creepsentityrobotted, d, d1, d2);
            float f5 = this.handleRotationFloat(creepsentityrobotted, f1);
            this.rotateCorpse(creepsentityrobotted, f5, f2, f1);
            float f6 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1F, -1F, 1.0F);
            this.preRenderCallback(creepsentityrobotted, f1);
            GL11.glTranslatef(
                0.0F,
                -24F * f6 - 0.0078125F,
                0.0F); /*
                        * float f7 = creepsentityrobotted.field_705_Q + (creepsentityrobotted.field_704_R -
                        * creepsentityrobotted.field_705_Q) * f1;
                        * float f8 = creepsentityrobotted.field_703_S - creepsentityrobotted.field_704_R * (1.0F - f1);
                        * if (f7 > 1.0F)
                        * {
                        * f7 = 1.0F;
                        * }
                        * //loadDownloadableImageTexture(creepsentityrobotted.skinUrl,
                        * creepsentityrobotted.getTexture());
                        */
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            // mainModel.setLivingAnimations(creepsentityrobotted, f8, f7, f1);
            this.mainModel.render(creepsentityrobotted, f2, f3, f5, f3 - f2, f4, f6);

            for (int i = 0; i < 4; i++) {
                this.renderPassModel.render(creepsentityrobotted, f2, f3, f5, f3 - f2, f4, f6);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            float f9 = creepsentityrobotted.getBrightness(f1);
            int j = this.getColorMultiplier(creepsentityrobotted, f9, f1);

            if ((j >> 24 & 0xff) > 0 || creepsentityrobotted.hurtTime > 0 || creepsentityrobotted.deathTime > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if ((j >> 24 & 0xff) > 0) {
                    float f10 = (j >> 16 & 0xff) / 255F;
                    float f11 = (j >> 8 & 0xff) / 255F;
                    float f12 = (j & 0xff) / 255F;
                    float f13 = (j >> 24 & 0xff) / 255F;
                    GL11.glColor4f(f10, f11, f12, f13);
                    // mainModel.render(creepsentityrobotted, f8, f7, f5, f3 - f2, f4, f6);
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        this.passSpecialRender(creepsentityrobotted, d, d1, d2);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        super.doRender(entityliving, d, d1, d2, f, f1);
        // super.doRender(entityliving, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityRobotTed entity) {
        return new ResourceLocation(entity.texture);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return this.getEntityTexture((CREEPSEntityRobotTed) entity);
    }
}
