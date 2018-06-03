package me.bladian.uhc.player;

import me.bladian.uhc.Core;
import me.bladian.uhc.enums.PlayerState;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.manager.PlayerManager;
import me.bladian.uhc.util.Reference;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerPortalEvent;

/**
 * Created by BladianYT
 */
public class EventStat implements Listener
{

    private final Core core = Core.instance;
    private Reference reference = core.getReference();
    private PlayerManager playerManager = core.getPlayerManager();
    private GameManager gameManager = core.getGameManager();

    @EventHandler
    public void onInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
        {
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if (uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)
                {
                    if (p.getItemInHand() != null)
                    {
                        if (p.getItemInHand().getType() == Material.WOOD_SWORD || p.getItemInHand().getType() == Material.STONE_SWORD ||
                                p.getItemInHand().getType() == Material.GOLD_SWORD || p.getItemInHand().getType() == Material.IRON_SWORD
                                || p.getItemInHand().getType() == Material.DIAMOND_SWORD)
                        {
                            uhcPlayer.getSwordSwings().increaseAmount(1);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e)
    {
        if (!e.isCancelled())
        {
            if (e.getEntity() instanceof Player)
            {
                Player p = (Player) e.getEntity();
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                {
                    if (!e.isCancelled())
                    {
                        if (e.getDamager() instanceof Player)
                        {
                            Player d = (Player) e.getDamager();
                            UHCPlayer uhcDamager = playerManager.getUhcPlayerMap().get(d.getUniqueId());
                            if(p.getItemInHand() != null)
                            {
                                if (p.getItemInHand().getType() == Material.WOOD_SWORD || p.getItemInHand().getType() == Material.STONE_SWORD ||
                                        p.getItemInHand().getType() == Material.GOLD_SWORD || p.getItemInHand().getType() == Material.IRON_SWORD
                                        || p.getItemInHand().getType() == Material.DIAMOND_SWORD)
                                {
                                    uhcDamager.getSwordHits().increaseAmount(1);
                                }
                            }
                            uhcDamager.getHeartsDealt().increaseAmount((int) (e.getFinalDamage()));
                        }
                        else if (e.getDamager() instanceof Arrow)
                        {
                            Arrow arrow = (Arrow) e.getDamager();
                            if (arrow.getShooter() instanceof Player)
                            {
                                Player d = (Player) arrow.getShooter();
                                UHCPlayer uhcDamager = playerManager.getUhcPlayerMap().get(d.getUniqueId());
                                uhcDamager.getBowHits().increaseAmount(1);
                                uhcDamager.getHeartsDealt().increaseAmount((int) (e.getFinalDamage()));
                            }
                        }
                    }
                }
            }
        }
    }

    private double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e)
    {
        if(!e.isCancelled())
        {
            if(e.getEntity() instanceof Player)
            {
                Player p = (Player) e.getEntity();
                UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
                if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                {
                    uhcPlayer.getHeartsLost().increaseAmount((int) e.getFinalDamage());
                }
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e)
    {
        if (!e.isCancelled())
        {
            Player p = e.getPlayer();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                if (e.getItem().getType() == Material.GOLDEN_APPLE)
                {

                    if (e.getItem().getItemMeta().getDisplayName() != null)
                    {
                        if (e.getItem().getItemMeta().getDisplayName().equals("§eGolden Head"))
                        {
                            uhcPlayer.getHeadsEaten().increaseAmount(1);
                        }
                        else
                        {
                            uhcPlayer.getGapplesEaten().increaseAmount(1);
                        }
                    }
                    else
                    {
                        uhcPlayer.getGapplesEaten().increaseAmount(1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent e)
    {
        if(!e.isCancelled())
        {
                if(e.getEntity() instanceof Arrow)
                {
                    if (e.getEntity().getShooter() instanceof Player)
                    {
                        Player d = (Player) e.getEntity().getShooter();
                        UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(d.getUniqueId());
                        if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
                        {
                            uhcPlayer.getBowShots().increaseAmount(1);
                        }
                    }
                }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e)
    {
        Player p = e.getPlayer();
        if(!e.isCancelled())
        {
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                uhcPlayer.getBlocksBroken().increaseAmount(1);
                switch (e.getBlock().getType())
                {
                    case COAL_ORE:
                        uhcPlayer.getCoalMined().increaseAmount(1);
                        break;
                    case IRON_ORE:
                        uhcPlayer.getIronMined().increaseAmount(1);
                        break;
                    case GOLD_ORE:
                        uhcPlayer.getGoldMined().increaseAmount(1);
                        break;
                    case LAPIS_ORE:
                        uhcPlayer.getLapisMined().increaseAmount(1);
                        break;
                    case REDSTONE_ORE:
                        uhcPlayer.getRedstoneMined().increaseAmount(1);
                        break;
                    case GLOWING_REDSTONE_ORE:
                        uhcPlayer.getRedstoneMined().increaseAmount(1);
                        break;
                    case DIAMOND_ORE:
                        uhcPlayer.getDiamondMined().increaseAmount(1);
                        break;
                    case EMERALD_ORE:
                        uhcPlayer.getEmeraldMined().increaseAmount(1);
                        break;
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        if(!e.isCancelled())
        {
            Player p = e.getPlayer();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {

                uhcPlayer.getBlocksPlaced().increaseAmount(1);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent e)
    {
        if(!e.isCancelled())
        {
            Player p = e.getPlayer();
            UHCPlayer uhcPlayer = playerManager.getUhcPlayerMap().get(p.getUniqueId());
            if(uhcPlayer.getPlayerState() == PlayerState.INGAME)
            {
                if(e.getTo().getWorld().getEnvironment() == World.Environment.NETHER)
                {

                    uhcPlayer.getNethersEntered().increaseAmount(1);
                }

            }
        }
    }
}
