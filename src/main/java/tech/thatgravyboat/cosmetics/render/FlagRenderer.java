package tech.thatgravyboat.cosmetics.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import tech.thatgravyboat.cosmetics.FlagStorage;
import tech.thatgravyboat.cosmetics.types.Flag;

import java.util.UUID;

public class FlagRenderer {

    @SubscribeEvent
    public void onNameTagRenderer(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        RendererLivingEntity<EntityLivingBase> renderer = event.renderer;
        EntityLivingBase entity = event.entity;

        if (!(entity instanceof AbstractClientPlayer)) return;

        RenderManager renderManager = renderer.getRenderManager();

        if (!canRenderName(renderManager, entity)) return;

        double distanceToEntity = entity.getDistanceSqToEntity(renderManager.livingPlayer);
        float maxDistance = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;

        UUID uuid = entity.getUniqueID();
        Flag userFlag = FlagStorage.getUserFlag(uuid);
        if (userFlag == null) return;
        ResourceLocation location = userFlag.getTexture();
        if (location == null) return;

        if (distanceToEntity < (maxDistance * maxDistance)) {

            String str = entity.getDisplayName().getFormattedText();

            double x = event.x;
            double y = event.y;
            double z = event.z;

            FontRenderer fontrenderer = renderer.getFontRendererFromRenderManager();
            float f1 = 0.016666668F * 1.6F;
            GlStateManager.pushMatrix();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.translate((float)x + 0.0F, (float)y + entity.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = str.equals("deadmau5") ? -10 : 0;
            int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(j + 1.0, -1.0 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(j + 1.0, 8.0 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(j + 11.0, 8.0 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos(j + 11.0, -1.0 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();

            renderer.bindTexture(location);

            worldrenderer = tessellator.getWorldRenderer();

            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldrenderer.pos(j + 1.0, 0.25+i, 0).tex(0, 10d/72d).color(1f,1f,1f,1f).endVertex();
            worldrenderer.pos(j + 1.0, 6.75 + i, 0).tex(0, 62d/72d).color(1f,1f,1f,1f).endVertex();
            worldrenderer.pos(j + 10.0, 6.75 + i, 0).tex(1, 62d/72d).color(1f,1f,1f,1f).endVertex();
            worldrenderer.pos(j + 10.0, 0.25+i, 0).tex(1, 10d/72d).color(1f,1f,1f,1f).endVertex();
            tessellator.draw();

            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    protected static boolean canRenderName(RenderManager renderManager, EntityLivingBase entity) {
        EntityPlayerSP entityplayersp = Minecraft.getMinecraft().thePlayer;

        if (entity instanceof EntityPlayer && entity != entityplayersp) {
            Team team = entity.getTeam();
            Team team1 = entityplayersp.getTeam();

            if (team != null) {
                switch (team.getNameTagVisibility()) {
                    case NEVER: return false;
                    case HIDE_FOR_OTHER_TEAMS: return team1 == null || team.isSameTeam(team1);
                    case HIDE_FOR_OWN_TEAM: return team1 == null || !team.isSameTeam(team1);
                    default: return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && entity != renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }
}
