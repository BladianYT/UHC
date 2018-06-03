package me.bladian.uhc.manager;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Reference;
import me.bladian.uhc.util.Util;
import me.bladian.uhc.world.generator.BiomeSwap;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by BladianYT
 */
public class WorldManager
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    private Util util = core.getUtil();
    private GameManager gameManager = core.getGameManager();

    public void createWorld(String worldName)
    {
        /*BiomeSwap biomeSwap = new BiomeSwap();
        biomeSwap.startWorldGen();*/
        Bukkit.broadcastMessage(reference.GAME + "§fStarted creating the world§7 " + worldName);
        World world = Bukkit.createWorld(new WorldCreator(worldName).environment(World.Environment.NORMAL).type(WorldType.NORMAL).generator("TerrainControl"));
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("naturalRegeneration", "false");
        Bukkit.broadcastMessage(reference.GAME + "§fThe world has finished creating");
    }

    public void createNether(String worldName)
    {
        /*BiomeSwap biomeSwap = new BiomeSwap();
        biomeSwap.startWorldGen();*/
        Bukkit.broadcastMessage(reference.GAME + "§fStarted creating the world§7 " + worldName);
        World world = Bukkit.createWorld(new WorldCreator(worldName).environment(World.Environment.NETHER).type(WorldType.NORMAL));
        world.setDifficulty(Difficulty.PEACEFUL);
        world.setGameRuleValue("naturalRegeneration", "false");
        Bukkit.broadcastMessage(reference.GAME + "§fThe world has finished creating");
    }

    public void unloadWorld(World world)
    {
        if (world != null)
        {
            for (Player p : world.getPlayers())
            {
                p.teleport(reference.SPAWN);
            }
            Bukkit.broadcastMessage(reference.GAME + "§fThe world§7 " + world.getName() + "§f has begun§7 unloading");
            Bukkit.broadcastMessage(reference.GAME + "§fWill begin deleting in§7 30 seconds");
            Bukkit.getServer().unloadWorld(world, false);
        }
    }

    public void deleteWorld(String world)
    {
        File filePath = new File(Bukkit.getWorldContainer(), world);
        util.deleteFiles(filePath);
        Bukkit.broadcastMessage(reference.GAME + "§fThe world§7 " + world + "§f has begun§7 deleting");
        Bukkit.broadcastMessage(reference.GAME + "§fWill finish deleting in§7 30 seconds");
    }

    public void loadWorld(final World world, final int radius, final int speed)
    {
        final String name = world.getName();
        Bukkit.broadcastMessage(reference.GAME + "Started clearing the world§7 " + world.getName());
        prepareSpawn(world);
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                Bukkit.broadcastMessage(reference.GAME + "Started loading the world§7 " + world.getName());
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb shape square");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + name + " set " + radius + " " + radius + " 0 0");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + name + " fill " + speed);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "wb " + name + " fill confirm");
            }
        }.runTaskLater(core, 15 * 20L);
    }

    private void prepareSpawn(final World world)
    {
        final Queue<Location> locationQueue = new ArrayDeque<>();

        final Location max = new Location(world, 125, 160, 125);
        final Location min = new Location(world, -125, 45, -125);
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                {
                    final Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.VINE || block.getType() == Material.SNOW || block.getType() == Material.DOUBLE_PLANT
                            || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM)
                    {
                        locationQueue.add(block.getLocation());
                        if ((x == 125 && z == 125) || (x == -125 && z == 125) || (x == -125 || z == -125) || (x == 125 && z == -125))
                        {
                            //loop(block, locationQueue);
                            //Need to try and find a solution for looping
                        }
                    }
                }
            }
        }
        final int blocks = locationQueue.size();
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                for (int y = 0; y < 150; y++)
                {
                    if (!locationQueue.isEmpty())
                    {
                        Location location = locationQueue.poll();
                        location.getBlock().setType(Material.AIR);
                    }
                    else
                    {
                        this.cancel();
                        if (y == 149)
                        {
                            Bukkit.broadcastMessage(reference.GAME + "§fFinished clearing§7 " + world.getName() + "§f by changing§7 " + blocks + " blocks");
                            Bukkit.broadcastMessage(reference.GAME + "§fStarted patching§7 " + world.getName());
                            for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
                            {
                                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                                {
                                    Block bl = Bukkit.getWorld("uhc").getHighestBlockAt(x, z);
                                    Block blk = bl.getLocation().add(0, -1, 0).getBlock();
                                    if (blk.getType() == Material.DIRT)
                                    {
                                        locationQueue.add(blk.getLocation());
                                    }
                                }
                            }
                            final int blocks2 = locationQueue.size();
                            new BukkitRunnable()
                            {

                                @Override
                                public void run()
                                {
                                    for (int y = 0; y < 150; y++)
                                    {
                                        if (!locationQueue.isEmpty())
                                        {
                                            Location location = locationQueue.poll();
                                            location.getBlock().setType(Material.GRASS);
                                        }
                                        else
                                        {
                                            this.cancel();
                                            if (y == 149)
                                            {
                                                this.cancel();
                                                Bukkit.broadcastMessage(reference.GAME + "§fFinished patching§7 " + world.getName() + "§f by changing§7 " + blocks2 + " blocks");
                                            }
                                        }
                                    }
                                }
                            }.runTaskTimer(core, 0L, 1L);
                        }
                    }
                }
            }
        }.runTaskTimer(core, 0L, 1L);
    }

    public void prepareSpawn()
    {
        final int[] i = {0};
        final Queue<Location> locationQueue = new ArrayDeque<>();
        final Queue<Material> materialQueue = new ArrayDeque<>();
        final World world = Bukkit.getWorld("uhc");
        final Location max = new Location(world, 125, 160, 125);
        final Location min = new Location(world, -125, 50, -125);
        for (int x = min.getBlockX(); x <= max.getBlockX(); x++)
        {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y++)
            {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++)
                {
                    final Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    if (block.getType() == Material.GRASS)
                    {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.STAINED_CLAY);
                    }
                    else if (block.getType() == Material.LOG || block.getType() == Material.LOG_2 || block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2
                            || block.getType() == Material.VINE || block.getType() == Material.SNOW || block.getType() == Material.DOUBLE_PLANT
                            || block.getType() == Material.YELLOW_FLOWER || block.getType() == Material.RED_MUSHROOM || block.getType() == Material.BROWN_MUSHROOM)
                    {
                        locationQueue.add(block.getLocation());
                        materialQueue.add(Material.AIR);
                    }
                }
            }
        }
        final int blocks = locationQueue.size();
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                for (int x = 0; x < 150; x++)
                {
                    if (!locationQueue.isEmpty())
                    {
                        Location location = locationQueue.poll();
                        Material material = materialQueue.poll();
                        if (material == Material.STAINED_CLAY)
                        {
                            int rand = (int) ((Math.random() * 2) + 1);
                            int data;
                            if (rand == 1)
                            {
                                data = 1;
                            }
                            else
                            {
                                data = 4;
                            }
                            location.getBlock().setType(Material.STAINED_CLAY);
                            location.getBlock().setData((byte) data);
                        }
                        else
                        {
                            location.getBlock().setType(Material.AIR);
                        }
                    }
                    else
                    {
                        this.cancel();
                        if (x == 149)
                        {
                            Bukkit.broadcastMessage(reference.GAME + "§fFinished clearing§7 " + world.getName() + "§f by changing§7 " + blocks + " blocks");
                        }
                    }
                }
            }
        }.runTaskTimer(core, 0L, 1L);
    }

    private void loop(Block block1, Queue<Location> locations)
    {
        for (BlockFace blockface : BlockFace.values())
        {
            if (block1.getRelative(blockface).getType().equals(Material.LOG) || block1.getRelative(blockface).getType().equals(Material.LOG_2)
                    || block1.getRelative(blockface).getType() == Material.LEAVES || block1.getRelative(blockface).getType() == Material.LEAVES_2)
            {
                Block block = block1.getRelative(blockface);
                locations.add(block.getLocation());
                loop(block, locations);
            }
        }
    }
}
