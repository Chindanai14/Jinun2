package com.Aaeli.jinun.mixin;

import com.Aaeli.jinun.client.JinunClient;
import com.Aaeli.jinun.ui.clickgui.ClickGui;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {

    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int key, KeyInput input, CallbackInfo ci) {
        if (((KeyInputAccessor)(Object) input).getAction() != GLFW.GLFW_PRESS) return;
        if (JinunClient.CONTEXT == null) return;

        // Right Shift — toggle Click GUI
        if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.currentScreen instanceof ClickGui) {
                mc.setScreen(null);
            } else {
                mc.setScreen(new ClickGui(JinunClient.CONTEXT));
            }
            return;
        }

        JinunClient.CONTEXT.keybindManager.onKeyPressed(key);
    }
}