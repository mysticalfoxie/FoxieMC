package me.m1chelle99.foxiemc.init;

import me.m1chelle99.foxiemc.FoxieMCMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = FoxieMCMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ItemInit {
    public static final DeferredRegister<CreativeModeTab> TABS
            = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FoxieMCMod.ID);

    public static final RegistryObject<CreativeModeTab> MAIN_TAB
            = TABS.register("foxiemc_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("FoxieMC"))
            .icon(() -> new ItemStack(ItemInit.FOXIE_SPAWN_EGG.get()))
            .displayItems((displayParams, output) -> {
                output.accept(ItemInit.FOXIE_SPAWN_EGG.get());
            })
            .build());

    public static final DeferredRegister<Item> ITEMS = DeferredRegister
        .create(ForgeRegistries.ITEMS, FoxieMCMod.ID);

    public static final RegistryObject<ForgeSpawnEggItem> FOXIE_SPAWN_EGG =
        ITEMS.register("foxie_spawn_egg", createSpawnEggItem());

    @NotNull
    private static Supplier<ForgeSpawnEggItem> createSpawnEggItem() {
        return () -> new ForgeSpawnEggItem(
            EntityInit.FOXIE, 0x777777, 0xffffff, props().stacksTo(16));
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == MAIN_TAB.get())
            event.accept(FOXIE_SPAWN_EGG);
    }

    public static Item.Properties props() {
        return new Item.Properties();
    }
}
