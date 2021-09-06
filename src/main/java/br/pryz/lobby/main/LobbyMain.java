package br.pryz.lobby.main;

import br.pryz.lobby.commands.Quit;
import br.pryz.lobby.events.ItemEvent;
import br.pryz.lobby.events.PlayerEvent;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.Logger;
import br.pryz.lobby.utils.profile.ProfileManager;
import br.pryz.lobby.utils.profile.StorageType;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class LobbyMain extends JavaPlugin {
    private ProfileManager pm;
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    public static boolean sendPluginMessage(JavaPlugin pl, Player player, String channel, String subchannel, String... data) {
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
                player.sendPluginMessage(pl, channel, out.toByteArray());
                return true;
            }
            player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            if (player == null) return false;
            player.sendPluginMessage(pl, channel, out.toByteArray());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public static JavaPlugin getInstance() {
        return getPlugin(LobbyMain.class);
    }

    public ProfileManager getProfileManager() {
        return pm;
    }

    public void onEnable() {
        Logger.log(ccs, "&f[&dp&aLobby&f]&a Iniciando configuração...");
        pm = new ProfileManager(this, StorageType.YML);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Lobby.getConfig().saveDefaultConfig();
        Lobby.getLocationsYml().saveDefaultConfig();
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
                Logger.log(ccs, "&f[&dp&aLobby&f]&a Criando o mundo &e&lLobby" + i);
            }
        } else {
            for (int i = 1; i <= Lobby.getNumberOfLobbys(); i++) {
                Lobby.getLocationsYml()
                        .setLocation("lobby" + i, Bukkit.getWorld("lobby" + i).getSpawnLocation());
                Logger.log(ccs, "&f[&dp&aLobby&f]&a Carregando o mundo &e&lLobby" + i);
            }
        }
        //Loading Managers...
        pm.loadProfiles();

        //Loading Events...
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        getServer().getPluginManager().registerEvents(new ItemEvent(this), this);

        //Loading commands...
        getCommand("sair").setExecutor(new Quit());

        //Finish Enable
        Logger.log(ccs, "&f[&dp&aLobby&f]&a Plugin iniciado com sucesso!");
    }

    public void onDisable() {
        HandlerList.unregisterAll();
        pm.saveProfiles();
        Logger.log(ccs, "&f[&dp&aLobby&f]&c Plugin desligado com sucesso!");
    }


}

