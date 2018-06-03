package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.manager.TeamManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.Reference;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComBackPack implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("bp"))
        {
            if(gameManager.getGameState() != GameState.STARTED)
            {
                commandSender.sendMessage(reference.ERROR + "§cGame hasn't started");
                return true;
            }
            if(!Scenario.BACKPACKS.isEnabled())
            {
                commandSender.sendMessage(reference.ERROR + "§cBackPacks isn't enabled");
                return true;
            }
            Player p = (Player) commandSender;
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() != PlayerState.INGAME)
            {
                return true;
            }
            Team team = teamManager.getTeams().get(uhcPlayer.getTeamNumber());
            p.openInventory(team.getInventory());
        }
        return false;
    }
}
