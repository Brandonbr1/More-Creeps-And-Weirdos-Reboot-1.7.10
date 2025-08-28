package fr.elias.morecreeps.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fr.elias.morecreeps.client.models.CREEPSModelCastleKing;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleKing;

public class CREEPSRenderCastleKing extends RenderLiving {

    protected CREEPSModelCastleKing modelcastlekingmain;

    public CREEPSRenderCastleKing(CREEPSModelCastleKing creepsmodelcastleking, float f) {
        super(creepsmodelcastleking, f);
        this.modelcastlekingmain = creepsmodelcastleking;
    }

    protected void fattenup(CREEPSEntityCastleKing creepsentitycastleking, float f) {
        GL11.glScalef(2.0F, 1.5F, 2.0F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        CREEPSEntityCastleKing creepsentitycastleking = (CREEPSEntityCastleKing) entityliving;
        this.modelcastlekingmain.hammerswing = creepsentitycastleking.hammerswing;
        this.fattenup((CREEPSEntityCastleKing) entityliving, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityCastleKing entity) {
        return new ResourceLocation(entity.texture);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return this.getEntityTexture((CREEPSEntityCastleKing) entity);
    }
}
