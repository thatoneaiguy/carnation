package com.everest.mixin;

import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "dropInventory", at = @At("HEAD"), cancellable = true)
    private void dropInventory(CallbackInfo ci) {
        ci.cancel();
    }
}
