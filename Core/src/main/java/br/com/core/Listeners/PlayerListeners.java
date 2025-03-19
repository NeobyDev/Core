package br.com.core.Listeners;

import br.com.core.Core;
import br.com.core.Utils.Configs;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;

public class PlayerListeners implements Listener {

    private Core main;
    public PlayerListeners(Core main) {
        this.main = main;
    }
    public Configs config;

    // Bloqueia quebra de bloco
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission(Utils.ADMIN_PERMISSION_NODE)) {
            e.setCancelled(true);
        }
    }

    // Bloqueia colocação de blocos
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!player.hasPermission(Utils.ADMIN_PERMISSION_NODE)) {
            e.setCancelled(true);
        }
    }

    // Evita o jogador cair no vazio
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location location = player.getLocation();

        if (player.getWorld().equals(main.getServer().getWorlds().get(0)) && location.getY() <= 0) {
            player.teleport(config.getLocation("spawn"));
        }
    }

    // Bloqueia spawn de qualquer mob ou animal
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        e.setCancelled(true);
    }

    // Bloqueia qualquer dano no jogador
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        e.setCancelled(true);
    }

    // Mantém sempre ensolarado e sem chuva
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        // Se estiver tentando mudar para chuva, cancela o evento
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    // Impede perda de fome e mantém a vida sempre cheia
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        Player player = (Player) e.getEntity();
        player.setFoodLevel(20);
        player.setSaturation(20.0f);
        player.setHealth(20.0);
        e.setCancelled(true);
    }

    // Impede que os jogadores mexam no inventário (exceto no próprio inventário)
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player) || e.getCurrentItem() == null) {
            return;
        }

        if (e.getInventory().getType() != InventoryType.PLAYER) {
            e.setCancelled(true);
        }
    }

    // Bloqueia interação se o jogador não for admin
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (Core.build.contains(player)) {
            e.setCancelled(true);
        }
    }

    // Remove a mensagem de saída e limpa o inventário
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        player.getInventory().clear();
        e.setQuitMessage(null);
    }

    // Bloqueia fogo e explosões
    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    // Impede que jogadores sejam kickados por fly ao receberem permissão para voar
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        if (player.getAllowFlight()) {
            e.setCancelled(false);
        }
    }
}