package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComHealth implements CommandExecutor
{

    private Reference reference = Core.instance.getReference();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("health"))
        {
            Player p = (Player) commandSender;
            if (strings.length == 0)
            {
                p.sendMessage("§c/h <Player>");
            }
            else
            {
                @SuppressWarnings("deprecation") Player t = Bukkit.getPlayer(strings[0]);
                if (t == null)
                {
                    p.sendMessage("Player isn't online");
                }
                else
                {
                    p.sendMessage(reference.GAME + "§a" + t.getName() + "§e is currently at §a" + ((int)t.getHealth()) + "§4❤");
                }
            }
        }
        return false;
    }
}
