package me.bladian.uhc.world.generator;

import net.minecraft.server.v1_7_R4.BiomeBase;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 *
 */


public class BiomeHandler
{
    private BiomeBase[] origBiomes;

    public BiomeHandler() {
        this.origBiomes = getMcBiomesCopy();
    }

    public void swapBiome(BiomeSwap.Biome oldBiome, BiomeSwap.Biome newBiome) {
        if (oldBiome.getId() != BiomeSwap.Biome.SKY.getId()) {
            BiomeBase[] biomes = getMcBiomes();
            biomes[oldBiome.getId()] = getOrigBiome(newBiome.getId());
        } else {
            Bukkit.getLogger().warning("Cannot swap SKY biome!");
        }
    }

    private BiomeBase[] getMcBiomesCopy() {
        BiomeBase[] b = getMcBiomes();
        return (BiomeBase[]) Arrays.copyOf(b, b.length);
    }

    private BiomeBase[] getMcBiomes() {
        try {
            Field biomeF = BiomeBase.class.getDeclaredField("biomes");
            biomeF.setAccessible(true);
            return (BiomeBase[]) biomeF.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return new BiomeBase[256];
    }

    private BiomeBase getOrigBiome(int value) {
        return this.origBiomes[value];
    }
}
