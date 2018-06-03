package me.bladian.uhc.cave;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Reference;
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


public class GiantCave extends BlockPopulator
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();

    private final Material material = Material.AIR;

    public void populate(World world, Random random, Chunk source)
    {
        GCRandom gcRandom = new GCRandom(source);
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                for (int y = reference.CAVE_MAX_Y; y >= reference.CAVE_MIN_Y; y--)
                {
                    if (gcRandom.isInGiantCave(x, y, z))
                    {
                        Block block = source.getBlock(x, y, z);
                        block.setType(this.material);
                    }
                }
            }
        }
    }
}

