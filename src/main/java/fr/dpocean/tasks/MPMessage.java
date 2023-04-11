package fr.dpocean.tasks;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MPMessage extends BukkitRunnable {
    private Player ply;

    public MPMessage(Player ply) {
        this.ply = ply;
    }

    @Override
    public void run() {
        ply.sendMessage(ChatColor.YELLOW + "You are no longer invincible!");
    }
}
