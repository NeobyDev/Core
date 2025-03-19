package br.com.core.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Configs {
    private JavaPlugin plugin;
    private String name;
    private static File file;
    private static FileConfiguration config;

    public String getName() {
        return this.name;
    }

    public Configs setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        return this;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public Configs(String name, JavaPlugin plugin) {
        this.plugin = plugin;
        if (plugin == null) {
            this.plugin = JavaPlugin.getProvidingPlugin(this.getClass());
        }
        this.name = name;
        this.reloadConfig();
    }

    public Configs(String name) {
        this(name, null);
    }

    public Configs reloadConfig() {
        file = new File(this.plugin.getDataFolder(), this.name);
        config = YamlConfiguration.loadConfiguration((File)file);
        InputStream defaults = this.plugin.getResource(file.getName());
        return null;
    }

    public Configs saveConfig() {
        try {
            config.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return this;
    }

    public String message(String path) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)this.getConfig().getString(path));
    }

    public Configs saveDefaultConfig() {
        if (this.plugin.getResource(this.name) != null) {
            this.plugin.saveResource(this.name, false);
        }
        return this;
    }

    public void remove(String path) {
        config.set(path, null);
    }

    public Configs saveDefault() {
        config.options().copyDefaults(true);
        this.saveConfig();
        return this;
    }

    public void setItem(String path, ItemStack item) {
        Configs.setItem(Configs.create(path), item);
    }

    public ItemStack getItem(String path) {
        return Configs.getItem(Configs.getSection(path));
    }

    public static void setLocation(String path, Location location) {
        Configs.setLocation(Configs.create(path), location);
    }

    public static Location getLocation(String path) {
        return Configs.getLocation(Configs.getSection(path));
    }

    public static void setItem(ConfigurationSection section, ItemStack item) {
        section.set("id", (Object)item.getTypeId());
        section.set("data", (Object)item.getDurability());
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                section.set("name", (Object)meta.getDisplayName());
            }
            if (meta.hasLore()) {
                ArrayList<String> lines = new ArrayList<String>();
                for (String line : meta.getLore()) {
                    lines.add(line);
                }
                section.set("lore", lines);
            }
        }
        StringBuilder text = new StringBuilder();
        for (Map.Entry enchant : item.getEnchantments().entrySet()) {
            text.append(((Enchantment)enchant.getKey()).getId() + "-" + enchant.getValue() + ",");
        }
        section.set("enchant", (Object)text.toString());
    }

    public static void setLocation(ConfigurationSection section, Location location) {
        section.set("world", (Object)location.getWorld().getName());
        section.set("x", (Object)location.getX());
        section.set("y", (Object)location.getY());
        section.set("z", (Object)location.getZ());
        section.set("yaw", (Object)Float.valueOf(location.getYaw()));
        section.set("pitch", (Object)Float.valueOf(location.getPitch()));
    }

    public static Location getLocation(ConfigurationSection section) {
        World world = Bukkit.getWorld((String)section.getString("world"));
        double x = section.getDouble("x");
        double y = section.getDouble("y");
        double z = section.getDouble("z");
        float yaw = (float)section.getDouble("yaw");
        float pitch = (float)section.getDouble("pitch");
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static Location toLocation(String text) {
        String[] split = text.split(",");
        World world = Bukkit.getWorld((String)split[0]);
        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[2]);
        double z = Double.parseDouble(split[3]);
        float yaw = Float.parseFloat(split[4]);
        float pitch = Float.parseFloat(split[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String toChatMessage(String text) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)text);
    }

    public static String saveLocation(Location location) {
        StringBuilder text = new StringBuilder();
        text.append(location.getWorld().getName() + ",");
        text.append(location.getX() + ",");
        text.append(location.getY() + ",");
        text.append(location.getZ() + ",");
        text.append(location.getYaw() + ",");
        text.append(location.getPitch());
        return text.toString();
    }

    public static String toConfigMessage(String text) {
        return text.replace("\u00a7", "&");
    }

    public static ItemStack getItem(ConfigurationSection section) {
        ItemStack item = new ItemStack(section.getInt("id"), section.getInt("data"));
        ItemMeta meta = item.getItemMeta();
        if (section.contains("name")) {
            meta.setDisplayName(Configs.toChatMessage(section.getString("name")));
        }
        if (section.contains("lore")) {
            ArrayList<String> lines = new ArrayList<String>();
            for (String line : meta.getLore()) {
                lines.add(Configs.toChatMessage(line));
            }
        }
        if (section.contains("enchant")) {
            for (String value : section.getString("enchant").split(",")) {
                if (value == null || value.isEmpty() || !value.contains("-")) continue;
                String[] split = value.split("-");
                item.addUnsafeEnchantment(Enchantment.getById((int)Integer.valueOf(split[0])), Integer.valueOf(split[1]).intValue());
            }
        }
        return item;
    }

    public boolean delete() {
        return file.delete();
    }

    public boolean exists() {
        return file.exists();
    }

    public void add(String path, Object value) {
        config.addDefault(path, value);
    }

    public static boolean contains(String path) {
        return config.contains(path);
    }

    public static ConfigurationSection create(String path) {
        return config.createSection(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public static ConfigurationSection getSection(String path) {
        return config.getConfigurationSection(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<Integer> getIntegerList(String path) {
        return config.getIntegerList(path);
    }

    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }

    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    public List<Map<?, ?>> getMapList(String path) {
        return config.getMapList(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public Map<String, Object> getValues(boolean deep) {
        return config.getValues(deep);
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }
}

