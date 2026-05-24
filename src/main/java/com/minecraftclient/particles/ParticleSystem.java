package com.minecraftclient.particles;

import com.minecraftclient.render.Renderer;
import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystem {
    private List<Particle> particles;
    private Random random;
    
    public ParticleSystem() {
        this.particles = new ArrayList<>();
        this.random = new Random();
    }
    
    public void emit(Vector3f position, Vector3f velocity, int count) {
        for (int i = 0; i < count; i++) {
            float vx = velocity.x + (random.nextFloat() - 0.5f) * 2.0f;
            float vy = velocity.y + (random.nextFloat() - 0.5f) * 2.0f;
            float vz = velocity.z + (random.nextFloat() - 0.5f) * 2.0f;
            
            particles.add(new Particle(
                new Vector3f(position),
                new Vector3f(vx, vy, vz),
                1.0f,
                1.0f
            ));
        }
    }
    
    public void update(double deltaTime) {
        particles.removeIf(p -> !p.isAlive());
        
        for (Particle p : particles) {
            p.update(deltaTime);
        }
    }
    
    public void render(Renderer renderer) {
        for (Particle p : particles) {
            p.render(renderer);
        }
    }
    
    public static class Particle {
        private Vector3f position;
        private Vector3f velocity;
        private float lifetime;
        private float maxLifetime;
        private float size;
        
        public Particle(Vector3f position, Vector3f velocity, float lifetime, float size) {
            this.position = position;
            this.velocity = velocity;
            this.lifetime = lifetime;
            this.maxLifetime = lifetime;
            this.size = size;
        }
        
        public void update(double deltaTime) {
            float dt = (float) deltaTime;
            position.add(velocity.x * dt, velocity.y * dt, velocity.z * dt);
            velocity.y -= 9.8f * dt;
            lifetime -= dt;
        }
        
        public void render(Renderer renderer) {
            // Particle rendering (simplified)
        }
        
        public boolean isAlive() {
            return lifetime > 0;
        }
    }
}