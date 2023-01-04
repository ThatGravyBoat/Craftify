//#if MODERN==0
package tech.thatgravyboat.cosmetics;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import tech.thatgravyboat.craftify.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FlagCosmetics {

    private static final Gson GSON = new Gson();

    private final Map<String, Flag> flags = new HashMap<>();
    private final Map<UUID, String> users = new HashMap<>();
    private final Flag defaultFlag;

    public FlagCosmetics(String url) {
        String rep = Utils.INSTANCE.fetchString(url);
        JsonObject json = GSON.fromJson(rep, JsonObject.class);

        JsonElement icons = json.get("icons");
        if (icons.isJsonArray()) {
            for (JsonElement icon : icons.getAsJsonArray()) {
                try {
                    Flag flag = GSON.fromJson(icon, Flag.class);
                    flags.putIfAbsent(flag.getId(), flag);
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
            defaultFlag = flags.get(defaultIcon.getAsString());
        } else {
            defaultFlag = null;
        }
    }

    private void addUser(String uuid, String icon){
        try {
            getUUID(uuid).ifPresent(uid -> users.put(uid, icon));
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

    public Flag getUserFlag(UUID uuid) {
        String id = users.get(uuid);
        if (id == null) return defaultFlag;
        return flags.getOrDefault(id, defaultFlag);
    }
}

//#endif