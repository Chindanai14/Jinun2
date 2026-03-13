package com.Aaeli.jinun.settings;

public abstract class Setting<T> {
    public final String name;
    protected T value;

    public Setting(String name, T defaultValue) {
        this.name  = name;
        this.value = defaultValue;
    }

    public T    get()       { return value; }
    public void set(T val)  { this.value = val; }
}