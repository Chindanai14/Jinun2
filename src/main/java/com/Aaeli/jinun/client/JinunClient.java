package com.Aaeli.jinun.client;

import net.fabricmc.api.ClientModInitializer;

public class JinunClient implements ClientModInitializer {

    public static final String NAME = "Jinun";
    public static final String VERSION = "0.1";

    private static JinunClient INSTANCE;

    public static JinunClient getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;

        System.out.println(NAME + " client loaded!");

        initManagers();
    }

    private void initManagers() {
        // ModuleManager.init();
        // EventBus.init();
        // CommandManager.init();
    }
}