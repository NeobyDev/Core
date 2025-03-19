package br.com.core.Utils;

import br.com.core.Core;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Files {

    private Core main;
    @Getter public File configFile;
    @Getter public FileConfiguration config;

    public Files(Core main) {
        this.main = main;
    }
    public void loadConfig() {
        if (!this.main.getDataFolder().exists()) {
            this.main.getDataFolder().mkdirs();
        }

        configFile = new File(main.getDataFolder(), "server.yml");
        if (!configFile.exists()) {
            this.main.saveResource("server.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }
}
