package me.bladian.uhc.team;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by BladianYT
 */
public class Team
{

    private int teamNumber;
    private ChatColor chatColor;

    private int kills = 0;

    private List<UUID> alivePlayers = new ArrayList<>();
    private List<UUID> players = new ArrayList<>();

    private UUID leader;

    private Inventory inventory = Bukkit.createInventory(null, 27, "§e§lBACKPACK");

    public Team(int team, UUID uuid)
    {
        this.teamNumber = team;
        this.chatColor = ChatColor.GOLD;
        this.leader = uuid;

        players.add(uuid);
    }

    public int getTeamNumber()
    {
        return teamNumber;
    }

    public List<UUID> getAlivePlayers()
    {
        return alivePlayers;
    }

    public int getKills()
    {
        return kills;
    }

    public void setKills(int kills)
    {
        this.kills = kills;
    }

    public Inventory getInventory()
    {
        return inventory;
    }

    public List<UUID> getPlayers()
    {
        return players;
    }

    public UUID getLeader()
    {
        return leader;
    }

    public void setLeader(UUID leader)
    {
        this.leader = leader;
    }

    public ChatColor getChatColor()
    {
        return chatColor;
    }
}

