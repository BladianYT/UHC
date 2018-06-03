package me.bladian.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created by BladianYT
 */
public class ComChat implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("clearchat"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                for (int i = 0; i < 25; i++)
                {
                    Bukkit.broadcastMessage("");
                }
            }
        }
        return false;
    }
}
