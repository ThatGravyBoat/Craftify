package tech.thatgravyboat.cosmetics;

import gg.essential.api.utils.Multithreading;
import gg.essential.api.utils.WebUtil;
import gg.essential.universal.UGraphics;
import gg.essential.universal.utils.ReleasedDynamicTexture;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Optional;
import java.util.concurrent.Future;

public class Flag {

    private final String id;
    private final String url;

    private transient ReleasedDynamicTexture texture = null;
    private transient boolean errored = false;
    private transient Future<?> future = null;

    public Flag(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public Optional<Integer> getTextureId() {
        if (future == null) {
            File cache = new File("gravy-cosmetics/icons/"+getId());

            boolean shouldDownload = true;

            if (cache.isFile()) {
                try {
                    texture = UGraphics.getTexture(ImageIO.read(cache));
                    texture.uploadTexture();
                    shouldDownload = false;
                }catch (Exception ignored) {}
            }

            if (shouldDownload) {
                startDownload(cache);
                return Optional.empty();
            }
        }
        return !errored && texture != null ? Optional.of(texture.getGlTextureId()) : Optional.empty();
    }

    private void startDownload(File cache) {
        future = Multithreading.INSTANCE.submit(() -> {
            try {
                WebUtil.downloadToFile(url, cache, "Mozilla/4.76 (Gravy Cosmetics)");
            } catch (Exception e) {
                errored = true;
            }
        });
    }
}
