package com.seq.pingmod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = PingMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModKeyBindings {
    public static final KeyMapping PING_KEY = new KeyMapping(
            "key.ping", // The translation key of the key binding's name
            InputConstants.Type.KEYSYM, // The type of the key binding, in this case, a keyboard key
            GLFW.GLFW_KEY_H, // The default key for the key binding
            "key.categories." + PingMod.MODID // The translation key of the key binding's category
    );

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(PING_KEY);
    }
}
