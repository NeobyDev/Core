package br.com.core;

import br.com.core.Commands.*;
import br.com.core.Listeners.*;
import br.com.core.NPC.NPCManager;
import br.com.core.Scoreboard.Scoreboard;
import br.com.core.Utils.*;
import br.com.core.mysql.MySQLManager;
import br.com.core.mysql.Profile;
import br.com.core.mysql.ProfileManager;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public final class Core extends JavaPlugin implements Listener {
    private static NPCManager npcManager;
    private TagAPI tagAPI;
    public static Configs config;
    public static Core instance;
    public MySQLManager MySQLManager;
    public ProfileManager profileManager;
    public Files files;
    public static ArrayList<Player> build;
    public static ArrayList<Player> fly;

    public static Core getPlugin() {
        return (Core) JavaPlugin.getPlugin(Core.class);
    }

    public void onEnable() {
        instance = this;
        this.MySQLManager = new MySQLManager(this);
        this.profileManager = new ProfileManager(this);
        this.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
        new CommandLib().setupBukkit((Plugin) this).register(new RankCommand());
        this.files = new Files(this);
        this.files.loadConfig();
        this.initiateManagers();
        this.registerEvents();
        config = new Configs("config.yml");
        PluginCommand BuildCommand2 = this.getCommand("build");
        BuildCommand2.setExecutor((CommandExecutor) new BuildCommand());
        BuildCommand2.setPermission("build.use");
        BuildCommand2.setPermissionMessage("§cVocê não possui permissão para usar este comando.");
        BuildCommand2.setDescription("Habilitar ou desabilitar o build");
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents((Listener) new BuildListener(), (Plugin) this);
        PluginCommand FlyCommand2 = this.getCommand("fly");
        FlyCommand2.setExecutor((CommandExecutor) new FlyCommand(this));
        FlyCommand2.setPermission("fly.use");
        FlyCommand2.setPermissionMessage("§cVocê não tem acesso a este recurso.\n §cAdquira Ranks para voar nos lobbies.");
        FlyCommand2.setDescription("Habilitar ou desabilitar o voo");
        npcManager = new NPCManager(this);
        pm.registerEvents((Listener) this, (Plugin) this);
        this.getServer().getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
        new BungeeChannelApi((Plugin) this);
        this.setupScoreboard();
        getServer().getScheduler().runTaskLater(this, () -> {
            for (World world : Bukkit.getWorlds()) {
                world.setStorm(false);
                world.setThundering(false);

                for (Entity entity : world.getEntities()) {
                    if (entity.getType() != EntityType.PLAYER) {
                        entity.remove();
                    }
                }
            }
        }, 5L);
    }

    public void onDisable() {
        this.getMySQLManager().shutdown();
    }

    @EventHandler
    public void onProfileLoad(AsyncPlayerPreLoginEvent event) {
        try {
            this.getProfileManager().handleProfileCreation(event.getUniqueId(), event.getName());
            Profile profile = this.getProfileManager().getProfile(event.getUniqueId());
            if (profile != null) {
                profile.getData().loadProfileData();
            }
        } catch (NullPointerException e) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "§cO servidor não conseguiu carregar seu perfil.");
        }
    }

    @EventHandler
    public void saveData(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.getProfileManager().getProfile(player);
        try {
            Bukkit.getScheduler().runTaskAsynchronously((Plugin) this, () -> {
                if (profile != null) {
                    profile.getData().save(player);
                }
            });
        } catch (NullPointerException e) {
            player.kickPlayer("§cO servidor não conseguiu carregar seu perfil.");
        }
    }

    private void setupScoreboard() {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        if (scoreboardManager == null) {
            getLogger().severe("❌ ScoreboardManager não foi inicializado!");
            return;
        }

        org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
        getCommand("tag").setExecutor(new TagCommand());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileManager.getProfile(player);

        if (profile == null) {
            getLogger().severe("❌ Erro: perfil de " + player.getName() + " é NULL após login!");
            player.kickPlayer("§cErro ao carregar seu perfil!");
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> profile.getData().load(player));

        try {
            org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam("damn");

            if (team == null) {
                team = scoreboard.registerNewTeam("damn");
            }

            if (!team.hasEntry(player.getName())) {
                team.addEntry(player.getName());
            }
        } catch (Exception e) {
            getLogger().severe("❌ Erro ao adicionar " + player.getName() + " à scoreboard: " + e.getMessage());
        }
    }

    private void initiateManagers() {
        new CommandManager(this);
    }

    private void registerEvents() {
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        this.getServer().getPluginCommand("lobby").setExecutor((CommandExecutor) new LobbyCommand());
        this.getServer().getPluginCommand("l").setExecutor((CommandExecutor) new LobbyCommand());
        this.getServer().getPluginCommand("hub").setExecutor((CommandExecutor) new LobbyCommand());
        this.getServer().getPluginManager().registerEvents((Listener) new PlayerListeners(this), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new MenuListenerLobby(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new NametagListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new MenuListenerSelector(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new ChatListener(), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new JoinListener(this, scoreboard), (Plugin) this);
        this.getServer().getPluginManager().registerEvents((Listener) new PlayerInteractListener(this), (Plugin) this);
        this.tagAPI = new TagAPI(scoreboard);
        new Scoreboard(this, this.tagAPI);
    }

    public static Core getInstance() {
        return instance;
    }

    @EventHandler
    public void event(PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (Configs.contains("spawn")) {
            e.setRespawnLocation(Configs.getLocation("spawn"));
            new BukkitRunnable() {
                public void run() {
                    p.getLocation();
                }
            };
        }
    }

    public MySQLManager getMySQLManager() {
        return this.MySQLManager;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public Files getFiles() {
        return this.files;
    }

    static {
        build = new ArrayList<>();
        fly = new ArrayList<>();
    }
}