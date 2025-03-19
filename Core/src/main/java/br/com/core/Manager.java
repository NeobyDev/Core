package br.com.core;

public class Manager {
    protected Core plugin;

    public Manager(Core plugin) {
        this.plugin = plugin;
    }

    public Core getPlugin() {
        return this.plugin;
    }
}

