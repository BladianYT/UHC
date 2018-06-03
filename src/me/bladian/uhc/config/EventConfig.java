package me.bladian.uhc.config;

import me.bladian.uhc.Core;
import me.bladian.uhc.manager.ConfigManager;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BladianYT
 */
public class EventConfig implements Listener
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private ConfigManager configManager = core.getConfigManager();

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e)
    {
        if (!configManager.isEnderpearlDamage())
        {
            Player p = e.getPlayer();
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL)
            {
                e.setCancelled(true);
                p.teleport(e.getTo());
            }
        }
    }

    @EventHandler
    public void onBrew(BrewEvent e)
    {
        if(e.getContents().getIngredient().getType() == Material.SUGAR)
        {
            if(!configManager.isSpeed1())
            {
                e.setCancelled(true);
            }
        }
        if(e.getContents().getIngredient().getType() == Material.BLAZE_POWDER)
        {
            if(!configManager.isStrength1())
            {
                e.setCancelled(true);
            }
        }
        List<ItemStack> potions = new ArrayList<>();
        potions.addAll(Arrays.asList(e.getContents().getContents()));
        for(ItemStack itemStack : potions)
        {
            if(itemStack.getType() == Material.POTION)
            {
                Potion potion = Potion.fromItemStack(itemStack);
                if(potion.getType() == PotionType.SPEED && potion.getLevel() == 1)
                {
                    if(!configManager.isSpeed2())
                    {
                        e.setCancelled(true);
                    }
                }
                if(potion.getType() == PotionType.STRENGTH && potion.getLevel() == 1)
                {
                    if(!configManager.isStrength2())
                    {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onCraftItem(PrepareItemCraftEvent e)
    {
        Material itemType = e.getRecipe().getResult().getType();
        Byte itemData = e.getRecipe().getResult().getData().getData();
        if (itemType == Material.GOLDEN_APPLE && itemData == 1)
        {
            if(!configManager.isGodApples())
            {
                e.getInventory().setResult(new ItemStack(Material.AIR));
            }
        }
    }


    @EventHandler
    public void onHealthRegain(EntityRegainHealthEvent e)
    {
        if(e.getEntity() instanceof Horse)
        {
            if(!configManager.isHorseHealing())
            {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(final PlayerPortalEvent e)
    {
        if(!configManager.isNether())
        {
            e.setCancelled(true);
        }
        else
        {
            if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)
            {
                if(e.getFrom().getWorld().getName().equalsIgnoreCase("uhc"))
                {
                    Player p = e.getPlayer();
                    double x = p.getLocation().getX() / 8;
                    double y = p.getLocation().getY();
                    double z = p.getLocation().getZ() / 8;
                    e.setTo(e.getPortalTravelAgent().findOrCreate(new Location(Bukkit.getWorld("uhc_nether"), x, y, z)));
                }
                else if(e.getFrom().getWorld().getName().equalsIgnoreCase("uhc_nether"))
                {
                    Player p = e.getPlayer();
                    double x = p.getLocation().getX() * 8;
                    double y = p.getLocation().getY();
                    double z = p.getLocation().getZ() * 8;
                    e.setTo(e.getPortalTravelAgent().findOrCreate(new Location(Bukkit.getWorld("uhc"), x, y, z)));
                }

            }
        }
    }
}
