package me.bladian.uhc.config;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.*;
import me.bladian.uhc.scenarios.Scenario;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComConfig implements CommandExecutor
{
    private Core core = Core.instance;
    private PlayerManager playerManager = core.getPlayerManager();
    private ConfigManager configManager = core.getConfigManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(strings.length == 0)
        {
            commandSender.sendMessage("§8§m------------------------");
            commandSender.sendMessage("§6FAZON.GG UHC CONFIG");
            commandSender.sendMessage("");
            String host = "";
            if(playerManager.getHost() != null)
            {
                host = Bukkit.getOfflinePlayer(playerManager.getHost()).getName();
            }
            else
            {
                host = "Not Set";
            }
            commandSender.sendMessage("§bHost:§f " + host);
            commandSender.sendMessage("§bMax Players:§f " + Bukkit.getMaxPlayers());
            commandSender.sendMessage("§bTeam Type:§f " + WordUtils.capitalize(String.valueOf(gameManager.getTeamType())));
            String x = "";
            x = gameManager.getTeamType() == TeamType.TEAMS ? "To" + teamManager.getTeamSize() : "N/A";
            commandSender.sendMessage("§bTeam Size:§f " + WordUtils.capitalize(x));
            commandSender.sendMessage("§bNether:§f " + WordUtils.capitalize(String.valueOf(configManager.isNether())));
            commandSender.sendMessage("§bSpeed 1: §f" + WordUtils.capitalize(String.valueOf(configManager.isSpeed1())));
            commandSender.sendMessage("§bSpeed 2: §f" + WordUtils.capitalize(String.valueOf(configManager.isSpeed2())));
            commandSender.sendMessage("§bStrength 1: §f" + WordUtils.capitalize(String.valueOf(configManager.isStrength1())));
            commandSender.sendMessage("§bStrength 2: §f" + WordUtils.capitalize(String.valueOf(configManager.isStrength2())));
            commandSender.sendMessage("§bEnderPearl Damage:§f " + WordUtils.capitalize(String.valueOf(configManager.isEnderpearlDamage())));
            commandSender.sendMessage("§bGod Apples:§f " + WordUtils.capitalize(String.valueOf(configManager.isGodApples())));
            commandSender.sendMessage("§bHorses:§f " + WordUtils.capitalize(String.valueOf(Scenario.HORSELESS.isEnabled() ? "False" : "True")));
            commandSender.sendMessage("§bShears:§f " + WordUtils.capitalize(String.valueOf(configManager.isShears())));
            commandSender.sendMessage("§bHorse Healing:§f " + WordUtils.capitalize(String.valueOf(configManager.isHorseHealing())));
            commandSender.sendMessage("§bStats:§f ");
            commandSender.sendMessage("§bStarter Food:§f 16 Steak");
            commandSender.sendMessage("§bFinal Heal:§f " + gameManager.getHealTime() + " minutes");
            commandSender.sendMessage("§bPVP:§f " + gameManager.getPvpTime() + " minutes");
            commandSender.sendMessage("§bPerma-Day:§f " + gameManager.getBorderTime() + " minutes");
            commandSender.sendMessage("§bBorder Time:§f " + (gameManager.getBorderTime()+5)  + " minutes");
            commandSender.sendMessage("§bWorld Size:§f " + gameManager.getBorderRadius() + "x" + gameManager.getBorderRadius());
            commandSender.sendMessage("§bWorld Shape:§f Square");
            commandSender.sendMessage("§bCurrent Border:§f " + gameManager.getBorderRadius());
            commandSender.sendMessage("§8§m------------------------");
        }
        return false;
    }
}
