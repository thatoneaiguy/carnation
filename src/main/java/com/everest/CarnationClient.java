package com.everest;

import com.everest.init.CarnationEntities;
import com.everest.render.GraveEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class CarnationClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(CarnationEntities.GRAVE_ENTITY_TYPE, GraveEntityRenderer::new);
    }
}
