package br.com.core.Utils;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TagAPI {
    private Scoreboard scoreboard;

    public TagAPI(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public String getTagPrefix(Player player) {
        for (PermissionAttachmentInfo attachment : player.getEffectivePermissions()) {
            String tagName;
            Team team;
            String permission = attachment.getPermission();
            if (!permission.startsWith("tag.") || (team = this.scoreboard.getTeam(tagName = permission.substring("tag.".length()))) == null) continue;
            return team.getPrefix();
        }
        return "";
    }
}

