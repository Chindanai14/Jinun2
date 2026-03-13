package com.Aaeli.jinun.ui.hud;

import net.minecraft.client.gui.DrawContext;

public abstract class HudElement {
    public final String name;
    private boolean visible = true;

    public HudElement(String name) { this.name = name; }

    public abstract void render(DrawContext context, float tickDelta);

    public boolean isVisible()              { return visible; }
    public void    setVisible(boolean v)    { this.visible = v; }
}