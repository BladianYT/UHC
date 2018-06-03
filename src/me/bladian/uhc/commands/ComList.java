package me.bladian.uhc.commands;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by BladianYT
 */
public class ComList implements CommandExecutor
{
    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();
    private Util util = core.getUtil();


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings)
    {
        if(command.getName().equalsIgnoreCase("list"))
        {

            commandSender.sendMessage("§8§m-------------------------");
            commandSender.sendMessage("§ePlayers Online:§7 " + Bukkit.getOnlinePlayers().size());
            commandSender.sendMessage("§ePlayers Alive:§7 " + gameManager.getPlayers().size());
            commandSender.sendMessage("§eSpectators:§7 " + playerManager.getSpectators().size());
            commandSender.sendMessage("");
            String host = "";
            if(playerManager.getHost() != null)
            {
                host = Bukkit.getOfflinePlayer(playerManager.getHost()).getName();
            }
            else
            {
                host = "N/A";
            }
            List<String> mods = new ArrayList<>();
            for(UUID uuid : playerManager.getModerators())
            {
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                if(!name.equals(host))
                {
                    mods.add(name);
                }
            }
            commandSender.sendMessage("§eHost:§f " + host);
            commandSender.sendMessage("§eMods:§f " + Arrays.toString(mods.toArray()));
            commandSender.sendMessage("§8§m-----------------------");
        }
        return false;
    }
}
