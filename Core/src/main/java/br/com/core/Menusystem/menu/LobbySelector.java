package br.com.core.Menusystem.menu;

import br.com.core.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import java.util.ArrayList;

public class LobbySelector {

    public LobbySelector(Player player) {
        ArrayList<String> Lore_Lobby = new ArrayList<>();
        Lore_Lobby.add("§7Jogadores: " + Bukkit.getOnlinePlayers().size() + "/60");
        Lore_Lobby.add("");
        Lore_Lobby.add("§eClique para conectar!");
        Inventory inventory = Bukkit.createInventory(null, 27, "Selecionar Lobby (1/1)");
        inventory.setItem(10, new ItemBuilder(Material.STAINED_GLASS_PANE, "§aLobby #1").changeId(5).withLore(Lore_Lobby).build());
        player.openInventory(inventory);
    }
}

