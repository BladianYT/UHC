package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by BladianYT
 */
public class ComStart implements CommandExecutor
{

    private Util util = Core.instance.getUtil();

    private final Inventory inventory = Bukkit.createInventory(null, 9, "§e§lREADY TO START");
    private final ItemStack solo = util.createItem(Material.GOLD_INGOT, "§e§lSTART");

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("start"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                if (commandSender instanceof Player)
                {
                    {
                        inventory.clear();
                        inventory.setItem(4, solo);
                        ((Player) commandSender).openInventory(inventory);
                    }
                }
            }
        }
        return false;
    }
}
