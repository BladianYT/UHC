package me.bladian.uhc.commands.editor;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComWorldEditor implements CommandExecutor
{

    private final Core core = Core.instance;

    private Inventories inventories = core.getInventories();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("worldEditor"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                Player p = (Player) commandSender;
                inventories.worldEditor(p);
            }
        }
        return false;
    }
}
