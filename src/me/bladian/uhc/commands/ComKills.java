package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
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

import java.util.*;

/**
 * Created by BladianYT
 */
public class ComKills implements CommandExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {

        if(command.getName().equalsIgnoreCase("kt"))
        {
            Player p = (Player) commandSender;
            if(gameManager.getGameState() != GameState.STARTED)
            {
                p.sendMessage(reference.ERROR + "§cA game isn't currently running");
                return true;
            }
            p.sendMessage(reference.GAME + "§b§lTOP TEN KILLS");
            p.sendMessage(reference.GAME + "");
            Map<UUID, Integer> unsortedkills = new HashMap<>();
            for(UHCPlayer uhcPlayer : playerManager.getUhcPlayerMap().values())
            {
                if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                {
                    unsortedkills.put(uhcPlayer.getUuid(), uhcPlayer.getKills());
                }
            }
            Map<UUID, Integer> kills = sortByValue(unsortedkills);
            int x = 1;
            for(Object object : kills.keySet())
            {
                if(x != 11)
                {
                    UUID uuid = (UUID) object;
                    if(kills.get(uuid) != 0)
                    {
                        p.sendMessage(reference.GAME + "§7 - §b" + Bukkit.getOfflinePlayer(uuid).getName() + "§f: " + kills.get(uuid));
                    }
                    x++;
                }
                else
                {
                    break;
                }
            }
        }
        return false;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
