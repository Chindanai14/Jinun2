package com.Aaeli.jinun.managers;

import com.Aaeli.jinun.Jinun;
import com.Aaeli.jinun.features.module.Module;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.ArrayList;
import java.util.List;

public class KeybindManager {

    // key = GLFW keycode, value = list of modules bound to that key
    private final Int2ObjectMap<List<Module>> bindings = new Int2ObjectArrayMap<>();

    /**
     * ผูก module กับ key
     * ตัวอย่าง: bind(module, GLFW.GLFW_KEY_R)
     */
    public void bind(Module module, int keyCode) {
        // ลบ binding เดิมของ module ออกก่อน
        unbindAll(module);

        bindings.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(module);
        Jinun.LOGGER.info("[KeybindManager] Bound '{}' → key {}", module.getName(), keyCode);
    }

    /** ลบทุก binding ของ module */
    public void unbindAll(Module module) {
        bindings.values().forEach(list -> list.remove(module));
    }

    /** เรียกเมื่อกด key — toggle module ที่ผูกอยู่ */
    public void onKeyPressed(int keyCode) {
        List<Module> modules = bindings.get(keyCode);
        if (modules == null || modules.isEmpty()) return;
        for (Module m : modules) {
            m.toggle();
            Jinun.LOGGER.info("[KeybindManager] Toggled '{}' → {}",
                    m.getName(), m.isEnabled() ? "ON" : "OFF");
        }
    }

    /** ดึง keyCode ของ module (-1 = ไม่มี binding) */
    public int getKey(Module module) {
        for (Int2ObjectMap.Entry<List<Module>> entry : bindings.int2ObjectEntrySet()) {
            if (entry.getValue().contains(module)) return entry.getIntKey();
        }
        return -1;
    }

    public Int2ObjectMap<List<Module>> getBindings() { return bindings; }
}