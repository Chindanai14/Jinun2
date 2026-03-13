package com.Aaeli.jinun.commands;

public abstract class Command {
    private final String name;
    private final String usage;

    public Command(String name, String usage) {
        this.name  = name;
        this.usage = usage;
    }

    public String getName()  { return name; }
    public String getUsage() { return usage; }

    /** args[0] = command name, args[1..] = arguments */
    public abstract void execute(String[] args);
}