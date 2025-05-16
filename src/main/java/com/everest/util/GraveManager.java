package com.everest.util;

import com.everest.entity.GraveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GraveManager {
    private static final Map<UUID, GraveData> GRAVE_MAP = new HashMap<>();

    public static record GraveData(Identifier world, BlockPos pos, String dimension) {}

    public static void addGrave(PlayerEntity player, GraveEntity grave) {
        GRAVE_MAP.put(player.getUuid(), new GraveData(
                grave.getWorld().getRegistryKey().getValue(),
                grave.getBlockPos(),
                grave.getWorld().getDimensionKey().getValue().toString()
        ));
    }

    public static void removeGrave(UUID playerUUID) {
        GRAVE_MAP.remove(playerUUID);
    }

    public static Optional<GraveData> getGraveData(UUID playerUUID) {
        return Optional.ofNullable(GRAVE_MAP.get(playerUUID));
    }
}
