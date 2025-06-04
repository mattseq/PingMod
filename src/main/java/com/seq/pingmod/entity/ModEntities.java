package com.seq.pingmod.entity;

import com.seq.pingmod.PingMod;
import com.seq.pingmod.entity.client.PingEntityRenderer;
import com.seq.pingmod.entity.custom.PingEntity;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PingMod.MODID);


    public static final RegistryObject<EntityType<PingEntity>> PING_ENTITY =
            REGISTRY.register("ping_entity",
                    () -> EntityType.Builder.<PingEntity>of(PingEntity::new, MobCategory.MISC)
                            .sized(0.5f, 1.5f)
                            .build(new ResourceLocation(PingMod.MODID, "ping_entity").toString()));

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {

    }


    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.PING_ENTITY.get(), PingEntityRenderer::new);
    }
}