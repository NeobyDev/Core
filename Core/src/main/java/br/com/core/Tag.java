package br.com.core;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public enum Tag {
    admin("Admin", "§4Admin", "§4§lADMIN§r§4 ", "§4§lADMIN§r§4 ", ChatColor.DARK_RED, true, false, 2),
    membro("Membro", "§7Membro", ChatColor.GRAY + "", ChatColor.GRAY + "", ChatColor.GRAY, false, false, 1);

    private static final Map<String, Tag> TAG_MAP = new HashMap<>();

    static {
        for (Tag tag : values()) {
            TAG_MAP.put(tag.name().toLowerCase(), tag);
        }
    }

    private String name;
    private String profilename;
    private String chatname;
    private String tabname;
    private ChatColor color;
    private boolean bolded;
    private boolean italicized;
    private int level;

    private Tag(String name, String profilename, String chatname, String tabname, ChatColor color, boolean bolded, boolean italicized, int level) {
        this.name = name;
        this.profilename = profilename;
        this.chatname = chatname;
        this.tabname = tabname;
        this.color = color;
        this.bolded = bolded;
        this.italicized = italicized;
        this.level = level;
    }

    public boolean isHigherOrEqualsTo(Player player, Tag tag, boolean callback) {
        if (callback && this.level < tag.level) {
            player.sendMessage("§cVocê não possui permissão para usar este comando.");
            return false;
        }
        return this.level >= tag.level;
    }

    public static boolean contains(String rank) {
        return TAG_MAP.containsKey(rank.toLowerCase());
    }

    public String getNameHigher() {
        return this.name.toUpperCase();
    }

    public static String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    public String getPrefix() {
        String nameFormatted = this.name.toUpperCase();
        if (this.bolded && this.italicized) {
            return translate(this.color + "&o&l" + nameFormatted);
        }
        if (this.bolded) {
            return translate(this.color + "&l" + nameFormatted);
        }
        if (this.italicized) {
            return translate(this.color + "&o" + nameFormatted);
        }
        return translate(this.color + nameFormatted);
    }

    public String getName() {
        return this.name;
    }

    public String getProfilename() {
        return this.profilename;
    }

    public String getChatname() {
        return this.chatname;
    }

    public String getTabname() {
        return translate(this.tabname);
    }

    public ChatColor getColor() {
        return this.color;
    }

    public boolean isBolded() {
        return this.bolded;
    }

    public boolean isItalicized() {
        return this.italicized;
    }

    public int getLevel() {
        return this.level;
    }
}