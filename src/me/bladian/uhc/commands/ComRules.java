package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.Reference;
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


public class ComRules implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("rules"))
        {
            if (!commandSender.isOp())
            {
                return true;
            }
            Player p = (Player) commandSender;
            if(strings.length == 0)
            {
                p.sendMessage(reference.ERROR + "/rules <on/off>");
                return true;
            }
            if(strings[0].equalsIgnoreCase("on"))
            {
                gameManager.setRules(true);
                p.sendMessage(reference.GAME + "§7Rules§f have been§a enabled");
                return true;
            }
            if(strings[0].equalsIgnoreCase("off"))
            {
                gameManager.setRules(false);
                p.sendMessage(reference.GAME + "§7Rules§f have been§c disabled");
            }
        }
        return false;
    }
}
