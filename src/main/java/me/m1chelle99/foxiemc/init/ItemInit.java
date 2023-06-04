package me.m1chelle99.foxiemc.init;

import me.m1chelle99.foxiemc.FoxieMCMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FoxieMCMod.ID);
    public static final RegistryObject<ForgeSpawnEggItem> FOXIE_SPAWN_EGG = ITEMS.register("foxie_spawn_egg",
            () -> new ForgeSpawnEggItem(EntityInit.FOXIE, 0x0, 0xffffff, props().stacksTo(16)));

    public static Item.Properties props() {
        return new Item.Properties().tab(FoxieMCMod.TAB);
    }
}
