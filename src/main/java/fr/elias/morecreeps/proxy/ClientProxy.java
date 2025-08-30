package fr.elias.morecreeps.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.models.*;
import fr.elias.morecreeps.client.particles.*;
import fr.elias.morecreeps.client.render.*;
import fr.elias.morecreeps.common.MoreCreepsAndWeirdos;
import fr.elias.morecreeps.common.entity.*;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityCamelJockey;
import fr.elias.morecreeps.common.entity.ai.CREEPSEntityHunchback;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuy;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityArmyGuyArm;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBabyMummy;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBigBaby;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBlackSoul;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityBum;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleCritter;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCastleKing;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityCaveman;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilChicken;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilCreature;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilPig;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilScientist;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityEvilSnowman;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityFloob;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityG;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityHunchbackSkeleton;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityLawyerFromHell;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityManDog;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityMummy;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityPrisoner;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityPyramidGuardian;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRatMan;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRobotTed;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityRobotTodd;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntitySneakySal;
import fr.elias.morecreeps.common.entity.hostile.CREEPSEntityThief;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityInvisibleMan;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityPreacher;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntityRockMonster;
import fr.elias.morecreeps.common.entity.netural.CREEPSEntitySnowDevil;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityBlorp;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityBubbleScum;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityCamel;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityDigBug;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityDoghouse;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityGuineaPig;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHippo;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHorseHead;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityHotdog;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityKid;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityLolliman;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityNonSwimmer;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntitySchlump;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntitySquimp;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityTowel;
import fr.elias.morecreeps.common.entity.nice.CREEPSEntityZebra;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityAtom;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityDesertLizard;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityEvilLight;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityFloobShip;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityFrisbee;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityGooDonut;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityGooGoat;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityGrow;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityMoney;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityRay;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityShrink;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTombstone;
import fr.elias.morecreeps.common.entity.proj.CREEPSEntityTrophy;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

  @Override
  public void render() {
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityArmyGuy.class, new CREEPSRenderArmyGuy(new CREEPSModelArmyGuy(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityArmyGuyArm.class,
        new CREEPSRenderArmyGuyArm(new CREEPSModelArmyGuyArm(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityAtom.class, new CREEPSRenderAtom(new CREEPSModelAtom(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBabyMummy.class, new CREEPSRenderBabyMummy(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBigBaby.class, new CREEPSRenderBigBaby(new CREEPSModelBigBaby(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBlackSoul.class, new CREEPSRenderBlackSoul(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBlorp.class, new CREEPSRenderBlorp(new CREEPSModelBlorp(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBubbleScum.class,
        new CREEPSRenderBubbleScum(new CREEPSModelBubbleScum(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityBum.class, new CREEPSRenderBum(new CREEPSModelBum(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityCamel.class, new CREEPSRenderCamel(new CREEPSModelCamel(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityCamelJockey.class,
        new CREEPSRenderCamelJockey(new CREEPSModelCamelJockey(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityCastleCritter.class,
        new CREEPSRenderCastleCritter(new CREEPSModelCastleCritter(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityCastleKing.class,
        new CREEPSRenderCastleKing(new CREEPSModelCastleKing(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityCaveman.class, new CREEPSRenderCaveman(new CREEPSModelCaveman(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityDesertLizard.class,
        new CREEPSRenderDesertLizard(new CREEPSModelDesertLizard(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityDigBug.class, new CREEPSRenderDigBug(new CREEPSModelDigBug(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityDoghouse.class, new CREEPSRenderDoghouse(new CREEPSModelDoghouse(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilChicken.class,
        new CREEPSRenderEvilChicken(new CREEPSModelEvilChicken(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilCreature.class,
        new CREEPSRenderEvilCreature(new CREEPSModelEvilCreature(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilLight.class, new CREEPSRenderEvilLight(new CREEPSModelEvilLight(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilPig.class, new CREEPSRenderEvilPig(new CREEPSModelEvilPig(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilScientist.class,
        new CREEPSRenderEvilScientist(new CREEPSModelEvilScientist(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityEvilSnowman.class,
        new CREEPSRenderEvilSnowman(new CREEPSModelEvilSnowman(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityFloob.class, new CREEPSRenderFloob(new CREEPSModelFloob(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityFloobShip.class, new CREEPSRenderFloobShip(new CREEPSModelFloobShip(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityG.class, new CREEPSRenderG(new CREEPSModelG(), 0.5F));

    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityGooGoat.class, new CREEPSRenderGooGoat(new CREEPSModelGooGoat(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityGuineaPig.class,
        new CREEPSRenderGuineaPig(new CREEPSModelGPig(), new CREEPSModelGPig(0.5F), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityHippo.class, new CREEPSRenderHippo(new CREEPSModelHippo(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityHorseHead.class, new CREEPSRenderHorseHead(new CREEPSModelHorseHead(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityHotdog.class, new CREEPSRenderHotdog(new CREEPSModelHotdog(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityHunchback.class, new CREEPSRenderHunchback(new CREEPSModelHunchback(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityHunchbackSkeleton.class,
        new CREEPSRenderHunchbackSkeleton(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityKid.class, new CREEPSRenderKid(new CREEPSModelKid(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityLawyerFromHell.class,
        new CREEPSRenderLawyerFromHell(new CREEPSModelLawyerFromHell(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityLolliman.class, new CREEPSRenderLolliman(new CREEPSModelLolliman(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityManDog.class, new CREEPSRenderManDog(new CREEPSModelManDog(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityMummy.class, new CREEPSRenderMummy(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityNonSwimmer.class,
        new CREEPSRenderNonSwimmer(new CREEPSModelNonSwimmer(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityPreacher.class, new CREEPSRenderPreacher(new CREEPSModelPreacher(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityPrisoner.class, new CREEPSRenderPrisoner(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityPyramidGuardian.class,
        new CREEPSRenderPyramidGuardian(new CREEPSModelPyramidGuardian(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRatMan.class, new CREEPSRenderRatMan(new CREEPSModelRatMan(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRobotTed.class, new CREEPSRenderRobotTed(new CREEPSModelRobotTed(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRobotTodd.class, new CREEPSRenderRobotTodd(new CREEPSModelRobotTodd(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRocketGiraffe.class,
        new CREEPSRenderRocketGiraffe(new CREEPSModelRocketGiraffe(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRockMonster.class,
        new CREEPSRenderRockMonster(new CREEPSModelRockMonster(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntitySchlump.class, new CREEPSRenderSchlump(new CREEPSModelSchlump(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityShrink.class, new RenderSnowball(MoreCreepsAndWeirdos.shrinkshrink));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityRay.class, new RenderSnowball(MoreCreepsAndWeirdos.rayray));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityGrow.class, new RenderSnowball(MoreCreepsAndWeirdos.shrinkshrink));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityMoney.class, new RenderSnowball(MoreCreepsAndWeirdos.money));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityGooDonut.class, new RenderSnowball(MoreCreepsAndWeirdos.goodonut));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityFrisbee.class, new RenderSnowball(MoreCreepsAndWeirdos.frisbee));

    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntitySneakySal.class, new CREEPSRenderSneakySal(new CREEPSModelSneakySal(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntitySnowDevil.class,
        new CREEPSRenderSnowDevil(
            new CREEPSModelSnowDevil(), new CREEPSModelSnowDevil(0.5F), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntitySquimp.class, new CREEPSRenderSquimp(new CREEPSModelSquimp(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityThief.class, new CREEPSRenderThief(new ModelBiped(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityTombstone.class, new CREEPSRenderTombstone(new CREEPSModelTombstone(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityTowel.class, new CREEPSRenderTowel(new CREEPSModelTowel(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityTrophy.class, new CREEPSRenderTrophy(new CREEPSModelTrophy(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityZebra.class, new CREEPSRenderZebra(new CREEPSModelZebra(), 0.5F));
    RenderingRegistry.registerEntityRenderingHandler(
        CREEPSEntityInvisibleMan.class, new CREEPSRenderInvisibleMan(new ModelBiped(), 0.5F));
  }

  @Override
  public void guineaPigParticles(World world, Entity entity) {
    if (CREEPSConfig.Blood && world.isRemote) {
      for (int i = 0; i < 10; i++) {
        CREEPSFxBlood creepsfxblood =
            new CREEPSFxBlood(
                world,
                entity.posX,
                entity.posY + 1.0D,
                entity.posZ,
                MoreCreepsAndWeirdos.partRed,
                0.255F);
        creepsfxblood.renderDistanceWeight = 20D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxblood);
      }
    }
  }

  @Override
  public void spawnLollimanConfetti(
      World world, Entity entity, Random rand, int iterAmm1, int iterAmm2) {
    // 20 for lolli man
    for (int i = 1; i < iterAmm1; i++) {
      for (int j = 0; j < iterAmm2; j++) {
        CREEPSFxConfetti creepsfxconfetti =
            new CREEPSFxConfetti(
                world,
                entity.posX + (world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F),
                entity.posY + rand.nextInt(4) + 6D,
                entity.posZ + (world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F));
        creepsfxconfetti.renderDistanceWeight = 30D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxconfetti);
      }
    }
  }

  @Override
  public void spawnSchlumpParticles(
      World world, Entity entity, Random rand, int iterAmm1, int iterAmm2) {
    for (int i = 1; i < iterAmm1; i++) {
      for (int j = 0; j < iterAmm2; j++) {
        CREEPSFxConfetti creepsfxconfetti =
            new CREEPSFxConfetti(
                world,
                entity.posX + (world.rand.nextFloat() * 4F - world.rand.nextFloat() * 4F),
                entity.posY + rand.nextInt(4) + 6D,
                entity.posZ + (world.rand.nextFloat() * 4F - world.rand.nextFloat() * 4F));
        creepsfxconfetti.renderDistanceWeight = 20D;
        // Alternative not available?
        // Just call it in the actual class
        // creepsfxconfetti.particleMaxAge = rand.nextInt(40) + 30;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxconfetti);
      }
    }
  }

  @Override
  public void spawnSalparticles(World world, Entity entity, Random random) {
    if (random.nextInt(10) == 0) {
      double d1 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
      double d3 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(
              world,
              entity.posX + d1 * 0.5D,
              entity.posY + 2D,
              entity.posZ + d3 * 0.5D,
              0.5F,
              0.5F);
      creepsfxsmoke.renderDistanceWeight = 15D;
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
    }
  }

  @Override
  public void shrinkBlast(World world, Entity entity, Random rand) {
    if (world.isRemote && entity != null) {
      for (int i = 0; i < 8; i++) {
        byte byte0 = 7;

        if (rand.nextInt(4) == 0) {
          byte0 = 11;
        }
        CREEPSFxSmoke creepsfxsmoke =
            new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 0.5F, 0);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
      }
    }
  }

  @Override
  public void spawnSpit(World world, CREEPSEntityCamel entity, int iterAmm) {
    if (world != null && world.isRemote && entity != null) {
      double d1 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
      double d3 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
      for (int i = 0; i < iterAmm; i++) {
        CREEPSFxSpit creepsfxspit =
            new CREEPSFxSpit(
                world,
                entity.posX + d1 * (4.5F - (2.0F - entity.modelsize)),
                (entity.posY + 2.4000000953674316D) - (2.0F - entity.modelsize),
                entity.posZ + d3 * (4.5F - (2.0F - entity.modelsize)),
                MoreCreepsAndWeirdos.partBubble);
        creepsfxspit.renderDistanceWeight = 10D;
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxspit);
        }
      }
    }
  }

  @Override
  public void shrinkSmoke(World world, Entity entity) {
    if (world != null && world.isRemote && entity != null) {
      for (int k = 0; k < 8; k++) {
        CREEPSFxSmoke creepsfxsmoke =
            new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 0.25F, 0);
        creepsfxsmoke.renderDistanceWeight = 30D;
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
      }
    }
  }

  @Override
  public void rocketGoBoom(World world, Entity entity, Random rand) {
    if (world != null && world.isRemote && entity != null && rand != null) {
      for (int i = 0; i < 20; i++) {
        Item j = MoreCreepsAndWeirdos.partYellow;
        if (rand.nextInt(3) == 0) {
          j = MoreCreepsAndWeirdos.partRed;
        }
        CREEPSFxSmoke creepsfxsmoke =
            new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 1.0F, 0F);
        creepsfxsmoke.renderDistanceWeight = 30D;
        if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
        }
      }
    }
  }

  @Override
  public void rocketSmoke(World world, Entity entity, Item item) {
    if (world != null && world.isRemote && entity != null && item != null) {
      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 13, 3F);
      creepsfxsmoke.renderDistanceWeight = 15D;
      if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
      }
      CREEPSFxSmoke creepsfxsmoke1 =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 14, 3F);
      creepsfxsmoke1.renderDistanceWeight = 15D;
      if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke1);
      }
      CREEPSFxSmoke creepsfxsmoke2 =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 24, 3F);
      creepsfxsmoke2.renderDistanceWeight = 15D;
      if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().effectRenderer != null) {
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke2);
      }
    }
  }

  @Override
  public void robotTedSmoke(
      World world, double posX, double posY, double posZ, int floattimer, float modelspeed) {
    if (world.isRemote) {
      for (int i = 0; i < 15; i++) {
        CREEPSFxSmoke creepsfxsmoke =
            new CREEPSFxSmoke(
                world,
                posX - 0.40000000000000002D,
                (posY - 0.5D) + floattimer / 100,
                posZ,
                13,
                0.4F - (0.51F - modelspeed));
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
      }

      for (int j = 0; j < 15; j++) {
        CREEPSFxSmoke creepsfxsmoke1 =
            new CREEPSFxSmoke(
                world,
                posX + 0.40000000000000002D,
                (posY - 0.5D) + floattimer / 100,
                posZ,
                13,
                0.4F - (0.51F - modelspeed));
        creepsfxsmoke1.renderDistanceWeight = 15D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke1);
      }
    }
  }

  @Override
  public void barf(World world, EntityPlayer player) {
    if (world != null && world.isRemote && player != null) {
      double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
      for (int i = 0; i < 5; i++) {
        for (int j = 0; j < 50; j++) {
          CREEPSFxBlood creepsfxblood =
              new CREEPSFxBlood(
                  world,
                  player.posX,
                  player.posY + 0.60000000298023224D,
                  player.posZ,
                  MoreCreepsAndWeirdos.partBarf,
                  0.85F);
          creepsfxblood.motionX += d * 0.25D;
          creepsfxblood.motionZ += d1 * 0.25D;
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxblood);
        }
      }
    }
  }

  @Override
  public void blood(World world, double posx, double posy, double posz, boolean realBlood) {
    if (world != null && world.isRemote) {
      if (CREEPSConfig.Blood) {
        for (int i = 0; i < 30; i++) {
          CREEPSFxBlood creepsfxblood =
              new CREEPSFxBlood(
                  world,
                  posx,
                  posy,
                  posz,
                  realBlood ? MoreCreepsAndWeirdos.partRed : MoreCreepsAndWeirdos.partWhite,
                  0.255F);
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxblood);
        }
      }
    }
  }

  @Override
  public void confettiB(World world, CREEPSEntityTrophy trophy) {
    if (world.isRemote && trophy != null) {
      for (int i = 1; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
          CREEPSFxConfetti creepsfxconfetti =
              new CREEPSFxConfetti(
                  world,
                  trophy.posX + (world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F),
                  trophy.posY + world.rand.nextInt(4) + 4D,
                  trophy.posZ + (world.rand.nextFloat() * 8F - world.rand.nextFloat() * 8F));
          Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxconfetti);
        }
      }
    }
  }

  @Override
  public void eatingParticles(Entity entity, World world, int iterAmm) {
    if (!world.isRemote) return;
    // 35 for giraffe
    for (int i = 0; i < iterAmm; i++) {
      double d2 = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
      double d4 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
      CREEPSFxDirt creepsfxdirt =
          new CREEPSFxDirt(
              world,
              entity.posX + d2 * 0.40000000596046448D,
              entity.posY + 4.5D,
              entity.posZ + d4 * 0.40000000596046448D,
              Item.getItemById(12));
      creepsfxdirt.renderDistanceWeight = 6D;
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt);
    }
  }

  @Override
  public void spawnBubbles(Entity entity, World world, int iterAmm) {
    if (!world.isRemote) return;
    // 25 for scum
    for (int i = 0; i < iterAmm; i++) {

      // fixed error and decommeted (Is there a better word?) it
      double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
      CREEPSFxBubbles creepsfxbubbles =
          new CREEPSFxBubbles(
              world,
              entity.posX + d * 0.5D,
              entity.posY,
              entity.posZ + d1 * 0.5D,
              MoreCreepsAndWeirdos.partRed,
              0.5F);
      creepsfxbubbles.renderDistanceWeight = 10D;
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
    }
  }

  @Override
  public void dirt(World world, EntityPlayer player, Random random, int l, int i1, int k) {
    if (world.isRemote && player != null) {
      for (int j1 = 0; j1 < 15; j1++) {
        CREEPSFxDirt creepsfxdirt =
            new CREEPSFxDirt(
                world,
                (int) (player.posX + l + random.nextGaussian() * 0.02D),
                (int) (player.posY + k),
                (int) (player.posZ + i1 + random.nextGaussian() * 0.02D),
                Item.getItemFromBlock(Blocks.dirt));
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt);
      }
    }
  }

  @Override
  public void dirtDigBug(World world, CREEPSEntityDigBug dbug, Random random, int k2) {
    if (world.isRemote && dbug != null) {
      CREEPSFxDirt creepsfxdirt2 =
          new CREEPSFxDirt(
              world, dbug.posX, dbug.posY + k2, dbug.posZ, Item.getItemFromBlock(Blocks.dirt));
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt2);
    }
  }

  @Override
  public void foam(World world, EntityPlayer player) {
    if (world.isRemote && player != null) {
      double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
      for (int i = 0; i < 25; i++) {
        CREEPSFxFoam creepsfxfoam =
            new CREEPSFxFoam(
                world,
                player.posX + d * 0.20000000000000001D,
                player.posY * 0.80000001192092896D,
                player.posZ + d1 * 0.5D,
                MoreCreepsAndWeirdos.partWhite);
        creepsfxfoam.motionX += d * 1.3999999761581421D;
        creepsfxfoam.motionZ += d1 * 1.3999999761581421D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxfoam);
      }
    }
  }

  @Override
  public void foam2(World world, CREEPSEntityAtom atom) {
    if (world.isRemote && atom != null) {
      for (int i1 = 0; i1 < atom.atomsize; i1++) {
        CREEPSFxAtoms creepsfxatoms =
            new CREEPSFxAtoms(
                world,
                atom.posX,
                atom.posY + (int) (atom.atomsize / 3F),
                atom.posZ,
                Item.getItemById(CREEPSEntityAtom.rand.nextInt(99) + 1),
                0.3F);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxatoms);
      }
    }
  }

  @Override
  public void foam3(World world, CREEPSEntityCaveman player, int i, int j, int k) {
    if (world.isRemote && player != null) {
      double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
      double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
      for (int i1 = 0; i1 < 25; i1++) {
        CREEPSFxFoam creepsfxfoam =
            new CREEPSFxFoam(world, i, j, k, MoreCreepsAndWeirdos.partWhite);
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxfoam);
      }
    }
  }

  @Override
  public void kingSmoke(World world, Entity entity, Random random) {
    double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);

    CREEPSFxSmoke creepsfxsmoke =
        new CREEPSFxSmoke(
            world,
            (entity.posX + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
            ((entity.posY - 1.0D) + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
            (entity.posZ + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
            0.55F,
            0);
    creepsfxsmoke.renderDistanceWeight = 20D;
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
  }

  @Override
  public void smoke(World world, EntityPlayer player, Random random) {
    if (!world.isRemote) return;
    double d = -MathHelper.sin((player.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((player.rotationYaw * (float) Math.PI) / 180F);
    CREEPSFxSmoke creepsfxsmoke =
        new CREEPSFxSmoke(
            world,
            ((player.posX + random.nextGaussian() * 0.25D) - random.nextGaussian() * 0.25D)
                + d * 1.0D,
            ((player.posY - 0.5D) + random.nextGaussian() * 0.5D) - random.nextGaussian() * 0.5D,
            ((player.posZ + random.nextGaussian() * 0.25D) - random.nextGaussian() * 0.25D)
                + d1 * 1.0D,
            0.05F,
            0.0F);
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
  }

  @Override
  public void smokeHorseHead(World world, CREEPSEntityHorseHead horsehead, Random rand) {
    if (!world.isRemote) return;
    CREEPSFxSmoke creepsfxsmoke =
        new CREEPSFxSmoke(
            world,
            horsehead.posX,
            (horsehead.posY - 0.5D) + rand.nextGaussian() * 0.20000000000000001D,
            horsehead.posZ,
            0.25F,
            0.0F);
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
  }

  @Override
  public void smoke2(World world, Entity entity, Random random) {
    if (!world.isRemote) return;
    for (int i = 0; i < 8; i++) {
      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 0.2F, 0.5F);
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
    }
  }

  @Override
  public void smoke3(World world, Entity entity, Random random) {
    if (!world.isRemote) return;
    for (int i = 0; i < 5; i++) {
      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(
              world,
              entity.posX,
              (entity.posY - 0.5D) + random.nextGaussian() * 0.20000000000000001D,
              entity.posZ,
              0.65F,
              0.0F);
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
    }
  }

  @Override
  public void growParticle(World world, Entity entity) {
    if (!world.isRemote) return;
    for (int k = 0; k < 8; k++) {
      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 0.25F, 0.0F);
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
    }
  }

  public void smokeRay(World world, Entity entity, byte b0) {
    if (!world.isRemote) return;
    CREEPSFxSmoke creepsfxsmoke =
        new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, b0, 0.5F);
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
  }

  @Override
  public void shrinkParticle(World world, Entity entity) {
    if (!world.isRemote) return;
    for (int i = 0; i < 8; i++) {
      byte byte0 = 7;

      if (world.rand.nextInt(4) == 0) {
        byte0 = 11;
      }

      CREEPSFxSmoke creepsfxsmoke =
          new CREEPSFxSmoke(world, entity.posX, entity.posY, entity.posZ, 0.5F, 0.0F);
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxsmoke);
    }
  }

  @Override
  public void addChatMessage(String s) {
    if (Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null) {
      Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(s));
    }
  }

  @Override
  public void playSoundEffectAtPlayer(World world, String s, float volume, float pitch) {
    if (!world.isRemote) return;
    EntityPlayer player = Minecraft.getMinecraft().thePlayer;
    world.playSoundEffect(player.posX, player.posY, player.posZ, s, volume, pitch);
  }

  @Override
  public void bubble(World world, EntityLivingBase entity) {
    if (!world.isRemote) return;
    double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
    CREEPSFxBubbles creepsfxbubbles =
        new CREEPSFxBubbles(
            world,
            entity.posX + d * 0.5D,
            entity.posY + 0.75D,
            entity.posZ + d1 * 0.5D,
            MoreCreepsAndWeirdos.partBubble,
            0.7F);
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
  }

  @Override
  public void bubbleDoghouse(World world, EntityLivingBase entity) {
    if (!world.isRemote) return;
    double d = -MathHelper.sin((entity.rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((entity.rotationYaw * (float) Math.PI) / 180F);
    CREEPSFxBubbles creepsfxbubbles =
        new CREEPSFxBubbles(
            world,
            entity.posX + d * 0.10000000000000001D,
            entity.posY + 2D,
            (entity.posZ - 0.75D) + d1 * 0.5D,
            MoreCreepsAndWeirdos.partWhite,
            1.2F);
    Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxbubbles);
  }

  @Override
  public void pee(
      World world, double posX, double posY, double posZ, float rotationYaw, float modelsize) {
    if (!world.isRemote) return;
    double d = -MathHelper.sin((rotationYaw * (float) Math.PI) / 180F);
    double d1 = MathHelper.cos((rotationYaw * (float) Math.PI) / 180F);

    for (int i = 0; i < 25; i++) {
      CREEPSFxPee creepsfxpee =
          new CREEPSFxPee(
              world,
              posX + d * 0.20000000000000001D,
              (posY + 0.75D) - (1.0F - modelsize) * 0.8F,
              posZ + d1 * 0.20000000000000001D,
              Item.getItemFromBlock(Blocks.cobblestone));
      creepsfxpee.motionX += d * 0.23999999463558197D;
      creepsfxpee.motionZ += d1 * 0.23999999463558197D;
      Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxpee);
    }
  }

  @Override
  public boolean isJumpKeyDown() {
    System.out.println("Pressed");
    return Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode());
  }

  // Rocket Giraffe mouth-dirt burst (client only)
  @Override
  public void giraffeEatDirt(
      World world, fr.elias.morecreeps.common.entity.nice.CREEPSEntityRocketGiraffe giraffe) {
    if (world != null && world.isRemote && giraffe != null) {
      double d2 = -MathHelper.sin((giraffe.rotationYaw * (float) Math.PI) / 180F);
      double d4 = MathHelper.cos((giraffe.rotationYaw * (float) Math.PI) / 180F);
      for (int i = 0; i < 35; i++) {
        CREEPSFxDirt creepsfxdirt =
            new CREEPSFxDirt(
                world,
                giraffe.posX + d2 * 0.40000000596046448D,
                giraffe.posY + 4.5D,
                giraffe.posZ + d4 * 0.40000000596046448D,
                Item.getItemFromBlock(Blocks.dirt));
        creepsfxdirt.renderDistanceWeight = 6D;
        Minecraft.getMinecraft().effectRenderer.addEffect(creepsfxdirt);
      }
    }
  }
}
