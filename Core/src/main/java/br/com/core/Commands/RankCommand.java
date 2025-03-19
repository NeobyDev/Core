package br.com.core.Commands;

import br.com.core.Core;
import br.com.core.Listeners.NametagListener;
import br.com.core.Rank;
import br.com.core.Tag;
import br.com.core.mysql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DynamicCommand(name="rank", console=true)
public class RankCommand extends Command {
    Core plugin = Core.getInstance();

    @Override
    public void execute(CommandSender sender, String... args) {
        // Verifica se o sender é um jogador e tem permissão para alterar ranks
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Profile profile = plugin.getProfileManager().getProfile(player);

            if (profile == null || !profile.getData().getRank().isHigherOrEqualsTo(player, Rank.admin, true)) {
                sender.sendMessage("§cVocê não tem permissão para usar esse comando.");
                return;
            }
        }

        // Verifica se o número de argumentos está correto
        if (args.length < 2) {
            sender.sendMessage("§cUso correto: /rank <Jogador> <Cargo>");
            return;
        }

        String targetName = args[0];
        String rankName = args[1];

        // Verifica se o rank informado é válido
        if (!Rank.contains(rankName)) {
            sender.sendMessage("§cEsse cargo não existe.");
            return;
        }

        // Obtém o jogador alvo
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage("§cJogador não encontrado.");
            return;
        }

        // Atualiza o rank e a tag no banco de dados
        try {
            Rank rank = Rank.valueOf(rankName);
            Tag tag = Tag.valueOf(rankName.toLowerCase()); // Supondo que a tag corresponda ao nome do rank em minúsculas

            plugin.getMySQLManager().execute("UPDATE player SET rank=?, tag=? WHERE UUID=?", rankName, tag.toString(), target.getUniqueId().toString());
        } catch (Exception e) {
            sender.sendMessage("§cOcorreu um erro ao atualizar o banco de dados.");
            e.printStackTrace(); // Log para debug
            return;
        }

        // Atualiza o perfil do jogador caso esteja online
        Profile targetProfile = plugin.getProfileManager().getProfile(target);
        if (targetProfile != null) {
            targetProfile.getData().setRank(Rank.valueOf(rankName));
            targetProfile.getData().setTag(Tag.valueOf(rankName.toLowerCase())); // Atualiza a tag do jogador

            // Se o jogador estiver online, atualiza a nametag e avisa sobre a mudança de rank
            if (target.isOnline() && target.getPlayer() != null) {
                Player onlinePlayer = target.getPlayer();

                // Atualiza a nametag no TAB e acima da cabeça do jogador
                NametagListener.updateNametag(onlinePlayer);

                // Mensagem para o jogador informando a mudança
                onlinePlayer.sendMessage("§aSeu cargo foi atualizado para " + targetProfile.getData().getRank().getProfilename() + "§a.");
            }
        }

        // Mensagem de confirmação para o executor do comando
        sender.sendMessage("§aVocê atualizou o cargo do jogador §b" + target.getName() + " §apara " + rankName + "§a.");
    }
}