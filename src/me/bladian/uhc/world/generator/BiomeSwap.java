package me.bladian.uhc.world.generator;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Bladian. Before using the code, kindly ask permission to him via the following methods.
 * <p>
 * Twitter: BladianMC
 * Discord: Bladian#6411
 * <p>
 * Thank you for reading!
 */


public class BiomeSwap
{
    private BiomeHandler swapper;

    public void startWorldGen() {
        try {
            Class<?> clazz = Class.forName("me.bladian.uhc.world.generator.BiomeHandler");
            swapper = ((BiomeHandler) clazz.getConstructor(new Class[0]).newInstance(new Object[0]));

        } catch(InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException
                | ClassNotFoundException e) {
            e.printStackTrace();
        }
        swapper.swapBiome(Biome.OCEAN, Biome.FOREST);
        swapper.swapBiome(Biome.DEEP_OCEAN, Biome.FOREST);
        swapper.swapBiome(Biome.EXTREME_HILLS, Biome.PLAINS);
        swapper.swapBiome(Biome.EXTREME_HILLS_PLUS, Biome.PLAINS);
        swapper.swapBiome(Biome.DESERT_HILLS, Biome.PLAINS);
        swapper.swapBiome(Biome.FROZEN_OCEAN, Biome.FOREST);
        swapper.swapBiome(Biome.ICE_PLAINS, Biome.PLAINS);
        swapper.swapBiome(Biome.TAIGA_HILLS, Biome.FOREST);
        swapper.swapBiome(Biome.SMALL_MOUNTAINS, Biome.PLAINS);
        swapper.swapBiome(Biome.JUNGLE, Biome.PLAINS);
        swapper.swapBiome(Biome.JUNGLE_HILLS, Biome.PLAINS);
        swapper.swapBiome(Biome.JUNGLE_EDGE, Biome.PLAINS);
        swapper.swapBiome(Biome.COLD_TAIGA, Biome.PLAINS);
        swapper.swapBiome(Biome.COLD_TAIGA_HILLS, Biome.PLAINS);
        swapper.swapBiome(Biome.ROOFED_FOREST, Biome.PLAINS);
        swapper.swapBiome(Biome.MEGA_TAIGA_HILLS, Biome.PLAINS);
        swapper.swapBiome(Biome.MEGA_TAIGA, Biome.PLAINS);
        swapper.swapBiome(Biome.SAVANNA, Biome.FOREST);
        swapper.swapBiome(Biome.SAVANNA_PLATEAU, Biome.FOREST);
        swapper.swapBiome(Biome.MESA, Biome.PLAINS);
        swapper.swapBiome(Biome.MESA_PLATEAU, Biome.PLAINS);
        swapper.swapBiome(Biome.MESA_PLATEAU_F, Biome.PLAINS);
        swapper.swapBiome(Biome.ICE_PLAINS_SPIKES, Biome.PLAINS);
        swapper.swapBiome(Biome.EXTREME_HILLS_M, Biome.PLAINS);
        swapper.swapBiome(Biome.EXTREME_HILLS_M_PLUS, Biome.PLAINS);
        swapper.swapBiome(Biome.MEGA_SPRUCE_TAIGA, Biome.FOREST);
        swapper.swapBiome(Biome.JUNGLE_M, Biome.FOREST);
        swapper.swapBiome(Biome.JUNGLE_EDGE_M, Biome.PLAINS);
        swapper.swapBiome(Biome.MESA_BRYCE, Biome.PLAINS);
        swapper.swapBiome(Biome.SAVANNA_M, Biome.PLAINS);
        swapper.swapBiome(Biome.MESA_PLATEAU_F_M, Biome.PLAINS);
        swapper.swapBiome(Biome.PLATEAU_M, Biome.PLAINS);
        swapper.swapBiome(Biome.PLATEAUM, Biome.PLAINS);
    }

    enum Biome {
        OCEAN(0), PLAINS(1), DESERT(2), EXTREME_HILLS(3), FOREST(4), TAIGA(5), SWAMPLAND(6),
        RIVER(7), HELL(8), SKY(9), FROZEN_OCEAN(10), FROZEN_RIVER(11), ICE_PLAINS(12),
        ICE_MOUNTAINS(13), MUSHROOM_ISLAND(14), MUSHROOM_SHORE(15), BEACH(16),
        DESERT_HILLS(17), FOREST_HILLS(18), TAIGA_HILLS(19), SMALL_MOUNTAINS(20),
        JUNGLE(21), JUNGLE_HILLS(22), JUNGLE_EDGE(23), DEEP_OCEAN(24),
        STONE_BEACH(25), COLD_BEACH(26), BIRCH_FOREST(27), BIRCH_FOREST_HILLS(28),
        ROOFED_FOREST(29), COLD_TAIGA(30), COLD_TAIGA_HILLS(31), MEGA_TAIGA(32),
        MEGA_TAIGA_HILLS(33), EXTREME_HILLS_PLUS(34), SAVANNA(35), SAVANNA_PLATEAU(36),
        MESA(37), MESA_PLATEAU_F(38), MESA_PLATEAU(39), ICE_PLAINS_SPIKES(140),
        EXTREME_HILLS_M(131), EXTREME_HILLS_M_PLUS(162), MEGA_SPRUCE_TAIGA(160),
        ROOFED_FOREST_M(157), SWAMPLAND_M(134), JUNGLE_M(149), JUNGLE_EDGE_M(151),
        MESA_BRYCE(165), SAVANNA_M(163), MESA_PLATEAU_F_M(166), PLATEAU_M(164), PLATEAUM(167);

        private final int id;
        Biome(int id){this.id = id;}

        public int getId(){return this.id;}
    }
}
