package br.com.core.mysql;

import br.com.core.Core;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MySQLManager {
    private final Core plugin;
    private HikariDataSource hikari;

    public MySQLManager(Core plugin) {
        this.plugin = plugin;
        setupDatabase();
    }

    private void setupDatabase() {
        Bukkit.getLogger().info("🔄 Conectando ao MySQL...");

        String host = plugin.getConfig().getString("database.host");
        int port = plugin.getConfig().getInt("database.port");
        String database = plugin.getConfig().getString("database.database");
        String username = plugin.getConfig().getString("database.username");
        String password = plugin.getConfig().getString("database.password");
        int poolSize = plugin.getConfig().getInt("database.pool-size");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true");
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setMaxLifetime(60000);
        config.setConnectionTimeout(10000);

        hikari = new HikariDataSource(config);
        Bukkit.getLogger().info("✅ Conectado ao MySQL com sucesso!");

        createTables();
    }

    private void createTables() {
        executeUpdate("CREATE TABLE IF NOT EXISTS players (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "name VARCHAR(16) NOT NULL, " +
                "rank VARCHAR(20) NOT NULL DEFAULT 'membro', " +
                "tag VARCHAR(20) NOT NULL DEFAULT 'membro', " +
                "visibility VARCHAR(20) NOT NULL DEFAULT 'ativado')");
    }

    public void shutdown() {
        if (hikari != null && !hikari.isClosed()) {
            hikari.close();
            Bukkit.getLogger().info("🛑 Conexão com MySQL fechada.");
        }
    }

    public CompletableFuture<Void> executeUpdate(String query, Object... values) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = hikari.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }
                stmt.executeUpdate();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("❌ Erro ao executar query: " + query);
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<ResultSet> executeQuery(String query, Object... values) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection conn = hikari.getConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                for (int i = 0; i < values.length; i++) {
                    stmt.setObject(i + 1, values[i]);
                }
                return stmt.executeQuery();
            } catch (SQLException e) {
                Bukkit.getLogger().severe("❌ Erro ao executar consulta: " + query);
                e.printStackTrace();
                return null;
            }
        });
    }
}
