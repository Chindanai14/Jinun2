package com.Aaeli.jinun.ui.hud.impl;

import com.Aaeli.jinun.client.JinunClient;
import com.Aaeli.jinun.features.module.Module;
import com.Aaeli.jinun.ui.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;

public class ArrayListHud extends HudElement {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public ArrayListHud() { super("ArrayList"); }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (JinunClient.CONTEXT == null) return;
        if (mc.options.hudHidden) return;

        List<Module> active = JinunClient.CONTEXT.moduleManager.getModules()
                .stream()
                .filter(Module::isEnabled)
                .sorted(Comparator.comparingInt(
                        m -> -mc.textRenderer.getWidth(m.getName())
                ))
                .toList();

        int screenWidth = mc.getWindow().getScaledWidth();
        int y = 2;

        for (Module m : active) {
            String name = m.getName();
            int textW = mc.textRenderer.getWidth(name);
            int x     = screenWidth - textW - 4;

            // Background
            context.fill(x - 2, y - 1, x + textW + 2, y + 9, 0x80000000);
            // Accent bar on right
            context.fill(x + textW + 2, y - 1, x + textW + 3, y + 9, 0xFF5BC0EB);
            // Text
            context.drawTextWithShadow(mc.textRenderer, name, x, y, 0xFFFFFF);

            y += 11;
        }
    }
}