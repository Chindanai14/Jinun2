package com.Aaeli.jinun.commands;

import com.Aaeli.jinun.Jinun;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {
    public static final String PREFIX = ".";
    private final List<Command> commands = new ArrayList<>();

    public void register(Command cmd) { commands.add(cmd); }

    /** return true = intercepted (cancel chat send) */
    public boolean handle(String message) {
        if (!message.startsWith(PREFIX)) return false;
        String[] args = message.substring(PREFIX.length()).trim().split(" +");
        if (args.length == 0 || args[0].isEmpty()) return false;

        for (Command cmd : commands) {
            if (cmd.getName().equalsIgnoreCase(args[0])) {
                try {
                    cmd.execute(args);
                } catch (Exception e) {
                    Jinun.LOGGER.error("[CommandManager] Error", e);
                    sendMessage("§cError: " + e.getMessage());
                }
                return true;
            }
        }
        sendMessage("§cUnknown command — type §f.help");
        return true;
    }

    public static void sendMessage(String msg) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player != null)
            mc.player.sendMessage(Text.literal("§7[§bJinun§7] " + msg), false);
    }

    public List<Command> getCommands() { return commands; }
}