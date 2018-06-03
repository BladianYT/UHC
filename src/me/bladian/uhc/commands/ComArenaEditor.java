package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ComArenaEditor implements CommandExecutor, Listener
{

    private Core core = Core.instance;
    private Inventories inventories = core.getInventories();
    private Reference reference = core.getReference();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("arenaEditor"))
        {
            Player p = (Player) commandSender;
            if(p.isOp())
            {
                inventories.arenaEditor(p);
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if (e.getInventory().getName().equalsIgnoreCase("§e§lARENA EDITOR"))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType() != Material.AIR)
                {
                    Player p = (Player) e.getWhoClicked();
                    ItemStack itemStack = e.getCurrentItem();
                    if (itemStack.getType() == Material.WOOL)
                    {
                        if (itemStack.getData().getData() == 5)
                        {
                            if (reference.ARENA_LOCATIONS.size() == 54)
                            {
                                p.sendMessage(reference.ERROR + "You have too many arenas, delete one to add another");
                            }
                            else
                            {
                                List<String> locations = core.getConfig().getStringList("arena.locations");
                                String world = p.getWorld().getName();
                                Location location = p.getLocation();
                                String x = String.valueOf(location.getBlockX());
                                String y = String.valueOf(location.getBlockY());
                                String z = String.valueOf(location.getBlockZ());
                                String yaw = String.valueOf(location.getYaw());
                                String pitch = String.valueOf(location.getPitch());
                                String save = world + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch + ";";
                                locations.add(save);
                                reference.ARENA_LOCATIONS.add(location);
                                p.sendMessage(reference.GAME + "§7Added§f an arena location§7 (" + x + "," + y + "," + z + ")");
                                core.getConfig().set("arena.locations", locations);
                                core.saveConfig();
                            }
                        }
                        if (itemStack.getData().getData() == 14)
                        {
                            int number = 1;
                            Inventory inventory = Bukkit.createInventory(null, 54, "§e§lDELETE ARENA");
                            for (Location location : reference.ARENA_LOCATIONS)
                            {
                                ItemStack itemLoc;
                                if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
                                {
                                    itemLoc = new ItemStack(Material.REDSTONE);
                                }
                                else
                                {
                                    itemLoc = new ItemStack(location.getBlock().getRelative(BlockFace.DOWN).getType());
                                }
                                List<String> lore = new ArrayList<>();
                                lore.add("§7X:§f " + location.getBlockX());
                                lore.add("§7Y:§f " + location.getBlockY());
                                lore.add("§7Z:§f " + location.getBlockZ());
                                ItemMeta itemMeta = itemLoc.getItemMeta();
                                itemMeta.setDisplayName("§e§lLOCATION " + number);
                                itemMeta.setLore(lore);
                                itemLoc.setItemMeta(itemMeta);
                                inventory.addItem(itemLoc);
                                number++;
                            }
                            p.openInventory(inventory);
                        }
                        if (itemStack.getData().getData() == 4)
                        {
                            inventories.kitSelector(p);
                        }
                    }
                }
            }
        }
        if (e.getInventory().getName().equalsIgnoreCase("§e§lDELETE ARENA"))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType() != Material.AIR)
                {
                    Player p = (Player) e.getWhoClicked();
                    ItemStack itemStack = e.getCurrentItem();
                    if (itemStack.getItemMeta().getDisplayName() != null)
                    {
                        String name = itemStack.getItemMeta().getDisplayName();
                        String[] split = name.replace("§e§l", "").split(" ");
                        int number = Integer.parseInt(split[1]);
                        List<String> locations = core.getConfig().getStringList("arena.locations");
                        locations.remove(number - 1);
                        p.sendMessage(reference.GAME + "§7Removed§f arena location§7 " + number);
                        e.getClickedInventory().remove(itemStack);
                        core.getConfig().set("arena.locations", locations);
                        core.saveConfig();
                        e.getClickedInventory().clear();
                        reference.ARENA_LOCATIONS.remove(number - 1);
                        int numberI = 1;
                        for (Location location : reference.ARENA_LOCATIONS)
                        {
                            ItemStack itemLoc;
                            if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)
                            {
                                itemLoc = new ItemStack(Material.REDSTONE);
                            }
                            else
                            {
                                itemLoc = new ItemStack(location.getBlock().getRelative(BlockFace.DOWN).getType());
                            }
                            List<String> lore = new ArrayList<>();
                            lore.add("§7X:§f " + location.getBlockX());
                            lore.add("§7Y:§f " + location.getBlockY());
                            lore.add("§7Z:§f " + location.getBlockZ());
                            ItemMeta itemMeta = itemLoc.getItemMeta();
                            itemMeta.setDisplayName("§e§lLOCATION " + numberI);
                            itemMeta.setLore(lore);
                            itemLoc.setItemMeta(itemMeta);
                            e.getClickedInventory().addItem(itemLoc);
                            numberI++;
                        }
                    }
                }
            }
        }
        if (e.getInventory().getName().equalsIgnoreCase("§e§lKIT SELECTOR"))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType() != Material.AIR)
                {
                    Player p = (Player) e.getWhoClicked();
                    ItemStack itemStack = e.getCurrentItem();
                    if (itemStack.getType() == Material.DIAMOND_SWORD)
                    {
                        reference.setARENA_KIT(2);
                        p.sendMessage(reference.GAME + "Set the §7arena kit§f to§7 BuildUHC");
                        core.getConfig().set("arena.kit", 2);
                        core.saveConfig();
                    }
                    if (itemStack.getType() == Material.IRON_SWORD)
                    {
                        reference.setARENA_KIT(1);
                        p.sendMessage(reference.GAME + "Set the §7arena kit§f to§7 UHC");
                        core.getConfig().set("arena.kit", 1);
                        core.saveConfig();
                    }
                }
            }
        }
    }
}
