package com.everest.init;

import com.everest.Carnation;
import com.everest.entity.GraveEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CarnationEntities {
    public static final EntityType<GraveEntity> GRAVE_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier(Carnation.MODID, "grave"),
            FabricEntityTypeBuilder.<GraveEntity>create(SpawnGroup.MISC, GraveEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5F, 0.5F))
                    .fireImmune()
                    .build());

    public static void register() {}
}
