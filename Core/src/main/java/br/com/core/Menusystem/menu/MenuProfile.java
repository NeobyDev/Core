package br.com.core.Menusystem.menu;

import br.com.core.Core;
import br.com.core.Utils.ItemBuilder;
import br.com.core.mysql.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class MenuProfile {
    public MenuProfile(Player player) {
        Profile profile = Core.getInstance().getProfileManager().getProfile(player);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));
        ArrayList<String> Lore_Lobby = new ArrayList<String>();
        Lore_Lobby.add("§7Rank: " + profile.getData().getRank().getProfilename());
        Lore_Lobby.add("§7Primeiro login: " + dateFormat.format(player.getFirstPlayed()));
        Lore_Lobby.add("§7Último login: " + dateFormat.format(player.getLastPlayed()));
        Inventory inventory = Bukkit.createInventory(null, (int)45, (String)"Meu Perfil");
        inventory.setItem(13, new ItemBuilder(Material.SKULL_ITEM, "§aSuas Informações").withLore(Lore_Lobby).withSkullOwner(player.getName()).build());
        player.openInventory(inventory);
    }
}

