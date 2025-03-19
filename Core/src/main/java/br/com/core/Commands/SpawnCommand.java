package br.com.core.Commands;

import br.com.core.Utils.Configs;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    public Configs config;

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.config = new Configs("config.yml");
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (command.getName().equalsIgnoreCase("spawn")) {
                if (Configs.contains("spawn")) {
                    p.teleport(Configs.getLocation("spawn"));
                    if (p.hasPermission("*") || p.hasPermission("tag.youtuber")) {
                        p.teleport(new Location(p.getWorld(), 20.5, 90.0, 4.5, 90.0f, -1.0f));
                    }
                }
            }
        }
        return true;
    }
}
