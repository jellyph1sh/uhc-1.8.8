package fr.dpocean;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
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

public class UHCPlugin extends JavaPlugin {
    public boolean isGame = false;
    public BukkitTask restartTask = null;
    public List<Player> players = null;
    private UHCScheduler scheduler;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        Bukkit.getWorld(getConfig().getConfigurationSection("worlds").getString("lobby")).setStorm(false);

        this.getCommand("uhcstart").setExecutor(new UHCStart(this));
        this.getCommand("uhcstop").setExecutor(new UHCStop(this));

        getServer().getPluginManager().registerEvents(new UHCListenersGameManager(this), this);
        getServer().getPluginManager().registerEvents(new UHCListenersLobby(this), this);
        getServer().getPluginManager().registerEvents(new UHCListenersGame(this), this);

        WorldUtils.setLobbySettings(Bukkit.getWorld(getConfig().getConfigurationSection("worlds").getString("lobby")));
        this.scheduler = new UHCScheduler(this);
    }

    public void startGame() {
        Bukkit.broadcastMessage("UHC is starting!");
        ConfigurationSection worlds = getConfig().getConfigurationSection("worlds");
        World uhcWorld = WorldUtils.createWorld(worlds.getString("uhc"));
        WorldUtils.setWorldBorderSettings(uhcWorld, getConfig().getInt("border-limit"));
        WorldUtils.setUHCWorldSettings(uhcWorld);
        Bukkit.unloadWorld(Bukkit.getWorld(worlds.getString("lobby")), false);
        this.players = Bukkit.getWorld(worlds.getString("lobby")).getPlayers();
        TeleportUtils.teleportUHCAllPlayers(players, uhcWorld.getName(), getConfig().getInt("border-limit"));
        scheduler.pvpScheduler(getServer().getScheduler());
        isGame = true;
        Bukkit.broadcastMessage("Start UHC!");
    }

    public boolean stopGame() {
        getServer().getScheduler().cancelAllTasks();
        ConfigurationSection worlds = getConfig().getConfigurationSection("worlds");
        World worldUhc = Bukkit.getWorld(worlds.getString("uhc"));
        if (worldUhc == null) {
            return true;
        }
        TeleportUtils.teleportLobbyAllPlayers(worlds.getString("lobby"));
        this.players = null;
        Bukkit.unloadWorld(worldUhc, false);
        File worldFile = worldUhc.getWorldFolder();
        WorldUtils.deleteWorld(worldFile);
        Bukkit.broadcastMessage("Stop UHC!");
        isGame = false;
        return false;
    }
}
