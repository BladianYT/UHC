package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComWhitelist implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private GameManager gameManager = core.getGameManager();
    private PlayerManager playerManager = core.getPlayerManager();

    @Override
    public boolean onCommand(CommandSender p, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("whitelist"))
        {
            if (p.hasPermission("rank.host"))
            {
                if (strings.length == 0)
                {
                    p.sendMessage(reference.ERROR + "§c/whitelist <add/remove> <player>");
                    p.sendMessage(reference.ERROR + "§c/whitelist <on/off>");
                }
                else
                {
                    if (strings[0].equalsIgnoreCase("on"))
                    {
                        Bukkit.broadcastMessage(reference.GAME + "§7Whitelist§f has been turned§a on");
                        gameManager.setWhitelistEnabled(true);
                    }
                    else if (strings[0].equalsIgnoreCase("off"))
                    {
                        Bukkit.broadcastMessage(reference.GAME + "§7Whitelist§f has been turned§c off");
                        gameManager.setWhitelistEnabled(false);
                    }
                    else if (strings[0].equalsIgnoreCase("all"))
                    {
                        for (Player all : Bukkit.getOnlinePlayers())
                        {
                            gameManager.getWhitelist().add(all.getUniqueId());
                            all.sendMessage(reference.GAME + "You've been§7 whitelisted");
                        }
                        Bukkit.broadcastMessage(reference.GAME + "The server has been§7 whitelisted");
                        gameManager.setWhitelistEnabled(true);
                    }
                    else if (strings[0].equalsIgnoreCase("add"))
                    {
                        if (strings.length >= 2)
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                            p.sendMessage(reference.GAME + "§7" + offlinePlayer.getName() + "§f has been§7 added§f to the whitelist");
                            gameManager.getWhitelist().add(offlinePlayer.getUniqueId());
                        }
                    }
                    else if (strings[0].equalsIgnoreCase("remove"))
                    {
                        if (strings.length >= 2)
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                            p.sendMessage(reference.GAME + "§7" + offlinePlayer.getName() + "§f has been§7 removed§f from the whitelist");
                            gameManager.getWhitelist().remove(offlinePlayer.getUniqueId());
                        }
                    }
                }
            }
            else
            {
                if (p.hasPermission("rank.add-whitelist"))
                {
                    Player player = (Player) p;
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(player.getUniqueId());
                    if (strings.length < 2)
                    {
                        p.sendMessage(reference.ERROR + "§c/whitelist <add/remove> <player>");
                    }
                    else
                    {
                        if (gameManager.getGameState() != GameState.LOBBY)
                        {
                            p.sendMessage(reference.ERROR + "You can only use this in the lobby");
                            return true;
                        }
                        if (strings[0].equalsIgnoreCase("add"))
                        {
                            if (strings.length >= 2)
                            {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                                if (uhcPlayer.getWhitelistedPlayers().size() == 1)
                                {
                                    p.sendMessage(reference.ERROR + "You have no whitelists left");
                                    return true;
                                }
                                else
                                {
                                    if (uhcPlayer.getWhitelistedPlayers().contains(offlinePlayer.getName()))
                                    {
                                        p.sendMessage(reference.ERROR + "You already whitelisted this player");
                                        return true;
                                    }
                                    else
                                    {
                                        gameManager.getWhitelist().add(offlinePlayer.getUniqueId());
                                        uhcPlayer.getWhitelistedPlayers().add(offlinePlayer.getName());
                                        p.sendMessage(reference.GAME + "You whitelisted§b " + offlinePlayer.getName());
                                    }
                                }
                            }
                        }
                        else if (strings[0].equalsIgnoreCase("remove"))
                        {
                            if (strings.length >= 2)
                            {
                                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(strings[1]);
                                if (!uhcPlayer.getWhitelistedPlayers().contains(offlinePlayer.getName()))
                                {
                                    p.sendMessage(reference.ERROR + "You didn't whitelist this player");
                                    return true;
                                }
                                /*else
                                {
                                    gameManager.getWhitelist().remove(offlinePlayer.getUniqueId());
                                    uhcPlayer.getWhitelistedPlayers().remove(offlinePlayer.getName());
                                    p.sendMessage(reference.GAME + "You un-whitelisted§c " + offlinePlayer.getName());
                                    if (offlinePlayer.isOnline() && offlinePlayer.getPlayer().set)
                                    {
                                        offlinePlayer.getPlayer().kickPlayer("§cYour whitelist was removed");
                                    }
                                }*/
                            }
                        }
                    }
                }
            }
        }
        if (command.getName().equalsIgnoreCase("dWhitelist"))
        {
            if (p.hasPermission("rank.host"))
            {
                if (strings.length == 0)
                {
                    p.sendMessage(reference.ERROR + "§c/dWhitelist <on/off>");
                }
                else
                {
                    if (strings[0].equalsIgnoreCase("on"))
                    {
                        Bukkit.broadcastMessage(reference.GAME + "§7Donator Whitelist§f has been turned§a on");
                        gameManager.setDonatorWhitelistEnabled(true);
                    }
                    else if (strings[0].equalsIgnoreCase("off"))
                    {
                        Bukkit.broadcastMessage(reference.GAME + "§7Donator Whitelist§f has been turned§c off");
                        gameManager.setDonatorWhitelistEnabled(false);
                    }
                }
            }
        }
        return false;
    }
}



