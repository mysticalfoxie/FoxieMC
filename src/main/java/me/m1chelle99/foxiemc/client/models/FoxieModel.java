package me.m1chelle99.foxiemc.client.models;

// This implementation is created by the great spirit of "itsmeow". 
// All rights on his side. https://github.com/itsmeow

// I don't take credit for his beautiful creation <3
// Contact me via email if I should take this down.

// (this will be soon replaced with my own version anyways)

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.m1chelle99.foxiemc.FoxieMCMod;
import me.m1chelle99.foxiemc.entities.foxie.Foxie;
import me.m1chelle99.foxiemc.entities.foxie.FoxieStates;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class FoxieModel extends EntityModel<Foxie> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(FoxieMCMod.ID, "foxie"), "main");

    public ModelPart body;
    public ModelPart rear;
    public ModelPart lHindLeg01;
    public ModelPart lHindLeg02;
    public ModelPart lHindLeg03;
    public ModelPart lHindpaw;
    public ModelPart rHindLeg01;
    public ModelPart rHindLeg02;
    public ModelPart rHindLeg03;
    public ModelPart rHindpaw;
    public ModelPart tail01;
    public ModelPart tail02a;
    public ModelPart tail02b;
    public ModelPart tail02c;
    public ModelPart tail02d;
    public ModelPart tail03;
    public ModelPart tail04;
    public ModelPart tail05;
    public ModelPart lArm01;
    public ModelPart lArm02;
    public ModelPart lForepaw;
    public ModelPart rArm01;
    public ModelPart rArm02;
    public ModelPart rForepaw;
    public ModelPart neck;
    public ModelPart head;
    public ModelPart snoot;
    public ModelPart lowerJaw;
    public ModelPart upperJawL;
    public ModelPart upperJawR;
    public ModelPart lEar01;
    public ModelPart lEar02;
    public ModelPart lEar03;
    public ModelPart lEar04;
    public ModelPart rEar01;
    public ModelPart rEar02;
    public ModelPart rEar03;
    public ModelPart rEar04;
    public ModelPart lCheekFur;
    public ModelPart rCheekFur;
    public ModelPart fur01;
    public ModelPart fur02;
    private float rotation;


    public FoxieModel(ModelPart root) {
        this.body = root.getChild("body");
        this.rear = body.getChild("rear");
        this.lHindLeg01 = rear.getChild("lHindLeg01");
        this.lHindLeg02 = lHindLeg01.getChild("lHindLeg02");
        this.lHindLeg03 = lHindLeg02.getChild("lHindLeg03");
        this.lHindpaw = lHindLeg03.getChild("lHindpaw");
        this.rHindLeg01 = rear.getChild("rHindLeg01");
        this.rHindLeg02 = rHindLeg01.getChild("rHindLeg02");
        this.rHindLeg03 = rHindLeg02.getChild("rHindLeg03");
        this.rHindpaw = rHindLeg03.getChild("rHindpaw");
        this.tail01 = rear.getChild("tail01");
        this.tail02a = tail01.getChild("tail02a");
        this.tail02b = tail02a.getChild("tail02b");
        this.tail02c = tail02a.getChild("tail02c");
        this.tail02d = tail02a.getChild("tail02d");
        this.tail03 = tail02a.getChild("tail03");
        this.tail04 = tail03.getChild("tail04");
        this.tail05 = tail04.getChild("tail05");
        this.lArm01 = body.getChild("lArm01");
        this.lArm02 = lArm01.getChild("lArm02");
        this.lForepaw = lArm02.getChild("lForepaw");
        this.rArm01 = body.getChild("rArm01");
        this.rArm02 = rArm01.getChild("rArm02");
        this.rForepaw = rArm02.getChild("rForepaw");
        this.neck = body.getChild("neck");
        this.head = neck.getChild("head");
        this.snoot = head.getChild("snoot");
        this.lowerJaw = head.getChild("lowerJaw");
        this.upperJawL = head.getChild("upperJawL");
        this.upperJawR = head.getChild("upperJawR");
        this.lEar01 = head.getChild("lEar01");
        this.lEar02 = lEar01.getChild("lEar02");
        this.lEar03 = lEar01.getChild("lEar03");
        this.lEar04 = lEar01.getChild("lEar04");
        this.rEar01 = head.getChild("rEar01");
        this.rEar02 = rEar01.getChild("rEar02");
        this.rEar03 = rEar01.getChild("rEar03");
        this.rEar04 = rEar01.getChild("rEar04");
        this.lCheekFur = head.getChild("lCheekFur");
        this.rCheekFur = head.getChild("rCheekFur");
        this.fur01 = neck.getChild("fur01");
        this.fur02 = neck.getChild("fur02");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 2).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 14.1F, -1.7F));
        PartDefinition rear = body.addOrReplaceChild("rear", CubeListBuilder.create().texOffs(0, 17).addBox(-2.5F, -2.5F, -0.3F, 5.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.4F, 2.8F, -0.0873F, 0.0F, 0.0F));
        PartDefinition lHindLeg01 = rear.addOrReplaceChild("lHindLeg01", CubeListBuilder.create().texOffs(31, 1).mirror().addBox(0.0F, -2.1F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.0F, 0.2F, 3.4F, -0.1745F, 0.0F, 0.0F));
        PartDefinition lHindLeg02 = lHindLeg01.addOrReplaceChild("lHindLeg02", CubeListBuilder.create().texOffs(32, 12).mirror().addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.4F, 2.6F, -0.6F, 0.8029F, 0.0F, 0.0F));
        PartDefinition lHindLeg03 = lHindLeg02.addOrReplaceChild("lHindLeg03", CubeListBuilder.create().texOffs(34, 19).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.1F, 2.4F, 0.2F, -0.5411F, 0.0F, 0.0F));
        lHindLeg03.addOrReplaceChild("lHindpaw", CubeListBuilder.create().texOffs(32, 28).mirror().addBox(-1.5F, -0.5F, -2.3F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 4.6F, 0.0F));
        PartDefinition rHindLeg01 = rear.addOrReplaceChild("rHindLeg01", CubeListBuilder.create().texOffs(31, 1).addBox(-3.0F, -2.1F, -2.0F, 3.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.2F, 3.4F, -0.1745F, 0.0F, 0.0F));
        PartDefinition rHindLeg02 = rHindLeg01.addOrReplaceChild("rHindLeg02", CubeListBuilder.create().texOffs(32, 12).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.4F, 2.6F, -0.6F, 0.8029F, 0.0F, 0.0F));
        PartDefinition rHindLeg03 = rHindLeg02.addOrReplaceChild("rHindLeg03", CubeListBuilder.create().texOffs(34, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, 2.4F, 0.2F, -0.5411F, 0.0F, 0.0F));
        rHindLeg03.addOrReplaceChild("rHindpaw", CubeListBuilder.create().texOffs(32, 28).addBox(-1.5F, -0.5F, -2.3F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.6F, 0.0F));
        PartDefinition tail01 = rear.addOrReplaceChild("tail01", CubeListBuilder.create().texOffs(0, 39).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.1F, 4.8F, -0.6807F, 0.0F, 0.0F));
        PartDefinition tail02a = tail01.addOrReplaceChild("tail02a", CubeListBuilder.create().texOffs(0, 45).addBox(-1.7F, -1.7F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.1F, 1.6F, -0.1745F, 0.0F, 0.0F));
        tail02a.addOrReplaceChild("tail02b", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-0.3F, -1.7F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        tail02a.addOrReplaceChild("tail02c", CubeListBuilder.create().texOffs(0, 45).addBox(-1.69F, -0.3F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        tail02a.addOrReplaceChild("tail02d", CubeListBuilder.create().texOffs(0, 45).mirror().addBox(-0.31F, -0.3F, 0.0F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition tail03 = tail02a.addOrReplaceChild("tail03", CubeListBuilder.create().texOffs(0, 51).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.6F, 0.1222F, 0.0F, 0.0F));
        PartDefinition tail04 = tail03.addOrReplaceChild("tail04", CubeListBuilder.create().texOffs(21, 53).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 5.6F, 0.1222F, 0.0F, 0.0F));
        tail04.addOrReplaceChild("tail05", CubeListBuilder.create().texOffs(36, 53).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0349F, 0.0F, 0.0F));
        PartDefinition lArm01 = body.addOrReplaceChild("lArm01", CubeListBuilder.create().texOffs(48, 0).mirror().addBox(-1.0F, -2.4F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.9F, 0.1F, -2.5F, 0.2269F, 0.0F, -0.0873F));
        PartDefinition lArm02 = lArm01.addOrReplaceChild("lArm02", CubeListBuilder.create().texOffs(51, 10).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.8F, 3.1F, 0.1F, -0.2269F, 0.0F, 0.0873F));
        lArm02.addOrReplaceChild("lForepaw", CubeListBuilder.create().texOffs(32, 28).mirror().addBox(-1.5F, -0.9F, -2.3F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 6.5F, 0.0F));
        PartDefinition rArm01 = body.addOrReplaceChild("rArm01", CubeListBuilder.create().texOffs(48, 0).addBox(-2.0F, -2.4F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9F, 0.1F, -2.5F, 0.2269F, 0.0F, 0.0873F));
        PartDefinition rArm02 = rArm01.addOrReplaceChild("rArm02", CubeListBuilder.create().texOffs(51, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.8F, 3.1F, 0.1F, -0.2269F, 0.0F, -0.0873F));
        rArm02.addOrReplaceChild("rForepaw", CubeListBuilder.create().texOffs(32, 28).addBox(-1.5F, -0.9F, -2.3F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.5F, 0.0F));
        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 30).addBox(-2.0F, -2.0F, -3.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9F, -4.0F, -0.3491F, 0.0F, 0.0F));
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(17, 34).addBox(-2.5F, -2.0F, -4.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, -1.9F, 0.3491F, 0.0F, 0.0F));
        head.addOrReplaceChild("snoot", CubeListBuilder.create().texOffs(23, 43).addBox(-1.0F, -1.0F, -2.4F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, -4.0F, 0.2276F, 0.0F, 0.0F));
        head.addOrReplaceChild("lowerJaw", CubeListBuilder.create().texOffs(23, 48).addBox(-1.0F, -0.6F, -2.6F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, -3.7F));
        head.addOrReplaceChild("upperJawL", CubeListBuilder.create().texOffs(34, 43).addBox(-1.0F, -0.5F, -2.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4F, 0.9F, -3.9F, 0.0F, 0.1396F, -0.0873F));
        head.addOrReplaceChild("upperJawR", CubeListBuilder.create().texOffs(34, 43).mirror().addBox(-1.0F, -0.5F, -2.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-0.4F, 0.9F, -3.9F, 0.0F, -0.1396F, 0.0873F));
        PartDefinition lEar01 = head.addOrReplaceChild("lEar01", CubeListBuilder.create().texOffs(21, 0).mirror().addBox(-1.0F, -1.9F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(1.6F, -1.4F, -1.5F, 0.0F, -0.4014F, 0.2618F));
        lEar01.addOrReplaceChild("lEar02", CubeListBuilder.create().texOffs(21, 5).mirror().addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.015F)).mirror(false), PartPose.offsetAndRotation(0.5F, -1.7F, 0.0F, 0.0F, 0.0F, -0.4363F));
        lEar01.addOrReplaceChild("lEar03", CubeListBuilder.create().texOffs(29, 0).mirror().addBox(-0.5F, -3.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.5F, 0.8F, 0.2269F, 0.0F, 0.0F));
        lEar01.addOrReplaceChild("lEar04", CubeListBuilder.create().texOffs(21, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)), PartPose.offsetAndRotation(-0.5F, -1.7F, 0.0F, 0.0F, 0.0F, 0.4363F));
        PartDefinition rEar01 = head.addOrReplaceChild("rEar01", CubeListBuilder.create().texOffs(21, 0).addBox(-1.0F, -1.9F, -0.5F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.6F, -1.4F, -1.5F, 0.0F, 0.4014F, -0.2618F));
        rEar01.addOrReplaceChild("rEar02", CubeListBuilder.create().texOffs(21, 5).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.015F)), PartPose.offsetAndRotation(-0.5F, -1.7F, 0.0F, 0.0F, 0.0F, 0.4363F));
        rEar01.addOrReplaceChild("rEar03", CubeListBuilder.create().texOffs(29, 0).addBox(-0.5F, -3.7F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.8F, 0.2269F, 0.0F, 0.0F));
        rEar01.addOrReplaceChild("rEar04", CubeListBuilder.create().texOffs(21, 5).mirror().addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(-0.01F)).mirror(false), PartPose.offsetAndRotation(0.5F, -1.7F, 0.0F, 0.0F, 0.0F, -0.4363F));
        head.addOrReplaceChild("lCheekFur", CubeListBuilder.create().texOffs(0, 0).addBox(-0.1F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.9F, -0.5F, -1.4F, 0.0F, -0.4014F, -0.2618F));
        head.addOrReplaceChild("rCheekFur", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-2.9F, -1.0F, 0.0F, 3.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.9F, -0.5F, -1.4F, 0.0F, 0.4014F, 0.2618F));
        neck.addOrReplaceChild("fur01", CubeListBuilder.create().texOffs(47, 20).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.7F, -1.5F, -0.8652F, 0.0F, 0.0F));
        neck.addOrReplaceChild("fur02", CubeListBuilder.create().texOffs(46, 27).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -0.5F, -0.7741F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public static float rad(float deg) {
        return (float) Math.toRadians(deg);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack matrixStackIn, @NotNull VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.body.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(Foxie foxie, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setRotateAngle(lHindLeg02, 0.8028514559173915F, 0.0F, 0.0F);
        this.setRotateAngle(rCheekFur, 0.0F, 0.40142572795869574F, -0.2617993877991494F);
        this.setRotateAngle(fur02, -0.7740535232594852F, 0.0F, 0.0F);
        this.setRotateAngle(rear, -0.08726646259971647F, 0.0F, 0.0F);
        this.setRotateAngle(neck, -0.3490658503988659F, 0.0F, 0.0F);
        this.setRotateAngle(tail02a, -0.17453292519943295F, 0.0F, 0.0F);
        this.setRotateAngle(rEar01, 0.0F, 0.40142572795869574F, -0.2617993877991494F);
        this.setRotateAngle(fur01, -0.8651597102135892F, 0.0F, 0.0F);
        this.setRotateAngle(snoot, 0.22759093446006054F, 0.0F, 0.0F);
        this.setRotateAngle(lHindLeg01, -0.17453292519943295F, 0.0F, 0.0F);
        this.setRotateAngle(lEar01, 0.0F, -0.40142572795869574F, 0.2617993877991494F);
        this.setRotateAngle(tail05, 0.03490658503988659F, 0.0F, 0.0F);
        this.setRotateAngle(lArm01, 0.22689280275926282F, 0.0F, -0.08726646259971647F);
        this.setRotateAngle(lHindLeg03, -0.5410520681182421F, 0.0F, 0.0F);
        this.setRotateAngle(upperJawL, 0.0F, 0.13962634015954636F, 0.08726646259971647F);
        this.setRotateAngle(rArm02, -0.22689280275926282F, 0.0F, -0.08726646259971647F);
        this.setRotateAngle(lEar02, 0.0F, 0.0F, -0.40980330836826856F);
        this.setRotateAngle(lEar04, 0.0F, 0.0F, 0.40980330836826856F);
        this.setRotateAngle(rHindLeg02, 0.8028514559173915F, 0.0F, 0.0F);
        this.setRotateAngle(tail04, 0.12217304763960307F, 0.0F, 0.0F);
        this.setRotateAngle(rArm01, 0.22689280275926282F, 0.0F, 0.08726646259971647F);
        this.setRotateAngle(lCheekFur, 0.0F, -0.40142572795869574F, 0.2617993877991494F);
        this.setRotateAngle(lEar03, 0.22689280275926282F, 0.0F, 0.0F);
        this.setRotateAngle(rHindLeg01, -0.17453292519943295F, 0.0F, 0.0F);
        this.setRotateAngle(rEar03, 0.22689280275926282F, 0.0F, 0.0F);
        this.setRotateAngle(head, 0.3490658503988659F, 0.0F, 0.0F);
        this.setRotateAngle(rEar02, 0.0F, 0.0F, 0.40980330836826856F);
        this.setRotateAngle(lArm02, -0.22689280275926282F, 0.0F, 0.08726646259971647F);
        this.setRotateAngle(tail03, 0.12217304763960307F, 0.0F, 0.0F);
        this.setRotateAngle(upperJawR, 0.0F, -0.13962634015954636F, -0.08726646259971647F);
        this.setRotateAngle(tail01, -0.6806784082777886F, 0.0F, 0.0F);
        this.setRotateAngle(rHindLeg03, -0.5410520681182421F, 0.0F, 0.0F);
        this.setRotateAngle(rEar04, 0.0F, 0.0F, -0.40980330836826856F);
        this.lHindLeg01.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount - 0.17453292519943295F;
        this.lArm01.xRot = Mth.cos(limbSwing * 0.6662F + 0.3F) * limbSwingAmount + 0.22689280275926282F;
        this.rHindLeg01.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI + 0.3F) * limbSwingAmount - 0.17453292519943295F;
        this.rArm01.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount + 0.22689280275926282F;
        this.body.xRot = 0;

        if (!foxie.getFlag(FoxieStates.SLEEPING) && !foxie.getFlag(FoxieStates.FACEPLANTED) && !foxie.getFlag(FoxieStates.CROUCHING)) {
            this.head.xRot = rad(headPitch);
            this.head.yRot = rad(netHeadYaw);
        }

        if (foxie.getFlag(FoxieStates.SLEEPING)) {
            this.setRotateAngle(lHindLeg02, 0.8028514559173915F, 0.0F, 0.0F);
            this.setRotateAngle(lHindLeg03, -0.5410520681182421F, 0.0F, 0.0F);
            this.setRotateAngle(lEar04, 0.0F, 0.0F, 0.40980330836826856F);
            this.setRotateAngle(rHindLeg02, 0.8028514559173915F, 0.0F, 0.0F);
            this.setRotateAngle(upperJawR, 0.0F, -0.13962634015954636F, -0.08726646259971647F);
            this.setRotateAngle(lCheekFur, 0.0F, -0.40142572795869574F, 0.2617993877991494F);
            this.setRotateAngle(rHindpaw, 1.3962634015954636F, 0.0F, 0.0F);
            this.setRotateAngle(rArm02, -2.705260340591211F, 0.0F, -0.08726646259971647F);
            this.setRotateAngle(rHindLeg01, -1.5707963267948966F, 0.0F, 0.0F);
            this.setRotateAngle(lForepaw, 1.5707963267948966F, 0.0F, 0.0F);
            this.setRotateAngle(tail01, -0.12217304763960307F, 0.6981317007977318F, 0.0F);
            this.setRotateAngle(lHindLeg01, -1.5707963267948966F, -0.3490658503988659F, 0.0F);
            this.setRotateAngle(lEar02, 0.0F, 0.0F, -0.40980330836826856F);
            this.setRotateAngle(rEar02, 0.0F, 0.0F, 0.40980330836826856F);
            this.setRotateAngle(lEar01, 0.0F, -0.40142572795869574F, 0.2617993877991494F);
            this.setRotateAngle(rCheekFur, 0.0F, 0.40142572795869574F, -0.2617993877991494F);
            this.setRotateAngle(rHindLeg03, -0.5410520681182421F, 0.0F, 0.0F);
            this.setRotateAngle(tail04, 0.0F, 0.6283185307179586F, 0.0F);
            this.setRotateAngle(tail02a, -0.17453292519943295F, 0.6981317007977318F, 0.0F);
            this.setRotateAngle(upperJawL, 0.0F, 0.13962634015954636F, 0.08726646259971647F);
            this.setRotateAngle(lHindpaw, 1.3962634015954636F, 0.0F, 0.0F);
            this.setRotateAngle(rArm01, 1.1344640137963142F, 0.0F, 0.08726646259971647F);
            this.setRotateAngle(rEar04, 0.0F, 0.0F, -0.40980330836826856F);
            this.setRotateAngle(lEar03, 0.22689280275926282F, 0.0F, 0.0F);
            this.setRotateAngle(neck, 0.0F, -0.7853981633974483F, 0.0F);
            this.setRotateAngle(fur02, -0.7740535232594852F, 0.0F, 0.0F);
            this.setRotateAngle(rEar01, 0.0F, 0.40142572795869574F, -0.2617993877991494F);
            this.setRotateAngle(tail03, 0.0F, 0.6981317007977318F, 0.0F);
            this.setRotateAngle(rEar03, 0.22689280275926282F, 0.0F, 0.0F);
            this.setRotateAngle(fur01, -0.8651597102135892F, 0.0F, 0.0F);
            this.setRotateAngle(tail05, 0.03490658503988659F, 0.0F, 0.0F);
            this.setRotateAngle(rForepaw, 1.5707963267948966F, 0.0F, 0.0F);
            this.setRotateAngle(lArm01, 1.1344640137963142F, 0.0F, -0.08726646259971647F);
            this.setRotateAngle(lArm02, -2.705260340591211F, 0.0F, 0.08726646259971647F);
            this.setRotateAngle(head, 0.0F, -0.6981317007977318F, 0.0F);
            this.setRotateAngle(snoot, 0.22759093446006054F, 0.0F, 0.0F);
            this.setRotateAngle(rear, -0.08726646259971647F, 0.6283185307179586F, 0.0F);
            this.head.xRot = 0.0F;
            this.head.zRot = Mth.cos(ageInTicks * 0.027F) / 22.0F;
        }

        if (foxie.getFlag(FoxieStates.SITTING)) {
            this.setRotateAngleDeg(this.neck, 50, 0, 0);
            this.setRotateAngleDeg(this.body, -50, 0, 0);
            this.setRotateAngleDeg(this.rear, -40, 0, 0);
            this.setRotateAngleDeg(this.tail01, 90, 0, 0);
            this.setRotateAngleDeg(this.lArm01, 36, 0, -5);
            this.setRotateAngleDeg(this.lArm02, -26, 0, 5);
            this.setRotateAngleDeg(this.lForepaw, 28, 0, 0);
            this.setRotateAngleDeg(this.rArm01, 36, 0, 5);
            this.setRotateAngleDeg(this.rArm02, -26, 0, -5);
            this.setRotateAngleDeg(this.rForepaw, 28, 0, 0);
            this.setRotateAngleDeg(this.lHindLeg01, -13, 0, -16);
            this.setRotateAngle(this.lHindLeg02, 0.8996066167365371F, 0.0F, 0.0F);
            this.setRotateAngleDeg(this.lHindpaw, 90, 0, 0);
            this.setRotateAngleDeg(this.rHindLeg01, -13, 0, 16);
            this.setRotateAngle(this.rHindLeg02, 0.8996066167365371F, 0.0F, 0.0F);
            this.setRotateAngleDeg(this.rHindpaw, 90, 0, 0);
            this.head.xRot -= Math.toRadians(20);
        }

        if (foxie.getCrouchAmount() > 0 && (foxie.getFlag(FoxieStates.CROUCHING) || foxie.getFlag(FoxieStates.COMMAND_DOWN))) {
            this.lArm01.y = 0.1F - 1.25F;
            this.rArm01.y = 0.1F - 1.25F;
            this.lHindLeg01.y = 0.2F - 1.25F;
            this.rHindLeg01.y = 0.2F - 1.25F;
        } else {
            this.lArm01.y = 0.1F;
            this.rArm01.y = 0.1F;
            this.lHindLeg01.y = 0.2F;
            this.rHindLeg01.y = 0.2F;
        }

        if (foxie.getFlag(FoxieStates.SLEEPING)) {
            this.body.y = 19.0F;
        } else if (foxie.getFlag(FoxieStates.SITTING)) {
            this.body.y = 16.5F;
        } else {
            this.body.y = 14.1F;
        }

        if (foxie.getFlag(FoxieStates.FACEPLANTED)) {
            this.rotation += 0.67F;
            this.body.xRot = Mth.cos(this.rotation * 0.4662F) * 0.1F;
        }
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public void setRotateAngleDeg(ModelPart modelRenderer, float x, float y, float z) {
        setRotateAngle(modelRenderer, rad(x), rad(y), rad(z));
    }
}
