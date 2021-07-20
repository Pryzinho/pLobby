package br.pryz.lobby.events;
import java.util.ArrayList;
import java.util.List;

import br.pryz.lobby.main.LobbyMain;
import br.pryz.lobby.utils.ItemBuilder;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.PvP;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemEvent
        implements Listener {
    private JavaPlugin pl = LobbyMain.getInstance();
    public static List<Player> invanish = new ArrayList<Player>();

    @EventHandler
    public void onLobbyInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getType() != Material.NETHER_STAR) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        }
        if (!e.getItem().getItemMeta().getDisplayName().equals("§aLobbys")) return;
        Lobby.openLobbys(p);
    }

    @EventHandler
    public void onLobbyClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getView().getTitle().equals("§aLobbys")) return;
        Player p = (Player)e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Lobby.teleportLobby(p, item.getItemMeta().getDisplayName());
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50.0f, 50.0f);
    }

    @EventHandler
    public void onServerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getType() != Material.COMPASS) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        }
        if (!e.getItem().getItemMeta().getDisplayName().equals("§aServidores")) return;
        Lobby.openServers(p);
    }

    @EventHandler
    public void onServerClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getView().getTitle().equals("§aServidores")) return;
        Player p = (Player)e.getWhoClicked();
        try {
            Lobby.teleportServer(p, 1);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50.0f, 50.0f);
            return;
        }
        catch (Exception ex) {
            p.sendMessage("§cAlgo está errado.");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50.0f, 50.0f);
        }
    }

    @EventHandler
    public void onChangeVisibility(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getType() != Material.FIRE_CHARGE) {
            if (e.getItem().getType() != Material.SLIME_BALL) return;
        }
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        }
        if (!e.getItem().getItemMeta().getDisplayName().equals("§aVisibilidade")) return;
        e.setCancelled(true);
        if (!invanish.contains(p)) {
            invanish.add(p);
            ItemStack fwt = ItemBuilder.newItem((String)"§aVisibilidade", (Material)Material.FIRE_CHARGE);
            p.getWorld().getPlayers().stream().filter(p2 -> {
                if (!p2.hasPermission("pry.lobby.staff")) return true;
                return false;
            }).forEach(player2 -> {
                if (player2.equals(p)) {
                    return;
                }
                p.hidePlayer((Plugin)this.pl, player2);
            });
            p.getInventory().setItemInMainHand(fwt);
            return;
        }
        invanish.remove(p);
        p.getWorld().getPlayers().forEach(player2 -> p.showPlayer((Plugin)this.pl, player2));
        Lobby.giveItens((Player)p);
    }

    @EventHandler
    public void onPvpInteract(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getType() != Material.IRON_SWORD) {
            return;
        }
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        }
        if (!e.getItem().getItemMeta().getDisplayName().equals("§aIr Batalhar")) return;
        e.setCancelled(true);
        PvP.addPlayer((Player)p);
    }
}

