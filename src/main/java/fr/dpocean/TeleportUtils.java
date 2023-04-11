package fr.dpocean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.dpocean.tasks.MPMessage;

public class TeleportUtils {
    private UHCPlugin plugin;

    public TeleportUtils(UHCPlugin plugin) {
        this.plugin = plugin;
    }

    public void teleportUHCAllPlayers(List<Player> players, String uhcName, int limit) {
        for (Player player : players) {
            player.sendMessage(ChatColor.YELLOW + "You are now invincible for 30 seconds!");
            new MPMessage(player).runTaskLater(plugin, 20 * 30);
            player.setNoDamageTicks(20 * 30);
            player.teleport(generateLocation(Bukkit.getWorld(uhcName), player, limit));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
        }
    }

    public void teleportLobbyAllPlayers(String worldName) {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            player.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
            player.setGameMode(GameMode.SURVIVAL);
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
        }
    }

    public HashSet<Material> badBlocks = new HashSet<Material>();

    {
        badBlocks.add(Material.LAVA);
        badBlocks.add(Material.FIRE);
        badBlocks.add(Material.STONE);
        badBlocks.add(Material.WATER);
        badBlocks.add(Material.STATIONARY_WATER);
        badBlocks.add(Material.STATIONARY_LAVA);
        badBlocks.add(Material.SAND);
        badBlocks.add(Material.GRAVEL);
    }

    public Location generateLocation(World world, Player player, int limit) {
        Random random = new Random();
        int x = random.nextInt(limit/2-1), y = 150, z = random.nextInt(limit/2-1);
        Location randomLocation = new Location(world, x, y, z);

        randomLocation.setY(randomLocation.getWorld().getHighestBlockYAt(randomLocation));

        while (!isLocationSafe(randomLocation)) {
            x = random.nextInt(limit/2-1);
            y = 150;
            z = random.nextInt(limit/2-1);
            randomLocation = new Location(world, x, y, z);

            randomLocation.setY(randomLocation.getWorld().getHighestBlockYAt(randomLocation));
        }

        randomLocation.setY(150);

        return randomLocation;
    }

    public boolean isLocationSafe(Location location) {
        int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return !(badBlocks.contains(below.getType()) || (block.getType().isSolid()) || (above.getType().isSolid()));
    }
}
