package com.everest.render;

import com.everest.entity.GraveEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
//? if >=1.20.0 {
import net.minecraft.client.render.model.json.ModelTransformationMode;
/*import net.minecraft.client.render.model.json.ModelTransformationMode;*/
//?} else {
import net.minecraft.client.render.model.json.ModelTransformation;
/*import net.minecraft.client.render.model.json.ModelTransformationMode;*/
//?}

//? if >=1.19.4 {
import net.minecraft.util.math.RotationAxis;
/*import net.minecraft.util.math.Vec3f;*/
//?} else {
/*import net.minecraft.util.math.RotationAxis;*/
//?}

import net.minecraft.util.Identifier;

import static java.lang.Math.*;

public class GraveEntityRenderer extends EntityRenderer<GraveEntity> {
    public final ItemRenderer itemRenderer;

    public GraveEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(GraveEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        float age = entity.age + tickDelta;
        matrices.translate(0, (abs(sin(age / 10) - 1) / 5), 0);
        matrices.scale(1.5f, 1.5f, 1.5f);
        //? if >=1.19.4 {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(age / 10));
        //?} else {
        /*matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float) (age / 10 * 180 / PI)));*/
        //?}

        ItemStack head = new ItemStack(Items.SKELETON_SKULL);

        //? if >=1.20.0 {
        itemRenderer.renderItem(head, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        //?} else {
        /*itemRenderer.renderItem(head, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);*/
        //?}
        matrices.pop();

        ItemStack bone = new ItemStack(Items.BONE);

        matrices.push();
        matrices.translate(-0.3, 0.05, -0.2);
        //? if >=1.19.4 {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        //?} else {
        /*matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(30));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));*/
        //?}
        matrices.scale(0.8f, 0.8f, 0.8f);
        //? if >=1.20.0 {
        itemRenderer.renderItem(bone, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        //?} else {
        /*itemRenderer.renderItem(bone, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);*/
        //?}
        matrices.pop();

        matrices.push();
        matrices.translate(0.25, 0.05, 0.3);
        //? if >=1.19.4 {
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-20));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        //?} else {
        /*matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-20));
        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-90));*/
        //?}
        matrices.scale(0.9f, 0.9f, 0.9f);
        //? if >=1.20.0 {
        itemRenderer.renderItem(bone, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        //?} else {
        /*itemRenderer.renderItem(bone, ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, 0);*/
        //?}
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(GraveEntity entity) {
        return null;
    }
}
