package com.Aaeli.jinun.events;

import com.Aaeli.jinun.core.event.BaseEvent;

public final class EventTickEnd extends BaseEvent {
    public static final int         ID       = 1;
    public static final EventTickEnd INSTANCE = new EventTickEnd();

    private EventTickEnd() {}

    @Override public int     getEventId()   { return ID; }
    @Override public boolean isCancelable() { return false; }
}