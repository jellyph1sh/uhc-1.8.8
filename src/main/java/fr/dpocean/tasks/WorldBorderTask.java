package fr.dpocean.tasks;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class WorldBorderTask extends BukkitRunnable {
    private World world;
    private WorldBorder wb;

    public WorldBorderTask(UHCPlugin plugin) {
        this.world = Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc"));
        this.wb = world.getWorldBorder();
    }

    @Override
    public void run() {
        this.wb.setSize(wb.getSize() - 1);
    }
}
