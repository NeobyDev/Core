package br.com.core.Listeners;

import br.com.core.Core;
import br.com.core.Tag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NametagListener implements Listener {

    public static void updateNametag(Player player) {
        // Obtém a tag do jogador
        Tag tag = Core.getInstance().getProfileManager().getProfile(player).getData().getTag();

        // Itera sobre todos os jogadores online e atualiza a nametag deles
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            updatePlayerNametagFor(onlinePlayer, player, tag);
        }
    }

    private static void updatePlayerNametagFor(Player onlinePlayer, Player targetPlayer, Tag tag) {
        // Obtém o scoreboard do jogador online, ou cria um novo se necessário
        Scoreboard scoreboard = onlinePlayer.getScoreboard();
        if (scoreboard == null || scoreboard == Bukkit.getScoreboardManager().getMainScoreboard()) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            onlinePlayer.setScoreboard(scoreboard);
        }

        // Nome do time será o nome da tag (evita criar um time para cada jogador)
        String teamName = tag.name();
        Team team = scoreboard.getTeam(teamName);

        // Se o time ainda não existir, cria um novo
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setNameTagVisibility(NameTagVisibility.ALWAYS);
        }

        // Aplica cores corretamente no prefixo usando ChatColor.translateAlternateColorCodes
        String formattedPrefix = ChatColor.translateAlternateColorCodes('&', tag.getTabname());
        team.setPrefix(formattedPrefix);

        // Remove o jogador de outros times e adiciona ao time correto
        setPlayerTeam(targetPlayer, team, scoreboard);
    }

    private static void setPlayerTeam(Player player, Team newTeam, Scoreboard scoreboard) {
        // Remove o jogador de outros times
        for (Team team : scoreboard.getTeams()) {
            if (team.hasEntry(player.getName())) {
                team.removeEntry(player.getName());
            }
        }

        // Adiciona o jogador ao novo time
        newTeam.addEntry(player.getName());
    }
}