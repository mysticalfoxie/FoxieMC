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

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FoxieMCMod.ID);

    public static Item.Properties props() {
        return new Item.Properties().tab(CREATIVE_MODE_TAB);
    }

    public static final CreativeModeTab CREATIVE_MODE_TAB = new CreativeModeTab(FoxieMCMod.ID) {
        @Override
        public @NotNull ItemStack makeIcon() {
            return ItemInit.FOXIE_SPAWN_EGG.get().getDefaultInstance();
        }
    };


    public static final RegistryObject<ForgeSpawnEggItem> FOXIE_SPAWN_EGG = ITEMS.register("foxie_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityInit.FOXIE, 0x0, 0xffffff, props().stacksTo(16)));
}
