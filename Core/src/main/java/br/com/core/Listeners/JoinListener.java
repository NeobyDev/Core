package br.com.core.Listeners;

import br.com.core.Core;
import br.com.core.Tag;
import br.com.core.Utils.Configs;
import br.com.core.Utils.ItemBuilder;
import br.com.core.mysql.Profile;
import br.com.core.mysql.PlayerData;
import br.com.core.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class JoinListener implements Listener {
    private final Core plugin;
    private final Scoreboard scoreboard;

    public JoinListener(Core plugin, Scoreboard scoreboard) {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player player = e.getPlayer();
        player.sendTitle("§b§lNEOBYMC", "§e§lLOBBY");
        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(player.getWorld().getSpawnLocation());

        Core.build.add(player);
        Core.fly.remove(player);

        if (Configs.contains("spawn")) {
            player.teleport(Configs.getLocation("spawn"));
        }

        // Chama o método para configurar os itens com base na visibilidade
        sendItems(player);

        // Atualiza a tag e a posição do jogador para todos os jogadores
        updateNametagAndPosition(player);

        // Atualiza as tags dos jogadores online para o jogador que acabou de entrar
        updateAllNametagsForPlayer(player);
    }

    public void sendItems(Player player) {
        player.getInventory().setItem(0, new ItemBuilder(Material.COMPASS, "§aSelecionar jogo").build());
        player.getInventory().setItem(1, new ItemBuilder(Material.SKULL_ITEM, "§aPerfil").withSkullOwner(player.getName()).build());
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR, "§aSelecionar lobby").build());

        PlayerData playerData = new PlayerData(player.getUniqueId(), player.getName());
        playerData.load(player); // Carrega os dados da database

        Visibility currentVisibility = playerData.getVisibility();

        if (currentVisibility == Visibility.ativado) {
            player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, "§fJogadores: §aON").changeId(10).build());
        } else if (currentVisibility == Visibility.desativado) {
            player.getInventory().setItem(7, new ItemBuilder(Material.INK_SACK, "§fJogadores: §cOFF").changeId(8).build());
        }
    }

    private void updateNametagAndPosition(Player player) {
        // Retrieve the player's profile and tag
        Profile profile = Core.getInstance().getProfileManager().getProfile(player);
        if (profile != null) {
            Tag tag = profile.getData().getTag();
            if (tag != null) {
                // Itera sobre todos os jogadores online e atualiza a nametag e a posição deles
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    updatePlayerNametagAndPositionFor(onlinePlayer, player, tag);
                }

                // Check if the player has a tag other than "membro"
                if (!tag.toString().equalsIgnoreCase("membro")) {
                    // Teleport the player slightly higher and enable flying
                    player.teleport(player.getLocation().add(0, 0.5, 0));
                    player.setAllowFlight(true);
                    player.setFlying(true);

                    // Send a message to all players in the chat
                    Bukkit.broadcastMessage(tag.getChatname() + player.getName() + " §6entrou neste lobby!");
                }
            }
        }
    }

    private void updatePlayerNametagAndPositionFor(Player onlinePlayer, Player targetPlayer, Tag tag) {
        // Create a new scoreboard for the player if they don't already have one
        Scoreboard scoreboard = onlinePlayer.getScoreboard();
        if (scoreboard == null || scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            onlinePlayer.setScoreboard(scoreboard);
        }

        // Nome do time será o nome da tag (evita criar um time para cada jogador)
        String teamName = tag.getTabname();
        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }

        // Aplica cores corretamente no prefixo usando ChatColor.translateAlternateColorCodes
        String formattedPrefix = ChatColor.translateAlternateColorCodes('&', tag.getTabname());
        team.setPrefix(formattedPrefix);

        // Remove o jogador de outros times e adiciona ao time correto
        removePlayerFromTeams(targetPlayer, scoreboard);
        team.addEntry(targetPlayer.getName());
        onlinePlayer.setScoreboard(scoreboard);
    }

    private static void removePlayerFromTeams(Player player, Scoreboard scoreboard) {
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }
    }

    private void updateAllNametagsForPlayer(Player player) {
        // Itera sobre todos os jogadores online e atualiza a tag de cada um em relação ao jogador que entrou
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = Core.getInstance().getProfileManager().getProfile(onlinePlayer);
            if (profile != null) {
                Tag tag = profile.getData().getTag();
                if (tag != null) {
                    updatePlayerNametagAndPositionFor(player, onlinePlayer, tag);
                }
            }
        }
    }
}