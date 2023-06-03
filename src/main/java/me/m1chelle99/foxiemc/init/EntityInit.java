package me.m1chelle99.foxiemc.init;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entities.Foxie;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, FoxieMCMod.ID);
    public static final RegistryObject<EntityType<Foxie>> FOXIE = ENTITIES.register(Foxie.ID, () -> EntityType.Builder
            .of(Foxie::new, MobCategory.AMBIENT)
            .build(FoxieMCMod.ID + ":foxie"));
}
