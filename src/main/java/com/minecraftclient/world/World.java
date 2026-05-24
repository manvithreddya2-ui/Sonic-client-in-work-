package com.minecraftclient.world;

import com.minecraftclient.render.Renderer;

public class World {
    private Chunk[][] chunks;
    private static final int RENDER_DISTANCE = 5;
    
    public World() {
        chunks = new Chunk[RENDER_DISTANCE * 2 + 1][RENDER_DISTANCE * 2 + 1];
        generateChunks();
    }
    
    private void generateChunks() {
        for (int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for (int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {
                int index = x + RENDER_DISTANCE;
                int zIndex = z + RENDER_DISTANCE;
                chunks[index][zIndex] = new Chunk(x, z);
                chunks[index][zIndex].generate();
            }
        }
    }
    
    public void update(double deltaTime) {
        // Update world state
    }
    
    public void render(Renderer renderer) {
        for (int x = 0; x < chunks.length; x++) {
            for (int z = 0; z < chunks[x].length; z++) {
                if (chunks[x][z] != null) {
                    chunks[x][z].render(renderer);
                }
            }
        }
    }
}