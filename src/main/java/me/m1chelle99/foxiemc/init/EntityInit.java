package me.m1chelle99.foxiemc.init;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, FoxieMCMod.ID);
    
    public static final RegistryObject<EntityType<Foxie>> FOXIE =
        ENTITIES.register(Foxie.ID, () -> EntityType.Builder
            .of(Foxie::new, MobCategory.AMBIENT)
            .build(FoxieMCMod.ID + ":foxie"));
}
