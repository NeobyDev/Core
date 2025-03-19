package br.com.core.Commands.register;

import br.com.core.Commands.Command;
import br.com.core.Commands.CommandLib;
import org.bukkit.plugin.Plugin;

public abstract class RegisterBase {
    private CommandLib commandLib;

    public abstract void setup();

    public CommandLib registerCommand(Command command, Plugin plugin) {
        return this.commandLib;
    }

    public RegisterBase(CommandLib commandLib) {
        this.commandLib = commandLib;
    }

    public CommandLib getCommandLib() {
        return this.commandLib;
    }
}
