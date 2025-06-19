package com.seq.pingmod.entity.client;

import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public class PingEntityRenderer extends EntityRenderer<PingEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(PingMod.MODID, "textures/entity/ping.png");

    public PingEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(PingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        Minecraft mc = Minecraft.getInstance();
        Vec3 playerPosition = mc.player.position();

        // Calculate direction from player to ping
        Vec3 pingPosition = entity.position();

        double distance = pingPosition.subtract(playerPosition).length();

        // ping -> player = player - ping
        Vec3 targetVec = playerPosition.subtract(pingPosition);
        double angle = Math.atan2(targetVec.x, targetVec.z) * 180 / Math.PI;

        Quaternionf rotation = Axis.YP.rotationDegrees((float) angle);

        // Move the entity rendering slightly up so it doesn't clip into the ground and slightly toward the player
        poseStack.translate(0, 0.5, 0);
        poseStack.translate(targetVec.normalize().x, targetVec.normalize().y, targetVec.normalize().z);

        // Rotate the ping entity to face the player
        poseStack.mulPose(rotation);

        // scale by distance
        float size = 0.1f * (float) Math.pow(distance, 0.7);
        poseStack.scale(size, size, size);

        // render ping
        renderTexturedQuad(poseStack, bufferSource, 15728640);

        // Translate to render text slightly below the ping
        poseStack.translate(0, -1.5, 0);
        Quaternionf rotationFlip = Axis.XP.rotationDegrees(180);
        poseStack.mulPose(rotationFlip);
        poseStack.scale(.05f, .05f, .05f);
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

    private void renderTexturedQuad(PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        int overlay = OverlayTexture.NO_OVERLAY;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.textSeeThrough(TEXTURE)); // Use translucent for alpha

        // Define the size of the quad
        float halfSize = 0.5f;

        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, -halfSize, 0)
                .color(1f, 1f, 1f, 1f)
                .uv(0f, 1f)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), halfSize, -halfSize, 0)
                .color(1f, 1f, 1f, 1f)
                .uv(1f, 1f)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), halfSize, halfSize, 0)
                .color(1f, 1f, 1f, 1f)
                .uv(1f, 0f)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();

        vertexConsumer.vertex(poseStack.last().pose(), -halfSize, halfSize, 0)
                .color(1f, 1f, 1f, 1f)
                .uv(0f, 0f)
                .overlayCoords(overlay)
                .uv2(light)
                .normal(0, 1, 0)
                .endVertex();
    }


    @Override
    public ResourceLocation getTextureLocation(PingEntity entity) {
        return TEXTURE;
    }
}