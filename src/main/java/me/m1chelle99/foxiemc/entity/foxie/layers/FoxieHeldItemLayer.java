package me.m1chelle99.foxiemc.entity.foxie.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.m1chelle99.foxiemc.client.models.FoxieModel;
import me.m1chelle99.foxiemc.entity.foxie.Foxie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FoxieHeldItemLayer extends RenderLayer<Foxie, FoxieModel> {
    public FoxieHeldItemLayer(RenderLayerParent<Foxie, FoxieModel> parent) {
        super(parent);
    }

    @Override
    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource bufferIn, int packedLightIn, Foxie foxie, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!foxie.mouthControl.hasItem()) return;

        foxie.isSleeping();
        pose.pushPose();

        pose.translate(((this.getParentModel()).head.x + (this.getParentModel()).neck.x + (this.getParentModel()).body.x) / 16.0F, ((this.getParentModel()).head.y + (this.getParentModel()).neck.y + (this.getParentModel()).body.y) / 16.0F, ((this.getParentModel()).head.z + (this.getParentModel()).neck.z + (this.getParentModel()).body.z) / 16.0F);
        pose.mulPose(Vector3f.ZP.rotation(this.getParentModel().head.zRot));
        pose.mulPose(Vector3f.YP.rotation(this.getParentModel().head.yRot));
        pose.mulPose(Vector3f.XP.rotation(this.getParentModel().head.xRot));

        if (foxie.aiControl.isSleeping())
            pose.translate(0.6F, 0.1F, -0.3F);
        else pose.translate(0F, -0.1F, -0.5F);

        pose.mulPose(Vector3f.XP.rotationDegrees(90F));
        if (foxie.aiControl.isSleeping())
            pose.mulPose(Vector3f.ZP.rotationDegrees(90F));

        var renderer = Minecraft.getInstance().getItemInHandRenderer();
        var item = foxie.mouthControl.getItem();
        renderer.renderItem(foxie, item, ItemTransforms.TransformType.GROUND, false, pose, bufferIn, packedLightIn);

        pose.popPose();
    }
}
