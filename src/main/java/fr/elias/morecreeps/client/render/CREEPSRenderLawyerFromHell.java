package fr.elias.morecreeps.client.render;

import com.mojang.authlib.GameProfile;
import fr.elias.morecreeps.client.models.CREEPSModelLawyerFromHell;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityLawyerFromHell;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

public class CREEPSRenderLawyerFromHell extends RenderLiving {

  protected CREEPSModelLawyerFromHell modelBipedMain;

  public CREEPSRenderLawyerFromHell(CREEPSModelLawyerFromHell creepsmodellawyerfromhell, float f) {
    super(creepsmodellawyerfromhell, f);
    this.modelBipedMain = creepsmodellawyerfromhell;
  }

  /**
   * Allows the render to do any OpenGL state modifications necessary before the model is rendered.
   * Args: entityLiving, partialTickTime
   */
  protected void preRenderCallback(EntityLiving entityliving, float f) {
    CREEPSEntityLawyerFromHell creepsentitylawyerfromhell =
        (CREEPSEntityLawyerFromHell) entityliving;
    this.modelBipedMain.modelsize = creepsentitylawyerfromhell.modelsize;
    this.fattenup((CREEPSEntityLawyerFromHell) entityliving, f);
  }

  protected void fattenup(CREEPSEntityLawyerFromHell creepsentitylawyerfromhell, float f) {
    GL11.glScalef(
        creepsentitylawyerfromhell.modelsize,
        creepsentitylawyerfromhell.modelsize,
        creepsentitylawyerfromhell.modelsize);
  }

