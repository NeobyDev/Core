package br.com.core.Utils;

import br.com.core.Core;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class Items {

    public ItemStack Item_Compass = new ItemStack(Material.COMPASS, 1);

    public ItemStack Item_Lobby = new ItemStack(Material.NETHER_STAR, 1);

    ItemMeta Meta_Compass = (ItemMeta) Item_Compass.getItemMeta();
    ItemMeta Meta_Lobby = (ItemMeta) Item_Lobby.getItemMeta();

    ArrayList<String> Lore_Compass = new ArrayList<>();
    ArrayList<String> Lore_Lobby = new ArrayList<>();

    private Core main;

    public Items() {
        this.main = main;
    }

    public void ServerSelector(Player p){
        Lore_Compass.clear();
        Lore_Compass.add(ChatColor.GRAY + "Abra o menu de jogos!");
        Meta_Compass.setLore(Lore_Compass);
        Meta_Compass.setDisplayName(ChatColor.GREEN + "Selecionar jogo");
        Item_Compass.setItemMeta(Meta_Compass);
    }
    public void Lobby(Player p){
        Lore_Lobby.clear();
        Lore_Lobby.add(ChatColor.GRAY + "Escolha um outro lobby!");
        Meta_Lobby.setLore(Lore_Lobby);
        Meta_Lobby.setDisplayName(ChatColor.GREEN + "Selecionar lobby");
        Item_Lobby.setItemMeta(Meta_Lobby);
    }

}
