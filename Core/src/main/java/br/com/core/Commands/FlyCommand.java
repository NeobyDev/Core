package br.com.core.Commands;

import br.com.core.Core;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private Core main;

    public FlyCommand(Core main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cVocê não tem acesso a este recurso.");
            commandSender.sendMessage("§cAdquira Ranks para voar nos lobbies.");
            return true;
        }
        Player p = (Player) commandSender;
        if (Core.fly.contains(p)) {
            Core.fly.remove(p);
            p.sendMessage("§aO voo foi habilitado.");
            p.setAllowFlight(true);
            p.setFlying(true);
            return true;
        } else {
            Core.fly.add(p);
            p.sendMessage("§cO voo foi desabilitado.");
            p.setAllowFlight(false);
            p.setFlying(false);
            return false;
        }
    }
}
