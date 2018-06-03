package me.bladian.uhc;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.bladian.uhc.cave.GCRandom;
import me.bladian.uhc.cave.GiantCave;
import me.bladian.uhc.combat.CombatSkeleton;
import me.bladian.uhc.combat.MobUtil;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.event.*;
import me.bladian.uhc.manager.*;
import me.bladian.uhc.player.UHCPlayer;
import me.bladian.uhc.scenarios.Scenario;
import me.bladian.uhc.team.Team;
import me.bladian.uhc.util.*;
import me.uhc.worldborder.Events.WorldBorderFillFinishedEvent;
import net.minecraft.util.org.apache.commons.lang3.text.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.logging.Level;

/**
 * Created by BladianYT
 */
public class Event implements Listener
{

    private final Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private ConfigManager configManager = core.getConfigManager();
    private Inventories inventories = core.getInventories();
    private WorldManager worldManager = core.getWorldManager();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();
    private ScenarioManager scenarioManager = core.getScenarioManager();
    private ScoreboardManager scoreboardManager = core.getScoreboardManager();
    private TeamManager teamManager = core.getTeamManager();

    private String ARROW = "§8»";
    private String XRAY_PREFIX = "§cX-Ray " + ARROW + " ";
    private String DEATH_PREFIX = "§bDeath " + ARROW + " ";


    private boolean warned = false;


