package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Created by BladianYT
 */
@SuppressWarnings("deprecation")
public class ComTele implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("tele"))
        {
            Player p = (Player) commandSender;
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if ((commandSender.hasPermission("rank.host") && (uhcPlayer.getPlayerState() == PlayerState.MODERATOR || uhcPlayer.getPlayerState() == PlayerState.SPECTATOR || uhcPlayer.getPlayerState() == PlayerState.HOST)))
            {
                    String correct = reference.ERROR + "§c/tele <player> [me]";
                    if(strings.length == 0)
                    {
                        p.sendMessage(correct);
                        return true;
                    }
                    if(strings.length == 1)
                    {
                        Player t = Bukkit.getPlayer(strings[0]);
                        if(t == null)
                        {
                            p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                            return true;
                        }
                        p.teleport(t);
                        p.sendMessage(reference.GAME + "§7Teleported§f you to§7 " + t.getName());
                        Bukkit.getLogger().log(Level.INFO, "[Teleport] " + p.getName() + " has teleported to " + t.getName());
                        return true;
                    }
                    if(strings.length == 2)
                    {
                        if(!p.hasPermission("rank.host"))
                        {
                            return true;
                        }
                        if(!strings[1].equalsIgnoreCase("me"))
                        {
                            p.sendMessage(correct);
                            return true;
                        }
                        Player t = Bukkit.getPlayer(strings[0]);
                        if(!t.isOnline())
                        {
                            p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                            return true;
                        }
                        t.teleport(p);
                        p.sendMessage(reference.GAME + "§7Teleported§7 " + t.getName() + "§f to you");
                        return true;
                    }
                }
            }
        return false;
    }
}
