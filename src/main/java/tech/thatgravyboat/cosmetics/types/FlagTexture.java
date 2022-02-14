package tech.thatgravyboat.cosmetics.types;

import gg.essential.api.utils.Multithreading;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Future;

public class FlagTexture extends SimpleTexture {

    private final Flag flag;
    private final File cache;

    private BufferedImage bufferedImage = null;
    private boolean uploaded = false;
    private Future<?> future = null;

    public FlagTexture(Flag flag) {
        super(new ResourceLocation("textures/misc/forcefield.png"));
        this.flag = flag;
        this.cache = new File("gravy-cosmetics/icons/"+flag.getId());
    }

    private void downloadTexture() {
        future = Multithreading.INSTANCE.submit(() -> {
            HttpURLConnection connection = null;
            boolean errored = false;

            try {
                connection = ((HttpURLConnection) (new URL(flag.getUrl())).openConnection());
                connection.setDoInput(true);
                connection.connect();

                if (connection.getResponseCode() / 100 == 2) {
                    FileUtils.copyInputStreamToFile(connection.getInputStream(), cache);
                    bufferedImage = ImageIO.read(connection.getInputStream());
                }else {
                    errored = true;
                }
            }catch (Exception e) {
                e.printStackTrace();
                errored = true;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }

            if (errored) flag.error();
        });
    }

    @Override
    public int getGlTextureId() {
        if (!uploaded && bufferedImage != null) {
            if (textureLocation != null) deleteGlTexture();
            TextureUtil.uploadTextureImage(super.getGlTextureId(), bufferedImage);
            uploaded = true;
        }
        return super.getGlTextureId();
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        if (bufferedImage == null && textureLocation != null) {
            super.loadTexture(resourceManager);
        }

        if (future == null) {
            if (cache.isFile()) {
                try {
                    bufferedImage = ImageIO.read(cache);
                } catch (Exception e) {
                    downloadTexture();
                }
            } else {
                downloadTexture();
            }
        }
    }
}
