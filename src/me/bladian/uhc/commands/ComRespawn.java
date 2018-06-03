package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.manager.ScenarioManager;
import me.bladian.uhc.manager.TeamManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.Reference;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * Created by BladianYT
 */
public class ComRespawn implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();
    private ScenarioManager scenarioManager = core.getScenarioManager();
    private TeamManager teamManager = core.getTeamManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("respawn"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                if (strings.length == 0)
                {
                    commandSender.sendMessage("§c/respawn <Player>");
                    return true;
                }
                Player t = Bukkit.getPlayer(strings[0]);
                if (t == null)
                {
                    commandSender.sendMessage(reference.ERROR + "§cPlayer isn't online");
                    return true;
                }
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(t.getUniqueId());
                if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                {
                    commandSender.sendMessage(reference.ERROR + "§cPlayer is already in the UHC");
                    return true;
                }
                t.setGameMode(GameMode.SURVIVAL);
                t.setFlying(false);
                t.getInventory().clear();
                t.getInventory().setArmorContents(null);
                t.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
                t.setHealth(20);
                t.setSaturation(20);
                t.setFoodLevel(20);
                for (PotionEffect potionEffect : t.getActivePotionEffects())
                {
                    t.removePotionEffect(potionEffect.getType());
                }
                ItemStack[] itemStacks = uhcPlayer.getItemStacks();
                ItemStack[] armorItemStacks = uhcPlayer.getArmorItemStacks();
                t.getInventory().setArmorContents(armorItemStacks);
                t.getInventory().setContents(itemStacks);
                t.setLevel(uhcPlayer.getLevels());
                commandSender.sendMessage(reference.GAME + "§7" + t.getName() + "§e has been respawn'd");
                t.setMaxHealth(20);
                gameManager.getPlayers().add(t.getUniqueId());
                uhcPlayer.setPlayerState(PlayerState.INGAME);
                playerManager.getSpectators().remove(t.getUniqueId());
                playerManager.getModerators().remove(t.getUniqueId());
                if(uhcPlayer.getDeathLocation() != null)
                {
                    t.teleport(uhcPlayer.getDeathLocation());
                }
                else
                {
                    Location location = gameManager.getLocation();
                    t.teleport(location);
                }
                for (Player all : Bukkit.getOnlinePlayers())
                {
                    all.showPlayer(t);
                }
                if (Scenario.INFINITEENCHANTER.isEnabled())
                {
                    t.setLevel(30000);
                    t.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                    t.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                    t.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                }
                if (Scenario.GONEFISHING.isEnabled())
                {
                    t.setExp(0);
                    t.setLevel(30000);
                    t.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                    ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                    fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
                    fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
                    fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
                    t.getInventory().addItem(fishingRod);
                }
                if (Scenario.BESTPVE.isEnabled())
                {
                    scenarioManager.getBestPVEList().add(t.getUniqueId());
                }
                if(gameManager.getTeamType() == TeamType.TEAMS)
                {
                    if(uhcPlayer.getTeamDeathNumber() != -1)
                    {
                        Team team = teamManager.getTeams().get(uhcPlayer.getTeamDeathNumber());
                        team.getAlivePlayers().add(t.getUniqueId());
                    }
                    else
                    {
                        teamManager.createTeam(t.getUniqueId(), uhcPlayer);
                    }
                }
                t.kickPlayer("§cYou've been respawn'd! Join the server!");
            }
        }
        return false;
    }
}
