package fr.dpocean.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.dpocean.UHCPlugin;

public class UHCStart implements CommandExecutor {
    private UHCPlugin plugin;

    public UHCStart(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!plugin.isGame) {
                if (plugin.restartTask != null) {
                    plugin.restartTask.cancel();
                    plugin.restartTask = null;
                    player.sendMessage(plugin.messagePrefix + ChatColor.GREEN + "The automatic game start has been cancel!");
                }
                plugin.startGame();
            } else {
                player.sendMessage(plugin.messagePrefix + ChatColor.RED + "A game is already in current!");
            }
        }

        return true;
    }

}
