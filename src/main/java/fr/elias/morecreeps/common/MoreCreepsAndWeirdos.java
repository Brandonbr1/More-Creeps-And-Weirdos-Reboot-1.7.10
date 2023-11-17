package fr.elias.morecreeps.common;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.stats.Achievement;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.elias.morecreeps.client.config.CREEPSConfig;
import fr.elias.morecreeps.client.gui.handler.CREEPSGuiHandler;
import fr.elias.morecreeps.common.entity.*;
import fr.elias.morecreeps.common.items.*;
import fr.elias.morecreeps.common.recipes.CREEPSRecipeHandler;
import fr.elias.morecreeps.common.world.WorldGenStructures;
import fr.elias.morecreeps.proxy.CommonProxy;

@Mod(modid = "morecreeps", name = "More Creeps And Weirdos Unofficial", version = "1.0.0")
public class MoreCreepsAndWeirdos {

    @SidedProxy(
        clientSide = "fr.elias.morecreeps.proxy.ClientProxy",
        serverSide = "fr.elias.morecreeps.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("morecreeps")
    public static MoreCreepsAndWeirdos instance;

    public static Random rand = new Random();

    private int count;

    public int spittime = 500;

    public int currentJailX;
    public int currentJailY;
    public int currentJailZ;
    public boolean jailBuilt;

    public int currentfine;

    public int creepsTimer;

    public static int prisonercount = 0;
    public static int colacount = 0;
    public static int rocketcount = 0;
    public static int floobcount = 0;
    public static int goatcount = 0;
    public static int preachercount = 0;
    public static int cavemancount = 0;
    public static boolean cavemanbuilding = false;

    // BELOW : This motherfucking particle system of Minecraft... -_-
    public static Item partBubble, partWhite, partRed, partBlack, partYellow, partBlue, partShrink, partBarf;

    public static Item a_hell, a_pig, a_pyramid, a_floob, a_rockmonster, a_bubble, a_hotdog, a_camel, a_zebra,
        a_nonswimmer, a_caveman;

    public static Item blorpcola, bandaid, goodonut, money, raygun, shrinkray, shrinkshrink, limbs, babyjarempty,
        babyjarfull, mobilephone, growray, frisbee, rayray, guineapigradio, evilegg, rocket, atompacket, ram16k,
        battery, horseheadgem, armygem, gun, bullet, lifegem, lolly, armsword, donut, extinguisher, zebrahide, firegem,
        earthgem, mininggem, healinggem, skygem, gemsword, moopsworm, cavemanclub, popsicle;

    public static ArmorMaterial zebraARMOR = EnumHelper.addArmorMaterial("zebraARMOR", 25, new int[] { 2, 6, 4, 2 }, 5);
    public static Item zebrahelmet, zebrabody, zebralegs, zebraboots;

    public static int aX;
    public static int aY;
    public static Achievement achievefrisbee;
    public static Achievement achieveradio;
    public static Achievement achievegotohell;
    public static Achievement achievechugcola;
    public static Achievement achievepigtaming;
    public static Achievement achievepiglevel5;
    public static Achievement achievepiglevel10;
    public static Achievement achievepiglevel20;
    public static Achievement achieverocketgiraffe;
    public static Achievement achieverocket;
    public static Achievement achieverocketrampage;
    public static Achievement achievepyramid;
    public static Achievement achievefloobkill;
    public static Achievement achievefloobicide;
    public static Achievement achievegookill;
    public static Achievement achievegookill10;
    public static Achievement achievegookill25;
    public static Achievement achievesnowdevil;
    public static Achievement achievehunchback;
    public static Achievement achieverockmonster;
    public static Achievement achievebumflower;
    public static Achievement achievebumpot;
    public static Achievement achievebumlava;
    public static Achievement achieve100bucks;
    public static Achievement achieve500bucks;
    public static Achievement achieve1000bucks;
    public static Achievement achievepighotel;
    public static Achievement achieve10bubble;
    public static Achievement achieve25bubble;
    public static Achievement achieve50bubble;
    public static Achievement achieve100bubble;
    public static Achievement achievesnow;
    public static Achievement achievesnowtiny;
    public static Achievement achievesnowtall;
    public static Achievement achievehotdoglevel5;
    public static Achievement achievehotdoglevel10;
    public static Achievement achievehotdoglevel25;
    public static Achievement achievehotdogheaven;
    public static Achievement achievehotdogtaming;
    public static Achievement achieveram128;
    public static Achievement achieveram512;
    public static Achievement achieveram1024;
    public static Achievement achievefalseidol;
    public static Achievement achievecamel;
    public static Achievement achievelolliman;
    public static Achievement achievezebra;
    public static Achievement achieveschlump;
    public static Achievement achievenonswimmer;
    public static Achievement achieveprisoner;
    public static Achievement achieve5prisoner;
    public static Achievement achieve10prisoner;
    public static Achievement achieve1caveman;
    public static Achievement achieve10caveman;
    public static Achievement achieve50caveman;

    public static CreativeTabs creepsTab = new CreativeTabs("creepsTab") {

        public Item getTabIconItem() {
            return MoreCreepsAndWeirdos.a_floob;
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        // Loads config
        CREEPSConfig.preInit(event);

        partBubble = new Item().setUnlocalizedName("partBubble")
            .setTextureName("morecreeps:bubble");
        partWhite = new Item().setUnlocalizedName("partWhite")
            .setTextureName("morecreeps:White");
        partRed = new Item().setUnlocalizedName("partRed")
            .setTextureName("morecreeps:Red");
        partBlack = new Item().setUnlocalizedName("partBlack")
            .setTextureName("morecreeps:Black");
        partYellow = new Item().setUnlocalizedName("partYellow")
            .setTextureName("morecreeps:Yellow");
        partBlue = new Item().setUnlocalizedName("partBlue")
            .setTextureName("morecreeps:Blue");
        partShrink = new Item().setUnlocalizedName("partShrink")
            .setTextureName("morecreeps:Shrink");
        partBarf = new Item().setUnlocalizedName("partBarf")
            .setTextureName("morecreeps:Barf");
        GameRegistry.registerItem(partBubble, "partBubble");
        GameRegistry.registerItem(partWhite, "partWhite");
        GameRegistry.registerItem(partRed, "partRed");
        GameRegistry.registerItem(partBlack, "partBlack");
        GameRegistry.registerItem(partYellow, "partYellow");
        GameRegistry.registerItem(partBlue, "partBlue");
        GameRegistry.registerItem(partShrink, "partShrink");
        GameRegistry.registerItem(partBarf, "partBarf");

        zebrahelmet = (new CREEPSItemArmorZebra(zebraARMOR, 5, 0)).setUnlocalizedName("zebraHelmet")
            .setTextureName("morecreeps:zebraHelmet");
        zebrabody = (new CREEPSItemArmorZebra(zebraARMOR, 5, 1)).setUnlocalizedName("zebraBody")
            .setTextureName("morecreeps:zebraBody");
        zebralegs = new CREEPSItemArmorZebra(zebraARMOR, 5, 2).setUnlocalizedName("zebraLegs")
            .setTextureName("morecreeps:zebralegs");
        zebraboots = (new CREEPSItemArmorZebra(zebraARMOR, 5, 3)).setUnlocalizedName("zebraBoots")
            .setTextureName("morecreeps:zebraBoots");
        GameRegistry.registerItem(zebrahelmet, "zebraHelmet");
        GameRegistry.registerItem(zebrabody, "zebraBody");
        GameRegistry.registerItem(zebralegs, "zebraLegs");
        GameRegistry.registerItem(zebraboots, "zebraBoots");

        a_hell = new Item().setUnlocalizedName("morecreeps:a_hell")
            .setTextureName("morecreeps:hell");
        a_pig = new Item().setUnlocalizedName("morecreeps:a_pig")
            .setTextureName("morecreeps:pig");
        a_pyramid = new Item().setUnlocalizedName("morecreeps:a_pyramid")
            .setTextureName("morecreeps:pyramid");
        a_floob = new Item().setUnlocalizedName("morecreeps:a_floob")
            .setTextureName("morecreeps:floob");
        a_rockmonster = new Item().setUnlocalizedName("morecreeps:a_rockmonster")
            .setTextureName("morecreeps:rockmonster");
        a_bubble = new Item().setUnlocalizedName("morecreeps:a_bubble")
            .setTextureName("morecreeps:bubble");
        a_hotdog = new Item().setUnlocalizedName("morecreeps:a_hotdog")
            .setTextureName("morecreeps:hotdog");
        a_camel = new Item().setUnlocalizedName("morecreeps:a_camel")
            .setTextureName("morecreeps:camel");
        a_zebra = new Item().setUnlocalizedName("morecreeps:a_zebra")
            .setTextureName("morecreeps:zebra");
        a_nonswimmer = new Item().setUnlocalizedName("morecreeps:a_nonswimmer")
            .setTextureName("morecreeps:nonswimmer");
        a_caveman = new Item().setUnlocalizedName("morecreeps:a_caveman")
            .setTextureName("morecreeps:caveman");
        GameRegistry.registerItem(a_hell, "a_hell");
        GameRegistry.registerItem(a_pig, "a_pig");
        GameRegistry.registerItem(a_pyramid, "a_pyramid");
        GameRegistry.registerItem(a_floob, "a_floob");
        GameRegistry.registerItem(a_rockmonster, "a_rockmonster");
        GameRegistry.registerItem(a_bubble, "a_bubble");
        GameRegistry.registerItem(a_hotdog, "a_hotdog");
        GameRegistry.registerItem(a_camel, "a_camel");
        GameRegistry.registerItem(a_zebra, "a_zebra");
        GameRegistry.registerItem(a_nonswimmer, "a_nonswimmer");
        GameRegistry.registerItem(a_caveman, "a_caveman");

        blorpcola = new CREEPSItemBlorpCola().setCreativeTab(creepsTab)
            .setUnlocalizedName("blorpCola")
            .setTextureName("morecreeps:blorpCola");
        bandaid = new CREEPSItemBandAid().setCreativeTab(creepsTab)
            .setUnlocalizedName("bandAid")
            .setTextureName("morecreeps:bandAid");
        goodonut = new CREEPSItemGooDonut().setCreativeTab(creepsTab)
            .setUnlocalizedName("gooDonut")
            .setTextureName("morecreeps:gooDonut");
        money = new CREEPSItemMoney().setCreativeTab(creepsTab)
            .setUnlocalizedName("money")
            .setTextureName("morecreeps:money");
        raygun = new CREEPSItemRayGun().setCreativeTab(creepsTab)
            .setUnlocalizedName("raygun")
            .setTextureName("morecreeps:raygun");
        shrinkray = new CREEPSItemShrinkRay().setCreativeTab(creepsTab)
            .setUnlocalizedName("shrinkray")
            .setTextureName("morecreeps:shrinkray");
        shrinkshrink = new Item().setCreativeTab(creepsTab)
            .setUnlocalizedName("shrinkshrink")
            .setTextureName("morecreeps:shrinkshrink");
        limbs = new CREEPSItemLimbs().setCreativeTab(creepsTab)
            .setUnlocalizedName("limbs")
            .setTextureName("morecreeps:limbs");
        babyjarempty = new CREEPSItemBabyJarEmpty().setCreativeTab(creepsTab)
            .setUnlocalizedName("babyJarEmpty")
            .setTextureName("morecreeps:babyJarEmpty");
        babyjarfull = new CREEPSItemBabyJarFull().setCreativeTab(creepsTab)
            .setUnlocalizedName("babyJarFull")
            .setTextureName("morecreeps:babyJarFull");
        mobilephone = new CREEPSItemMobilePhone().setCreativeTab(creepsTab)
            .setUnlocalizedName("mobilephone")
            .setTextureName("morecreeps:mobilephone");
        growray = new CREEPSItemGrowRay().setCreativeTab(creepsTab)
            .setUnlocalizedName("growray")
            .setTextureName("morecreeps:growray");
        frisbee = new CREEPSItemFrisbee().setCreativeTab(creepsTab)
            .setUnlocalizedName("frisbee")
            .setTextureName("morecreeps:frisbee");
        rayray = new CREEPSItemRayRay().setCreativeTab(creepsTab)
            .setUnlocalizedName("rayray")
            .setTextureName("morecreeps:rayray");
        guineapigradio = new CREEPSItemGuineaPigRadio().setCreativeTab(creepsTab)
            .setUnlocalizedName("guineapigRadio")
            .setTextureName("morecreeps:guineapigRadio");
        evilegg = new CREEPSItemEvilEgg().setCreativeTab(creepsTab)
            .setUnlocalizedName("evilEgg")
            .setTextureName("morecreeps:evilEgg");
        rocket = new Item().setCreativeTab(creepsTab)
            .setUnlocalizedName("rocket")
            .setTextureName("morecreeps:rocket");
        atompacket = new CREEPSItemAtom().setCreativeTab(creepsTab)
            .setUnlocalizedName("atomPacket")
            .setTextureName("morecreeps:atomPacket");
        ram16k = new Item().setCreativeTab(creepsTab)
            .setUnlocalizedName("ram16k")
            .setTextureName("morecreeps:ram16k");
        battery = new CREEPSItemBattery().setCreativeTab(creepsTab)
            .setUnlocalizedName("battery")
            .setTextureName("morecreeps:battery");
        horseheadgem = new CREEPSItemHorseHeadGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("horseHeadGem")
            .setTextureName("morecreeps:horseHeadGem");
        armygem = new CREEPSItemArmyGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("armyGem")
            .setTextureName("morecreeps:armyGem");
        gun = new CREEPSItemGun().setCreativeTab(creepsTab)
            .setUnlocalizedName("gun")
            .setTextureName("morecreeps:gun");
        bullet = new CREEPSItemBullet().setCreativeTab(creepsTab)
            .setUnlocalizedName("bullet")
            .setTextureName("morecreeps:bullet");
        lifegem = new CREEPSItemLifeGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("lifeGem")
            .setTextureName("morecreeps:lifeGem");
        lolly = new CREEPSItemLolly().setCreativeTab(creepsTab)
            .setUnlocalizedName("lolly")
            .setTextureName("morecreeps:lolly");
        armsword = new CREEPSItemArmSword().setCreativeTab(creepsTab)
            .setUnlocalizedName("armSword")
            .setTextureName("morecreeps:armSword");
        donut = new CREEPSItemDonut().setCreativeTab(creepsTab)
            .setUnlocalizedName("donut")
            .setTextureName("morecreeps:donut");
        extinguisher = new CREEPSItemExtinguisher().setCreativeTab(creepsTab)
            .setUnlocalizedName("extinguisher")
            .setTextureName("morecreeps:extinguisher");
        zebrahide = new Item().setCreativeTab(creepsTab)
            .setUnlocalizedName("zebrahide")
            .setTextureName("morecreeps:zebrahide");
        firegem = new CREEPSItemFireGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("fireGem")
            .setTextureName("morecreeps:fireGem");
        earthgem = new CREEPSItemEarthGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("earthGem")
            .setTextureName("morecreeps:earthGem");
        mininggem = new CREEPSItemEarthGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("miningGem")
            .setTextureName("morecreeps:miningGem");
        healinggem = new CREEPSItemHealingGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("healingGem")
            .setTextureName("morecreeps:healingGem");
        skygem = new CREEPSItemSkyGem().setCreativeTab(creepsTab)
            .setUnlocalizedName("skyGem")
            .setTextureName("morecreeps:skyGem");
        gemsword = new CREEPSItemGemSword().setCreativeTab(creepsTab)
            .setUnlocalizedName("gemSword")
            .setTextureName("morecreeps:gemSword");
        moopsworm = new CREEPSItemMoopsWorm().setCreativeTab(creepsTab)
            .setUnlocalizedName("moopsWorm")
            .setTextureName("morecreeps:moopsWorm");
        cavemanclub = new CREEPSItemCavemanClub().setCreativeTab(creepsTab)
            .setUnlocalizedName("cavemanClub")
            .setTextureName("morecreeps:cavemanClub");
        popsicle = new CREEPSItemPopsicle().setCreativeTab(creepsTab)
            .setUnlocalizedName("popsicle")
            .setTextureName("morecreeps:popsicle");

        GameRegistry.registerItem(blorpcola, "blorpCola", "morecreeps");
        GameRegistry.registerItem(bandaid, "bandAid", "morecreeps");
        GameRegistry.registerItem(goodonut, "gooDonut", "morecreeps");
        GameRegistry.registerItem(money, "money", "morecreeps");
        GameRegistry.registerItem(raygun, "raygun", "morecreeps");
        GameRegistry.registerItem(shrinkray, "shrinkray", "morecreeps");
        GameRegistry.registerItem(shrinkshrink, "shrinkshrink", "morecreeps");
        GameRegistry.registerItem(limbs, "limbs", "morecreeps");
        GameRegistry.registerItem(babyjarempty, "babyJarEmpty", "morecreeps");
        GameRegistry.registerItem(babyjarfull, "babyJarFull", "morecreeps");
        GameRegistry.registerItem(mobilephone, "mobilephone", "morecreeps");
        GameRegistry.registerItem(growray, "growray", "morecreeps");
        GameRegistry.registerItem(frisbee, "frisbee", "morecreeps");
        GameRegistry.registerItem(rayray, "rayray", "morecreeps");
        GameRegistry.registerItem(guineapigradio, "guineapigRadio", "morecreeps");
        GameRegistry.registerItem(evilegg, "evilEgg", "morecreeps");
        GameRegistry.registerItem(rocket, "rocket", "morecreeps");
        GameRegistry.registerItem(atompacket, "atomPacket", "morecreeps");
        GameRegistry.registerItem(ram16k, "ram16k", "morecreeps");
        GameRegistry.registerItem(battery, "battery", "morecreeps");
        GameRegistry.registerItem(horseheadgem, "horseHeadGem", "morecreeps");
        GameRegistry.registerItem(armygem, "armyGem", "morecreeps");
        GameRegistry.registerItem(gun, "gun", "morecreeps");
        GameRegistry.registerItem(bullet, "bullet", "morecreeps");
        GameRegistry.registerItem(lifegem, "lifeGem", "morecreeps");
        GameRegistry.registerItem(lolly, "lolly", "morecreeps");
        GameRegistry.registerItem(armsword, "armSword", "morecreeps");
        GameRegistry.registerItem(donut, "donut", "morecreeps");
        GameRegistry.registerItem(extinguisher, "extinguisher", "morecreeps");
        GameRegistry.registerItem(zebrahide, "zebrahide", "morecreeps");
        GameRegistry.registerItem(firegem, "fireGem", "morecreeps");
        GameRegistry.registerItem(earthgem, "earthGem", "morecreeps");
        GameRegistry.registerItem(mininggem, "miningGem", "morecreeps");
        GameRegistry.registerItem(healinggem, "healingGem", "morecreeps");
        GameRegistry.registerItem(skygem, "skyGem", "morecreeps");
        GameRegistry.registerItem(gemsword, "gemSword", "morecreeps");
        GameRegistry.registerItem(moopsworm, "moopsWorm", "morecreeps");
        GameRegistry.registerItem(cavemanclub, "cavemanClub", "morecreeps");
        GameRegistry.registerItem(popsicle, "popsicle", "morecreeps");

        aX = -2;
        aY = 15;
        achievefrisbee = (new Achievement("frisbee", "frisbee", aX, aY, frisbee, null)).registerStat();
        achievechugcola = (new Achievement("chugcola", "chugcola", aX + 2, aY, blorpcola, null)).registerStat();
        achieveradio = (new Achievement("guineapigradio", "guineapigradio", aX + 4, aY, guineapigradio, null))
            .registerStat();
        achievepyramid = (new Achievement("pyramid", "pyramid", aX + 6, aY, a_pyramid, null)).registerStat();
        achievelolliman = (new Achievement("lolliman", "lolliman", aX + 8, aY, lolly, null)).registerStat();
        achievesnowdevil = (new Achievement("snowdevil", "snowdevil", aX + 2, aY + 2, Blocks.ice, null)).registerStat();
        achievehunchback = (new Achievement("hunchback", "hunchback", aX + 4, aY + 2, Items.cake, null)).registerStat();
        achievecamel = (new Achievement("camel", "camel", aX + 6, aY + 2, a_camel, achievecamel)).registerStat();
        achievezebra = (new Achievement("zebra", "zebra", aX + 8, aY + 2, a_zebra, achievezebra)).registerStat();
        achieverockmonster = (new Achievement("rockmonster", "rockmonster", aX, aY + 2, a_rockmonster, null))
            .registerStat();
        achieveschlump = (new Achievement("schlump", "schlump", aX, aY + 4, babyjarfull, achieveschlump))
            .registerStat();
        achievenonswimmer = (new Achievement(
            "nonswimmer",
            "nonswimmer",
            aX + 2,
            aY + 4,
            a_nonswimmer,
            achievenonswimmer)).registerStat();
        achievepigtaming = (new Achievement("pigtaming", "pigtaming", aX, aY + 6, a_pig, null)).registerStat();
        achievepiglevel5 = (new Achievement("level5", "level5", aX + 2, aY + 6, a_pig, achievepigtaming))
            .registerStat();
        achievepiglevel10 = (new Achievement("level10", "level10", aX + 4, aY + 6, a_pig, achievepiglevel5))
            .registerStat();
        achievepiglevel20 = (new Achievement("level20", "level20", aX + 6, aY + 6, a_pig, achievepiglevel10))
            .registerStat();
        achievepighotel = (new Achievement("pighotel", "pighotel", aX + 8, aY + 6, a_pig, achievepiglevel20))
            .registerStat();
        achieverocketgiraffe = (new Achievement("rocketgiraffe", "rocketgiraffe", aX, aY + 8, rocket, null))
            .registerStat();
        achieverocket = (new Achievement("rocket", "rocket", aX + 2, aY + 8, rocket, achieverocketgiraffe))
            .registerStat();
        achieverocketrampage = (new Achievement(
            "rocketrampage",
            "rocketrampage",
            aX + 4,
            aY + 8,
            rocket,
            achieverocket)).registerStat();
        achievefloobkill = (new Achievement("floobkill", "floobkill", aX, aY + 10, a_floob, null)).registerStat();
        achievefloobicide = (new Achievement("floobicide", "floobicide", aX + 2, aY + 10, a_floob, achievefloobkill))
            .registerStat();
        achievegookill = (new Achievement("gookill", "gookill", aX, aY + 12, goodonut, null)).registerStat();
        achievegookill10 = (new Achievement("gookill10", "gookill10", aX + 2, aY + 12, goodonut, achievegookill))
            .registerStat();
        achievegookill25 = (new Achievement("gookill25", "gookill25", aX + 4, aY + 12, goodonut, achievegookill10))
            .registerStat();
        achievebumflower = (new Achievement("bumflower", "bumflower", aX, aY + 14, Blocks.yellow_flower, null))
            .registerStat();
        achievebumpot = (new Achievement("bumpot", "bumpot", aX + 2, aY + 14, Items.bucket, achievebumflower))
            .registerStat();
        achievebumlava = (new Achievement("bumlava", "bumlava", aX + 4, aY + 14, Items.lava_bucket, achievebumpot))
            .registerStat();
        achieve100bucks = (new Achievement("achieve100bucks", "achieve100bucks", aX, aY + 16, money, null))
            .registerStat();
        achieve500bucks = (new Achievement(
            "achieve500bucks",
            "achieve500bucks",
            aX + 2,
            aY + 16,
            money,
            achieve100bucks)).registerStat();
        achieve1000bucks = (new Achievement(
            "achieve1000bucks",
            "achieve1000bucks",
            aX + 4,
            aY + 16,
            money,
            achieve500bucks)).registerStat();
        achieve10bubble = (new Achievement("achieve10bubble", "achieve10bubble", aX, aY + 18, a_bubble, null))
            .registerStat();
        achieve25bubble = (new Achievement(
            "achieve25bubble",
            "achieve25bubble",
            aX + 2,
            aY + 18,
            a_bubble,
            achieve10bubble)).registerStat();
        achieve50bubble = (new Achievement(
            "achieve50bubble",
            "achieve50bubble",
            aX + 4,
            aY + 18,
            a_bubble,
            achieve25bubble)).registerStat();
        achieve100bubble = (new Achievement(
            "achieve100bubble",
            "achieve100bubble",
            aX + 6,
            aY + 18,
            a_bubble,
            achieve50bubble)).registerStat();
        achievesnow = (new Achievement("achievesnow", "achievesnow", aX, aY + 20, Items.snowball, null)).registerStat();
        achievesnowtiny = (new Achievement("achievesnowtiny", "achievesnowtiny", aX + 2, aY + 20, Items.snowball, null))
            .registerStat();
        achievesnowtall = (new Achievement("achievesnowtall", "achievesnowtall", aX + 4, aY + 20, Items.snowball, null))
            .registerStat();
        achievehotdogtaming = (new Achievement("hotdogtaming", "hotdogtaming", aX, aY + 22, a_hotdog, null))
            .registerStat();
        achievehotdoglevel5 = (new Achievement(
            "hotdoglevel5",
            "level5",
            aX + 2,
            aY + 22,
            a_hotdog,
            achievehotdogtaming)).registerStat();
        achievehotdoglevel10 = (new Achievement(
            "hotdoglevel10",
            "level10",
            aX + 4,
            aY + 22,
            a_hotdog,
            achievehotdoglevel5)).registerStat();
        achievehotdoglevel25 = (new Achievement(
            "hotdoglevel25",
            "level25",
            aX + 6,
            aY + 22,
            a_hotdog,
            achievehotdoglevel10)).registerStat();
        achievehotdogheaven = (new Achievement(
            "hotdogheaven",
            "hotdogheaven",
            aX + 8,
            aY + 22,
            a_hotdog,
            achievehotdoglevel25)).registerStat();
        achieveram128 = (new Achievement("ram128", "ram128", aX, aY + 24, ram16k, null)).registerStat();
        achieveram512 = (new Achievement("ram512", "ram512", aX + 2, aY + 24, ram16k, achieveram128)).registerStat();
        achieveram1024 = (new Achievement("ram1024", "ram1024", aX + 4, aY + 24, ram16k, achieveram512)).registerStat();
        achievegotohell = (new Achievement("gotohell", "gotohell", aX, aY + 26, a_hell, null)).registerStat();
        achievefalseidol = (new Achievement("falseidol", "falseidol", aX + 2, aY + 26, a_hell, achievegotohell))
            .registerStat();
        achieveprisoner = (new Achievement("achieveprisoner", "achieveprisoner", aX, aY + 28, Blocks.iron_bars, null))
            .registerStat();
        achieve5prisoner = (new Achievement(
            "achieve5prisoner",
            "achieve5prisoner",
            aX + 2,
            aY + 28,
            Blocks.iron_bars,
            achieveprisoner)).registerStat();
        achieve10prisoner = (new Achievement(
            "achieve10prisoner",
            "achieve10prisoner",
            aX + 4,
            aY + 28,
            Blocks.iron_bars,
            achieve5prisoner)).registerStat();
        achieve1caveman = (new Achievement("achieve1caveman", "achieve1caveman", aX, aY + 30, a_caveman, null))
            .registerStat();
        achieve10caveman = (new Achievement(
            "achieve10caveman",
            "achieve10caveman",
            aX + 2,
            aY + 30,
            a_caveman,
            achieve1caveman)).registerStat();
        achieve50caveman = (new Achievement(
            "achieve50caveman",
            "achieve50caveman",
            aX + 4,
            aY + 30,
            a_caveman,
            achieve10caveman)).registerStat();

        GameRegistry.registerWorldGenerator(new WorldGenStructures(), 0);
        MinecraftForge.EVENT_BUS.register(new CraftingHandlerEvent());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MoreCreepsAndWeirdos.instance, new CREEPSGuiHandler());
        // projectiles registry
        EntityRegistry.registerModEntity(
            CREEPSEntityShrink.class,
            "ShrinkEnt",
            CREEPSConfig.shrink_projectile_ID,
            this,
            40,
            1,
            true);
        EntityRegistry
            .registerModEntity(CREEPSEntityRay.class, "RayEnt", CREEPSConfig.ray_projectile_ID, this, 40, 1, true);
        EntityRegistry.registerModEntity(
            CREEPSEntityMoney.class,
            "MoneyEnt",
            CREEPSConfig.money_projectile_ID,
            this,
            40,
            1,
            true);
        EntityRegistry.registerModEntity(
            CREEPSEntityBullet.class,
            "BulletEnt",
            CREEPSConfig.bullet_projectile_ID,
            this,
            40,
            1,
            true);
        EntityRegistry
            .registerModEntity(CREEPSEntityGrow.class, "GrowEnt", CREEPSConfig.grow_projectile_ID, this, 40, 1, true);
        EntityRegistry.registerModEntity(
            CREEPSEntityGooDonut.class,
            "GooDonutEnt",
            CREEPSConfig.gdonut_projectile_ID,
            this,
            40,
            1,
            true);
        EntityRegistry.registerModEntity(
            CREEPSEntityFrisbee.class,
            "FrisbeeEnt",
            CREEPSConfig.frisbee_projectile_ID,
            this,
            40,
            1,
            true);
        EntityRegistry
            .registerModEntity(CREEPSEntityFoam.class, "FoamEnt", CREEPSConfig.foam_projectile_ID, this, 40, 1, true);
        ////////////////////////
        EntityRegistry.registerModEntity(
            CREEPSEntityArmyGuyArm.class,
            "ArmyGuyArm",
            CREEPSConfig.armyguyArm_ID,
            this,
            40,
            1,
            true);

        addMob(
            CREEPSEntityArmyGuy.class,
            "ArmyGuy",
            CREEPSConfig.armyguy_ID,
            CREEPSConfig.sarmyguy,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBigBaby.class,
            "BigBaby",
            CREEPSConfig.bigbaby_ID,
            CREEPSConfig.sbigbaby,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBabyMummy.class,
            "BabyMummy",
            CREEPSConfig.babymummy_ID,
            CREEPSConfig.sbabymummy,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBlackSoul.class,
            "BlackSoul",
            CREEPSConfig.blacksoul_ID,
            CREEPSConfig.sblacksoul,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBlorp.class,
            "Blorp",
            CREEPSConfig.blorp_ID,
            CREEPSConfig.sblorp,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBubbleScum.class,
            "BubbleScum",
            CREEPSConfig.bubblescum_ID,
            CREEPSConfig.sbubblescum,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityBum.class,
            "Bum",
            CREEPSConfig.bum_ID,
            CREEPSConfig.sbum,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityCamel.class,
            "Camel",
            CREEPSConfig.camel_ID,
            CREEPSConfig.scamel,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityCamelJockey.class,
            "CamelJockey",
            CREEPSConfig.cameljockey_ID,
            CREEPSConfig.scameljockey,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityCaveman.class,
            "Caveman",
            CREEPSConfig.caveman_ID,
            CREEPSConfig.scaveman,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityDesertLizard.class,
            "DesertLizard",
            CREEPSConfig.desertlizard_ID,
            CREEPSConfig.sdesertlizard,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityDigBug.class,
            "DigBug",
            CREEPSConfig.digbug_ID,
            CREEPSConfig.sdigbug,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityEvilScientist.class,
            "EvilScientist",
            CREEPSConfig.evilscientist_ID,
            CREEPSConfig.sevilscientist,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityFloob.class,
            "Floob",
            CREEPSConfig.floob_ID,
            CREEPSConfig.sfloob,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityFloobShip.class,
            "FloobShip",
            CREEPSConfig.floobship_ID,
            CREEPSConfig.sfloobship,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        // todo
      /*  addMob(
            CREEPSEntityG.class,
            "G",
            CREEPSConfig.g_ID,
            CREEPSConfig.sg,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());

       */
        addMob(
            CREEPSEntityGooGoat.class,
            "GooGoat",
            CREEPSConfig.googoat_ID,
            CREEPSConfig.sgoogoat,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityGuineaPig.class,
            "GuineaPig",
            CREEPSConfig.guineapig_ID,
            CREEPSConfig.sguineapig,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityHippo.class,
            "Hippo",
            CREEPSConfig.hippo_ID,
            CREEPSConfig.shippo,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityHorseHead.class,
            "HorseHead",
            CREEPSConfig.horsehead_ID,
            CREEPSConfig.shorsehead,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());

        addMob(
            CREEPSEntityHotdog.class,
            "Hotdog",
            CREEPSConfig.hotdog_ID,
            CREEPSConfig.shotdog,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityHunchback.class,
            "Hunchback",
            CREEPSConfig.hunchback_ID,
            CREEPSConfig.shunchback,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        // todo terminate the mod
    /*    addMob(
            CREEPSEntityInvisibleMan.class,
            "InvisibleMan",
            CREEPSConfig.invisibleman_ID,
            CREEPSConfig.sinvisibleman,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());

     */
        addMob(
            CREEPSEntityKid.class,
            "Kid",
            CREEPSConfig.kid_ID,
            CREEPSConfig.skid,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityLawyerFromHell.class,
            "LawyerFromHell",
            CREEPSConfig.lawyer_ID,
            CREEPSConfig.slawyerfromhell,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityLolliman.class,
            "Lolliman",
            CREEPSConfig.lolliman_ID,
            CREEPSConfig.slolliman,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityManDog.class,
            "ManDog",
            CREEPSConfig.mandog_ID,
            CREEPSConfig.smandog,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        // todo
        /*
        addMob(
            CREEPSEntityMummy.class,
            "Mummy",
            CREEPSConfig.mummy_ID,
            CREEPSConfig.smummy,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());

         */
        addMob(
            CREEPSEntityNonSwimmer.class,
            "NonSwimmer",
            CREEPSConfig.nonswimmer_ID,
            CREEPSConfig.snonswimmer,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityPreacher.class,
            "Preacher",
            CREEPSConfig.preacher_ID,
            CREEPSConfig.spreacher,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityRobotTed.class,
            "RobotTed",
            CREEPSConfig.robotted_ID,
            CREEPSConfig.srobotted,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        // todo
        /*
        addMob(
            CREEPSEntityRobotTodd.class,
            "RobotTodd",
            CREEPSConfig.robottodd_ID,
            CREEPSConfig.srobottodd,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());

         */
        addMob(
            CREEPSEntityRocketGiraffe.class,
            "RocketGiraffe",
            CREEPSConfig.rocketgiraffe_ID,
            CREEPSConfig.srocketgiraffe,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityRockMonster.class,
            "RockMonster",
            CREEPSConfig.rockmonster_ID,
            CREEPSConfig.srockmonster,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntitySneakySal.class,
            "SneakySal",
            CREEPSConfig.sneakysal_ID,
            CREEPSConfig.ssneakysal,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntitySnowDevil.class,
            "SnowDevil",
            CREEPSConfig.snowdevil_ID,
            CREEPSConfig.ssnowdevil,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityThief.class,
            "Thief",
            CREEPSConfig.thief_ID,
            CREEPSConfig.sthief,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        addMob(
            CREEPSEntityZebra.class,
            "Zebra",
            CREEPSConfig.zebra_ID,
            CREEPSConfig.szebra,
            1,
            4,
            EnumCreatureType.creature,
            allBiomes());
        proxy.render();
        proxy.renderModelItem();
        // Registers Recipes
        CREEPSRecipeHandler.Init(event);
    }

    public void addMob(Class<? extends EntityLiving> classz, String name, int id, int weightedProb, int min, int max,
        EnumCreatureType typeOfCreature, BiomeGenBase... biomes) {
        EntityRegistry
            .registerGlobalEntityID(classz, name, EntityRegistry.findGlobalUniqueEntityId(), 0x000000, 0xFFFFFF);
        EntityRegistry.registerModEntity(classz, name, id, this, 40, 1, true);
        if (weightedProb > 0) {
            EntityRegistry.addSpawn(classz, weightedProb, min, max, typeOfCreature, biomes);
        }
    }

    public BiomeGenBase[] allBiomes() {
        return new BiomeGenBase[] { BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.extremeHills,
            BiomeGenBase.forest, BiomeGenBase.taiga, BiomeGenBase.swampland, BiomeGenBase.icePlains,
            BiomeGenBase.iceMountains, BiomeGenBase.beach, BiomeGenBase.desertHills, BiomeGenBase.forestHills,
            BiomeGenBase.taigaHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.jungle, BiomeGenBase.stoneBeach };
    }
}
