package me.m1chelle99.foxiemc.init;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.biome.FoxieBiomeModifier;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeInit {
    public static DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS 
        = DeferredRegister.create(
            ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, 
            FoxieMCMod.ID
        );

    public static RegistryObject<Codec<FoxieBiomeModifier>> FOXIE_BIOME_MOD 
        = SERIALIZERS.register(
            FoxieBiomeModifier.ID, 
            () -> RecordCodecBuilder.create(builder -> builder
                .group(Biome.LIST_CODEC
                    .fieldOf("biomes")
                    .forGetter(FoxieBiomeModifier::biomes))
                .apply(builder, FoxieBiomeModifier::new))
        );
}
