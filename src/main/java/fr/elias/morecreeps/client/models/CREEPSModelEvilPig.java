package fr.elias.morecreeps.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class CREEPSModelEvilPig extends ModelBase {

  public ModelRenderer body;
  public ModelRenderer headEvilpig;
  public ModelRenderer leg1;
  public ModelRenderer leg2;
  public ModelRenderer leg3;
  public ModelRenderer leg4;
  public ModelRenderer headEvilpig1;
  public ModelRenderer headEvilpig2;
  public ModelRenderer headEvilpig3;
  public ModelRenderer headEvilpig4;
  public ModelRenderer headEvilpig5;

  // Base Z positions (from your constructor boxes)
  private final float baseZ0 = -10F;
  private final float baseZ1 = -13F;
  private final float baseZ2 = -10F;
  private final float baseZ3 = -6F;
  private final float baseZ4 = -9F;
  private final float baseZ5 = -6F;

  public CREEPSModelEvilPig() {
    byte byte0 = 6;

    headEvilpig = new ModelRenderer(this, 0, 0);
    headEvilpig.addBox(-16F, -4F, -10F, 8, 8, 8);
    headEvilpig.setRotationPoint(0.0F, 23 - byte0, -6F);

    headEvilpig1 = new ModelRenderer(this, 0, 0);
    headEvilpig1.addBox(-4F, -4F, -13F, 8, 8, 8);
    headEvilpig1.setRotationPoint(0.0F, 23 - byte0, -6F);

    headEvilpig2 = new ModelRenderer(this, 0, 0);
    headEvilpig2.addBox(8F, -4F, -10F, 8, 8, 8);
    headEvilpig2.setRotationPoint(0.0F, 23 - byte0, -6F);

    headEvilpig3 = new ModelRenderer(this, 0, 0);
    headEvilpig3.addBox(-16F, -4F, -6F, 8, 8, 8);
    headEvilpig3.setRotationPoint(0.0F, 12 - byte0, -6F);

    headEvilpig4 = new ModelRenderer(this, 0, 0);
    headEvilpig4.addBox(-4F, -4F, -9F, 8, 8, 8);
    headEvilpig4.setRotationPoint(0.0F, 12 - byte0, -6F);

    headEvilpig5 = new ModelRenderer(this, 0, 0);
    headEvilpig5.addBox(8F, -4F, -6F, 8, 8, 8);
    headEvilpig5.setRotationPoint(0.0F, 12 - byte0, -6F);

    body = new ModelRenderer(this, 20, 8);
    body.addBox(-10F, -10F, -7F, 20, 16, 12);
    body.setRotationPoint(0.0F, 17 - byte0, 2.0F);

    leg1 = new ModelRenderer(this, 0, 16);
    leg1.addBox(-2F, 0.0F, -2F, 4, byte0, 4);
    leg1.setRotationPoint(-8F, 24 - byte0, 7F);

    leg2 = new ModelRenderer(this, 0, 16);
    leg2.addBox(-2F, 0.0F, -2F, 4, byte0, 4);
    leg2.setRotationPoint(8F, 24 - byte0, 7F);

    leg3 = new ModelRenderer(this, 0, 16);
    leg3.addBox(-2F, 0.0F, -2F, 4, byte0, 4);
    leg3.setRotationPoint(-8F, 24 - byte0, -5F);

    leg4 = new ModelRenderer(this, 0, 16);
    leg4.addBox(-2F, 0.0F, -2F, 4, byte0, 4);
    leg4.setRotationPoint(8F, 24 - byte0, -5F);
  }

  /** Sets the models various rotation angles then renders the model. */
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
    setRotationAngles(f, f1, f2, f3, f4, f5);
    body.render(f5);
    headEvilpig.render(f5);
    leg1.render(f5);
    leg2.render(f5);
    leg3.render(f5);
    leg4.render(f5);
    headEvilpig1.render(f5);
    headEvilpig2.render(f5);
    headEvilpig3.render(f5);
    headEvilpig4.render(f5);
    headEvilpig5.render(f5);
  }

  /** Sets the models various rotation angles. */
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {
    // Body & legs: unchanged vanilla-like animation
    body.rotateAngleX = ((float) Math.PI / 2F);
    leg1.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;
    leg2.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
    leg3.rotateAngleX = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * f1;
    leg4.rotateAngleX = MathHelper.cos(f * 0.6662F) * 1.4F * f1;

    // Smooth, consistent head yaw/pitch (all heads follow the same look)
    final float yawRad   = f3 * ((float)Math.PI / 180F);
    final float pitchRad = -f4 * ((float)Math.PI / 180F);
    headEvilpig.rotateAngleY  = yawRad; headEvilpig.rotateAngleX  = pitchRad;
    headEvilpig1.rotateAngleY = yawRad; headEvilpig1.rotateAngleX = pitchRad;
    headEvilpig2.rotateAngleY = yawRad; headEvilpig2.rotateAngleX = pitchRad;
    headEvilpig3.rotateAngleY = yawRad; headEvilpig3.rotateAngleX = pitchRad;
    headEvilpig4.rotateAngleY = yawRad; headEvilpig4.rotateAngleX = pitchRad;
    headEvilpig5.rotateAngleY = yawRad; headEvilpig5.rotateAngleX = pitchRad;

    // --- Jitter fix: replace per-frame RNG “teleport” with smooth tweened pop-out ---
    // f2 == ageInTicks (ticksExisted + partialTicks) from RenderLivingBase
    // Cycle which head is extended every ~8 ticks, with a sine ease for smooth in/out.
    final float cycle   = f2 * 0.125F;                 // slower cycle
    final int activeIdx = MathHelper.floor_float(cycle) % 6;
    final float phase   = cycle - MathHelper.floor_float(cycle);
    final float extend  = MathHelper.sin(phase * (float)Math.PI) * 3.0F; // 0..3 blocks (matches your +/-3 deltas)

    // Reset to base, then extend the active head only.
    headEvilpig.rotationPointZ  = baseZ0 - (activeIdx == 0 ? extend : 0F);
    headEvilpig1.rotationPointZ = baseZ1 - (activeIdx == 1 ? extend : 0F);
    headEvilpig2.rotationPointZ = baseZ2 - (activeIdx == 2 ? extend : 0F);
    headEvilpig3.rotationPointZ = baseZ3 - (activeIdx == 3 ? extend : 0F);
    headEvilpig4.rotationPointZ = baseZ4 - (activeIdx == 4 ? extend : 0F);
    headEvilpig5.rotationPointZ = baseZ5 - (activeIdx == 5 ? extend : 0F);
  }
}
