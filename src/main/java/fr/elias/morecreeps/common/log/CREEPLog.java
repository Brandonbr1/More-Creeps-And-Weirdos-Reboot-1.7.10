package fr.elias.morecreeps.common.log;

public class CREEPLog {

    public static final boolean dev = true;

    public static void logDbgOnly(Object c, String s) {
        if (dev) {
            System.out.println(c.toString() + s);
        }
    }

}
