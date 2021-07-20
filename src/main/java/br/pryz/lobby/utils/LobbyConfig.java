package br.pryz.lobby.utils;


import br.pryz.lobby.main.LobbyMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class LobbyConfig {
    private static PryConfig config = new PryConfig(LobbyMain.getInstance(), "config.yml");

    public static PryConfig getConfig() {
        return config;
    }

    public static String getLobbyPrefix() {
        if (LobbyConfig.getConfig().getString("ServerConfigs.prefixo") != null){
            String txt = LobbyConfig.getConfig().getString("ServerConfigs.prefixo");
            return color(txt);
        } else {
            return color("&f[&a&lServer&f]");
        }
    }

    public static void setLobbyLocation(int lobby, Location location) {
        LobbyConfig.getConfig().set("Lobby" + lobby + ".world", (Object)location.getWorld().getName());
        LobbyConfig.getConfig().set("Lobby" + lobby + ".x", (Object)location.getX());
        LobbyConfig.getConfig().set("Lobby" + lobby + ".y", (Object)location.getY());
        LobbyConfig.getConfig().set("Lobby" + lobby + ".z", (Object)location.getZ());
        LobbyConfig.getConfig().saveConfig();
    }

    public static Location getLobbyLocation(int lobby) {
        World world = Bukkit.getWorld(LobbyConfig.getConfig().getString("Lobby" + lobby + ".world"));
        double x = LobbyConfig.getConfig().getDouble("Lobby" + lobby + ".x");
        double y = LobbyConfig.getConfig().getDouble("Lobby" + lobby + ".y");
        double z = LobbyConfig.getConfig().getDouble("Lobby" + lobby + ".z");
        return new Location(world, x, y, z);
    }
    private static String color(String txt){
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}
