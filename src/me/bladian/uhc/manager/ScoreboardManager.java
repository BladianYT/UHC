package me.bladian.uhc.manager;

import me.bladian.uhc.Core;
import me.bladian.uhc.config.BorderSize;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by BladianYT
 */
public class ScoreboardManager
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();


    private final DecimalFormat df = new DecimalFormat("###,##0.###");

    public String ARROW = "§8»";

    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat f1 = new SimpleDateFormat("dd/MM/yyyy");

    private String borderInfo;

    public ScoreboardManager()
    {
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        f1.setTimeZone(TimeZone.getTimeZone("UTC"));

        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                borderInfo = getBorderInfo();
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    updateScoreboard(p, gameManager.getGameState(), uhcPlayer);
                    updateIP(p);
                }
                if(infoChange == 15)
                {
                    infoChange = -1;
                }
                infoChange++;
            }
        }.runTaskTimer(core, 0L, 20L);
    }

    public void createScoreboard(Player p, UHCPlayer uhcPlayer)
    {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = scoreboard.registerNewObjective("practice", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        p.setScoreboard(scoreboard);

        changeScoreboard(p, gameManager.getGameState(), uhcPlayer);
    }

    public void changeScoreboard(Player p, GameState gameState, UHCPlayer uhcPlayer)
    {
        Scoreboard scoreboard = p.getScoreboard();
        for (Team team : scoreboard.getTeams())
        {
            team.unregister();
        }
        Objective oldObj = scoreboard.getObjective(DisplaySlot.SIDEBAR);
        oldObj.unregister();
        if (scoreboard.getObjective(DisplaySlot.BELOW_NAME) != null)
        {
            scoreboard.getObjective(DisplaySlot.BELOW_NAME).unregister();
        }
        Objective obj = scoreboard.registerNewObjective("practice", "dummy");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        //obj.setDisplayName("§4§lFAZON.GG §7§l:|: §6§lUHC");
        obj.setDisplayName("§6§lFAZON §f§lUHC");
        if (gameState == GameState.STARTED)
        {
            Objective healthPList = scoreboard.registerNewObjective("h", "health");
            healthPList.setDisplaySlot(DisplaySlot.PLAYER_LIST);

            Objective healthName = scoreboard.registerNewObjective("h1", "health");

            healthName.setDisplayName(ChatColor.DARK_RED + "❤");
            healthName.setDisplaySlot(DisplaySlot.BELOW_NAME);

            if (gameManager.getTeamType() == TeamType.SOLO)
            {

                Team newLine = scoreboard.registerNewTeam(ChatColor.GREEN.toString());
                newLine.addEntry("§7§m--------");
                newLine.setSuffix("--------");
                obj.getScore("§7§m--------").setScore(10);

                Team timer = scoreboard.registerNewTeam(ChatColor.ITALIC.toString());
                timer.addEntry("§fGame Time: ");
                timer.setSuffix("§600:00");
                obj.getScore("§fGame Time: ").setScore(9);

                Team border = scoreboard.registerNewTeam(ChatColor.DARK_BLUE.toString());
                border.addEntry("§fBorder:§6 ");
                border.setSuffix(String.valueOf(gameManager.getBorderRadius()));
                obj.getScore("§fBorder:§6 ").setScore(8);

                Team newLine3 = scoreboard.registerNewTeam(ChatColor.DARK_GRAY.toString());
                newLine3.addEntry("§5");
                obj.getScore("§5").setScore(7);

                Team kill = scoreboard.registerNewTeam(ChatColor.YELLOW.toString());
                kill.addEntry("§fKills: ");
                kill.setSuffix("§6" + 0);
                obj.getScore("§fKills: ").setScore(6);

                Team alive = scoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString());
                alive.addEntry("§fPlayers: ");
                alive.setSuffix("§6" + gameManager.getPlayers().size());
                obj.getScore("§fPlayers: ").setScore(5);

                Team specs = scoreboard.registerNewTeam(ChatColor.MAGIC.toString());
                specs.addEntry("§fSpectators: ");
                int total = playerManager.getModerators().size() + playerManager.getSpectators().size();
                specs.setSuffix("§6" + total);
                obj.getScore("§fSpectators: ").setScore(4);

                Team newLine2 = scoreboard.registerNewTeam(ChatColor.BOLD.toString());
                newLine2.addEntry("§l");
                obj.getScore("§l").setScore(3);

                Team website = scoreboard.registerNewTeam(ChatColor.STRIKETHROUGH.toString());
                website.addEntry("§6");
                website.setSuffix(message);
                obj.getScore("§6").setScore(2);

                Team footer = scoreboard.registerNewTeam(ChatColor.DARK_AQUA.toString());
                footer.addEntry("§7§m-------");
                footer.setSuffix("---------");
                obj.getScore("§7§m-------").setScore(1);
            }
            else
            {
                Team newLine = scoreboard.registerNewTeam(ChatColor.GREEN.toString());
                newLine.addEntry("§7§m--------");
                newLine.setSuffix("--------");
                obj.getScore("§7§m--------").setScore(12);

                Team timer = scoreboard.registerNewTeam(ChatColor.ITALIC.toString());
                timer.addEntry("§fGame Time: ");
                timer.setSuffix("§600:00");
                obj.getScore("§fGame Time: ").setScore(11);

                Team border = scoreboard.registerNewTeam(ChatColor.DARK_BLUE.toString());
                border.addEntry("§fBorder:§6 ");
                border.setSuffix(String.valueOf(gameManager.getBorderRadius()));
                obj.getScore("§fBorder:§6 ").setScore(10);

                Team newLine3 = scoreboard.registerNewTeam(ChatColor.DARK_GRAY.toString());
                newLine3.addEntry("§5");
                obj.getScore("§5").setScore(9);

                Team kill = scoreboard.registerNewTeam(ChatColor.YELLOW.toString());
                kill.addEntry("§fKills: ");
                kill.setSuffix("§6" + 0);
                obj.getScore("§fKills: ").setScore(8);

                Team teamK = scoreboard.registerNewTeam(ChatColor.RESET.toString());
                teamK.addEntry("§fTeam Kills: ");
                teamK.setSuffix("§6 0");
                if (teamManager.getTeams().containsKey(uhcPlayer.getTeamNumber()))
                {
                    teamK.setSuffix("§6" + teamManager.getTeams().get(uhcPlayer.getTeamNumber()).getKills());
                }
                obj.getScore("§fTeam Kills: ").setScore(7);

                Team alive = scoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString());
                alive.addEntry("§fPlayers: ");
                alive.setSuffix("§6" + gameManager.getPlayers().size());
                obj.getScore("§fPlayers: ").setScore(6);

                Team team = scoreboard.registerNewTeam(ChatColor.DARK_RED.toString());
                team.addEntry("§fTeams: ");
                team.setSuffix("§6" + teamManager.getTeams().size());
                obj.getScore("§fTeams: ").setScore(5);

                Team specs = scoreboard.registerNewTeam(ChatColor.MAGIC.toString());
                specs.addEntry("§fSpectators: ");
                int total = playerManager.getModerators().size() + playerManager.getSpectators().size();
                specs.setSuffix("§6" + total);
                obj.getScore("§fSpectators: ").setScore(4);

                Team newLine7 = scoreboard.registerNewTeam(ChatColor.BOLD.toString());
                newLine7.addEntry("§l");
                obj.getScore("§l").setScore(3);

                Team website = scoreboard.registerNewTeam(ChatColor.STRIKETHROUGH.toString());
                website.addEntry("§6");
                website.setSuffix(message);
                obj.getScore("§6").setScore(2);

                Team footer = scoreboard.registerNewTeam(ChatColor.DARK_AQUA.toString());
                footer.addEntry("§7§m-------");
                footer.setSuffix("---------");
                obj.getScore("§7§m-------").setScore(1);
            }
        }
        else if (gameState == GameState.LOBBY)
        {

            Team newLine = scoreboard.registerNewTeam(ChatColor.GREEN.toString());
            newLine.addEntry("§7§m--------");
            newLine.setSuffix("--------");
            obj.getScore("§7§m--------").setScore(10);

            Team timer = scoreboard.registerNewTeam(ChatColor.ITALIC.toString());
            timer.addEntry("§fPlayers: ");
            timer.setSuffix("§f" + Bukkit.getOnlinePlayers().size() + "/" + gameManager.getMaxPlayers());
            obj.getScore("§fPlayers: ").setScore(9);

            Team newLine1 = scoreboard.registerNewTeam(ChatColor.RED.toString());
            newLine1.addEntry("§f");
            obj.getScore("§f").setScore(8);

            Team list = scoreboard.registerNewTeam(ChatColor.DARK_BLUE.toString());
            list.addEntry("§f/list");
            obj.getScore("§f/list").setScore(7);

            Team scenarios = scoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString());
            scenarios.addEntry("§f/scenarios");
            obj.getScore("§f/scenarios").setScore(6);

            Team config = scoreboard.registerNewTeam(ChatColor.BOLD.toString());
            config.addEntry("§f/config");
            obj.getScore("§f/config").setScore(5);

            Team explain = scoreboard.registerNewTeam(ChatColor.DARK_RED.toString());
            explain.addEntry("§f/helpop");
            obj.getScore("§f/helpop").setScore(4);

            Team newLine3 = scoreboard.registerNewTeam(ChatColor.MAGIC.toString());
            newLine3.addEntry("§5");
            obj.getScore("§5").setScore(3);

            Team website = scoreboard.registerNewTeam(ChatColor.STRIKETHROUGH.toString());
            website.addEntry("§6");
            website.setSuffix(message);
            obj.getScore("§6").setScore(2);

            Team footer = scoreboard.registerNewTeam(ChatColor.DARK_AQUA.toString());
            footer.addEntry("§7§m------");
            footer.setSuffix("----------");
            obj.getScore("§7§m------").setScore(1);
        }
        else if (gameState == GameState.SCATTERING)
        {
            Team newLine = scoreboard.registerNewTeam(ChatColor.GREEN.toString());
            newLine.addEntry("§7§m--------");
            newLine.setSuffix("--------");
            obj.getScore("§7§m--------").setScore(8);

            Team timer = scoreboard.registerNewTeam(ChatColor.ITALIC.toString());
            timer.addEntry("§fTime Left: ");
            timer.setSuffix("§6" + gameManager.getScatterTimeLeft());
            obj.getScore("§fTime Left: ").setScore(7);

            Team newLine1 = scoreboard.registerNewTeam(ChatColor.RED.toString());
            newLine1.addEntry("§f");
            obj.getScore("§f").setScore(6);

            Team list = scoreboard.registerNewTeam(ChatColor.DARK_BLUE.toString());
            list.addEntry("§fScattering: ");
            list.setSuffix("§6" + (gameManager.getPlayers().size() - gameManager.getScatterTimes()));
            obj.getScore("§fScattering: ").setScore(5);

            Team scattered = scoreboard.registerNewTeam(ChatColor.LIGHT_PURPLE.toString());
            scattered.addEntry("§fScattered: ");
            scattered.setSuffix("§6" + gameManager.getScatterTimes());
            obj.getScore("§fScattered: ").setScore(4);

            Team newLine3 = scoreboard.registerNewTeam(ChatColor.BOLD.toString());
            newLine3.addEntry("§5");
            obj.getScore("§5").setScore(3);

            Team website = scoreboard.registerNewTeam(ChatColor.STRIKETHROUGH.toString());
            website.addEntry("§6");
            website.setSuffix(message);
            obj.getScore("§6").setScore(2);

            Team footer = scoreboard.registerNewTeam(ChatColor.DARK_AQUA.toString());
            footer.addEntry("§7§m------");
            footer.setSuffix("----------");
            obj.getScore("§7§m------").setScore(1);
        }
    }

    public void updateScoreboard(Player p, GameState gameState, UHCPlayer uhcPlayer)
    {
        Scoreboard scoreboard = p.getScoreboard();

        if (gameState == GameState.LOBBY)
        {
            Team pl = scoreboard.getTeam(ChatColor.ITALIC.toString());
            pl.setSuffix("§6" + Bukkit.getOnlinePlayers().size() + "/" + gameManager.getMaxPlayers());
        }
        if (gameState == GameState.SCATTERING)
        {
            Team i = scoreboard.getTeam(ChatColor.ITALIC.toString());
            i.setSuffix("§6" + gameManager.getScatterTimeLeft());

            Team pl = scoreboard.getTeam(ChatColor.DARK_BLUE.toString());
            pl.setSuffix("§6" + (gameManager.getPlayers().size() - gameManager.getScatterTimes()));

            Team s = scoreboard.getTeam(ChatColor.LIGHT_PURPLE.toString());
            s.setSuffix("§6" + gameManager.getScatterTimes());
        }
        if (gameState == GameState.STARTED)
        {
            Team pl = scoreboard.getTeam(ChatColor.ITALIC.toString());
            pl.setSuffix("§6" + gameManager.getFormatTime());

            Team border = scoreboard.getTeam(ChatColor.DARK_BLUE.toString());
            border.setSuffix(gameManager.getBorderRadius() + " " + borderInfo);

            Team kills = scoreboard.getTeam(ChatColor.YELLOW.toString());
            kills.setSuffix("§6" + uhcPlayer.getKills());

            Team players = scoreboard.getTeam(ChatColor.LIGHT_PURPLE.toString());
            players.setSuffix("§6" + gameManager.getPlayers().size());

            Team specs = scoreboard.getTeam(ChatColor.MAGIC.toString());
            int total = playerManager.getModerators().size() + playerManager.getSpectators().size();
            specs.setSuffix("§6" + total);

            if (gameManager.getTeamType() == TeamType.TEAMS)
            {
                Team team = scoreboard.getTeam(ChatColor.DARK_RED.toString());
                team.setSuffix("§6" + teamManager.getTeams().size());

                if (teamManager.getTeams().containsKey(uhcPlayer.getTeamNumber()))
                {
                    Team teamK = scoreboard.getTeam(ChatColor.RESET.toString());
                    teamK.setSuffix("§6" + teamManager.getTeams().get(uhcPlayer.getTeamNumber()).getKills());
                }
            }
        }
    }

    int infoChange = -1;

    private String message = "fazon.gg";

    private void updateIP(Player p)
    {
        Scoreboard scoreboard = p.getScoreboard();
        Team pl = scoreboard.getTeam(ChatColor.STRIKETHROUGH.toString());
        if (infoChange == 0)
        {
            pl.setSuffix("fazon.gg");
            message = "fazon.gg";
        }
        else if (infoChange == 5)
        {
            pl.setSuffix("store.fazon.gg");
            message = "store.fazon.gg";
        }
        else if (infoChange == 10)
        {
            pl.setSuffix("@FazonGG");
            message = "@FazonGG";
        }

    }

    public String getBorderInfo() {
        int timer = gameManager.getTimer();
        int borderNumber = gameManager.getBorderNumber();
        if(gameManager.getBorderRadius() == 25)
        {
            return "";
        }
        else if (timer <= gameManager.getBorderTimeInSeconds(borderNumber) - (4*60) && timer >= gameManager.getBorderTimeInSeconds(borderNumber) - (5*60))
        {
            return "§7(§c5m§7)";
        }
        else if (timer <= gameManager.getBorderTimeInSeconds(borderNumber) - (3*60) && timer >= gameManager.getBorderTimeInSeconds(borderNumber) - (4*60))
        {
            return "§7(§c4m§7)";
        }
        else if (timer <= gameManager.getBorderTimeInSeconds(borderNumber) - (2*60) && timer >= gameManager.getBorderTimeInSeconds(borderNumber) - (3*60))
        {
            return "§7(§c3m§7)";
        }
        else if (timer <= gameManager.getBorderTimeInSeconds(borderNumber) - 60 && timer >= gameManager.getBorderTimeInSeconds(borderNumber) - (2*60))
        {
            return "§7(§c2m§7)";
        }
        else if (timer <= gameManager.getBorderTimeInSeconds(borderNumber) - 1 && timer >= gameManager.getBorderTimeInSeconds(borderNumber) - 60)
        {
            int number = gameManager.getBorderTimeInSeconds(borderNumber) - timer;
            return "§7(§c" + number + "s§7)";
        }
        return "";
    }

}