package com.nanianjinxiu.creepertv.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nanianjinxiu.creepertv.CreeperTV;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PhanperModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(new ResourceLocation(CreeperTV.MODID, "phanper"), "main");

	private final ModelPart body;
	private final ModelPart wing0;
	private final ModelPart wingtip0;
	private final ModelPart wing1;
	private final ModelPart wingtip1;
	private final ModelPart head;
	private final ModelPart tail;
	private final ModelPart tailtip;
	private final ModelPart tailtip2;
	private final ModelPart tailtip3;
	private final ModelPart tailtip4;

	public PhanperModel(ModelPart root) {
		this.body = root.getChild("body");
		this.wing0 = this.body.getChild("wing0");
		this.wingtip0 = this.wing0.getChild("wingtip0");
		this.wing1 = this.body.getChild("wing1");
		this.wingtip1 = this.wing1.getChild("wingtip1");
		this.head = this.body.getChild("head");
		this.tail = this.body.getChild("tail");
		this.tailtip = this.tail.getChild("tailtip");
		this.tailtip2 = this.tailtip.getChild("tailtip2");
		this.tailtip3 = this.tailtip2.getChild("tailtip3");
		this.tailtip4 = this.tailtip3.getChild("tailtip4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition Body_r1 = body.addOrReplaceChild("Body_r1", CubeListBuilder.create().texOffs(0, 48).addBox(-4.0F, -18.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -14.0F, -1.6581F, 0.0F, 0.0F));

		PartDefinition wing0 = body.addOrReplaceChild("wing0", CubeListBuilder.create().texOffs(23, 12).addBox(2.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, -8.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition wingtip0 = wing0.addOrReplaceChild("wingtip0", CubeListBuilder.create().texOffs(16, 24).addBox(2.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

		PartDefinition wing1 = body.addOrReplaceChild("wing1", CubeListBuilder.create().texOffs(23, 12).mirror().addBox(-7.0F, 0.0F, 0.0F, 6.0F, 2.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -2.0F, -8.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition wingtip1 = wing1.addOrReplaceChild("wingtip1", CubeListBuilder.create().texOffs(16, 24).mirror().addBox(-14.0F, 0.0F, 0.0F, 13.0F, 1.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-6.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -7.0F, -9.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -7.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, -2.0F, 1.0F));

		PartDefinition tailtip = tail.addOrReplaceChild("tailtip", CubeListBuilder.create().texOffs(0, 19).addBox(-3.0F, -0.5F, 3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition tailtip2 = tailtip.addOrReplaceChild("tailtip2", CubeListBuilder.create().texOffs(42, 1).addBox(-2.0F, -0.8F, 3.0F, 4.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, 6.0F));

		PartDefinition tailtip3 = tailtip2.addOrReplaceChild("tailtip3", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -0.7F, 10.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition tailtip4 = tailtip3.addOrReplaceChild("tailtip4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.0873F, 0.0F, 0.0F));

		PartDefinition tailtip4_r1 = tailtip4.addOrReplaceChild("tailtip4_r1", CubeListBuilder.create().texOffs(21, 37).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.6F, 11.7F, -0.1745F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = Math.max(-0.5F, Math.min(0.5F, netHeadYaw * ((float)Math.PI / 180F)));
		this.head.xRot = Math.max(-0.5F, Math.min(0.5F, -headPitch * ((float)Math.PI / 180F)));
		this.body.xRot = -headPitch * ((float)Math.PI / 180F);

		float flap = Mth.cos(ageInTicks * 0.3F) * 0.2F;
		this.wing0.zRot = 0.0873F + flap;
		this.wingtip0.zRot = 0.1745F + flap * 0.5F;
		this.wing1.zRot = -0.0873F - flap;
		this.wingtip1.zRot = -0.1745F - flap * 0.5F;

		float wave = Mth.sin(ageInTicks * 0.3F);
		float tailSwing = wave < 0 ? wave * 0.04F : wave * 0.08F;

		this.tail.xRot = tailSwing;
		this.tailtip.xRot = -0.0873F + tailSwing * 1.2F;
		this.tailtip2.xRot = tailSwing * 1.4F;
		this.tailtip3.xRot = tailSwing * 1.6F;
		this.tailtip4.xRot = -0.0873F + tailSwing * 1.8F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}