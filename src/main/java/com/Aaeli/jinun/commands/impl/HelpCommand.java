package com.Aaeli.jinun.commands.impl;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.commands.Command;
import com.Aaeli.jinun.commands.CommandManager;

public class HelpCommand extends Command {
    private final JinunContext ctx;

    public HelpCommand(JinunContext ctx) {
        super("help", ".help");
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) {
        CommandManager.sendMessage("§7=== Commands ===");
        for (Command cmd : ctx.commandManager.getCommands())
            CommandManager.sendMessage("§b" + cmd.getUsage());
    }
}