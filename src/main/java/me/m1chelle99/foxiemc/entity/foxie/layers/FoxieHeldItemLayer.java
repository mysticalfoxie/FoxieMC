package me.m1chelle99.foxiemc.entity.foxie.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.m1chelle99.foxiemc.client.models.FoxieModel;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public final class FoxieHeldItemLayer extends RenderLayer<Foxie, FoxieModel> {
    public FoxieHeldItemLayer(RenderLayerParent<Foxie, FoxieModel> parent) {
        super(parent);
    }

    @Override
    public void render(
            @NotNull PoseStack pose,
            @NotNull MultiBufferSource bufferIn,
            int packedLightIn, Foxie foxie,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (!foxie.mouthControl.hasItem()) return;

        foxie.isSleeping();
        pose.pushPose();

        var x_translate = this.getParentModel().head.x;
        x_translate += this.getParentModel().neck.x;
        x_translate += this.getParentModel().body.x;
        x_translate /= 16.0F;

        var y_translate = this.getParentModel().head.y;
        y_translate += this.getParentModel().neck.y;
        y_translate += this.getParentModel().body.y;
        y_translate /= 16.0F;

        var z_translate = this.getParentModel().head.z;
        z_translate += this.getParentModel().neck.z;
        z_translate += this.getParentModel().body.z;
        z_translate /= 16.0F;


        pose.translate(x_translate, y_translate, z_translate);

        pose.mulPose(Axis.ZP.rotation(this.getParentModel().head.zRot));
        pose.mulPose(Axis.YP.rotation(this.getParentModel().head.yRot));
        pose.mulPose(Axis.XP.rotation(this.getParentModel().head.xRot));

        if (foxie.aiControl.isSleeping())
            pose.translate(0.6F, 0.1F, -0.3F);
        else pose.translate(0F, -0.1F, -0.5F);

        pose.mulPose(Axis.XP.rotationDegrees(90F));
        if (foxie.aiControl.isSleeping())
            pose.mulPose(Axis.ZP.rotationDegrees(90F));

        var minecraft = Minecraft.getInstance();
        var dispatcher = minecraft.getEntityRenderDispatcher();
        var renderer = dispatcher.getItemInHandRenderer();
        var item = foxie.mouthControl.getItem();
        renderer.renderItem(
            foxie,
            item,
            ItemDisplayContext.GROUND,
            false,
            pose,
            bufferIn,
            packedLightIn
        );

        pose.popPose();
    }
}
