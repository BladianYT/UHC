package me.bladian.uhc.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComSpec implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("spec"))
        {
            if(strings.length == 0)
            {

            }
        }
        return false;
    }
}
