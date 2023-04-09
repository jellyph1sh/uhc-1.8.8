package fr.dpocean;

import java.io.File;

import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;

public class WorldUtils {

    public static World createWorld(String name) {
        WorldCreator wc = new WorldCreator(name);
        wc.environment(Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        wc.generateStructures(false);
        return wc.createWorld();
    }

    public static void setWorldBorderSettings(World world, int limit) {
        WorldBorder wb = world.getWorldBorder();
        wb.setSize(limit);
    }

    public static void setLobbySettings(World lobby) {
        lobby.setAmbientSpawnLimit(0);
        lobby.setAnimalSpawnLimit(0);
        lobby.setDifficulty(Difficulty.PEACEFUL);
        lobby.setMonsterSpawnLimit(0);
        lobby.setPVP(false);
        lobby.setGameRuleValue("doDaylightCycle", "false");
        lobby.setGameRuleValue("doWeatherCycle", "false");
    }

    public static void setUHCWorldSettings(World uhc) {
        uhc.setAmbientSpawnLimit(0);
        uhc.setDifficulty(Difficulty.HARD);
        uhc.setMonsterSpawnLimit(0);
        uhc.setPVP(false);
        uhc.setGameRuleValue("doDaylightCycle", "false");
        uhc.setGameRuleValue("doWeatherCycle", "false");
        uhc.setGameRuleValue("naturalRegeneration", "false");
        uhc.setGameRuleValue("doImmediateRespawn", "false");
    }

    public static boolean deleteWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return path.delete();
    }

}
