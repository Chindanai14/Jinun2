package com.Aaeli.jinun.managers;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.features.combat.Killaura;
import com.Aaeli.jinun.features.module.Module;
import com.Aaeli.jinun.features.render.ESP;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final JinunContext ctx;
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager(JinunContext ctx) { this.ctx = ctx; }

    public void init() {
        modules.add(new Killaura(ctx));
        modules.add(new ESP(ctx));
        // เพิ่ม module ใหม่ตรงนี้
    }

    public List<Module> getModules() { return modules; }
}