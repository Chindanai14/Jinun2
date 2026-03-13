package com.Aaeli.jinun.settings.types;

import com.Aaeli.jinun.settings.Setting;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean def) { super(name, def); }
    public void toggle() { value = !value; }
}