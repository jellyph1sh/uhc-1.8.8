package fr.dpocean.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import fr.dpocean.UHCPlugin;

public class UHCListenersGameManager implements Listener {
    private UHCPlugin plugin;

    public UHCListenersGameManager(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (!plugin.isGame) {
                if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("start-game") && plugin.restartTask == null) {
                    Bukkit.broadcastMessage("A game start in 1 minute!");
                    BukkitScheduler scheduler = plugin.getServer().getScheduler();
                    plugin.restartTask = scheduler.runTaskLater(plugin, new Runnable() {
                        @Override
                        public void run() {
                            Bukkit.broadcastMessage("A game is starting!");
                            plugin.startGame();
                            plugin.restartTask = null;
                        }
                    }, 20*60);
                }
                Bukkit.broadcastMessage("There is " + Bukkit.getOnlinePlayers().size() + " players connected!");
            } else {
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (plugin.isGame) {
                plugin.players.remove(event.getPlayer());
                if (plugin.players.size() <= 1) {
                    plugin.stopGame();
                }
            }
            if (Bukkit.getOnlinePlayers().size() - 1 < plugin.getConfig().getInt("start-game") && plugin.restartTask != null) {
                plugin.restartTask.cancel();
                plugin.restartTask = null;
                Bukkit.broadcastMessage("The game start has been cancel!");
            }
            Bukkit.broadcastMessage("There is " + (Bukkit.getOnlinePlayers().size() - 1) + " players connected!");
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent event) {
        if ((Player) event.getEntity() instanceof Player && plugin.isGame) {
            Player ply = (Player) event.getEntity();
            Location deathLocation = ply.getLocation();
            ply.spigot().respawn();
            plugin.players.remove(ply);
            if (plugin.players.size() <= 1) {
                if (plugin.stopGame()) {
                    if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("start-game") && !plugin.isGame && plugin.restartTask == null) {
                        Bukkit.broadcastMessage("A game start in 1 minute!");
                        BukkitScheduler scheduler = plugin.getServer().getScheduler();
                        plugin.restartTask = scheduler.runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                Bukkit.broadcastMessage("A game is starting!");
                                plugin.startGame();
                                plugin.restartTask = null;
                            }
                        }, 20*60);
                    }
                }
            } else {
                ply.teleport(deathLocation);
                ply.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
