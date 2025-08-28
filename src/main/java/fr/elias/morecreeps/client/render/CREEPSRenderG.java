package fr.elias.morecreeps.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelG;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityG;

public class CREEPSRenderG extends RenderLiving {

    protected CREEPSModelG modelBipedMain;

    public CREEPSRenderG(CREEPSModelG creepsmodelg, float f) {
        super(creepsmodelg, f);
        this.modelBipedMain = creepsmodelg;
    }

    protected void fattenup(CREEPSEntityG creepsentityg, float f) {
        GL11.glScalef(creepsentityg.modelsize, creepsentityg.modelsize, creepsentityg.modelsize);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        this.fattenup((CREEPSEntityG) entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityG entity) {
        return entity.texture;
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        super.doRender(entityliving, d, d1, d2, f, f1);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return this.getEntityTexture((CREEPSEntityG) entity);
    }
}
