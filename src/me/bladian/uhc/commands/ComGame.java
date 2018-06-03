package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.manager.ScenarioManager;
import me.bladian.uhc.manager.TeamManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.UUID;

/**
 * Created by BladianYT
 */
@SuppressWarnings("deprecation")
public class ComGame implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private PlayerManager playerManager = core.getPlayerManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();
    private ScenarioManager scenarioManager = core.getScenarioManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("game"))
        {
            Player p = (Player) commandSender;
            if (commandSender.hasPermission("rank.host"))
            {
                if(gameManager.getGameState() != GameState.STARTED)
                {
                    p.sendMessage(reference.ERROR + "§cA UHC isn't running right now");
                    return true;
                }

                if (strings.length == 0)
                {
                    p.sendMessage(reference.ERROR + "§c/game list");
                    p.sendMessage(reference.ERROR + "§c/game add <player>");
                    p.sendMessage(reference.ERROR + "§c/game remove <player>");
                }
                else
                {
                    if (strings[0].equalsIgnoreCase("list"))
                    {
                        Inventory inventory = Bukkit.createInventory(null, 54, "§ePlayers");
                        for (UUID uuid : gameManager.getPlayers())
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                            ItemStack itemStack = util.createItem(Material.SKULL_ITEM, offlinePlayer.getName());
                            inventory.addItem(itemStack);
                        }
                        p.openInventory(inventory);
                    }
                    else if (strings[0].equalsIgnoreCase("add"))
                    {
                        if (strings.length >= 2)
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(offlinePlayer.getUniqueId());
                            if(uhcPlayer.getPlayerState() == PlayerState.INGAME || uhcPlayer.getPlayerState() == PlayerState.SCATTERING || uhcPlayer.getPlayerState() == PlayerState.SCATTERED)
                            {
                                p.sendMessage(reference.ERROR + "§cPlayer is already in the UHC");
                                return true;
                            }
                            if (offlinePlayer.isOnline())
                            {
                                Player t = offlinePlayer.getPlayer();
                                t.setGameMode(GameMode.SURVIVAL);
                                t.setFlying(false);
                                t.getInventory().clear();
                                t.getInventory().setArmorContents(null);
                                t.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
                                t.getInventory().addItem(new ItemStack(Material.FEATHER, 16));
                                t.getInventory().addItem(new ItemStack(Material.LEATHER, 16));
                                t.getInventory().addItem(new ItemStack(Material.STRING, 5));
                                t.setHealth(20);
                                t.setSaturation(20);
                                t.setFoodLevel(20);
                                for (PotionEffect potionEffect : t.getActivePotionEffects())
                                {
                                    t.removePotionEffect(potionEffect.getType());
                                }
                                t.teleport(gameManager.getLocation());
                                p.sendMessage(reference.GAME + "§7" + t.getName() + "§f has been added to the game");
                                t.setMaxHealth(20);
                                gameManager.getPlayers().add(t.getUniqueId());
                                uhcPlayer.setPlayerState(PlayerState.INGAME);
                                playerManager.getSpectators().remove(t.getUniqueId());
                                playerManager.getModerators().remove(t.getUniqueId());
                                for(Player all : Bukkit.getOnlinePlayers())
                                {
                                    all.showPlayer(p);
                                }
                                if(Scenario.INFINITEENCHANTER.isEnabled())
                                {
                                    t.setLevel(30000);
                                    t.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                                    t.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                    t.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                }
                                if (Scenario.GONEFISHING.isEnabled())
                                {
                                    p.setExp(0);
                                    p.setLevel(30000);
                                    p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                    ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                                    fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
                                    fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
                                    fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
                                    p.getInventory().addItem(fishingRod);
                                }
                                if(Scenario.BESTPVE.isEnabled())
                                {
                                    scenarioManager.getBestPVEList().add(t.getUniqueId());
                                }
                                if(gameManager.getTeamType() == TeamType.TEAMS)
                                {
                                    teamManager.createTeam(t.getUniqueId(), uhcPlayer);
                                }
                                for(Player all : Bukkit.getOnlinePlayers())
                                {
                                    all.showPlayer(p);
                                }
                            }
                            else
                            {
                                p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                            }
                        }
                    }
                    else if (strings[0].equalsIgnoreCase("remove"))
                    {
                        if (strings.length >= 2)
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(offlinePlayer.getUniqueId());
                            if(uhcPlayer.getPlayerState() != PlayerState.INGAME && uhcPlayer.getPlayerState() != PlayerState.SCATTERING && uhcPlayer.getPlayerState() != PlayerState.SCATTERED)
                            {
                                p.sendMessage(reference.ERROR + "§cPlayer isn't in the UHC");
                                return true;
                            }
                            if (offlinePlayer.isOnline())
                            {
                                Player t = Bukkit.getPlayer(strings[1]);
                                t.setHealth(0);
                                p.sendMessage(reference.GAME + "§7" + t.getName() + "§f has been removed from the UHC");
                            }
                            else
                            {
                                gameManager.getPlayers().remove(offlinePlayer.getUniqueId());
                                p.sendMessage(reference.GAME + "§7" + offlinePlayer.getName() + "§f has been removed from the UHC");
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
