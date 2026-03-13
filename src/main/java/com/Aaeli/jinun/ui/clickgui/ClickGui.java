package com.Aaeli.jinun.ui.clickgui;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.features.module.Category;
import com.Aaeli.jinun.features.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {
    private final JinunContext ctx;
    private final List<Panel>  panels = new ArrayList<>();

    public ClickGui(JinunContext ctx) {
        super(Text.literal("Jinun"));
        this.ctx = ctx;
    }

    @Override
    protected void init() {
        panels.clear();
        int x = 8;
        for (Category cat : Category.values()) {
            List<Module> mods = ctx.moduleManager.getModules()
                    .stream().filter(m -> m.getCategory() == cat).toList();
            if (!mods.isEmpty()) {
                panels.add(new Panel(cat.name(), mods, x, 8));
                x += Panel.WIDTH + 6;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, width, height, 0x70000000);
        for (Panel p : panels) p.render(context, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Panel p : panels)
            if (p.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button,
                                double deltaX, double deltaY) {
        for (Panel p : panels) p.mouseDragged(mouseX, mouseY, button);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (Panel p : panels) p.mouseReleased();
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override public boolean shouldPause() { return false; }
}