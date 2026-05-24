package com.minecraftclient.world;

import com.minecraftclient.render.Renderer;
import org.joml.Vector3f;
import java.util.HashMap;
import java.util.Map;

public class World {
    private Map<String, Chunk> chunks;
    private TerrainGenerator generator;
    private static final int RENDER_DISTANCE = 10;
    private int seed;
    
    public World() {
        this.seed = (int) System.currentTimeMillis();
        this.generator = new TerrainGenerator(seed);
        this.chunks = new HashMap<>();
        generateInitialChunks();
    }
    
    private void generateInitialChunks() {
        for (int x = -RENDER_DISTANCE; x <= RENDER_DISTANCE; x++) {
            for (int z = -RENDER_DISTANCE; z <= RENDER_DISTANCE; z++) {
                loadChunk(x, z);
            }
        }
    }
    
    private String getChunkKey(int x, int z) {
        return x + "," + z;
    }
    
    private void loadChunk(int x, int z) {
        String key = getChunkKey(x, z);
        if (!chunks.containsKey(key)) {
            chunks.put(key, new Chunk(x, z, generator));
        }
    }
    
    public void update(double deltaTime) {
        // Update world state (day/night cycle, etc)
    }
    
    public void render(Renderer renderer) {
        for (Chunk chunk : chunks.values()) {
            chunk.render(renderer);
        }
    }
    
    public boolean isBlockSolid(Vector3f position) {
        int x = (int) position.x;
        int y = (int) position.y;
        int z = (int) position.z;
        
        int chunkX = x / Chunk.CHUNK_SIZE;
        int chunkZ = z / Chunk.CHUNK_SIZE;
        int blockX = x % Chunk.CHUNK_SIZE;
        int blockZ = z % Chunk.CHUNK_SIZE;
        
        Chunk chunk = chunks.get(getChunkKey(chunkX, chunkZ));
        if (chunk == null) return false;
        
        Block block = chunk.getBlock(blockX, y, blockZ);
        return block != null && block.isSolid();
    }
    
    public void breakBlock(Vector3f position) {
        int x = (int) position.x;
        int y = (int) position.y;
        int z = (int) position.z;
        
        int chunkX = x / Chunk.CHUNK_SIZE;
        int chunkZ = z / Chunk.CHUNK_SIZE;
        int blockX = x % Chunk.CHUNK_SIZE;
        int blockZ = z % Chunk.CHUNK_SIZE;
        
        Chunk chunk = chunks.get(getChunkKey(chunkX, chunkZ));
        if (chunk != null) {
            chunk.setBlock(blockX, y, blockZ, new Block(Block.Type.AIR));
        }
    }
    
    public void placeBlock(Vector3f position, Block.Type type) {
        int x = (int) position.x;
        int y = (int) position.y;
        int z = (int) position.z;
        
        int chunkX = x / Chunk.CHUNK_SIZE;
        int chunkZ = z / Chunk.CHUNK_SIZE;
        int blockX = x % Chunk.CHUNK_SIZE;
        int blockZ = z % Chunk.CHUNK_SIZE;
        
        Chunk chunk = chunks.get(getChunkKey(chunkX, chunkZ));
        if (chunk != null) {
            chunk.setBlock(blockX, y, blockZ, new Block(type));
        }
    }
    
    public Chunk getChunk(int x, int z) {
        return chunks.get(getChunkKey(x, z));
    }
}