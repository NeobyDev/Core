package br.com.core.Commands;

import br.com.core.Core;
import br.com.core.Utils.ItemBuilder;
import br.com.core.Utils.Items;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BuildCommand implements CommandExecutor {

    private Core main;

    public BuildCommand() {
        this.main = main;
    }

    public static boolean hasBuilder(Player player) {
        return false;
    }

    Items gui = new Items();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cEste comando pode ser executado apenas por jogadores.");
            return true;
        }
        Player p = (Player) commandSender;
        if (Core.build.contains(p)) {
            Core.build.remove(p);
            p.sendMessage("§aBuild ativado.");
            p.getInventory().clear();
            p.setGameMode(GameMode.CREATIVE);
            return true;
        } else {
            Core.build.add(p);
            p.sendMessage("§cBuild desativado.");
            p.getInventory().clear();
            sendItems(p);
            p.setGameMode(GameMode.ADVENTURE);
            if (p.hasPermission("tag.youtuber") || p.hasPermission("*")) {
                Core.fly.add(p);
            }
        }

            return false;
        }
    public void sendItems(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS, "§aSelecionar jogo").build());
        player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM, "§aPerfil").withSkullOwner(player.getName()).build());
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR, "§aSelecionar lobby").build());
        player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, "§fJogadores: §aON").changeId(10).build());
    }
    }



