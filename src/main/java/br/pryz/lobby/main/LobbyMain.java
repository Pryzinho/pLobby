package br.pryz.lobby.main;

import br.pryz.lobby.commands.Quit;
import br.pryz.lobby.events.ItemEvent;
import br.pryz.lobby.events.PlayerEvent;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.easydatabase.EasyDatabaseException;
import br.pryz.lobby.utils.profile.ProfileManager;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class LobbyMain extends JavaPlugin {
    private static ProfileManager pm;
    private final CommandSender console = Bukkit.getConsoleSender();

    public static boolean sendPluginMessage(Player player, String channel, String subchannel, String... data) {
        try {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(subchannel);
            String[] stringArray = data;
            int n = data.length;
            int n2 = 0;
            while (n2 < n) {
                String dataToSend = stringArray[n2];
                out.writeUTF(dataToSend == null ? "" : dataToSend);
                ++n2;
            }
            if (player != null) {
                player.sendPluginMessage(LobbyMain.getInstance(), channel, out.toByteArray());
                return true;
            }
            player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) return false;
            player.sendPluginMessage(LobbyMain.getInstance(), channel, out.toByteArray());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static LobbyMain getInstance() {
        return getPlugin(LobbyMain.class);
    }

    public static ProfileManager getProfileManager() {
        return pm;
    }

    @Override
    public void onLoad() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Lobby.getConfig().saveDefaultConfig();
        Lobby.getLocationsYml().saveDefaultConfig();
    }

    public void onEnable() {
        //Loading Events...
        getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemEvent(), this);
        //Loading worlds...
        if (Lobby.noExists()) {
            Lobby.getConfig().reloadConfig();
            Lobby.getLocationsYml().reloadConfig();
            for (int i = 1; i <= Lobby.getNumberOfLobbys(); i++) {
                WorldCreator wc = new WorldCreator("lobby" + i);
                wc.type(WorldType.FLAT);
                wc.generateStructures(false);
                Bukkit.createWorld(wc);
                Lobby.getLocationsYml()
                        .setLocation("lobby" + i, Bukkit.getWorld("lobby" + 1).getSpawnLocation());
            }
        } else {
            for (int i = 1; i <= Lobby.getNumberOfLobbys(); i++) {
                Lobby.getLocationsYml()
                        .setLocation("lobby" + i, Bukkit.getWorld("lobby" + i).getSpawnLocation());
            }
        }

        //Loading Managers...
        pm = new ProfileManager(this, ProfileManager.StorageType.valueOf(Lobby.getConfig().getString("StorageType")));

        //Loading commands...
        getCommand("sair").setExecutor(new Quit());

        //Finish Enable
        console.sendMessage("--------------------");
        console.sendMessage(color(" &ePryz Lobby"));
        console.sendMessage(color(" &aPugin Habilitado "));
        console.sendMessage("--------------------");
        //new /* Unavailable Anonymous Inner Class!! */.run();
    }

    public void onDisable() {
        HandlerList.unregisterAll();
        console.sendMessage("--------------------");
        console.sendMessage(color(" &ePryz Lobby "));
        console.sendMessage(color(" &cPugin Desabilitado "));
        console.sendMessage("--------------------");
    }

    private String color(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}

