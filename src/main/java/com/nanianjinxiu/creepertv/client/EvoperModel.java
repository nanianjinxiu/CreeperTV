package com.nanianjinxiu.creepertv.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nanianjinxiu.creepertv.CreeperTV;
import com.nanianjinxiu.creepertv.entity.Evoper;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.monster.AbstractIllager;

public class EvoperModel<T extends Entity> extends EntityModel<T> implements ArmedModel, HeadedModel {

	public static final ModelLayerLocation LAYER_LOCATION =
			new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(CreeperTV.MODID, "evoper"), "main");

	private final ModelPart body;
	private final ModelPart Head;
	private final ModelPart nose;
	private final ModelPart arms;
	private final ModelPart leg0;
	private final ModelPart leg1;
	private final ModelPart RightArm;
	private final ModelPart LeftArm;

	public EvoperModel(ModelPart root) {
		this.body = root.getChild("body");
		this.Head = this.body.getChild("Head");
		this.nose = this.Head.getChild("nose");
		this.arms = this.body.getChild("arms");
		this.leg0 = this.body.getChild("leg0");
		this.leg1 = this.body.getChild("leg1");
		this.RightArm = this.body.getChild("RightArm");
		this.LeftArm = this.body.getChild("LeftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition nose = Head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

		PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(44, 22).addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, -1.0F, -0.75F, 0.0F, 0.0F));

		PartDefinition leg0 = body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

		PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

		PartDefinition RightArm = body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 46).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition LeftArm = body.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(40, 46).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// 头部跟随视角
		this.Head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.Head.xRot = headPitch * ((float) Math.PI / 180F);

		// 基础走路动画（左右手臂摆动）
		this.RightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
		this.RightArm.yRot = 0.0F;
		this.RightArm.zRot = 0.0F;
		this.LeftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
		this.LeftArm.yRot = 0.0F;
		this.LeftArm.zRot = 0.0F;

		// 腿部走路摆动
		this.leg0.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.leg0.yRot = 0.0F;
		this.leg0.zRot = 0.0F;
		this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.leg1.yRot = 0.0F;
		this.leg1.zRot = 0.0F;

		// 手臂姿态控制
		if (entity instanceof Evoper evoper) {
			AbstractIllager.IllagerArmPose armPose = evoper.getArmPose();

			if (armPose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
				this.RightArm.z = 0.0F;
				this.RightArm.x = -5.0F;
				this.LeftArm.z = 0.0F;
				this.LeftArm.x = 5.0F;
				this.RightArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.LeftArm.xRot = Mth.cos(ageInTicks * 0.6662F) * 0.25F;
				this.RightArm.zRot = 2.3561945F;
				this.LeftArm.zRot = -2.3561945F;
				this.RightArm.yRot = 0.0F;
				this.LeftArm.yRot = 0.0F;
			}

			boolean crossed = armPose == AbstractIllager.IllagerArmPose.CROSSED;
			this.arms.visible = crossed;
			this.RightArm.visible = !crossed;
			this.LeftArm.visible = !crossed;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void translateToHand(HumanoidArm side, PoseStack poseStack) {
		ModelPart arm = side == HumanoidArm.LEFT ? this.LeftArm : this.RightArm;
		arm.translateAndRotate(poseStack);
	}

	@Override
	public ModelPart getHead() {
		return this.Head;
	}
}