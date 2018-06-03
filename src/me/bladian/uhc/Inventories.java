package me.bladian.uhc;

import me.bladian.uhc.enums.TeamType;
import me.bladian.uhc.manager.ConfigManager;
import me.bladian.uhc.manager.GameManager;
import me.bladian.uhc.util.ItemBuilder;
import me.bladian.uhc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class Inventories
{

    private Core core = Core.instance;
    private Util util = core.getUtil();
    private ConfigManager configManager = core.getConfigManager();
    private GameManager gameManager = core.getGameManager();

    public Inventories()
    {

        ItemStack arenaAdd = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta arenaAddMeta = arenaAdd.getItemMeta();
        arenaAddMeta.setDisplayName("§e§lADD ARENA LOCATION");
        arenaAdd.setItemMeta(arenaAddMeta);
        ItemStack arenaDelete = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta arenaDeleteMeta = arenaAdd.getItemMeta();
        arenaDeleteMeta.setDisplayName("§e§lDELETE ARENA LOCATION");
        arenaDelete.setItemMeta(arenaDeleteMeta);
        ItemStack arenaKit = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta arenaKitMeta = arenaKit.getItemMeta();
        arenaKitMeta.setDisplayName("§e§lSET ARENA KIT");
        arenaKit.setItemMeta(arenaKitMeta);
        arenaEditor.setItem(1, arenaAdd);
        arenaEditor.setItem(4, arenaDelete);
        arenaEditor.setItem(7, arenaKit);

        kitSelector.setItem(1, new ItemBuilder(Material.IRON_SWORD).setName("§e§lUHC KIT").toItemStack());
        kitSelector.setItem(7, new ItemBuilder(Material.DIAMOND_SWORD).setName("§e§lBUILD UHC KIT").toItemStack());

        worldSelector.setItem(1, util.createItem(Material.GRASS, "§e§lOVERWORLD"));
        worldSelector.setItem(4, util.createItem(Material.SMOOTH_BRICK, "§e§lSPAWN"));
        worldSelector.setItem(7, util.createItem(Material.NETHERRACK, "§e§lNETHER"));

        worldEditor.setItem(10, util.createItem(Material.GRASS, "§e§lCREATE WORLD"));
        worldEditor.setItem(12, util.createItem(Material.GRASS, "§e§lDELETE WORLD"));
        worldEditor.setItem(14, util.createItem(Material.GRASS, "§e§lLOAD WORLD"));
        worldEditor.setItem(16, util.createItem(Material.GRASS, "§e§lRESET WORLD"));

        worldEditor.setItem(28, util.createItem(Material.NETHERRACK, "§e§lCREATE NETHER"));
        worldEditor.setItem(30, util.createItem(Material.NETHERRACK, "§e§lDELETE NETHER"));
        worldEditor.setItem(32, util.createItem(Material.NETHERRACK, "§e§lLOAD NETHER"));
        worldEditor.setItem(34, util.createItem(Material.NETHERRACK, "§e§lRESET NETHER"));
    }

    private final ItemStack center = util.createItem(Material.NETHER_STAR, "§e§lCENTER OF MAP (0,0)");
    private final ItemStack nearby = util.createItem(Material.COMPASS, "§e§lNEARBY PLAYERS");
    private final ItemStack left = util.createItem(Material.EMERALD, "§e§lALIVE PLAYERS");
    private final ItemStack diamonds = util.createItem(Material.DIAMOND, "§e§lDIAMONDS MINED");
    private final ItemStack teleport = util.createItem(Material.BOOK, "§e§lRANDOM PLAYER");
    private final ItemStack world = util.createItem(Material.STICK, "§e§lWORLD TELEPORTER");
    private final ItemStack contents = util.createItem(Material.IRON_INGOT, "§e§lINVENTORY CONTENTS");

    public void moderator(Player p)
    {
        PlayerInventory playerInventory = p.getInventory();
        playerInventory.clear();
        playerInventory.setItem(0, world);
        playerInventory.setItem(1, nearby);
        playerInventory.setItem(2, left);
        playerInventory.setItem(3, teleport);
        playerInventory.setItem(4, center);
        playerInventory.setItem(6, diamonds);
        playerInventory.setItem(8, contents);
        p.setGameMode(GameMode.CREATIVE);
    }

    public void spectator(Player p)
    {
        PlayerInventory playerInventory = p.getInventory();
        playerInventory.clear();
        playerInventory.setItem(0, left);
    }

    private Inventory arenaEditor = Bukkit.createInventory(null, 9, "§e§lARENA EDITOR");


    private Inventory worldEditor = Bukkit.createInventory(null, 45, "§e§lWORLD EDITOR");

    public void worldEditor(Player p)
    {
        p.openInventory(worldEditor);
    }

    private Inventory worldSelector = Bukkit.createInventory(null, 9, "§e§lWORLD SELECTOR");

    public void worldSelect(Player p)
    {
        p.openInventory(worldSelector);
    }

    private Inventory gameEditor = Bukkit.createInventory(null, 36, "§e§lGAME EDITOR");

    private final ItemStack healTime = util.createItem(Material.WATCH, "§e§lFINAL HEAL TIME");
    private final ItemStack pvpTime = util.createItem(Material.WATCH, "§e§lPVP TIME");
    private final ItemStack borderTime = util.createItem(Material.WATCH, "§e§lBORDER TIME");
    private final ItemStack rules = util.createItem(Material.BOOK_AND_QUILL, "§e§lRULES");
    private final ItemStack scenario = util.createItem(Material.PAPER, "§e§lSCENARIO EDITOR");
    private final ItemStack worldItem = util.createItem(Material.DIAMOND_PICKAXE, "§e§lWORLD EDITOR");
    private final ItemStack teamType = util.createItem(Material.EMERALD, "§e§lTEAM TYPE");
    private final ItemStack godApples = util.createItem(Material.GOLDEN_APPLE, "§e§lGOD APPLES");
    private final ItemStack enderpearl = util.createItem(Material.ENDER_PEARL, "§e§lENDERPEARL DAMAGE");
    private ItemStack speed1 = util.createItem(Material.POTION, "§e§lSPEED 1");
    private ItemStack speed2 = util.createItem(Material.POTION, "§e§lSPEED 2");
    private ItemStack strength1 = util.createItem(Material.POTION, "§e§lSTRENGTH 1");
    private ItemStack strength2 = util.createItem(Material.POTION, "§e§lSTRENGTH 2");
    private final ItemStack horseHealing = util.createItem(Material.WHEAT, "§e§lHORSE HEALING");
    private final ItemStack nether = util.createItem(Material.OBSIDIAN, "§e§lNETHER");
    private final ItemStack flint = util.createItem(Material.FLINT, "§e§lFLINT RATES");
    private final ItemStack apple = util.createItem(Material.APPLE, "§e§lAPPLE RATES");
    private final ItemStack shears = util.createItem(Material.SHEARS, "§e§lSHEARS");
    private final ItemStack border = util.createItem(Material.BEDROCK, "§e§lBORDER RADIUS");

    public void gameEditor(Player p)
    {
        gameEditor.clear();

        ItemMeta healMeta = healTime.getItemMeta();
        List<String> healLore = new ArrayList<>();
        healLore.add("§8-§b " + gameManager.getHealTime() + " Minutes");
        healLore.add("§8-§c /healTime <minute>");
        healMeta.setLore(healLore);
        healTime.setItemMeta(healMeta);

        ItemMeta pvpMeta = pvpTime.getItemMeta();
        List<String> pvpLore = new ArrayList<>();
        pvpLore.add("§8-§b " + gameManager.getPvpTime() + " Minutes");
        pvpLore.add("§8-§c /pvpTime <minute>");
        pvpMeta.setLore(pvpLore);
        pvpTime.setItemMeta(pvpMeta);

        ItemMeta borderMeta = borderTime.getItemMeta();
        List<String> borderLore = new ArrayList<>();
        borderLore.add("§8-§b " + gameManager.getBorderTime() + " Minutes");
        borderLore.add("§8-§c /borderTime <minute>");
        borderMeta.setLore(borderLore);
        borderTime.setItemMeta(borderMeta);

        ItemMeta rulesMeta = rules.getItemMeta();
        List<String> rulesLore = new ArrayList<>();
        if(!gameManager.isRules())
        {
            rulesLore.add("§aEnabled §f⬅");
            rulesLore.add("§cDisabled");
        }
        else
        {
            rulesLore.add("§aEnabled");
            rulesLore.add("§cDisabled §f⬅");
        }
        rulesLore.add("");
        rulesLore.add("Warning: Only disable rules if you're");
        rulesLore.add("testing, or if the amount of players");
        rulesLore.add("left to scatter are 0.");
        rulesMeta.setLore(rulesLore);
        rules.setItemMeta(rulesMeta);

        ItemMeta teamMeta = teamType.getItemMeta();
        List<String> teamLore = new ArrayList<>();
        if(gameManager.getTeamType() == TeamType.SOLO)
        {
            teamLore.add("§bSolo §f⬅");
            teamLore.add("§bTeams");
        }
        else
        {
            teamLore.add("§bSolo");
            teamLore.add("§bTeams §f⬅");
        }
        teamMeta.setLore(teamLore);
        teamType.setItemMeta(teamMeta);

        godApples.setData(new MaterialData(Material.GOLDEN_APPLE, (byte) 1));
        ItemMeta godApplesMeta = godApples.getItemMeta();
        List<String> godApplesLore = new ArrayList<>();
        if(configManager.isGodApples())
        {
            godApplesLore.add("§aEnabled §f⬅");
            godApplesLore.add("§cDisabled");
        }
        else
        {
            godApplesLore.add("§aEnabled");
            godApplesLore.add("§cDisabled §f⬅");
        }
        godApplesMeta.setLore(godApplesLore);
        godApples.setItemMeta(godApplesMeta);

        ItemMeta enderpearlMeta = enderpearl.getItemMeta();
        List<String> enderpearlLore = new ArrayList<>();
        if(configManager.isEnderpearlDamage())
        {
            enderpearlLore.add("§aEnabled §f⬅");
            enderpearlLore.add("§cDisabled");
        }
        else
        {
            enderpearlLore.add("§aEnabled");
            enderpearlLore.add("§cDisabled §f⬅");
        }
        enderpearlMeta.setLore(enderpearlLore);
        enderpearl.setItemMeta(enderpearlMeta);

        speed1 = new ItemStack(Material.POTION, 1, (byte) 8194);
        ItemMeta speed1Meta = speed1.getItemMeta();
        speed1Meta.setDisplayName("§e§lSPEED 1");
        List<String> speed1Lore = new ArrayList<>();
        if(configManager.isSpeed1())
        {
            speed1Lore.add("§aEnabled §f⬅");
            speed1Lore.add("§cDisabled");
        }
        else
        {
            speed1Lore.add("§aEnabled");
            speed1Lore.add("§cDisabled §f⬅");
        }
        speed1Meta.setLore(speed1Lore);
        speed1.setItemMeta(speed1Meta);

        speed2 = new ItemStack(Material.POTION, 1, (byte) 8194);
        ItemMeta speed2Meta = speed2.getItemMeta();
        speed2Meta.setDisplayName("§e§lSPEED 2");
        List<String> speed2Lore = new ArrayList<>();
        if(configManager.isSpeed2())
        {
            speed2Lore.add("§aEnabled §f⬅");
            speed2Lore.add("§cDisabled");
        }
        else
        {
            speed2Lore.add("§aEnabled");
            speed2Lore.add("§cDisabled §f⬅");
        }
        speed2Meta.setLore(speed2Lore);
        speed2.setItemMeta(speed2Meta);

        strength1 = new ItemStack(Material.POTION, 1, (byte) 8201);
        ItemMeta strength1Meta = strength1.getItemMeta();
        strength1Meta.setDisplayName("§e§lSTRENGTH 1");
        List<String> strength1Lore = new ArrayList<>();
        if(configManager.isStrength1())
        {
            strength1Lore.add("§aEnabled §f⬅");
            strength1Lore.add("§cDisabled");
        }
        else
        {
            strength1Lore.add("§aEnabled");
            strength1Lore.add("§cDisabled §f⬅");
        }
        strength1Meta.setLore(strength1Lore);
        strength1.setItemMeta(strength1Meta);

        strength2 = new ItemStack(Material.POTION, 1, (byte) 8201);
        ItemMeta strength2Meta = strength2.getItemMeta();
        strength2Meta.setDisplayName("§e§lSTRENGTH 2");
        List<String> strength2Lore = new ArrayList<>();
        if(configManager.isStrength2())
        {
            strength2Lore.add("§aEnabled §f⬅");
            strength2Lore.add("§cDisabled");
        }
        else
        {
            strength2Lore.add("§aEnabled");
            strength2Lore.add("§cDisabled §f⬅");
        }
        strength2Meta.setLore(strength2Lore);
        strength2.setItemMeta(strength2Meta);

        ItemMeta horseHealingMeta = horseHealing.getItemMeta();
        List<String> horseHealingLore = new ArrayList<>();
        if(configManager.isHorseHealing())
        {
            horseHealingLore.add("§aEnabled §f⬅");
            horseHealingLore.add("§cDisabled");
        }
        else
        {
            horseHealingLore.add("§aEnabled");
            horseHealingLore.add("§cDisabled §f⬅");
        }
        horseHealingMeta.setLore(horseHealingLore);
        horseHealing.setItemMeta(horseHealingMeta);

        ItemMeta netherMeta = nether.getItemMeta();
        List<String> netherLore = new ArrayList<>();
        if(configManager.isNether())
        {
            netherLore.add("§aEnabled §f⬅");
            netherLore.add("§cDisabled");
        }
        else
        {
            netherLore.add("§aEnabled");
            netherLore.add("§cDisabled §f⬅");
        }
        netherMeta.setLore(netherLore);
        nether.setItemMeta(netherMeta);

        ItemMeta appleMeta = apple.getItemMeta();
        List<String> appleLore = new ArrayList<>();
        appleLore.add("§8-§b " + gameManager.getAppleRates() + "%");
        appleLore.add("§8-§c /rates apple <percent>");
        appleMeta.setLore(appleLore);
        apple.setItemMeta(appleMeta);

        ItemMeta flintMeta = flint.getItemMeta();
        List<String> flintLore = new ArrayList<>();
        flintLore.add("§8-§b " + gameManager.getFlintRates() + "%");
        flintLore.add("§8-§c /rates flint <percent>");
        flintMeta.setLore(flintLore);
        flint.setItemMeta(flintMeta);

        ItemMeta shearsMeta = shears.getItemMeta();
        List<String> shearsLore = new ArrayList<>();
        if(configManager.isShears())
        {
            shearsLore.add("§aEnabled §f⬅");
            shearsLore.add("§cDisabled");
        }
        else
        {
            shearsLore.add("§aEnabled");
            shearsLore.add("§cDisabled §f⬅");
        }
        shearsMeta.setLore(shearsLore);
        shears.setItemMeta(shearsMeta);

        gameEditor.setItem(0, healTime);
        gameEditor.setItem(1, pvpTime);
        gameEditor.setItem(2, borderTime);
        gameEditor.setItem(4, border);
        gameEditor.setItem(6, flint);
        gameEditor.setItem(7, apple);
        gameEditor.setItem(9, scenario);
        gameEditor.setItem(10, worldItem);
        gameEditor.setItem(18, teamType);
        //gameEditor.setItem(19, rules);
        gameEditor.setItem(27, speed1);
        gameEditor.setItem(28, speed2);
        gameEditor.setItem(29, strength1);
        gameEditor.setItem(30, strength2);
        gameEditor.setItem(31, godApples);
        gameEditor.setItem(32, horseHealing);
        gameEditor.setItem(33, enderpearl);
        gameEditor.setItem(34, nether);
        gameEditor.setItem(35, shears);

        p.openInventory(gameEditor);

    }

    private Inventory borderRadius = Bukkit.createInventory(null, 27, "§e§lBORDER RADIUS");

    private final ItemStack radius3500 = util.createItem(Material.BEDROCK, "§e§l3500 BORDER");
    private final ItemStack radius3000 = util.createItem(Material.BEDROCK, "§e§l3000 BORDER");
    private final ItemStack radius2500 = util.createItem(Material.BEDROCK, "§e§l2500 BORDER");
    private final ItemStack radius2000 = util.createItem(Material.BEDROCK, "§e§l2000 BORDER");
    private final ItemStack radius1500 = util.createItem(Material.BEDROCK, "§e§l1500 BORDER");
    private final ItemStack radius1000 = util.createItem(Material.BEDROCK, "§e§l1000 BORDER");
    private final ItemStack radius500 = util.createItem(Material.BEDROCK, "§e§l500 BORDER");

    public void borderEditor(Player p)
    {
        borderRadius.setItem(10, radius3500);
        borderRadius.setItem(11, radius3000);
        borderRadius.setItem(12, radius2500);
        borderRadius.setItem(13, radius2000);
        borderRadius.setItem(14, radius1500);
        borderRadius.setItem(15, radius1000);
        borderRadius.setItem(16, radius500);

        p.openInventory(borderRadius);
    }

    private ItemStack helmet = new ItemBuilder(Material.IRON_HELMET).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2).toItemStack();
    private ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();
    private ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack();
    private ItemStack boots = new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 2).toItemStack();
    private ItemStack sword = new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 2).toItemStack();
    private ItemStack bow = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 2).toItemStack();
    private ItemStack rod = new ItemBuilder(Material.FISHING_ROD).toItemStack();
    private ItemStack arrow = new ItemBuilder(Material.ARROW, 32).toItemStack();
    private ItemStack gApple = new ItemBuilder(Material.GOLDEN_APPLE).toItemStack();

    private ItemStack helmetB = new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_PROJECTILE, 3).toItemStack();
    private ItemStack chestplateB = new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack();
    private ItemStack leggingsB = new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).toItemStack();
    private ItemStack bootsB = new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_PROJECTILE, 3).toItemStack();
    private ItemStack swordB = new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).toItemStack();
    private ItemStack bowB = new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).toItemStack();
    private ItemStack rodB = new ItemBuilder(Material.FISHING_ROD).toItemStack();
    private ItemStack arrowB = new ItemBuilder(Material.ARROW, 48).toItemStack();
    private ItemStack gAppleB = new ItemBuilder(Material.GOLDEN_APPLE, 6).toItemStack();
    private ItemStack gHead = new ItemBuilder(Material.GOLDEN_APPLE, 3).setName("§eGolden Head").toItemStack();
    private ItemStack blocks = new ItemBuilder(Material.COBBLESTONE, 64).toItemStack();

    public void arena(Player p, int number)
    {
        PlayerInventory playerInventory = p.getInventory();
        if(number == 1)
        {
            playerInventory.setHelmet(helmet);
            playerInventory.setChestplate(chestplate);
            playerInventory.setLeggings(leggings);
            playerInventory.setBoots(boots);
            playerInventory.setItem(0, sword);
            playerInventory.setItem(1, rod);
            playerInventory.setItem(2, bow);
            playerInventory.setItem(3, gApple);
            playerInventory.setItem(8, arrow);
            return;
        }
        if(number == 2)
        {
            playerInventory.setHelmet(helmetB);
            playerInventory.setChestplate(chestplateB);
            playerInventory.setLeggings(leggingsB);
            playerInventory.setBoots(bootsB);
            playerInventory.setItem(0, swordB);
            playerInventory.setItem(1, rodB);
            playerInventory.setItem(2, bowB);
            playerInventory.setItem(3, gAppleB);
            playerInventory.setItem(4, gHead);
            playerInventory.setItem(7, arrowB);
            playerInventory.setItem(8, blocks);
        }
    }

    public void arenaEditor(Player p)
    {
        p.openInventory(arenaEditor);
    }

    private Inventory kitSelector = Bukkit.createInventory(null, 9, "§e§lKIT SELECTOR");

    public void kitSelector(Player p)
    {
        p.openInventory(kitSelector);
    }
}
