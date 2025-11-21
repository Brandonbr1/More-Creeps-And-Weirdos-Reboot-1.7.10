package fr.elias.morecreeps.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBlackSoul;

public class CREEPSRenderBlackSoul extends RenderLiving {

    public static ResourceLocation layer_texture = new ResourceLocation("morecreeps:textures/entity/blacksoulglow.png");
    public CREEPSRenderBlackSoul blackSoulRender;
    ResourceLocation eyeglow = new ResourceLocation(
        Reference.MOD_ID,
        Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_BLACK_SOUL_EYES);

    public CREEPSRenderBlackSoul(ModelBiped modelbiped, float f) {
        super(new ModelBiped(), 1.0F);
    }

    public void doRenderLayerBlackSoul(CREEPSEntityBlackSoul p_177141_1_, float p_177141_2_, float p_177141_3_,
        float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_) {
        this.bindTexture(layer_texture);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

        if (p_177141_1_.isInvisible()) {
            GL11.glDepthMask(false);
        } else {
            GL11.glDepthMask(true);
        }

        char c0 = 61680;
        int i = c0 % 65536;
        int j = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i / 1.0F, (float) j / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        // this.blackSoulRender.getMainModel().render(p_177141_1_, p_177141_2_, p_177141_3_,
        // p_177141_5_, p_177141_6_,
        // p_177141_7_, p_177141_8_);
        int k = p_177141_1_.getBrightnessForRender(p_177141_4_);
        i = k % 65536;
        j = k / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) i / 1.0F, (float) j / 1.0F);
        // this.blackSoulRender.func_177105_a(p_177141_1_, p_177141_4_);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA);
    }

    protected int eyeGlow(CREEPSEntityBlackSoul creepsentityblacksoul, int i, float f) {
        if (i != 0) {
            return -1;
        }

        if (i != 0) {
            return -1;
        } else {
            this.bindTexture(eyeglow);
            float f1 = (1.0F - creepsentityblacksoul.getBrightness(1.0F)) * 0.5F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
            return 1;
        }
    }

    protected void fattenup(CREEPSEntityBlackSoul creepsentityblacksoul, float f) {
        GL11.glScalef(
            creepsentityblacksoul.modelsize,
            creepsentityblacksoul.modelsize,
            creepsentityblacksoul.modelsize);
    }

    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        fattenup((CREEPSEntityBlackSoul) entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityBlackSoul entity) {

        return entity.texture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return getEntityTexture((CREEPSEntityBlackSoul) entity);
    }
}
