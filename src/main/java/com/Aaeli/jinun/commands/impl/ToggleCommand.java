package com.Aaeli.jinun.commands.impl;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.commands.Command;
import com.Aaeli.jinun.commands.CommandManager;
import com.Aaeli.jinun.features.module.Module;

public class ToggleCommand extends Command {
    private final JinunContext ctx;

    public ToggleCommand(JinunContext ctx) {
        super("toggle", ".toggle <module>");
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            CommandManager.sendMessage("§cUsage: " + getUsage()); return;
        }
        for (Module m : ctx.moduleManager.getModules()) {
            if (m.getName().equalsIgnoreCase(args[1])) {
                m.toggle();
                CommandManager.sendMessage("§f" + m.getName() + " §7→ "
                        + (m.isEnabled() ? "§aON" : "§cOFF"));
                return;
            }
        }
        CommandManager.sendMessage("§cModule not found: §f" + args[1]);
    }
}