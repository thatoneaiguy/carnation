package com.everest.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class GravePersistentStorage {
    public static final String GRAVE_DATA_KEY = "carnation.GraveData";

    public static void load(PlayerEntity player) {
        NbtCompound nbt = player.getPersistentData();

    }
}
