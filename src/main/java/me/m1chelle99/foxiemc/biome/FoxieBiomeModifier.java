package me.m1chelle99.foxiemc.biome;

import com.mojang.serialization.Codec;
import me.m1chelle99.foxiemc.init.BiomeInit;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record FoxieBiomeModifier(HolderSet<Biome> biomes) 
    implements BiomeModifier {
    public static final String ID = "foxie_biome_modifier";

    @Override
    public void modify(
        Holder<Biome> biome,
        Phase phase,
        ModifiableBiomeInfo.BiomeInfo.Builder builder
    ) {
        if (phase != Phase.ADD) return;
        if (biome == null) return;

        this.removeVanillaFox(builder);

        if (!this.doesFoxieSpawnIn(biome)) return;
        
        this.addFoxieFor(Biomes.TAIGA, biome, builder);
        this.addFoxieFor(Biomes.FOREST, biome, builder);
        this.addFoxieFor(Biomes.PLAINS, biome, builder);
        this.addFoxieFor(Biomes.BIRCH_FOREST, biome, builder);
        this.addFoxieFor(Biomes.DARK_FOREST, biome, builder);
        this.addFoxieFor(Biomes.FLOWER_FOREST, biome, builder);
        this.addFoxieFor(Biomes.GROVE, biome, builder);
        this.addFoxieFor(Biomes.RIVER, biome, builder);
        this.addFoxieFor(Biomes.OLD_GROWTH_BIRCH_FOREST, biome, builder);
        this.addFoxieFor(Biomes.OLD_GROWTH_PINE_TAIGA, biome, builder);
        this.addFoxieFor(Biomes.OLD_GROWTH_SPRUCE_TAIGA, biome, builder);
        this.addFoxieFor(Biomes.SWAMP, biome, builder);
        this.addFoxieFor(Biomes.WINDSWEPT_FOREST, biome, builder);
    }
    
    private boolean doesFoxieSpawnIn(Holder<Biome> biome) {
        if (biome.is(Biomes.TAIGA)) return true;
        if (biome.is(Biomes.FOREST)) return true;
        if (biome.is(Biomes.PLAINS)) return true;
        if (biome.is(Biomes.BIRCH_FOREST)) return true;
        if (biome.is(Biomes.DARK_FOREST)) return true;
        if (biome.is(Biomes.FLOWER_FOREST)) return true;
        if (biome.is(Biomes.GROVE)) return true;
        if (biome.is(Biomes.RIVER)) return true;
        if (biome.is(Biomes.OLD_GROWTH_BIRCH_FOREST)) return true;
        if (biome.is(Biomes.OLD_GROWTH_PINE_TAIGA)) return true;
        if (biome.is(Biomes.OLD_GROWTH_SPRUCE_TAIGA)) return true;
        if (biome.is(Biomes.SWAMP)) return true;
        return biome.is(Biomes.WINDSWEPT_FOREST);
    }
    
    private void addFoxieFor(
        ResourceKey<Biome> targetBiome,
        Holder<Biome> currentBiome,
        ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        //noinspection EqualsBetweenInconvertibleTypes
        if (currentBiome != targetBiome) return;
        
        var spawnData = this.getSpawnData(targetBiome);
        var settings = builder.getMobSpawnSettings();
        settings.addSpawn(MobCategory.CREATURE, spawnData);
    }
    
    private MobSpawnSettings.SpawnerData getSpawnData (
        ResourceKey<Biome> biome) {
        var foxie = EntityInit.FOXIE.get();
        
        var highPopulation = new MobSpawnSettings.SpawnerData(foxie, 12, 1, 2);
        var mediumPopulation = new MobSpawnSettings.SpawnerData(foxie, 7, 1, 2);
        var lowPopulation = new MobSpawnSettings.SpawnerData(foxie, 3, 1, 1);

        if (biome == Biomes.TAIGA)
            return mediumPopulation;
            
        if (biome == Biomes.FOREST)
            return highPopulation;
        
        if (biome == Biomes.PLAINS)
            return lowPopulation;
        
        if (biome == Biomes.BIRCH_FOREST)
            return mediumPopulation;
        
        if (biome == Biomes.DARK_FOREST)
            return highPopulation;
        
        if (biome == Biomes.FLOWER_FOREST)
            return mediumPopulation;
        
        if (biome == Biomes.GROVE)
            return lowPopulation;
        
        if (biome == Biomes.RIVER)
            return lowPopulation;
        
        if (biome == Biomes.OLD_GROWTH_BIRCH_FOREST)
            return mediumPopulation;
        
        if (biome == Biomes.OLD_GROWTH_PINE_TAIGA)
            return mediumPopulation;
        
        if (biome == Biomes.OLD_GROWTH_SPRUCE_TAIGA)
            return mediumPopulation;
        
        if (biome == Biomes.SWAMP)
            return lowPopulation;
        
        if (biome == Biomes.WINDSWEPT_FOREST)
            return mediumPopulation;
        
        return lowPopulation;
    }

    private void removeVanillaFox(
        ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        var settings = builder.getMobSpawnSettings();
        var spawner = settings.getSpawner(MobCategory.CREATURE);
        spawner.removeIf(x -> x.type == EntityType.FOX);
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return BiomeInit.FOXIE_BIOME_MOD.get();
    }
}
