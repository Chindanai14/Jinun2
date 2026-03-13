package com.Aaeli.jinun.settings.types;

import com.Aaeli.jinun.settings.Setting;

public class NumberSetting extends Setting<Double> {
    public final double min, max;

    public NumberSetting(String name, Double def, double min, double max) {
        super(name, def);
        this.min = min;
        this.max = max;
    }

    @Override
    public void set(Double val) {
        value = Math.max(min, Math.min(max, val));
    }
}