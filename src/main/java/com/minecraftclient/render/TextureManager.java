package com.minecraftclient.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.stb.STBImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.system.MemoryStack.stackPush;

public class TextureManager {
    private static final Logger logger = LoggerFactory.getLogger(TextureManager.class);
    private Map<String, Integer> textures = new HashMap<>();
    private int defaultTexture;
    
    public TextureManager() {
        defaultTexture = createPlainColorTexture(255, 100, 100);
    }
    
    public int createPlainColorTexture(int r, int g, int b) {
        int textureID = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
        
        byte[] data = new byte[4];
        data[0] = (byte) r;
        data[1] = (byte) g;
        data[2] = (byte) b;
        data[3] = (byte) 255;
        
        ByteBuffer buffer = org.lwjgl.system.MemoryUtil.memAlloc(4);
        buffer.put(data).flip();
        
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 1, 1, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        org.lwjgl.system.MemoryUtil.memFree(buffer);
        
        return textureID;
    }
    
    public int getTexture(String name) {
        return textures.getOrDefault(name, defaultTexture);
    }
    
    public void bindTexture(String name) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTexture(name));
    }
    
    public void cleanup() {
        for (int textureID : textures.values()) {
            GL11.glDeleteTextures(textureID);
        }
        GL11.glDeleteTextures(defaultTexture);
    }
}