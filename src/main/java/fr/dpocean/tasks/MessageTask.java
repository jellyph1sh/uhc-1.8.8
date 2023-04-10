package fr.dpocean.tasks;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import fr.dpocean.UHCPlugin;

public class MessageTask extends BukkitRunnable {
    private UHCPlugin plugin;
    private ChatColor color;
    private String msg;

    public MessageTask(UHCPlugin plugin, ChatColor color, String msg) {
        this.plugin = plugin;
        this.color = color;
        this.msg = msg;
    }

    @Override
    public void run() {
        plugin.broadcastMessage(color, msg);
    }
}
