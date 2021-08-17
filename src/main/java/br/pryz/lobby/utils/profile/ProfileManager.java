package br.pryz.lobby.utils.profile;

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
            default:

                break;
        }
    }

    public Profile createProfile(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        Profile profile = new Profile(uuid,
                "Ugh, e-mail?",
                "MEU, LINKA ISSO LOGO",
                p.getFirstPlayed(),
                System.currentTimeMillis());
        profiles.put(uuid, profile);
        return profile;
    }

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public void loadProfiles() {

    }

    public void saveProfiles() {
        switch (storageType) {
            case SQL:

                break;
            case YML:
               for (UUID uuid: profiles.keySet()){
                   ymlprofiles.set(String.valueOf(uuid) + ".email", profiles.get(uuid).getEmail());
                   ymlprofiles.set(String.valueOf(uuid) + ".discord", profiles.get(uuid).getDiscord());
                   ymlprofiles.set(String.valueOf(uuid) + ".firstPlayed", profiles.get(uuid).getFirstTimeOnline());
                   ymlprofiles.set(String.valueOf(uuid) + ".lastPlayed", profiles.get(uuid).getLastTimeOnline());
                   ymlprofiles.saveConfig();
               }
                break;
        }
    }

    public Map<UUID, Profile> getProfiles() {
        return profiles;
    }

    public enum StorageType {
        SQL,
        YML
    }
}
