package fr.elias.morecreeps.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class CREEPSModelLawyerFromHell extends ModelBase {

    public ModelRenderer headLawyer;
    public ModelRenderer body;
    public ModelRenderer rightarm;
    public ModelRenderer leftarm;
    public ModelRenderer rightleg;
    public ModelRenderer leftleg;
    public ModelRenderer casehandle;
    public ModelRenderer case2;
    public ModelRenderer glasses;
    public ModelRenderer watch1;
    public ModelRenderer watch2;
    public ModelRenderer shoeL;
    public ModelRenderer shoeR;
    public float modelsize;

    public CREEPSModelLawyerFromHell() {
        this(0.0F);
    }

    public CREEPSModelLawyerFromHell(float f) {
        this(f, 0.0F);
    }

    public CREEPSModelLawyerFromHell(float f, float f1) {
        float f2 = 0.0F;

        this.headLawyer = new ModelRenderer(this, 0, 0);
        this.headLawyer.addBox(-4F, -10F, -4F, 8, 8, 8, f2);
        this.headLawyer.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.body = new ModelRenderer(this, 16, 16);
        this.body.addBox(-3F, 0.0F, -2F, 6, 12, 4, f2);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.rightarm = new ModelRenderer(this, 40, 16);
        this.rightarm.addBox(-3F, -2F, -2F, 3, 12, 4, f2);
        this.rightarm.setRotationPoint(-3F, 2.0F, 0.0F);
        this.leftarm = new ModelRenderer(this, 40, 16);
        this.leftarm.addBox(-1F, -1F, -2F, 3, 12, 4, f2);
        this.leftarm.setRotationPoint(4F, 1.0F, 0.0F);
        this.rightleg = new ModelRenderer(this, 0, 16);
        this.rightleg.addBox(-2F, 0.0F, -2F, 3, 12, 4, f2);
        this.rightleg.setRotationPoint(-1F, 12F, 0.0F);
        this.leftleg = new ModelRenderer(this, 0, 16);
        this.leftleg.addBox(-2F, 0.0F, -2F, 3, 12, 4, f2);
        this.leftleg.setRotationPoint(2.0F, 12F, 0.0F);
        this.casehandle = new ModelRenderer(this, 32, 2);
        this.casehandle.addBox(0.0F, 9.25F, -3F, 1, 3, 6, f2);
        this.casehandle.setRotationPoint(4F, 1.0F, 0.0F);
        this.case2 = new ModelRenderer(this, 40, 0);
        this.case2.addBox(0.0F, 12.5F, -6F, 1, 5, 11, 1.0F);
        this.case2.setRotationPoint(4F, 1.0F, 0.0F);
        this.glasses = new ModelRenderer(this, 26, 0);
        this.glasses.addBox(-4F, -7F, -5.5F, 8, 2, 1, f2);
        this.glasses.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.watch1 = new ModelRenderer(this, 16, 2);
        this.watch1.addBox(-3.5F, 7F, -2.5F, 4, 1, 5, f2);
        this.watch1.setRotationPoint(-3F, 2.0F, 0.0F);
        this.watch2 = new ModelRenderer(this, 45, 0);
        this.watch2.addBox(-4F, 7F, -1F, 1, 1, 1, f2);
        this.watch2.setRotationPoint(-3F, 2.0F, 0.0F);
        this.shoeR = new ModelRenderer(this, 0, 0);
        this.shoeR.addBox(-2F, 11F, -5F, 3, 1, 3, f2);
        this.shoeR.setRotationPoint(-1F, 12F, 0.0F);
        this.shoeL = new ModelRenderer(this, 0, 0);
        this.shoeL.addBox(-2F, 11F, -5F, 3, 1, 3, f2);
        this.shoeL.setRotationPoint(2.0F, 12F, 0.0F);
    }

    /** Sets the models various rotation angles then renders the model. */
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5);
        this.headLawyer.render(f5);
        this.body.render(f5);
        this.rightarm.render(f5);
        this.leftarm.render(f5);
        this.rightleg.render(f5);
        this.leftleg.render(f5);
        this.casehandle.render(f5);
        this.case2.render(f5);
        this.glasses.render(f5);
        this.watch1.render(f5);
        this.watch2.render(f5);
        this.shoeL.render(f5);
        this.shoeR.render(f5);
    }

    /** Sets the models various rotation angles. */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        this.glasses.rotateAngleY = this.headLawyer.rotateAngleY = f3 / (180F / (float) Math.PI);
        this.glasses.rotateAngleX = this.headLawyer.rotateAngleX = f4 / (180F / (float) Math.PI);
        this.watch1.rotateAngleX = this.watch2.rotateAngleX = this.rightarm.rotateAngleX = MathHelper.cos(
            f * 0.6662F + (float) Math.PI) * 2.0F * f1 * 0.5F;
        this.case2.rotateAngleX = this.casehandle.rotateAngleX = this.leftarm.rotateAngleX = MathHelper.cos(f * 0.6662F)
            * 2.0F
            * f1
            * 0.5F;
        this.watch1.rotateAngleZ = this.watch2.rotateAngleZ = this.rightarm.rotateAngleZ = 0.0F;
        this.case2.rotateAngleZ = this.casehandle.rotateAngleZ = this.leftarm.rotateAngleZ = 0.0F;
        this.shoeR.rotateAngleX = this.rightleg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.shoeL.rotateAngleX = this.leftleg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.rightleg.rotateAngleY = 0.0F;
        this.leftleg.rotateAngleY = 0.0F;
        this.rightarm.rotateAngleY = 0.0F;
        this.leftarm.rotateAngleY = 0.0F;
        this.watch1.rotateAngleZ = this.watch2.rotateAngleZ = this.rightarm.rotateAngleZ += MathHelper.cos(f2 * 0.09F)
            * 0.05F + 0.05F;
        this.case2.rotateAngleZ = this.casehandle.rotateAngleZ = this.leftarm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F)
            * 0.05F + 0.05F;
        this.watch1.rotateAngleX = this.watch2.rotateAngleX = this.rightarm.rotateAngleX += MathHelper.sin(f2 * 0.067F)
            * 0.05F;
        this.case2.rotateAngleX = this.casehandle.rotateAngleX = this.leftarm.rotateAngleX -= MathHelper
            .sin(f2 * 0.067F) * 0.05F;
    }
}
