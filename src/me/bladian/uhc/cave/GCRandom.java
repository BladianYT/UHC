package me.bladian.uhc.cave;

import me.bladian.uhc.Core;
import me.bladian.uhc.util.Reference;
import org.bukkit.Chunk;
import org.bukkit.util.noise.NoiseGenerator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

/**
 * Taken from the GiantCaves plugin
 */


public class GCRandom
{

    private Core core = Core.instance;
    private Reference reference = core.getReference();
    public Chunk chunk;
    private final double f1xz;
    private final double f1y;
    private final int amplitude1 = 100;
    private final double subtractForLessThanCutoff;
    private final double f2xz = 0.25D;
    private final double f2y = 0.05D;
    private final int amplitude2 = 2;
    private final double f3xz = 0.025D;
    private final double f3y = 0.005D;
    private final int amplitude3 = 20;
    private final int caveBandBuffer;
    private final NoiseGenerator noiseGen1;
    private final NoiseGenerator noiseGen2;
    private final NoiseGenerator noiseGen3;

    public GCRandom(Chunk chunk)
    {
        this.chunk = chunk;
        this.subtractForLessThanCutoff = (100 - reference.CAVE_CUTOFF);
        this.f1xz = (1.0D / reference.CAVE_H_STRETCH);
        this.f1y = (1.0D / reference.CAVE_V_STRETCH);
        if (reference.CAVE_MAX_Y - reference.CAVE_MIN_Y > 128) {
            this.caveBandBuffer = 32;
        } else {
            this.caveBandBuffer = 16;
        }
        this.noiseGen1 = new SimplexNoiseGenerator(chunk.getWorld());
        this.noiseGen2 = new SimplexNoiseGenerator((long) this.noiseGen1.noise(chunk.getX(), chunk.getZ()));
        this.noiseGen3 = new SimplexNoiseGenerator((long) this.noiseGen1.noise(chunk.getX(), chunk.getZ()));
    }

    public boolean isInGiantCave(int x, int y, int z)
    {
        double xx = this.chunk.getX() << 4 | x & 0xF;
        double yy = y;
        double zz = this.chunk.getZ() << 4 | z & 0xF;

        double n1 = this.noiseGen1.noise(xx * this.f1xz, yy * this.f1y, zz * this.f1xz) * 100.0D;
        double n2 = this.noiseGen2.noise(xx * 0.25D, yy * 0.05D, zz * 0.25D) * 2.0D;
        double n3 = this.noiseGen3.noise(xx * 0.025D, yy * 0.005D, zz * 0.025D) * 20.0D;
        double lc = linearCutoffCoefficient(y);

        boolean isInCave = n1 + n2 - n3 - lc > reference.CAVE_CUTOFF;
        return isInCave;
    }

    private double linearCutoffCoefficient(int y)
    {
        if ((y < reference.CAVE_MIN_Y) || (y > reference.CAVE_MAX_Y)) {
            return this.subtractForLessThanCutoff;
        }
        if ((y >= reference.CAVE_MIN_Y) && (y <= reference.CAVE_MIN_Y+ this.caveBandBuffer))
        {
            double yy = y - reference.CAVE_MIN_Y;
            return -this.subtractForLessThanCutoff / this.caveBandBuffer * yy + this.subtractForLessThanCutoff;
        }
        if ((y <= reference.CAVE_MAX_Y) && (y >= reference.CAVE_MAX_Y - this.caveBandBuffer))
        {
            double yy = y - reference.CAVE_MAX_Y + this.caveBandBuffer;
            return this.subtractForLessThanCutoff / this.caveBandBuffer * yy;
        }
        return 0.0D;
    }
}