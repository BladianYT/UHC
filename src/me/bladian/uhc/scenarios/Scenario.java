package me.bladian.uhc.scenarios;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Reference;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BladianYT
 */

public enum Scenario
{

    CUTCLEAN(false, createItem(Material.BLAZE_POWDER, "§bCutClean"), new String[]{"Food and ores are presmelted, removing", "the needs of furnaces"}),//done
    TIMEBOMB(false, createItem(Material.TNT, "§bTimeBomb"), new String[]{"When you kill a player, a chest appears", "containing his drops. After 30 ", "seconds this chest will explode"}),//done
    DIAMONDLESS(false, createItem(Material.DIAMOND, "§bDiamondless"), new String[]{"Diamonds aren't dropped when mined,", "they only drop when a player is killed"}),//done
    BOWLESS(false, createItem(Material.BOW, "§bBowless"), new String[]{"Bows can't be crafted or used"}),//done
    SWORDLESS(false, createItem(Material.WOOD_SWORD, "§bSwordless"), new String[]{"You can't craft swords"}),
    RODLESS(false, createItem(Material.FISHING_ROD, "§bRodless"), new String[]{"You can't craft fishing rods"}),
    TRIPLEORES(false, createItem(Material.PACKED_ICE, "§bTriple Ores"), new String[]{"Triple the amount of an ore drops"}),//done
    BESTPVE(false, createItem(Material.WATCH, "§bBest PVE"), new String[]{"You're part of a BestPVE list, if you take damage", "you're removed from it and to be added back", "you need to kill a player. If you're part of", "the list, you'll gain an extra heart"}),//done
    HORSELESS(false, createItem(Material.SADDLE, "§bHorseless"), new String[]{"You can't tame/ride horses"}),//done
    INFINITEENCHANTER(false, createItem(Material.ENCHANTMENT_TABLE, "§bInfinite Enchanter"), new String[]{"You can infinitely enchant, no limitations"}),//done
    BAREBONES(false, createItem(Material.BONE, "§bBareBones"), new String[]{"You can only mine iron, you get 1 diamond", "1 gapple, 32 arrows and 2 strings from a kill"}),//done
    LONGSHOTS(false, createItem(Material.ARROW, "§bLong Shots"), new String[]{"If you get a shot from more than 50 blocks, ", "you get healed 1 heart, and do 1.5x the damage"} ),//done
    BACKPACKS(false, createItem(Material.CHEST, "§bBack Packs"), new String[]{"You get backpacks which you can share with", " your team"}),
    NOFALL(false, createItem(Material.FEATHER, "§bNoFall"), new String[]{"You take no fall damage"}),
    SWITCHEROO(false, createItem(Material.ENDER_PEARL, "§bSwitcheroo"), new String[]{"Everytime you shoot a player, you switch positions", "with him"}),//done
    LUCKYLEAVES(false, createItem(Material.LEAVES, "§bLucky Leaves"), new String[]{"There's a 0.3% chance of golden apples, ", "dropping from de-spawning leaves"}), //done
    TIMBER(false, createItem(Material.IRON_AXE, "§bTimber"), new String[]{"The whole tree drops when you mine a log"}), //done
    FIRELESS(false, createItem(Material.FLINT_AND_STEEL, "§bFireless"), new String[]{"You don't take any kind of fire/lava damage"}), //done
    GONEFISHING(false, createItem(Material.RAW_FISH, "§bGone Fishing"), new String[]{"You get an Unbreaking 250 and ", "Luck of the Sea 250 fishing rod along with 20 anvils."}),
    SOUP(false, createItem(Material.MUSHROOM_SOUP, "§bSoup"), new String[]{"When you right click a soup, you regain", "3.5 hearts"}),
    BLOODDIAMONDS(false, createItem(Material.REDSTONE, "§bBlood Diamonds"), new String[]{"When you mine a diamond, you", " lose half a heart"}),
    WEBCAGE(false, createItem(Material.WEB, "§bWeb Cage"), new String[]{"When you kill a player a sphere of", " cobwebs surrounds you"}),
    GOLDLESS(false, createItem(Material.GOLD_INGOT, "§bGoldless"), new String[]{"You can't mine gold, players drop 8 gold on death"}),
    NOCLEAN(false, createItem(Material.DIAMOND_SWORD, "§bNoClean"), new String[]{"When you kill a player you get 20", " seconds of invincibility"}),
    LIMITATIONS(false, createItem(Material.REDSTONE_BLOCK, "§bLimitations"), new String[]{"You can only mine 9 diamonds every", " 10 minutes"}),
    DONOTDISTURB(false, createItem(Material.BED, "§bDo Not Disturb"), new String[] {"Once you hit a player your fight cannot be interfered with", "Your tag lasts 30 seconds whenever you hit the player."});

