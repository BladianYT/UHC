package me.bladian.uhc.player;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.util.MySQL;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by BladianYT
 */
public class UHCPlayer
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util  = core.getUtil();

    private UUID uuid = null;

    private PlayerState playerState = PlayerState.LOBBY;

    private int teamNumber = -1;
    private Set<String> teamInvites = new HashSet<>();

    private int kills = 0;

    private Location scatter = null;

    private ItemStack[] itemStacks = null;
    private ItemStack[] armorItemStacks = null;
    private int levels = 0;
    private Location deathLocation = null;
    private int teamDeathNumber = -1;

    private UUID combatSkeletonUUID;

    private boolean xrayAlerts = true;

    private List<String> whitelistedPlayers = new ArrayList<>();

    private boolean frozen = false;

    private boolean teamChat = true;

    public boolean isTeamChat()
    {
        return teamChat;
    }

    public void setTeamChat(boolean teamChat)
    {
        this.teamChat = teamChat;
    }

    public boolean isFrozen()
    {
        return frozen;
    }

    public void setFrozen(boolean frozen)
    {
        this.frozen = frozen;
    }

    public int getTeamDeathNumber()
    {
        return teamDeathNumber;
    }

    public void setTeamDeathNumber(int teamDeathNumber)
    {
        this.teamDeathNumber = teamDeathNumber;
    }

    public void setLevels(int levels)
    {
        this.levels = levels;
    }

    public int getLevels()
    {
        return levels;
    }

    public UUID getCombatSkeletonUUID()
    {
        return combatSkeletonUUID;
    }

    public void setCombatSkeletonUUID(UUID combatSkeletonUUID)
    {
        this.combatSkeletonUUID = combatSkeletonUUID;
    }

    public Location getScatter()
    {
        return scatter;
    }

    public void setScatter(Location scatter)
    {
        this.scatter = scatter;
    }

    public ItemStack[] getItemStacks()
    {
        return itemStacks;
    }

    public void setItemStacks(ItemStack[] itemStacks)
    {
        this.itemStacks = itemStacks;
    }

    public ItemStack[] getArmorItemStacks()
    {
        return armorItemStacks;
    }

    public void setArmorItemStacks(ItemStack[] armorItemStacks)
    {
        this.armorItemStacks = armorItemStacks;
    }

    public Location getDeathLocation()
    {
        return deathLocation;
    }

    public void setDeathLocation(Location deathLocation)
    {
        this.deathLocation = deathLocation;
    }

    public int getTeamNumber()
    {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber)
    {
        this.teamNumber = teamNumber;
    }

    public Set<String> getTeamInvites()
    {
        return teamInvites;
    }

    private Stat wins = new Stat(Material.NETHER_STAR, "Wins");
    private Stat killsD = new Stat(Material.DIAMOND_SWORD, "Kills");
    private Stat deaths = new Stat(Material.SKULL_ITEM, "Deaths");

    private Stat swordSwings = new Stat(Material.DIAMOND_SWORD, "Sword Swings");
    private Stat swordHits = new Stat(Material.IRON_SWORD, "Sword Hits");
    private Stat bowShots = new Stat(Material.BOW, "Bow Shots");
    private Stat bowHits = new Stat(Material.ARROW, "Bow Hits");

    private Stat heartsDealt = new Stat(Material.DIAMOND_SWORD, "Hearts Dealt");
    private Stat heartsLost = new Stat(Material.FLINT, "Hearts Lost");
    private Stat gapplesEaten = new Stat(Material.GOLDEN_APPLE, "Golden Apples Eaten");
    private Stat headsEaten = new Stat(Material.GOLDEN_APPLE, "Golden Heads Eaten");

    private Stat coalMined = new Stat(Material.COAL_ORE, "Coal Ore Mined");
    private Stat ironMined = new Stat(Material.IRON_ORE, "Iron Ore Mined");
    private Stat goldMined = new Stat(Material.GOLD_ORE, "Gold Ore Mined");
    private Stat lapisMined = new Stat(Material.LAPIS_ORE, "Lapis Ore Mined");
    private Stat redstoneMined = new Stat(Material.REDSTONE_ORE, "Redstone Ore Mined");
    private Stat diamondMined = new Stat(Material.DIAMOND_ORE,"Diamond Ore Mined");
    private Stat emeraldMined = new Stat(Material.EMERALD_ORE, "Emerald Ore Mined");

    private Stat blocksBroken = new Stat(Material.DIAMOND_PICKAXE, "Blocks Broken");
    private Stat blocksPlaced = new Stat(Material.IRON_PICKAXE, "Blocks Placed");
    private Stat nethersEntered = new Stat(Material.OBSIDIAN, "Nethers Entered");

    public UHCPlayer(UUID uuid)
    {
        this.uuid = uuid;
    }

    public PlayerState getPlayerState()
    {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState)
    {
        this.playerState = playerState;
    }

    public int getKills()
    {
        return kills;
    }

    public void setKills(int kills)
    {
        this.kills = kills;
    }

    public boolean isXrayAlerts()
    {
        return xrayAlerts;
    }

    public void setXrayAlerts(boolean xrayAlerts)
    {
        this.xrayAlerts = xrayAlerts;
    }

    public void getInformation()
    {
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                Connection connection = MySQL.getConnection();
                PreparedStatement statement2 = null;
                ResultSet rs1 = null;
                PreparedStatement statement3 = null;
                try
                {
                    statement2 = connection.prepareStatement("SELECT * FROM `" + reference.TABLE + "` WHERE `uuid` = ? LIMIT 1;");
                    statement2.setString(1, uuid.toString());
                    statement2.executeQuery();
                    rs1 = statement2.getResultSet();
                    if (rs1.next())
                    {
                        wins.setAmount(rs1.getInt("wins"));
                        killsD.setAmount(rs1.getInt("kills"));
                        deaths.setAmount(rs1.getInt("deaths"));

                        swordSwings.setAmount(rs1.getInt("sword-swings"));
                        swordHits.setAmount(rs1.getInt("sword-hits"));
                        bowShots.setAmount(rs1.getInt("bow-shots"));
                        bowHits.setAmount(rs1.getInt("bow-hits"));

                        heartsDealt.setAmount(+ rs1.getInt("hearts-dealt"));
                        heartsLost.setAmount(+ rs1.getInt("hearts-lost"));
                        gapplesEaten.setAmount(rs1.getInt("gapples-eaten"));
                        headsEaten.setAmount(rs1.getInt("heads-eaten"));

                        coalMined.setAmount(rs1.getInt("coal-mined"));
                        ironMined.setAmount(rs1.getInt("iron-mined"));
                        goldMined.setAmount(rs1.getInt("gold-mined"));
                        lapisMined.setAmount(rs1.getInt("lapis-mined"));
                        redstoneMined.setAmount(rs1.getInt("redstone-mined"));
                        diamondMined.setAmount(rs1.getInt("diamond-mined"));
                        emeraldMined.setAmount(rs1.getInt("emerald-mined"));

                        blocksBroken.setAmount(rs1.getInt("blocks-broken"));
                        blocksPlaced.setAmount(rs1.getInt("blocks-placed"));
                        nethersEntered.setAmount(rs1.getInt("nethers-entered"));

                    }
                    else
                    {
                        statement3 = connection.prepareStatement("INSERT INTO `" + reference.TABLE + "` (uuid) VALUES (?);");
                        statement3.setString(1, uuid.toString());
                        statement3.executeUpdate();
                        statement3.close();
                    }
                } catch (SQLException ex)
                {
                    ex.printStackTrace();
                } finally
                {
                    try
                    {
                        if (statement3 != null)
                        {
                            statement3.close();
                        }
                        if (statement2 != null)
                        {
                            statement2.close();
                        }
                        if (rs1 != null)
                        {
                            rs1.close();
                        }
                    } catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskAsynchronously(core);
    }

    public void saveStats()
    {
        try
        {
            Connection connection = MySQL.getConnection();
            PreparedStatement statement = connection.prepareStatement("UPDATE " + reference.TABLE + " SET" +
                    " `wins` = " + wins.getAmount() +
                    ", `kills` = " + killsD.getAmount() +
                    ", `deaths` = " + deaths.getAmount() +

                    ", `sword-swings` = " + swordSwings.getAmount() +
                    ", `sword-hits` = " + swordHits.getAmount() +
                    ", `bow-shots` = " + bowShots.getAmount() +
                    ", `bow-hits` = " + bowHits.getAmount() +

                    ", `hearts-dealt` = " + heartsDealt.getAmount() +
                    ", `hearts-lost` = " + heartsLost.getAmount() +
                    ", `gapples-eaten` = " + gapplesEaten.getAmount() +
                    ", `heads-eaten` = " + headsEaten.getAmount() +

                    ", `coal-mined` = " + coalMined.getAmount() +
                    ", `iron-mined` = " + ironMined.getAmount() +
                    ", `gold-mined` = " + goldMined.getAmount() +
                    ", `lapis-mined` = " + lapisMined.getAmount() +
                    ", `redstone-mined` = " + redstoneMined.getAmount() +
                    ", `diamond-mined` = " + diamondMined.getAmount() +
                    ", `emerald-mined` = " + emeraldMined.getAmount() +

                    ", `blocks-broken` = " + blocksBroken.getAmount() +
                    ", `blocks-placed` = " + blocksPlaced.getAmount() +
                    ", `nethers-entered` = " + nethersEntered.getAmount() +
                    " WHERE `uuid` = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> getWhitelistedPlayers()
    {
        return whitelistedPlayers;
    }

    public Inventory getStatsInventory()
    {
        Inventory inventory = Bukkit.createInventory(null, 45, "§e§lSTATS:§f§l " + Bukkit.getOfflinePlayer(uuid).getName());
        inventory.setItem(0, wins.getItemStack());
        inventory.setItem(3, killsD.getItemStack());
        inventory.setItem(4, deaths.getItemStack());
        inventory.setItem(5, util.createItem(Material.TRIPWIRE_HOOK, "§e§lKDR:§f§l " + getFinalKDR(killsD.getAmount(), deaths.getAmount())));

        inventory.setItem(9, swordSwings.getItemStack());
        inventory.setItem(10, swordHits.getItemStack());
        inventory.setItem(11, util.createItem(Material.GOLD_SWORD, "§e§lSWORD ACCURACY:§f§l " + getPercentage(swordHits.getAmount(), swordSwings.getAmount()) ));
        inventory.setItem(15, bowShots.getItemStack());
        inventory.setItem(16, bowHits.getItemStack());
        inventory.setItem(17, util.createItem(Material.FLINT, "§e§lBOW ACCURACY:§f§l " + getPercentage(bowHits.getAmount(), bowShots.getAmount())));

        inventory.setItem(18, heartsDealt.getItemStack());
        inventory.setItem(19, heartsLost.getItemStack());
        inventory.setItem(25, gapplesEaten.getItemStack());
        inventory.setItem(26, headsEaten.getItemStack());


        inventory.setItem(27, coalMined.getItemStack());
        inventory.setItem(28, ironMined.getItemStack());
        inventory.setItem(29, goldMined.getItemStack());
        inventory.setItem(30, lapisMined.getItemStack());
        inventory.setItem(31, redstoneMined.getItemStack());
        inventory.setItem(32, diamondMined.getItemStack());
        inventory.setItem(33, emeraldMined.getItemStack());

        inventory.setItem(36, blocksBroken.getItemStack());
        inventory.setItem(37, blocksPlaced.getItemStack());
        inventory.setItem(39, nethersEntered.getItemStack());

        return inventory;
    }

    public Stat getWins()
    {
        return wins;
    }

    public Stat getKillsD()
    {
        return killsD;
    }

    public Stat getDeaths()
    {
        return deaths;
    }
    public Stat getSwordSwings()
    {
        return swordSwings;
    }

    public Stat getSwordHits()
    {
        return swordHits;
    }
    public Stat getBowShots()
    {
        return bowShots;
    }

    public Stat getBowHits()
    {
        return bowHits;
    }
    public Stat getHeartsDealt()
    {
        return heartsDealt;
    }

    public Stat getHeartsLost()
    {
        return heartsLost;
    }

    public Stat getGapplesEaten()
    {
        return gapplesEaten;
    }

    public Stat getHeadsEaten()
    {
        return headsEaten;
    }

    public Stat getCoalMined()
    {
        return coalMined;
    }

    public Stat getIronMined()
    {
        return ironMined;
    }

    public Stat getGoldMined()
    {
        return goldMined;
    }

    public Stat getLapisMined()
    {
        return lapisMined;
    }

    public Stat getRedstoneMined()
    {
        return redstoneMined;
    }

    public Stat getDiamondMined()
    {
        return diamondMined;
    }

    public Stat getEmeraldMined()
    {
        return emeraldMined;
    }

    public Stat getBlocksBroken()
    {
        return blocksBroken;
    }

    public Stat getBlocksPlaced()
    {
        return blocksPlaced;
    }

    public Stat getNethersEntered()
    {
        return nethersEntered;
    }

    public UUID getUuid()
    {
        return uuid;
    }

    private double getFinal(double number, double number2)
    {
        if (number != 0 && number2 == 0)
        {
            return number;
        }
        if (number == 0 && number2 != 0)
        {
            return 0;
        }
        double x = number / number2;
        return x*100;
    }

    private double getFinalKDR(double number, double number2)
    {
        if (number != 0 && number2 == 0)
        {
            return number;
        }
        if (number == 0 && number2 != 0)
        {
            return 0;
        }
        return number / number2;
    }

    private final DecimalFormat df = new DecimalFormat("###.##");

    private String getPercentage(double number, double number2)
    {

        double d = getFinal(number, number2);
        return df.format(d) + "%";
    }
}
