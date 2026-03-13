package com.Aaeli.jinun.ui.hud;

import com.Aaeli.jinun.Jinun;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class HudManager {
    private final List<HudElement> elements = new ArrayList<>();

    public void register(HudElement element) { elements.add(element); }

    public void render(DrawContext context, float tickDelta) {
        for (HudElement el : elements) {
            if (!el.isVisible()) continue;
            try {
                el.render(context, tickDelta);
            } catch (Exception e) {
                Jinun.LOGGER.error("[HudManager] {} crashed", el.name, e);
            }
        }
    }
}