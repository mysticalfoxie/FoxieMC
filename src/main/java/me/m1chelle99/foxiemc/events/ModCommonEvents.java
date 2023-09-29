package me.m1chelle99.foxiemc.events;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.data.EntityTypeTagsProvider;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(
        modid = FoxieMCMod.ID,
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public final class ModCommonEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        var foxie = EntityInit.FOXIE.get();
        var attributes = Foxie.getFoxieAttributes().build();
        event.put(foxie, attributes);
    }

    @SubscribeEvent
    public static void onDataGenerating(GatherDataEvent event) {
        var generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        var provider = new EntityTypeTagsProvider(generator, helper);
        generator.addProvider(provider);
        FoxieMCMod.LOGGER.info("Start tagging foxies in 2023, what a disaster");
    }
}