    private boolean enabled;
    private final ItemStack itemStack;
    private String[] explanation;

    private Core core = Core.instance;
    private Reference reference = core.getReference();

    Scenario(boolean enabled, ItemStack itemStack, String[] explanation)
    {
        this.enabled = enabled;
        this.itemStack = itemStack;
        this.explanation = explanation;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    private void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public ItemStack getItemStack()
    {
        return itemStack;
    }

    public String[] getExplanation()
    {
        return explanation;
    }



    public void enable()
    {
        this.setEnabled(true);
        Bukkit.broadcastMessage(reference.SCENARIO + "§3" + this.itemStack.getItemMeta().getDisplayName() + "§f has been§a enabled");
    }

    public void disable()
    {
        this.setEnabled(false);
        Bukkit.broadcastMessage(reference.SCENARIO + "§3" + this.itemStack.getItemMeta().getDisplayName() + "§f has been§c disabled");
    }

    private static Inventory inventory = Bukkit.createInventory(null, 45, "§e§lSCENARIO EDITOR");

    public static Inventory getScenarioEditorInventory()
    {
        return inventory;
    }


    public static Inventory getScenarioInventoryExplanation() {
        Inventory inventory = Bukkit.createInventory(null, 18, "§eScenarios Explained");
        inventory.clear();
        for(Scenario scenario : Scenario.values()) {
            if(scenario.isEnabled()) {
                ItemStack itemStack = scenario.getItemStack();
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLore(Arrays.asList(scenario.getExplanation()));
                itemStack.setItemMeta(itemMeta);
                inventory.addItem(scenario.getItemStack());
            }
        }
        return inventory;
    }

    public static Scenario getScenarioFromString(String string)
    {
        for(Scenario scenario : Scenario.values()) {
            if(scenario.toString().equalsIgnoreCase(string)) {
                return scenario;
            }
        }
        return null;
    }

    public static Scenario getScenarioFromName(String string)
    {
        for(Scenario scenario : Scenario.values()) {
            if(scenario.getItemStack().hasItemMeta())
            {
                if(scenario.getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(string))
                {
                    return scenario;
                }
            }
        }
        return null;
    }

    private static ItemStack createItem(Material material, String name)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    static
    {
        for (Scenario scenario : Scenario.values())
        {
            ItemStack itemStack = scenario.getItemStack().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(" ");
            if (scenario.isEnabled())
            {
                lore.add("§aEnabled §f⬅");
                lore.add("§cDisabled");
            }
            else
            {
                lore.add("§aEnabled");
                lore.add("§cDisabled §f⬅");
            }
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);

        }

        ItemStack enabled = createItem(Material.NETHER_STAR, "§bEnabled Scenarios");

        ItemMeta itemMeta = enabled.getItemMeta();
        List<String> lore = new ArrayList<>();
        for(Scenario scenario : Scenario.values())
        {
            if(scenario.isEnabled())
            {
                lore.add("§8-§f " + scenario.getItemStack().getItemMeta().getDisplayName().replace("§b", "§f"));
            }
        }
        itemMeta.setLore(lore);
        enabled.setItemMeta(itemMeta);
        inventory.setItem(40, enabled);

        ItemStack enableAll = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta enableAllMeta  = enableAll.getItemMeta();
        enableAllMeta.setDisplayName("§aEnable All Scenarios");
        enableAll.setItemMeta(enableAllMeta);
        inventory.setItem(36, enableAll);

        ItemStack disabledAll = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta disabledAllMeta  = disabledAll.getItemMeta();
        disabledAllMeta.setDisplayName("§cDisable All Scenarios");
        disabledAll.setItemMeta(disabledAllMeta);
        inventory.setItem(44, disabledAll);
    }

}
