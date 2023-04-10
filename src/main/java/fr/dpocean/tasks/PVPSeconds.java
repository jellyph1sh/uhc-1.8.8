package fr.dpocean.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class PVPSeconds extends BukkitRunnable {
    private UHCPlugin plugin;
    private int seconds = 10;

    public PVPSeconds(UHCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void run() {
        if (seconds <= 0) {
            return;
        }
        plugin.broadcastMessage(ChatColor.RED, "PVP start in " + seconds + " seconds!");
        seconds--;
    }
}
