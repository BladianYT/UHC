package me.bladian.uhc.util;

import me.bladian.uhc.enums.ScatterType;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BladianYT
 */
public class Reference
{


    /*public static final String URL = "jdbc:mysql://51.255.93.148:3306/" + Reference.DATABASE;
    public static final String USER = "serenitymc";
    public static final String PASSWORD = "@4buqW$QgD+P69A9";
    public static final String DATABASE = "serenityv2";
    public static final String TABLE = "uhc_stats";*/

    public String URL = "jdbc:mysql://localhost:3306/uhc_stats";
    public String USER = "";
    public String PASSWORD = "";
    public final String TABLE = "uhc_stats";

    public Location SPAWN = new Location(Bukkit.getWorld("spawn"), Bukkit.getWorld("spawn").getSpawnLocation().getX(), Bukkit.getWorld("spawn").getSpawnLocation().getY(), Bukkit.getWorld("spawn").getSpawnLocation().getZ(), -90, 0);

    public String ARROW = "§8»";
    public String RULES_PREFIX = "§bRules " + ARROW + " ";
    public String SCENARIO = "§3Scenarios " + ARROW + "§f ";
    public String GAME = "§7Game " + ARROW + "§f ";
    public String BORDER = "§6Border " + ARROW + "§f ";
    public String ERROR = "§cError " + ARROW + "§c ";
    public String MODERATOR = "§dModerator " + ARROW + "§f ";
    public String TEAM = "§6Team " + ARROW + "§f ";

    public void setSQLInfo(String ip, String port, String database, String user, String password)
    {
        URL = "jdbc:mysql://" + ip + ":" + port + "/" + database;
        USER = user;
        PASSWORD = password;
    }

    public boolean DEATH_MESSAGES_ENABLED = true;
    public String PLAYER_DEATH_SWORD;
    public String PLAYER_DEATH_BOW;
    public String PLAYER_DEATH_UNKNOWN;
    public String COMBAT_DEATH;

    public boolean STATS = true;

    public boolean ARENA_ENABLED = true;
    public int ARENA_KIT;
    public List<Location> ARENA_LOCATIONS = new ArrayList<>();

    public void setArenaInfo(boolean arenaEnabled, int arenaKit, List<Location> locations)
    {
        this.ARENA_ENABLED = arenaEnabled;
        this.ARENA_KIT = arenaKit;
        this.ARENA_LOCATIONS = locations;
    }

    public void setARENA_KIT(int ARENA_KIT)
    {
        this.ARENA_KIT = ARENA_KIT;
    }

    public void setDeathMessageInfo(String swordDeath, String bowDeath, String unknownDeath, String combatDeath, boolean enabled)
    {
        this.PLAYER_DEATH_SWORD = swordDeath;
        this.PLAYER_DEATH_BOW = bowDeath;
        this.PLAYER_DEATH_UNKNOWN = unknownDeath;
        this.COMBAT_DEATH = combatDeath;
        this.DEATH_MESSAGES_ENABLED = enabled;
    }

    public List<String> RULES = new ArrayList<>();

    public void setRULES(List<String> RULES)
    {
        this.RULES = RULES;
    }

    public ScatterType SCATTER_TYPE = ScatterType.PLAYER;

    public void setSCATTER_TYPE(ScatterType SCATTER_TYPE)
    {
        this.SCATTER_TYPE = SCATTER_TYPE;
    }

    public boolean CAVE_ENABLED = false;
    public int CAVE_H_STRETCH = 16;
    public int CAVE_V_STRETCH = 9;
    public int CAVE_CUTOFF = 55;
    public int CAVE_MIN_Y = 6;
    public int CAVE_MAX_Y = 52;

    public void setCaveInfo(boolean enabled, int h, int v, int cutoff, int min, int max)
    {
        this.CAVE_ENABLED = enabled;
        this.CAVE_H_STRETCH = h;
        this.CAVE_V_STRETCH = v;
        this.CAVE_CUTOFF = cutoff;
        this.CAVE_MIN_Y = min;
        this.CAVE_MAX_Y = max;
    }

    public int NETHER_WORLDBORDER = 100;

    public void setNETHER_WORLDBORDER(int NETHER_WORLDBORDER)
    {
        this.NETHER_WORLDBORDER = NETHER_WORLDBORDER;
    }

    public String END_COMMAND = "restart";

    public void setEND_COMMAND(String END_COMMAND)
    {
        this.END_COMMAND = END_COMMAND;
    }

    //"CREATE TABLE uhc_stats(wins INT(100), kills INT(100), deaths INT(100), `sword-swings` INT(100), `sword-hits` INT(100), `bow-shots` INT(100), `bow-hits` INT(100), `hearts-dealt` INT(100), `hearts-lost` INT(100), `gapples-eaten` INT(100), `heads-eaten` INT(100),`coal-mined` INT(100), `iron-mined` INT(100), `gold-mined` INT(100), `lapis-mined` INT(100), `redstone-mined` INT(100), `diamond-mined` INT(100), `emerald-mined` INT(100), `blocks-broken` INT(100), `blocks-placed` INT(100), `nethers-entered` INT(100), `ends-entered` INT(100), `crafting-tables-crafted` INT(100),  `enchant-tables-crafted` INT(100),   `anvils-crafted` INT(100),   `furnaces-crafted` INT(100),  `golden-apples-crafted` INT(100),  `arrows-crafted` INT(100),  `fishing-rods-crafted` INT(100),  `bows-crafted` INT(100));"
}
