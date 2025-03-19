package br.com.core.Listeners;

import br.com.core.Core;
import br.com.core.Menusystem.menu.LobbySelector;
import br.com.core.Menusystem.menu.MenuProfile;
import br.com.core.Menusystem.menu.ServerSelectorMenu;
import br.com.core.Utils.ItemBuilder;
import br.com.core.mysql.PlayerData;
import br.com.core.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private Core main;

    public PlayerInteractListener(Core main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (player.getItemInHand() == null || !player.getItemInHand().hasItemMeta() || !player.getItemInHand().getItemMeta().hasDisplayName()) {
            return;
        }

        String itemName = player.getItemInHand().getItemMeta().getDisplayName();
        PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName());
        playerData.load(player); // Carrega os dados da database

        Visibility currentVisibility = playerData.getVisibility(); // Obtém o estado atual do jogador

        // Alternar visibilidade dos jogadores
        if (itemName.equalsIgnoreCase("§fJogadores: §aON")) {
            e.setCancelled(true);

            // Se já estiver ativado, desativa
            if (currentVisibility == Visibility.ativado) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!onlinePlayer.equals(player)) {
                        player.hidePlayer(onlinePlayer);
                    }
                }
                player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, "§fJogadores: §cOFF").changeId(8).build());
                playerData.setVisibility(Visibility.desativado);
                playerData.save(player);
                Bukkit.getConsoleSender().sendMessage("🔴 Jogador " + player.getName() + " ocultou os jogadores.");
            }

        } else if (itemName.equalsIgnoreCase("§fJogadores: §cOFF")) {
            e.setCancelled(true);

            // Se já estiver desativado, ativa
            if (currentVisibility == Visibility.desativado) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    player.showPlayer(onlinePlayer);
                }
                player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, "§fJogadores: §aON").changeId(10).build());
                playerData.setVisibility(Visibility.ativado);
                playerData.save(player);
                Bukkit.getConsoleSender().sendMessage("🟢 Jogador " + player.getName() + " revelou os jogadores.");
            }
        }

        // Outros itens do menu
        if (itemName.equalsIgnoreCase("§aSelecionar jogo")) {
            new ServerSelectorMenu(player);
        } else if (itemName.equalsIgnoreCase("§aPerfil")) {
            new MenuProfile(player);
        } else if (itemName.equalsIgnoreCase("§aSelecionar lobby")) {
            new LobbySelector(player);
        }
    }
}