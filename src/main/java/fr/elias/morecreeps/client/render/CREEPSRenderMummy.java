package fr.elias.morecreeps.client.render;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityMummy;

public class CREEPSRenderMummy extends RenderBiped {

    public CREEPSRenderMummy(ModelBiped modelbiped, float f) {
        super(modelbiped, f);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityMummy entity) {
        return new ResourceLocation(entity.texture);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((CREEPSEntityMummy) entity);
    }
}
