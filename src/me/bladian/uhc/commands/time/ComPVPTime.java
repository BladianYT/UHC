package me.bladian.uhc.commands.time;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComPVPTime implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private GameManager gameManager = core.getGameManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("pvpTime"))
        {
            if(commandSender.hasPermission("rank.host"))
            {
                if(strings.length == 0)
                {
                    commandSender.sendMessage(reference.ERROR + "§c/pvpTime <minute>");
                    return true;
                }
                if(!util.isNumeric(strings[0]))
                {
                    commandSender.sendMessage(reference.ERROR + "§cTime has to be a number");
                    return true;
                }
                int pvpTime = Integer.parseInt(strings[0]);
                gameManager.setPvpTime(pvpTime);
                Bukkit.broadcastMessage(reference.GAME + "§7PVP time§f is now set to§7 " + strings[0]);
            }
        }
        return false;
    }
}
