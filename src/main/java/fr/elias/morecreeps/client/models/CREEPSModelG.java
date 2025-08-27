package fr.elias.morecreeps.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import cpw.mods.fml.client.FMLClientHandler;

public class CREEPSModelG extends ModelBase {

    public ModelRenderer bipedRightArm;
    public ModelRenderer bipedLeftArm;
    public ModelRenderer bipedRightLeg;
    public ModelRenderer bipedLeftLeg;
    public ModelRenderer bipedEars;
    public ModelRenderer bipedCloak;

    /**
     * Records whether the model should be rendered holding an item in the left hand, and if that item is a block.
     */
    public boolean heldItemLeft;

    /**
     * Records whether the model should be rendered holding an item in the right hand, and if that item is a block.
     */
    public boolean heldItemRight;
    public boolean isSneak;
    public ModelRenderer eyeL;
    public ModelRenderer g3;
    public ModelRenderer g5;
    public ModelRenderer g1;
    public ModelRenderer g2;
    public ModelRenderer g4;
    public ModelRenderer eyeR;
    public float modelsize;
    public float swingProgress;

    public CREEPSModelG() {
        this(0.0F);
    }

    public CREEPSModelG(float f) {
        this(f, 0.0F);
    }

    public CREEPSModelG(float f, float f1) {
        this.heldItemLeft = false;
        this.heldItemRight = false;
        this.isSneak = false;
        float f2 = 0.0F;
        this.eyeL = new ModelRenderer(this, 0, 0);
        this.eyeL.addBox(-3F, -17F, -2.5F, 3, 3, 1, f2);
        this.eyeL.setRotationPoint(0.0F, 8F, 0.0F);
        this.g3 = new ModelRenderer(this, 16, 6);
        this.g3.addBox(-8F, 0.0F, -2F, 16, 4, 4, f2);
        this.g3.setRotationPoint(0.0F, 8F, 0.0F);
        this.bipedLeftArm = new ModelRenderer(this, 56, 16);
        this.bipedLeftArm.addBox(-2F, -1F, -1F, 2, 8, 2, f2);
        this.bipedLeftArm.setRotationPoint(-8F, 8F, 0.0F);
        this.g5 = new ModelRenderer(this, 16, 6);
        this.g5.addBox(-8F, -16F, -2F, 16, 4, 4, f2);
        this.g5.setRotationPoint(0.0F, 8F, 0.0F);
        this.bipedRightLeg = new ModelRenderer(this, 0, 16);
        this.bipedRightLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, f2);
        this.bipedRightLeg.setRotationPoint(-3F, 12F, 0.0F);
        this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
        this.bipedLeftLeg.addBox(-2F, 0.0F, -2F, 4, 12, 4, f2);
        this.bipedLeftLeg.setRotationPoint(3F, 12F, 0.0F);
        this.g1 = new ModelRenderer(this, 16, 16);
        this.g1.addBox(0.0F, -7F, -2F, 8, 4, 4, f2);
        this.g1.setRotationPoint(0.0F, 8F, 0.0F);
        this.g2 = new ModelRenderer(this, 40, 16);
        this.g2.addBox(4F, -3F, -2F, 4, 3, 4, f2);
        this.g2.setRotationPoint(0.0F, 8F, 0.0F);
        this.g4 = new ModelRenderer(this, 40, 16);
        this.g4.addBox(-8F, -12F, -2F, 4, 12, 4, f2);
        this.g4.setRotationPoint(0.0F, 8F, 0.0F);
        this.bipedRightArm = new ModelRenderer(this, 56, 16);
        this.bipedRightArm.addBox(0.0F, -1F, -1F, 2, 8, 2, f2);
        this.bipedRightArm.setRotationPoint(8F, 8F, 0.0F);
        this.eyeR = new ModelRenderer(this, 0, 0);
        this.eyeR.addBox(1.0F, -17F, -2.5F, 3, 3, 1, f2);
        this.eyeR.setRotationPoint(0.0F, 8F, 0.0F);
        this.initializeSwingProgress();
    }

    private void initializeSwingProgress() {
        if (FMLClientHandler.instance() != null && FMLClientHandler.instance()
                .getClient() != null
                && FMLClientHandler.instance()
                .getClient().thePlayer != null) {
            this.swingProgress = FMLClientHandler.instance()
                    .getClient().thePlayer.swingProgress;
        } else {
            this.swingProgress = 0.0F;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5);
        this.eyeL.render(f5);
        this.g3.render(f5);
        this.bipedLeftArm.render(f5);
        this.g5.render(f5);
        this.bipedRightLeg.render(f5);
        this.bipedLeftLeg.render(f5);
        this.g1.render(f5);
        this.g2.render(f5);
        this.g4.render(f5);
        this.bipedRightArm.render(f5);
        this.eyeR.render(f5);
    }

    /**
     * Sets the models various rotation angles.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
        this.bipedRightArm.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2.0F * f1 * 0.5F;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(f * 0.6662F) * 2.0F * f1 * 0.5F;
        this.bipedRightArm.rotateAngleZ = 0.0F;
        this.bipedLeftArm.rotateAngleZ = 0.0F;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
        this.bipedRightLeg.rotateAngleY = 0.0F;
        this.bipedLeftLeg.rotateAngleY = 0.0F;

        if (this.isRiding) {
            this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
            this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
            this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
            this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
        }

        if (this.heldItemLeft) {
            this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        if (this.heldItemRight) {
            this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
        }

        this.bipedRightArm.rotateAngleY = 0.0F;
        this.bipedLeftArm.rotateAngleY = 0.0F;

        if (this.swingProgress > -9990F) {
            float f6 = this.swingProgress;
            f6 = 1.0F - this.swingProgress;
            f6 *= f6;
            f6 *= f6;
            f6 = 1.0F - f6;
            float f7 = MathHelper.sin(f6 * (float) Math.PI);
            this.bipedRightArm.rotateAngleZ = MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
        }

        if (this.isSneak) {
            this.bipedRightLeg.rotateAngleX -= 0.0F;
            this.bipedLeftLeg.rotateAngleX -= 0.0F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4F;
            this.bipedLeftLeg.rotationPointZ = 4F;
            this.bipedRightLeg.rotationPointY = 9F;
            this.bipedLeftLeg.rotationPointY = 9F;
        } else {
            this.bipedRightLeg.rotationPointZ = 0.0F;
            this.bipedLeftLeg.rotationPointZ = 0.0F;
            this.bipedRightLeg.rotationPointY = 12F;
            this.bipedLeftLeg.rotationPointY = 12F;
        }

        this.bipedRightArm.rotateAngleZ += MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(f2 * 0.09F) * 0.05F + 0.05F;
        this.bipedRightArm.rotateAngleX += MathHelper.sin(f2 * 0.067F) * 0.05F;
        this.bipedLeftArm.rotateAngleX -= MathHelper.sin(f2 * 0.067F) * 0.05F;
    }

    public void renderEars(float f) {
        this.bipedEars.rotationPointX = 0.0F;
        this.bipedEars.rotationPointY = 0.0F;
        this.bipedEars.render(f);
    }

    public void renderCloak(float f) {
        this.bipedCloak.render(f);
    }
}
