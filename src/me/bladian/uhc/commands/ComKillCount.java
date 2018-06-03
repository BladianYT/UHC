package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComKillCount implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("kc"))
        {
            if (strings.length == 0)
            {
                commandSender.sendMessage(reference.ERROR + "§c/kc <name>");
                return true;
            }
            if (gameManager.getGameState() != GameState.STARTED)
            {
                commandSender.sendMessage(reference.ERROR + "§cGame hasn't started yet");
                return true;
            }
            Player t = Bukkit.getPlayer(strings[0]);
            if(t == null)
            {
                commandSender.sendMessage(reference.ERROR + "§cPlayer isn't online");
                return true;
            }
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(t.getUniqueId());
            if(uhcPlayer.getPlayerState() != PlayerState.INGAME)
            {
                commandSender.sendMessage(reference.ERROR + "§cPlayer isn't in the game ");
                return true;
            }
            commandSender.sendMessage("§c" + t.getName() + "'s Kills: " + uhcPlayer.getKills());
        }
        return false;
    }
}
