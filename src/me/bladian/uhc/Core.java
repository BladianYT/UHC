package me.bladian.uhc;

import com.khorn.terraincontrol.TerrainControl;
import me.bladian.uhc.combat.CombatSkeleton;
import me.bladian.uhc.combat.MobUtil;
import me.bladian.uhc.combat.PlaceHolder;
import me.bladian.uhc.commands.*;
import me.bladian.uhc.commands.ComBackPack;
import me.bladian.uhc.commands.ComTeam;
import me.bladian.uhc.commands.editor.ComGameEditor;
import me.bladian.uhc.commands.editor.ComScenarioEditor;
import me.bladian.uhc.commands.editor.ComWorldEditor;
import me.bladian.uhc.commands.time.ComBorderTime;
import me.bladian.uhc.commands.time.ComHealTime;
import me.bladian.uhc.commands.time.ComPVPTime;
import me.bladian.uhc.config.ComConfig;
import me.bladian.uhc.config.EventConfig;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.ScatterType;
import me.bladian.uhc.manager.*;
import me.bladian.uhc.manager.WorldManager;
import me.bladian.uhc.player.EventStat;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.ComScenario;
import me.bladian.uhc.scenarios.EventScenario;
import me.bladian.uhc.util.*;
import me.bladian.uhc.world.generator.BiomeSwap;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BladianYT
 */
public class Core extends JavaPlugin
{

    public static Core instance;

    private Reference reference;

    public Reference getReference()
    {
        return reference;
    }

    private Util util;

    public Util getUtil()
    {
        return util;
    }

    private Inventories inventories;

    public Inventories getInventories()
    {
        return inventories;
    }

    private ConfigManager configManager;

    public ConfigManager getConfigManager()
    {
        return configManager;
    }

    private ScenarioManager scenarioManager;

    public ScenarioManager getScenarioManager()
    {
        return scenarioManager;
    }

    private PlayerManager playerManager;

    public PlayerManager getPlayerManager()
    {
        return playerManager;
    }

    private TeamManager teamManager;

    public TeamManager getTeamManager()
    {
        return teamManager;
    }

    private GameManager gameManager;

    public GameManager getGameManager()
    {
        return gameManager;
    }

    private WorldManager worldManager;

    public WorldManager getWorldManager()
    {
        return worldManager;
    }

    private ScoreboardManager scoreboardManager;

    public ScoreboardManager getScoreboardManager()
    {
        return scoreboardManager;
    }

