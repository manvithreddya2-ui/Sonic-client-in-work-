package com.minecraftclient.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class Renderer {
    private int width;
    private int height;
    private Matrix4f projectionMatrix;
    
    public Renderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.projectionMatrix = new Matrix4f();
        setupProjection();
    }
    
    private void setupProjection() {
        float fov = (float) Math.toRadians(70.0f);
        float aspect = (float) width / height;
        projectionMatrix.perspective(fov, aspect, 0.1f, 1000.0f);
    }
    
    public void beginFrame(Camera camera) {
        GL11.glViewport(0, 0, width, height);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
    
    public void endFrame() {
        // Frame rendering complete
    }
    
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f(projectionMatrix);
    }
    
    public void cleanup() {
        // Cleanup renderer resources
    }
}