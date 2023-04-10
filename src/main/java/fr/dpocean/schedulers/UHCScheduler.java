package fr.dpocean.schedulers;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitTask;

import fr.dpocean.UHCPlugin;
import fr.dpocean.tasks.MessageTask;
import fr.dpocean.tasks.PVPSeconds;
import fr.dpocean.tasks.PVPTask;
import fr.dpocean.tasks.StopGame;
import fr.dpocean.tasks.WorldBorderTask;

public class UHCScheduler {
    private UHCPlugin plugin;
    public Collection<BukkitTask> tasks;

    public UHCScheduler(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    public void pvpScheduler() {
        new PVPTask(plugin).runTaskTimer(plugin, 0, 20 * 60 * 5);
        new MessageTask(plugin, ChatColor.RED, "PVP start in 1 minute!").runTaskLater(plugin, 20 * 60 * 19);
        new PVPSeconds(plugin).runTaskTimer(plugin, 20 * 60 * 19 + 20 * 50, 20);
        new WorldBorderTask(plugin).runTaskTimer(plugin, 20 * 60 * 20, 20);
    }

    public void stopGameLater() {
        new StopGame(plugin).runTaskLater(plugin, 20 * 30);
    }
}
