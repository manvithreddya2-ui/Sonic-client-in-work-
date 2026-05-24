package com.minecraftclient.input;

import com.minecraftclient.render.Camera;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class InputHandler {
    private long windowHandle;
    private Camera camera;
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private boolean firstMouse = true;
    private float moveSpeed = 30.0f;
    private boolean sprinting = false;
    
    public InputHandler(long windowHandle, Camera camera) {
        this.windowHandle = windowHandle;
        this.camera = camera;
        
        GLFW.glfwSetCursorPosCallback(windowHandle, (window, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
                return;
            }
            
            double xoffset = xpos - lastMouseX;
            double yoffset = lastMouseY - ypos;
            lastMouseX = xpos;
            lastMouseY = ypos;
            
            camera.rotate((float) xoffset, (float) yoffset);
        });
    }
    
    public void update(double deltaTime) {
        float speed = moveSpeed * (float) deltaTime;
        
        sprinting = GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
        if (sprinting) speed *= 2.0f;
        
        Vector3f forward = camera.getForward();
        Vector3f right = camera.getRight();
        
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.move(forward, speed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.move(forward, -speed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.move(right, speed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.move(right, -speed);
        }
    }
    
    public boolean isKeyPressed(int key) {
        return GLFW.glfwGetKey(windowHandle, key) == GLFW.GLFW_PRESS;
    }
}