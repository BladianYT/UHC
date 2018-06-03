package me.bladian.uhc.util;

import net.minecraft.server.v1_7_R4.DedicatedServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.*;

/**
 * Created by BladianYT
 */
public class Util
{

    public String toPrefix(String string)
    {
        return string + " §8:|:";
    }

    public ItemStack createItem(Material material, String name)
    {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createItemWithData(Material material, String name, int data)
    {
        ItemStack item = new ItemStack(material, 1, (short) data);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }


    public boolean deleteFiles(File path)
    {
        if (path.exists())
        {
            File files[] = path.listFiles();
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    deleteFiles(file);
                }
                else
                {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    public boolean isNumeric(String string)
    {
        try
        {
            Double d = Double.parseDouble(string);
        }
        catch (NumberFormatException nfe)
        {
            double d;
            return false;
        }
        return true;
    }

    static Random random = new Random();

    public Location getLocation()
    {
        World uhc = Bukkit.getWorld("uhc");
        int x = random.nextInt(1900) - 950;
        int z = random.nextInt(1900) - 950;
        Location location = new Location(uhc, x, uhc.getHighestBlockYAt(x, z), z);
        if(location.add(0, -1, 0).getBlock().getType() == Material.WATER || location.add(0, -1, 0).getBlock().getType() == Material.LAVA
                || location.add(0, -1, 0).getBlock().getType() == Material.STATIONARY_WATER || location.add(0, -1, 0).getBlock().getType() == Material.STATIONARY_LAVA)
        {
            getLocation();
        }
        else
        {
            return location.add(0, 2, 0);
        }
        return null;
    }

    public  <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public List<Location> getSphere(Location centerBlock, int radius, boolean hollow) {

        List<Location> circleBlocks = new ArrayList<Location>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for(int x = bx - radius; x <= bx + radius; x++) {
            for(int y = by - radius; y <= by + radius; y++) {
                for(int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx-x) * (bx-x) + ((bz-z) * (bz-z)) + ((by-y) * (by-y)));

                    if(distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);

                        circleBlocks.add(l);

                    }

                }
            }
        }

        return circleBlocks;
    }

    public void setMOTD(String motd) {

        DedicatedServer server = (((CraftServer)Bukkit.getServer()).getHandle().getServer());

        server.setMotd(motd);
    }

    //13-9-14-5-13-5-24

    char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public String getGameState(String string)
    {
        String[] split = string.split("-");
        StringBuilder value = new StringBuilder();
        for(String strings : split)
        {
            int singleNumber = Integer.parseInt(strings);
            value.append(alphabet[singleNumber-1]);
        }
        return value.toString();
    }
}
