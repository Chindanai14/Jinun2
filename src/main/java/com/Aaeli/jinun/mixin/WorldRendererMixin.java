package com.Aaeli.jinun.mixin;

import com.Aaeli.jinun.client.JinunClient;
import com.Aaeli.jinun.events.EventRender3D;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void afterRender(RenderTickCounter tickCounter,
                             boolean renderBlockOutline,
                             Camera camera,
                             GameRenderer gameRenderer,
                             LightmapTextureManager lightmapTextureManager,
                             Matrix4f positionMatrix,
                             Matrix4f projectionMatrix,
                             CallbackInfo ci) {
        if (JinunClient.CONTEXT == null) return;

        MatrixStack matrices = new MatrixStack();
        matrices.multiplyPositionMatrix(positionMatrix);

        JinunClient.CONTEXT.eventBus.post(new EventRender3D(
                matrices,
                camera.getPos(),
                tickCounter.getTickDelta(true)
        ));
    }
}