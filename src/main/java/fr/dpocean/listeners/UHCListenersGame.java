package fr.dpocean.listeners;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import fr.dpocean.UHCPlugin;

public class UHCListenersGame implements Listener {
    private UHCPlugin plugin;
    private Map<Material,Material> materialsOre = new Hashtable<Material,Material>();
    private Material[] craftPickaxes = {Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE};
    private Material[] animalsDrops = {Material.COOKED_BEEF, Material.ARROW, Material.STRING, Material.LEATHER};

    public UHCListenersGame(UHCPlugin plugin) {
        this.plugin = plugin;
        this.materialsOre.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        this.materialsOre.put(Material.IRON_ORE, Material.IRON_INGOT);
    }

    private void breakTree(Block log) {
        log.breakNaturally();
        String uhcWorldName = plugin.getConfig().getConfigurationSection("worlds").getString("uhc");
        if (Bukkit.getWorld(uhcWorldName).getBlockAt(log.getX(), log.getY() + 1, log.getZ()).getType() == Material.LOG) {
            breakTree(Bukkit.getWorld(uhcWorldName).getBlockAt(log.getX(), log.getY() + 1, log.getZ()));
        }
        if (Bukkit.getWorld(uhcWorldName).getBlockAt(log.getX(), log.getY() - 1, log.getZ()).getType() == Material.LOG) {
            breakTree(Bukkit.getWorld(uhcWorldName).getBlockAt(log.getX(), log.getY() - 1, log.getZ()));
        }
    }

    @EventHandler
    public void onPlayerBrokeBlock(BlockBreakEvent event) {
        if (plugin.isGame) {
            if (event.getBlock().getType() == Material.LOG) {
                breakTree(event.getBlock());
            }
            if (event.getBlock().getType() == Material.LEAVES) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                Random rand = new Random();
                int dropChance = rand.nextInt(100);
                if (dropChance >= 65) {
                    Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
                }
            }
            if (event.getBlock().getType() == Material.IRON_ORE || event.getBlock().getType() == Material.GOLD_ORE) {
                for (Material pickaxe : craftPickaxes) {
                    if (event.getPlayer().getItemInHand().getType() == pickaxe) {
                        event.setCancelled(true);
                        Material material = event.getBlock().getType();
                        event.getBlock().setType(Material.AIR);
                        Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).dropItemNaturally(event.getBlock().getLocation(), new ItemStack(materialsOre.get(material), 2));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLeavesBroke(LeavesDecayEvent event) {
        if (plugin.isGame) {
            event.setCancelled(false);
            event.getBlock().setType(Material.AIR);
            Random rand = new Random();
            int dropChance = rand.nextInt(100);
            if (dropChance >= 65) {
                Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.APPLE, 1));
            }
        }
    }

    @EventHandler
    public void onPlayerKillAnimals(EntityDeathEvent event) {
        if (plugin.isGame) {
            if ((Player) event.getEntity() instanceof Player) {
                return;
            }
            if ((Animals) event.getEntity() instanceof Animals) {
                Animals animal = (Animals) event.getEntity();
                event.getDrops().clear();
                Random rand = new Random();
                for (Material drop : animalsDrops) {
                    int randInt = rand.nextInt(2);
                    if (randInt != 0) {
                        Bukkit.getWorld(plugin.getConfig().getConfigurationSection("worlds").getString("uhc")).dropItemNaturally(animal.getLocation(), new ItemStack(drop, randInt));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCraftPickaxe(CraftItemEvent event) {
        if (plugin.isGame) {
            for (Material pickaxe : craftPickaxes) {
                if (event.getInventory().getResult().getType() == pickaxe) {
                    event.getCurrentItem().addEnchantment(Enchantment.DIG_SPEED, 3);
                }
            }
        }
    }
}
