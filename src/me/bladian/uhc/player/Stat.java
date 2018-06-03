package me.bladian.uhc.player;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by BladianYT
 */
public class Stat
{

    private Material material;
    private String name;
    private int amount = 0;

    public Stat(Material material, String name)
    {
        this.material = material;
        this.name = "§e§l" + name.toUpperCase() + ":§f§l ";
    }

    public int getAmount()
    {
        return amount;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }

    public void increaseAmount(int amount)
    {
        this.amount = this.amount+amount;
    }

    public ItemStack getItemStack()
    {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(name.contains(""))
        itemMeta.setDisplayName(name + amount);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
