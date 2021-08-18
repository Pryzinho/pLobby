package br.pryz.lobby.utils.profile;

import br.pryz.lobby.main.LobbyMain;
import br.pryz.lobby.utils.PryConfig;
import br.pryz.lobby.utils.easydatabase.EasyMysql;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ProfileManager {
    private JavaPlugin plugin;
    private PryConfig ymlprofiles;
    private EasyMysql mysqlprofiles;
    private Map<UUID, Profile> profiles = new HashMap<UUID, Profile>();
    private StorageType storageType;


    public ProfileManager(JavaPlugin plugin, StorageType storageType) {
        this.plugin = plugin;
        this.storageType = storageType;
        switch (storageType) {
            case SQL:

                break;
            case YML:
			ymlprofiles = new PryConfig(plugin, "profiles.yml");
                ymlprofiles.saveDefaultConfig();
                break;

        }
    }

    public Profile createProfile(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        Profile profile = new Profile(uuid,
                (p.getName() + "@MeuEmail..."),
                "Linka, linka imediatamente O-O",
                p.getFirstPlayed(),
                System.currentTimeMillis());
        profiles.put(uuid, profile);
        return profile;
    }

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public void loadProfiles() {
        switch (storageType) {
            case SQL:
                break;
            case YML:
                for (String key : ymlprofiles.getSection("")) {
                    UUID uuid = UUID.fromString(key);
                    Profile profile = new Profile(uuid,
                            ymlprofiles.getString(uuid.toString() + ".email"),
                            ymlprofiles.getString(uuid.toString() + ".discord"),
                            ymlprofiles.getLong(uuid.toString() + ".firstPlayed"),
                            ymlprofiles.getLong(uuid.toString() + ".lastPlayed")
                    );
                    profiles.put(uuid, profile);
                }
                break;
        }
    }

    public void saveProfiles() {
        switch (storageType) {
            case SQL:
                break;
            case YML:
                for (UUID uuid : profiles.keySet()) {
                    ymlprofiles.set(uuid.toString() + ".email", profiles.get(uuid).getEmail());
                    ymlprofiles.set(uuid.toString() + ".discord", profiles.get(uuid).getDiscord());
                    ymlprofiles.set(uuid.toString() + ".firstPlayed", profiles.get(uuid).getFirstTimeOnline());
                    ymlprofiles.set(uuid.toString() + ".lastPlayed", profiles.get(uuid).getLastTimeOnline());
                    ymlprofiles.saveConfig();
                }
                break;
        }
    }

    public Map<UUID, Profile> getProfiles() {
        return profiles;
    }

}
