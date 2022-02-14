package tech.thatgravyboat.cosmetics;

public class Cosmetics {

    private static boolean initalized = false;

    private Cosmetics() throws IllegalAccessException {
        throw new IllegalAccessException("Cosmetics use is only in static context.");
    }

    public static void initalize() {
        if (initalized) return;
        initalized = true;
        //MinecraftForge.EVENT_BUS.register(new FlagRenderer());
        //FlagStorage.loadApi();
    }
}
