package me.bladian.uhc.world.custom;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class OrePopulator extends BlockPopulator
{
    @Override
    public void populate(World world, Random random, Chunk chunk)
    {
        int highest = 100;
        int lowest = 30;
        int x = chunk.getX() * 16 + random.nextInt(16);
        int y = lowest + random.nextInt(highest - lowest);
        int z = chunk.getZ() * 16 + random.nextInt(16);
        Bukkit.broadcastMessage(x + " " + y + " " + z);
        populateOre(world, random, x, y, z, 50, Material.DIAMOND_ORE);
    }

    private void populateOre(World world, Random rand, int x, int y, int z, int size, Material material) {
        double rpi = rand.nextDouble() * 3.1415926535897D;

        double x1 = x + 8 + Math.sin(rpi) * size / 8.0D;
        double x2 = x + 8 - Math.sin(rpi) * size / 8.0D;
        double z1 = z + 8 + Math.cos(rpi) * size / 8.0D;
        double z2 = z + 8 - Math.cos(rpi) * size / 8.0D;

        double y1 = y + rand.nextInt(3) + 2;
        double y2 = y + rand.nextInt(3) + 2;
        for (int i = 0; i <= size; i++) {
            double xPos = x1 + (x2 - x1) * i / size;
            double yPos = y1 + (y2 - y1) * i / size;
            double zPos = z1 + (z2 - z1) * i / size;

            double work = rand.nextDouble() * size / 16.0D;
            double workXZ = (Math.sin((float) (i * 3.1415926535897D / size)) + 1.0D) * work + 1.0D;
            double workY = (Math.sin((float) (i * 3.1415926535897D / size)) + 1.0D) * work + 1.0D;

            int xBegin = (int) Math.floor(xPos - workXZ / 2.0D);
            int yBegin = (int) Math.floor(yPos - workY / 2.0D);
            int zBegin = (int) Math.floor(zPos - workXZ / 2.0D);

            int xFinish = (int) Math.floor(xPos + workXZ / 2.0D);
            int yFinish = (int) Math.floor(yPos + workY / 2.0D);
            int zFinish = (int) Math.floor(zPos + workXZ / 2.0D);
            for (int ix = xBegin; ix <= xFinish; ix++) {
                double xMore = (ix + 0.5D - xPos) / (workXZ / 2.0D);
                if (xMore * xMore < 1.0D) {
                    for (int iy = yBegin; iy <= yFinish; iy++) {
                        double yMore = (iy + 0.5D - yPos) / (workY / 2.0D);
                        if (xMore * xMore + yMore * yMore < 1.0D) {
                            for (int iz = zBegin; iz <= zFinish; iz++) {
                                double zMore = (iz + 0.5D - zPos) / (workXZ / 2.0D);
                                if (xMore * xMore + yMore * yMore + zMore * zMore < 1.0D) {
                                    Block block = getBlock(world, ix, iy, iz);
                                    if ((block != null) && block.getType() == Material.STONE) {
                                        block.setType(material);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Block getBlock(World world, int x, int y, int z)
    {
        int cx = x >> 4;
        int cz = z >> 4;
        if ((!world.isChunkLoaded(cx, cz)) &&
                (!world.loadChunk(cx, cz, false))) {
            return null;
        }
        Chunk chunk = world.getChunkAt(cx, cz);
        if (chunk == null) {
            return null;
        }
        return chunk.getBlock(x & 0xF, y, z & 0xF);
    }
}
