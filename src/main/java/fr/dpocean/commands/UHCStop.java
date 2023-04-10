package fr.dpocean.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dpocean.UHCPlugin;
import fr.dpocean.tasks.RestartGame;

public class UHCStop implements CommandExecutor {
    private UHCPlugin plugin;

    public UHCStop(UHCPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.stopGame();
            player.sendMessage(plugin.messagePrefix + ChatColor.GREEN + "You stopped the game!");
            if (Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("start-game") && !plugin.isGame && plugin.restartTask == null) {
                plugin.broadcastMessage(ChatColor.YELLOW, "A new game start in 1 minute!");
                plugin.restartTask = new RestartGame(plugin).runTaskLater(plugin, 20*60);
            }
        }
        return true;
    }

}
