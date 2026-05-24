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
        
        if (rotation.x > 89) rotation.x = 89;
        if (rotation.x < -89) rotation.x = -89;
    }
    
    public Vector3f getPosition() {
        return new Vector3f(position);
    }
    
    public void setPosition(Vector3f pos) {
        this.position = new Vector3f(pos);
    }
    
    public Vector3f getRotation() {
        return new Vector3f(rotation);
    }
    
    public Matrix4f getViewMatrix() {
        return new Matrix4f(viewMatrix);
    }
    
    public Vector3f getForward() {
        Vector3f forward = new Vector3f(0, 0, -1);
        forward.rotateX((float) Math.toRadians(rotation.x));
        forward.rotateY((float) Math.toRadians(rotation.y));
        return forward;
    }
    
    public Vector3f getRight() {
        Vector3f right = new Vector3f(1, 0, 0);
        right.rotateY((float) Math.toRadians(rotation.y));
        return right;
    }
}