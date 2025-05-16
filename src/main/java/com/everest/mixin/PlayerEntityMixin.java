package com.everest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerEntity.vanishCursedItems ()V", shift = At.Shift.AFTER), cancellable = true)
    private void carnation$stopDropInventory(CallbackInfo ci) {
        ci.cancel();
    }

    @ModifyReturnValue(method = "shouldAlwaysDropXp", at = @At("RETURN"))
    private boolean carnation$dropsXP(boolean original) {
        return false;
    }
}
