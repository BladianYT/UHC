package me.bladian.uhc.event;

import me.bladian.uhc.enums.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class GameStateChangeEvent extends Event
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


    private GameState gameState;

    public GameStateChangeEvent(GameState gameState)
    {
        this.gameState = gameState;
    }

    public GameState getGameState()
    {
        return gameState;
    }
}
