package com.nanianjinxiu.creepertv.client.model;

import com.nanianjinxiu.creepertv.CreeperTV;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Vex;

public class VeperModel<T extends Entity> extends HierarchicalModel<T> {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation(CreeperTV.MODID, "veper"), "main");

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightWing;
	private final ModelPart leftWing;

	public VeperModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.head = this.root.getChild("head");
		this.rightArm = this.body.getChild("right_arm");
		this.leftArm = this.body.getChild("left_arm");
		this.rightWing = this.body.getChild("right_wing");
		this.leftWing = this.body.getChild("left_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition rootPart = mesh.getRoot().addOrReplaceChild("root",
				CubeListBuilder.create(), PartPose.offset(0.0F, -2.5F, 0.0F));

		rootPart.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0).addBox(-2.5F, -5.0F, -2.5F, 5.0F, 5.0F, 5.0F),
				PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition body = rootPart.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 10).addBox(-1.5F, 0.0F, -1.0F, 3.0F, 4.0F, 2.0F)
						.texOffs(0, 16).addBox(-1.5F, 1.0F, -1.0F, 3.0F, 5.0F, 2.0F, new CubeDeformation(-0.2F)),
				PartPose.offset(0.0F, 20.0F, 0.0F));

		body.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(23, 0).addBox(-1.25F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(-1.75F, 0.25F, 0.0F));

		body.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.texOffs(23, 6).addBox(-0.75F, -0.5F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(-0.1F)),
				PartPose.offset(1.75F, 0.25F, 0.0F));

		body.addOrReplaceChild("left_wing", CubeListBuilder.create()
						.texOffs(16, 22).mirror()
						.addBox(0.0F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false),
				PartPose.offset(0.5F, 1.0F, 1.0F));

		body.addOrReplaceChild("right_wing", CubeListBuilder.create()
						.texOffs(16, 22).addBox(-8.0F, 0.0F, 0.0F, 8.0F, 5.0F, 0.0F, new CubeDeformation(0.0F)),
				PartPose.offset(-0.5F, 1.0F, 1.0F));

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount,
						  float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
		this.head.xRot = headPitch * ((float) Math.PI / 180F);

		float f = Mth.cos(ageInTicks * 5.5F * ((float) Math.PI / 180F)) * 0.1F;
		this.rightArm.zRot = ((float) Math.PI / 5F) + f;
		this.leftArm.zRot = -(((float) Math.PI / 5F) + f);

		if (entity instanceof Vex vex && vex.isCharging()) {
			this.body.xRot = 0.0F;
			this.rightArm.xRot = -1.2217305F;
			this.rightArm.yRot = 0.2617994F;
			this.rightArm.zRot = -0.47123888F - f;
			this.leftArm.xRot = -1.2217305F;
			this.leftArm.yRot = -0.2617994F;
			this.leftArm.zRot = 0.47123888F + f;
		} else {
			this.body.xRot = 0.15707964F;
		}

		// 翅膀：yRot 基准取反，让 X 轴翅膀朝后展开
		this.leftWing.yRot = -1.0995574F + Mth.cos(ageInTicks * 45.836624F * ((float) Math.PI / 180F))
				* ((float) Math.PI / 180F) * 16.2F;
		this.rightWing.yRot = -this.leftWing.yRot;
		this.leftWing.xRot = 0.47123888F;
		this.leftWing.zRot = -0.47123888F;
		this.rightWing.xRot = 0.47123888F;
		this.rightWing.zRot = 0.47123888F;
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}