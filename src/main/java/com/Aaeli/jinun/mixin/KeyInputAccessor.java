package com.Aaeli.jinun.mixin;

import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyInput.class)
public interface KeyInputAccessor {
    @Accessor("action") int getAction();
}