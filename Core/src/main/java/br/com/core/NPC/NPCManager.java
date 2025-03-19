package br.com.core.NPC;

import br.com.core.Core;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.github.leonardosnt.bungeechannelapi.BungeeChannelApi;
import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.events.NPCInteractEvent;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import net.jitse.npclib.api.skin.Skin;
import net.jitse.npclib.api.state.NPCSlot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.Arrays;

import static org.bukkit.Bukkit.*;

public class NPCManager implements Listener {

    private Core main;
    public static NPCLib npcLib;

    private NPC bedwarsLobbyNPC;
    private NPC skywarsLobbyNPC;

    public NPCManager(Core main) {
        this.main = main;
        this.npcLib = new NPCLib(main);
        load();
        getPluginManager().registerEvents(this, main);
    }

    private void load() {
        Skin skinSW = new Skin("ewogICJ0aW1lc3RhbXAiIDogMTc0MjI0NzM2Mjc3NiwKICAicHJvZmlsZUlkIiA6ICI1ODljYzAyZDg2MjE0NDdjYmQxZjAwOTEyMDkwMjM3NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZW9ieSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80ZDUzMGEyMWYwMDk4Y2M5OGFlZDdhOGM1YjJkYWMyNGUzNTljNzRkOWExOThkZDk2ZGY2OTRmODVjZTM2YmZhIgogICAgfSwKICAgICJDQVBFIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81NjliN2YyYTFkMDBkMjZmMzBlZmUzZjlhYjlhYzgxN2IxZTZkMzVmNGYzY2ZiMDMyNGVmMmQzMjgyMjNkMzUwIgogICAgfQogIH0KfQ==", "W/Et8VfFMla/C7mLAq43boqiBhqLXxBVLvvUNeYSMvpaiZpUstmrj+fRFqv/09WzVOSCmr30qnzPsfhZ5lRwPwdNYn2YuAqoII+EwkcNzFsYqd23oE8opppO0VMCoxrslLqIjOSsu58/IHx1JDXbArhH+QCwQrygdeiuNt95bbUsaxcNAw3o/LTf2a4/TyK6ZRGJ/Vob5dt+14LoMIu9Zb/FWLBCAGunJcqpwoX+IkBt9DNZU0BYO1R3brYUKEfQoPtITpXBzKD3qSNu41eC1Pj0rS9hzOO/FQXN+/v5TktRLLwlGXiw4/J8Z+/Gb0O8WALgMSTPH7ayRBo4fx/9T4vUI12o82YCvnWToVnQeslImUGckOwunE/dm6cAcSwr/7MoXtxHxkvIhYtqdw55v+LwqBIs0DzHOMk/PdL2ziRAFQ2iG9DjwAhbn/+Qz79Vk5d/Yzs1kzNSw1AIhKNjlA6lV3uEQN2PtCae4Gwa/gyNo0tkTsu1pR8UwlNTwQ9/BsMHPfxtiFB+9WRQTb3p0OWPDkAfZf5JqFopZQojbMUCfgvc3E9mXRE1EWhdx7Idnn85wwiySTczNpaUCxCKF3UKjRNDrsW9QKNOKcTStksNWVzS+F7zQhA4RQhyqiJDkpBz4aY7UKeRazS5BJBj0880bAdcySWQPOfo23PTDu0=");
        Skin skinBW = new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyODE5ODUzMzM4MiwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg5NjJhMGNiZjdiMWI5NDQ4MjU5YjEzYTBkMmM3Y2E1OTZiMmU2NTdlZWQwZjc1MGYxY2ViZTQ4MWNjN2I5MTEiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==","MdAteUKFZFa9YcAZTpztqK4x/uHPmRFcdQdGX5E3XQW57SQy4P2wxReZOgy6O3/lDYcVOdUZOvE7yw1v/WI7JW1alTgxH5LjoBwXDsGHZTUFMdEfXrrv1eRmJQ6p44IEnRnvKtIXUp4qoDtCHY7yv1BrDXRJ07MFI+qPeU1mT8rVT7w8so5FThfcQEPPd8KrvhIBA+HISmxc7Lfm8qwO4V2MGWu95zFRdAiH1R90fzBphecVHxtmsdpsuxT4r+/KlP/lTCDQxuVHXWEErZUT71rbX18mlIwPTdV4oUiQeBKEns5XzMEABjQl9Y10R9GGwNuXM0TEyyc9G66lqJKcmH8yhjhX6fCeIatUWlI3FivIEA2hJ2F3tNLV/jlzL4SXtlXCDHNpsggHCZDSMJ1PkfMax3b8JMBfCHHZ/LOTIBujyMCrHjjndponVjvTkwXL8YAdupj02CUYRIBDUrkvkmH9rJ7Zqa+7WdX8FnwHfELaB3l+l5An6tn2fG8WooTHC9ZqEf62b6HUfRSBH9VsAgD1DIHGGHqJWM4ID8R54asRZmCSOLccgYDOuv7IhDPMEfE9ArKhIlmCRKmStOyKqYckNKVGWGJpoOrfABZ8/9sWNalOX2abybuw+Bh0Qcbk9+/cSIpO0vw2VHXV1wF5q0f1rScR5hQg8W4+KDwVUw0=");
            bedwarsLobbyNPC = npcLib.createNPC(Arrays.asList(ChatColor.AQUA + "Bed Wars", ChatColor.YELLOW + "" + "... jogando"));
            bedwarsLobbyNPC.setLocation(new Location(getWorlds().get(0), 1337.500, 41.00000, 228.500, 172, 7));
            bedwarsLobbyNPC.setItem(NPCSlot.MAINHAND, new ItemStack(Material.BED));
            bedwarsLobbyNPC.setSkin(skinBW);
            bedwarsLobbyNPC.create();
            skywarsLobbyNPC = npcLib.createNPC(Arrays.asList(ChatColor.AQUA + "Sky Wars", ChatColor.YELLOW + "... jogando"));
            skywarsLobbyNPC.setLocation(new Location(getWorlds().get(0), 1331.500, 41.00000, 228.500, -172, 7));
            skywarsLobbyNPC.setItem(NPCSlot.MAINHAND, new ItemStack(Material.ENDER_PEARL));
            skywarsLobbyNPC.setSkin(skinSW);
            skywarsLobbyNPC.create();
    }

    private void sendServer(Player player, String server) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.sendPluginMessage(Core.getInstance(), "BungeeCord", byteArrayOutputStream.toByteArray());
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        Player player = event.getWhoClicked();
        if(event.getNPC() == skywarsLobbyNPC) {
            sendServer(player, "SkyWars");
        }
        if(event.getNPC() == bedwarsLobbyNPC) {
            sendServer(player, "BedWars");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        getServer().getScheduler().scheduleSyncDelayedTask(Core.getPlugin(), () -> {
            getScheduler().runTask(Core.getPlugin(), () -> bedwarsLobbyNPC.show(event.getPlayer()));
            getScheduler().runTask(Core.getPlugin(), () -> skywarsLobbyNPC.show(event.getPlayer()));
        }, 5L);
    }
}