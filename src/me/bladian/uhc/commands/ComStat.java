package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
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
public class ComStat implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("stats"))
        {
            Player p = (Player) commandSender;
            if(strings.length == 0)
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getUniqueId());
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(offlinePlayer.getUniqueId());
                p.openInventory(uhcPlayer.getStatsInventory());
            }
            else
            {
                Player t = Bukkit.getPlayer(strings[0]);
                if(t == null)
                {
                    p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                }
                else
                {
                    if(playerManager.getUhcPlayerMap().containsKey(t.getUniqueId()))
                    {
                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(t.getUniqueId());
                        p.openInventory(uhcPlayer.getStatsInventory());
                    }
                }
            }
        }
        return false;
    }

}
