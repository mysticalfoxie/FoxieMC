package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.client.renderer.FoxieRenderer;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.client.model.FoxModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoxieMCMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void entityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.FOXIE.get(), FoxieRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FoxieRenderer.LAYER_LOCATION, FoxModel::createBodyLayer);
    }
}
