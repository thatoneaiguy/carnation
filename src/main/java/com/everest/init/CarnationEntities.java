package com.everest.init;

import com.everest.Carnation;
import com.everest.entity.GraveEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
//? if >=1.20.0 {
import net.minecraft.registry.Registries;
/*import net.minecraft.util.registry.Registry;*/
//?} else {
/*import net.minecraft.util.registry.Registry;*/
import net.minecraft.registry.Registries;
//?}
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

public class CarnationEntities {

    public static final EntityType<GraveEntity> GRAVE_ENTITY_TYPE =
            //? if >=1.20.0 {
            Registry.register(
                    Registries.ENTITY_TYPE,
                    new Identifier(Carnation.MODID, "grave"),
                    FabricEntityTypeBuilder.<GraveEntity>create(SpawnGroup.MISC, GraveEntity::new)
                            .dimensions(EntityDimensions.fixed(0.75F, 0.75F))
                            .fireImmune()
                            .build()
            );
    //?} else {
    /*Registry.register(
        Registry.ENTITY_TYPE,
        new Identifier(Carnation.MODID, "grave"),
        FabricEntityTypeBuilder.<GraveEntity>create(SpawnGroup.MISC, GraveEntity::new)
                .dimensions(EntityDimensions.fixed(0.75F, 0.75F))
                .fireImmune()
                .build()
    );*/
    //?}

    public static void register() {}
}