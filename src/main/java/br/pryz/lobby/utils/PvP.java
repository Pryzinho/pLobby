package br.pryz.lobby.utils;

import java.util.ArrayList;
import java.util.List;

import br.pryz.lobby.main.LobbyMain;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PvP {
    private static JavaPlugin pl = LobbyMain.getInstance();
    private static List<Player> inpvp = new ArrayList<Player>();

    public static List<Player> getPlayers() {
        return inpvp;
    }

    public static boolean isInPvp(Player p) {
        if (!PvP.getPlayers().contains(p)) return false;
        return true;
    }

    public static void addPlayer(Player p) {
        PvP.giveItens(p);
        p.setGameMode(GameMode.SURVIVAL);
        PvP.getPlayers().add(p);
        p.getWorld().getPlayers().forEach(player2 -> {
            if (PvP.isInPvp(player2)) return;
            p.hidePlayer((Plugin) pl, player2);
        });
        p.sendMessage("§aVocê entrou na batalha caso queira sair digite: §c/sair");
    }

    public static void removePlayer(Player p) {
        p.setGameMode(GameMode.CREATIVE);
        p.setFlying(false);
        p.setAllowFlight(false);
        PvP.getPlayers().remove(p);
        p.getWorld().getPlayers().forEach(player2 -> p.showPlayer((Plugin) pl, player2));
        Lobby.giveItens((Player) p);
    }

    public static void giveItens(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        ItemStack espada = ItemBuilder.newItem((String) "Espada", (Material) Material.IRON_SWORD);
        ItemStack comida = ItemBuilder.newItem((String) "Maçã Pryzat", (Material) Material.GOLDEN_APPLE, (int) 10);
        ItemStack capacete = ItemBuilder.newItem((String) "Armadura", (Material) Material.IRON_HELMET);
        ItemStack peitoral = ItemBuilder.newItem((String) "Armadura", (Material) Material.IRON_CHESTPLATE);
        ItemStack calcas = ItemBuilder.newItem((String) "Armadura", (Material) Material.IRON_LEGGINGS);
        ItemStack bota = ItemBuilder.newItem((String) "Armadura", (Material) Material.IRON_BOOTS);
        inv.setHelmet(capacete);
        inv.setChestplate(peitoral);
        inv.setLeggings(calcas);
        inv.setBoots(bota);
        inv.setItem(0, espada);
        inv.setItem(1, comida);
        p.updateInventory();
    }
}

