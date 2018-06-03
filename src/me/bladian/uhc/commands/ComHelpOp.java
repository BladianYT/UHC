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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by BladianYT
 */
public class ComHelpOp implements CommandExecutor
{
    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();


    private final List<UUID> cooldown = new ArrayList<>();

    private String ARROW = "§8»";
    private String HELPOP_PREFIX = "§5Helpop " + ARROW + " ";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("helpop"))
        {
            final Player t = (Player) commandSender;
            /*if(me.aries.core.Core.instance.getPlayerManager().getAriesPlayer(t.getUniqueId()).isMute())
            {
                t.sendMessage(reference.ERROR + "§cYou can't helpop while muted");
                return true;
            }*/
            if(strings.length == 0)
            {
                t.sendMessage(reference.ERROR + "§c/helpop <message>");
                return true;
            }
            if (!cooldown.contains(t.getUniqueId()))
            {
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : strings)
                {
                    stringBuilder.append(string).append(" ");
                }
                t.sendMessage(HELPOP_PREFIX + "§fYour§5 helpop§f has been sent to our§5 moderators");
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.MODERATOR || uhcPlayer.getPlayerState() == PlayerState.HOST)
                    {
                        p.sendMessage(HELPOP_PREFIX + "§5" + t.getName() + ":§7 " + stringBuilder);
                        cooldown.add(t.getUniqueId());
                        new BukkitRunnable()
                        {

                            @Override
                            public void run()
                            {
                                cooldown.remove(t.getUniqueId());
                            }
                        }.runTaskLater(core, 200L);
                    }
                }
            }
            else
            {
                t.sendMessage(reference.ERROR + "§cYou can only use helpop every 10 seconds");
            }
        }
        return false;
    }
}
