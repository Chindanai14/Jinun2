package com.Aaeli.jinun.events;

import com.Aaeli.jinun.core.event.BaseEvent;

public final class EventTickStart extends BaseEvent {
    public static final int          ID       = 0;
    public static final EventTickStart INSTANCE = new EventTickStart();

    private EventTickStart() {}

    @Override public int     getEventId()   { return ID; }
    @Override public boolean isCancelable() { return false; }
}