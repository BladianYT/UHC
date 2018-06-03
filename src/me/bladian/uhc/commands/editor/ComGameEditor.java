package me.bladian.uhc.commands.editor;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.ConfigManager;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.TeamManager;
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


public class ComGameEditor implements CommandExecutor, Listener
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private ConfigManager configManager = core.getConfigManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();
    private Inventories inventories = core.getInventories();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("gameEditor"))
        {
            Player p = (Player) commandSender;
            if (p.hasPermission("rank.host"))
            {
                inventories.gameEditor(p);
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if (e.getInventory().getName().equalsIgnoreCase("§e§lGAME EDITOR"))
        {
            e.setCancelled(true);
            if (e.getCurrentItem() != null)
            {
                if (e.getCurrentItem().getType() != Material.AIR)
                {
                    Player p = (Player) e.getWhoClicked();
                    ItemStack itemStack = e.getCurrentItem();
                    if (itemStack.getType() == Material.BOOK_AND_QUILL)
                    {
                        if (!gameManager.isRules())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            lore.add("");
                            lore.add("Warning: Only disable rules if you're");
                            lore.add("testing, or if the amount of players");
                            lore.add("left to scatter are 0.");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            gameManager.setRules(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cRules§f have been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            lore.add("");
                            lore.add("Warning: Only disable rules if you're");
                            lore.add("testing, or if the amount of players");
                            lore.add("left to scatter are 0.");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            gameManager.setRules(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cRules§f have been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.DIAMOND_PICKAXE)
                    {
                        p.closeInventory();
                        p.performCommand("worldEditor");
                    }
                    else if (itemStack.getType() == Material.PAPER)
                    {
                        p.closeInventory();
                        p.performCommand("scenarioEditor");
                    }
                    else if (itemStack.getType() == Material.EMERALD)
                    {
                        if (gameManager.getGameState() == GameState.LOBBY)
                        {
                            if (gameManager.getTeamType() == TeamType.TEAMS)
                            {
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                List<String> teamLore = new ArrayList<>();
                                teamLore.add("§bSolo §f⬅");
                                teamLore.add("§bTeams");
                                itemMeta.setLore(teamLore);
                                itemStack.setItemMeta(itemMeta);
                                gameManager.setTeamType(TeamType.SOLO);
                                Bukkit.broadcastMessage(reference.GAME + "The§c Team Type§f has been changed to§c Solo");
                                teamManager.getTeams().clear();
                            }
                            else
                            {
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                List<String> teamLore = new ArrayList<>();
                                teamLore.add("§bSolo");
                                teamLore.add("§bTeams §f⬅");
                                itemMeta.setLore(teamLore);
                                itemStack.setItemMeta(itemMeta);
                                gameManager.setTeamType(TeamType.TEAMS);
                                Bukkit.broadcastMessage(reference.GAME + "The§c Team Type§f has been changed to§c Teams");
                            }
                        }
                    }
                    else if (itemStack.getType() == Material.OBSIDIAN)
                    {
                        if (!configManager.isNether())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setNether(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cNether§f has been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setNether(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cNether§f has been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.ENDER_PEARL)
                    {
                        if (!configManager.isEnderpearlDamage())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setEnderpearlDamage(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cEnderpearl Damage§f has been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setEnderpearlDamage(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cEnderpearl Damage§f has been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.WHEAT)
                    {
                        if (!configManager.isHorseHealing())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setHorseHealing(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cHorse Healing§f has been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setHorseHealing(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cHorse Healing§f has been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.GOLDEN_APPLE)
                    {
                        if (!configManager.isGodApples())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setGodApples(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cGod Apples§f have been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setGodApples(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cGod Apples§f have been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.POTION)
                    {
                        if(itemStack.getItemMeta().getDisplayName() != null)
                        {
                            if (itemStack.getItemMeta().getDisplayName().contains("SPEED 1"))
                            {

                                if (!configManager.isSpeed1())
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled §f⬅");
                                    lore.add("§cDisabled");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setSpeed1(true);
                                    Bukkit.broadcastMessage(reference.GAME + "§cSpeed 1§f has been§a enabled");
                                }
                                else
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled");
                                    lore.add("§cDisabled §f⬅");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setSpeed1(false);
                                    Bukkit.broadcastMessage(reference.GAME + "§cSpeed 1§f has been§c disabled");
                                }
                            }
                            else if (itemStack.getItemMeta().getDisplayName().contains("SPEED 2"))
                            {

                                if (!configManager.isSpeed2())
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled §f⬅");
                                    lore.add("§cDisabled");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setSpeed2(true);
                                    Bukkit.broadcastMessage(reference.GAME + "§cSpeed 2§f has been§a enabled");
                                }
                                else
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled");
                                    lore.add("§cDisabled §f⬅");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setSpeed2(false);
                                    Bukkit.broadcastMessage(reference.GAME + "§cSpeed 2§f has been§c disabled");
                                }
                            }
                            else if (itemStack.getItemMeta().getDisplayName().contains("STRENGTH 1"))
                            {

                                if (!configManager.isStrength1())
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled §f⬅");
                                    lore.add("§cDisabled");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setStrength1(true);
                                    Bukkit.broadcastMessage(reference.GAME + "§cStrength 1§f has been§a enabled");
                                }
                                else
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled");
                                    lore.add("§cDisabled §f⬅");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setStrength1(false);
                                    Bukkit.broadcastMessage(reference.GAME + "§cStrength 1§f has been§c disabled");
                                }
                            }
                            else if (itemStack.getItemMeta().getDisplayName().contains("STRENGTH 2"))
                            {

                                if (!configManager.isStrength2())
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled §f⬅");
                                    lore.add("§cDisabled");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setStrength2(true);
                                    Bukkit.broadcastMessage(reference.GAME + "§cStrength 2§f has been§a enabled");
                                }
                                else
                                {
                                    ItemMeta itemMeta = itemStack.getItemMeta();
                                    List<String> lore = new ArrayList<>();
                                    lore.add("§aEnabled");
                                    lore.add("§cDisabled §f⬅");
                                    itemMeta.setLore(lore);
                                    itemStack.setItemMeta(itemMeta);
                                    configManager.setStrength2(false);
                                    Bukkit.broadcastMessage(reference.GAME + "§cStrength 2§f has been§c disabled");
                                }
                            }
                        }
                    }
                    else if (itemStack.getType() == Material.SHEARS)
                    {
                        if (!configManager.isShears())
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled §f⬅");
                            lore.add("§cDisabled");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setShears(true);
                            Bukkit.broadcastMessage(reference.GAME + "§cShears§f have been§a enabled");
                        }
                        else
                        {
                            ItemMeta itemMeta = itemStack.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            lore.add("§aEnabled");
                            lore.add("§cDisabled §f⬅");
                            itemMeta.setLore(lore);
                            itemStack.setItemMeta(itemMeta);
                            configManager.setShears(false);
                            Bukkit.broadcastMessage(reference.GAME + "§cShears§f have been§c disabled");
                        }
                    }
                    else if (itemStack.getType() == Material.BEDROCK)
                    {
                        p.closeInventory();
                        p.performCommand("borderradius");
                    }
                }
            }
        }
    }
}