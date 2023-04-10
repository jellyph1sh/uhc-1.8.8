package fr.dpocean.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class PVPTask extends BukkitRunnable {
    private UHCPlugin plugin;
    private int time = 0;

    public PVPTask(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        switch (time) {
            case 0:
                plugin.broadcastMessage(ChatColor.RED, "PVP start in 20 minutes!");
                break;
            case 1:
                plugin.broadcastMessage(ChatColor.RED, "PVP start in 15 minutes!");
                break;
            case 2:
                plugin.broadcastMessage(ChatColor.RED, "PVP start in 10 minutes!");
                break;
            case 3:
                plugin.broadcastMessage(ChatColor.RED, "PVP start in 5 minutes!");
                break;
            case 4:
                Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).setPVP(true);
                plugin.broadcastMessage(ChatColor.RED, "PVP is now active!");
                plugin.broadcastMessage(ChatColor.RED, "World border starting to shrink!");
                break;
            default:
                break;
        }
        time++;
    }
}
