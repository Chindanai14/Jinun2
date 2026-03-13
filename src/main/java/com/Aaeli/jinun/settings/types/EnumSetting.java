package com.Aaeli.jinun.settings.types;

import com.Aaeli.jinun.settings.Setting;

public class EnumSetting<T extends Enum<T>> extends Setting<T> {
    private final T[] values;

    @SuppressWarnings("unchecked")
    public EnumSetting(String name, T defaultValue) {
        super(name, defaultValue);
        this.values = (T[]) defaultValue.getDeclaringClass().getEnumConstants();
    }

    /** เปลี่ยนไป enum ถัดไป (วนรอบ) */
    public void cycle() {
        int next = (value.ordinal() + 1) % values.length;
        value = values[next];
    }

    public T[] getValues() { return values; }
}