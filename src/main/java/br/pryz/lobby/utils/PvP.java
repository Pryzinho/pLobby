package br.pryz.lobby.utils;

import br.pryz.lobby.main.LobbyMain;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

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
            p.hidePlayer(pl, player2);
        });
        p.sendMessage("§aVocê entrou na batalha caso queira sair digite: §c/sair");
    }

    public static void removePlayer(Player p) {
        p.setGameMode(GameMode.CREATIVE);
        p.setFlying(false);
        p.setAllowFlight(false);
        PvP.getPlayers().remove(p);
        p.getWorld().getPlayers().forEach(player2 -> p.showPlayer(pl, player2));
        Lobby.giveItens(p);
    }

    public static void giveItens(Player p) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        ItemStack espada = PryItem.create("Espada", Material.IRON_SWORD);
        ItemStack comida = PryItem.create("Maçã Dourada", Material.GOLDEN_APPLE, 5);
        ItemStack capacete = PryItem.create("Armadura", Material.IRON_HELMET);
        ItemStack peitoral = PryItem.create("Armadura", Material.IRON_CHESTPLATE);
        ItemStack calcas = PryItem.create("Armadura", Material.IRON_LEGGINGS);
        ItemStack bota = PryItem.create("Armadura", Material.IRON_BOOTS);
        inv.setHelmet(capacete);
        inv.setChestplate(peitoral);
        inv.setLeggings(calcas);
        inv.setBoots(bota);
        inv.setItem(0, espada);
        inv.setItem(1, comida);
        p.updateInventory();
    }
}

