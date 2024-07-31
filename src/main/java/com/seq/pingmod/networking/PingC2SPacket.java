package com.seq.pingmod.networking;

import com.seq.pingmod.entity.ModEntities;
import com.seq.pingmod.entity.custom.PingEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PingC2SPacket {
    private final double x, y, z;

    public PingC2SPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // decode
    public PingC2SPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    // encode
    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ServerPlayer player = supplier.get().getSender();
            if (player != null) {
                // create PingEntity at the ping location
                Level world = player.level();
                PingEntity pingEntity = new PingEntity(ModEntities.PING_ENTITY.get(), world);
                pingEntity.setPos(x, y, z);
                world.addFreshEntity(pingEntity);
            }
        });

        context.setPacketHandled(true);

        return true;
    }
}
