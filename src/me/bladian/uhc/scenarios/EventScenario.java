package me.bladian.uhc.scenarios;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.GameState;
import me.bladian.uhc.manager.ConfigManager;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.ScenarioManager;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by BladianYT
 */
public class EventScenario implements Listener
{

    private final Core core = Core.instance;

    private Util util = core.getUtil();
    private Reference reference = core.getReference();
    private ConfigManager configManager = core.getConfigManager();
    private ScenarioManager scenarioManager = core.getScenarioManager();
    private GameManager gameManager = core.getGameManager();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        if(!e.isCancelled())
        {
            Player p = e.getPlayer();
            Block b = e.getBlock();
            if(b.getType() == Material.GRAVEL)
            {
                if (getRandomValue(random, 0, 100, 1) <= gameManager.getFlintRates())
                {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.FLINT));
                }
            }
            if(b.getType() == Material.LEAVES || b.getType() == Material.LEAVES_2)
            {
                if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR)
                {
                    if(p.getItemInHand().getType() == Material.SHEARS)
                    {
                        if(configManager.isShears())
                        {
                            if (getRandomValue(random, 0, 100, 1) <= gameManager.getAppleRates())
                            {
                                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
                            }
                        }
                    }
                    else
                    {
                        if (getRandomValue(random, 0, 100, 1) <= gameManager.getAppleRates())
                        {
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
                        }
                    }
                }
                else
                {
                    if (getRandomValue(random, 0, 100, 1) <= gameManager.getAppleRates())
                    {
                        e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
                    }
                }
            }
            if (Scenario.TIMBER.isEnabled())
            {
                if (b.getType() == Material.LOG || b.getType() == Material.LOG_2)
                {
                    /*MassBlockUpdate massBlockUpdate = CraftMassBlockUpdate.createMassBlockUpdater(core, p.getWorld());
                    massBlockUpdate.setRelightingStrategy(MassBlockUpdate.RelightingStrategy.NEVER);
                    massBlockUpdate.setBlock(b.getX(), b.getY(), b.getZ(), 0);*/
                    //massBlockUpdate.notifyClients();
                    //loop(b, p, massBlockUpdate, b.getX(), b.getZ());
                    p.getInventory().addItem(new ItemStack(b.getType()));
                    loop(b, p);
                }
            }
            if (Scenario.TRIPLEORES.isEnabled())
            {
                if (b.getType() == Material.DIAMOND_ORE)
                {
                    if(!Scenario.BAREBONES.isEnabled())
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.DIAMOND, 3));
                        b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                    }
                    else
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                    }
                }
                if (b.getType() == Material.IRON_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.IRON_INGOT, 3));
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
                if (b.getType() == Material.GOLD_ORE)
                {
                    if(!Scenario.BAREBONES.isEnabled() && !Scenario.GOLDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GOLD_INGOT, 3));
                        b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                    }
                    else
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                    }
                }
                if (b.getType() == Material.COAL_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.COAL, 3));
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
                if (b.getType() == Material.EMERALD_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.EMERALD, 3));
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
                if (b.getType() == Material.GRAVEL)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.FLINT, 3));
                }
                return;
            }
            if (Scenario.CUTCLEAN.isEnabled())
            {
                if (b.getType() == Material.IRON_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.IRON_INGOT));
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
                if (b.getType() == Material.GOLD_ORE)
                {
                    if(!Scenario.BAREBONES.isEnabled() && !Scenario.GOLDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                        b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                        b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                    }
                    else
                    {
                        e.setCancelled(true);
                        b.setType(Material.AIR);
                        b.getState().update();
                    }
                }
                if (b.getType() == Material.GRAVEL)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.FLINT));
                }

            }
            if (Scenario.DIAMONDLESS.isEnabled())
            {
                if (b.getType() == Material.DIAMOND_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                }
            }
            if(Scenario.BAREBONES.isEnabled())
            {
                if (b.getType() == Material.DIAMOND_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
                if (b.getType() == Material.GOLD_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                    b.getWorld().spawn(b.getLocation(), ExperienceOrb.class).setExperience(1);
                }
            }
            if(Scenario.GOLDLESS.isEnabled())
            {
                if(b.getType() == Material.GOLD_ORE)
                {
                    e.setCancelled(true);
                    b.setType(Material.AIR);
                    b.getState().update();
                }
            }
            if(Scenario.BLOODDIAMONDS.isEnabled())
            {
                if(b.getType() == Material.DIAMOND_ORE)
                {
                    p.damage(1);
                }
            }
            /*if(Scenario.LIMITATIONS.isEnabled())
            {
                if(b.getType() == Material.DIAMOND_ORE)
                {
                    if(gameManager.getTimer() <= (10*60))
                    {
                        if(scenarioManager.getLimitationOres().containsKey(p.getUniqueId()))
                        {
                            if(scenarioManager.getLimitationOres().get(p.getUniqueId()) == 9)
                            {
                                p.sendMessage(reference.ERROR + "You can only mine 9 diamonds every 10 minutes");
                                e.setCancelled(true);
                            }
                        }
                    }
                    else
                }
            }*/
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e)
    {
        if(!e.isCancelled())
        {
            if (e.getEntity() instanceof Player)
            {
                Player p = (Player) e.getEntity();
                if (Scenario.FIRELESS.isEnabled())
                {
                    if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || e.getCause() == EntityDamageEvent.DamageCause.LAVA)
                    {
                        e.setCancelled(true);
                    }
                }
                if(Scenario.NOFALL.isEnabled())
                {
                    if(e.getCause() == EntityDamageEvent.DamageCause.FALL)
                    {
                        e.setCancelled(true);
                    }
                }
                if(Scenario.BESTPVE.isEnabled() && !Scenario.FIRELESS.isEnabled())
                {
                    if(scenarioManager.getBestPVEList().contains(p.getUniqueId()))
                    {
                        scenarioManager.getBestPVEList().remove(p.getUniqueId());
                        Bukkit.broadcastMessage("§fBestPVE §8:|: §e" + p.getName() + "§e is no longer on the§c BestPVE list");
                    }
                }
                if(Scenario.NOCLEAN.isEnabled())
                {
                    if(e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK)
                    {
                        if(scenarioManager.getNoCleanList().contains(p.getUniqueId()))
                        {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void  onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if(!e.isCancelled())
        {
            if(Scenario.SWITCHEROO.isEnabled())
            {
                if(e.getEntity() instanceof Player)
                {
                    if(e.getDamager() instanceof Arrow)
                    {
                        Arrow arrow = (Arrow) e.getDamager();
                        if(arrow.getShooter() instanceof Player)
                        {
                            Player shooter = (Player) arrow.getShooter();
                            Player damaged = (Player) e.getEntity();
                            Location sLocation = shooter.getLocation();
                            Location dLocation = damaged.getLocation();
                            shooter.teleport(dLocation);
                            damaged.teleport(sLocation);
                            shooter.sendMessage("§fSwitcheroo §8:|: §eYou got §dswitcheroo'd§e with §e" + damaged.getName());
                            damaged.sendMessage("§fSwitcheroo §8:|: §eYou got §dswitcheroo'd§e with §e" + shooter.getName());
                        }
                    }
                }
            }
            if(Scenario.LONGSHOTS.isEnabled())
            {
                if(e.getEntity() instanceof Player)
                {
                    if(e.getDamager() instanceof Arrow)
                    {
                        Arrow arrow = (Arrow) e.getDamager();
                        if(arrow.getShooter() instanceof Player)
                        {
                            Player shooter = (Player) arrow.getShooter();
                            Player damaged = (Player) e.getEntity();
                            Location sLocation = shooter.getLocation();
                            Location dLocation =  damaged.getLocation();
                            int x = (int) sLocation.distance(dLocation);
                            if(x >= 50)
                            {
                                e.setDamage(e.getDamage() * 1.5);
                                if(shooter.getHealth() > 18)
                                {
                                    shooter.setHealth(20L);
                                }
                                else
                                {
                                    shooter.setHealth(shooter.getHealth() + 2.0);
                                }
                                Bukkit.broadcastMessage("§fLongShots §8:|: §d" + shooter.getName() + "§f got a§b longshot§f of§c " + x + " blocks");
                            }
                        }
                    }
                }
            }
            if(Scenario.NOCLEAN.isEnabled())
            {
                if(e.getEntity() instanceof Player)
                {
                    if(e.getDamager() instanceof Player)
                    {
                        Player p = (Player) e.getEntity();
                        Player d = (Player) e.getDamager();
                        if(scenarioManager.getNoCleanList().contains(p.getUniqueId()))
                        {
                            d.sendMessage(reference.ERROR + "That player has PVP protection enabled");
                            e.setCancelled(true);
                        }
                        else if(scenarioManager.getNoCleanList().contains(d.getUniqueId()))
                        {
                            scenarioManager.getNoCleanList().remove(d.getUniqueId());
                            scenarioManager.getNoCleanScheduler().get(d.getUniqueId()).cancel();
                            d.sendMessage(reference.SCENARIO + "§fYour§3 PVP Protection§f has been removed");
                        }
                    }
                    if(e.getDamager() instanceof Arrow && ((Arrow) e.getDamager()).getShooter() instanceof Player)
                    {
                        Player p = (Player) e.getEntity();
                        Player d = (Player) ((Arrow) e.getDamager()).getShooter();
                        if(scenarioManager.getNoCleanList().contains(p.getUniqueId()))
                        {
                            d.sendMessage(reference.ERROR + "That player has PVP protection enabled");
                            e.setCancelled(true);
                        }
                        else if(scenarioManager.getNoCleanList().contains(d.getUniqueId()))
                        {
                            scenarioManager.getNoCleanList().remove(d.getUniqueId());
                            scenarioManager.getNoCleanScheduler().get(d.getUniqueId()).cancel();
                            d.sendMessage(reference.SCENARIO + "§fYour§3 PVP Protection§f has been removed");
                        }
                    }
                }
            }
            if(Scenario.DONOTDISTURB.isEnabled() && !e.isCancelled())
            {
                if(e.getEntity() instanceof Player)
                {
                    if (e.getDamager() instanceof Player)
                    {
                        Player p = (Player) e.getEntity();
                        Player d = (Player) e.getDamager();

                        if(scenarioManager.getDoNotDisturbInfo().containsKey(d.getUniqueId()))
                        {
                            if(p.getUniqueId().equals(scenarioManager.getDoNotDisturbInfo().get(d.getUniqueId())))
                            {
                                scenarioManager.getDoNotDisturbInfo().
                            }
                            else
                            {
                                d.sendMessage(reference.ERROR + "You can't hit that player, he's not linked to you");
                            }
                        }
                        else
                        {
                            scenarioManager.getDoNotDisturbInfo().put(d.getUniqueId(), p.getUniqueId());
                            scenarioManager.getDoNotDisturbInfo().put(p.getUniqueId(), d.getUniqueId());
                            BukkitTask bukkitTask = new BukkitRunnable()
                            {
                                @Override
                                public void run()
                                {
                                    scenarioManager.getDoNotDisturbTask().remove(d.getUniqueId());
                                    scenarioManager.getDoNotDisturbTask().remove(p.getUniqueId());
                                    scenarioManager.getDoNotDisturbInfo().remove(d.getUniqueId());
                                    scenarioManager.getDoNotDisturbInfo().remove(p.getUniqueId());
                                    if(p != null)
                                    {
                                        p.sendMessage(reference.SCENARIO + "§fYour§3 Do Not Disturb§f status has been removed");
                                    }
                                    if(d != null)
                                    {
                                        d.sendMessage(reference.SCENARIO + "§fYour§3 Do Not Disturb§f status has been removed");
                                    }
                                }
                            }.runTaskLater(core, 25*20L);
                            scenarioManager.getDoNotDisturbTask().put(d.getUniqueId(), bukkitTask);
                            scenarioManager.getDoNotDisturbTask().put(p.getUniqueId(), bukkitTask);

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        Entity e = event.getEntity();
        if (Scenario.CUTCLEAN.isEnabled())
        {
            if (e.getType() == EntityType.CHICKEN)
            {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(Material.FEATHER, 1));
                event.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, 3));
            }
            if (e.getType() == EntityType.COW)
            {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(Material.LEATHER, 1));
                event.getDrops().add(new ItemStack(Material.COOKED_BEEF, 3));
            }
            if (e.getType() == EntityType.PIG)
            {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(Material.GRILLED_PORK, 3));
            }
        }
        if (e.getType() == EntityType.HORSE)
        {
            event.getDrops().clear();
            event.getDrops().add(new ItemStack(Material.LEATHER, 2));
        }
        if(Scenario.BOWLESS.isEnabled())
        {
            if(e.getType() == EntityType.SKELETON)
            {
               for(ItemStack itemStack : event.getDrops())
               {
                   if(itemStack.getType() == Material.BOW)
                   {
                       event.getDrops().remove(itemStack);
                   }
               }
            }
        }
    }

    @EventHandler
    public void onVehicleMount(VehicleEnterEvent e)
    {
        if(!e.isCancelled())
        {
            {
                if (e.getVehicle().getType() == EntityType.HORSE)
                {
                    if (Scenario.HORSELESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        if(Scenario.SOUP.isEnabled())
        {
            Player player = e.getPlayer();
            if (((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)) &&
                    (e.getItem() != null) &&
                    (e.getItem().getType() == Material.MUSHROOM_SOUP))
            {
                e.setCancelled(true);
                player.getItemInHand().setType(Material.BOWL);
                player.setHealth(player.getHealth() + 7.0D > player.getMaxHealth() ? player.getMaxHealth() : player.getHealth() + 7.0D);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e)
    {
        final Player p = e.getEntity();
        final Location location = p.getLocation();
        if (!p.getWorld().getName().equals("spawn"))
        {
            if(Scenario.BESTPVE.isEnabled())
            {
                if(p.getKiller() != null)
                {
                    Player t = p.getKiller();
                    scenarioManager.getBestPVEList().add(t.getUniqueId());
                    Bukkit.broadcastMessage("§fBestPVE §8:|: §e" + t.getName() + "§e has been added back to the§c BestPVE list");
                }
            }
            if(Scenario.WEBCAGE.isEnabled())
            {
                if(p.getKiller() != null)
                {
                    List<Location> locations = util.getSphere(location, 5, true);
                    for(Location blocks : locations)
                    {
                        if(blocks.getBlock().getType() == Material.AIR)
                        {
                            blocks.getBlock().setType(Material.WEB);
                        }
                    }
                }
            }
            if(Scenario.DIAMONDLESS.isEnabled())
            {
                e.getDrops().add(new ItemStack(Material.DIAMOND, 1));
            }
            if(Scenario.GOLDLESS.isEnabled())
            {
                e.getDrops().add(new ItemStack(Material.GOLD_INGOT, 8));
            }
            if(Scenario.BAREBONES.isEnabled())
            {
                e.getDrops().add(new ItemStack(Material.ARROW, 32));
                e.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1));
                e.getDrops().add(new ItemStack(Material.STRING, 2));
                e.getDrops().add(new ItemStack(Material.DIAMOND, 1));
            }
            if (Scenario.TIMEBOMB.isEnabled())
            {
                Inventory inventory = p.getInventory();
                p.getLocation().getBlock().breakNaturally();
                p.getLocation().getBlock().setType(Material.CHEST);
                p.getLocation().add(1, 0, 0).getBlock().breakNaturally();
                p.getLocation().add(1, 0, 0).getBlock().setType(Material.CHEST);
                p.getLocation().add(0, 1, 0).getBlock().setType(Material.AIR);
                p.getLocation().add(1, 1, 0).getBlock().setType(Material.AIR);
                Chest chest = (Chest) p.getLocation().getBlock().getState();
                chest.getInventory().setContents(inventory.getContents());
                chest.getInventory().addItem(p.getInventory().getArmorContents());
                if(Scenario.DIAMONDLESS.isEnabled())
                {
                    inventory.addItem(new ItemStack(Material.DIAMOND, 1));
                }
                e.getDrops().clear();
                ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName("§eGolden Head");
                itemStack.setItemMeta(itemMeta);
                chest.getInventory().addItem(itemStack);
                if(Scenario.DIAMONDLESS.isEnabled())
                {
                    chest.getInventory().addItem(new ItemStack(Material.DIAMOND));
                }
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        location.getWorld().createExplosion(location, 6);
                        location.getWorld().strikeLightning(location);
                        Bukkit.broadcastMessage("§fTimeBomb §8:|: §e" + p.getName() + "§7's corpse has§c blown up");
                    }
                }.runTaskLater(core, 600);
            }
            else
            {
                if(gameManager.getGameState() == GameState.STARTED)
                {
                    location.getBlock().setType(Material.NETHER_FENCE);
                    Location locationAbove = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
                    Block block = locationAbove.getBlock();
                    block.setType(Material.SKULL);
                    Skull skull = (Skull) block.getState();
                    skull.setSkullType(SkullType.PLAYER);
                    skull.setOwner(p.getName());
                    skull.setRawData((byte) 1);
                    skull.update();
                }
            }
            if(Scenario.NOCLEAN.isEnabled())
            {
                if(p.getKiller() != null)
                {
                    final Player k = p.getKiller();
                    final UUID uuidK = k.getUniqueId();
                    scenarioManager.getNoCleanList().add(uuidK);
                    BukkitTask bukkitTask = new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            scenarioManager.getNoCleanList().remove(uuidK);
                            scenarioManager.getNoCleanScheduler().remove(uuidK);
                            k.sendMessage(reference.SCENARIO + "§fYour§3 PVP Protection§f has been removed");
                        }
                    }.runTaskLater(core, 25*20L);
                    scenarioManager.getNoCleanScheduler().put(uuidK, bukkitTask);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e)
    {
        if(!e.isCancelled())
        {
            if(Scenario.BAREBONES.isEnabled())
            {
                if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
                {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent e)
    {
        if(!e.isCancelled())
        {
            switch (e.getCurrentItem().getType())
            {
                case BOW:
                    if (Scenario.BOWLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case WOOD_SWORD:
                    if(Scenario.SWORDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case STONE_SWORD:
                    if(Scenario.SWORDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case GOLD_SWORD:
                    if(Scenario.SWORDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case IRON_SWORD:
                    if(Scenario.SWORDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case DIAMOND_SWORD:
                    if(Scenario.SWORDLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case FISHING_ROD:
                    if(Scenario.RODLESS.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                case ANVIL:
                {
                    if(Scenario.BAREBONES.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                }
                case ENCHANTMENT_TABLE:
                {
                    if(Scenario.BAREBONES.isEnabled())
                    {
                        e.setCancelled(true);
                    }
                    break;
                }
                case GOLDEN_APPLE:
                    if(Scenario.BAREBONES.isEnabled())
                    {
                        e.setCancelled(true);
                    }
            }
        }
    }

    private final Random random = new Random();

    @EventHandler
    public void onDecay(LeavesDecayEvent e)
    {
        if(!e.isCancelled())
        {
            if(Scenario.LUCKYLEAVES.isEnabled())
            {
                if (getRandomValue(random, 0, 100, 1) <= 0.3)
                {
                    e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.GOLDEN_APPLE));
                }
            }
        }
        e.setCancelled(true);
        e.getBlock().setType(Material.AIR);
        if(Scenario.TRIPLEORES.isEnabled())
        {
            if (getRandomValue(random, 0, 100, 1) <= gameManager.getAppleRates()*3)
            {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
        else
        {
            if (getRandomValue(random, 0, 100, 1) <= gameManager.getAppleRates())
            {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if (!e.isCancelled())
        {
            if (e.getInventory().getName().equals("§eScenarios Enabled"))
            {
                e.setCancelled(true);
            }
            if (e.getInventory().getName().equals("§eScenarios Explained"))
            {
                e.setCancelled(true);
            }
        }
    }

    /*private void loop(Block block1, Player p, MassBlockUpdate massBlockUpdate, int mainX, int mainZ)
    {
        for (BlockFace blockface : BlockFace.values())
        {
            if (block1.getRelative(blockface).getType().equals(Material.LOG) || block1.getRelative(blockface).getType().equals(Material.LOG_2) /*||
                    /*block1.getRelative(blockface).getType().equals(Material.LEAVES) || block1.getRelative(blockface).getType().equals(Material.LEAVES_2)*/
            //{
                //Block block = block1.getRelative(blockface);
                /*if(block.getX() < mainX+4 && block.getX() > mainX-4 &&
                        block.getZ() < mainZ+4 && block.getZ() > mainZ-4)
                {*/
                    /*p.getInventory().addItem(new ItemStack(block.getType()));
                    /*massBlockUpdate.setBlock(block.getX(), block.getY(), block.getZ(), 0);*/
              /*  block.setType(Material.AIR);
                    loop(block, p, massBlockUpdate, mainX, mainZ);*/
                //}
            /*}
        }
    }*/

    @EventHandler
    public void onFish(PlayerFishEvent e)
    {
        if(e.getState() == PlayerFishEvent.State.CAUGHT_ENTITY)
        {
            if(e.getCaught() instanceof Player)
            {
                if (Scenario.NOCLEAN.isEnabled())
                {
                    Player p = e.getPlayer();
                    if (scenarioManager.getNoCleanList().contains(p.getUniqueId()))
                    {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private void loop(Block block1, Player p)
    {
        for (BlockFace blockface : BlockFace.values())
        {
            if (block1.getRelative(blockface).getType().equals(Material.LOG) || block1.getRelative(blockface).getType().equals(Material.LOG_2))
            {
                Block block = block1.getRelative(blockface);
                p.getInventory().addItem(new ItemStack(block.getType()));
                block.setType(Material.AIR);
                loop(block, p);
            }
        }
    }

    private double getRandomValue(final Random random,
                                        final int lowerBound,
                                        final int upperBound,
                                        final int decimalPlaces){

        if(lowerBound < 0 || upperBound <= lowerBound || decimalPlaces < 0){
            throw new IllegalArgumentException("Put error message here");
        }

        final double dbl =
                ((random == null ? new Random() : random).nextDouble() //
                        * (upperBound - lowerBound))
                        + lowerBound;
        return Double.parseDouble((String.format("%." + decimalPlaces + "f", dbl)).replace(",", "."));
    }
}
