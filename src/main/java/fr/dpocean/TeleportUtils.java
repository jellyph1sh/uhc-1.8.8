package fr.dpocean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TeleportUtils {

    public static void teleportUHCAllPlayers(List<Player> players, String uhcName, int limit) {
        for (Player player : players) {
            player.teleport(generateLocation(Bukkit.getWorld(uhcName), player, limit));
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
        }
    }

    public static void teleportLobbyAllPlayers(String worldName) {
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

    public static HashSet<Material> badBlocks = new HashSet<Material>();

    static {
        badBlocks.add(Material.LAVA);
        badBlocks.add(Material.FIRE);
        badBlocks.add(Material.STONE);
        badBlocks.add(Material.WATER);
        badBlocks.add(Material.STATIONARY_WATER);
        badBlocks.add(Material.STATIONARY_LAVA);
        badBlocks.add(Material.SAND);
        badBlocks.add(Material.GRAVEL);
    }

    public static Location generateLocation(World world, Player player, int limit) {
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

        return randomLocation;
    }

    public static boolean isLocationSafe(Location location) {
        int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();

        Block block = location.getWorld().getBlockAt(x, y, z);
        Block below = location.getWorld().getBlockAt(x, y - 1, z);
        Block above = location.getWorld().getBlockAt(x, y + 1, z);

        return !(badBlocks.contains(below.getType()) || (block.getType().isSolid()) || (above.getType().isSolid()));
    }
}
