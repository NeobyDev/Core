package br.com.core.Commands;

import br.com.core.Core;
import br.com.core.Tag;
import br.com.core.Rank;
import br.com.core.mysql.Profile;
import br.com.core.Listeners.NametagListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TagCommand implements CommandExecutor {
    private final Core plugin;

    public TagCommand() {
        this.plugin = Core.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Comando permitido somente para jogadores.");
            return true;
        }

        Player player = (Player) sender;
        Profile profile = plugin.getProfileManager().getProfile(player);

        if (profile == null || profile.getData() == null) {
            player.sendMessage(ChatColor.RED + "❌ Seu perfil ainda está carregando. Tente novamente em alguns segundos.");
            return true;
        }

        if (args.length == 0) {
            listAvailableTags(player, profile);
            return true;
        }

        String tagName = args[0];
        Tag selectedTag = getTagByName(tagName);

        if (selectedTag == null) {
            sendMessage(player, ChatColor.RED + "'" + tagName + "': Esta tag não foi encontrada ou você não a possui.");
            return true;
        }

        Set<String> availableTags = getTagsByRank(profile.getData().getRank());

        if (!availableTags.contains(selectedTag.name().toLowerCase())) {
            sendMessage(player, ChatColor.RED + "'" + tagName + "': Esta tag não foi encontrada ou você não a possui.");
            return true;
        }

        // Verifica se a tag já está equipada
        if (profile.getData().getTag() == selectedTag) {
            sendMessage(player, ChatColor.RED + "Você já está com a tag " + selectedTag.getProfilename() + ChatColor.RED + ".");
            return true;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            setTag(player, selectedTag);
            sendMessage(player, ChatColor.GREEN + "Você alterou sua tag para " + selectedTag.getProfilename() + ChatColor.GREEN + ".");
        });

        return true;
    }

    /**
     * Lista todas as tags disponíveis para o jogador.
     */
    private void listAvailableTags(Player player, Profile profile) {
        Set<String> availableTags = getTagsByRank(profile.getData().getRank());

        if (availableTags.isEmpty()) {
            sendMessage(player, ChatColor.RED + "❌ Você não tem nenhuma tag disponível.");
            return;
        }

        TextComponent message = new TextComponent(ChatColor.GREEN + "Suas tags: ");

        int count = 0;
        int total = availableTags.size(); // Quantidade total de tags disponíveis

        for (Tag tag : Tag.values()) {
            if (availableTags.contains(tag.name().toLowerCase())) {
                TextComponent tagComponent = new TextComponent(tag.getProfilename());
                tagComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.name()));
                tagComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(ChatColor.WHITE + "Exemplo: " + tag.getChatname() + player.getName() +
                                "\n\nClique para selecionar!").create()));

                message.addExtra(tagComponent);

                count++;
                if (count < total) { // Adiciona a vírgula somente se não for a última tag
                    message.addExtra(new TextComponent(ChatColor.WHITE + ", "));
                }
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> player.spigot().sendMessage(message));
    }
    /**
     * Define a tag do jogador.
     */
    private void setTag(Player player, Tag tag) {
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(scoreboard);
        }

        Team team = scoreboard.getTeam(tag.getName());
        if (team == null) {
            team = scoreboard.registerNewTeam(tag.getName());
        }

        team.setPrefix(tag.getTabname());
        team.addEntry(player.getName());

        Profile profile = plugin.getProfileManager().getProfile(player);
        if (profile != null) {
            profile.getData().setTag(tag);
            updateTagInDatabase(player, tag);
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> NametagListener.updateNametag(player), 3L);
    }

    /**
     * Atualiza a tag no banco de dados.
     */
    private void updateTagInDatabase(Player player, Tag tag) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                plugin.getMySQLManager().execute("UPDATE player SET tag=? WHERE uuid=?", tag.name(), player.getUniqueId().toString());
            } catch (Exception e) {
                sendMessage(player, "§cErro ao atualizar a tag no banco de dados.");
                e.printStackTrace();
            }
        });
    }

    /**
     * Retorna todas as tags disponíveis para um determinado rank.
     */
    private Set<String> getTagsByRank(Rank playerRank) {
        Set<String> tags = new HashSet<>();

        for (Tag tag : Tag.values()) {
            Rank tagRank = getRankByTag(tag);
            if (tagRank != null && playerRank.getLevel() >= tagRank.getLevel()) {
                tags.add(tag.name().toLowerCase());
            }
        }

        return tags;
    }

    /**
     * Obtém o Rank correspondente à Tag.
     */
    private Rank getRankByTag(Tag tag) {
        Map<Tag, Rank> tagRankMap = new HashMap<>();
        tagRankMap.put(Tag.admin, Rank.admin);
        tagRankMap.put(Tag.membro, Rank.membro);
        // Adicione mais conforme necessário

        return tagRankMap.getOrDefault(tag, null);
    }

    /**
     * Obtém uma tag pelo nome.
     */
    private Tag getTagByName(String name) {
        for (Tag tag : Tag.values()) {
            if (tag.name().equalsIgnoreCase(name)) {
                return tag;
            }
        }
        return null;
    }

    /**
     * Envia uma mensagem ao jogador na thread principal.
     */
    private void sendMessage(Player player, String message) {
        Bukkit.getScheduler().runTask(plugin, () -> player.sendMessage(message));
    }
}
