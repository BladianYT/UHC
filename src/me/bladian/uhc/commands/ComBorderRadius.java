package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ComBorderRadius implements CommandExecutor, Listener
{

    private Core core = Core.instance;
    private GameManager gameManager = core.getGameManager();
    private Reference reference = core.getReference();
    private Inventories inventories = core.getInventories();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("borderRadius"))
        {
            if(commandSender.hasPermission("rank.host"))
            {
                Player p = (Player) commandSender;
                inventories.borderEditor(p);
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if (e.getInventory().getName().equalsIgnoreCase("§e§lBORDER RADIUS"))
        {
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType() != Material.AIR)
                {
                    ItemStack itemStack = e.getCurrentItem();
                    if (itemStack.getType() == Material.BEDROCK)
                    {
                        if (itemStack.getItemMeta().getDisplayName() != null)
                        {
                            e.setCancelled(true);
                            Player p = (Player) e.getWhoClicked();
                            p.closeInventory();
                            if (e.getSlot() == 10)
                            {
                                gameManager.setBorderStartingNumber(0);
                                gameManager.setBorderRadius(3500);
                            }
                            if (e.getSlot() == 11)
                            {
                                gameManager.setBorderStartingNumber(1);
                                gameManager.setBorderRadius(3000);
                            }
                            if (e.getSlot() == 12)
                            {
                                gameManager.setBorderStartingNumber(2);
                                gameManager.setBorderRadius(2500);
                            }
                            if (e.getSlot() == 13)
                            {
                                gameManager.setBorderStartingNumber(3);
                                gameManager.setBorderRadius(2000);
                            }
                            if (e.getSlot() == 14)
                            {
                                gameManager.setBorderStartingNumber(4);
                                gameManager.setBorderRadius(1500);
                            }
                            if (e.getSlot() == 15)
                            {
                                gameManager.setBorderStartingNumber(5);
                                gameManager.setBorderRadius(1000);
                            }
                            if (e.getSlot() == 16)
                            {
                                gameManager.setBorderStartingNumber(6);
                                gameManager.setBorderRadius(500);
                            }
                        }
                        String[] split = itemStack.getItemMeta().getDisplayName().split(" ");
                        Bukkit.broadcastMessage(reference.GAME + "Changed the§7 Border Radius§f to§7 " + split[0].replace("§e§l", ""));

                    }
                }
            }
        }
    }
}
