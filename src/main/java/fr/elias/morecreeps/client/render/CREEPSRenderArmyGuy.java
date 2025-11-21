package fr.elias.morecreeps.client.render;

import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
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

import com.mojang.authlib.GameProfile;

import fr.elias.morecreeps.client.models.CREEPSModelArmyGuy;
import fr.elias.morecreeps.common.Reference;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuy;

public class CREEPSRenderArmyGuy extends RenderLiving {

    protected CREEPSModelArmyGuy modelBipedMain;
    public static final ResourceLocation texture = new ResourceLocation(
        Reference.MOD_ID,
        Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_DEFAULT);
    public static final ResourceLocation texture_loyal = new ResourceLocation(
        Reference.MOD_ID,
        Reference.TEXTURE_PATH_ENTITES + Reference.TEXTURE_ARMY_GUY_LOYAL);

    public CREEPSRenderArmyGuy(CREEPSModelArmyGuy creepsmodelarmyguy, float f) {
        super(creepsmodelarmyguy, f);
        this.modelBipedMain = creepsmodelarmyguy;
        this.mainModel = creepsmodelarmyguy;
    }

    protected void preRenderCallback(CREEPSEntityArmyGuy entityliving, float f) {
        CREEPSEntityArmyGuy creepsentityarmyguy = entityliving;
        this.modelBipedMain.armright = creepsentityarmyguy.armright;
        this.modelBipedMain.armleft = creepsentityarmyguy.armleft;
        this.modelBipedMain.legright = creepsentityarmyguy.legright;
        this.modelBipedMain.legleft = creepsentityarmyguy.legleft;
        this.modelBipedMain.helmet = creepsentityarmyguy.helmet;
        this.modelBipedMain.head = creepsentityarmyguy.head;
        this.modelBipedMain.modelsize = creepsentityarmyguy.modelsize;
        if (creepsentityarmyguy.legleft && creepsentityarmyguy.legright && creepsentityarmyguy.head) {
            GL11.glTranslated(0.0D, 1.4D, 0.0D);
        } else if (creepsentityarmyguy.legleft && creepsentityarmyguy.legright) {
            GL11.glTranslated(0.0D, 0.75D, 0.0D);
        } else {
            GL11.glTranslated(0.0D, 0.0D, 0.0D);
        }
        this.fattenup(entityliving, f);
    }

    protected void fattenup(CREEPSEntityArmyGuy creepsentityarmyguy, float f) {
        GL11.glScalef(creepsentityarmyguy.modelsize, creepsentityarmyguy.modelsize, creepsentityarmyguy.modelsize);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
        this.preRenderCallback((CREEPSEntityArmyGuy) p_77041_1_, p_77041_2_);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.doRender((CREEPSEntityArmyGuy) entity, d, d1, d2, f, f1);
    }

    protected ResourceLocation getEntityTexture(CREEPSEntityArmyGuy entity) {
        return !entity.loyal ? texture : texture_loyal;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return this.getEntityTexture((CREEPSEntityArmyGuy) entity);
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
            this.modelBipedMain.bipedHead.postRender(0.0625F);
            item = itemstack1.getItem();

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient
                .getItemRenderer(itemstack1, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(
                net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,
                itemstack1,
                net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (item instanceof ItemBlock) {
                if (is3D || RenderBlocks.renderItemIn3d(
                    Block.getBlockFromItem(item)
                        .getRenderType())) {
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

                TileEntitySkullRenderer.field_147536_b
                    .func_152674_a(-0.5F, 0.0F, -0.5F, 1, 180.0F, itemstack1.getItemDamage(), gameprofile);
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

            this.modelBipedMain.bipedRightArm.postRender(0.0625F);
            GL11.glTranslatef(-0.0625F, 0.4375F, 0.0625F);

            net.minecraftforge.client.IItemRenderer customRenderer = net.minecraftforge.client.MinecraftForgeClient
                .getItemRenderer(itemstack, net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(
                net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED,
                itemstack,
                net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D));

            if (item instanceof ItemBlock && (is3D || RenderBlocks.renderItemIn3d(
                Block.getBlockFromItem(item)
                    .getRenderType()))) {
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

            if (itemstack.getItem()
                .requiresMultipleRenderPasses()) {
                for (i = 0; i < itemstack.getItem()
                    .getRenderPasses(itemstack.getItemDamage()); ++i) {
                    int j = itemstack.getItem()
                        .getColorFromItemStack(itemstack, i);
                    f5 = (j >> 16 & 255) / 255.0F;
                    f2 = (j >> 8 & 255) / 255.0F;
                    float f3 = (j & 255) / 255.0F;
                    GL11.glColor4f(f5, f2, f3, 1.0F);
                    this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, i);
                }
            } else {
                i = itemstack.getItem()
                    .getColorFromItemStack(itemstack, 0);
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
        this.renderEquippedItems((CREEPSEntityArmyGuy) p_77029_1_, p_77029_2_);
    }
}
