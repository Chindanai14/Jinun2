package com.Aaeli.jinun.features.module;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.core.event.EventBus;
import com.Aaeli.jinun.events.EventWorldChange;
import com.Aaeli.jinun.settings.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class Module {
    protected final JinunContext ctx;
    private final String   name;
    private final Category category;
    private final int      defaultKey;
    private boolean        enabled;
    private final List<Setting<?>> settings = new ArrayList<>();

    private final Consumer<EventWorldChange> worldListener = this::onWorldChangeInternal;

    public Module(JinunContext ctx) {
        this.ctx = ctx;
        ModuleInfo info = getClass().getAnnotation(ModuleInfo.class);
        this.name       = info.name();
        this.category   = info.category();
        this.defaultKey = info.defaultKey();

        // ผูก default key อัตโนมัติถ้ามีกำหนดไว้
        if (defaultKey != -1) {
            ctx.keybindManager.bind(this, defaultKey);
        }

        ctx.eventBus.bind(EventWorldChange.ID, worldListener);
    }

    public String          getName()      { return name; }
    public Category        getCategory()  { return category; }
    public boolean         isEnabled()    { return enabled; }
    public int             getDefaultKey(){ return defaultKey; }
    public List<Setting<?>> getSettings() { return settings; }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) { bindEvents(ctx.eventBus);   onEnable(); }
        else         { unbindEvents(ctx.eventBus); onDisable(); }
        ctx.configManager.save();
    }

    public void toggle() { setEnabled(!this.enabled); }

    protected void bindEvents(EventBus bus)   {}
    protected void unbindEvents(EventBus bus) {}
    protected void onEnable()     {}
    protected void onDisable()    {}
    protected void onWorldJoin()  {}
    protected void onWorldLeave() {}

    private void onWorldChangeInternal(EventWorldChange e) {
        if (e.world != null) onWorldJoin();
        else                 onWorldLeave();
    }

    protected <T extends Setting<?>> T register(T s) {
        settings.add(s);
        return s;
    }
}