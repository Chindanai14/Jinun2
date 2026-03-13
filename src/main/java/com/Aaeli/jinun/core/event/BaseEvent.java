package com.Aaeli.jinun.core.event;

public abstract class BaseEvent {
    private boolean cancelled;

    public abstract int getEventId();

    public boolean isCancelable()                      { return true; }
    public boolean isCancelled()                       { return cancelled; }
    public void    setCancelled(boolean cancelled)     { this.cancelled = cancelled; }
    public void    cancel()                            { this.cancelled = true; }
}