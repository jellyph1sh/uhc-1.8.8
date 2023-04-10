package fr.dpocean;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import fr.dpocean.commands.UHCStart;
import fr.dpocean.commands.UHCStop;
import fr.dpocean.listeners.UHCListenersGame;
import fr.dpocean.listeners.UHCListenersGameManager;
import fr.dpocean.listeners.UHCListenersLobby;
import fr.dpocean.schedulers.UHCScheduler;
import fr.dpocean.tasks.RestartGame;

public class UHCPlugin extends JavaPlugin {
    public boolean isGame = false;
    public BukkitTask restartTask = null;
    public List<Player> players = null;
    private UHCScheduler scheduler;
    public String messagePrefix;

    public void Log(String msg) {
        getLogger().info(msg);
    }

    public void broadcastMessage(ChatColor color, String message) {
        Bukkit.broadcastMessage(messagePrefix + color + message);
    }

    @Override
    public void onEnable() {
        Log("Loading config!");
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        this.messagePrefix = ChatColor.BLUE + getConfig().getString("prefix") + " ";
        Log("Config loaded!");

        Log("Loading commands!");
        this.getCommand("uhcstart").setExecutor(new UHCStart(this));
        this.getCommand("uhcstop").setExecutor(new UHCStop(this));
        Log("Commands loaded!");

        Log("Loading listeners!");
        getServer().getPluginManager().registerEvents(new UHCListenersGameManager(this), this);
        getServer().getPluginManager().registerEvents(new UHCListenersLobby(this), this);
        getServer().getPluginManager().registerEvents(new UHCListenersGame(this), this);
        Log("Listeners loaded!");

        Log("Set world lobby parameters!");
        WorldUtils.setLobbyWorldParameters(Bukkit.getWorld(getConfig().getConfigurationSection("worlds").getString("lobby")));
        
        Log("Instance game scheduler!");
        this.scheduler = new UHCScheduler(this);
    }

    public void startGame() {
        broadcastMessage(ChatColor.YELLOW, "Game is starting!");
        ConfigurationSection worlds = getConfig().getConfigurationSection("worlds");
        World uhcWorld = WorldUtils.createWorld(worlds.getString("uhc"));
        WorldUtils.setWorldBorderParameters(uhcWorld, getConfig().getInt("border-limit"));
        WorldUtils.setUHCWorldParameters(uhcWorld);
        Bukkit.unloadWorld(Bukkit.getWorld(worlds.getString("lobby")), false);
        this.players = Bukkit.getWorld(worlds.getString("lobby")).getPlayers();
        TeleportUtils.teleportUHCAllPlayers(players, uhcWorld.getName(), getConfig().getInt("border-limit"));
        isGame = true;
        broadcastMessage(ChatColor.GREEN, "Game started! GOOD LUCK!");
        scheduler.pvpScheduler();
    }

    public void stopGameLater() {
        scheduler.stopGameLater();
        broadcastMessage(ChatColor.YELLOW, "Go back to lobby in 30 seconds!");
    }

    public void stopGame() {
        ConfigurationSection worlds = getConfig().getConfigurationSection("worlds");
        World worldUhc = Bukkit.getWorld(worlds.getString("uhc"));
        if (worldUhc == null) {
            return ;
        }
        TeleportUtils.teleportLobbyAllPlayers(worlds.getString("lobby"));
        this.players = null;
        Bukkit.unloadWorld(worldUhc, false);
        File worldFile = worldUhc.getWorldFolder();
        WorldUtils.deleteWorld(worldFile);
        isGame = false;
        if (Bukkit.getOnlinePlayers().size() >= getConfig().getInt("start-game") && !isGame && restartTask == null) {
            broadcastMessage(ChatColor.YELLOW, "A new game start in 1 minute!");
            this.restartTask = new RestartGame(this).runTaskLater(this, 20*60);
        }
    }
}
