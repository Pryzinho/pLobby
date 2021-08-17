package br.pryz.lobby.events;

import br.pryz.lobby.main.LobbyMain;
import br.pryz.lobby.utils.ItemBuilder;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.PvP;
import br.pryz.lobby.utils.profile.ProfileManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ItemEvent
        implements Listener {
    private static List<Player> invanish = Lobby.getVanishedPlayers();
    private JavaPlugin pl;
    private ProfileManager pm;

    public ItemEvent(LobbyMain plugin){
        pl = plugin;
        pm = plugin.getProfileManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem() == null) return;

        if (e.getItem().getType() == Material.NETHER_STAR) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
            if (!e.getItem().getItemMeta().getDisplayName().equals(color("&aLobbys"))) return;
            Lobby.openLobbys(p);
            return;
        }
        if (e.getItem().getType() == Material.SKELETON_SKULL) {
            if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
            if (!e.getItem().getItemMeta().getDisplayName().equals(color("&aPerfil"))) return;
            Lobby.openProfile(pm, p, p);
            return;
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (!(e.getRightClicked() instanceof Player)) return;
        Player t = (Player) e.getRightClicked();
        if (p.getEquipment().getItemInMainHand().getType() == Material.SKELETON_SKULL) {
            if (!p.getEquipment().getItemInMainHand().getItemMeta().getDisplayName().equals(color("&aPerfil"))) return;
            Lobby.openProfile(pm, p, t);
            return;
        }
    }


    @EventHandler
    public void onLobbyClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getView().getTitle().equals(color("&aLobbys"))) return;
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        Lobby.teleportLobby(p, item.getItemMeta().getDisplayName());
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 50.0f, 50.0f);
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
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;

        if (!e.getItem().getItemMeta().getDisplayName().equals(color("&aServidores"))) return;
        Lobby.openServers(p);
    }

    @EventHandler
    public void onServerClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!e.getView().getTitle().equals(color("&aServidores"))) return;
        Player p = (Player) e.getWhoClicked();
        try {
            Lobby.teleportServer(p, e.getCurrentItem().getItemMeta().getDisplayName());
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 50.0f, 50.0f);
            return;
        } catch (Exception ex) {
            p.sendMessage(Lobby.getServerPrefix() + color("&cHouve um erro na conexão, contate um membro da equipe."));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50.0f, 50.0f);
            return;
        }
    }

    @EventHandler
    public void onChangeVisibility(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if (e.getItem().getType() != Material.FIRE_CHARGE && (e.getItem().getType() != Material.SLIME_BALL)) return;

        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!e.getItem().getItemMeta().getDisplayName().equals(color("&aVisibilidade"))) return;
        if (!invanish.contains(p)) {
            invanish.add(p);
            ItemStack fwt = ItemBuilder.newItem("&aVisibilidade", Material.FIRE_CHARGE);
            p.getWorld().getPlayers().stream().filter(p2 -> !p2.hasPermission("pry.lobby.staff")).forEach(p3 -> {
                if (p3.equals(p)) return;
                p.hidePlayer(pl, p3);
            });
            p.getInventory().setItemInMainHand(fwt);
            return;
        }
        invanish.remove(p);
        p.getWorld().getPlayers().forEach(p2 -> p.showPlayer(pl, p2));
        Lobby.giveItens(p);
        e.setCancelled(true);
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
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (!e.getItem().getItemMeta().getDisplayName().equals(color("&aIr Batalhar"))) return;
        e.setCancelled(true);
        if (invanish.contains(p)) {
            invanish.remove(p);
            p.getWorld().getPlayers().forEach(p2 -> p.showPlayer(pl, p2));
        }
        PvP.addPlayer(p);
    }

    private String color(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}

