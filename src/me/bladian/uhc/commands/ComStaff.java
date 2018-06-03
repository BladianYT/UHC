package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.UUID;

/**
 * Created by BladianYT
 */

public class ComStaff implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Inventories inventories = core.getInventories();
    private PlayerManager playerManager = core.getPlayerManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("mod"))
        {
            if (commandSender instanceof Player)
            {
                Player p = (Player) commandSender;
                UUID uuid = p.getUniqueId();
                if (p.hasPermission("rank.host"))
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.INGAME || uhcPlayer.getPlayerState() == PlayerState.SCATTERED || uhcPlayer.getPlayerState() == PlayerState.SCATTERING)
                    {
                        p.sendMessage(reference.ERROR + "§cYou can't be a mod while you're in game");
                    }
                    else
                    {
                        if(uhcPlayer.getPlayerState() == PlayerState.MODERATOR)
                        {
                            p.getInventory().clear();
                            uhcPlayer.setPlayerState(PlayerState.LOBBY);
                            playerManager.getModerators().remove(p.getUniqueId());
                            p.teleport(reference.SPAWN);
                            p.sendMessage(reference.MODERATOR + "You were removed from§d Moderator§f mode");
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.showPlayer(p);
                            }
                            p.setGameMode(GameMode.SURVIVAL);

                        }
                        else if(uhcPlayer.getPlayerState() == PlayerState.HOST)
                        {
                            p.sendMessage(reference.MODERATOR + "You're currently in§d Host§f mode, not§d Moderator§f mode");
                        }
                        else
                        {
                            inventories.moderator(p);
                            uhcPlayer.setPlayerState(PlayerState.MODERATOR);
                            playerManager.getModerators().add(uuid);
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.hidePlayer(p);
                            }
                            p.sendMessage(reference.MODERATOR + "You're now in§d Moderator§f mode");
                            PermissionAttachment attachment = p.addAttachment(core);
                            attachment.setPermission("worldedit.navigation.thru.tool", true);
                            attachment.setPermission("worldedit.navigation.jumpto.tool", true);
                        }
                    }
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("host"))
        {
            if (commandSender instanceof Player)
            {
                Player p = (Player) commandSender;
                UUID uuid = p.getUniqueId();
                if (p.hasPermission("rank.host"))
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.INGAME || uhcPlayer.getPlayerState() == PlayerState.SCATTERED || uhcPlayer.getPlayerState() == PlayerState.SCATTERING)
                    {
                        p.sendMessage(reference.ERROR + "§cYou can't be a host while you're in game");
                    }
                    else
                    {
                        if (uhcPlayer.getPlayerState() == PlayerState.HOST)
                        {
                            p.getInventory().clear();
                            uhcPlayer.setPlayerState(PlayerState.LOBBY);
                            playerManager.getModerators().remove(p.getUniqueId());
                            playerManager.setHost(null);
                            p.teleport(reference.SPAWN);
                            p.sendMessage(reference.MODERATOR + "You were removed from§d Host§f mode");
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.showPlayer(p);
                            }
                            p.setGameMode(GameMode.SURVIVAL);
                        }
                        else if (uhcPlayer.getPlayerState() == PlayerState.MODERATOR)
                        {
                            p.sendMessage(reference.MODERATOR + "You're currently in§d Moderator§f mode, not§d Host§f mode");
                        }
                        else
                        {
                            inventories.moderator(p);
                            uhcPlayer.setPlayerState(PlayerState.HOST);
                            playerManager.getModerators().add(uuid);
                            playerManager.setHost(p.getUniqueId());
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.hidePlayer(p);
                            }
                            p.sendMessage(reference.MODERATOR + "You're now in§d Host§f mode");
                            PermissionAttachment attachment = p.addAttachment(core);
                            attachment.setPermission("worldedit.navigation.thru.tool", true);
                            attachment.setPermission("worldedit.navigation.jumpto.tool", true);
                        }
                    }
                }
            }
        }
        return false;
    }
}
