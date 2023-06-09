package me.m1chelle99.foxiemc.events;


import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.data.EntityTypeTagsProvider;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = FoxieMCMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCommonEvents {
    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityInit.FOXIE.get(), Foxie.getFoxieAttributes().build());
    }

    @SubscribeEvent
    public static void onDataGenerating(GatherDataEvent event) {
        var generator = event.getGenerator();
        var provider = new EntityTypeTagsProvider(generator, event.getExistingFileHelper());
        generator.addProvider(provider);
        FoxieMCMod.LOGGER.info("Start tagging foxies in 2023, what a disaster");
    }
}