    public Event()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (gameManager.getGameState() == GameState.LOBBY)
                {
                    if (gameManager.getTeamType() == TeamType.SOLO)
                    {
                        for (Player all : Bukkit.getOnlinePlayers())
                        {
                            for (Player all1 : Bukkit.getOnlinePlayers())
                            {
                                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                                UHCPlayer uhcPlayer1 = playerManager.getUhcPlayerMap().get(all1.getUniqueId());
                                if(uhcPlayer.getPlayerState() != PlayerState.ARENA  && uhcPlayer1.getPlayerState() != PlayerState.ARENA)
                                {
                                    all.hidePlayer(all1);
                                    all1.hidePlayer(all);
                                }
                            }
                        }
                    }
                    else
                    {
                        for (Player all : Bukkit.getOnlinePlayers())
                        {
                            for (Player all1 : Bukkit.getOnlinePlayers())
                            {
                                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                                UHCPlayer uhcPlayer1 = playerManager.getUhcPlayerMap().get(all1.getUniqueId());
                                if(uhcPlayer.getPlayerState() != PlayerState.ARENA  && uhcPlayer1.getPlayerState() != PlayerState.ARENA)
                                {
                                    all.hidePlayer(all1);
                                    all1.hidePlayer(all);
                                }
                            }
                        }
                        /*for (Player all : Bukkit.getOnlinePlayers())
                        {
                            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                            if (uhcPlayer.getTeamNumber() == -1)
                            {
                                for (Player all1 : Bukkit.getOnlinePlayers())
                                {
                                    UHCPlayer uhcPlayer1 = playerManager.getUhcPlayerMap().get(all1.getUniqueId());
                                    if(uhcPlayer.getPlayerState() != PlayerState.ARENA  && uhcPlayer1.getPlayerState() != PlayerState.ARENA)
                                    {
                                        all.hidePlayer(all1);
                                        all1.hidePlayer(all);
                                    }

                                }
                            }
                            else
                            {
                                for (Player all1 : Bukkit.getOnlinePlayers())
                                {
                                    UHCPlayer uhcPlayer1 = playerManager.getUhcPlayerMap().get(all1.getUniqueId());
                                    if(uhcPlayer.getPlayerState() != PlayerState.ARENA  && uhcPlayer1.getPlayerState() != PlayerState.ARENA)
                                    {
                                        if(uhcPlayer.getTeamNumber() != -1)
                                        {
                                            if (!teamManager.getTeams().get(uhcPlayer.getTeamNumber()).getPlayers().contains(all.getUniqueId()))
                                            {
                                                all.hidePlayer(all1);
                                                all1.hidePlayer(all);
                                            }
                                            else
                                            {
                                                all.showPlayer(all1);
                                                all1.showPlayer(all);

                                            }
                                        }
                                    }
                                *
                            }*/
                    }
                }
                if (gameManager.getGameState() == GameState.STARTED)
                {
                    List<UUID> hide = new ArrayList<>();
                    hide.addAll(playerManager.getModerators());
                    hide.addAll(playerManager.getSpectators());
                    for (UUID players : hide)
                    {
                        Player p = Bukkit.getPlayer(players);
                        if (p != null)
                        {
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.hidePlayer(p);
                            }
                        }
                    }
                    for (UUID specs : playerManager.getSpectators())
                    {
                        Player spec = Bukkit.getPlayer(specs);
                        if (spec != null)
                        {
                            if (Math.abs(spec.getLocation().getBlockX()) >= configManager.getSpectatorRadius() || Math.abs(spec.getLocation().getBlockZ()) >= configManager.getSpectatorRadius())
                            {
                                spec.teleport(new Location(Bukkit.getWorld("uhc"), 0, Bukkit.getWorld("uhc").getHighestBlockAt(0, 0).getY() + 20, 0));
                                spec.sendMessage(reference.ERROR + "§cYou can't pass the " + configManager.getSpectatorRadius() + " border");
                                return;
                            }
                            if (spec.getLocation().getBlockY() < 45)
                            {
                                spec.teleport(new Location(Bukkit.getWorld("uhc"), 0, Bukkit.getWorld("uhc").getHighestBlockAt(0, 0).getY() + 20, 0));
                                spec.sendMessage(reference.ERROR + "§cYou can't go under Y:45");
                                return;
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(core, 0L, 100L);
    }


    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e)
    {
        if (gameManager.getGameState() == GameState.SCATTERING)
        {
            e.setKickMessage("§cThe scatter is currently happening, give it time to finish then join");
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }
        if (e.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED)
        {
            if (!playerManager.getUhcPlayerMap().containsKey(e.getUniqueId()))
            {

                UHCPlayer UHCPlayer = new UHCPlayer(e.getUniqueId());
                playerManager.getUhcPlayerMap().put(e.getUniqueId(), UHCPlayer);
                if(reference.STATS && MySQL.isConnectionSuccessful())
                {
                    UHCPlayer.getInformation();
                }
            }
        }
    }

    /*@EventHandler
    public void onWorldInit(WorldInitEvent e)
    {
        e.getWorld().getPopulators().add(new OrePopulator());
        Bukkit.broadcastMessage("Started a new ore generator");
    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent e)
    {
        Player p = e.getPlayer();
        if(!Bukkit.hasWhitelist())
        {
            if (gameManager.getGameState() == GameState.LOBBY)
            {
                if(gameManager.isDonatorWhitelistEnabled())
                {
                    if(!p.hasPermission("rank.staff"))
                    {
                        e.setKickMessage("§cYou can't join the server while it's being setup");
                        e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    }
                    if (gameManager.getWhitelist().contains(p.getUniqueId()))
                    {
                        e.setResult(PlayerLoginEvent.Result.ALLOWED);
                    }
                }
                if (gameManager.isWhitelistEnabled() && !gameManager.isDonatorWhitelistEnabled())
                {
                    if (!p.hasPermission("rank.whitelist"))
                    {
                        e.setKickMessage("§6§m---------------------------------------\n§f§lWant to§6§l JOIN BEFORE WHITELIST IS OFF?\n§f§lBuy a §6§lPremium Rank§f§l NOW!\n§f§lSTORE: §6§lhttp://store.fazon.gg\n§6§m---------------------------------------");
                        e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    }
                    else
                    {
                        e.setResult(PlayerLoginEvent.Result.ALLOWED);
                    }
                    if (gameManager.getWhitelist().contains(p.getUniqueId()))
                    {
                        e.setResult(PlayerLoginEvent.Result.ALLOWED);
                    }
                }
                else
                {
                    if (Bukkit.getOnlinePlayers().size() >= gameManager.getMaxPlayers())
                    {
                        if (p.hasPermission("rank.whitelist"))
                        {
                            e.setResult(PlayerLoginEvent.Result.ALLOWED);
                        }
                        else
                        {
                            e.setKickMessage("§6§m---------------------------------------\n§f§lWant to have a§6§l RESERVED SLOT?\n§f§lBuy a §6§lPremium Rank§f§l NOW!\n§f§lSTORE: §6§lhttp://store.fazon.gg\n§6§m---------------------------------------");
                            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                        }
                    }
                }
            }
            if (gameManager.getGameState() == GameState.STARTED)
            {
                if (gameManager.isWhitelistEnabled())
                {
                    if (gameManager.getWhitelist().contains(p.getUniqueId()))
                    {
                        e.setResult(PlayerLoginEvent.Result.ALLOWED);
                    }
                    else
                    {
                        if (!p.hasPermission("rank.whitelist"))
                        {
                            e.setKickMessage("§6§m---------------------------------------\n§cThe server is full\n§f§lWant to§6§l JOIN MID-GAME?\n§f§lBuy a §6§lPremium Rank§f§l NOW!\n§f§lSTORE: §6§lhttp://store.fazon.gg\n§6§m---------------------------------------");
                            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                        }
                    }
                }
                //Check if they have a staff rank or a donator rank
            }
        }
    }

    public void onTNT(EntityExplodeEvent e) {
        e.setCancelled(true);
        for (Block block : e.blockList())
        {
            if (block.getType() == Material.BEDROCK)
            {
                e.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getInventory().getName().contains("STATS"))
        {
            e.setCancelled(true);
            return;
        }
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
        if (uhcPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
        }
        if(uhcPlayer.getPlayerState() == PlayerState.ARENA)
        {
            e.setCancelled(false);
        }
        Inventory inventory = e.getInventory();
        if (inventory.getName().equalsIgnoreCase("§e§lREADY TO START"))
        {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
            {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack.hasItemMeta())
                {
                    if (itemStack.getItemMeta().getDisplayName() != null)
                    {
                        if (itemStack.getType() == Material.GOLD_INGOT)
                        {
                            if (gameManager.getGameState() == GameState.STARTED)
                            {
                                p.sendMessage("§cGame is already running");
                            }
                            else
                            {
                                gameManager.startGame();
                            }
                            e.setCancelled(true);
                            p.closeInventory();
                        }
                    }
                }
            }
        }
        if(inventory.getName().equalsIgnoreCase("§e§lALIVE PLAYERS") || inventory.getName().equalsIgnoreCase("§e§lORES MINED"))
        {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
            {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack.hasItemMeta())
                {
                    if (itemStack.getItemMeta().getDisplayName() != null)
                    {
                        e.setCancelled(true);
                        if (e.getCurrentItem().getType() == Material.SKULL_ITEM)
                        {
                            Player t = Bukkit.getPlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                            if(t == null)
                            {
                                p.sendMessage(reference.ERROR + "§cPlayer isn't online");
                            }
                            else
                            {
                                p.teleport(t);
                                p.sendMessage(reference.GAME + "§cTeleported§f you to§c " + t.getName());
                                Bukkit.getLogger().log(Level.INFO, "[Teleport] " + p.getName() + " has teleported to " + t.getName());
                            }
                        }
                    }
                }
            }
        }
        if (inventory.getName().equalsIgnoreCase("§e§lWORLD EDITOR"))
        {
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
            {
                ItemStack itemStack = e.getCurrentItem();
                if (itemStack.hasItemMeta())
                {
                    if (itemStack.getItemMeta().getDisplayName() != null)
                    {
                        e.setCancelled(true);
                        if (e.getCurrentItem().getType() == Material.GRASS)
                        {
                            String name = e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase();
                            if (gameManager.getGameState() != GameState.LOBBY)
                            {
                                p.sendMessage(reference.ERROR + "You can only do this in lobby mode");
                            }
                            else
                            {
                                if (name.contains("create"))
                                {
                                    if (Bukkit.getWorld("uhc") != null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC World already exists, you have to delete it first");
                                    }
                                    else
                                    {
                                        worldManager.createWorld("uhc");
                                        p.closeInventory();
                                    }
                                }
                                else if (name.contains("delete"))
                                {
                                    if (Bukkit.getWorld("uhc") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC World doesn't exist");
                                    }
                                    else
                                    {
                                        p.closeInventory();
                                        World world = Bukkit.getWorld("uhc");
                                        worldManager.unloadWorld(world);
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {

                                                worldManager.deleteWorld("uhc");
                                                new BukkitRunnable()
                                                {

                                                    @Override
                                                    public void run()
                                                    {
                                                        Bukkit.broadcastMessage(reference.GAME + "The§c UHC World§f has finished deleting");
                                                    }
                                                }.runTaskLater(core, 600L);
                                            }
                                        }.runTaskLater(core, 600L);
                                    }
                                }
                                else if(name.contains("load"))
                                {
                                    e.setCancelled(true);
                                    if(Bukkit.getWorld("uhc") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC World doesn't exist");
                                    }
                                    else
                                    {
                                        p.closeInventory();
                                        worldManager.loadWorld(Bukkit.getWorld("uhc"), gameManager.getBorderRadius(), 125);
                                    }
                                }
                                else if(name.contains("reset"))
                                {
                                    e.setCancelled(true);
                                    if (Bukkit.getWorld("uhc") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC World doesn't exist");
                                    }
                                    else
                                    {
                                        p.closeInventory();
                                        World world = Bukkit.getWorld("uhc");
                                        worldManager.unloadWorld(world);
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {

                                                worldManager.deleteWorld("uhc");
                                                new BukkitRunnable()
                                                {

                                                    @Override
                                                    public void run()
                                                    {
                                                        Bukkit.broadcastMessage(reference.GAME + "The§c UHC World§f has finished deleting");
                                                        worldManager.createWorld("uhc");
                                                    }
                                                }.runTaskLater(core, 600L);
                                            }
                                        }.runTaskLater(core, 600L);
                                    }
                                }
                            }
                        }
                        else if (e.getCurrentItem().getType() == Material.NETHERRACK)
                        {
                            String name = e.getCurrentItem().getItemMeta().getDisplayName().toLowerCase();
                            if (gameManager.getGameState() != GameState.LOBBY)
                            {
                                p.sendMessage(reference.ERROR + "You can only do this in lobby mode");
                            }
                            else
                            {
                                if (name.contains("create"))
                                {
                                    if (Bukkit.getWorld("uhc") != null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC Nether already exists, you have to delete it first");
                                    }
                                    else
                                    {
                                        worldManager.createNether("uhc_nether");
                                        p.closeInventory();
                                    }
                                }
                                else if (name.contains("delete"))
                                {
                                    if (Bukkit.getWorld("uhc_nether") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC Nether doesn't exist");
                                    }
                                    else
                                    {
                                        p.closeInventory();
                                        World world = Bukkit.getWorld("uhc_nether");
                                        worldManager.unloadWorld(world);
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                worldManager.deleteWorld("nether");
                                                new BukkitRunnable()
                                                {

                                                    @Override
                                                    public void run()
                                                    {
                                                        Bukkit.broadcastMessage(reference.GAME + "The§c UHC Nether§f has finished deleting");
                                                    }
                                                }.runTaskLater(core, 600L);
                                            }
                                        }.runTaskLater(core, 600L);
                                    }
                                }
                                else if(name.contains("load"))
                                {
                                    if(Bukkit.getWorld("uhc_nether") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC Nether doesn't exist");
                                    }
                                    else
                                    {
                                        worldManager.loadWorld(Bukkit.getWorld("uhc_nether"), 500, 150);
                                        p.closeInventory();
                                    }
                                }
                                else if(name.contains("reset"))
                                {
                                    e.setCancelled(true);
                                    if (Bukkit.getWorld("uhc_nether") == null)
                                    {
                                        p.sendMessage(reference.ERROR + "The UHC Nether doesn't exist");
                                    }
                                    else
                                    {
                                        p.closeInventory();
                                        World world = Bukkit.getWorld("uhc_nether");
                                        worldManager.unloadWorld(world);
                                        new BukkitRunnable()
                                        {
                                            @Override
                                            public void run()
                                            {

                                                worldManager.deleteWorld("uhc_nether");
                                                new BukkitRunnable()
                                                {

                                                    @Override
                                                    public void run()
                                                    {
                                                        Bukkit.broadcastMessage(reference.GAME + "The§c UHC Nether§f has finished deleting");
                                                        worldManager.createNether("uhc_nether");
                                                    }
                                                }.runTaskLater(core, 600L);
                                            }
                                        }.runTaskLater(core, 600L);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (uhcPlayer.getPlayerState() == PlayerState.HOST || uhcPlayer.getPlayerState() == PlayerState.MODERATOR)
        {
            if (e.getInventory().getName().equals("§e§lWORLD SELECTOR"))
            {
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR)
                {
                    if (e.getCurrentItem().hasItemMeta())
                    {
                        if (e.getCurrentItem().getItemMeta().getDisplayName() != null)
                        {
                            if (e.getCurrentItem().getType() == Material.GRASS)
                            {
                                World uhc = Bukkit.getWorld("uhc");
                                if (uhc == null)
                                {
                                    p.sendMessage(reference.ERROR + "§cThe UHC world doesn't exist");
                                }
                                else
                                {
                                    p.teleport(new Location(uhc, 0, uhc.getHighestBlockYAt(0, 0) + 15, 0));
                                }
                            }
                            else if (e.getCurrentItem().getType() == Material.SMOOTH_BRICK)
                            {
                                p.teleport(reference.SPAWN);
                            }
                            else if (e.getCurrentItem().getType() == Material.NETHERRACK)
                            {
                                World uhc = Bukkit.getWorld("uhc_nether");
                                if (uhc == null)
                                {
                                    p.sendMessage(reference.ERROR + "§cThe UHC nether doesn't exist");
                                }
                                else
                                {
                                    p.teleport(new Location(uhc, 0, uhc.getHighestBlockYAt(0, 0) + 15, 0));
                                }
                            }
                            e.setCancelled(true);
                            p.closeInventory();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onExit(VehicleExitEvent e)
    {
        if(gameManager.getGameState() == GameState.SCATTERING)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWin(PlayerWinUHCEvent e)
    {
        Bukkit.broadcastMessage(reference.GAME + "§b" + e.getPlayer().getName() + " §ehas won the UHC! Thank you for playing a UHC hosted by§c FazonGG");
        Bukkit.broadcastMessage("§cNow attempting to save stats to the database...");
        UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(e.getPlayer().getUniqueId());
        UHCPlayer1.getWins().increaseAmount(1);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (UHCPlayer UHCPlayer : playerManager.getUhcPlayerMap().values())
                {
                    UHCPlayer.saveStats();
                }

            }
        }.runTaskAsynchronously(core);
        Bukkit.broadcastMessage("§aStats have successfully saved");
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        Player p = e.getEntity();
        //EntityDamageEvent entityDamageEvent = p.getLastDamageCause();
        UHCPlayer uhcPlayerArmor = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        uhcPlayerArmor.setItemStacks(p.getInventory().getContents());
        uhcPlayerArmor.setArmorItemStacks(p.getInventory().getArmorContents());
        uhcPlayerArmor.setDeathLocation(p.getLocation());
        uhcPlayerArmor.setLevels((int) p.getExp());
        if (p.getKiller() != null)
        {
            Player k = p.getKiller();
            UUID uuid = p.getUniqueId();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getUniqueId());
                PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(offlinePlayer, k);
                Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                EntityDamageEvent entityDamageEvent = p.getLastDamageCause();
                UHCPlayer uhcKiller = playerManager.getUhcPlayerMap().get(k.getUniqueId());
                if(entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                {
                    if(reference.DEATH_MESSAGES_ENABLED)
                    {
                        e.setDeathMessage(reference.PLAYER_DEATH_BOW.replace("%player%", p.getName()).replace("%killer%", k.getName())
                                .replace("%pkills%", String.valueOf(uhcPlayer.getKills()))
                                .replace("%kkills%", String.valueOf(uhcKiller.getKills())));
                    }
                }
                else
                {
                    if(reference.DEATH_MESSAGES_ENABLED)
                    {
                        e.setDeathMessage(reference.PLAYER_DEATH_SWORD.replace("%player%", p.getName()).replace("%killer%", k.getName())
                                .replace("%pkills%", String.valueOf(uhcPlayer.getKills()))
                                .replace("%kkills%", String.valueOf(uhcKiller.getKills())));
                    }
                }
            }
            if (uhcPlayer.getPlayerState() == PlayerState.ARENA)
            {
                if (gameManager.getArenaPlayers().contains(p.getUniqueId()))
                {
                    EntityDamageEvent entityDamageEvent = p.getLastDamageCause();
                    UHCPlayer uhcKiller = playerManager.getUhcPlayerMap().get(k.getUniqueId());
                    if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                    {
                        for (UUID uuid1 : gameManager.getArenaPlayers())
                        {
                            Player t = Bukkit.getPlayer(uuid1);
                            if (t != null)
                            {
                                if (reference.DEATH_MESSAGES_ENABLED)
                                {
                                    t.sendMessage(reference.PLAYER_DEATH_BOW.replace("%player%", p.getName()).replace("%killer%", k.getName())
                                            .replace("%pkills%", String.valueOf(uhcPlayer.getKills()))
                                            .replace("%kkills%", String.valueOf(uhcKiller.getKills())));
                                }
                                else
                                {
                                    t.sendMessage(e.getDeathMessage());
                                }
                            }
                        }
                    }
                    else
                    {
                        for (UUID uuid1 : gameManager.getArenaPlayers())
                        {
                            Player t = Bukkit.getPlayer(uuid1);
                            if (t != null)
                            {
                                if (reference.DEATH_MESSAGES_ENABLED)
                                {
                                    t.sendMessage(reference.PLAYER_DEATH_SWORD.replace("%player%", p.getName()).replace("%killer%", k.getName())
                                            .replace("%pkills%", String.valueOf(uhcPlayer.getKills()))
                                            .replace("%kkills%", String.valueOf(uhcKiller.getKills())));
                                }
                                else
                                {
                                    t.sendMessage(e.getDeathMessage());
                                }
                            }
                        }
                    }
                    e.setDeathMessage("");
                    k.getInventory().addItem(new ItemBuilder(Material.GOLDEN_APPLE, 1).toItemStack());
                    gameManager.getArenaPlayers().remove(p.getUniqueId());
                    e.getDrops().clear();
                    for (Player all : Bukkit.getOnlinePlayers())
                    {
                        all.hidePlayer(p);
                        p.hidePlayer(all);
                    }
                    uhcPlayer.setPlayerState(PlayerState.LOBBY);
                }
                e.getDrops().clear();
                p.getInventory().setArmorContents(null);
                p.getInventory().clear();
            }
        }
        else
        {
            UUID uuid = p.getUniqueId();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
            if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(p.getUniqueId());
                PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(offlinePlayer);
                Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                if(reference.DEATH_MESSAGES_ENABLED)
                {
                    e.setDeathMessage(reference.PLAYER_DEATH_UNKNOWN.replace("%player%", p.getName()).replace("%pkills%", String.valueOf(uhcPlayer.getKills())));
                }
            }
            if (uhcPlayer.getPlayerState() == PlayerState.ARENA)
            {
                e.getDrops().clear();
                for(UUID uuid1 : gameManager.getArenaPlayers())
                {
                    Player t = Bukkit.getPlayer(uuid1);
                    if(t != null)
                    {
                        if(reference.DEATH_MESSAGES_ENABLED)
                        {
                            t.sendMessage(reference.PLAYER_DEATH_UNKNOWN.replace("%player%", p.getName()));
                        }
                        else
                        {
                            t.sendMessage(e.getDeathMessage());
                        }
                    }
                }
                gameManager.getArenaPlayers().remove(p.getUniqueId());
                e.setDeathMessage("");
                for(Player all : Bukkit.getOnlinePlayers())
                {
                    all.hidePlayer(p);
                    p.hidePlayer(all);
                }
                uhcPlayer.setPlayerState(PlayerState.LOBBY);
                p.getInventory().setArmorContents(null);
                p.getInventory().clear();
            }
        }
    }

    private boolean won = false;

    @EventHandler
    private void onPlayerUHCDeath(PlayerUHCDeathEvent e)
    {
        OfflinePlayer p = e.getOfflinePlayer();
        final UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        UHCPlayer.getDeaths().increaseAmount(1);
        UHCPlayer.setPlayerState(PlayerState.DEAD);
        gameManager.getWhitelist().remove(p.getUniqueId());
        gameManager.getPlayers().remove(p.getUniqueId());
        gold.remove(p.getUniqueId());
        diamond.remove(p.getUniqueId());
        if (gameManager.getTeamType() != TeamType.TEAMS)
        {
            if (e.getKiller() != null)
            {
                OfflinePlayer k = e.getKiller();
                UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(k.getUniqueId());
                UHCPlayer1.setKills(UHCPlayer1.getKills() + 1);
                UHCPlayer1.getKillsD().increaseAmount(1);
            }
            if (p.isOnline())
            {
                final Player online = p.getPlayer();

                if (!online.hasPermission("rank.spectate") && !online.hasPermission("rank.host"))
                {
                    online.sendMessage("§6§m---------------------------------------");
                    online.sendMessage("§fThank you for playing a §6§lFazonGG UHC");
                    online.sendMessage("");
                    online.sendMessage("§f§lWant to§6§l SPECTATE?");
                    online.sendMessage("§f§lBuy a §6§lPremium Rank§f§l NOW!");
                    online.sendMessage("§f§lSTORE: §6§lhttp://store.fazon.gg");
                    online.sendMessage("§6§m---------------------------------------");
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if(online != null && UHCPlayer.getPlayerState() != PlayerState.INGAME)
                            {
                                online.kickPlayer("");
                            }
                        }
                    }.runTaskLater(core, 20 * 15);
                }
            }
            if (gameManager.getPlayers().size() == 1)
            {
                Player k = Bukkit.getPlayer(gameManager.getPlayers().get(0));
                PlayerWinUHCEvent playerWinUHCEvent = new PlayerWinUHCEvent(k);
                Bukkit.getPluginManager().callEvent(playerWinUHCEvent);
                return;
            }
            if (gameManager.getPlayers().size() == 0)
            {
                UHCEndEvent uhcEndEvent = new UHCEndEvent();
                Bukkit.getPluginManager().callEvent(uhcEndEvent);
            }
        }
        else
        {
            if (e.getKiller() != null)
            {
                OfflinePlayer k = e.getKiller();
                UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(k.getUniqueId());
                UHCPlayer1.setKills(UHCPlayer1.getKills() + 1);
                UHCPlayer1.getKillsD().increaseAmount(1);
                Team team = teamManager.getTeams().get(UHCPlayer1.getTeamNumber());
                team.setKills(team.getKills() + 1);
            }
            UHCPlayer.setTeamDeathNumber(UHCPlayer.getTeamNumber());
            Team team = teamManager.getTeams().get(UHCPlayer.getTeamNumber());
            team.getAlivePlayers().remove(p.getUniqueId());
            if(team.getAlivePlayers().size() == 0)
            {
                teamManager.getTeams().remove(team.getTeamNumber());
            }
            List<Integer> teamsList = new ArrayList<>();
            teamsList.addAll(teamManager.getTeams().keySet());
            for(int teams : teamsList)
            {
                if (teamManager.getTeams().get(teams).getAlivePlayers().size() == 0)
                {
                    teamManager.getTeams().remove(teams);
                }
                else
                {
                    Team team1 = teamManager.getTeams().get(teams);
                    for(UUID uuid : team1.getAlivePlayers())
                    {
                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
                        if(uhcPlayer.getPlayerState() != PlayerState.INGAME)
                        {
                            team1.getAlivePlayers().remove(uuid);
                        }
                    }
                    if (teamManager.getTeams().get(teams).getAlivePlayers().size() == 0)
                    {
                        teamManager.getTeams().remove(teams);
                    }
                }
            }
            if (p.isOnline())
            {
                final Player online = p.getPlayer();
                online.sendMessage(reference.GAME + "§cYou'll be kicked in 30 seconds! Thanks for playing!");
                if (!online.hasPermission("rank.spectate") && !online.hasPermission("rank.host"))
                {
                    online.sendMessage("§6§m---------------------------------------");
                    online.sendMessage("§fThank you for playing a §6§lFazonGG UHC");
                    online.sendMessage("");
                    online.sendMessage("§f§lWant to§6§l SPECTATE?");
                    online.sendMessage("§f§lBuy a §6§lPremium Rank§f§l NOW!");
                    online.sendMessage("§f§lSTORE: §6§lhttp://store.fazon.gg");
                    online.sendMessage("§6§m---------------------------------------");
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            if (online != null && UHCPlayer.getPlayerState() != PlayerState.INGAME)
                            {
                                online.kickPlayer("");
                            }
                        }
                    }.runTaskLater(core, 20 * 15);
                }
            }
            if (teamManager.getTeams().size() == 1)
            {
                if (!won)
                {
                    TeamUHCWinEvent teamUHCWinEvent = new TeamUHCWinEvent(teamManager.getTeams().values().iterator().next());
                    Bukkit.getPluginManager().callEvent(teamUHCWinEvent);
                    won = true;
                }
                return;
            }
            if (gameManager.getPlayers().size() == 0)
            {
                UHCEndEvent uhcEndEvent = new UHCEndEvent();
                Bukkit.getPluginManager().callEvent(uhcEndEvent);
            }
        }
    }

    @EventHandler
    public void onTeamUHCWinEvent(TeamUHCWinEvent e)
    {
        Team team = e.getTeam();
        List<String> names = new ArrayList<>();
        for (UUID uuid : team.getPlayers())
        {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(uuid);
            UHCPlayer1.getWins().increaseAmount(1);
            names.add(offlinePlayer.getName());
        }
        String[] stringArray = names.toArray(new String[names.size()]);
        Bukkit.broadcastMessage(reference.GAME + "§b" + Arrays.toString(stringArray) + " §ehas won the UHC! Thank you for playing a UHC hosted by §cFazonGG");
        Bukkit.broadcastMessage("§cNow attempting to save stats to the database...");
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (UHCPlayer UHCPlayer : playerManager.getUhcPlayerMap().values())
                {
                    UHCPlayer.saveStats();
                }
            }
        }.runTaskAsynchronously(core);
        Bukkit.broadcastMessage("§aStats have successfully saved");
    }

    @EventHandler
    public void onUHCEnd(UHCEndEvent e)
    {
        Bukkit.broadcastMessage(reference.GAME + "§fThe server will be restarting in§c 40 seconds");
        final World world = Bukkit.getWorld("uhc");
        final World nether = Bukkit.getWorld("uhc_nether");
        worldManager.unloadWorld(world);
        worldManager.unloadWorld(nether);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                worldManager.deleteWorld("uhc");
                worldManager.deleteWorld("uhc_nether");
                Bukkit.broadcastMessage(reference.GAME + "§cDeleted§f the§c UHC Nether");
            }
        }.runTaskLater(core, 20*20);
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reference.END_COMMAND);
            }
        }.runTaskLater(core, 20*40);
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        e.setQuitMessage("");
        final Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(uuid);
        if (uhcPlayer.getPlayerState() == PlayerState.MODERATOR || uhcPlayer.getPlayerState() == PlayerState.HOST)
        {
            p.getInventory().clear();
            p.teleport(reference.SPAWN);
            p.setGameMode(GameMode.SURVIVAL);
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.showPlayer(p);
            }
            uhcPlayer.setPlayerState(PlayerState.LOBBY);
            playerManager.getModerators().remove(uuid);
        }
        if(gameManager.getGameState() == GameState.SCATTERING)
        {
            if(p.isInsideVehicle())
            {
                if(p.getVehicle() instanceof Horse)
                {
                    p.getVehicle().remove();
                }
            }
        }
        if(uhcPlayer.isFrozen())
        {
            Bukkit.broadcastMessage("§c[Alert]" + p.getName() + " logged out while frozen");
        }
        if (uhcPlayer.getPlayerState() == PlayerState.SCATTERING || uhcPlayer.getPlayerState() == PlayerState.SCATTERED)
        {
            int bukkitTask = Bukkit.getScheduler().scheduleSyncDelayedTask(core, new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (playerRunnable2.containsKey(p.getUniqueId()))
                    {
                        playerRunnable2.remove(p.getUniqueId());
                        PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(Bukkit.getOfflinePlayer(p.getUniqueId()));
                        Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                        gameManager.getWhitelist().remove(p.getUniqueId());
                    }
                }
            }, 12000L);
            playerRunnable2.put(p.getUniqueId(), bukkitTask);
        }
        else if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
        {
            net.minecraft.server.v1_7_R4.World world = ((CraftWorld) p.getWorld()).getHandle();
            final CombatSkeleton combatSkeleton = new CombatSkeleton(world);
            combatSkeleton.setCustomName(p.getName() + " [Combat Skeleton]");
            MobUtil.spawnEntity(combatSkeleton, p.getLocation());
            combatSkeleton.setEquipment(1, CraftItemStack.asNMSCopy(p.getInventory().getHelmet()));
            combatSkeleton.setEquipment(2, CraftItemStack.asNMSCopy(p.getInventory().getChestplate()));
            combatSkeleton.setEquipment(3, CraftItemStack.asNMSCopy(p.getInventory().getLeggings()));
            combatSkeleton.setEquipment(4, CraftItemStack.asNMSCopy(p.getInventory().getBoots()));
            final Entity entity = combatSkeleton.getBukkitEntity();
            ((LivingEntity) entity).setRemoveWhenFarAway(false);
            chunks.add(entity.getLocation().getChunk());
            uhcPlayer.setCombatSkeletonUUID(entity.getUniqueId());
            uhcPlayer.setItemStacks(p.getPlayer().getInventory().getContents());
            uhcPlayer.setLevels((int) p.getExp());
            uhcPlayer.setDeathLocation(p.getLocation());
            uhcPlayer.setArmorItemStacks(p.getPlayer().getInventory().getArmorContents());
            int bukkitTask = Bukkit.getScheduler().scheduleSyncDelayedTask(core, new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (playerRunnable.containsKey(p.getUniqueId()))
                    {
                        chunks.remove(entity.getLocation().getChunk());
                        entity.remove();
                    }
                }
            }, 12000L);
            playerRunnable.put(p.getUniqueId(), bukkitTask);
            int bukkitTask1 = Bukkit.getScheduler().scheduleSyncDelayedTask(core, new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if (playerRunnable2.containsKey(p.getUniqueId()))
                    {
                        playerRunnable2.remove(p.getUniqueId());
                        PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(Bukkit.getOfflinePlayer(p.getUniqueId()));
                        Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                    }
                }
            }, 12000L);
            playerRunnable2.put(p.getUniqueId(), bukkitTask1);
        }
        playerManager.getModerators().remove(p.getUniqueId());
        playerManager.getSpectators().remove(p.getUniqueId());
        gameManager.getArenaPlayers().remove(p.getUniqueId());
        if(uhcPlayer.getPlayerState() == PlayerState.ARENA)
        {
            uhcPlayer.setPlayerState(PlayerState.LOBBY);
        }
    }

    private final Map<UUID, Integer> playerRunnable = new HashMap<>();
    private final Map<UUID, Integer> playerRunnable2 = new HashMap<>();

    private final List<Chunk> chunks = new ArrayList<>();

    @EventHandler
    public void onChunk(ChunkUnloadEvent e)
    {
        if (chunks.contains(e.getChunk()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFillFinish(WorldBorderFillFinishedEvent e)
    {
        if (e.getWorld().getName().equalsIgnoreCase("uhc"))
        {
            Bukkit.broadcastMessage(reference.GAME + "§fFinished filling world with a total of§c " + e.getTotalChunks() + " chunks");
            Bukkit.broadcastMessage(reference.GAME + "§fRestarting server to clear memory in§c 10 seconds");
            new BukkitRunnable()
            {
                @Override
                public void run()
                {

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reference.END_COMMAND);
                }

            }.runTaskLater(core, 200L);
        }
        else if (e.getWorld().getName().equalsIgnoreCase("uhc_nether"))
        {
            Bukkit.broadcastMessage(reference.GAME + "§fFinished filling nether with a total of§c " + e.getTotalChunks() + " chunks");
            Bukkit.broadcastMessage(reference.GAME + "§fRestarting server to clear memory in§c 5 seconds");
            new BukkitRunnable()
            {
                @Override
                public void run()
                {

                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reference.END_COMMAND);
                }

            }.runTaskLater(core, 200L);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        final Player p = e.getPlayer();
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        scoreboardManager.createScoreboard(p, uhcPlayer);
        p.spigot().setCollidesWithEntities(true);
        for(UUID uuid : playerManager.getModerators())
        {
            Player t = Bukkit.getPlayer(uuid);
            if(t != null)
            {
                p.hidePlayer(t);
            }
        }
        if(gameManager.getGameState() == GameState.LOBBY)
        {
            uhcPlayer.setPlayerState(PlayerState.LOBBY);
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(reference.SPAWN);
            /*p.sendMessage("§8§m-------------------------");
            p.sendMessage("§7Welcome to§e UHC V3 (" + core.getDescription().getVersion() + ")");
            p.sendMessage("");
            p.sendMessage("§ePlayers Online:§7 " + Bukkit.getOnlinePlayers().size());
            p.sendMessage("");
            String host = "";
            if (playerManager.getHost() != null)
            {
                host = Bukkit.getOfflinePlayer(playerManager.getHost()).getName();
            }
            else
            {
                host = "N/A";
            }
            List<String> mods = new ArrayList<>();
            for (UUID uuid : playerManager.getModerators())
            {
                String name = Bukkit.getOfflinePlayer(uuid).getName();
                if (!name.equals(host))
                {
                    mods.add(name);
                }
            }
            p.sendMessage("§eHost:§7 " + host);
            p.sendMessage("§eMods:§7 " + Arrays.toString(mods.toArray()));
            List<String> scenarios = new ArrayList<>();
            for(Scenario scenario : Scenario.values())
            {
                if(scenario.isEnabled())
                {
                    scenarios.add(WordUtils.capitalize(scenario.toString().toLowerCase()));
                }
            }
            p.sendMessage("§eScenarios:§7 " + Arrays.toString(scenarios.toArray()));
            p.sendMessage("");
            p.sendMessage("§cAll bugs should be messaged on Twitter: @BladianMC or Discord: Bladian#6411");
            p.sendMessage("§8§m-------------------------");*/
        }
        if(uhcPlayer.getPlayerState() == PlayerState.LOBBY || uhcPlayer.getPlayerState() == PlayerState.DEAD)
        {
            p.teleport(reference.SPAWN);
            p.getInventory().clear();
        }
        if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
        {
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.showPlayer(p);
            }

