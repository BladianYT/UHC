package me.bladian.uhc.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by BladianYT
 */
public class PlayerWinUHCEvent extends Event
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


    private final Player player;

    public PlayerWinUHCEvent(Player player)
    {
        this.player = player;
    }

    public Player getPlayer()
    {
        return player;
    }
}
