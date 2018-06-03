package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ComGiveAll implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private PlayerManager playerManager = core.getPlayerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("giveAll"))
        {
            if(commandSender.hasPermission("rank.give-all"))
            {
                if(strings.length < 2)
                {
                    commandSender.sendMessage(reference.ERROR + "/giveAll <material> <amount>");
                    return true;
                }
                if(Material.getMaterial(strings[0]) == null)
                {
                    commandSender.sendMessage(reference.ERROR + "You have to type a Material (BOOK, IRON_SWORD)");
                    return true;
                }
                if(!util.isNumeric(strings[1]))
                {
                    commandSender.sendMessage(reference.ERROR + "Amount has to be a number");
                    return true;
                }
                Material material = Material.getMaterial(strings[0]);
                int amount = Integer.parseInt(strings[1]);
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                    {
                        p.getInventory().addItem(new ItemStack(material, amount));
                        p.sendMessage(reference.GAME + "You received§7 " + amount + " " + material);
                    }
                }
                commandSender.sendMessage(reference.GAME + "You gave§7 " + amount + " " + material);
            }
        }
        return false;
    }
}
