package br.com.core.Commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;

public abstract class Command {
    public void execute(CommandSender sender, String ... args) {
    }

    public List<String> tabComplete(CommandSender sender, String alias, String ... args) {
        return new ArrayList<String>();
    }
}

