package me.m1chelle99.foxiemc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.client.models.FoxieModel;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import me.m1chelle99.foxiemc.entity.foxie.layers.FoxieHeldItemLayer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class FoxieRenderer extends MobRenderer<Foxie, FoxieModel> {
    public static final ResourceLocation LAYER_RESOURCE =
        new ResourceLocation(FoxieMCMod.ID, "foxie");
    public static final ModelLayerLocation LAYER_LOCATION =
        new ModelLayerLocation(LAYER_RESOURCE, "main");
    private static final ResourceLocation TEXTURE = new
        ResourceLocation(FoxieMCMod.ID, "textures/entities/foxie/foxie.png");
    private static final ResourceLocation TEXTURE_SLEEP = new
        ResourceLocation(
        FoxieMCMod.ID, "textures/entities/foxie/foxie_sleep.png");

    public FoxieRenderer(EntityRendererProvider.Context context) {
        super(
            context,
            new FoxieModel(context.bakeLayer(FoxieModel.LAYER_LOCATION)), 0.5F
        );

        this.addLayer(new FoxieHeldItemLayer(this));
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(Foxie foxie) {
        if (foxie.aiControl.isSleeping()) return TEXTURE_SLEEP;
        return TEXTURE;
    }

    @Override
    protected void setupRotations(
        @NotNull Foxie foxie,
        @NotNull PoseStack pose,
        float x,
        float y,
        float z
    ) {
        super.setupRotations(foxie, pose, x, y, z);
    }
}
