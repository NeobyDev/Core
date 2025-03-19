package br.com.core.Listeners;

import br.com.core.Core;
import br.com.core.Tag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ChatListener implements Listener {
    private final Scoreboard scoreboard;

    public ChatListener() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        // Assume team names are based on tags or ranks, not player names
        Tag tag = Core.getInstance().getProfileManager().getProfile(player).getData().getTag();
        String teamName = tag.getTabname();
        Team team = this.scoreboard.getTeam(teamName);

        if (team == null) {
            team = this.scoreboard.registerNewTeam(teamName);
        }

        if (tag != null) {
            event.setFormat(tag.getChatname() + player.getDisplayName() + ": §f" + event.getMessage().replace("&", "§"));
        }
    }
}