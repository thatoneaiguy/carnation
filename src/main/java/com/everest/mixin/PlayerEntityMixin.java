package com.everest.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if <1.21.0 {
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(
            method = "dropInventory",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;vanishCursedItems()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void carnation$stopDropInventory(CallbackInfo ci) {
        ci.cancel();
    }

    @ModifyReturnValue(method = "shouldAlwaysDropXp", at = @At("RETURN"))
    private boolean carnation$dropsXP(boolean original) {
        return false;
    }
}
//?} else {
/*@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    // Note: vanishCursedItems() no longer exists — don’t inject here

    @ModifyReturnValue(method = "shouldAlwaysDropXp", at = @At("RETURN"))
    private boolean carnation$dropsXP(boolean original) {
        return false;
    }
}*/
//?}