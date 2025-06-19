package com.seq.pingmod.events;

import com.seq.pingmod.PingMod;
import com.seq.pingmod.ModKeyBindings;
import com.seq.pingmod.networking.PacketHandler;
import com.seq.pingmod.networking.PingC2SPacket;
import com.seq.pingmod.sounds.ModSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;

@Mod.EventBusSubscriber(modid = PingMod.MODID, value = Dist.CLIENT)
public class PingKey {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && ModKeyBindings.PING_KEY.consumeClick()) {
            Player player = mc.player;
            Vec3 targetPos = getTargetPos(mc.player, player.level(), 100);
            player.level().playSound(player, player.blockPosition(), ModSoundEvents.PING.get(), SoundSource.PLAYERS, .35f, 1);
            PacketHandler.sendToServer(new PingC2SPacket(targetPos.x, targetPos.y, targetPos.z));
        }
    }

    public static Vec3 getTargetPos(LocalPlayer player, Level level, int range) {
        double distance = 0;
        for (int index0 = 0; index0 < range; index0++) {
            if (!level
                    .getEntitiesOfClass(LivingEntity.class, AABB.ofSize(new Vec3(
                                    (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                            player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                            ClipContext.Fluid.NONE, player)).getBlockPos().getX()),
                                    (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                            player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                            ClipContext.Fluid.NONE, player)).getBlockPos().getY()),
                                    (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                            player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                            ClipContext.Fluid.NONE, player)).getBlockPos().getZ())),
                            1, 1, 1), e -> true)
                    .isEmpty()) {
                Entity foundEntity = ((Entity) level
                        .getEntitiesOfClass(LivingEntity.class,
                                AABB.ofSize(
                                        new Vec3(
                                                (player.level()
                                                        .clip(new ClipContext(player.getEyePosition(1f),
                                                                player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)),
                                                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                                                        .getBlockPos().getX()),
                                                (player.level()
                                                        .clip(new ClipContext(player.getEyePosition(1f),
                                                                player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)),
                                                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                                                        .getBlockPos().getY()),
                                                (player.level()
                                                        .clip(new ClipContext(player.getEyePosition(1f),
                                                                player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)),
                                                                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player))
                                                        .getBlockPos().getZ())),
                                        1, 1, 1),
                                e -> true)
                        .stream().sorted(new Object() {
                            Comparator<Entity> compareDistOf(double _x, double _y, double _z) {
                                return Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_x, _y, _z));
                            }
                        }.compareDistOf(
                                (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                        player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                        ClipContext.Fluid.NONE, player)).getBlockPos().getX()),
                                (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                        player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                        ClipContext.Fluid.NONE, player)).getBlockPos().getY()),
                                (player.level().clip(new ClipContext(player.getEyePosition(1f),
                                        player.getEyePosition(1f).add(player.getViewVector(1f).scale(distance)), ClipContext.Block.COLLIDER,
                                        ClipContext.Fluid.NONE, player)).getBlockPos().getZ())))
                        .findFirst().orElse(null));
                if (foundEntity != null && (!(foundEntity == player))) {
                    return foundEntity.position();
                }

            }
            distance = distance + 1;
        }

        double x = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(range)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation().x();
        double y = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(range)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation().y();
        double z = player.level().clip(new ClipContext(player.getEyePosition(1f), player.getEyePosition(1f).add(player.getViewVector(1f).scale(range)),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getLocation().z();

        return new Vec3(x, y, z);
    }
}
