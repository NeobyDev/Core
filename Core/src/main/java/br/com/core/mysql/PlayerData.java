package br.com.core.mysql;

import br.com.core.Core;
import br.com.core.Rank;
import br.com.core.Tag;
import br.com.core.Visibility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.UUID;

public class PlayerData {
    private final Core plugin = Core.getInstance();
    private final UUID UUID;
    private final String playerName;
    private Rank rank = Rank.membro;
    private Tag tag = Tag.membro;
    private Visibility visibility = Visibility.ativado; // Define um valor padrão seguro

    public PlayerData(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
    }

    public void load(Player player) {
        Core.getInstance().getMySQLManager().select("SELECT * FROM player WHERE uuid=?", resultSet -> {
            try {
                if (resultSet.next()) {
                    this.rank = Rank.valueOf(resultSet.getString("rank"));
                    this.tag = Tag.valueOf(resultSet.getString("tag"));

                    // Adicionando verificação para evitar erros
                    String visStr = resultSet.getString("visibility");
                    if (visStr != null) {
                        this.visibility = Visibility.valueOf(visStr);
                    } else {
                        this.visibility = Visibility.ativado; // Valor padrão se for nulo no banco
                    }

                    Bukkit.getConsoleSender().sendMessage("✅ Dados carregados para " + player.getName() + ": Visibility = " + this.visibility);
                } else {
                    // Insere novo jogador caso ele não exista
                    Core.getInstance().getMySQLManager().execute(
                            "INSERT INTO player(uuid, name, rank, tag, visibility) VALUES (?,?,?,?,?);",
                            player.getUniqueId().toString(),
                            player.getName(),
                            Rank.membro.toString(),
                            Tag.membro.toString(),
                            Visibility.ativado.toString()
                    );
                }
            } catch (SQLException e) {
                Bukkit.getConsoleSender().sendMessage("❌ Erro ao carregar dados do jogador: " + e.getMessage());
            }
        }, player.getUniqueId().toString());
    }

    public void loadProfileData() {
        Core.getInstance().getMySQLManager().select(
                "SELECT * FROM player WHERE uuid=?",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            this.rank = Rank.valueOf(resultSet.getString("rank"));
                            this.tag = Tag.valueOf(resultSet.getString("tag"));

                            // Mantém visibility sem sobrescrever se já tiver um valor salvo corretamente
                            String visStr = resultSet.getString("visibility");
                            if (visStr != null && !this.visibility.toString().equalsIgnoreCase(visStr)) {
                                this.visibility = Visibility.valueOf(visStr);
                            }

                            Bukkit.getConsoleSender().sendMessage("✅ Perfil carregado para " + this.playerName + ": Visibility = " + this.visibility);
                        } else {
                            Core.getInstance().getMySQLManager().execute(
                                    "INSERT INTO player(uuid, name, rank, tag, visibility) VALUES (?,?,?,?,?);",
                                    this.UUID.toString(),
                                    this.playerName,
                                    Rank.membro.toString(),
                                    Tag.membro.toString(),
                                    Visibility.ativado.toString()
                            );
                        }
                    } catch (SQLException e) {
                        Bukkit.getConsoleSender().sendMessage("❌ Erro ao carregar perfil do jogador: " + e.getMessage());
                    }
                }, this.UUID.toString()
        );
    }

    public void save(Player player) {
        Core.getInstance().getMySQLManager().execute(
                "UPDATE player SET tag=?, rank=?, visibility=? WHERE uuid=?",
                this.tag.toString(),
                this.rank.toString(),
                this.visibility.toString(),
                player.getUniqueId().toString()
        );
        Bukkit.getConsoleSender().sendMessage("💾 Dados salvos para " + player.getName() + ": Visibility = " + this.visibility);
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Rank getRank() {
        return this.rank;
    }

    public Tag getTag() {
        return this.tag;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}