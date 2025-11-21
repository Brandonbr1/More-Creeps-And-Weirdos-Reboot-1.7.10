package fr.elias.morecreeps.client.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import fr.elias.morecreeps.client.models.CREEPSModelTowel;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityTowel;

public class CREEPSRenderTowel extends RenderLiving {

    protected CREEPSModelTowel modelBipedMain;

    public CREEPSRenderTowel(CREEPSModelTowel creepsmodeltowel, float f) {
        super(creepsmodeltowel, f);
        this.modelBipedMain = creepsmodeltowel;
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityTowel entity) {
        switch (entity.textureindex) {
            case 0:
                return new ResourceLocation("morecreeps:textures/entity/towel0.png");
            case 1:
                return new ResourceLocation("morecreeps:textures/entity/towel1.png");
            case 2:
                return new ResourceLocation("morecreeps:textures/entity/towel2.png");
            case 3:
                return new ResourceLocation("morecreeps:textures/entity/towel3.png");
            case 4:
                return new ResourceLocation("morecreeps:textures/entity/towel4.png");
            case 5:
                return new ResourceLocation("morecreeps:textures/entity/towel5.png");
            case 6:
                return new ResourceLocation("morecreeps:textures/entity/towel6.png");
            case 7:
                return new ResourceLocation("morecreeps:textures/entity/towel7.png");
        }
        return new ResourceLocation(entity.texture);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {

        return this.getEntityTexture((CREEPSEntityTowel) entity);
    }
}
