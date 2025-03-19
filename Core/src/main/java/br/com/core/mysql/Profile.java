package br.com.core.mysql;

import br.com.core.Core;

import java.util.UUID;

public class Profile {
    private Core plugin = Core.getInstance();
    private PlayerData data;
    private UUID UUID;
    private String playerName;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new PlayerData(uuid, name);
    }

    /**
     * Carrega os dados do jogador do banco de dados.
     */
    public void loadProfileData() {
        data.load(); // Chama o método correto para carregar os dados
    }

    /**
     * Salva os dados do jogador no banco de dados.
     */
    public void saveProfileData() {
        data.save();
    }

    public Core getPlugin() {
        return this.plugin;
    }

    public PlayerData getData() {
        return this.data;
    }

    public UUID getUUID() {
        return this.UUID;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlugin(Core plugin) {
        this.plugin = plugin;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }

    public void setUUID(UUID UUID2) {
        this.UUID = UUID2;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
