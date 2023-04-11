package fr.dpocean;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardUHC {
    private UHCPlugin plugin;
    private Scoreboard scoreboard;

    public ScoreboardUHC(UHCPlugin plugin) {
        this.plugin = plugin;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective(ChatColor.RED + "DPOCEAN | UHC", "uhcscoreboard");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score score = obj.getScore(" ");
        score.setScore(1);

        score = obj.getScore(" ");
        score.setScore(3);

        score = obj.getScore(ChatColor.YELLOW + "IP : uhc.dpocean.com");
        score.setScore(2);
    }

    public void setScoreboard(Player ply) {
        ply.setScoreboard(scoreboard);
    }
}
