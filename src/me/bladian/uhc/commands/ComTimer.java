package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComTimer implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private GameManager gameManager = core.getGameManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("timer"))
        {
            if(commandSender.isOp())
            {
                if(strings.length < 2)
                {
                    commandSender.sendMessage(reference.ERROR + "§c/timer <set/add/remove> <seconds>");
                    return true;
                }
                if(!util.isNumeric(strings[1]))
                {
                    commandSender.sendMessage(reference.ERROR + "Time has to be a number");
                    return true;
                }
                int x = Integer.parseInt(strings[1]);
                if(strings[0].equalsIgnoreCase("set"))
                {
                    gameManager.setTimer(x);
                    commandSender.sendMessage(reference.GAME + "§7Timer§f is now at §7" + x);
                    return true;
                }
                if(strings[0].equalsIgnoreCase("add"))
                {
                    gameManager.setTimer(x+gameManager.getTimer());
                    commandSender.sendMessage(reference.GAME + "§7" + x + "§f has been§7 added§f to the§7 Timer§f");
                    return true;
                }
                if(strings[0].equalsIgnoreCase("remove"))
                {
                    gameManager.setTimer(gameManager.getTimer()-x);
                    commandSender.sendMessage(reference.GAME + "§7" + x + "§f has been§7 removed§f to the§7 Timer§f");
                    return true;
                }
            }
        }
        return false;
    }
}
