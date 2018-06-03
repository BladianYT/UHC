package me.bladian.uhc.manager;

import me.bladian.uhc.Core;
import me.bladian.uhc.Inventories;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.util.Reference;

import java.util.*;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class PlayerManager
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Inventories inventories = core.getInventories();

    private Map<UUID, UHCPlayer> uhcPlayerMap = new HashMap<>();

    public Map<UUID, UHCPlayer> getUhcPlayerMap()
    {
        return uhcPlayerMap;
    }

    private UUID host = null;
    private List<UUID> moderators = new ArrayList<>();
    private List<UUID> spectators = new ArrayList<>();

    public UUID getHost()
    {
        return host;
    }

    public void setHost(UUID host)
    {
        this.host = host;
    }

    public List<UUID> getModerators()
    {
        return moderators;
    }

    public List<UUID> getSpectators()
    {
        return spectators;
    }
}
