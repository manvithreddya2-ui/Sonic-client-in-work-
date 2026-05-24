package com.minecraftclient;

import com.minecraftclient.input.InputHandler;
import com.minecraftclient.particles.ParticleSystem;
import com.minecraftclient.physics.PhysicsEngine;
import com.minecraftclient.render.Camera;
import com.minecraftclient.render.Renderer;
import com.minecraftclient.render.TextureManager;
import com.minecraftclient.ui.HUD;
import com.minecraftclient.world.World;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final String TITLE = "⚡ Sonic Client";
    
    private long windowHandle;
    private Renderer renderer;
    private Camera camera;
    private World world;
    private InputHandler inputHandler;
    private PhysicsEngine physics;
    private ParticleSystem particles;
    private HUD hud;
    private TextureManager textureManager;
    
    private boolean running = true;
    private boolean showDebug = false;
    private boolean particlesEnabled = true;
    private double fpsTimer = 0;
    private int frameCount = 0;
    private int fps = 0;
    
    public static void main(String[] args) {
        new Client().run();
    }
    
    public void run() {
        try {
            init();
            loop();
            cleanup();
        } catch (Exception e) {
            logger.error("Fatal error", e);
            System.exit(1);
        }
    }
    
    private void init() {
        logger.info("🚀 Initializing Sonic Client...");
        
        GLFWErrorCallback.createPrint(System.err).set();
        
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
        
        windowHandle = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        
        GLFW.glfwSetWindowPos(windowHandle, 100, 100);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowHandle);
        
        GL.createCapabilities();
        GL11.glClearColor(0.529f, 0.808f, 0.922f, 1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_MULTISAMPLE);
        
        setupCallbacks();
        
        textureManager = new TextureManager();
        renderer = new Renderer(WIDTH, HEIGHT, textureManager);
        camera = new Camera();
        world = new World();
        inputHandler = new InputHandler(windowHandle, camera);
        physics = new PhysicsEngine(camera, world);
        particles = new ParticleSystem();
        hud = new HUD(WIDTH, HEIGHT);
        
        logger.info("✅ Initialization complete");
    }
    
    private void setupCallbacks() {
        GLFW.glfwSetWindowCloseCallback(windowHandle, window -> running = false);
        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_RELEASE) {
                switch (key) {
                    case GLFW.GLFW_KEY_ESCAPE:
                        running = false;
                        break;
                    case GLFW.GLFW_KEY_F3:
                        showDebug = !showDebug;
                        break;
                    case GLFW.GLFW_KEY_F5:
                        particlesEnabled = !particlesEnabled;
                        break;
                }
            }
        });
    }
    
    private void loop() {
        logger.info("🎮 Starting game loop");
        
        long lastTime = System.nanoTime();
        
        while (running && !GLFW.glfwWindowShouldClose(windowHandle)) {
            long currentTime = System.nanoTime();
            double deltaTime = Math.min((currentTime - lastTime) / 1_000_000_000.0, 0.016);
            lastTime = currentTime;
            
            update(deltaTime);
            render();
            
            GLFW.glfwSwapBuffers(windowHandle);
            GLFW.glfwPollEvents();
            
            updateFPS(deltaTime);
        }
    }
    
    private void updateFPS(double deltaTime) {
        fpsTimer += deltaTime;
        frameCount++;
        if (fpsTimer >= 1.0) {
            fps = frameCount;
            frameCount = 0;
            fpsTimer = 0;
        }
    }
    
    private void update(double deltaTime) {
        inputHandler.update(deltaTime);
        physics.update(deltaTime);
        camera.update();
        world.update(deltaTime);
        
        if (particlesEnabled) {
            particles.update(deltaTime);
        }
    }
    
    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        renderer.beginFrame(camera);
        world.render(renderer);
        
        if (particlesEnabled) {
            particles.render(renderer);
        }
        
        renderer.endFrame();
        
        hud.render(showDebug, fps, (int) camera.getPosition().x, 
                   (int) camera.getPosition().y, (int) camera.getPosition().z,
                   physics.isGrounded());
    }
    
    private void cleanup() {
        logger.info("🧹 Cleaning up...");
        
        if (renderer != null) renderer.cleanup();
        if (textureManager != null) textureManager.cleanup();
        if (hud != null) hud.cleanup();
        
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
        
        logger.info("👋 Goodbye!");
    }
}