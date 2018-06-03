package me.bladian.uhc.manager;

import me.bladian.uhc.Core;
import me.bladian.uhc.combat.MobUtil;
import me.bladian.uhc.combat.PlaceHolder;
import me.bladian.uhc.config.BorderSize;
import me.bladian.uhc.enums.*;
import me.bladian.uhc.event.GameStateChangeEvent;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.Reference;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;


/**
 * Created by BladianYT
 */
public class GameManager
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private ConfigManager configManager = core.getConfigManager();
    private ScenarioManager scenarioManager = core.getScenarioManager();
    private PlayerManager playerManager = core.getPlayerManager();
    private TeamManager teamManager = core.getTeamManager();


    private GameState gameState = GameState.LOBBY;
    private TeamType teamType = TeamType.SOLO;

    private List<UUID> players = new ArrayList<>();

    private List<UUID> arenaPlayers = new ArrayList<>();

    private boolean PVP = false;

    private int borderNumber = 1;
    private int borderStartingNumber = 2;

    private List<UUID> whitelist = new ArrayList<>();
    private boolean donatorWhitelistEnabled = true;
    private boolean whitelistEnabled = true;

    private int maxPlayers = 250;

    private int timer = 0;

    private int healTime = 10;
    private int pvpTime = 20;
    private int borderTime = 35;

    private int borderRadius = 2500;

    private  int appleRates = 2;
    private  int flintRates = 1;

    private boolean rules = true;

    public GameState getGameState()
    {
        return gameState;
    }

    public int getBorderNumber()
    {
        return borderNumber;
    }

    public TeamType getTeamType()
    {
        return teamType;
    }

    public List<UUID> getPlayers()
    {
        return players;
    }

    public boolean isPVP()
    {
        return PVP;
    }

    public int getTimer()
    {
        return timer;
    }

    public void setWhitelistEnabled(boolean whitelistEnabled)
    {
        this.whitelistEnabled = whitelistEnabled;
    }

    public void setTimer(int timer)
    {
        this.timer = timer;
    }

    public int getBorderStartingNumber()
    {
        return borderStartingNumber;
    }

    public void setBorderStartingNumber(int borderStartingNumber)
    {
        this.borderStartingNumber = borderStartingNumber;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public List<UUID> getArenaPlayers()
    {
        return arenaPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }


    private int scatterTimeLeft = reference.RULES.size()*5;

    public int getScatterTimeLeft()
    {
        return scatterTimeLeft;
    }

    private int scatterTimes = 0;

    public int getScatterTimes()
    {
        return scatterTimes;
    }

    public void startGame()
    {
        final World uhc = Bukkit.getWorld("uhc");
        uhc.setTime(0);
        uhc.setGameRuleValue("doMobSpawning", "false");
        List<UUID> arena = new ArrayList<>();
        arena.addAll(arenaPlayers);
        for(UUID uuid : arena)
        {
            Player p = Bukkit.getPlayer(uuid);
            if(p != null)
            {
                p.teleport(reference.SPAWN);
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                uhcPlayer.setPlayerState(PlayerState.LOBBY);
                p.getInventory().clear();
                arenaPlayers.remove(p.getUniqueId());
                for(Player all : Bukkit.getOnlinePlayers())
                {
                    all.hidePlayer(p);
                    p.hidePlayer(all);
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers())
        {
            UUID uuid = p.getUniqueId();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if (uhcPlayer.getPlayerState() == PlayerState.LOBBY)
            {
                players.add(uuid);
                uhcPlayer.setPlayerState(PlayerState.SCATTERING);
            }
            whitelist.add(uuid);
        }
        if(teamType == TeamType.SOLO)
        {
            for(Player p : Bukkit.getOnlinePlayers())
            {
                UUID uuid = p.getUniqueId();
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                uhcPlayer.setScatter(getLocation());
            }
        }
        else
        {
            List<Integer> teams = new ArrayList<>();
            teams.addAll(teamManager.getTeams().keySet());
            for(int team : teams)
            {
                if (teamManager.getTeams().get(team).getPlayers().size() == 0)
                {
                    teamManager.getTeams().remove(team);
                }
            }
            for(Player p : Bukkit.getOnlinePlayers())
            {
                UUID uuid = p.getUniqueId();
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                if(uhcPlayer.getPlayerState() == PlayerState.SCATTERING)
                {
                    if (uhcPlayer.getTeamNumber() == -1)
                    {
                        teamManager.createTeam(uuid, uhcPlayer);
                    }
                }
            }
            for(Team team : teamManager.getTeams().values())
            {
                List<UUID> players = new ArrayList<>();
                players.addAll(team.getPlayers());
                for (UUID uuid : players)
                {
                    Player p = Bukkit.getPlayer(uuid);
                    if (p == null)
                    {
                        team.getPlayers().remove(uuid);
                    }
                }
            }
            teams.clear();
            teams.addAll(teamManager.getTeams().keySet());
            for(int team : teams)
            {
                if (teamManager.getTeams().get(team).getPlayers().size() == 0)
                {
                    teamManager.getTeams().remove(team);
                }
            }
            for(Team team : teamManager.getTeams().values())
            {
                Location location = getLocation();
                for(UUID uuid : team.getPlayers())
                {
                    Player p = Bukkit.getPlayer(uuid);
                    if(p != null)
                    {
                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                        uhcPlayer.setScatter(location);
                        team.getAlivePlayers().add(uuid);
                    }
                }
            }
            teams.clear();
            teams.addAll(teamManager.getTeams().keySet());
            for(int team : teamManager.getTeams().keySet())
            {
                if (teamManager.getTeams().get(team).getPlayers().size() == 0)
                {
                    teamManager.getTeams().remove(team);
                }
            }
        }
        whitelistEnabled = true;
        Bukkit.broadcastMessage(reference.GAME + "Server has been§7 whitelisted§f, now starting the§7 UHC");
        Bukkit.broadcastMessage(reference.GAME + "Starting the scatter, should take around §73 minutes");
        gameState = GameState.SCATTERING;
        Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(gameState));
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (scatterTimes != players.size())
                {
                    final Player p = Bukkit.getPlayer(players.get(scatterTimes));
                    if (p != null)
                    {
                        UUID uuid = p.getUniqueId();
                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                        uhcPlayer.setPlayerState(PlayerState.SCATTERED);
                        p.setGameMode(GameMode.SURVIVAL);
                        p.getInventory().clear();
                        Location location = uhcPlayer.getScatter();
                        assert location != null;
                        p.teleport(location);
                        p.setMaxHealth(20);
                        for (Player all : Bukkit.getOnlinePlayers())
                        {
                            all.showPlayer(p);
                        }
                        if(reference.SCATTER_TYPE == ScatterType.PLAYER)
                        {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 10000000, false));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 10000000, false));
                        }
                        else
                        {
                            new BukkitRunnable()
                            {

                                @Override
                                public void run()
                                {
                                    net.minecraft.server.v1_7_R4.World world = ((CraftWorld) p.getWorld()).getHandle();
                                    final PlaceHolder placeHolder = new PlaceHolder(world);
                                    MobUtil.spawnEntity(placeHolder, p.getLocation());
                                    new BukkitRunnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            placeHolder.setInvisible(true);
                                        }
                                    }.runTaskLater(core, 4L);
                                    Horse horse = (Horse) placeHolder.getBukkitEntity();
                                    horse.setTamed(true);
                                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
                                    horse.setPassenger(p);
                                }
                            }.runTaskLater(core, 30L);
                        }
                    }
                    scatterTimes++;
                }
                else
                {
                    this.cancel();
                }
            }
        }.runTaskTimer(core, 0L, 10L);
        scatterTimeLeft=reference.RULES.size()*5;
        new BukkitRunnable()
        {
            int y = 0;
            @Override
            public void run()
            {
                if(rules && scatterTimeLeft != 0)
                {
                    if (scatterTimeLeft % 5 == 0)
                    {
                        Bukkit.broadcastMessage(reference.RULES_PREFIX + reference.RULES.get(y));
                        y++;
                    }
                    scatterTimeLeft = scatterTimeLeft-1;
                }
                else
                {
                    this.cancel();
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            timer++;
                            if (timer == 1)
                            {
                                for (Player p : Bukkit.getOnlinePlayers())
                                {
                                    UUID uuid = p.getUniqueId();
                                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                    if (uhcPlayer.getPlayerState() == PlayerState.SCATTERED)
                                    {
                                        p.setGameMode(GameMode.SURVIVAL);
                                        p.setFlying(false);
                                        p.getInventory().clear();
                                        p.getInventory().setArmorContents(null);
                                        p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
                                        p.getInventory().addItem(new ItemStack(Material.FEATHER, 16));
                                        p.getInventory().addItem(new ItemStack(Material.LEATHER, 16));
                                        p.getInventory().addItem(new ItemStack(Material.STRING, 5));
                                        p.setHealth(20);
                                        p.setSaturation(20);
                                        p.setFoodLevel(20);
                                        p.setLevel(0);
                                        for (PotionEffect potionEffect : p.getActivePotionEffects())
                                        {
                                            p.removePotionEffect(potionEffect.getType());
                                        }
                                        if(p.isInsideVehicle())
                                        {
                                            p.getVehicle().remove();
                                        }
                                        uhcPlayer.setPlayerState(PlayerState.INGAME);
                                    }
                                    p.setHealth(p.getHealth());
                                }
                                uhc.setDifficulty(Difficulty.NORMAL);
                                Bukkit.broadcastMessage(reference.GAME + "The §7UHC§f has now§7 begun§f, good luck and have fun");
                                gameState = GameState.STARTED;
                                Bukkit.getPluginManager().callEvent(new GameStateChangeEvent(gameState));
                                playSound();
                                if (Scenario.INFINITEENCHANTER.isEnabled())
                                {
                                    for (Player p : Bukkit.getOnlinePlayers())
                                    {
                                        UUID uuid = p.getUniqueId();
                                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                        if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                                        {
                                            p.setExp(0);
                                            p.setLevel(30000);
                                            p.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                                            p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                            p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                            p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                                        }
                                    }
                                    Bukkit.broadcastMessage(reference.SCENARIO + "§3Players§f have received their§3 Infinite Enchanter Items");
                                }
                                if (Scenario.GONEFISHING.isEnabled())
                                {
                                    for (Player p : Bukkit.getOnlinePlayers())
                                    {
                                        UUID uuid = p.getUniqueId();
                                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                        if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                                        {
                                            p.setExp(0);
                                            p.setLevel(30000);
                                            p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                                            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
                                            fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
                                            fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
                                            fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
                                            p.getInventory().addItem(fishingRod);
                                        }
                                    }
                                }
                                if (Scenario.BESTPVE.isEnabled())
                                {
                                    for (Player p : Bukkit.getOnlinePlayers())
                                    {
                                        UUID uuid = p.getUniqueId();
                                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                        if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                                        {
                                            scenarioManager.getBestPVEList().add(p.getUniqueId());
                                        }
                                    }
                                    Bukkit.broadcastMessage(reference.SCENARIO + "§3Players§f have added to the§3 BestPVE List");
                                    new BukkitRunnable()
                                    {

                                        @Override
                                        public void run()
                                        {
                                            for (Player p : Bukkit.getOnlinePlayers())
                                            {
                                                UUID uuid = p.getUniqueId();
                                                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                                if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                                                {
                                                    if (scenarioManager.getBestPVEList().contains(p.getUniqueId()))
                                                    {
                                                        p.setMaxHealth(p.getMaxHealth() + 2.0);
                                                        p.setHealth(p.getHealth() + 2);
                                                    }
                                                }
                                            }
                                            Bukkit.broadcastMessage("§fBestPVE §8:|: §ePlayers§e on the§c BestPVE list§e have gained§e 1 heart");
                                        }
                                    }.runTaskTimer(core, 20 * 60 * 10, 20 * 60 * 10);
                                }
                                //createInitialBorder(borderRadius);
                            }
                            if (timer == getHealTimeInSeconds())
                            {
                                for (Player p : Bukkit.getOnlinePlayers())
                                {
                                    UUID uuid = p.getUniqueId();
                                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                                    if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                                    {
                                        p.setHealth(20);
                                        p.setSaturation(20);
                                    }
                                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
                                }
                                Bukkit.broadcastMessage(reference.GAME + "§7Players§f have received their§7 final heal");
                            }
                            if (timer == getPvpInSeconds())
                            {
                                PVP = true;
                                Bukkit.broadcastMessage(reference.GAME + "§7PVP§f has been enabled");
                                playSound();
                            }
                            if (timer == getBorderTimeInSeconds(borderNumber) - (5*60))
                            {
                                Bukkit.broadcastMessage(reference.BORDER + "The border will begin shrinking in 5 minutes");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 60)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 1 minute");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 30)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 30 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 10)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 10 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 5)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 5 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 4)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 4 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 3)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 3 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 2)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 2 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber) - 1)
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                Bukkit.broadcastMessage(reference.BORDER + "The border will start shrinking to §6" + borderSize.getSize() + "§f in§6 1 second(s)");
                                playSound();
                            }
                            else if (timer == getBorderTimeInSeconds(borderNumber))
                            {
                                BorderSize borderSize = BorderSize.getBorderByNumber(borderNumber, borderStartingNumber);
                                if(borderSize.getSize() == 25)
                                {
                                    if(players.size() > 8)
                                    {
                                        Bukkit.broadcastMessage(reference.BORDER + "Need 8 or less players to shrink to§6 25,§f delaying by§6 2 minutes");
                                        borderTime+=2;
                                    }
                                    else
                                    {
                                        borderNumber++;
                                        setBorderRadius(borderSize.getSize());
                                        updateBorder(borderRadius);
                                    }
                                }
                                else
                                {
                                    borderNumber++;
                                    setBorderRadius(borderSize.getSize());
                                    updateBorder(borderRadius);
                                }
                            }
                        }
                    }.runTaskTimer(core, 0, 20L);
                }
            }
        }.runTaskTimer(core, 0L, 20L);
    }

    public List<UUID> getWhitelist()
    {
        return whitelist;
    }

    public String getFormatTime()
    {
        int hours = timer / 3600;
        int secondsLeft = timer - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if(hours >= 1)
        {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds ;

        return formattedTime;
    }

    private final Random random = new Random();

    private int times = 0;

    public Location getLocation()
    {
        World uhc = Bukkit.getWorld("uhc");
        int z = random.nextInt(getBorderRadius() + 1 - (getBorderRadius() * -1)) + (getBorderRadius() * -1);
        int x = random.nextInt(getBorderRadius() + 1 - (getBorderRadius() * -1)) + (getBorderRadius() * -1);
        Location location = new Location(uhc, x, uhc.getHighestBlockYAt(x, z), z);
        if (location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.WATER &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_WATER &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.LAVA &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.STATIONARY_LAVA &&
                location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.CACTUS)
        {
            times = 0;
            return location.add(0, 5, 0);
        }
        if(times == 10)
        {
            location.getBlock().setType(Material.GRASS);
            Bukkit.getLogger().log(Level.INFO, "Failed to get a location after 10 attempts, this location will look fake");
            times = 0;
            return location.add(0, 5, 0);
        }
        times++;
        return getLocation();
    }



    public void lateScatter(Player t)
    {
        t.setGameMode(GameMode.SURVIVAL);
        t.setFlying(false);
        t.getInventory().clear();
        t.getInventory().setArmorContents(null);
        t.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
        t.getInventory().addItem(new ItemStack(Material.FEATHER, 16));
        t.getInventory().addItem(new ItemStack(Material.LEATHER, 16));
        t.getInventory().addItem(new ItemStack(Material.STRING, 5));
        t.setHealth(20);
        t.setSaturation(20);
        t.setFoodLevel(20);
        for (PotionEffect potionEffect : t.getActivePotionEffects())
        {
            t.removePotionEffect(potionEffect.getType());
        }
        t.teleport(getLocation());
        t.setMaxHealth(20);
        players.add(t.getUniqueId());
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(t.getUniqueId());
        uhcPlayer.setPlayerState(PlayerState.INGAME);
        playerManager.getSpectators().remove(t.getUniqueId());
        playerManager.getModerators().remove(t.getUniqueId());
        for(Player all : Bukkit.getOnlinePlayers())
        {
            all.showPlayer(t);
        }
        if(Scenario.INFINITEENCHANTER.isEnabled())
        {
            t.setLevel(30000);
            t.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
            t.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
            t.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
        }
        if (Scenario.GONEFISHING.isEnabled())
        {
            t.setExp(0);
            t.setLevel(30000);
            t.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
            ItemStack fishingRod = new ItemStack(Material.FISHING_ROD);
            fishingRod.addUnsafeEnchantment(Enchantment.LUCK, 250);
            fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);
            fishingRod.addUnsafeEnchantment(Enchantment.DURABILITY, 100);
            t.getInventory().addItem(fishingRod);
        }
        if(Scenario.BESTPVE.isEnabled())
        {
            scenarioManager.getBestPVEList().add(t.getUniqueId());
        }
        if(teamType == TeamType.TEAMS)
        {
            teamManager.createTeam(t.getUniqueId(), uhcPlayer);
        }
        for(Player all : Bukkit.getOnlinePlayers())
        {
            all.showPlayer(t);
        }
    }

    public int getAppleRates()
    {
        return appleRates;
    }

    public void setAppleRates(int appleRates)
    {
        this.appleRates = appleRates;
    }

    public int getFlintRates()
    {
        return flintRates;
    }

    public void setFlintRates(int flintRates)
    {
        this.flintRates = flintRates;
    }

    private Map<Integer, Location> teamRandomScatterLoc = new HashMap<>();

    private void updateBorder(int radius)
    {
        World world = Bukkit.getWorld("uhc");

        if(radius == 500 || radius == 250 || radius == 100 || radius == 50)
        {
            if(teamType == TeamType.SOLO)
            {
                for (Player p : Bukkit.getWorld("uhc").getPlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                    {
                        Location location = p.getLocation();
                        if (Math.abs(location.getX()) > radius || Math.abs(location.getZ()) > radius)
                        {
                            Location location1 = getLocation();
                            p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                        }
                    }
                }
            }
            else
            {
                for (Player p : Bukkit.getWorld("uhc").getPlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
                    {
                        Location location = p.getLocation();
                        if (Math.abs(location.getX()) > radius || Math.abs(location.getZ()) > radius)
                        {
                            if (teamRandomScatterLoc.containsKey(uhcPlayer.getTeamNumber()))
                            {
                                Location location1 = teamRandomScatterLoc.get(uhcPlayer.getTeamNumber());
                                p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                            }
                            else
                            {
                                Location location1 = getLocation();
                                p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                                teamRandomScatterLoc.put(uhcPlayer.getTeamNumber(), location1);
                            }
                        }
                    }
                }
            }
        }
        teamRandomScatterLoc.clear();
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world.getName() + " set " + radius + " " + radius + " 0 0");
        final int blocksPerTick = 6;
        final int highestBlock = 4;

        int posX = radius;
        int negX = 0 - radius;

        int posZ = radius;
        int negZ = 0 - radius;

        final Queue<Location> locations1 = new ArrayDeque<>();
        final Queue<Location> locations2 = new ArrayDeque<>();

        final Queue<Location> locations3 = new ArrayDeque<>();
        final Queue<Location> locations4 = new ArrayDeque<>();

        final Queue<Location> locations5 = new ArrayDeque<>();
        final Queue<Location> locations6 = new ArrayDeque<>();

        final Queue<Location> locations7 = new ArrayDeque<>();
        final Queue<Location> locations8 = new ArrayDeque<>();



        for(int t = posX; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations1.add(new Location(world, t, y, posZ));
                }
            }
        }
        for(int t = negX; t <= 0; t++)
        {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations2.add(new Location(world, t, y, posZ));
                }
            }
        }

        for(int t = posX; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations3.add(new Location(world, t, y, negZ));
                }
            }
        }
        for(int t = negX; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations4.add(new Location(world, t, y, negZ));
                }
            }
        }
        for(int t = posZ; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations5.add(new Location(world, posX, y, t));
                }
            }
        }
        for(int t = negZ; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations6.add(new Location(world, posX, y, t));
                }
            }
        }

        for(int t = posZ; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations7.add(new Location(world, negX, y, t));
                }
            }
        }
        for(int t = negZ; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations8.add(new Location(world, negX, y, t));
                }
            }
        }
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                for(int x = 0; x<blocksPerTick; x++)
                {
                    if (!locations1.isEmpty())
                    {
                        locations1.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations2.isEmpty())
                    {
                        locations2.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations3.isEmpty())
                    {
                        locations3.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations4.isEmpty())
                    {
                        locations4.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations5.isEmpty())
                    {
                        locations5.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations6.isEmpty())
                    {
                        locations6.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations7.isEmpty())
                    {
                        locations7.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations8.isEmpty())
                    {
                        locations8.poll().getBlock().setType(Material.BEDROCK);
                    }
                    else
                    {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(core, 0L, 1L);

        if(radius == 50)
        {
            Scenario.NOCLEAN.enable();
        }
        if(radius == 500)
        {
            if (configManager.isNether())
            {
                if (Bukkit.getWorld("uhc_nether") != null)
                {
                    if (Bukkit.getWorld("uhc_nether").getPlayers().size() != 0)
                    {
                        for (Player p : Bukkit.getWorld("uhc_nether").getPlayers())
                        {
                            Location location1 = getLocation();
                            p.teleport(world.getHighestBlockAt(location1.getBlockX(), location1.getBlockZ()).getLocation());
                        }
                    }
                }
            }
        }
    }

    private void createInitialBorder(int radius)
    {
        World world = Bukkit.getWorld("uhc");

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + world.getName() + " set " + radius + " " + radius + " 0 0");

        final int blocksPerTick = 4;
        final int highestBlock = 4;

        int posX = radius;
        int negX = 0 - radius;

        int posZ = radius;
        int negZ = 0 - radius;

        final Queue<Location> locations1 = new ArrayDeque<>();
        final Queue<Location> locations2 = new ArrayDeque<>();

        final Queue<Location> locations3 = new ArrayDeque<>();
        final Queue<Location> locations4 = new ArrayDeque<>();

        final Queue<Location> locations5 = new ArrayDeque<>();
        final Queue<Location> locations6 = new ArrayDeque<>();

        final Queue<Location> locations7 = new ArrayDeque<>();
        final Queue<Location> locations8 = new ArrayDeque<>();



        for(int t = posX; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations1.add(new Location(world, t, y, posZ));
                }
            }
        }
        for(int t = negX; t <= 0; t++)
        {
            int min = world.getHighestBlockYAt(t, posZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations2.add(new Location(world, t, y, posZ));
                }
            }
        }

        for(int t = posX; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations3.add(new Location(world, t, y, negZ));
                }
            }
        }
        for(int t = negX; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(t, negZ);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations4.add(new Location(world, t, y, negZ));
                }
            }
        }
        for(int t = posZ; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations5.add(new Location(world, posX, y, t));
                }
            }
        }
        for(int t = negZ; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(posX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations6.add(new Location(world, posX, y, t));
                }
            }
        }

        for(int t = posZ; t >= 0; t--)
        {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations7.add(new Location(world, negX, y, t));
                }
            }
        }
        for(int t = negZ; t <= -0; t++)
        {
            int min = world.getHighestBlockYAt(negX, t);
            int max = min + highestBlock;
            if(max < 256)
            {
                for (int y = min; y < max; y++)
                {
                    locations8.add(new Location(world, negX, y, t));
                }
            }
        }
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                for(int x = 0; x<blocksPerTick; x++)
                {
                    if (!locations1.isEmpty())
                    {
                        locations1.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations2.isEmpty())
                    {
                        locations2.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations3.isEmpty())
                    {
                        locations3.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations4.isEmpty())
                    {
                        locations4.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations5.isEmpty())
                    {
                        locations5.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations6.isEmpty())
                    {
                        locations6.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations7.isEmpty())
                    {
                        locations7.poll().getBlock().setType(Material.BEDROCK);
                    }
                    if (!locations8.isEmpty())
                    {
                        locations8.poll().getBlock().setType(Material.BEDROCK);
                    }
                    else
                    {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(core, 0L, 1L);
    }

    private void playSound()
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1, 1);
        }
    }

    public List<Player> getPlayersWithin(Player player, int radius)
    {
        List<Player> res = new ArrayList<>();
        int d2 = radius * radius;
        for (Player p : Bukkit.getServer().getOnlinePlayers())
        {
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                if (p.getWorld() == player.getWorld() && p.getLocation().distanceSquared(player.getLocation()) <= d2)
                {
                    res.add(p);
                }
            }
        }
        return res;
    }

    private int x = 0;

    public Player getRandomPlayer(Player p)
    {
        x++;
        if (x >= players.size())
        {
            x = 0;
        }
        List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
        Player t = list.get(x);
        if (t.isOnline() && (!(t.getName().equals(p.getName()))))
        {
            return t.getPlayer();
        }
        return null;
    }

    public boolean isRules()
    {
        return rules;
    }

    public void setRules(boolean rules)
    {
        this.rules = rules;
    }

    public boolean isWhitelistEnabled()
    {
        return whitelistEnabled;
    }

    public int getHealTime()
    {
        return healTime;
    }

    public int getHealTimeInSeconds()
    {
        return healTime*60;
    }

    public void setHealTime(int healTime)
    {
        this.healTime = healTime;
    }

    public int getPvpTime()
    {
        return pvpTime;
    }

    public int getPvpInSeconds()
    {
        return pvpTime*60;
    }

    public void setPvpTime(int pvpTime)
    {
        this.pvpTime = pvpTime;
    }

    public int getBorderTime()
    {
        return borderTime;
    }

    public int getBorderTimeInSeconds(int number)
    {
        return (borderTime+(number*5))*60;
    }

    public void setBorderTime(int borderTime)
    {
        this.borderTime = borderTime;
    }

    public int getBorderRadius()
    {
        return borderRadius;
    }

    public void setBorderRadius(int borderRadius)
    {
        this.borderRadius = borderRadius;
    }

    public void setTeamType(TeamType teamType)
    {
        this.teamType = teamType;
    }

    public boolean isDonatorWhitelistEnabled()
    {
        return donatorWhitelistEnabled;
    }

    public void setDonatorWhitelistEnabled(boolean donatorWhitelistEnabled)
    {
        this.donatorWhitelistEnabled = donatorWhitelistEnabled;
    }
}
