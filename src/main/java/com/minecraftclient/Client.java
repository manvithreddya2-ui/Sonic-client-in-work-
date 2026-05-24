package com.minecraftclient;

import com.minecraftclient.input.InputHandler;
import com.minecraftclient.render.Camera;
import com.minecraftclient.render.Renderer;
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
    private static final String TITLE = "Minecraft Client";
    
    private long windowHandle;
    private Renderer renderer;
    private Camera camera;
    private World world;
    private InputHandler inputHandler;
    private boolean running = true;
    
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
        logger.info("Initializing Minecraft Client...");
        
        // Set error callback
        GLFWErrorCallback.createPrint(System.err).set();
        
        // Initialize GLFW
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        
        // Create window
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        
        windowHandle = GLFW.glfwCreateWindow(WIDTH, HEIGHT, TITLE, 0, 0);
        if (windowHandle == 0) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        // Center window
        GLFW.glfwSetWindowPos(windowHandle, 100, 100);
        
        // Make context current
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1); // V-sync
        
        // Show window
        GLFW.glfwShowWindow(windowHandle);
        
        // Create GL capabilities
        GL.createCapabilities();
        
        // Setup callbacks
        GLFW.glfwSetWindowCloseCallback(windowHandle, window -> running = false);
        GLFW.glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                running = false;
            }
        });
        
        // Initialize components
        renderer = new Renderer(WIDTH, HEIGHT);
        camera = new Camera();
        world = new World();
        inputHandler = new InputHandler(windowHandle, camera);
        
        // OpenGL settings
        GL11.glClearColor(0.529f, 0.808f, 0.922f, 1.0f); // Minecraft sky blue
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        
        logger.info("Initialization complete");
    }
    
    private void loop() {
        logger.info("Starting game loop");
        
        long lastTime = System.nanoTime();
        double deltaTime;
        
        while (running && !GLFW.glfwWindowShouldClose(windowHandle)) {
            long currentTime = System.nanoTime();
            deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;
            
            // Update
            update(deltaTime);
            
            // Render
            render();
            
            // Swap buffers and poll events
            GLFW.glfwSwapBuffers(windowHandle);
            GLFW.glfwPollEvents();
        }
    }
    
    private void update(double deltaTime) {
        inputHandler.update(deltaTime);
        camera.update();
        world.update(deltaTime);
    }
    
    private void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        renderer.beginFrame(camera);
        world.render(renderer);
        renderer.endFrame();
    }
    
    private void cleanup() {
        logger.info("Cleaning up...");
        
        if (renderer != null) {
            renderer.cleanup();
        }
        
        GLFW.glfwDestroyWindow(windowHandle);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
        
        logger.info("Goodbye!");
    }
}