package com.Aaeli.jinun.managers;

import com.Aaeli.jinun.Jinun;
import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.features.module.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class ConfigManager {
    private final JinunContext ctx;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;

    public ConfigManager(JinunContext ctx) {
        this.ctx = ctx;
        File dir = FabricLoader.getInstance().getConfigDir().resolve("jinun").toFile();
        if (!dir.exists()) dir.mkdirs();
        this.configFile = new File(dir, "modules.json");
    }

    public void save() {
        JsonObject root = new JsonObject();
        for (Module m : ctx.moduleManager.getModules()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("enabled", m.isEnabled());
            // บันทึก keybind
            int key = ctx.keybindManager.getKey(m);
            obj.addProperty("key", key);
            root.add(m.getName(), obj);
        }
        try (FileWriter w = new FileWriter(configFile)) {
            GSON.toJson(root, w);
        } catch (Exception e) {
            Jinun.LOGGER.error("[ConfigManager] Failed to save config", e);
        }
    }

    public void load() {
        if (!configFile.exists()) return;
        try (FileReader r = new FileReader(configFile)) {
            JsonObject root = GSON.fromJson(r, JsonObject.class);
            if (root == null) return;
            for (Module m : ctx.moduleManager.getModules()) {
                if (!root.has(m.getName())) continue;
                JsonObject obj = root.getAsJsonObject(m.getName());

                // โหลด enabled state
                if (obj.has("enabled"))
                    m.setEnabled(obj.get("enabled").getAsBoolean());

                // โหลด keybind (ถ้ามีบันทึกไว้)
                if (obj.has("key")) {
                    int key = obj.get("key").getAsInt();
                    if (key != -1) ctx.keybindManager.bind(m, key);
                }
            }
        } catch (Exception e) {
            Jinun.LOGGER.error("[ConfigManager] Failed to load config", e);
        }
    }
}