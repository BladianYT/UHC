package me.bladian.uhc.manager;

import me.bladian.uhc.Core;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class TeamManager
{

    private Reference reference = Core.instance.getReference();

    private Map<Integer, Team> teams = new HashMap<>();

    private int teamNumber = 1;
    private int teamSize = 2;

    private boolean management = true;

    public Map<Integer, Team> getTeams()
    {
        return teams;
    }

    public int getTeamNumber()
    {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
    }

    public int getTeamSize()
    {
        return teamSize;
    }

    public void setTeamSize(int teamSize)
    {
        this.teamSize = teamSize;
    }

    public boolean isManagement()
    {
        return management;
    }

    public void setManagement(boolean management)
    {
        this.management = management;
    }

    public void createTeam(UUID uuid, UHCPlayer uhcPlayer)
    {
        Player p = Bukkit.getPlayer(uuid);
        Team team = new Team(teamNumber, uuid);
        uhcPlayer.setTeamNumber(teamNumber);
        p.sendMessage(reference.TEAM + "You created§6 Team #" + teamNumber);
        teams.put(teamNumber, team);
        teamNumber = teamNumber + 1;
    }

    public void addPlayer(int teamID, UUID uuid, UHCPlayer uhcPlayer)
    {
        Team team = teams.get(teamID);
        team.getPlayers().add(uuid);
        uhcPlayer.setTeamNumber(teamID);
        sendMessage(teamID, reference.TEAM + "You joined§6 Team #" + teamID);
    }

    public void removePlayer(int teamID, UUID uuid, UHCPlayer uhcPlayer)
    {
        Team team = teams.get(teamID);
        team.getPlayers().remove(uuid);
        uhcPlayer.setTeamNumber(-1);
        Player p = Bukkit.getPlayer(uuid);
        if(team.getLeader().equals(uuid))
        {
            if(p != null)
            {
                p.sendMessage(reference.TEAM + "You left§6 Team #" + teamID);
            }
            if (team.getPlayers().size() != 0)
            {
                team.setLeader(team.getPlayers().get(0));
                sendMessage(teamID, reference.TEAM + Bukkit.getOfflinePlayer(uuid).getName() + "left§6 Team #" + teamID);
                sendMessage(teamID, reference.TEAM + "The new§6leader§f is now§6 " + Bukkit.getOfflinePlayer(team.getPlayers().get(0)).getName());
            }
            else
            {
                teams.remove(teamID);
            }
        }
        else
        {
            if(p != null)
            {
                p.sendMessage(reference.TEAM + "You left§6 Team #" + teamID);
                sendMessage(teamID, reference.TEAM + Bukkit.getOfflinePlayer(uuid).getName() + "left§6 Team #" + teamID);
            }
        }
    }

    public void kickPlayer(int teamID, UUID uuid, UHCPlayer uhcPlayer)
    {
        Team team = teams.get(teamID);
        team.getPlayers().remove(uuid);
        uhcPlayer.setTeamNumber(-1);
        Player p = Bukkit.getPlayer(uuid);
        if (p != null)
        {
            p.sendMessage(reference.TEAM + "You were kicked from§6 Team #" + teamID);
        }
        sendMessage(teamID, reference.TEAM + Bukkit.getOfflinePlayer(uuid).getName() + "left§6 Team #" + teamID);
    }

    public String getPrefix(int teamID)
    {
        Team team = teams.get(teamID);
        return team.getChatColor() + "Team " + teamID + "§8 :|:";
    }

    public void sendMessage(int teamID, String string)
    {
        Team team = teams.get(teamID);
        for(UUID uuid : team.getPlayers())
        {
            Player p = Bukkit.getPlayer(uuid);
            if(p != null)
            {
                p.sendMessage(string);
            }
        }
    }
}
