package br.com.core.Commands;

import br.com.core.Commands.register.BukkitRegister;
import br.com.core.Commands.register.RegisterBase;
import org.bukkit.plugin.Plugin;

public class CommandLib {
    private Plugin bukkitPlugin;
    private RegisterBase platform;

    public CommandLib setupBukkit(Plugin bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
        this.platform = new BukkitRegister(this);
        this.platform.setup();
        return this;
    }

    public CommandLib register(Command command) {
        if (this.bukkitPlugin != null) {
            this.platform.registerCommand(command, this.bukkitPlugin);
        }
        return this;
    }
}