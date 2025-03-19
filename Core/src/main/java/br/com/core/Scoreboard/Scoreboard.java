package br.com.core.Scoreboard;

import br.com.core.Core;
import br.com.core.Rank;
import br.com.core.Utils.TagAPI;
import br.com.core.mysql.Profile;
import fr.mrmicky.fastboard.FastBoard;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class Scoreboard implements Listener {
    private final Map<UUID, FastBoard> boards = new HashMap<>();

    public Scoreboard(Core plugin, TagAPI tagAPI) {
        BungeeChannelApi api = BungeeChannelApi.of(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (FastBoard board : boards.values()) {
                Player player = board.getPlayer();
                Profile profile = Core.getInstance().getProfileManager().getProfile(player);
                if (profile != null) {
                    Rank rank = profile.getData().getRank();
                    updateBoard(board, "", "&fRank: " + rank.getScorename(), "&fLobby: &7#1", "", "&fPlayers: &a" + plugin.getServer().getOnlinePlayers().size(), "", "&bneobymc.com.br");
                }
            }
        }, 0L, 10L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        FastBoard board = new FastBoard(player);
        String title = "&b&lNEOBYMC";
        board.updateTitle(ChatColor.translateAlternateColorCodes('&', title));
        updatePlayerScoreboard(player);
        boards.put(player.getUniqueId(), board);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        FastBoard board = boards.remove(player.getUniqueId());
        if (board != null) {
            board.delete();
        }
    }

    private void updateBoard(FastBoard board, String... lines) {
        for (int a = 0; a < lines.length; ++a) {
            lines[a] = ChatColor.translateAlternateColorCodes('&', lines[a]);
        }
        board.updateLines(lines);
    }

    private void updatePlayerScoreboard(Player player) {
        FastBoard board = boards.get(player.getUniqueId());
        if (board != null) {
            Profile profile = Core.getInstance().getProfileManager().getProfile(player);
            if (profile != null) {
                Rank rank = profile.getData().getRank();
                updateBoard(board, "", "&fRank: " + rank.getScorename(), "&fLobby: &7#1", "", "&fPlayers: &a" + Bukkit.getServer().getOnlinePlayers().size(), "", "&bneobymc.com.br");
            }
        }
    }
}