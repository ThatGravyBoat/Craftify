//#if MODERN==0
package tech.thatgravyboat.cosmetics;

import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;

public class Cosmetics {

    private static final String API = "https://cosmetics.thatgravyboat.tech/icons";

    private static boolean initialized = false;
    private static FlagCosmetics flagCosmetics = null;

    private Cosmetics() throws IllegalAccessException {
        throw new IllegalAccessException("Cosmetics use is only in static context.");
    }

    public static void initialize() {
        if (initialized) return;
        initialized = true;
        MinecraftForge.EVENT_BUS.register(new FlagRenderer());
        try {
            flagCosmetics = new FlagCosmetics(API);
        }catch (Exception ignored) {}
    }

    @Nullable
    public static FlagCosmetics getCosmetics() {
        return flagCosmetics;
    }
}
//#endif