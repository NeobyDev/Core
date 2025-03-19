package br.com.core.Menusystem.menu;

import br.com.core.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;

public class ServerSelectorMenu {

    public ServerSelectorMenu(Player player) {
        ArrayList<String> Lore_Bedwars = new ArrayList<>();
        ArrayList<String> Lore_Skywars = new ArrayList<>();
        Lore_Bedwars.add(ChatColor.GRAY + "" + "0 jogando agora!");
        Lore_Skywars.add(ChatColor.GRAY + "" + "0 jogando agora!");
        Inventory inventory = Bukkit.createInventory(null, 27, "Selecionar Jogo");
        inventory.setItem(10, new ItemBuilder(Material.BED, "§aBed Wars").withLore(Lore_Bedwars).build());
        inventory.setItem(11, new ItemBuilder(Material.ENDER_PEARL, "§aSky Wars").withLore(Lore_Skywars).build());
        player.openInventory(inventory);
    }
}

