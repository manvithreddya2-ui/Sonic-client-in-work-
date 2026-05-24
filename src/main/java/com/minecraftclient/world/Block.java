package com.minecraftclient.world;

public class Block {
    public enum Type {
        AIR(false, "air"),
        STONE(true, "stone"),
        DIRT(true, "dirt"),
        GRASS(true, "grass"),
        COBBLESTONE(true, "cobblestone"),
        SAND(true, "sand"),
        GRAVEL(true, "gravel"),
        OAK_LOG(true, "oak_log"),
        OAK_LEAVES(true, "oak_leaves"),
        BEDROCK(true, "bedrock"),
        COAL_ORE(true, "coal_ore"),
        IRON_ORE(true, "iron_ore"),
        GOLD_ORE(true, "gold_ore"),
        DIAMOND_ORE(true, "diamond_ore"),
        WATER(false, "water"),
        LAVA(false, "lava");
        
        private boolean solid;
        private String textureName;
        
        Type(boolean solid, String textureName) {
            this.solid = solid;
            this.textureName = textureName;
        }
        
        public boolean isSolid() {
            return solid;
        }
        
        public String getTextureName() {
            return textureName;
        }
    }
    
    private Type type;
    
    public Block(Type type) {
        this.type = type;
    }
    
    public Type getType() {
        return type;
    }
    
    public boolean isSolid() {
        return type.isSolid();
    }
    
    public void setType(Type type) {
        this.type = type;
    }
}