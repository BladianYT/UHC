package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComMaxPlayers implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("maxplayers"))
        {
            if(!commandSender.hasPermission("rank.host"))
            {
                return true;
            }
            if(strings.length == 0)
            {
                commandSender.sendMessage(reference.ERROR + "§c/maxplayers <amount>");
                return true;
            }
            String amountS = strings[0];
            if(!util.isNumeric(amountS))
            {
                commandSender.sendMessage(reference.ERROR + "§cAmount must be a number");
                return true;
            }
            int amount = Integer.parseInt(amountS);
            gameManager.setMaxPlayers(amount);
            commandSender.sendMessage(reference.GAME + "Set the max amount of players to§7 " + amount);
        }
        return false;
    }
}