    @Override
    public void onEnable()
    {
        instance = this;

        saveDefaultConfig();

        ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§eGolden Head");
        itemStack.setItemMeta(itemMeta);
        ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape("&&&", "&/&", "&&&");
        shapedRecipe.setIngredient('&', Material.GOLD_INGOT);
        shapedRecipe.setIngredient('/', Material.SKULL_ITEM, 3);
        Bukkit.getServer().addRecipe(shapedRecipe);

        /*BiomeSwap biomeSwap = new BiomeSwap();
        biomeSwap.startWorldGen();*/
        Bukkit.createWorld(new WorldCreator("spawn"));

        reference = new Reference();
        util = new Util();
        configManager = new ConfigManager();
        scenarioManager = new ScenarioManager();
        playerManager = new PlayerManager();
        teamManager = new TeamManager();
        gameManager = new GameManager();
        worldManager = new WorldManager();
        inventories = new Inventories();
        scoreboardManager = new ScoreboardManager();

        util.setMOTD(GameState.LOBBY.toString());

        configManager.setSpectatorRadius(getConfig().getInt("spectator-radius"));

        reference.setSQLInfo(
                getConfig().getString("sql.ip"),
                getConfig().getString("sql.port"),
                getConfig().getString("sql.database"),
                getConfig().getString("sql.user"),
                getConfig().getString("sql.password")
        );

        List<Location> locations = new ArrayList<>();
        for (String string : getConfig().getStringList("arena.locations"))
        {
            String[] split = string.split(";");
            World world = Bukkit.getWorld(split[0]);
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            float yaw = Float.parseFloat(split[4]);
            float pitch = Float.parseFloat(split[5]);
            Location location = new Location(world, x, y, z, yaw, pitch);
            locations.add(location);
        }
        reference.setArenaInfo(getConfig().getBoolean("arena.enabled"), getConfig().getInt("arena.kit"), locations);

        reference.setRULES(getConfig().getStringList("rules"));

        reference.setSCATTER_TYPE(ScatterType.valueOf(getConfig().getString("scatter")));

        reference.setDeathMessageInfo(
                getConfig().getString("death-message.sword-death"),
                getConfig().getString("death-message.bow-death"),
                getConfig().getString("death-message.unknown-death"),
                getConfig().getString("death-message.combat-logger-death"),
                getConfig().getBoolean("death-message.enabled"));

        reference.setCaveInfo(
                getConfig().getBoolean("custom-caves.enabled"),
                getConfig().getInt("custom-caves.horizontal-stretch"),
                getConfig().getInt("custom-caves.vertical-stretch"),
                getConfig().getInt("custom-caves.cutoff"),
                getConfig().getInt("custom-caves.min"),
                getConfig().getInt("custom-caves.max"));

        reference.setNETHER_WORLDBORDER(getConfig().getInt("nether-worldborder"));

        reference.setEND_COMMAND(getConfig().getString("end-command"));

        if(reference.STATS)
        {
            MySQL.openConnection();
        }

        if(reference.STATS && MySQL.isConnectionSuccessful())
        {
            MySQL.createTable();
        }


        MobUtil.getInstance().registerEntityEdit(CombatSkeleton.class, 51);
        MobUtil.getInstance().registerEntity(PlaceHolder.class, 100);

        getCommand("clear").setExecutor(new ComClear());
        getCommand("heal").setExecutor(new ComHeal());
        getCommand("health").setExecutor(new ComHealth());
        ComStaff comStaff = new ComStaff();
        getCommand("mod").setExecutor(comStaff);
        getCommand("host").setExecutor(comStaff);
        getCommand("start").setExecutor(new ComStart());
        getCommand("stats").setExecutor(new ComStat());
        getCommand("tele").setExecutor(new ComTele());
        getCommand("clearchat").setExecutor(new ComChat());
        getCommand("game").setExecutor(new ComGame());
        ComWhitelist comWhitelist = new ComWhitelist();
        getCommand("whitelist").setExecutor(comWhitelist);
        getCommand("dWhitelist").setExecutor(comWhitelist);
        getCommand("helpop").setExecutor(new ComHelpOp());
        getCommand("timer").setExecutor(new ComTimer());
        getCommand("kt").setExecutor(new ComKills());
        getCommand("scenarios").setExecutor(new ComScenario());
        getCommand("list").setExecutor(new ComList());
        getCommand("config").setExecutor(new ComConfig());
        getCommand("maxplayers").setExecutor(new ComMaxPlayers());
        getCommand("kc").setExecutor(new ComKillCount());
        getCommand("rules").setExecutor(new ComRules());
        getCommand("giveAll").setExecutor(new ComGiveAll());
        getCommand("xAlerts").setExecutor(new ComXAlerts());

        ComTeam comTeam = new ComTeam();
        getCommand("team").setExecutor(comTeam);
        getCommand("team").setTabCompleter(comTeam);
        getCommand("sc").setExecutor(comTeam);
        getCommand("tc").setExecutor(comTeam);
        getCommand("tctoggle").setExecutor(comTeam);

        getCommand("bp").setExecutor(new ComBackPack());

        getCommand("rates").setExecutor(new ComRates());

        ComScenarioEditor comScenarioEditor = new ComScenarioEditor();
        getCommand("scenarioEditor").setExecutor(comScenarioEditor);
        getServer().getPluginManager().registerEvents(comScenarioEditor, this);
        getCommand("worldEditor").setExecutor(new ComWorldEditor());
        ComGameEditor comGameEditor = new ComGameEditor();
        getCommand("gameEditor").setExecutor(comGameEditor);
        getServer().getPluginManager().registerEvents(comGameEditor, this);

        ComArenaEditor comArenaEditor = new ComArenaEditor();
        getCommand("arenaEditor").setExecutor(comArenaEditor);
        getServer().getPluginManager().registerEvents(comArenaEditor, this);

        getCommand("arena").setExecutor(new ComArena());

        getCommand("respawn").setExecutor(new ComRespawn());

        getCommand("healTime").setExecutor(new ComHealTime());
        getCommand("pvpTime").setExecutor(new ComPVPTime());
        getCommand("borderTime").setExecutor(new ComBorderTime());

        ComBorderRadius comBorderRadius = new ComBorderRadius();
        getCommand("borderRadius").setExecutor(comBorderRadius);
        getServer().getPluginManager().registerEvents(comBorderRadius, this);

        getServer().getPluginManager().registerEvents(new Event(), this);
        if(reference.STATS && MySQL.isConnectionSuccessful())
        {
            getServer().getPluginManager().registerEvents(new EventStat(), this);
        }
        getServer().getPluginManager().registerEvents(new EventScenario(), this);
        getServer().getPluginManager().registerEvents(new EventConfig(), this);

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        World uhc = Bukkit.createWorld(new WorldCreator("uhc").environment(World.Environment.NORMAL).type(WorldType.NORMAL).generator("TerrainControl"));
        uhc.setGameRuleValue("doDaylightCycle", "false");
        uhc.setTime(0);
        uhc.setGameRuleValue("naturalRegeneration", "false");

        World uhcNether = Bukkit.createWorld(new WorldCreator("uhc_nether").environment(World.Environment.NETHER).type(WorldType.NORMAL));
        uhcNether.setGameRuleValue("doDaylightCycle", "false");
        uhcNether.setTime(0);
        uhcNether.setGameRuleValue("naturalRegeneration", "false");

        World world = Bukkit.createWorld(new WorldCreator("spawn").environment(World.Environment.NORMAL).type(WorldType.NORMAL));
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setTime(13000);
        world.setDifficulty(Difficulty.PEACEFUL);

        for(Player p : Bukkit.getOnlinePlayers())
        {
            UHCPlayer UHCPlayer = new UHCPlayer(p.getUniqueId());
            playerManager.getUhcPlayerMap().put(p.getUniqueId(), UHCPlayer);
            scoreboardManager.createScoreboard(p, UHCPlayer);
            if(reference.STATS && MySQL.isConnectionSuccessful())
            {
                UHCPlayer.getInformation();
            }
        }
    }

    @Override
    public void onDisable()
    {
        instance = null;

        if(reference.STATS && MySQL.isConnectionSuccessful())
        {
            MySQL.closeConnection();
        }
    }

}

//Game add
//Relog a couple bugs
//ConfigEditor needs to be implemented and changed accordingly
//PVP time
//Border time
/*
/gameeditor
Scenario editor
World editor

 */



/*
Config - http://prntscr.com/c9ivem


 - CONFIG - http://prntscr.com/c9ivem


 */

/*
- Fixed teams not being removed, not allowing for a game to end
- /Explain command was removed, it's all in /scenarios
- Edited a couple of the rules and made them simpler
- Added armour to the Combat Skeleton
- Fixed team wins message
- Added a rates command /rates <apple/flint> <percent>
- Fixed scoreboard not updated in team changes
- Removed /alertStart, as need to work on a Bungee System (will add back soon)
- Fixed the respawn command, only use it when a player has died though, if you want to add a player use /game add <player>
 */