            UUID uuid = uhcPlayer.getCombatSkeletonUUID();
            for (Entity entity : Bukkit.getWorld("uhc").getEntities())
            {
                if (entity.getType() == EntityType.SKELETON)
                {
                    Skeleton skeleton = (Skeleton) entity;
                    if (skeleton.getUniqueId().equals(uuid))
                    {
                        skeleton.remove();
                    }
                }
            }
            for (Entity entity : Bukkit.getWorld("uhc_nether").getEntities())
            {
                if (entity.getType() == EntityType.SKELETON)
                {
                    Skeleton skeleton = (Skeleton) entity;
                    if (skeleton.getUniqueId().equals(uuid))
                    {
                        skeleton.remove();
                    }
                }
            }
        }
        if(gameManager.getGameState() == GameState.STARTED)
        {
            if (gameManager.getTimer() <= 60 * 20)
            {
                if (p.hasPermission("rank.emerald") && !p.hasPermission("rank.host") && uhcPlayer.getPlayerState() == PlayerState.LOBBY)
                {
                    gameManager.lateScatter(p);
                }

            }
            else
            {
                if (p.hasPermission("rank.emerald") && !p.hasPermission("rank.host") && (uhcPlayer.getPlayerState() == PlayerState.LOBBY || uhcPlayer.getPlayerState() == PlayerState.DEAD))
                {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(reference.GAME + "§aYou're now in spectator mode");
                    playerManager.getSpectators().add(p.getUniqueId());
                    for (Player all : Bukkit.getOnlinePlayers())
                    {
                        all.hidePlayer(p);
                    }
                    uhcPlayer.setPlayerState(PlayerState.SPECTATOR);
                    inventories.spectator(p);
                }
            }
            if (gameManager.getTimer() <= 60 * 15)
            {
                if (p.hasPermission("rank.diamond") && !p.hasPermission("rank.host") && uhcPlayer.getPlayerState() == PlayerState.LOBBY)
                {
                    gameManager.lateScatter(p);
                }
            }
            else
            {
                if (p.hasPermission("rank.diamond") && !p.hasPermission("rank.host") && (uhcPlayer.getPlayerState() == PlayerState.LOBBY || uhcPlayer.getPlayerState() == PlayerState.DEAD))
                {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(reference.GAME + "§aYou're now in spectator mode");
                    playerManager.getSpectators().add(p.getUniqueId());
                    for (Player all : Bukkit.getOnlinePlayers())
                    {
                        all.hidePlayer(p);
                    }
                    uhcPlayer.setPlayerState(PlayerState.SPECTATOR);
                    inventories.spectator(p);
                }
            }
            if (gameManager.getTimer() <= 60 * 10)
            {
                if (p.hasPermission("rank.gold") && !p.hasPermission("rank.host") && uhcPlayer.getPlayerState() == PlayerState.LOBBY)
                {
                    gameManager.lateScatter(p);
                }
            }
            else
            {
                if (p.hasPermission("rank.gold") && !p.hasPermission("rank.host") && (uhcPlayer.getPlayerState() == PlayerState.LOBBY || uhcPlayer.getPlayerState() == PlayerState.DEAD))
                {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(reference.GAME + "§aYou're now in spectator mode");
                    playerManager.getSpectators().add(p.getUniqueId());
                    for (Player all : Bukkit.getOnlinePlayers())
                    {
                        all.hidePlayer(p);
                    }
                    uhcPlayer.setPlayerState(PlayerState.SPECTATOR);
                    inventories.spectator(p);
                }
            }
        }
        if(uhcPlayer.getPlayerState() == PlayerState.SCATTERING)
        {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
            p.getInventory().addItem(new ItemStack(Material.FEATHER, 16));
            p.getInventory().addItem(new ItemStack(Material.LEATHER, 16));
            p.getInventory().addItem(new ItemStack(Material.STRING, 5));
            /*p.getInventory().addItem(new ItemStack(Material.FEATHER, 32));
            p.getInventory().addItem(new ItemStack(Material.LEATHER, 32));
            p.getInventory().addItem(new ItemStack(Material.STRING, 5));*/
            p.setHealth(20);
            p.setSaturation(20);
            p.setFoodLevel(20);
            p.setLevel(0);
            for (PotionEffect potionEffect : p.getActivePotionEffects())
            {
                p.removePotionEffect(potionEffect.getType());
            }
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.showPlayer(p);
            }
            uhcPlayer.setPlayerState(PlayerState.INGAME);
            p.teleport(uhcPlayer.getScatter());
            if (Scenario.BESTPVE.isEnabled())
            {
                scenarioManager.getBestPVEList().add(p.getUniqueId());
            }
            if (Scenario.INFINITEENCHANTER.isEnabled())
            {
                p.setExp(0);
                p.setLevel(30000);
                p.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
            }
            if (Scenario.GONEFISHING.isEnabled())
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
        if(uhcPlayer.getPlayerState() == PlayerState.SCATTERED)
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
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.showPlayer(p);
            }
            uhcPlayer.setPlayerState(PlayerState.INGAME);
            p.teleport(uhcPlayer.getScatter());
            if (Scenario.BESTPVE.isEnabled())
            {
                scenarioManager.getBestPVEList().add(p.getUniqueId());
            }
            if (Scenario.INFINITEENCHANTER.isEnabled())
            {
                p.setExp(0);
                p.setLevel(30000);
                p.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
                p.getInventory().addItem(new ItemStack(Material.BOOKSHELF, 64));
            }
            if (Scenario.GONEFISHING.isEnabled())
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
        if(uhcPlayer.getPlayerState() == PlayerState.LOBBY)
        {
            p.getInventory().clear();
            p.getInventory().setArmorContents(null);
        }
        if(uhcPlayer.getPlayerState() == PlayerState.SPECTATOR)
        {
            p.setGameMode(GameMode.CREATIVE);
            playerManager.getSpectators().add(p.getUniqueId());
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.hidePlayer(p);
            }
            uhcPlayer.setPlayerState(PlayerState.SPECTATOR);
            inventories.spectator(p);
        }
        if(uhcPlayer.getPlayerState() == PlayerState.HOST || uhcPlayer.getPlayerState() == PlayerState.MODERATOR || uhcPlayer.getPlayerState() == PlayerState.SPECTATOR)
        {
            for(Player all : Bukkit.getOnlinePlayers())
            {
                all.hidePlayer(p);
            }
            for(UUID mods : playerManager.getModerators())
            {
                Player t = Bukkit.getPlayer(mods);
                if(t != null)
                {
                    p.hidePlayer(t);
                    t.hidePlayer(p);
                }
            }
            for(UUID specs : playerManager.getSpectators())
            {
                Player t = Bukkit.getPlayer(specs);
                if(t != null)
                {
                    p.hidePlayer(t);
                    t.hidePlayer(p);
                }
            }
        }
        e.setJoinMessage("");
        if (playerRunnable.containsKey(p.getUniqueId()))
        {
            Bukkit.getScheduler().cancelTask(playerRunnable.get(p.getUniqueId()));
            playerRunnable.remove(p.getUniqueId());
        }
        if (playerRunnable2.containsKey(p.getUniqueId()))
        {
            Bukkit.getScheduler().cancelTask(playerRunnable2.get(p.getUniqueId()));
            playerRunnable2.remove(p.getUniqueId());
        }

    }

    @EventHandler
    public void onGameStateChange(GameStateChangeEvent e)
    {
        for(Player p : Bukkit.getOnlinePlayers())
        {
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            scoreboardManager.changeScoreboard(p, gameManager.getGameState(), uhcPlayer);
            util.setMOTD(gameManager.getGameState().toString());
        }
    }


        /*
        p.setFallDistance(-20);
        chunks.remove(p.getLocation().getChunk());

        e.setJoinMessage("");
        if (gameManager.getGameState() != GameState.STARTED)
        {
            p.getInventory().setArmorContents(null);
            p.getInventory().clear();
            p.setGameMode(GameMode.SURVIVAL);
            p.teleport(Reference.SPAWN);
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.hidePlayer(p);
                p.hidePlayer(all);
            }
            p.removePotionEffect(PotionEffectType.SLOW);
            p.removePotionEffect(PotionEffectType.JUMP);
        }
        else
        {
            //p.setScoreboard(objects.getScoreboardMap().get(p.getUniqueId()));
            p.setFallDistance(-20);
            if (!objects.isInGame(p))
            {
                p.teleport(Reference.SPAWN);
                p.getInventory().clear();
                p.setGameMode(GameMode.SURVIVAL);
                p.getInventory().setArmorContents(null);

                if (objects.getDonators().contains(p.getUniqueId()))
                {
                    if (!objects.isDead(p))
                    {
                        if (objects.getTimer() <= 1220)
                        {
                            p.setGameMode(GameMode.SURVIVAL);
                            p.setFlying(false);
                            p.getInventory().clear();
                            p.getInventory().setArmorContents(null);
                            p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
                            p.setHealth(20);
                            p.setSaturation(20);
                            p.setFoodLevel(20);
                            for (PotionEffect potionEffect : p.getActivePotionEffects())
                            {
                                p.removePotionEffect(potionEffect.getType());
                            }
                            Location location = Util.getLocation();
                            assert location != null;
                            p.teleport(location);
                            p.setMaxHealth(20);
                            objects.getPlayers().add(p.getUniqueId());
                            objects.getPlayerKills().put(p.getUniqueId(), 0);
                            if (Scenario.INFINITEENCHANTER.isEnabled())
                            {
                                p.setLevel(30000);
                                p.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                                p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                            }

                        }
                        else
                        {
                            if (!p.isOp())
                            {
                                p.setGameMode(GameMode.CREATIVE);
                                p.sendMessage("§aYou're now in spectator mode");
                                objects.getSpectators().add(p.getUniqueId());
                                for (Player all : Bukkit.getOnlinePlayers())
                                {
                                    all.hidePlayer(p);
                                }
                                World world = Bukkit.getWorld("uhc");
                                Location location = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 20, 0);
                                p.teleport(location);
                            }
                        }
                    }
                    else
                    {
                        if (objects.getDonators().contains(p.getUniqueId()) && !p.isOp())
                        {
                            p.setGameMode(GameMode.CREATIVE);
                            p.sendMessage("§aYou're now in spectator mode");
                            objects.getSpectators().add(p.getUniqueId());
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.hidePlayer(p);
                            }
                            World world = Bukkit.getWorld("uhc");
                            Location location = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 20, 0);
                            p.teleport(location);
                        }
                    }
                }
            }
            else
            {

                if (objects.getPlayerToScatter().containsKey(p.getUniqueId()))
                {
                    p.teleport(objects.getPlayerToScatter().get(p.getUniqueId()));
                    p.getInventory().clear();
                    p.getInventory().setArmorContents(null);
                    p.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 16));
                    if (Scenario.INFINITEENCHANTER.isEnabled())
                    {
                        p.setLevel(30000);
                        p.getInventory().addItem(new ItemStack(Material.ENCHANTMENT_TABLE, 64));
                        p.getInventory().addItem(new ItemStack(Material.ANVIL, 64));
                    }
                    if (Scenario.BESTPVE.isEnabled())
                    {
                        objects.getBestPVEList().add(p.getUniqueId());

                    }
                    if (Scenario.BACKPACKS.isEnabled())
                    {
                        Team team = ComTeam.playerTeam.get(p.getUniqueId());
                        Inventory inventory = Bukkit.createInventory(null, 27, "BackPack");
                        objects.getBackPacksInventory().put(team, inventory);
                    }
                }
                else
                {
                    if (p.getLocation().getWorld().getName().equalsIgnoreCase("spawn"))
                    {
                        Location location = Util.getLocation();
                        p.teleport(location);
                    }
                }
                for (PotionEffect potionEffect : p.getActivePotionEffects())
                {
                    if (potionEffect.getType() == PotionEffectType.SLOW || potionEffect.getType() == PotionEffectType.JUMP)
                    {
                        p.removePotionEffect(potionEffect.getType());
                    }
                }
            }
        }
        for(UUID uuid : objects.getSpectators())
        {
            Player t = Bukkit.getPlayer(uuid);
            if(t != null)
            {
                p.hidePlayer(t);
            }
        }
    }*/


    @EventHandler
    public void onRespawn(PlayerRespawnEvent e)
    {
        e.setRespawnLocation(reference.SPAWN);
        final Player p = e.getPlayer();
        final UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if(gameManager.getGameState() == GameState.STARTED)
        {
            if (p.hasPermission("rank.spectate") && !p.hasPermission("rank.host"))
            {
                World world = Bukkit.getWorld("uhc");
                Location location = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 15, 0);
                e.setRespawnLocation(location);
                new BukkitRunnable()
                {

                    @Override
                    public void run()
                    {
                        p.setGameMode(GameMode.CREATIVE);
                        p.sendMessage(reference.GAME + "§aYou're now in spectator mode");
                        playerManager.getSpectators().add(p.getUniqueId());
                        for (Player all : Bukkit.getOnlinePlayers())
                        {
                            all.hidePlayer(p);
                        }
                        uhcPlayer.setPlayerState(PlayerState.SPECTATOR);
                        p.spigot().setCollidesWithEntities(false);
                        inventories.spectator(p);
                    }
                }.runTaskLater(core, 20L);
            }
            else
            {
                if (!p.hasPermission("rank.host"))
                {
                    World world = Bukkit.getWorld("uhc");
                    Location location = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 15, 0);
                    e.setRespawnLocation(location);
                    new BukkitRunnable()
                    {

                        @Override
                        public void run()
                        {
                            p.setGameMode(GameMode.CREATIVE);
                            for (Player all : Bukkit.getOnlinePlayers())
                            {
                                all.hidePlayer(p);
                            }
                            inventories.spectator(p);
                            playerManager.getSpectators().add(p.getUniqueId());
                        }
                    }.runTaskLater(core, 20L);
                }
            }
        }
        /*{
            new BukkitRunnable()
            {

                @Override
                public void run()
                {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage("§aYou're now in spectator mode");
                    playerManager.getSpectators().add(p.getUniqueId());
                    for (Player all : Bukkit.getOnlinePlayers())
                    {
                        all.hidePlayer(p);
                    }
                    World world = Bukkit.getWorld("uhc");
                    Location location = new Location(world, 0, world.getHighestBlockYAt(0, 0) + 20, 0);
                    p.teleport(location);
                }
            }.runTaskLater(core, 60L);
        }*/
    }


    @SuppressWarnings("deprecation")
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e)
    {
        if (e.getEntity() instanceof Skeleton)
        {
            Skeleton skeleton = (Skeleton) e.getEntity();
            if (skeleton.getCustomName() != null)
            {
                String splitter = " ";
                String[] split = skeleton.getCustomName().split(splitter);
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(split[0]);
                Location location = skeleton.getLocation();
                location.getBlock().setType(Material.NETHER_FENCE);
                Location locationAbove = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
                Block block = locationAbove.getBlock();
                block.setType(Material.SKULL);
                Skull skull = (Skull) block.getState();
                skull.setSkullType(SkullType.PLAYER);
                skull.setOwner(split[0]);
                skull.setRawData((byte) 1);
                skull.update();
                Bukkit.broadcastMessage(skeleton.getCustomName() + " has died");
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(offlinePlayer.getUniqueId());
                ItemStack[] itemStacks = uhcPlayer.getItemStacks();
                ItemStack[] armorItemStacks = uhcPlayer.getArmorItemStacks();
                for (ItemStack itemStack : itemStacks)
                {

                    if (itemStack != null)
                    {
                        e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack);
                    }
                }
                for (ItemStack itemStack : armorItemStacks)
                {
                    if (itemStack != null)
                    {
                        if (itemStack.getType() != Material.AIR)
                        {
                            e.getEntity().getLocation().getWorld().dropItemNaturally(e.getEntity().getLocation(), itemStack);
                        }
                    }
                }
                if (e.getEntity().getKiller() != null)
                {
                    Player k = e.getEntity().getKiller();
                    UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(k.getUniqueId());
                    if (UHCPlayer1.getPlayerState() == PlayerState.INGAME)
                    {
                        PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(offlinePlayer, k);
                        Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                    }
                }
                else
                {
                    UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(offlinePlayer.getUniqueId());
                    if (UHCPlayer1.getPlayerState() == PlayerState.INGAME)
                    {
                        PlayerUHCDeathEvent playerUHCDeathEvent = new PlayerUHCDeathEvent(offlinePlayer);
                        Bukkit.getPluginManager().callEvent(playerUHCDeathEvent);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent e)
    {
        Player p = e.getPlayer();
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e)
    {
        Player p = e.getPlayer();
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e)
    {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if (e.getEntity() instanceof Player)
        {
            Player p = (Player) e.getEntity();
            UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
            {
                if (e.getCause() == EntityDamageEvent.DamageCause.VOID)
                {
                    p.teleport(reference.SPAWN);
                }
                e.setCancelled(true);
                if(UHCPlayer.getPlayerState() == PlayerState.ARENA)
                {
                    e.setCancelled(false);
                    return;
                }
                return;
            }
            if (gameManager.getGameState() != GameState.STARTED)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCreature(CreatureSpawnEvent e)
    {
        if(gameManager.getGameState() != GameState.STARTED)
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e)
    {
        if(e.getInventory() instanceof HorseInventory)
        {
            if(gameManager.getGameState() != GameState.STARTED)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();

        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
        {
            event.setCancelled(true);

            player.teleport(event.getTo());
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e)
    {
        if (e.getDamager() instanceof Player)
        {
            Player d = (Player) e.getDamager();
            UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(d.getUniqueId());
            if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
            {
                e.setCancelled(true);
            }
            if (e.getEntity() instanceof Player)
            {
                Player p = (Player) e.getEntity();
                UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                if(UHCPlayer1.getPlayerState() == PlayerState.ARENA)
                {
                    e.setCancelled(true);
                    if(gameManager.getArenaPlayers().contains(p.getUniqueId()))
                    {
                        e.setCancelled(false);
                        return;
                    }
                }
                if (UHCPlayer1.getPlayerState() == PlayerState.INGAME && UHCPlayer.getPlayerState() == PlayerState.INGAME)
                {
                    if (!gameManager.isPVP())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cPVP enables at " + gameManager.getPvpTime() + " minutes");
                        return;
                    }
                    if(UHCPlayer.isFrozen())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cThe player is currently frozen");
                        return;
                    }
                    if(UHCPlayer1.isFrozen())
                    {
                        e.setCancelled(true);
                        p.sendMessage(reference.ERROR + "You're currently frozen, join the TeamSpeak being messaged to you.");
                    }
                }

            }
        }
        if (e.getDamager() instanceof Arrow)
        {
            if (((Arrow) e.getDamager()).getShooter() instanceof Player)
            {
                if (e.getEntity() instanceof Player)
                {
                    Player d = (Player) ((Arrow) e.getDamager()).getShooter();
                    Player p = (Player) e.getEntity();
                    UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(d.getUniqueId());
                    UHCPlayer UHCPlayer1 = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                    if(UHCPlayer1.getPlayerState() == PlayerState.ARENA)
                    {
                        e.setCancelled(true);
                        if(gameManager.getArenaPlayers().contains(p.getUniqueId()))
                        {
                            e.setCancelled(false);
                            double x = p.getHealth() - e.getFinalDamage();
                            d.sendMessage("§a" + p.getName() + "§f is currently at§c " + (roundToHalf((x / 2))) + "§4♥");
                            return;
                        }
                    }
                    if (!gameManager.isPVP())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cPVP enables at " + gameManager.getPvpTime() + " minutes");
                        return;
                    }
                    if (UHCPlayer.isFrozen())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cThe player is currently frozen");
                        return;
                    }
                    if (UHCPlayer1.isFrozen())
                    {
                        e.setCancelled(true);
                        p.sendMessage(reference.ERROR + "You're currently frozen, join the TeamSpeak being messaged to you.");
                        return;
                    }
                    double x = p.getHealth() - e.getFinalDamage();
                    d.sendMessage("§a" + p.getName() + "§f is currently at§c " + (roundToHalf((x / 2))) + "§4♥");
                }
            }
        }
        if (e.getDamager() instanceof Snowball)
        {
            if (((Snowball) e.getDamager()).getShooter() instanceof Player)
            {
                if (e.getEntity() instanceof Player)
                {
                    Player d = (Player) ((Snowball) e.getDamager()).getShooter();
                    if (!gameManager.isPVP())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cPVP enables at " + gameManager.getPvpTime() + " minutes");
                    }

                }
            }
        }
        if (e.getDamager() instanceof FishHook)
        {
            if (((FishHook) e.getDamager()).getShooter() instanceof Player)
            {
                if (e.getEntity() instanceof Player)
                {
                    Player d = (Player) ((FishHook) e.getDamager()).getShooter();
                    if (!gameManager.isPVP())
                    {
                        e.setCancelled(true);
                        d.sendMessage(reference.ERROR + "§cPVP enables at " + gameManager.getPvpTime() + " minutes");
                    }

                }
            }
        }
        if (e.getEntity().getType() == EntityType.SKELETON)
        {
            Skeleton skeleton = (Skeleton) e.getEntity();
            if (skeleton.getCustomName() != null)
            {
                if (!gameManager.isPVP())
                {
                    e.setCancelled(true);
                }
            }
        }
    }


    private double roundToHalf(double d)
    {
        return Math.round(d * 2) / 2.0;
    }

    @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        if (e.getCaught() instanceof Player)
        {
            Player p = (Player) e.getCaught();
            Player d = e.getPlayer();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            UHCPlayer uhcDamager = playerManager.getUhcPlayerMap().get(d.getUniqueId());
            if(uhcDamager.getPlayerState() == PlayerState.INGAME)
            {
                if (!gameManager.isPVP())
                {
                    e.setCancelled(true);
                    d.sendMessage(reference.ERROR + "§cPVP enables at " + gameManager.getPvpTime() + " minutes");
                    return;
                }
            }
            if(uhcPlayer.isFrozen())
            {
                e.setCancelled(true);
                d.sendMessage(reference.ERROR + "§cThe player is currently frozen");
                return;
            }
            if(uhcDamager.isFrozen())
            {
                e.setCancelled(true);
                p.sendMessage(reference.ERROR + "You're currently frozen, join the TeamSpeak being messaged to you.");
            }
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent e)
    {
        Player p = e.getPlayer();
        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (p.getItemInHand().getType() == Material.LAVA_BUCKET)
        {
            if (!gameManager.isPVP())
            {
                if (gameManager.getTeamType() == TeamType.SOLO)
                {
                    if (gameManager.getPlayersWithin(p, 10).size() != 1)
                    {
                        p.sendMessage(reference.ERROR + "§cYou can't place lava when there's a player 10 blocks next to you");
                        e.setCancelled(true);
                    }
                }
                else
                {
                    if (gameManager.getPlayersWithin(p, 10).size() != 1)
                    {
                        List<Player> players = gameManager.getPlayersWithin(p, 10);
                        Team team = teamManager.getTeams().get(uhcPlayer.getTeamNumber());
                        for (Player all : players)
                        {
                            if (!team.getAlivePlayers().contains(all.getUniqueId()))
                            {
                                p.sendMessage(reference.ERROR + "§cYou can't place lava when there's a player 10 blocks next to you");
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent e)
    {
        final Player p = e.getPlayer();
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
            if(UHCPlayer.getPlayerState() == PlayerState.ARENA)
            {
                e.setCancelled(false);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        if(p != null)
                        {
                            p.getInventory().addItem(new ItemStack(e.getBlock().getType()));
                        }
                        e.getBlock().setType(Material.AIR);
                    }
                }.runTaskLater(core, 100L);
            }
        }
        else
        {
            if(UHCPlayer.isFrozen())
            {
                p.sendMessage(reference.ERROR + "You're currently frozen, join the TeamSpeak being messaged to you.");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event)
    {
        if(reference.CAVE_ENABLED)
        {
            if (event.getWorld().getName().equalsIgnoreCase("uhc"))
            {
                event.getWorld().getPopulators().add(new GiantCave());
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e)
    {
        Player p = e.getPlayer();
        if (gameManager.getGameState() != GameState.STARTED)
        {
            e.setCancelled(true);
            return;
        }
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
            return;
        }
        else
        {
            if(UHCPlayer.isFrozen())
            {
                p.sendMessage(reference.ERROR + "You're currently frozen, join the TeamSpeak being messaged to you.");
                e.setCancelled(true);
            }
        }
        UUID uuid = p.getUniqueId();
        if (e.getBlock().getType() == Material.DIAMOND_ORE)
        {
            if (diamond.get(uuid) == null)
            {
                diamond.put(uuid, 1);
            }
            else
            {
                diamond.put(uuid, diamond.get(uuid) + 1);
            }
            if (diamond.get(uuid) > 10)
            {
                int diamondsMined = diamond.get(uuid);
                int goldMined = 0;
                if(gold.containsKey(uuid))
                {
                    goldMined = gold.get(uuid);
                }
                for (Player all : Bukkit.getOnlinePlayers())
                {
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                    if (uhcPlayer.getPlayerState() == PlayerState.HOST || uhcPlayer.getPlayerState() == PlayerState.MODERATOR)
                    {
                        if(uhcPlayer.isXrayAlerts())
                        {
                            all.sendMessage(XRAY_PREFIX + "§c" + p.getName() + "§7 is mining§b diamonds§7 §8[§bD:" + diamondsMined + "§8][§6G:" + goldMined + "§8]");
                        }
                    }
                }
            }
        }
        if (e.getBlock().getType() == Material.GOLD_ORE)
        {
            if (!gold.containsKey(uuid))
            {
                gold.put(uuid, 1);
            }
            else
            {
                gold.put(uuid, gold.get(uuid) + 1);
            }
        }
    }



    @EventHandler
    public void onEat(PlayerItemConsumeEvent e)
    {
        Player p = e.getPlayer();
        if (e.getItem() != null)
        {
            if (e.getItem().getType() == Material.GOLDEN_APPLE)
            {
                if (e.getItem().getItemMeta().getDisplayName() != null)
                {
                    if (e.getItem().getItemMeta().getDisplayName().equals("§eGolden Head"))
                    {
                        p.removePotionEffect(PotionEffectType.REGENERATION);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
                    }
                }
            }
        }

    }

    @EventHandler
    public void onHungerLoss(FoodLevelChangeEvent e)
    {
        Player p = (Player) e.getEntity();
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
        {
            e.setCancelled(true);
        }
        else
        {
            if (gameManager.getGameState() != GameState.STARTED)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e)
    {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
        {
            if (e.getFrom().getWorld().getName().equalsIgnoreCase("uhc"))
            {
                if (gameManager.getBorderRadius() <= 100)
                {
                    e.getPlayer().sendMessage(reference.ERROR + "§cYou can't go to the nether at 100x100");
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e)
    {
        if (e.getRightClicked() instanceof Player)
        {
            Player c = (Player) e.getRightClicked();
            UHCPlayer UHCClicker = playerManager.getUhcPlayerMap().get(e.getPlayer().getUniqueId());
            if (UHCClicker.getPlayerState() == PlayerState.MODERATOR || UHCClicker.getPlayerState() == PlayerState.HOST)
            {
                e.setCancelled(true);
                if (e.getPlayer().getItemInHand() != null)
                {
                    if (e.getPlayer().getItemInHand().getType() == Material.IRON_INGOT)
                    {
                        Inventory inventory = Bukkit.createInventory(null, 54, "§c" + c.getName() + "'s Inventory");
                        ItemStack pane = util.createItem(Material.STAINED_GLASS_PANE, "");
                        PlayerInventory playerInventory = c.getInventory();
                        inventory.setItem(0, pane);
                        inventory.setItem(1, pane);
                        inventory.setItem(2, playerInventory.getHelmet());
                        inventory.setItem(3, playerInventory.getChestplate());
                        inventory.setItem(4, pane);
                        inventory.setItem(5, playerInventory.getLeggings());
                        inventory.setItem(6, playerInventory.getBoots());
                        inventory.setItem(7, pane);
                        inventory.setItem(8, pane);
                        for (int i = 9; i < 45; i++)
                        {
                            int slot = i - 9;
                            inventory.setItem(i, playerInventory.getItem(slot));
                        }
                        ItemStack level = util.createItem(Material.EXP_BOTTLE, "§a" + c.getLevel() + " levels");
                        ItemStack health = util.createItem(Material.POTION, "§c" + (int) c.getHealth() + "/" + (int) c.getMaxHealth());
                        ItemStack head = util.createItem(Material.SKULL_ITEM, "§a" + c.getName());
                        ItemStack hunger = util.createItem(Material.COOKED_BEEF, "§c" + c.getFoodLevel() + "/20");
                        inventory.setItem(45, pane);
                        inventory.setItem(46, level);
                        inventory.setItem(47, pane);
                        inventory.setItem(48, health);
                        inventory.setItem(49, pane);
                        inventory.setItem(50, head);
                        inventory.setItem(51, pane);
                        inventory.setItem(52, hunger);
                        inventory.setItem(53, pane);
                        e.getPlayer().openInventory(inventory);
                    }
                }
            }
        }
    }

    private Map<UUID, Integer> diamond = new HashMap<>();
    private Map<UUID, Integer> gold = new HashMap<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        Block block = e.getClickedBlock();
        if (e.getClickedBlock() != null)
        {
            if (block.getType() == Material.CHEST)
            {
                if (UHCPlayer.getPlayerState() == PlayerState.MODERATOR || UHCPlayer.getPlayerState() == PlayerState.HOST)
                {
                    e.setCancelled(true);
                    /*InventoryHolder ih = (InventoryHolder) block.getState();
                    Inventory i = ih.getInventory();
                    p.getPlayer().openInventory(i);*/
                }
                if(UHCPlayer.getPlayerState() == PlayerState.SPECTATOR)
                {
                    e.setCancelled(true);
                }
            }
        }
        if(UHCPlayer.getPlayerState() == PlayerState.SPECTATOR || UHCPlayer.getPlayerState() == PlayerState.MODERATOR || UHCPlayer.getPlayerState() == PlayerState.HOST)
        {
            e.setCancelled(true);
        }
        if (e.getItem() != null)
        {
            if (e.getItem().getItemMeta().getDisplayName() != null)
            {
                if (UHCPlayer.getPlayerState() == PlayerState.SPECTATOR)
                {
                    ItemStack itemStack = e.getItem();
                    e.setCancelled(true);
                    if(itemStack.getType() == Material.EMERALD)
                    {
                        Inventory inventory = Bukkit.createInventory(null, 54, "§e§lALIVE PLAYERS");
                        for (UUID uuid : gameManager.getPlayers())
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                            ItemStack head = util.createItemWithData(Material.SKULL_ITEM, offlinePlayer.getName(), 3);
                            inventory.addItem(head);
                        }
                        p.openInventory(inventory);
                    }
                    if(e.getAction() == Action.PHYSICAL)
                    {
                        e.setCancelled(true);
                    }
                }
                if(UHCPlayer.getPlayerState() == PlayerState.MODERATOR || UHCPlayer.getPlayerState() == PlayerState.HOST)
                {
                    ItemStack itemStack = e.getItem();
                    if(e.getAction() == Action.PHYSICAL)
                    {
                        e.setCancelled(true);
                    }
                    if(itemStack.getType() == Material.STICK)
                    {
                        inventories.worldSelect(p);
                    }
                    else if(itemStack.getType() == Material.COMPASS)
                    {
                        for (Player all : gameManager.getPlayersWithin(p, 50))
                        {
                            int distance = (int) p.getLocation().distance(all.getLocation());
                            p.sendMessage(reference.MODERATOR + "§5" + all.getName() + "§f is §5" + distance + "§e blocks§f away");
                        }
                    }
                    else if(itemStack.getType() == Material.EMERALD)
                    {
                        Inventory inventory = Bukkit.createInventory(null, 54, "§e§lALIVE PLAYERS");
                        for (UUID uuid : gameManager.getPlayers())
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                            ItemStack head = util.createItemWithData(Material.SKULL_ITEM, offlinePlayer.getName(), 3);
                            inventory.addItem(head);
                        }
                        p.openInventory(inventory);
                    }
                    else if(itemStack.getType() == Material.DIAMOND)
                    {
                        Map<UUID, Integer> info = util.sortByValue(diamond);
                        Inventory inventory = Bukkit.createInventory(null, 54, "§e§lORES MINED");
                        for(UUID uuid : info.keySet())
                        {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                            ItemStack head = util.createItemWithData(Material.SKULL_ITEM, offlinePlayer.getName(), 3);
                            ItemMeta itemMeta = head.getItemMeta();
                            List<String> ores = new ArrayList<>();
                            ores.add("§bDiamonds Mined:§7 " + info.get(uuid));
                            int mined = 0;
                            if(gold.containsKey(uuid))
                            {
                                mined = gold.get(uuid);
                            }
                            ores.add("§6Gold Mined:§7 " + mined);
                            itemMeta.setLore(ores);
                            head.setItemMeta(itemMeta);
                            inventory.addItem(head);
                        }
                        p.openInventory(inventory);
                    }
                    else if(itemStack.getType() == Material.BOOK)
                    {
                        Player t = gameManager.getRandomPlayer(p);
                        if(t == null)
                        {
                            p.sendMessage(reference.ERROR + "Wasn't able to find a player");
                        }
                        else
                        {
                            t.hidePlayer(p);
                            p.teleport(t);
                            p.sendMessage(reference.MODERATOR + "§fYou've been teleported to§d " + t.getName());
                        }
                    }
                    else if(itemStack.getType() == Material.NETHER_STAR)
                    {
                        World uhcWorld = p.getWorld();
                        p.teleport(new Location(uhcWorld, 0, uhcWorld.getHighestBlockYAt(0, 0) + 15, 0));
                        p.sendMessage(reference.MODERATOR + "§fYou were teleported to the§d center of the map (0,0)");
                    }

                }
            }
        }
        if (e.getClickedBlock() != null)
        {
            if (e.getClickedBlock().getType() == Material.SOIL)
            {
                if (UHCPlayer.getPlayerState() != PlayerState.INGAME && gameManager.getGameState() != GameState.STARTED)
                {
                    if (e.getAction() == Action.PHYSICAL)
                    {
                        e.setCancelled(true);
                    }
                }
            }
            if(p.getTargetBlock((HashSet<Byte>) null, 5).getType() == Material.FIRE)
            {
                if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
                {
                    e.setCancelled(true);
                }
            }
        }
        if(e.getAction() == Action.PHYSICAL)
        {
            if (UHCPlayer.getPlayerState() != PlayerState.INGAME)
            {
                e.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onVehicleMount(VehicleEnterEvent e)
    {
        if (e.getEntered() instanceof Player)
        {
            Player p = (Player) e.getEntered();
            UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if (UHCPlayer.getPlayerState() != PlayerState.INGAME && gameManager.getGameState() != GameState.SCATTERING)
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void FromToHandler(BlockFromToEvent event)
    {
        Block b = event.getBlock();
        if ((b.getType() == Material.STATIONARY_WATER) || (b.getType() == Material.STATIONARY_LAVA))
        {
            boolean continuousFlowMode = isContinuousFlowMode(event.getBlock().getWorld());
            if (continuousFlowMode)
            {
                Chunk c = b.getChunk();
                GCRandom r = new GCRandom(c);
                if ((r.isInGiantCave(b.getX(), b.getY(), b.getZ())) &&
                        (b.getData() == 0)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    boolean isContinuousFlowMode(World world)
    {
        Object handle = ReflectionUtil.getProtectedValue(world, "world");
        Object d = ReflectionUtil.getProtectedValue(handle, "d");
        return ((Boolean)d).booleanValue();
    }

    /*@EventHandler
    public void onChat(AsyncPlayerChatEvent e)
    {
        Player p = e.getPlayer();
        AriesPlayer ariesPlayer = me.aries.core.Core.instance.getPlayerManager().getAriesPlayer(p.getUniqueId());
        UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
        if (UHCPlayer.getPlayerState() == PlayerState.HOST)
        {
            e.setFormat(util.toPrefix("§4Host") + " " + ariesPlayer.getColor() + p.getName() + "§f: " + e.getMessage());
            return;
        }
        if (UHCPlayer.getPlayerState() == PlayerState.MODERATOR)
        {
            e.setFormat(util.toPrefix("§3Mod") + " " + ariesPlayer.getColor() + p.getName() + "§f: " + e.getMessage());
            return;
        }
        if (UHCPlayer.getPlayerState() == PlayerState.SPECTATOR)
        {
            e.setFormat(util.toPrefix("§eSpec") + " " + ariesPlayer.getPrefix() + p.getName() + "§f: " + e.getMessage());
            List<Player> list = new ArrayList<>();
            list.addAll(e.getRecipients());
            for (Player all : list)
            {
                if (UHCPlayer.getPlayerState() != PlayerState.HOST && UHCPlayer.getPlayerState() != PlayerState.MODERATOR && UHCPlayer.getPlayerState() != PlayerState.SPECTATOR)
                {
                    e.getRecipients().remove(all);
                }
            }
            return;
        }
        if (UHCPlayer.getPlayerState() == PlayerState.DEAD)
        {
            e.setFormat(util.toPrefix("§aDead") + " " + ariesPlayer.getPrefix() + p.getName() + "§f: " + e.getMessage());
            List<Player> list = new ArrayList<>();
            list.addAll(e.getRecipients());
            for (Player all : list)
            {
                if (UHCPlayer.getPlayerState() != PlayerState.HOST && UHCPlayer.getPlayerState() != PlayerState.MODERATOR && UHCPlayer.getPlayerState() != PlayerState.SPECTATOR)
                {
                    e.getRecipients().remove(all);
                }
            }
            return;
        }
        if (gameManager.getTeamType() == TeamType.TEAMS)
        {
            if(UHCPlayer.getTeamNumber() != -1)
            {
                e.setFormat(teamManager.getPrefix(UHCPlayer.getTeamNumber()) + " " + ariesPlayer.getPrefix() + p.getName() + "§f: " + e.getMessage());
            }
        }
    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent e)
    {
        if(e.getMessage().contains("%"))
        {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cYou can't write a message using that symbol (temporary fix)");
        }
        if(!e.isCancelled())
        {
            Player p = e.getPlayer();

            UHCPlayer UHCPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if (UHCPlayer.getPlayerState() == PlayerState.HOST)
            {
                e.setFormat(util.toPrefix("§4Host") + " §f" + e.getFormat());
                if (e.getMessage().startsWith("-s"))
                {
                    e.setMessage(e.getMessage().replace("-s", ""));
                    e.setFormat(util.toPrefix("§dSpectator") + " §f" + e.getFormat().replaceAll("-s", ""));
                    List<Player> list = new ArrayList<>();
                    list.addAll(e.getRecipients());
                    for (Player all : list)
                    {
                        UHCPlayer uhcAll = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                        if (uhcAll.getPlayerState() == PlayerState.INGAME || uhcAll.getPlayerState() == PlayerState.DEAD || uhcAll.getPlayerState() == PlayerState.LOBBY)
                        {
                            e.getRecipients().remove(all);
                        }
                    }
                }
                if (e.getMessage().startsWith("-d"))
                {
                    e.setMessage(e.getMessage().replace("-d", ""));
                    e.setFormat(util.toPrefix("§aDead") + " §f" + e.getFormat().replaceAll("-d", ""));
                    List<Player> list = new ArrayList<>();
                    list.addAll(e.getRecipients());
                    for (Player all : list)
                    {
                        UHCPlayer uhcAll = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                        if (uhcAll.getPlayerState() == PlayerState.INGAME || uhcAll.getPlayerState() == PlayerState.SPECTATOR || uhcAll.getPlayerState() == PlayerState.LOBBY)
                        {
                            e.getRecipients().remove(all);
                        }
                    }
                }
                return;
            }
            if (UHCPlayer.getPlayerState() == PlayerState.MODERATOR)
            {
                e.setFormat(util.toPrefix("§3Mod") + " §f" + e.getFormat());
                if (e.getMessage().startsWith("-s"))
                {
                    e.setFormat(util.toPrefix("§dSpectator") + " §f" + e.getFormat().replaceAll("-s", ""));
                    e.setMessage(e.getMessage().replaceAll("-s", ""));
                    List<Player> list = new ArrayList<>();
                    list.addAll(e.getRecipients());
                    for (Player all : list)
                    {
                        UHCPlayer uhcAll = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                        if (uhcAll.getPlayerState() == PlayerState.INGAME || uhcAll.getPlayerState() == PlayerState.DEAD || uhcAll.getPlayerState() == PlayerState.LOBBY)
                        {
                            e.getRecipients().remove(all);
                        }
                    }
                }
                if (e.getMessage().startsWith("-d"))
                {
                    e.setMessage(e.getMessage().replace("-d", ""));
                    e.setFormat(util.toPrefix("§aDead") + " §f" + e.getFormat().replaceAll("-d", ""));
                    List<Player> list = new ArrayList<>();
                    list.addAll(e.getRecipients());
                    for (Player all : list)
                    {
                        UHCPlayer uhcAll = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                        if (uhcAll.getPlayerState() == PlayerState.INGAME || uhcAll.getPlayerState() == PlayerState.SPECTATOR || uhcAll.getPlayerState() == PlayerState.LOBBY)
                        {
                            e.getRecipients().remove(all);
                        }
                    }
                }
                return;
            }
            if (UHCPlayer.getPlayerState() == PlayerState.SPECTATOR)
            {
                e.setFormat(util.toPrefix("§dSpectator") + " §f" + e.getFormat());
                Iterator<Player> iter = e.getRecipients().iterator();
                while (iter.hasNext()) {
                    Player all = iter.next();
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                    {
                        iter.remove();
                    }
                }
                return;
            }
            if (UHCPlayer.getPlayerState() == PlayerState.DEAD)
            {
                e.setFormat(util.toPrefix("§aDead") + " §f" + e.getFormat());
                Iterator<Player> iter = e.getRecipients().iterator();
                while (iter.hasNext()) {
                    Player all = iter.next();
                    UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(all.getUniqueId());
                    if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                    {
                        iter.remove();
                    }
                }
                return;
            }
            if (gameManager.getTeamType() == TeamType.TEAMS && (UHCPlayer.getPlayerState() != PlayerState.DEAD &&
                    UHCPlayer.getPlayerState() != PlayerState.HOST
                    && UHCPlayer.getPlayerState() != PlayerState.MODERATOR
                    && UHCPlayer.getPlayerState() != PlayerState.SPECTATOR))
            {
                if (UHCPlayer.getTeamNumber() != -1)
                {
                    e.setFormat(teamManager.getPrefix(UHCPlayer.getTeamNumber()) + " §f" + e.getFormat());
                }
            }
            if (gameManager.getGameState() == GameState.SCATTERING)
            {
                if (!p.hasPermission("rank.host"))
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e)
    {
        if (e.getMessage().equalsIgnoreCase("/restart"))
        {
            if (e.getPlayer().hasPermission("rank.owner"))
            {
                e.setCancelled(true);
                for (Player p : Bukkit.getOnlinePlayers())
                {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF("Hub");
                    p.sendPluginMessage(core, "BungeeCord", out.toByteArray());
                }
                new BukkitRunnable()
                {

                    @Override
                    public void run()
                    {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), reference.END_COMMAND);
                    }
                }.runTaskLater(core, 100L);
                return;
            }
        }
        final Player p = e.getPlayer();
        String[] split = e.getMessage().toLowerCase().split(" ");
        String check = split[0].toLowerCase();
        if(check.contains("/me"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/bukkit:me"))
        {
            e.setCancelled(true);
        }
        if(check.contains("//calc"))
        {
            e.setCancelled(true);
        }
        if(check.contains("//calculate"))
        {
            e.setCancelled(true);
        }
        if(check.contains("//solve"))
        {
            e.setCancelled(true);
        }
        if(check.contains("//eval"))
        {
            e.setCancelled(true);
        }
        if(check.contains("//evaluate"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/worldedit:/calc"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/worldedit:/calculate"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/worldedit:/solve"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/worldedit:/eval"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/worldedit:/evaluate"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/minecraft:me"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/pl"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/plugins"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/bukkit:pl"))
        {
            e.setCancelled(true);
        }
        if(check.contains("/bukkit:plugins"))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBuild(BlockCanBuildEvent e)
    {
        e.setBuildable(true);
    }
}