package me.bladian.uhc.event;

import me.bladian.uhc.team.Team;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by BladianYT
 */
public class TeamUHCWinEvent extends Event
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


    private final Team team;

    public TeamUHCWinEvent(Team team)
    {
        this.team = team;
    }

    public Team getTeam()
    {
        return team;
    }
}
