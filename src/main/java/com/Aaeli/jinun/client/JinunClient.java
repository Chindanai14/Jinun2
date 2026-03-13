package com.Aaeli.jinun.client;

import com.Aaeli.jinun.Jinun;
import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.events.EventRender3D;
import net.fabricmc.api.ClientModInitializer;
// ✅ package ถูกต้อง
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;

public class JinunClient implements ClientModInitializer {

    public static JinunContext CONTEXT;

    @Override
    public void onInitializeClient() {
        Jinun.LOGGER.info("[{}] Booting engine...", Jinun.NAME);
        CONTEXT = new JinunContext();
        CONTEXT.init();

        WorldRenderEvents.AFTER_ENTITIES.register(context -> {
            if (CONTEXT == null) return;
            if (context.matrixStack() == null) return;
            if (context.consumers() == null) return;
            CONTEXT.eventBus.post(new EventRender3D(
                    context.matrixStack(),
                    context.consumers(),
                    context.camera().getPos(),
                    context.tickDelta()
            ));
        });

        Jinun.LOGGER.info("[{}] Engine started.", Jinun.NAME);
    }
}