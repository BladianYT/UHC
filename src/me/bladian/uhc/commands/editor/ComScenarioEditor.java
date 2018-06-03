package me.bladian.uhc.commands.editor;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.ScenarioManager;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BladianYT
 */
public class ComScenarioEditor implements CommandExecutor, Listener
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private ScenarioManager scenarioManager = core.getScenarioManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("scenarioEditor"))
        {
            Player p = (Player) commandSender;
            if(p.hasPermission("rank.host"))
            {
                p.openInventory(Scenario.getScenarioEditorInventory());
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(e.getInventory().getName().equalsIgnoreCase("§e§lSCENARIO EDITOR"))
        {
            if(e.getCurrentItem() != null)
            {
                if(e.getCurrentItem().getType() != Material.AIR)
                {
                    ItemStack itemStack = e.getCurrentItem();
                    if(itemStack.getType() == Material.WOOL)
                    {
                        if (itemStack.getData().getData() == 5)
                        {
                            for(ItemStack itemStacks : e.getClickedInventory().getContents())
                            {
                                if (itemStacks != null && itemStacks.getType() != Material.AIR)
                                {
                                    if (itemStacks.getItemMeta().hasDisplayName())
                                    {
                                        Scenario scenario = Scenario.getScenarioFromName(itemStacks.getItemMeta().getDisplayName());
                                        if (scenario != null)
                                        {
                                            if(scenario == Scenario.BACKPACKS && gameManager.getTeamType() == TeamType.SOLO)
                                            {
                                                Player p = (Player) e.getWhoClicked();
                                                p.sendMessage(reference.ERROR + " You can't enable backpacks without having teams");
                                            }
                                            else
                                            {
                                                scenario.enable();
                                                ItemMeta itemMeta = itemStacks.getItemMeta();
                                                List<String> lore = new ArrayList<>();
                                                lore.add(" ");
                                                lore.add("§aEnabled §f⬅");
                                                lore.add("§cDisabled");
                                                itemMeta.setLore(lore);
                                                itemStacks.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                            }

                            ItemStack enabled = createItem(Material.NETHER_STAR, "§bEnabled Scenarios");

                            ItemMeta itemMeta = enabled.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            for (Scenario scenarios : Scenario.values())
                            {
                                if (scenarios.isEnabled())
                                {
                                    lore.add("§8-§f " + scenarios.getItemStack().getItemMeta().getDisplayName().replace("§b", "§f"));
                                }
                            }
                            itemMeta.setLore(lore);
                            enabled.setItemMeta(itemMeta);
                            e.getClickedInventory().setItem(40, enabled);
                        }
                        else
                        {
                            for(ItemStack itemStacks : e.getClickedInventory().getContents())
                            {
                                if (itemStacks != null && itemStacks.getType() != Material.AIR)
                                {
                                    if (itemStacks.getItemMeta().hasDisplayName())
                                    {
                                        Scenario scenario = Scenario.getScenarioFromName(itemStacks.getItemMeta().getDisplayName());
                                        if (scenario != null)
                                        {
                                            if(scenario == Scenario.BACKPACKS && gameManager.getTeamType() == TeamType.SOLO)
                                            {
                                                Player p = (Player) e.getWhoClicked();
                                                p.sendMessage(reference.ERROR + " You can't enable backpacks without having teams");
                                            }
                                            else
                                            {
                                                scenario.disable();
                                                ItemMeta itemMeta = itemStacks.getItemMeta();
                                                List<String> lore = new ArrayList<>();
                                                lore.add(" ");
                                                lore.add("§aEnabled");
                                                lore.add("§cDisabled §f⬅");
                                                itemMeta.setLore(lore);
                                                itemStacks.setItemMeta(itemMeta);
                                            }
                                        }
                                    }
                                }
                            }
                            ItemStack enabled = createItem(Material.NETHER_STAR, "§bEnabled Scenarios");

                            ItemMeta itemMeta = enabled.getItemMeta();
                            itemMeta.setLore(new ArrayList<String>());
                            e.getClickedInventory().setItem(40, enabled);
                        }
                    }
                    if (itemStack.hasItemMeta())
                    {
                        Scenario scenario = Scenario.getScenarioFromName(itemStack.getItemMeta().getDisplayName());
                        if(scenario != null)
                        {
                            if(scenario.isEnabled())
                            {
                                scenario.disable();
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                List<String> lore = itemMeta.getLore();
                                lore.clear();
                                lore.add("§aEnabled");
                                lore.add("§cDisabled §f⬅");
                                itemMeta.setLore(lore);
                                itemStack.setItemMeta(itemMeta);
                                if (!Scenario.INFINITEENCHANTER.isEnabled())
                                {
                                    for (Player p : Bukkit.getOnlinePlayers())
                                    {
                                        if (p.getGameMode() == GameMode.SURVIVAL)
                                        {
                                            p.setLevel(0);
                                            p.getInventory().remove(Material.ENCHANTMENT_TABLE);
                                            p.getInventory().remove(Material.ANVIL);
                                            p.getInventory().remove(Material.BOOKSHELF);
                                        }
                                    }
                                }
                                if (!Scenario.BESTPVE.isEnabled())
                                {
                                    for(BukkitTask bukkitTask : scenarioManager.getBestPVEScheduler())
                                    {
                                        bukkitTask.cancel();
                                        for(Player p : Bukkit.getOnlinePlayers())
                                        {
                                            p.setMaxHealth(20.0);
                                        }
                                    }
                                    scenarioManager.getBestPVEScheduler().clear();
                                    scenarioManager.getBestPVEList().clear();
                                }
                            }
                            else
                            {
                                if(scenario == Scenario.BACKPACKS && gameManager.getTeamType() == TeamType.SOLO)
                                {
                                    Player p = (Player) e.getWhoClicked();
                                    p.sendMessage(reference.ERROR + " You can't enable backpacks without having teams");
                                    return;
                                }
                                scenario.enable();
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                List<String> lore = itemMeta.getLore();
                                lore.clear();
                                lore.add("§aEnabled §f⬅");
                                lore.add("§cDisabled");
                                itemMeta.setLore(lore);
                                itemStack.setItemMeta(itemMeta);
                            }

                            ItemStack enabled = createItem(Material.NETHER_STAR, "§bEnabled Scenarios");

                            ItemMeta itemMeta = enabled.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            for(Scenario scenarios : Scenario.values())
                            {
                                if(scenarios.isEnabled())
                                {
                                    lore.add("§8-§f " + scenarios.getItemStack().getItemMeta().getDisplayName().replace("§b", "§f"));
                                }
                            }
                            itemMeta.setLore(lore);
                            enabled.setItemMeta(itemMeta);
                            e.getClickedInventory().setItem(40, enabled);
                        }
                    }

                }
            }
            e.setCancelled(true);
        }
    }

    private ItemStack createItem(Material material, String name)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }
}
