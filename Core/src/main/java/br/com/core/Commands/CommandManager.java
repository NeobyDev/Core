package br.com.core.Commands;

import br.com.core.Core;

public class CommandManager {

    private Core main;

    public CommandManager(Core main) {
        this.main = main;

        registerCommands();
    }

    private void registerCommands() {
        main.getServer().getPluginCommand("fly").setExecutor(new FlyCommand(main));
        main.getServer().getPluginCommand("build").setExecutor(new BuildCommand());
        main.getServer().getPluginCommand("spawn").setExecutor(new SpawnCommand());
    }
}
