package me.m1chelle99.foxiemc.init;

import me.m1chelle99.foxiemc.FoxieMCMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister
        .create(ForgeRegistries.ITEMS, FoxieMCMod.ID);
    
    public static final RegistryObject<ForgeSpawnEggItem> FOXIE_SPAWN_EGG =
        ITEMS.register("foxie_spawn_egg", createSpawnEggItem());

    public static final CreativeModeTab CREATIVE_MODE_TAB =
        new CreativeModeTab(FoxieMCMod.ID) {
            @Override
            public @NotNull ItemStack makeIcon() {
                return ItemInit.FOXIE_SPAWN_EGG.get().getDefaultInstance();
            }
        };

    @NotNull
    private static Supplier<ForgeSpawnEggItem> createSpawnEggItem() {
        return () -> new ForgeSpawnEggItem(
            EntityInit.FOXIE, 0x777777, 0xffffff, props().stacksTo(16));
    }

    public static Item.Properties props() {
        return new Item.Properties().tab(CREATIVE_MODE_TAB);
    }
}
