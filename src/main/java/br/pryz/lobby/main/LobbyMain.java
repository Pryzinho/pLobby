package br.pryz.lobby.main;

import br.pryz.lobby.commands.Quit;
import br.pryz.lobby.events.ItemEvent;
import br.pryz.lobby.events.PlayerEvent;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.LobbyConfig;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.sun.tools.javac.resources.CompilerProperties;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;


public class LobbyMain extends JavaPlugin {
    private final CommandSender console = Bukkit.getConsoleSender();
    public static BossBar bossbar = Bukkit.createBossBar("§d§lloja.pryzat.com.br", BarColor.PURPLE, BarStyle.SOLID, new BarFlag[]{BarFlag.DARKEN_SKY});

    @Override
    public void onLoad() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        LobbyConfig.getConfig().saveDefaultConfig();
    }

    public void onEnable() {
        //Loading Events...
        getServer().getPluginManager().registerEvents(new PlayerEvent(), this);
        getServer().getPluginManager().registerEvents(new ItemEvent(), this);
        //Loading worlds...
        if (Lobby.noExists()) {
            Bukkit.createWorld(new WorldCreator("lobby1").type(WorldType.FLAT));
            Bukkit.createWorld(new WorldCreator("lobby2").type(WorldType.FLAT));
            Bukkit.createWorld(new WorldCreator("lobby3").type(WorldType.FLAT));
            Bukkit.createWorld(new WorldCreator("lobby4").type(WorldType.FLAT));
            Bukkit.createWorld(new WorldCreator("lobby5").type(WorldType.FLAT));
        }

        LobbyConfig.setLobbyLocation(1, Bukkit.getWorld("lobby1").getSpawnLocation());
        LobbyConfig.setLobbyLocation(2, Bukkit.getWorld("lobby2").getSpawnLocation());
        LobbyConfig.setLobbyLocation(3, Bukkit.getWorld("lobby3").getSpawnLocation());
        LobbyConfig.setLobbyLocation(4, Bukkit.getWorld("lobby4").getSpawnLocation());
        LobbyConfig.setLobbyLocation(5, Bukkit.getWorld("lobby5").getSpawnLocation());

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
        this.console.sendMessage("--------------------");
        this.console.sendMessage(color(" &ePryz Lobby "));
        this.console.sendMessage(color(" &cPugin Desabilitado "));
        this.console.sendMessage("--------------------");
    }

    private String color(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }

    public static LobbyMain getInstance() {
        return getPlugin(LobbyMain.class);
    }

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
}

