package com.Aaeli.jinun.managers;

import com.Aaeli.jinun.Jinun;
import com.Aaeli.jinun.friends.Friend;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.util.*;

public class FriendManager {
    private final List<Friend> friends = new ArrayList<>();
    private final File friendFile;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public FriendManager() {
        File dir = FabricLoader.getInstance().getConfigDir().resolve("jinun").toFile();
        if (!dir.exists()) dir.mkdirs();
        this.friendFile = new File(dir, "friends.json");
        load();
    }

    public void add(String name) {
        if (!isFriend(name)) {
            friends.add(new Friend(name));
            save();
        }
    }

    public void remove(String name) {
        friends.removeIf(f -> f.name.equalsIgnoreCase(name));
        save();
    }

    public boolean isFriend(String name) {
        return friends.stream().anyMatch(f -> f.name.equalsIgnoreCase(name));
    }

    public List<Friend> getFriends() { return friends; }

    private void save() {
        JsonArray arr = new JsonArray();
        for (Friend f : friends) arr.add(f.name);
        try (FileWriter w = new FileWriter(friendFile)) {
            GSON.toJson(arr, w);
        } catch (Exception e) {
            Jinun.LOGGER.error("[FriendManager] Failed to save", e);
        }
    }

    private void load() {
        if (!friendFile.exists()) return;
        try (FileReader r = new FileReader(friendFile)) {
            JsonArray arr = GSON.fromJson(r, JsonArray.class);
            if (arr == null) return;
            for (JsonElement el : arr) friends.add(new Friend(el.getAsString()));
        } catch (Exception e) {
            Jinun.LOGGER.error("[FriendManager] Failed to load", e);
        }
    }
}