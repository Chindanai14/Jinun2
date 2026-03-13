package com.Aaeli.jinun.events;

import com.Aaeli.jinun.core.event.BaseEvent;
import net.minecraft.client.world.ClientWorld;

public final class EventWorldChange extends BaseEvent {
    public static final int ID = 3;

    /** null = ออกจาก world, non-null = เข้า world */
    public final ClientWorld world;

    public EventWorldChange(ClientWorld world) { this.world = world; }

    @Override public int     getEventId()   { return ID; }
    @Override public boolean isCancelable() { return false; }
}