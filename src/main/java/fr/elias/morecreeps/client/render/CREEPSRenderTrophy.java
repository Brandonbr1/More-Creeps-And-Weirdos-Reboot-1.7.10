package fr.elias.morecreeps.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import fr.elias.morecreeps.client.models.CREEPSModelTrophy;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;

public class CREEPSRenderTrophy extends RenderLiving {

    protected CREEPSModelTrophy modelBipedMain;

    public CREEPSRenderTrophy(CREEPSModelTrophy creepsmodeltrophy, float f) {
        super(creepsmodeltrophy, f);
        modelBipedMain = creepsmodeltrophy;
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityTrophy entity) {
        return new ResourceLocation(entity.texture);
    }

    protected ResourceLocation getEntityTexture(Entity entity) {

        return getEntityTexture((CREEPSEntityTrophy) entity);
    }
}
