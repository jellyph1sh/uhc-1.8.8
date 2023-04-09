package fr.dpocean.schedulers;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import fr.dpocean.UHCPlugin;

public class UHCScheduler {
    private UHCPlugin plugin;
    public Collection<BukkitTask> tasks;

    public UHCScheduler(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    public void pvpScheduler(BukkitScheduler scheduler) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).setPVP(true);
                Bukkit.broadcastMessage("PVP IS NOW ACTIVE!");
            }
        }.runTaskLater(plugin, plugin.getConfig().getConfigurationSection("timers").getInt("pvpactivate"));
        
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                World world = Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc"));
                WorldBorder wb = world.getWorldBorder();
                wb.setSize(wb.getSize() - 1);
            }
        }, plugin.getConfig().getConfigurationSection("timers").getInt("pvpactivate"), 20);
    }
}
