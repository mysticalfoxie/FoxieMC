package me.m1chelle99.foxiemc.data;

import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.init.EntityInit;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class EntityTypeTagsProvider extends TagsProvider<EntityType<?>> {
    public static final String NAME = "foxiemc_entity_type_tags";

    public EntityTypeTagsProvider(
        DataGenerator generator,
        @Nullable ExistingFileHelper helper
    ) {
        //noinspection deprecation
        super(generator, Registry.ENTITY_TYPE, FoxieMCMod.ID, helper);
    }

    protected void addTags() {
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
