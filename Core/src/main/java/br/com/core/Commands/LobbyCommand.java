package br.com.core.Commands;

import br.com.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LobbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("lobby") || command.getName().equalsIgnoreCase("l") || command.getName().equalsIgnoreCase("hub")) {
            if (!(commandSender instanceof Player)) {
                System.out.println(ChatColor.RED + "Você não é um jogador.");
            }
            Player player = (Player) commandSender;
            sendServer(player, "Lobby");
        }
        return true;
    }

    private void sendServer(Player player, String server) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Core.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
    }
}
