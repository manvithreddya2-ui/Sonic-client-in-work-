package com.minecraftclient.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private Vector3f position;
    private Vector3f rotation;
    private Matrix4f viewMatrix;
    
    public Camera() {
        this.position = new Vector3f(0, 70, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.viewMatrix = new Matrix4f();
    }
    
    public void update() {
        // Update view matrix based on position and rotation
        viewMatrix.identity()
                .rotateX((float) Math.toRadians(rotation.x))
                .rotateY((float) Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }
    
    public void move(Vector3f direction, float speed) {
        position.add(direction.mul(speed));
    }
    
    public void rotate(float dx, float dy) {
        rotation.x += dy * 0.1f;
        rotation.y += dx * 0.1f;
        
        // Clamp pitch
        if (rotation.x > 89) rotation.x = 89;
        if (rotation.x < -89) rotation.x = -89;
    }
    
    public Vector3f getPosition() {
        return new Vector3f(position);
    }
    
    public Vector3f getRotation() {
        return new Vector3f(rotation);
    }
    
    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }
}