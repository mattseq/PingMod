package com.seq.pingmod.entity.client;

import com.seq.pingmod.PingMod;
import com.seq.pingmod.entity.custom.PingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class PingEntityRenderer extends EntityRenderer<PingEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(PingMod.MODID, "textures/entity/ping2.png");

    public PingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        // Move the entity rendering slightly up so it doesn't clip into the ground
        poseStack.translate(0, 0.5, 0);

        Minecraft mc = Minecraft.getInstance();
        Vec3 playerPosition = mc.player.position();
        Vec3 lookDirection = mc.player.getLookAngle();

        // Calculate direction from player to ping
        Vec3 pingPosition = entity.position();
        Vec3 directionToPing = pingPosition.subtract(playerPosition).normalize();

        double distance = pingPosition.subtract(playerPosition).length();

        // Calculate the angle between the look direction and the direction to the ping
//        double angle = calculateFinalAngle(Minecraft.getInstance().player.getForward(), playerPosition, pingPosition);

        // ping -> player = player - ping
        Vec3 targetVec = playerPosition.subtract(pingPosition);
        double angle = Math.atan2(targetVec.x, targetVec.z) * 180 / Math.PI;

        Quaternionf rotation = Axis.YP.rotationDegrees((float) angle);

        // Rotate the ping entity to face the player
        poseStack.mulPose(rotation);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.guiOverlay());

        // scale by distance
        float size = 0.05f * (float) Math.pow(distance, 0.7);
        poseStack.scale(size, size, size);

//        renderTexturedQuad(poseStack, vertexConsumer, 15728640);

        // render diamond
        renderDiamondOutline(poseStack, vertexConsumer, packedLight);
        renderTexturedDiamond(poseStack, vertexConsumer, packedLight);

        // Translate to render text slightly below the ping
        poseStack.translate(0, -1.5, 0);
        Quaternionf rotationFlip = Axis.XP.rotationDegrees(180);
        poseStack.mulPose(rotationFlip);
        poseStack.scale(.15f, .15f, .15f);
        renderDistanceText(poseStack, bufferSource, entity, distance);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    private void renderDistanceText(PoseStack poseStack, MultiBufferSource bufferSource, PingEntity entity, double distance) {
        Minecraft mc = Minecraft.getInstance();
        String distanceText = String.format("%,.0f blocks", distance);
        Font font = mc.font;

        // Calculate the position to center the text
        float textWidth = font.width(distanceText);
        poseStack.translate(-textWidth / 2.0, 0, 0);

        // Render the text
        Font.DisplayMode displayMode = Font.DisplayMode.SEE_THROUGH;
        font.drawInBatch(distanceText, 0, 0, 0xFFFFFF, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
    }

    private void renderSimpleBox(PoseStack poseStack, VertexConsumer vertexConsumer, int light) {
        // Vertex format: vertex(x, y, z, u, v, red, green, blue, alpha, overlay, light, normalX, normalY, normalZ)

        float red = 0.0f, green = 1.0f, blue = 1.0f, alpha = 1f; // semi-transparent red
        float black = 0.0f;
        int overlay = OverlayTexture.NO_OVERLAY;

        vertexConsumer.vertex(poseStack.last().pose(), -1, 0, 1).color(red, green, blue, alpha).uv(0, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, -1, 1).color(red, green, blue, alpha).uv(1, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 1, 0, 1).color(red, green, blue, alpha).uv(1, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, 1, 1).color(red, green, blue, alpha).uv(0, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), -1, 0, 1).color(black, black, black, alpha).uv(0, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, -1, 1).color(black, black, black, alpha).uv(1, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 1, 0, 1).color(black, black, black, alpha).uv(1, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, 1, 1).color(black, black, black, alpha).uv(0, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();

    }

    private void renderTexturedQuad(PoseStack poseStack, VertexConsumer vertexConsumer, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;

        vertexConsumer.vertex(poseStack.last().pose(), -0.5f, -0.5f, 0).color(1.0f, 1.0f, 1.0f, 1.0f).uv(0, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0.5f, -0.5f, 0).color(1.0f, 1.0f, 1.0f, 1.0f).uv(1, 0).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0.5f, 0.5f, 0).color(1.0f, 1.0f, 1.0f, 1.0f).uv(1, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), -0.5f, 0.5f, 0).color(1.0f, 1.0f, 1.0f, 1.0f).uv(0, 1).overlayCoords(overlay).uv2(light).normal(0, 1, 0).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(PingEntity entity) {
        return TEXTURE;
    }

    private void renderDiamondOutline(PoseStack poseStack, VertexConsumer vertexConsumer, int light) {
        // Render outline slightly larger than the texture
        float outlineSize = 0.55f;
        float outlineWidth = 0.05f; // Width of the outline

        // Draw the outline by drawing multiple slightly larger diamonds
        renderDiamond(poseStack, vertexConsumer, light, outlineSize, 0f, 0f, 0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f); // White color for the outline
    }

    private void renderTexturedDiamond(PoseStack poseStack, VertexConsumer vertexConsumer, int light) {
        // Render the actual diamond texture
        float size = 0.5f;
        renderDiamond(poseStack, vertexConsumer, light, size, 0.0f, 1f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f); // Blue color for the diamond
    }

    private void renderDiamond(PoseStack poseStack, VertexConsumer vertexConsumer, int light, float size, float r, float g, float b, float a, float u0, float v0, float u1, float v1) {
        int overlay = OverlayTexture.NO_OVERLAY;

        // Define vertices for a diamond shape
        vertexConsumer.vertex(poseStack.last().pose(), 0, size, 0)
                .color(r, g, b, a)
                .uv(0.5f, 1.0f)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), -size, 0, 0)
                .color(r, g, b, a)
                .uv(u0, v0)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), 0, -size, 0)
                .color(r, g, b, a)
                .uv(u1, v1)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), size, 0, 0)
                .color(r, g, b, a)
                .uv(u0, v0)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();
    }

    private static double calculateFinalAngle(Vec3 playerForward, Vec3 playerPosition, Vec3 pingPosition) {
        double playerAngle = Math.atan2(playerForward.x, playerForward.z) * 180 / Math.PI;

        Vec2 targetVec = new Vec2((float) pingPosition.x, (float) pingPosition.z).add(new Vec2((float) playerPosition.x, (float) playerPosition.z).negated());

        double enemyAngle = Math.atan2(targetVec.x, targetVec.y) * 180 / Math.PI;

        double finalAngle = enemyAngle - playerAngle;

        return -finalAngle;
    }
}
