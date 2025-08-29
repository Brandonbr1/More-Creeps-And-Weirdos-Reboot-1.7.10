package fr.elias.morecreeps.client.particles;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public class CREEPSFxSmoke extends EntityFX {

  public CREEPSFxSmoke(
      World world, double d, double d1, double d2, float f, float particleGravity) {
    super(world, d, d1, d2, 0.0D, 0.0D, 0.0D);
    setSize(0.7F, 0.7F);
    particleRed = 1.0F;
    particleBlue = 1.0F;
    particleGreen = 1.0F;
    this.particleGravity = particleGravity;
    particleScale *= f;
  }

  public int getFXLayer() {
    return 1;
  }

  public void renderParticle(
      Tessellator p_70539_1_,
      float p_70539_2_,
      float p_70539_3_,
      float p_70539_4_,
      float p_70539_5_,
      float p_70539_6_,
      float p_70539_7_) {
    float f6 = (float) this.particleTextureIndexX / 16.0F;
    float f7 = f6 + 0.01560938F;
    float f8 = (float) this.particleTextureIndexY / 16.0F;
    float f9 = f8 + 0.01560938F;
    float f10 = 0.1F * this.particleScale;

    if (this.particleIcon != null) {
      f6 = this.particleIcon.getMinU();
      f7 = this.particleIcon.getMaxU();
      f8 = this.particleIcon.getMinV();
      f9 = this.particleIcon.getMaxV();
    }

    float f11 =
        (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) p_70539_3_ - interpPosX);
    float f12 =
        (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) p_70539_3_ - interpPosY);
    float f13 =
        (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) p_70539_3_ - interpPosZ);
    p_70539_1_.setColorRGBA_F(
        this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
    p_70539_1_.addVertexWithUV(
        f11 - p_70539_4_ * f10 - p_70539_7_ * f10,
        f12 - p_70539_5_ * f10,
        f13 - p_70539_6_ * f10 * f10,
        f7,
        f9);
    p_70539_1_.addVertexWithUV(
        f11 - p_70539_4_ * f10 + p_70539_7_ * f10,
        f12 + p_70539_5_ * f10,
        f13 - p_70539_6_ * f10 * f10,
        f7,
        f8);
    p_70539_1_.addVertexWithUV(
        f11 + p_70539_4_ * f10 + p_70539_7_ * f10,
        f12 + p_70539_5_ * f10,
        f13 + p_70539_6_ * f10 * f10,
        f6,
        f8);
    p_70539_1_.addVertexWithUV(
        f11 + p_70539_4_ * f10 - p_70539_7_ * f10,
        f12 - p_70539_5_ * f10,
        f13 + p_70539_6_ * f10 * f10,
        f6,
        f9);
  }
}
