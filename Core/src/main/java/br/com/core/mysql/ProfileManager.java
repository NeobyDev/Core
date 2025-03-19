package br.com.core.mysql;

import br.com.core.Core;
import br.com.core.Manager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProfileManager extends Manager {
    private final Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(Core plugin) {
        super(plugin);
    }

    public void handleProfileCreation(UUID uuid, String name) {
        if (!profiles.containsKey(uuid)) {
            Profile profile = new Profile(uuid, name);
            profile.loadProfileData(); // Agora o método existe!
            profiles.put(uuid, profile);
        }
    }

    public Profile getProfile(Object object) {
        if (object instanceof Player) {
            return profiles.get(((Player) object).getUniqueId());
        }
        if (object instanceof UUID) {
            return profiles.get(object);
        }
        if (object instanceof String) {
            return profiles.values().stream()
                    .filter(profile -> profile.getPlayerName().equalsIgnoreCase((String) object))
                    .findFirst().orElse(null);
        }
        return null;
    }

    public void saveAllProfiles() {
        profiles.values().forEach(Profile::saveProfileData);
    }

    public void removeProfile(UUID uuid) {
        profiles.remove(uuid);
    }

    public Map<UUID, Profile> getProfiles() {
        return profiles;
    }
}
