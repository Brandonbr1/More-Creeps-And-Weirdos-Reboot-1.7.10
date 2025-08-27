package fr.elias.morecreeps.common.items;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CREEPSList {

    private static final Logger logger = LogManager.getLogger();
    public static Map < String, Class <? extends Entity >> stringToClassMapping = new HashMap();
    public static Map < Class <? extends Entity > , String > classToStringMapping = new HashMap();
    public static Map < Integer, Class <? extends Entity >> IDtoClassMapping = new HashMap();
    private static Map < Class <? extends Entity > , Integer > classToIDMapping = new HashMap();
    private static Map<String, Integer> stringToIDMapping = new HashMap();
    public static HashMap<Integer, CREEPSList.EntityEggInfo> entityEggs = new LinkedHashMap();
    private static int id2 = 1;

    /**
     * adds a mapping between Entity classes and both a string representation and an ID
     */
    public static void addMapping(Class claz, String sname, int id) {
        System.out.println(stringToClassMapping);
        if (stringToClassMapping.containsKey(sname))
            throw new IllegalArgumentException("ID is already registered: " + sname);
        else if (IDtoClassMapping.containsKey(Integer.valueOf(id)))
            throw new IllegalArgumentException("ID is already registered: " + id);
        else {
            stringToClassMapping.put(sname, claz);
            classToStringMapping.put(claz, sname);
            IDtoClassMapping.put(Integer.valueOf(id), claz);
            classToIDMapping.put(claz, Integer.valueOf(id));
            stringToIDMapping.put(sname, Integer.valueOf(id));
        }
    }

    /**
     * Adds a entity mapping with egg info.
     */
    public static void addMapping(Class p_75614_0_, String p_75614_1_, int p_75614_2_, int p_75614_3_, int p_75614_4_) {
        addMapping(p_75614_0_, p_75614_1_, p_75614_2_);
        entityEggs.put(Integer.valueOf(p_75614_2_), new CREEPSList.EntityEggInfo(p_75614_2_, p_75614_3_, p_75614_4_));
    }

    /**
     * Create a new instance of an entity in the world by using the entity name.
     */
    public static Entity createEntityByName(String p_75620_0_, World p_75620_1_) {
        Entity entity = null;

        try {
            Class oclass = stringToClassMapping.get(p_75620_0_);

            if (oclass != null) {
                entity = (Entity)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {p_75620_1_});
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return entity;
    }




    /**
     * Create a new instance of an entity in the world by using an entity ID.
     */
    public static Entity createEntityByID(int p_75616_0_, World p_75616_1_) {
        Entity entity = null;

        try {
            Class oclass = getClassFromID(p_75616_0_);

            if (oclass != null) {
                entity = (Entity)oclass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {p_75616_1_});
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (entity == null) {
            logger.warn("Skipping Entity with id " + p_75616_0_);
        }

        return entity;
    }

    /**
     * gets the entityID of a specific entity
     */
    public static int getEntityID(Entity p_75619_0_) {
        Class oclass = p_75619_0_.getClass();
        return classToIDMapping.containsKey(oclass) ? classToIDMapping.get(oclass).intValue() : 0;
    }

    /**
     * Return the class assigned to this entity ID.
     */
    public static Class getClassFromID(int p_90035_0_) {
        return IDtoClassMapping.get(Integer.valueOf(p_90035_0_));
    }

    /**
     * Gets the string representation of a specific entity.
     */
    public static String getEntityString(Entity p_75621_0_) {
        return classToStringMapping.get(p_75621_0_.getClass());
    }

    /**
     * Finds the class using IDtoClassMapping and classToStringMapping
     */
    public static String getStringFromID(int p_75617_0_) {
        Class oclass = getClassFromID(p_75617_0_);
        return oclass != null ? (String)classToStringMapping.get(oclass) : null;
    }



    public static void func_151514_a() {}

    public static Set func_151515_b() {
        return Collections.unmodifiableSet(stringToIDMapping.keySet());
    }

    public static void addCreepEntity(Class clazz, String name) {
        int id = id2++;
        addMapping(clazz, name, id, 0x000000, 0xFFFFFF );
    }

    public static class EntityEggInfo {
        /** The entityID of the spawned mob */
        public final int spawnedID;
        /** Base color of the egg */
        public final int primaryColor;
        /** Color of the egg spots */
        public final int secondaryColor;

        public EntityEggInfo(int p_i1583_1_, int p_i1583_2_, int p_i1583_3_) {
            this.spawnedID = p_i1583_1_;
            this.primaryColor = p_i1583_2_;
            this.secondaryColor = p_i1583_3_;
        }
    }

}
