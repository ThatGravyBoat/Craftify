package tech.thatgravyboat.cosmetics;

import com.google.gson.*;
import com.mojang.util.UUIDTypeAdapter;
import org.apache.commons.io.IOUtils;
import tech.thatgravyboat.cosmetics.types.Flag;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FlagStorage {

    private static final Gson GSON = new Gson();

    private static Flag defaultFlag = null;

    private static final Map<String, Flag> FLAGS = new HashMap<>();
    private static final Map<UUID, String> USERS = new HashMap<>();

    private FlagStorage() throws IllegalAccessException {
        throw new IllegalAccessException("FlagStorage use is only in static context.");
    }

    public static void loadApi() {
        try {
            URL url = new URL("https://cosmetics.thatgravyboat.tech/icons");
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (Gravy Cosmetics)");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            String rep = IOUtils.toString(new BufferedInputStream(connection.getInputStream()), StandardCharsets.UTF_8);
            JsonObject json = GSON.fromJson(rep, JsonObject.class);

            JsonElement icons = json.get("icons");
            if (icons.isJsonArray()) {
                for (JsonElement icon : icons.getAsJsonArray()) {
                    try {
                        Flag flag = GSON.fromJson(icon, Flag.class);
                        FLAGS.putIfAbsent(flag.getId(), flag);
                    }catch (Exception ignored) {
                        //Dont add flag to cache if an error happens serializing it.
                    }
                }
            }

            JsonElement users = json.get("users");
            if (users.isJsonObject()) {
                for (Map.Entry<String, JsonElement> user : users.getAsJsonObject().entrySet()) {
                    if (user.getValue().isJsonPrimitive()){
                        addUser(user.getKey(), user.getValue().getAsString());
                    }
                }
            }

            JsonElement defaultIcon = json.get("default");
            if (defaultIcon.isJsonPrimitive()) {
                defaultFlag = FLAGS.get(defaultIcon.getAsString());
            }
        }catch (Exception ignored){
            //Does nothing
        }
    }

    private static void addUser(String uuid, String icon){
        try {
            getUUID(uuid).ifPresent(uid -> USERS.put(uid, icon));
        } catch (Exception ignored) {
            //ignore if uuid is invalid.
        }
    }

    private static Optional<UUID> getUUID(String uuid) throws IllegalArgumentException {
        if (uuid.length() == 36 && uuid.contains("-")) {
            return Optional.of(UUIDTypeAdapter.fromString(uuid));
        } else if (uuid.length() == 32) {
            return Optional.of(UUID.fromString(uuid));
        }
        return Optional.empty();
    }

    public static Flag getUserFlag(UUID uuid) {
        String id = USERS.get(uuid);
        if (id == null) return defaultFlag;
        return FLAGS.getOrDefault(id, defaultFlag);
    }


}
