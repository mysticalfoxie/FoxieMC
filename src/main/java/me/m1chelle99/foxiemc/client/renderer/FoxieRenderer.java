package me.m1chelle99.foxiemc.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entities.Foxie;
import me.m1chelle99.foxiemc.entities.layers.FoxieHeldItemLayer;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FoxieRenderer extends MobRenderer<Foxie, FoxModel<Foxie>> {
    public static final ResourceLocation LAYER_RESOURCE = new ResourceLocation(FoxieMCMod.ID, "foxie");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(LAYER_RESOURCE, "main");
    private static final ResourceLocation TEXTURE = new ResourceLocation(FoxieMCMod.ID, "textures/entities/foxie/foxie.png");
    private static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation(FoxieMCMod.ID, "textures/entities/foxie/foxie_sleep.png");

    public FoxieRenderer(EntityRendererProvider.Context context) {
        super(context, new FoxModel<>(context.bakeLayer(ModelLayers.FOX)), 0.5F);
        this.addLayer(new FoxieHeldItemLayer(this));
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(Foxie foxie) {
        if (foxie.isSleeping()) return TEXTURE_SLEEP;

        return TEXTURE;
    }

    @Override
    protected void setupRotations(@NotNull Foxie foxie, @NotNull PoseStack pose, float x, float y, float z) {
        super.setupRotations(foxie, pose, x, y, z);
        if (foxie.isPouncing() || foxie.isFaceplanted()) {
            var rotation = -Mth.lerp(z, foxie.xRotO, foxie.getXRot());
            pose.mulPose(Vector3f.XP.rotationDegrees(rotation));
        }
    }
}
