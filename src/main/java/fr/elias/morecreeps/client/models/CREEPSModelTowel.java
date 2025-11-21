package fr.elias.morecreeps.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CREEPSModelTowel extends ModelBase {

    public ModelRenderer body;

    public CREEPSModelTowel() {
        this(0.0F);
    }

    public CREEPSModelTowel(float f) {
        this(f, 0.0F);
    }

    public CREEPSModelTowel(float f, float f1) {

        this.body = new ModelRenderer(this, 0, 0);
        this.body.addBox(-9.0F, 0.0F, -13.0F, 18, 1, 25, 0.0F);
        this.body.setRotationPoint(0.0F, 23.0F, 0.0F);
    }

    /** Sets the models various rotation angles then renders the model. */
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.body.render(f5);
    }
}
