package com.Aaeli.jinun.ui.clickgui;

import com.Aaeli.jinun.features.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class Panel {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    public static final int WIDTH    = 100;
    public static final int HEADER_H = 14;
    public static final int BUTTON_H = 12;

    private static final int COL_HEADER = 0xCC0F3460;
    private static final int COL_BG     = 0xCC1A1A2E;
    private static final int COL_ON     = 0xCC16213E;
    private static final int COL_HOVER  = 0x33FFFFFF;
    private static final int COL_ACCENT = 0xFF5BC0EB;

    public final String       title;
    public final List<Module> modules;
    public       int          x, y;
    public       boolean      collapsed = false;

    private boolean dragging = false;
    private double  dragOffX, dragOffY;

    public Panel(String title, List<Module> modules, int x, int y) {
        this.title   = title;
        this.modules = modules;
        this.x       = x;
        this.y       = y;
    }

    public void render(DrawContext ctx, int mouseX, int mouseY) {
        // Header
        ctx.fill(x, y, x + WIDTH, y + HEADER_H, COL_HEADER);
        ctx.fill(x, y + HEADER_H - 1, x + WIDTH, y + HEADER_H, COL_ACCENT);
        ctx.drawTextWithShadow(mc.textRenderer, title, x + 4, y + 3, 0xFFFFFF);

        if (collapsed) return;

        int totalH = modules.size() * BUTTON_H;
        ctx.fill(x, y + HEADER_H, x + WIDTH, y + HEADER_H + totalH, COL_BG);

        for (int i = 0; i < modules.size(); i++) {
            Module m    = modules.get(i);
            int    btnY = y + HEADER_H + i * BUTTON_H;
            boolean hover = mouseX >= x && mouseX <= x + WIDTH
                    && mouseY >= btnY && mouseY <= btnY + BUTTON_H;

            if (m.isEnabled()) ctx.fill(x, btnY, x + WIDTH, btnY + BUTTON_H, COL_ON);
            if (hover)          ctx.fill(x, btnY, x + WIDTH, btnY + BUTTON_H, COL_HOVER);
            if (m.isEnabled())  ctx.fill(x, btnY, x + 2, btnY + BUTTON_H, COL_ACCENT);

            ctx.drawTextWithShadow(mc.textRenderer, m.getName(), x + 5, btnY + 2,
                    m.isEnabled() ? 0x5BC0EB : 0xAAAAAA);
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Header
        if (isInHeader(mouseX, mouseY)) {
            if (button == 0) {
                dragging = true;
                dragOffX = mouseX - x;
                dragOffY = mouseY - y;
            } else if (button == 1) {
                collapsed = !collapsed;
            }
            return true;
        }
        if (collapsed) return false;

        // Module buttons
        for (int i = 0; i < modules.size(); i++) {
            int btnY = y + HEADER_H + i * BUTTON_H;
            if (mouseX >= x && mouseX <= x + WIDTH
                    && mouseY >= btnY && mouseY <= btnY + BUTTON_H) {
                if (button == 0) modules.get(i).toggle();
                return true;
            }
        }
        return false;
    }

    public void mouseDragged(double mouseX, double mouseY, int button) {
        if (dragging && button == 0) {
            x = (int)(mouseX - dragOffX);
            y = (int)(mouseY - dragOffY);
        }
    }

    public void mouseReleased() { dragging = false; }

    private boolean isInHeader(double mx, double my) {
        return mx >= x && mx <= x + WIDTH && my >= y && my <= y + HEADER_H;
    }

    public int totalHeight() {
        return HEADER_H + (collapsed ? 0 : modules.size() * BUTTON_H);
    }
}