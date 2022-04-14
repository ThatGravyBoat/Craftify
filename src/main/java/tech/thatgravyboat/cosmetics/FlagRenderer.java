//#if MODERN==0
package tech.thatgravyboat.cosmetics;

import gg.essential.universal.UGraphics;
import gg.essential.universal.UMatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.UUID;

public class FlagRenderer {

    @SubscribeEvent
    public void onNameTagRenderer(RenderLivingEvent.Specials.Pre<EntityLivingBase> event) {
        //#if MC==10809
        RendererLivingEntity<EntityLivingBase> renderer = event.renderer;
        EntityLivingBase entity = event.entity;
        //#else
        //$$ RenderLivingBase<EntityLivingBase> renderer = event.getRenderer();
        //$$ EntityLivingBase entity = event.getEntity();
        //#endif

        if (!(entity instanceof AbstractClientPlayer)) return;

        RenderManager renderManager = renderer.getRenderManager();

        if (!canRenderName(renderManager, entity)) return;

        double distanceToEntity = entity.getDistanceSqToEntity(renderManager.livingPlayer);
        float maxDistance = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;

        FlagCosmetics cosmetics = Cosmetics.getCosmetics();
        if (cosmetics == null) return;

        UUID uuid = entity.getUniqueID();
        Flag userFlag = cosmetics.getUserFlag(uuid);
        if (userFlag == null) return;
        userFlag.getTextureId().ifPresent(id -> {

            UMatrixStack stack = new UMatrixStack();
            if (distanceToEntity < (maxDistance * maxDistance)) {
                String str = entity.getDisplayName().getFormattedText();

                stack.push();
                GlStateManager.alphaFunc(516, 0.1F);
                //#if MC==10809
                stack.translate(event.x, event.y + entity.height + 0.5, event.z);
                //#else
                //$$ stack.translate(event.getX(), event.getY() + entity.height + 0.5, event.getZ());
                //#endif
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                stack.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                stack.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                stack.scale(-0.02666667f, -0.02666667f, -0.02666667f);
                if (entity.isSneaking())
                    stack.translate(0f, 9.374999F, 0f);
                UGraphics.disableLighting();
                UGraphics.enableBlend();
                UGraphics.tryBlendFuncSeparate(770, 771, 1, 0);

                UGraphics uRenderer = UGraphics.getFromTessellator();
                int offset = str.equals("deadmau5") ? -10 : 0;
                int center = UGraphics.getStringWidth(str) / 2;
                uRenderer.beginWithActiveShader(UGraphics.DrawMode.QUADS, DefaultVertexFormats.POSITION_COLOR);
                uRenderer.pos(stack, center + 1.0, -1.0 + offset, 0).color(0f, 0f, 0f, 0.25f).endVertex();
                uRenderer.pos(stack, center + 1.0, 8.0 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                uRenderer.pos(stack, center + 11.0, 8.0 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                uRenderer.pos(stack, center + 11.0, -1.0 + offset, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                uRenderer.drawDirect();

                UGraphics.bindTexture(0, id);

                float alpha = entity.isSneaking() ? 0.25f : 1f;

                GlStateManager.doPolygonOffset(-3.0F, -3.0F);
                GlStateManager.enablePolygonOffset();

                uRenderer.beginWithActiveShader(UGraphics.DrawMode.QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                uRenderer.pos(stack, center + 1.0, 0.25 + offset, 0).tex(0, 10d/72d).color(1f,1f,1f, alpha).endVertex();
                uRenderer.pos(stack, center + 1.0, 6.75 + offset, 0).tex(0, 62d/72d).color(1f,1f,1f, alpha).endVertex();
                uRenderer.pos(stack, center + 10.0, 6.75 + offset, 0).tex(1, 62d/72d).color(1f,1f,1f, alpha).endVertex();
                uRenderer.pos(stack, center + 10.0, 0.25 + offset, 0).tex(1, 10d/72d).color(1f,1f,1f, alpha).endVertex();
                uRenderer.drawDirect();

                GlStateManager.doPolygonOffset(0f, 0f);
                GlStateManager.disablePolygonOffset();


                UGraphics.enableDepth();
                UGraphics.depthMask(true);
                UGraphics.enableLighting();
                UGraphics.disableBlend();
                UGraphics.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                stack.pop();
            }
        });
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

        return Minecraft.isGuiEnabled() && entity != renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) &&
                //#if MC==10809
                entity.riddenByEntity == null;
                //#else
                //$$ !entity.isBeingRidden();
                //#endif
    }
}
//#endif