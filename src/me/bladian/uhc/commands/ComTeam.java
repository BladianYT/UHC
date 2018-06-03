package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.manager.TeamManager;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by BladianYT on 03/03/16.
 */
public class ComTeam implements CommandExecutor, TabExecutor
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private TeamManager teamManager = core.getTeamManager();
    private GameManager gameManager = core.getGameManager();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if (command.getName().equalsIgnoreCase("team"))
        {
            Player p = (Player) commandSender;
            UUID uuid = p.getUniqueId();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if (gameManager.getTeamType() == TeamType.SOLO)
            {
                p.sendMessage(reference.ERROR + "Teams are not enabled");
                return true;
            }
            if (strings.length == 0)
            {
                p.sendMessage(reference.ERROR + "/team create");
                p.sendMessage(reference.ERROR + "/team size <number>");
                p.sendMessage(reference.ERROR + "/team invite <name>");
                p.sendMessage(reference.ERROR + "/team accept <name>");
                p.sendMessage(reference.ERROR + "/team kick <name>");
                p.sendMessage(reference.ERROR + "/team leave");
                p.sendMessage(reference.ERROR + "/team list <name>");
                p.sendMessage(reference.ERROR + "/tc <message>");
                p.sendMessage(reference.ERROR + "/tctoggle");
                p.sendMessage(reference.ERROR + "/sc");
            }
            else
            {
                if (strings[0].equalsIgnoreCase("size"))
                {
                    if (commandSender.hasPermission("rank.host"))
                    {
                        if (gameManager.getGameState() != GameState.LOBBY)
                        {
                            p.sendMessage(reference.ERROR + "The game has already §cstarted");
                            return true;
                        }
                        if (gameManager.getTeamType() != TeamType.TEAMS)
                        {
                            p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                            return true;
                        }
                        if (uhcPlayer.getPlayerState() != PlayerState.LOBBY && uhcPlayer.getPlayerState() != PlayerState.MODERATOR && uhcPlayer.getPlayerState() != PlayerState.HOST)
                        {
                            p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                            return true;
                        }
                        if (strings.length < 2)
                        {
                            p.sendMessage(reference.ERROR + "/team size <size>");
                        }
                        else
                        {
                            if (isNumeric(strings[1]))
                            {
                                int size = Integer.parseInt(strings[1]);
                                teamManager.setTeamSize(size);
                                Bukkit.broadcastMessage(reference.TEAM + "§6Team size§7 has been set to§6 " + size);
                                return true;
                            }
                            else
                            {
                                p.sendMessage(reference.ERROR + "§cTeam size must be a number");
                                return true;
                            }
                        }
                    }
                }

                if (strings[0].equalsIgnoreCase("create"))
                {
                    if (gameManager.getGameState() != GameState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "The game has already §cstarted");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    if (uhcPlayer.getPlayerState() != PlayerState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                        return true;
                    }
                    if (getPlayerTeamID(uuid) != -1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou're already in a team");
                    }
                    else
                    {
                        teamManager.createTeam(uuid, uhcPlayer);
                        return true;
                    }
                }
                if (strings[0].equalsIgnoreCase("leave"))
                {
                    if (gameManager.getGameState() != GameState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "The game has already §cstarted");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    if (uhcPlayer.getPlayerState() != PlayerState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                        return true;
                    }
                    if (getPlayerTeamID(uuid) == -1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou're not in a team");
                    }
                    else
                    {
                        int teamID = uhcPlayer.getTeamNumber();
                        teamManager.removePlayer(teamID, uuid, uhcPlayer);
                    }
                }
                if (strings[0].equalsIgnoreCase("list"))
                {
                    if (strings.length < 2)
                    {
                        commandSender.sendMessage("§c/team list <name>");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    Player t = Bukkit.getPlayer(strings[1]);
                    if (t == null)
                    {
                        p.sendMessage(reference.ERROR + "§cThat player isn't online");
                        return true;
                    }
                    int teamID = getPlayerTeamID(t.getUniqueId());
                    if (teamID == -1)
                    {
                        p.sendMessage(reference.ERROR + "§cThe user is not in a team");
                        return true;
                    }
                    Team team = teamManager.getTeams().get(teamID);
                    p.sendMessage(reference.TEAM + "§c" + t.getName() + "'s team:");
                    for (UUID allUUID : team.getPlayers())
                    {
                        OfflinePlayer all = Bukkit.getOfflinePlayer(allUUID);
                        UHCPlayer uhcPlayerAll = playerManager.getUhcPlayerMap().get(allUUID);
                        if (uhcPlayerAll.getPlayerState() == PlayerState.INGAME)
                        {
                            p.sendMessage(reference.TEAM + "§7 - §a" + all.getName());
                        }
                        else
                        {
                            p.sendMessage(reference.TEAM + "§7 - §c" + all.getName());
                        }
                    }
                    return true;
                }

                if (strings[0].equalsIgnoreCase("kick"))
                {
                    if (strings.length < 2)
                    {
                        commandSender.sendMessage("§c/team list <name>");
                        return true;
                    }
                    if (gameManager.getGameState() != GameState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "The game has already §cstarted");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    if (uhcPlayer.getPlayerState() != PlayerState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                        return true;
                    }
                    if (getPlayerTeamID(uuid) == -1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou're not in a team");
                        return true;
                    }
                    Player t = Bukkit.getPlayer(strings[1]);
                    if (t == null)
                    {
                        p.sendMessage(reference.ERROR + "§cPlayer isn't currently online, if you want to create a new team do /team leave");
                        return true;
                    }
                    int teamID = getPlayerTeamID(uuid);
                    Team team = teamManager.getTeams().get(teamID);
                    if (!team.getPlayers().contains(t.getUniqueId()))
                    {
                        p.sendMessage(reference.ERROR + "§cThat player isn't in your team");
                        return true;
                    }
                    teamManager.kickPlayer(teamID, uuid, uhcPlayer);
                    return true;
                }
                else if (strings[0].equalsIgnoreCase("invite"))
                {
                    if (gameManager.getGameState() != GameState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "The game has already §cstarted");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    if (uhcPlayer.getPlayerState() != PlayerState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                        return true;
                    }
                    if (getPlayerTeamID(uuid) == -1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou're not in a team");
                        return true;
                    }
                    if(strings.length < 2)
                    {
                        p.sendMessage(reference.ERROR + "§c/team invite <player>");
                        return true;
                    }
                    if (!isPlayerLeader(uuid))
                    {
                        p.sendMessage(reference.ERROR + "§cOnly the team leader can invite players");
                        return true;
                    }
                    Team team = teamManager.getTeams().get(getPlayerTeamID(uuid));
                    if (team.getPlayers().size() == teamManager.getTeamSize())
                    {
                        p.sendMessage(reference.ERROR + "§cMaximum team size is " + teamManager.getTeamSize());
                        return true;
                    }
                    Player t = Bukkit.getPlayer(strings[1]);
                    if (t == null)
                    {
                        p.sendMessage(reference.ERROR + "§cPlayer isn't currently online");
                        return true;
                    }
                    if (p.getUniqueId().equals(t.getUniqueId()))
                    {
                        p.sendMessage(reference.ERROR + "§cYou can't invite yourself");
                        return true;
                    }
                    UHCPlayer uhcPlayer1 = playerManager.getUhcPlayerMap().get(t.getUniqueId());
                    uhcPlayer1.getTeamInvites().add(p.getName());
                    t.sendMessage(reference.TEAM + "§6" + p.getName() + "§f has§6 invited§f you to their team");
                    t.sendMessage(reference.TEAM + "Type§6 /team accept " + p.getName());
                    p.sendMessage(reference.TEAM + "§6" + t.getName() + "§f has been§6 invited§f to the team");
                    return true;

                }
                else if (strings[0].equalsIgnoreCase("accept"))
                {
                    if (gameManager.getGameState() != GameState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "The game has already §cstarted");
                        return true;
                    }
                    if (gameManager.getTeamType() != TeamType.TEAMS)
                    {
                        p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                        return true;
                    }
                    if (uhcPlayer.getPlayerState() != PlayerState.LOBBY)
                    {
                        p.sendMessage(reference.ERROR + "§cYou need to be in lobby mode to use this command");
                        return true;
                    }
                    if (getPlayerTeamID(uuid) != -1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou're already in a team");
                        return true;
                    }
                    Player t = Bukkit.getPlayer(strings[1]);
                    if (t == null)
                    {
                        p.sendMessage(reference.ERROR + "§cThat player isn't online");
                        return true;
                    }
                    if (!uhcPlayer.getTeamInvites().contains(t.getName()))
                    {
                        p.sendMessage(reference.ERROR + "§cYou have no invites from that player");
                        return true;
                    }
                    Team team = teamManager.getTeams().get(getPlayerTeamID(t.getUniqueId()));
                    int teamID = team.getTeamNumber();
                    if (team.getPlayers().size() == teamManager.getTeamSize())
                    {
                        p.sendMessage(reference.ERROR + "§cThe team already has " + teamManager.getTeamSize() + " players");
                        return true;
                    }
                    teamManager.addPlayer(teamID, uuid, uhcPlayer);
                    return true;
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("tc"))
        {
            Player p = (Player) commandSender;
            UUID uuid = p.getUniqueId();
            if(strings.length == 0)
            {
                p.sendMessage(reference.ERROR + "/tc <message>");
                return true;
            }
            if (gameManager.getTeamType() != TeamType.TEAMS)
            {
                p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                return true;
            }
            if (getPlayerTeamID(uuid) == -1)
            {
                p.sendMessage(reference.ERROR + "§cYou're not in a team");
                return true;
            }
            Team team = teamManager.getTeams().get(getPlayerTeamID(uuid));
            for (UUID players : team.getPlayers())
            {
                Player t = Bukkit.getPlayer(players);
                if (t != null)
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(t.getUniqueId());
                    if(uhcPlayer.isTeamChat())
                    {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("§8[§dTC§8]§e").append(p.getName()).append(": ");
                        for (String s1 : strings)
                        {
                            stringBuilder.append(s1).append(" ");
                        }
                        t.sendMessage(stringBuilder.toString());
                    }
                }
            }
        }
        else if (command.getName().equalsIgnoreCase("tctoggle"))
        {
            Player p = (Player) commandSender;
            UUID uuid = p.getUniqueId();
            if (gameManager.getTeamType() != TeamType.TEAMS)
            {
                p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                return true;
            }
            if (getPlayerTeamID(uuid) == -1)
            {
                p.sendMessage(reference.ERROR + "§cYou're not in a team");
                return true;
            }
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if(uhcPlayer.isTeamChat())
            {
                uhcPlayer.setTeamChat(false);
                p.sendMessage(reference.TEAM + "You have§6 disabled§f team chat");
            }
            else
            {
                uhcPlayer.setTeamChat(true);
                p.sendMessage(reference.TEAM + "You have§6 enabled§f team chat");
            }
        }
        else if (command.getName().equalsIgnoreCase("sc"))
        {
            Player p = (Player) commandSender;
            UUID uuid = p.getUniqueId();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if (gameManager.getGameState() != GameState.STARTED)
            {
                p.sendMessage(reference.ERROR + "The game hasn't started");
                return true;
            }
            if (gameManager.getTeamType() != TeamType.TEAMS)
            {
                p.sendMessage(reference.ERROR + "§cTeams are not currently enabled");
                return true;
            }
            if (uhcPlayer.getPlayerState() != PlayerState.INGAME)
            {
                p.sendMessage(reference.ERROR + "§cYou need to be in game to use this command");
                return true;
            }
            else
            {
                Team team = teamManager.getTeams().get(uhcPlayer.getTeamNumber());
                for (UUID players : team.getAlivePlayers())
                {
                    Player t = Bukkit.getPlayer(players);
                    if (t != null)
                    {
                        String message = reference.TEAM + "§6" + p.getName() + "'s Coordinates:§7 " + p.getLocation().getBlockX() + "/" + p.getLocation().getBlockY() + "/" + p.getLocation().getBlockZ();
                        t.sendMessage(message);
                    }
                }
            }
        }
        return false;
    }


    public boolean isPlayerLeader(UUID uuid) {
        return teamManager.getTeams().get(getPlayerTeamID(uuid)).getLeader().equals(uuid);
    }

    public int getPlayerTeamID(UUID uuid)
    {
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
        return uhcPlayer.getTeamNumber();
    }

    private boolean isNumeric(String string) {
        try {
            @SuppressWarnings("UnusedAssignment") double d = Double.parseDouble(string);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("team"))
        {
            if(strings[0].equalsIgnoreCase("invite") || strings[0].equalsIgnoreCase("accept") || strings[0].equalsIgnoreCase("kick") || strings[0].equalsIgnoreCase("list"))
            {
                List<String> players = new ArrayList<>();
                for(Player p : Bukkit.getOnlinePlayers())
                {
                    if(p.getName().toLowerCase().startsWith(strings[0].toLowerCase()))
                    {
                        players.add(p.getName());
                    }
                }
                return players;
            }
        }
        return null;
    }
}