  public void doRenderLiving(
      EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
    super.doRender(entityliving, d, d1, d2, f, f1);
    float f2 = 1.6F;
    float f3 = 0.01666667F * f2;
    float f4 = entityliving.getDistanceToEntity(this.renderManager.livingPlayer);
    String s = "";
    int i = MoreCreepsAndWeirdos.INSTANCE.currentfine;

    if (i > 0) {
      s =
          (new StringBuilder())
              .append("\247cFINE: \2472$\247f")
              .append(String.valueOf(i))
              .toString();
    }

    if (i >= 2500) {
      s = "\247cJAIL TIME!";
    }

    if (((CREEPSEntityLawyerFromHell) entityliving).undead) {
      s = "";
    }

    if (f4 < 20F && s.length() > 0) {
      FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
      GL11.glPushMatrix();
      GL11.glTranslatef((float) d + 0.0F, (float) d1 + 1.1F, (float) d2);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-f3, -f3, f3);
      GL11.glDisable(GL11.GL_LIGHTING);
      GL11.glDepthMask(false);
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      GL11.glEnable(GL11.GL_BLEND);
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      Tessellator tessellator = Tessellator.instance;
      float f5 = (1.0F - ((CREEPSEntityLawyerFromHell) entityliving).modelsize) * 9F;
      int j = -60 + (int) f5;
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      tessellator.startDrawingQuads();
      int k = fontrenderer.getStringWidth(s) / 2;
      tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
      tessellator.addVertex(-k - 1, -1 + j, 0.0D);
      tessellator.addVertex(-k - 1, 8 + j, 0.0D);
      tessellator.addVertex(k + 1, 8 + j, 0.0D);
      tessellator.addVertex(k + 1, -1 + j, 0.0D);
      tessellator.draw();
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j, 0x20ffffff);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      GL11.glDepthMask(true);
      fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, j, -1);
      GL11.glEnable(GL11.GL_LIGHTING);
      GL11.glDisable(GL11.GL_BLEND);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
    }
  }

  /**
   * Actually renders the given argument. This is a synthetic bridge method, always casting down its
   * argument and then handing it off to a worker function which does the actual work. In all
   * probabilty, the class Render is generic (Render<T extends Entity) and this method has signature
   * public void doRender(T entity, double d, double d1, double d2, float f, float f1). But JAD is
   * pre 1.5 so doesn't do that.
   */
  @Override
  public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
    this.doRenderLiving((EntityLiving) entity, d, d1, d2, f, f1);
  }

  protected ResourceLocation getEntityTexture(CREEPSEntityLawyerFromHell entity) {
    return new ResourceLocation(entity.texture);
  }

  @Override
  protected ResourceLocation getEntityTexture(Entity entity) {

    return this.getEntityTexture((CREEPSEntityLawyerFromHell) entity);
  }

  protected void renderEquippedItems(EntityLiving p_77029_1_, float p_77029_2_) {
    GL11.glColor3f(1.0F, 1.0F, 1.0F);
    super.renderEquippedItems(p_77029_1_, p_77029_2_);
    ItemStack itemstack = p_77029_1_.getHeldItem();
    ItemStack itemstack1 = p_77029_1_.func_130225_q(3);
    Item item;
    float f1;

    if (itemstack1 != null) {
      GL11.glPushMatrix();
      this.modelBipedMain.headLawyer.postRender(0.0625F);
      //  this.modelBipedMain.bipedHead.postRender(0.0625F);
      item = itemstack1.getItem();

      net.minecraftforge.client.IItemRenderer customRenderer =
          net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(
              itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
      boolean is3D =
          (customRenderer != null
              && customRenderer.shouldUseRenderHelper(
                  net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,
                  itemstack1,
                  net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

      if (item instanceof ItemBlock) {
        if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())) {
          f1 = 0.625F;
          GL11.glTranslatef(0.0F, -0.25F, 0.0F);
          GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
          GL11.glScalef(f1, -f1, -f1);
        }

        this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack1, 0);
      } else if (item == Items.skull) {
        f1 = 1.0625F;
        GL11.glScalef(f1, -f1, -f1);
        GameProfile gameprofile = null;

        if (itemstack1.hasTagCompound()) {
          NBTTagCompound nbttagcompound = itemstack1.getTagCompound();

          if (nbttagcompound.hasKey("SkullOwner", 10)) {
            gameprofile = NBTUtil.func_152459_a(nbttagcompound.getCompoundTag("SkullOwner"));
          } else if (nbttagcompound.hasKey("SkullOwner", 8)
              && !StringUtils.isNullOrEmpty(nbttagcompound.getString("SkullOwner"))) {
            gameprofile = new GameProfile((UUID) null, nbttagcompound.getString("SkullOwner"));
          }
        }

        TileEntitySkullRenderer.field_147536_b.func_152674_a(
            -0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), gameprofile);
      }

      GL11.glPopMatrix();
    }

    if (itemstack != null && itemstack.getItem() != null) {
      item = itemstack.getItem();
      GL11.glPushMatrix();

      if (this.mainModel.isChild) {
        f1 = 0.5F;
        GL11.glTranslatef(0.0F, 0.625F, 0.0F);
        GL11.glRotatef(-20.0F, -1.0F, 0.0F, 0.0F);
        GL11.glScalef(f1, f1, f1);
      }

      this.modelBipedMain.rightarm.postRender(0.0625F);
      // this.modelBipedMain.bipedRightArm.postRender(0.0625F);
      GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

      net.minecraftforge.client.IItemRenderer customRenderer =
          net.minecraftforge.client.MinecraftForgeClient.getItemRenderer(
              itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
      boolean is3D =
          (customRenderer != null
              && customRenderer.shouldUseRenderHelper(
                  net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,
                  itemstack,
                  net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

      if (item instanceof ItemBlock
          && (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType()))) {
        f1 = 0.5F;
        GL11.glTranslatef(0.0F, 0.1875F, -0.3125F);
        f1 *= 0.75F;
        GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
      } else if (item == Items.bow) {
        f1 = 0.625F;
        GL11.glTranslatef(0.0F, 0.125F, 0.3125F);
        GL11.glRotatef(-20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(f1, -f1, f1);
        GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      } else if (item.isFull3D()) {
        f1 = 0.625F;

        if (item.shouldRotateAroundWhenRendering()) {
          GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
          GL11.glTranslatef(0.0F, -0.125F, 0.0F);
        }

        this.func_82422_c();
        GL11.glScalef(f1, -f1, f1);
        GL11.glRotatef(-100.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
      } else {
        f1 = 0.375F;
        GL11.glTranslatef(0.25F, 0.1875F, -0.1875F);
        GL11.glScalef(f1, f1, f1);
        GL11.glRotatef(60.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(20.0F, 0.0F, 0.0F, 1.0F);
      }

      float f2;
      int i;
      float f5;

      if (itemstack.getItem().requiresMultipleRenderPasses()) {
        for (i = 0; i < itemstack.getItem().getRenderPasses(itemstack.getItemDamage()); ++i) {
          int j = itemstack.getItem().getColorFromItemStack(itemstack, i);
          f5 = (j >> 16 & 255) / 255.0F;
          f2 = (j >> 8 & 255) / 255.0F;
          float f3 = (j & 255) / 255.0F;
          GL11.glColor4f(f5, f2, f3, 1.0F);
          this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, i);
        }
      } else {
        i = itemstack.getItem().getColorFromItemStack(itemstack, 0);
        float f4 = (i >> 16 & 255) / 255.0F;
        f5 = (i >> 8 & 255) / 255.0F;
        f2 = (i & 255) / 255.0F;
        GL11.glColor4f(f4, f5, f2, 1.0F);
        this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
      }

      GL11.glPopMatrix();
    }
  }

  protected void func_82422_c() {
    GL11.glTranslatef(0.0F, 0.1875F, 0.0F);
  }

  @Override
  protected void func_82408_c(EntityLivingBase p_82408_1_, int p_82408_2_, float p_82408_3_) {
    this.func_82408_c(p_82408_1_, p_82408_2_, p_82408_3_);
  }

  @Override
  protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_) {
    this.renderEquippedItems((CREEPSEntityLawyerFromHell) p_77029_1_, p_77029_2_);
  }
}
