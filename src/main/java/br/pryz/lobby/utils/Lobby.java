package br.pryz.lobby.utils;


import br.pryz.lobby.main.LobbyMain;
import br.pryz.lobby.utils.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Lobby {
    private static JavaPlugin pl = LobbyMain.getInstance();
    private static PryConfig locations = new PryConfig(pl, "locations.yml");
    private static PryConfig config = new PryConfig(pl, "config.yml");
    private static int numberOfLobbys = config.getInt("NumberOfLobbys");
    private static List<Player> invanish = new ArrayList<Player>();

    public static boolean noExists() {
        for (int i = 1; i < numberOfLobbys; i++) {
            if (Bukkit.getWorld("lobby" + i) == null) return true;
        }

        return false;
    }

    /**
     *
     * @param p - Player
     * @param t - Target
     */
    public static void openProfile(Player p, Player t) {
        Profile pf = LobbyMain.getProfileManager().getProfile(t.getUniqueId());
        if (p == t) {
            Inventory inv = Bukkit.createInventory(null, 9 * 5, color("&aMeu Perfil"));
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
            Date first = new Date(pf.getFirstTimeOnline()), last = new Date(pf.getLastTimeOnline());

            String sfirst = color("&aConta criada em &f") + df.format(first),
                    slast = color("&cUltimo login em &f") + df.format(last),
                    discord = color("&aD&bi&cs&dc&eo&fr&ad&f: ") + pf.getDiscord(),
            email = color("&6E-Mail&f:");
            List<String> lore = Arrays.asList(color(pf.getStatus().name()), email, discord, sfirst, slast);
            ItemStack item = ItemBuilder.newItem(p.getName(), Material.SKELETON_SKULL, lore);
            inv.setItem(11, item);
            p.openInventory(inv);
            return;
        } else {
        if (p.hasPermission("pry.lobby.level.admin")){
            Inventory inv = Bukkit.createInventory(null, 9 * 5, color("&aMeu Perfil"));
            DateFormat df = DateFormat.getDateInstance(DateFormat.FULL);
            Date first = new Date(pf.getFirstTimeOnline()), last = new Date(pf.getLastTimeOnline());

            String sfirst = color("&aConta criada em &f") + df.format(first),
                    slast = color("&cUltimo login em &f") + df.format(last),
                    discord = color("&aD&bi&cs&dc&eo&fr&ad&f: ") + pf.getDiscord(),
                    email = color("&6E-Mail&f:");
            List<String> lore = Arrays.asList(color(pf.getStatus().name()), email, discord, sfirst, slast);
            ItemStack item = ItemBuilder.newItem(p.getName(), Material.SKELETON_SKULL, lore);
            inv.setItem(11, item);
            p.openInventory(inv);
            return;
        }
            Inventory inv = Bukkit.createInventory(null, 9 * 5, color("&aMeu Perfil"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            Date first = new Date(pf.getFirstTimeOnline()), last = new Date(pf.getLastTimeOnline());
            String sfirst = color("&aConta criada em &f") + sdf.format(first),
                    slast = color("&cUltimo login em &f") + sdf.format(last),
                    discord = color("&aD&bi&cs&dc&eo&fr&ad&f: ") + pf.getDiscord();
            List<String> lore = Arrays.asList(color(pf.getStatus().name()), discord, sfirst, slast);
            ItemStack item = ItemBuilder.newItem(p.getName(), Material.SKELETON_SKULL, lore);
            inv.setItem(11, item);
            p.openInventory(inv);
            return;
        }

    }

    public static void openLobbys(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, color("&aLobbys"));
        for (int i = 1; i < numberOfLobbys; i++) {
            ArrayList<String> lore = new ArrayList<>();
            String lobby = "lobby" + i;
            lore.add(color("&a") + Bukkit.getWorld(lobby).getPlayers().size() + color("&a jogadores estão conectados a este lobby."));
            ItemStack item = ItemBuilder.newItem("&aLobby " + i, Material.EMERALD, lore);
            inv.setItem(11 + i, item);
        }
        p.openInventory(inv);
        p.sendMessage(color("&aAbrindo Lobbys..."));
    }

    public static void giveItens(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(null);
        ItemStack head = ItemBuilder.newItem("&aPerfil", Material.SKELETON_SKULL);
        ItemStack compass = ItemBuilder.newItem("&aServidores", Material.COMPASS);
        ItemStack pvp = ItemBuilder.newItem("&aIr Batalhar", Material.IRON_SWORD);
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        ArrayList<String> texto = new ArrayList<String>();
        texto.add(color(" &5&lPryzat\n&aBem-Vindo(a) a versão &eBeta &ado servidor."));
        bookMeta.setTitle(color("&b&lNovidades"));
        bookMeta.setAuthor(color("&5&lPryzat"));
        bookMeta.setPages(texto);
        book.setItemMeta(bookMeta);

        ItemStack charge = ItemBuilder.newItem("&aVisibilidade", Material.SLIME_BALL);
        ItemStack star = ItemBuilder.newItem("&aLobbys", Material.NETHER_STAR);

        inv.setItem(0, head);
        inv.setItem(1, compass);
        inv.setItem(4, pvp);
        inv.setItem(3, book);
        inv.setItem(6, charge);
        inv.setItem(8, star);
        p.updateInventory();
    }

    public static void openServers(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, color("&aServidores"));
        ItemStack s1 = ItemBuilder.newItem("&aServer 1", Material.EMERALD);
        ItemStack s2 = ItemBuilder.newItem("&aServer 2", Material.EMERALD);
        ItemStack s3 = ItemBuilder.newItem("&aServer 3", Material.EMERALD);
        inv.setItem(12, s1);
        inv.setItem(13, s2);
        inv.setItem(14, s3);
        p.openInventory(inv);
        p.sendMessage(color("&aAbrindo Servidores..."));
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 50);
    }

    public static void teleportLobby(Player p, String lobby) {
        p.sendMessage(color("&aConectando-se ao ") + lobby);
        p.teleport(getLocationsYml().getLocation(lobby.toLowerCase()));
    }

    private static String color(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }

    public static void teleportServer(Player p, String server_name) {
        LobbyMain.sendPluginMessage(p, "BungeeCord", "Connect", server_name.toLowerCase());
    }

    public static int getNumberOfLobbys() {
        return numberOfLobbys;
    }

    public static List<Player> getVanishedPlayers() {
        return invanish;
    }

    //YML Section
    public static String getServerPrefix() {
        if (getConfig().getString("ServerPrefix") != null) {
            return color(getConfig().getString("ServerPrefix"));
        } else {
            return color("&eSistema &f>");
        }
    }


    public static PryConfig getConfig() {
        return config;
    }

    public static PryConfig getLocationsYml() {
        return locations;
    }

}

