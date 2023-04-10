package fr.dpocean.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class StopGame extends BukkitRunnable {
    private UHCPlugin plugin;

    public StopGame(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.broadcastMessage(ChatColor.YELLOW, "Welcome back to the lobby!");
        plugin.stopGame();
    }
}
