package fr.dpocean.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import fr.dpocean.UHCPlugin;

public class UHCStop implements CommandExecutor {
    private UHCPlugin plugin;

    public UHCStop(UHCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.stopGame()) {
                player.sendMessage("No game in current!");
            };
            player.sendMessage("You stopped the game!");
            if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("start-game") && !plugin.isGame && plugin.restartTask == null) {
                Bukkit.broadcastMessage("A game start in 1 minute!");
                BukkitScheduler scheduler = plugin.getServer().getScheduler();
                plugin.restartTask = scheduler.runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage("A game is starting!");
                        plugin.startGame();
                        plugin.restartTask = null;
                    }
                }, 20*60);
            }
        }
        return true;
    }

}
