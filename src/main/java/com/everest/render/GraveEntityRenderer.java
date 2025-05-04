package com.everest.render;

import com.everest.entity.GraveEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static java.lang.Math.abs;
import static java.lang.Math.sin;

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
        matrices.translate(0, (abs(sin(age / 10)-1)/5), 0);
        matrices.scale(1.5f, 1.5f, 1.5f);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(age / 10));

        ItemStack head = new ItemStack(Items.SKELETON_SKULL);

        itemRenderer.renderItem(head, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();

        ItemStack bone = new ItemStack(Items.BONE);

        matrices.push();
        matrices.translate(-0.3, 0.05, -0.2);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(30));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        matrices.scale(0.8f, 0.8f, 0.8f);
        itemRenderer.renderItem(bone, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();

        matrices.push();
        matrices.translate(0.25, 0.05, 0.3);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-20));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90));
        matrices.scale(0.9f, 0.9f, 0.9f);
        itemRenderer.renderItem(bone, ModelTransformationMode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(GraveEntity entity) {
        return null;
    }

}
