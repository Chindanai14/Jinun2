package com.Aaeli.jinun.mixin;

import com.Aaeli.jinun.client.JinunClient;
import com.Aaeli.jinun.events.EventTickEnd;
import com.Aaeli.jinun.events.EventTickStart;
import com.Aaeli.jinun.events.EventWorldChange;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTickStart(CallbackInfo ci) {
        if (JinunClient.CONTEXT == null) return;
        JinunClient.CONTEXT.eventBus.flush();
        JinunClient.CONTEXT.eventBus.post(EventTickStart.INSTANCE);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTickEnd(CallbackInfo ci) {
        if (JinunClient.CONTEXT != null)
            JinunClient.CONTEXT.eventBus.post(EventTickEnd.INSTANCE);
    }

    @Inject(method = "joinWorld", at = @At("TAIL"))
    private void onWorldJoin(ClientWorld world, CallbackInfo ci) {
        if (JinunClient.CONTEXT != null)
            JinunClient.CONTEXT.eventBus.post(new EventWorldChange(world));
    }

    @Inject(method = "disconnect()V", at = @At("HEAD"))
    private void onWorldLeave(CallbackInfo ci) {
        if (JinunClient.CONTEXT == null) return;
        // ยกเลิก scheduled tasks ทั้งหมดเมื่อออกจาก world
        JinunClient.CONTEXT.scheduler.cancelAll();
        JinunClient.CONTEXT.eventBus.post(new EventWorldChange(null));
    }
}