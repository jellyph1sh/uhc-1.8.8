package fr.dpocean.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class RestartGame extends BukkitRunnable {
    private UHCPlugin plugin;

    public RestartGame(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.startGame();
        plugin.restartTask = null;
    }
}
