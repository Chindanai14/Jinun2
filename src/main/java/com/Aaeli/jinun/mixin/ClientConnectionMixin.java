package com.Aaeli.jinun.mixin;

import com.Aaeli.jinun.client.JinunClient;
import com.Aaeli.jinun.events.EventPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(
            method = "send(Lnet/minecraft/network/packet/Packet;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        if (JinunClient.CONTEXT == null) return;
        EventPacket event = EventPacket.obtain(packet, EventPacket.Stage.MODIFY, false);
        JinunClient.CONTEXT.eventBus.post(event);
        if (event.isCancelled()) ci.cancel();
    }
}