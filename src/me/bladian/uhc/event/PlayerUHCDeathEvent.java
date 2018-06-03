package me.bladian.uhc.event;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by BladianYT
 */
public class PlayerUHCDeathEvent extends Event
{

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    private Player killer;
    private OfflinePlayer offlinePlayer;

    public PlayerUHCDeathEvent(OfflinePlayer player, Player killer)
    {
        this.offlinePlayer = player;
        this.killer = killer;
    }

    public PlayerUHCDeathEvent(OfflinePlayer player)
    {
        this.offlinePlayer = player;
    }

    public Player getKiller()
    {
        return killer;
    }

    public OfflinePlayer getOfflinePlayer()
    {
        return offlinePlayer;
    }
}
