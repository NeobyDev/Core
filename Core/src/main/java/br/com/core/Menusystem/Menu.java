package br.com.core.Menusystem;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
public abstract class Menu implements InventoryHolder {

    protected PlayerMenuUtil playerMenuUtil;
    protected Inventory inventory;
    protected ItemStack FILLER_GLASS = makeItem(Material.STAINED_GLASS_PANE, "");

    public Menu(PlayerMenuUtil playerMenuUtil) throws InstantiationException, IllegalAccessException {
        this.playerMenuUtil = playerMenuUtil;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract void handleMenu(InventoryClickEvent event);
    public abstract void setMenuItems(Player p);

    public void open(Player p) {
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems(p);

        playerMenuUtil.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void setFillerCompass() {
        for (int i = 0; i < getSlots(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, FILLER_GLASS);
            }
        }
    }

    public ItemStack makeItem(Material material, String displayname, String... lore) {
        ItemStack itemStack = new ItemStack(material);

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayname);
        itemMeta.setLore(Arrays.asList(lore));

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
