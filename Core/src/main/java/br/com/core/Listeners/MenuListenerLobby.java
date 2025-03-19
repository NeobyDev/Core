package br.com.core.Listeners;

import br.com.core.Core;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MenuListenerLobby implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

            if (!event.getView().getTitle().equals("Selecionar Lobby (1/1)")) {
                return;
            }

            event.setCancelled(true);

            if (event.getCurrentItem() == null) {
                return;
            }

        Player player = (Player) event.getWhoClicked();

            if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                player.sendMessage("§aEnviando para #1");
                sendServer(player, "Lobby");
            }
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