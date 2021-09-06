package br.pryz.lobby.events;

import br.pryz.lobby.main.LobbyMain;
import br.pryz.lobby.utils.Lobby;
import br.pryz.lobby.utils.PvP;
import br.pryz.lobby.utils.profile.Profile;
import br.pryz.lobby.utils.profile.ProfileManager;
import br.pryz.lobby.utils.profile.StatusType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class PlayerEvent
        implements Listener {
    private JavaPlugin pl;
    private ProfileManager pm;
    public PlayerEvent(LobbyMain plugin){
        pl = plugin;
        pm = plugin.getProfileManager();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (p.hasPermission("pry.lobby.staff")) {
            if (e.getMessage().startsWith("/")) return;
            if (e.getMessage().startsWith("!")) {
                Bukkit.getOnlinePlayers().forEach(p2 -> p2.sendMessage(color("&bEquipe&f > " + p.getName() + ": " + e.getMessage())));
            } else {
                p.getWorld().getPlayers().forEach(p2 -> p2.sendMessage(color("&bEquipe&f > " + p.getName() + ": " + e.getMessage())));
            }
            e.setCancelled(true);
        } else {
            if (e.getMessage().startsWith("/")) return;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void noPickItemOnCreative(InventoryOpenEvent e) {
        if (e.getView().getType() != InventoryType.CREATIVE) return;
        if (e.getPlayer().hasPermission("pry.lobby.bypass")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().hasPermission("pry.lobby.bypass")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (p.hasPermission("pry.lobby.bypass")) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        if (e.getWhoClicked().hasPermission("pry.lobby.bypass")) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (e.getView().getTitle().equals(color("&ePerfil"))) return;
        if (e.getView().getTitle().equals(color("&aLobbys"))) return;
        if (e.getView().getTitle().equals(color("&aServidores"))) return;
        e.setResult(Event.Result.DENY);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Profile profile = pm.getProfile(p.getUniqueId());
        if (profile == null){
            profile = pm.createProfile(p.getUniqueId());
        }
        profile.setStatus(StatusType.DISPONIVEL);

        PvP.getPlayers().forEach(p2 -> p2.hidePlayer(pl, p));
        p.setGameMode(GameMode.CREATIVE);
        if (!p.hasPermission("pry.lobby.vip")) {
            p.setAllowFlight(false);
            p.setFlying(false);
        }
        Lobby.giveItens(p);
        e.setJoinMessage(null);
        int lobby = new Random().nextInt(Lobby.getNumberOfLobbys());
        if (lobby == 0) {
            lobby = 1;
        }
        Lobby.teleportLobby(p, "lobby" + lobby);
        Lobby.getVanishedPlayers().forEach(p3 -> p3.hidePlayer(pl, p));
        //LobbyMain.bossbar.addPlayer(e.getPlayer());
        //LobbyMain.bossbar.setVisible(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Profile profile = pm.getProfile(p.getUniqueId());
        profile.setStatus(StatusType.OFFLINE);
        p.getInventory().clear();
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        p.setGameMode(GameMode.CREATIVE);
        if (!p.hasPermission("pry.lobby.vip")) {
            p.setAllowFlight(false);
            p.setFlying(false);
        }
        World world = p.getWorld();
        world.getPlayers().forEach(p2 -> {
            if (p.hasPermission("pry.lobby.staff")) {
                p2.sendMessage(color("&aEquipe&f > " + p.getName() + "&a entrou nesse &a&lLobby"));
                return;
            }
            p2.sendMessage(color("&e" + p.getName() + "&a entrou nesse &a&lLobby."));
        });
    }

    @EventHandler
    public void onVoid(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) && e.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getEntity();
        Lobby.teleportLobby(p, "lobby1");
        Location desbug = new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY() + 5.0, e.getEntity().getLocation().getZ());
        e.getEntity().teleport(desbug);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
        Player p = e.getEntity();
        p.spigot().respawn();
        e.setDeathMessage(null);
        if (!PvP.isInPvp(p)) {
            PvP.getPlayers()
                    .forEach(p2 ->
                            p2.sendMessage(color("&e" + p.getName() + "&c foi morto por &e" + p.getKiller().getName())));
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (PvP.isInPvp(p)){
            PvP.removePlayer(p);
            Lobby.giveItens(p);
        } else {
            Lobby.giveItens(p);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;

        Player p = (Player) e.getEntity();
        Player d = (Player) e.getDamager();
        if (PvP.isInPvp(d)) return;
        if (d.hasPermission("pry.lobby.mod")) return;
        e.setCancelled(true);
    }

    private String color(String txt) {
        return ChatColor.translateAlternateColorCodes('&', txt);
    }
}

