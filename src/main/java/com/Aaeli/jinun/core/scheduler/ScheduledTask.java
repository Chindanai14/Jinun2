package com.Aaeli.jinun.core.scheduler;

public class ScheduledTask {
    public enum Type { ONCE, REPEAT }

    public final Runnable task;
    public final Type     type;
    public final int      interval; // ticks
    public       int      ticksLeft;
    public       boolean  cancelled = false;

    public ScheduledTask(Runnable task, Type type, int delayTicks, int interval) {
        this.task      = task;
        this.type      = type;
        this.interval  = interval;
        this.ticksLeft = delayTicks;
    }

    public void cancel() { this.cancelled = true; }
}