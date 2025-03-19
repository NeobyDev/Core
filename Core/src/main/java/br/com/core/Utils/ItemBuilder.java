package br.com.core.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {
    private final ItemStack stack;
    private ItemMeta itemMeta;

    public ItemBuilder(Material type, String name, Integer amount) {
        this.stack = new ItemStack(type, amount.intValue());
        this.itemMeta = this.stack.getItemMeta();
        this.itemMeta.setDisplayName(name);
    }

    public ItemBuilder(Material type, String name, Integer amount, short id) {
        this.stack = new ItemStack(type, amount.intValue(), id);
        this.itemMeta = this.stack.getItemMeta();
        this.itemMeta.setDisplayName(name);
    }

    public ItemBuilder(Material type, String name) {
        this.stack = type.equals((Object)Material.SKULL_ITEM) ? new ItemStack(type, 1, (short) 3) : new ItemStack(type);
        this.itemMeta = this.stack.getItemMeta();
        this.itemMeta.setDisplayName(name);
    }

    public ItemBuilder changeAmount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int v, boolean b) {
        this.stack.getItemMeta().addEnchant(enchantment, v, b);
        return this;
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
        this.itemMeta = stack.getItemMeta();
    }

    public ItemBuilder changeId(int i) {
        this.stack.setDurability((short)i);
        return this;
    }

    public ItemBuilder withLore(String ... lore) {
        this.itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder withLore(List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder withItemFlags(ItemFlag ... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder withSkullOwner(String owner) {
        SkullMeta meta = (SkullMeta)this.itemMeta;
        meta.setOwner(owner);
        this.itemMeta = meta;
        this.stack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemBuilder withSkullURL(String url) {
        SkullMeta skullMeta = (SkullMeta)this.itemMeta;
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put((String) "textures", (Property) new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        this.stack.setItemMeta((ItemMeta)skullMeta);
        return this;
    }

    public ItemBuilder changeName(String name) {
        this.itemMeta.setDisplayName(name);
        return this;
    }

    public ItemStack build() {
        this.stack.setItemMeta(this.itemMeta);
        return this.stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public ItemMeta getItemMeta() {
        return this.itemMeta;
    }
}

