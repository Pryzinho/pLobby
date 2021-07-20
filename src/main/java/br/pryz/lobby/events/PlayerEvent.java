package br.pryz.lobby.events;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pry.lobby.eventos.ItensEvent;
import pry.lobby.principal.LobbyMain;
import pry.lobby.utils.Lobby;
import pry.lobby.utils.PvP;

public class PlayerEvent
        implements Listener {
    private JavaPlugin pl = LobbyMain.getInstance();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("pry.lobby.staff")) {
            e.setCancelled(true);
            return;
        }
        if (e.getMessage().startsWith("!")) {
            Bukkit.getOnlinePlayers().forEach(p2 -> p2.sendMessage("[§bEquipe§f] " + p.getName() + ": " + e.getMessage()));
        } else {
            p.getWorld().getPlayers().forEach(p3 -> p3.sendMessage("[§bEquipe§f] " + p.getName() + ": " + e.getMessage()));
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void t(InventoryOpenEvent e) {
        if (e.getView().getType() != InventoryType.CREATIVE) return;
        if (e.getPlayer().hasPermission("pry.lobby.bypass")) return;
        e.setCancelled(true);
        Player p = (Player)e.getPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50.0f, 50.0f);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e) {
        if (e.getPlayer().hasPermission("pry.lobby.bypass")) {
            return;
        }
        Player p = e.getPlayer();
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50.0f, 50.0f);
        p.sendMessage("§cVocê não pode dropar itens.");
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        if (((Player)e).getPlayer().hasPermission("pry.lobby.bypass")) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e) {
        if (e.getWhoClicked().hasPermission("pry.lobby.bypass")) {
            if (!e.getView().getTitle().equals("§ePerfil") && !e.getView().getTitle().equals("§aLobbys")) {
                if (!e.getView().getTitle().equals("§aServidores")) return;
            }
            e.setResult(Event.Result.DENY);
            return;
        }
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        e.setResult(Event.Result.DENY);
        if (e.getView().getTitle().equals("§ePerfil")) return;
        if (e.getView().getTitle().equals("§aLobbys")) return;
        if (e.getView().getTitle().equals("§aServidores")) {
            return;
        }
        Player p = (Player)e.getWhoClicked();
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 50.0f, 50.0f);
        p.sendMessage("§cVocê não pode pegar itens.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PvP.getPlayers().forEach(player2 -> player2.hidePlayer((Plugin)this.pl, p));
        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(false);
        p.setFlying(false);
        Lobby.giveItens((Player)p);
        e.setJoinMessage(null);
        int lobby = new Random().nextInt(5);
        if (lobby == 0) {
            lobby = 1;
        }
        Lobby.teleportLobby((Player)p, (int)lobby);
        ItensEvent.invanish.stream().filter(p2 -> {
            if (!p2.hasPermission("pry.lobby.staff")) return true;
            return false;
        }).forEach(p3 -> p3.hidePlayer((Plugin)this.pl, p));
        LobbyMain.bossbar.addPlayer(e.getPlayer());
        LobbyMain.bossbar.setVisible(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        p.getInventory().clear();
        e.setQuitMessage(null);
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        p.setGameMode(GameMode.CREATIVE);
        p.setAllowFlight(false);
        p.setFlying(false);
        World mundo = p.getWorld();
        mundo.getPlayers().forEach(player2 -> {
            if (p.hasPermission("pry.lobby.admin")) {
                player2.sendMessage("§f[§aStaff§f] " + p.getName() + "§a entrou nesse §a§lLobby");
                return;
            }
            player2.sendMessage("§e" + p.getName() + "§a entrou nesse §a§lLobby.");
        });
    }

    @EventHandler
    public void onVoid(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player) && e.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }
        e.setCancelled(true);
        Lobby.teleportLobby((Player)((Player)e.getEntity()), (int)1);
        Location desbug = new Location(e.getEntity().getWorld(), e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY() + 5.0, e.getEntity().getLocation().getZ());
        e.getEntity().teleport(desbug);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        e.setDroppedExp(0);
        e.getDrops().clear();
        Player p = e.getEntity();
        if (!PvP.isInPvp((Player)p)) return;
        e.setDeathMessage(null);
        PvP.getPlayers().forEach(player2 -> player2.sendMessage("§e" + p.getName() + "§c foi morto por §e" + p.getKiller().getName()));
        p.spigot().respawn();
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (!PvP.isInPvp((Player)p)) return;
        PvP.removePlayer((Player)p);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player)e.getEntity();
        Player d = (Player)e.getDamager();
        if (PvP.isInPvp((Player)d)) return;
        if (d.hasPermission("pry.lobby.admin")) return;
        e.setCancelled(true);
    }
}

