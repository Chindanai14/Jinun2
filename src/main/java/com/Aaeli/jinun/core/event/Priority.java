package com.Aaeli.jinun.core.event;

public enum Priority {
    LOW(0), NORMAL(1), HIGH(2), CRITICAL(3);
    public final int level;
    Priority(int level) { this.level = level; }
}