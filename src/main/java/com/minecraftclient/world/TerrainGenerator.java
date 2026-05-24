package com.minecraftclient.world;

import java.util.Random;

public class TerrainGenerator {
    private Random random;
    private int seed;
    
    public TerrainGenerator(int seed) {
        this.seed = seed;
        this.random = new Random(seed);
    }
    
    public int getHeight(int x, int z) {
        // Simple perlin-like noise implementation
        float baseHeight = 60;
        float noise = getNoise(x * 0.05f, z * 0.05f);
        return (int) (baseHeight + noise * 20);
    }
    
    public Block.Type getBlockType(int x, int y, int z) {
        int height = getHeight(x, z);
        
        if (y > height) {
            return Block.Type.AIR;
        } else if (y == height) {
            return Block.Type.GRASS;
        } else if (y > height - 3) {
            return Block.Type.DIRT;
        } else if (y > 40) {
            return Block.Type.STONE;
        } else if (y > 10) {
            // Random ore distribution
            if (random.nextFloat() < 0.02f) {
                return random.nextBoolean() ? Block.Type.COAL_ORE : Block.Type.IRON_ORE;
            }
            return Block.Type.STONE;
        } else {
            return Block.Type.BEDROCK;
        }
    }
    
    private float getNoise(float x, float z) {
        Random r = new Random((long)(x * 73856093 ^ z * 19349663));
        return r.nextFloat() * 2.0f - 1.0f;
    }
}