package me.m1chelle99.foxiemc.entities.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.m1chelle99.foxiemc.entities.Foxie;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FoxieHeldItemLayer extends RenderLayer<Foxie, FoxModel<Foxie>> {
    public FoxieHeldItemLayer(RenderLayerParent<Foxie, FoxModel<Foxie>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack pose, @NotNull MultiBufferSource source, int some_integer, Foxie foxie, float pos_x, float pos_y, float pos_z, float rot_x, float rot_y, float rot_z) {
        pose.pushPose();

        if (foxie.isBaby()) {
            pose.scale(0.75F, 0.75F, 0.75F);
            pose.translate(0.0D, 0.5D, 0.209375F);
        }

        var xTranslate = this.getParentModel().head.x / 16.0F;
        var yTranslate = this.getParentModel().head.y / 16.0F;
        var zTranslate = this.getParentModel().head.z / 16.0F;
        pose.translate(xTranslate, yTranslate, zTranslate);

        var xRot = foxie.getHeadRollAngle(pos_z);
        pose.mulPose(Vector3f.ZP.rotation(xRot));
        pose.mulPose(Vector3f.YP.rotationDegrees(rot_y));
        pose.mulPose(Vector3f.XP.rotationDegrees(rot_z));

        if (foxie.isBaby()) {
            var xBabyTranslate = foxie.isSleeping() ? 0.4F : 0.06F;
            var yBabyTranslate = 0.26F;
            var zBabyTranslate = foxie.isSleeping() ? 0.15F : -0.5D;
            pose.translate(xBabyTranslate, yBabyTranslate, zBabyTranslate);
        } else if (foxie.isSleeping())
            pose.translate(0.46F, 0.26F, 0.22F);
        else
            pose.translate(0.06F, 0.27F, -0.5D);

        pose.mulPose(Vector3f.XP.rotationDegrees(90.0F));
        if (foxie.isSleeping())
            pose.mulPose(Vector3f.ZP.rotationDegrees(90.0F));

        ItemStack itemstack = foxie.getItemBySlot(EquipmentSlot.MAINHAND);

        Minecraft
                .getInstance()
                .getItemInHandRenderer()
                .renderItem(foxie, itemstack, ItemTransforms.TransformType.GROUND, false, pose, source, some_integer);

        pose.popPose();
    }
}
