package tech.thatgravyboat.cosmetics;

import net.minecraftforge.common.MinecraftForge;
import tech.thatgravyboat.cosmetics.render.FlagRenderer;

public class Cosmetics {

    private static boolean initialized = false;

    private Cosmetics() throws IllegalAccessException {
        throw new IllegalAccessException("Cosmetics use is only in static context.");
    }

    public static void initialize() {
        if (initialized) return;
        initialized = true;
        MinecraftForge.EVENT_BUS.register(new FlagRenderer());
        FlagStorage.loadApi();
    }
}
