package fr.dpocean.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import fr.dpocean.UHCPlugin;

public class UHCListenersLobby implements Listener {
    private UHCPlugin plugin;

    public UHCListenersLobby(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if (!plugin.isGame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBrokeBlock(BlockBreakEvent event) {
        if (!plugin.isGame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageEvent event) {
        if (!plugin.isGame) {
            event.setCancelled(true);
        } 
    }
}
