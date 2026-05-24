package com.minecraftclient.physics;

import com.minecraftclient.render.Camera;
import com.minecraftclient.world.World;
import org.joml.Vector3f;

public class PhysicsEngine {
    private Camera camera;
    private World world;
    private Vector3f velocity;
    private float jumpForce = 15.0f;
    private float gravity = 32.0f;
    private float friction = 0.9f;
    private float acceleration = 50.0f;
    private boolean grounded = false;
    private float playerHeight = 1.7f;
    private float playerRadius = 0.3f;
    
    public PhysicsEngine(Camera camera, World world) {
        this.camera = camera;
        this.world = world;
        this.velocity = new Vector3f(0, 0, 0);
    }
    
    public void update(double deltaTime) {
        float dt = (float) deltaTime;
        
        // Apply gravity
        velocity.y -= gravity * dt;
        
        // Clamp fall speed
        if (velocity.y < -60) {
            velocity.y = -60;
        }
        
        // Check ground collision
        Vector3f pos = camera.getPosition();
        grounded = isGrounded(pos);
        
        if (grounded && velocity.y < 0) {
            velocity.y = 0;
        }
        
        // Apply friction
        velocity.x *= friction;
        velocity.z *= friction;
        
        // Apply velocity
        Vector3f newPos = new Vector3f(pos).add(
            velocity.x * dt,
            velocity.y * dt,
            velocity.z * dt
        );
        
        // Simple collision detection
        if (!isColliding(newPos)) {
            camera.setPosition(newPos);
        } else {
            // Slide along obstacles
            if (!isColliding(new Vector3f(newPos.x, pos.y, pos.z))) {
                camera.setPosition(new Vector3f(newPos.x, pos.y, pos.z));
            } else if (!isColliding(new Vector3f(pos.x, newPos.y, pos.z))) {
                camera.setPosition(new Vector3f(pos.x, newPos.y, pos.z));
            } else if (!isColliding(new Vector3f(pos.x, pos.y, newPos.z))) {
                camera.setPosition(new Vector3f(pos.x, pos.y, newPos.z));
            }
            velocity.y = 0;
        }
    }
    
    public void addVelocity(Vector3f vel) {
        velocity.add(vel);
    }
    
    public void jump() {
        if (grounded) {
            velocity.y = jumpForce;
            grounded = false;
        }
    }
    
    public void sprint(boolean sprinting) {
        acceleration = sprinting ? 100.0f : 50.0f;
    }
    
    private boolean isGrounded(Vector3f pos) {
        Vector3f checkPos = new Vector3f(pos.x, pos.y - 0.1f, pos.z);
        return !isColliding(checkPos);
    }
    
    private boolean isColliding(Vector3f pos) {
        // Simple sphere collision
        for (float dx = -playerRadius; dx <= playerRadius; dx += 0.2f) {
            for (float dy = -playerHeight; dy <= 0; dy += 0.2f) {
                for (float dz = -playerRadius; dz <= playerRadius; dz += 0.2f) {
                    Vector3f checkPos = new Vector3f(pos.x + dx, pos.y + dy, pos.z + dz);
                    if (world.isBlockSolid(checkPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isGrounded() {
        return grounded;
    }
    
    public Vector3f getVelocity() {
        return new Vector3f(velocity);
    }
}