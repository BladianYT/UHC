package me.bladian.uhc.manager;

import org.bukkit.scheduler.BukkitTask;

import java.util.*;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class ScenarioManager
{

    private List<UUID> bestPVEList = new ArrayList<>();

    public List<UUID> getBestPVEList()
    {
        return bestPVEList;
    }

    private List<BukkitTask> bestPVEScheduler = new ArrayList<>();

    public List<BukkitTask> getBestPVEScheduler()
    {
        return bestPVEScheduler;
    }

    private List<UUID> noCleanList = new ArrayList<>();

    public List<UUID> getNoCleanList()
    {
        return noCleanList;
    }

    private Map<UUID, BukkitTask> noCleanScheduler = new HashMap<>();

    public Map<UUID, BukkitTask> getNoCleanScheduler()
    {
        return noCleanScheduler;
    }

    private Map<UUID, Integer> limitationOres = new HashMap<>();

    public Map<UUID, Integer> getLimitationOres()
    {
        return limitationOres;
    }

    private Map<UUID, UUID> doNotDisturbInfo = new HashMap<>();

    public Map<UUID, UUID> getDoNotDisturbInfo()
    {
        return doNotDisturbInfo;
    }

    private Map<UUID, BukkitTask> doNotDisturbTask = new HashMap<>();

    public Map<UUID, BukkitTask> getDoNotDisturbTask()
    {
        return doNotDisturbTask;
    }
}
