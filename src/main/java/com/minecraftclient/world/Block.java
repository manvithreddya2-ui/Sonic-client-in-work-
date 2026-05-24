package com.minecraftclient.world;

public class Block {
    public enum Type {
        AIR,
        STONE,
        DIRT,
        GRASS,
        COBBLESTONE,
        SAND,
        GRAVEL,
        OAK_LOG,
        OAK_LEAVES
    }
    
    private Type type;
    private boolean solid;
    
    public Block(Type type) {
        this.type = type;
        this.solid = type != Type.AIR;
    }
    
    public Type getType() {
        return type;
    }
    
    public boolean isSolid() {
        return solid;
    }
}