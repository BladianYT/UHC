package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Created by BladianYT
 */
public class ComClear implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("clear"))
        {
            if (commandSender.hasPermission("rank.host"))
            {
                if(strings.length == 0)
                {
                    commandSender.sendMessage("§c/clear <player/all>");
                }
                else
                {
                    if (strings[0].equalsIgnoreCase("all"))
                    {
                        for (Player p : Bukkit.getOnlinePlayers())
                        {
                            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                            if(uhcPlayer.getPlayerState() == PlayerState.HOST || uhcPlayer.getPlayerState() == PlayerState.MODERATOR)
                            {
                                p.getInventory().clear();
                                p.getInventory().setArmorContents(null);
                                p.setTotalExperience(0);
                                p.setExp(0);
                                p.setLevel(0);
                            }
                        }
                        Bukkit.broadcastMessage(reference.GAME + "§fYour§7 inventory§f has been§7 cleared");
                    }
                    else
                    {
                        Player p = (Player) commandSender;
                        @SuppressWarnings("deprecation") Player t = Bukkit.getPlayer(strings[0]);
                        if (t != null)
                        {
                            t.getInventory().clear();
                            t.getInventory().setArmorContents(null);
                            t.sendMessage("§eYour§a inventory§e has been§a cleared");
                            p.sendMessage("§a" + t.getName() + "'s§e has been§a cleared");
                            t.setTotalExperience(0);
                            t.setExp(0);
                            t.setLevel(0);
                        }
                        else
                        {
                            p.sendMessage("§cPlayer isn't online");
                        }
                    }
                }
            }
            if(commandSender.isOp())
            {
                if(strings[0].equalsIgnoreCase("mobs"))
                {
                    Player player = (Player) commandSender;
                    int total = 0;

                    for (Entity en : player.getWorld().getEntities())
                    {
                        if (!(en instanceof Player))
                        {
                            en.remove();
                            total++;
                        }
                    }
                    commandSender.sendMessage("§eKilled a total of " + total + " mobs");
                    return false;
                }
            }
        }
        return false;
    }

}
