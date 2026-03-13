package com.Aaeli.jinun.commands.impl;

import com.Aaeli.jinun.JinunContext;
import com.Aaeli.jinun.commands.Command;
import com.Aaeli.jinun.commands.CommandManager;
import com.Aaeli.jinun.friends.Friend;

public class FriendCommand extends Command {
    private final JinunContext ctx;

    public FriendCommand(JinunContext ctx) {
        super("friend", ".friend <add|remove|list> [name]");
        this.ctx = ctx;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            CommandManager.sendMessage("§cUsage: " + getUsage()); return;
        }
        switch (args[1].toLowerCase()) {
            case "add" -> {
                if (args.length < 3) { CommandManager.sendMessage("§cProvide a name"); return; }
                ctx.friendManager.add(args[2]);
                CommandManager.sendMessage("§aAdded friend: §f" + args[2]);
            }
            case "remove" -> {
                if (args.length < 3) { CommandManager.sendMessage("§cProvide a name"); return; }
                ctx.friendManager.remove(args[2]);
                CommandManager.sendMessage("§cRemoved friend: §f" + args[2]);
            }
            case "list" -> {
                var list = ctx.friendManager.getFriends();
                if (list.isEmpty()) { CommandManager.sendMessage("§7No friends."); return; }
                StringBuilder sb = new StringBuilder("§7Friends: ");
                for (Friend f : list) sb.append("§f").append(f.name).append("§7, ");
                CommandManager.sendMessage(sb.toString());
            }
            default -> CommandManager.sendMessage("§cUsage: " + getUsage());
        }
    }
}