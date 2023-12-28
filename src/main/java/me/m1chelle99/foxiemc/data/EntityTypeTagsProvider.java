package me.m1chelle99.foxiemc.data;

import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public final class EntityTypeTagsProvider extends net.minecraft.data.tags.EntityTypeTagsProvider {
    public static final String NAME = "foxiemc_entity_type_tags";

    public EntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this
            .tag(EntityTypeTags.POWDER_SNOW_WALKABLE_MOBS)
            .add(EntityInit.FOXIE.get());

        this
            .tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
            .add(EntityInit.FOXIE.get());
    }

    public @NotNull String getName() {
        return NAME;
    }
}
