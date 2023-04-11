package fr.dpocean.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.dpocean.UHCPlugin;
import fr.dpocean.tasks.RestartGame;
import fr.dpocean.tasks.RestartGameSeconds;

public class UHCListenersGameManager implements Listener {
    private UHCPlugin plugin;

    public UHCListenersGameManager(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer() instanceof Player) {
            //plugin.scoreboard.setScoreboard(event.getPlayer());
            if (!plugin.isGame) {
                if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("start-game") && !plugin.isGame && plugin.restartTask == null) {
                    plugin.broadcastMessage(ChatColor.YELLOW, "A new game will start in 1 minute!");
                    plugin.restartTaskSeconds = new RestartGameSeconds(plugin).runTaskTimer(plugin, 20*50, 20);
                    plugin.restartTask = new RestartGame(plugin).runTaskLater(plugin, 20*60);
                }
                event.setJoinMessage(ChatColor.GREEN + event.getPlayer().getDisplayName() + " has join the server!");
                plugin.broadcastMessage(ChatColor.YELLOW, "There is " + Bukkit.getOnlinePlayers().size() + " players connected!");
            } else {
                event.setJoinMessage(ChatColor.YELLOW + event.getPlayer().getDisplayName() + " has join the game as spectator!");
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (event.getPlayer() instanceof Player) {
            if (plugin.isGame) {
                event.setQuitMessage(ChatColor.RED + event.getPlayer().getDisplayName() + " has left the game!");
                plugin.players.remove(event.getPlayer());
                if (plugin.players.size() <= 1) {
                    plugin.broadcastMessage(ChatColor.GOLD, "The winner is " + plugin.players.get(0) + "!");
                    plugin.stopGameLater();
                }
            } else {
                event.setQuitMessage(ChatColor.RED + event.getPlayer().getDisplayName() + " has left the server!");
                if (Bukkit.getOnlinePlayers().size() - 1 < plugin.getConfig().getInt("start-game") && plugin.restartTask != null) {
                    plugin.restartTask.cancel();
                    plugin.restartTask = null;
                    plugin.restartTaskSeconds.cancel();
                    plugin.restartTaskSeconds = null;
                    plugin.broadcastMessage(ChatColor.RED, "The automatic game starter has been cancel!");
                    plugin.broadcastMessage(ChatColor.RED, "Insufficient players!");
                }
                plugin.broadcastMessage(ChatColor.YELLOW, "There is " + (Bukkit.getOnlinePlayers().size() - 1) + " players connected!");
            }
        }
    }

    @EventHandler
    public void onPlayerDead(PlayerDeathEvent event) {
        if ((Player) event.getEntity() instanceof Player && plugin.isGame) {
            Player ply = (Player) event.getEntity();
            Location deathLocation = ply.getLocation();
            ply.spigot().respawn();
            ply.teleport(deathLocation);
            ply.setGameMode(GameMode.SPECTATOR);
            if (plugin.players.size() > 1 && ply.getKiller() instanceof Player) {
                ply.setSpectatorTarget(ply.getKiller());
            }
            plugin.players.remove(ply);
            if (plugin.players.size() <= 1) {
                plugin.broadcastMessage(ChatColor.GOLD, "The winner is " + plugin.players.get(0).getDisplayName() + "!");
                plugin.stopGameLater();
            }
        }
    }
}
