package me.bladian.uhc.util;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * Taken from's GregZ_ FFA code, all credit goes to him and him only.
 */
public class ItemBuilder
{
    /**
     * The ItemStack that is being built.
     */
    private ItemStack item;
    /**
     * Create a new item builder based on the given material type and a stack
     * size of one.
     *
     * @param material
     *            The material type for this item builder to use.
     */
    public ItemBuilder(Material material) {
        this(material, 1);
    }
    /**
     * Create a new item builder based on the given ItemStack.
     *
     * @param item
     *            The initial ItemStack to build from.
     */
    public ItemBuilder(ItemStack item) {
        this.item = item;
    }
    /**
     * Create a new item builder based on the given material type and stack
     * size.
     *
     * @param material
     *            The material type for this item builder to use.
     * @param amount
     *            The initial stack size for this item builder to use.
     */
    public ItemBuilder(Material material, int amount) {
        item = new ItemStack(material, amount);
    }
    /**
     * Set the durability of the item related to this builder.
     *
     * @param dur
     *            The new durability of the item.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setDurability(short dur) {
        item.setDurability(dur);
        return this;
    }
    /**
     * Set the name of the item related to this builder
     *
     * @param name
     *            The new name for the item.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setName(String name) {
        if (name == null) {
            return this;
        }
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(name);
        item.setItemMeta(im);
        return this;
    }
    /**
     * Add an enchantment to this item, ignoring level restrictions.
     *
     * @param ench
     *            The enchantment type to add.
     * @param level
     *            The level of the enchantment to add.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        item.addUnsafeEnchantment(ench, level);
        return this;
    }
    /**
     * Remove a given enchantment from this item.
     *
     * @param ench
     *            The enchantment to remove.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder removeEnchantment(Enchantment ench) {
        item.removeEnchantment(ench);
        return this;
    }
    /**
     * Set the owner of this skull item, provided that this is a skull item.
     *
     * @param owner
     *            The name of the skull owner.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setSkullOwner(String owner) {
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof SkullMeta)) {
            return this;
        }
        SkullMeta im = (SkullMeta) meta;
        item.setDurability((short) 3);
        im.setOwner(owner);
        item.setItemMeta(im);
        return this;
    }
    /**
     * Add an enchantment to the item, uses enchantment level limits.
     *
     * @param enchantment
     *            The enchantment type to add.
     * @param level
     *            The level of the enchantment to add.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta im = item.getItemMeta();
        im.addEnchant(enchantment, level, true);
        item.setItemMeta(im);
        return this;
    }
    /**
     * Add a map of enchantment's to this item, uses enchantment level limits.
     *
     * @param enchantments
     *            The map of enchantment's to add to the item.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder addEnchants(HashMap<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> enchant : enchantments.entrySet()) {
            addEnchant(enchant.getKey(), enchant.getValue());
        }
        return this;
    }
    /**
     * Add an enchantment to this items book meta, provided that this item is an
     * enchanted book, ignores enchantment level restrictions.
     *
     * @param enchantment
     *            The enchantment to add to the book.
     * @param level
     *            The level of enchantment to add.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder addBookEnchantment(Enchantment enchantment, int level) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) meta;
            bookMeta.addStoredEnchant(enchantment, level, true);
            item.setItemMeta(bookMeta);
        }
        return this;
    }
    /**
     * Set the lore of the item.
     *
     * @param lore
     *            The lore to set on the item.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setLore(String... lore) {
        if (lore == null) {
            return this;
        }
        ItemMeta im = item.getItemMeta();
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return this;
    }
    /**
     * Add lore to any lore that the item already contains.
     *
     * @param lore
     *            The additional lore strings to add.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder addLore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        List<String> current = meta.getLore();
        if (current == null) {
            current = new ArrayList<String>();
        }
        current.addAll(Arrays.asList(lore));
        meta.setLore(current);
        item.setItemMeta(meta);
        return this;
    }
    /**
     * Set the colour of the wool item, provided that this item is formed from
     * the material wool.
     *
     * @param colour
     *            The colour to set the wool to.
     * @return This item builder so that method calls can be chained.
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder setWoolColour(DyeColor colour) {
        if (!item.getType().equals(Material.WOOL)) {
            return this;
        }
        this.item.setDurability(colour.getData());
        return this;
    }
    /**
     * Set the colour of the leather armour, provided that this item is soem
     * kind of leather armour.
     *
     * @param colour
     *            The colour to set the armour as.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setLeatherArmorColor(Color colour) {
        ItemMeta meta = item.getItemMeta();
        if (!(meta instanceof LeatherArmorMeta)) {
            return this;
        }
        LeatherArmorMeta im = (LeatherArmorMeta) meta;
        im.setColor(colour);
        item.setItemMeta(im);
        return this;
    }
    /**
     * Set the amount of items in this stack.
     *
     * @param amount
     *            The amount of items to set in this stack.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }
    /**
     * Set if this item can be broken or not.
     *
     * @param unbreakable
     *            True if this item is unbreakable, otherwise false.
     * @return This item builder so that method calls can be chained.
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }
    /**
     * Get the constructed item from this ItemBuilder.
     *
     * @return The ItemStack that has been built.
     */
    public ItemStack toItemStack() {
        return item;
    }
    /**
     * Produce a new instance of ItemBuilder that is identical to this class.
     */
    @Override
    public ItemBuilder clone() {
        return new ItemBuilder(item);
    }
}
