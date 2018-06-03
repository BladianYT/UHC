package me.bladian.uhc.scenarios;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComScenario implements CommandExecutor
{

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("scenarios"))
            {
            if (commandSender instanceof Player)
            {
                ((Player) commandSender).openInventory(Scenario.getScenarioInventoryExplanation());
            }
        }
        return false;
    }
}
