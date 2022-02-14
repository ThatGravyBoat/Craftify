package tech.thatgravyboat.cosmetics.types;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;

public class Flag {

    public static final String NAMESPACE = "cosmeticflags";

    private final String id;
    private final String url;

    private transient SimpleTexture texture = null;
    private transient boolean delete = false;
    private transient ResourceLocation location = null;

    public Flag(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void error() {
        delete = true;
        location = null;
    }

    public ResourceLocation getTexture() {
        if (texture == null) {
            texture = new FlagTexture(this);
            location = new ResourceLocation(NAMESPACE, "textures/flags/"+getId());
            Minecraft.getMinecraft().renderEngine.loadTexture(location, texture);
        } else if (delete){
            texture.deleteGlTexture();
            delete = false;
        }
        return location;
    }
}
