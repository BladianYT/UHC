package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ComArena implements CommandExecutor
{
    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();
    private Inventories inventories = core.getInventories();

    int location = 0;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("arena"))
        {
            Player p = (Player) commandSender;
            if(gameManager.getGameState() != GameState.LOBBY)
            {
                p.sendMessage(reference.ERROR + "Can't join arena at the current time");
                return true;
            }
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() != PlayerState.LOBBY)
            {
                p.sendMessage(reference.ERROR + "You have to be in lobby to use this");
                return true;
            }
            if(uhcPlayer.getPlayerState() == PlayerState.ARENA)
            {
                p.sendMessage(reference.ERROR + "You're already in Arena");
                return true;
            }
            if(!reference.ARENA_ENABLED)
            {
                p.sendMessage(reference.ERROR + "Arena is not currently enabled");
                return true;
            }
            if(reference.ARENA_LOCATIONS.size() >= location+1)
            {
                location = 0;
            }
            Location locations = reference.ARENA_LOCATIONS.get(location);
            p.teleport(locations);
            inventories.arena(p, reference.ARENA_KIT);
            p.sendMessage(reference.GAME + "§fYou've joined the§7 Arena");
            uhcPlayer.setPlayerState(PlayerState.ARENA);
            gameManager.getArenaPlayers().add(p.getUniqueId());
            for(UUID all : gameManager.getArenaPlayers())
            {
                Player allP = Bukkit.getPlayer(all);
                if(allP != null)
                {
                    allP.showPlayer(p);
                    p.showPlayer(allP);
                }
            }
            location++;
        }
        return false;
    }
}
