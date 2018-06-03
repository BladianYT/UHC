package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ComRates implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("rates"))
        {
            Player p = (Player) commandSender;
            if(!p.hasPermission("rank.host"))
            {
                return true;
            }
            if(strings.length < 2)
            {
                p.sendMessage(reference.ERROR + "/rates <apple/flint> <percent>");
                return true;
            }
            if(strings[0].equalsIgnoreCase("apple"))
            {
                if(!util.isNumeric(strings[1]))
                {
                    p.sendMessage(reference.ERROR + "Percent has to be a number");
                    return true;
                }
                int rate = Integer.parseInt(strings[1]);
                gameManager.setAppleRates(rate);
                Bukkit.broadcastMessage(reference.GAME + "§7Apple Rates§f have been changed to§7 " + rate);
            }
            else if(strings[0].equalsIgnoreCase("flint"))
            {
                if(!util.isNumeric(strings[1]))
                {
                    p.sendMessage(reference.ERROR + "Percent has to be a number");
                    return true;
                }
                int rate = Integer.parseInt(strings[1]);
                gameManager.setFlintRates(rate);
                Bukkit.broadcastMessage(reference.GAME + "§7Flint Rates§f have been changed to§7 " + rate);
            }
        }
        return false;
    }
}
