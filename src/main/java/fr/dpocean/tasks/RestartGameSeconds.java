package fr.dpocean.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class RestartGameSeconds extends BukkitRunnable {
    private UHCPlugin plugin;
    private int seconds = 10;

    public RestartGameSeconds(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (seconds <= 0) {
            return;
        }
        plugin.broadcastMessage(ChatColor.YELLOW, "New game will start in " + seconds + " seconds!");
        seconds--;
    }
}
