package com.Aaeli.jinun.mixin;

import com.Aaeli.jinun.client.JinunClient;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(method = "sendMessage", at = @At("HEAD"), cancellable = true)
    private void onSendMessage(String message, boolean addToHistory,
                               CallbackInfoReturnable<Boolean> cir) {
        if (JinunClient.CONTEXT == null) return;
        if (JinunClient.CONTEXT.commandManager.handle(message)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}