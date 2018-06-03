package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
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


public class ComXAlerts implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("xAlerts"))
        {
            Player p = (Player) commandSender;
            if(p.hasPermission("rank.host"))
            {
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                if(uhcPlayer.isXrayAlerts())
                {
                    uhcPlayer.setXrayAlerts(false);
                    p.sendMessage(reference.MODERATOR + "§fYou've§c disabled§f your§d X-Ray Alerts");
                }
                else
                {
                    uhcPlayer.setXrayAlerts(true);
                    p.sendMessage(reference.MODERATOR + "§fYou've§a enabled§f your§d X-Ray Alerts");
                }
            }
        }
        return false;
    }
}
