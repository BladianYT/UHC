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

/**
 * Created by BladianYT
 */
public class ComHeal implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("heal"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                if(strings.length == 0)
                {
                    commandSender.sendMessage(reference.ERROR + "§c/heal <player/all>");
                }
                else
                {
                    Player i = (Player) commandSender;
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(i.getUniqueId());
                    if((uhcPlayer.getPlayerState() == PlayerState.HOST || uhcPlayer.getPlayerState() == PlayerState.MODERATOR) && !i.isOp())
                    {
                        i.sendMessage(reference.ERROR + "§cYou can't heal while in game");
                        return true;
                    }
                    if (strings[0].equalsIgnoreCase("all"))
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            p.setHealth(20.0);
                        }
                        Bukkit.broadcastMessage(reference.GAME + "§7Healed§f all players");
                    }
                    else
                    {
                        Player p = (Player) commandSender;
                        @SuppressWarnings("deprecation") Player t = Bukkit.getPlayer(strings[0]);
                        if (t != null)
                        {
                            t.setHealth(20.0);
                            t.setFoodLevel(20);
                            p.sendMessage(reference.GAME + "§fYou healed §7" + t.getName());
                            t.sendMessage(reference.GAME + "§fYou've been§7 healed§f by§7" + p.getName());
                        }
                        else
                        {
                            p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                        }
                    }
                }
            }
        }
        return false;
    }
}
