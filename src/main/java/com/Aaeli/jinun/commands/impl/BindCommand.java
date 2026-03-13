package com.Aaeli.jinun.commands.impl;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.commands.Command;
import com.Aaeli.jinun.commands.CommandManager;
import com.Aaeli.jinun.features.module.Module;
import org.lwjgl.glfw.GLFW;

public class BindCommand extends Command {
    private final JinunContext ctx;

    public BindCommand(JinunContext ctx) {
        super("bind", ".bind <module> <key>  e.g. .bind Killaura R");
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 3) {
            CommandManager.sendMessage("§cUsage: " + getUsage()); return;
        }

        Module target = null;
        for (Module m : ctx.moduleManager.getModules())
            if (m.getName().equalsIgnoreCase(args[1])) { target = m; break; }

        if (target == null) {
            CommandManager.sendMessage("§cModule not found: §f" + args[1]); return;
        }

        int keyCode = parseKey(args[2]);
        if (keyCode == -1) {
            CommandManager.sendMessage("§cInvalid key: §f" + args[2]); return;
        }

        ctx.keybindManager.bind(target, keyCode);
        CommandManager.sendMessage("§fBound §b" + target.getName() + "§f → §b" + args[2].toUpperCase());
    }

    private static int parseKey(String key) {
        key = key.toUpperCase();
        if (key.length() == 1) {
            char c = key.charAt(0);
            if (c >= 'A' && c <= 'Z') return c;  // GLFW_KEY_A = 65 = 'A'
            if (c >= '0' && c <= '9') return c;  // GLFW_KEY_0 = 48
        }
        return switch (key) {
            case "F1"  -> GLFW.GLFW_KEY_F1;  case "F2"  -> GLFW.GLFW_KEY_F2;
            case "F3"  -> GLFW.GLFW_KEY_F3;  case "F4"  -> GLFW.GLFW_KEY_F4;
            case "F5"  -> GLFW.GLFW_KEY_F5;  case "F6"  -> GLFW.GLFW_KEY_F6;
            case "F7"  -> GLFW.GLFW_KEY_F7;  case "F8"  -> GLFW.GLFW_KEY_F8;
            case "F9"  -> GLFW.GLFW_KEY_F9;  case "F10" -> GLFW.GLFW_KEY_F10;
            case "F11" -> GLFW.GLFW_KEY_F11; case "F12" -> GLFW.GLFW_KEY_F12;
            case "SPACE"  -> GLFW.GLFW_KEY_SPACE;
            case "TAB"    -> GLFW.GLFW_KEY_TAB;
            case "ENTER"  -> GLFW.GLFW_KEY_ENTER;
            case "LSHIFT", "LEFT_SHIFT"   -> GLFW.GLFW_KEY_LEFT_SHIFT;
            case "RSHIFT", "RIGHT_SHIFT"  -> GLFW.GLFW_KEY_RIGHT_SHIFT;
            case "LCTRL",  "LEFT_CTRL"    -> GLFW.GLFW_KEY_LEFT_CONTROL;
            case "RCTRL",  "RIGHT_CTRL"   -> GLFW.GLFW_KEY_RIGHT_CONTROL;
            case "LALT",   "LEFT_ALT"     -> GLFW.GLFW_KEY_LEFT_ALT;
            case "RALT",   "RIGHT_ALT"    -> GLFW.GLFW_KEY_RIGHT_ALT;
            case "UP"    -> GLFW.GLFW_KEY_UP;   case "DOWN"  -> GLFW.GLFW_KEY_DOWN;
            case "LEFT"  -> GLFW.GLFW_KEY_LEFT; case "RIGHT" -> GLFW.GLFW_KEY_RIGHT;
            default -> -1;
        };
    }
}