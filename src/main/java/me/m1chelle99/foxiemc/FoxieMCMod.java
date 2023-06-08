package me.m1chelle99.foxiemc;

import com.mojang.logging.LogUtils;
import me.m1chelle99.foxiemc.events.ModServerEvents;
import me.m1chelle99.foxiemc.init.EntityInit;
import me.m1chelle99.foxiemc.init.ItemInit;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FoxieMCMod.ID)
public class FoxieMCMod {
    public static final String ID = "foxiemc";
    public static final CreativeModeTab TAB = new CreativeModeTab(ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ItemInit.FOXIE_SPAWN_EGG.get().getDefaultInstance();
        }
    };

    private static final Logger LOGGER = LogUtils.getLogger();

    public FoxieMCMod() {
        var bus = FMLJavaModLoadingContext.get().getModEventBus();

        EntityInit.ENTITIES.register(bus);
        ItemInit.ITEMS.register(bus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(ModServerEvents.class);
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // Register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}
