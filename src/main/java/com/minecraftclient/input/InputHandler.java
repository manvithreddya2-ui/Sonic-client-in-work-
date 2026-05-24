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
        float moveSpeed = 30.0f * (float) deltaTime;
        
        Vector3f forward = new Vector3f(0, 0, -1);
        Vector3f right = new Vector3f(1, 0, 0);
        
        // Rotate vectors based on camera rotation
        forward.rotateY((float) Math.toRadians(camera.getRotation().y));
        right.rotateY((float) Math.toRadians(camera.getRotation().y));
        
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            camera.move(forward, moveSpeed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            camera.move(forward, -moveSpeed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            camera.move(right, moveSpeed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            camera.move(right, -moveSpeed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_SPACE) == GLFW.GLFW_PRESS) {
            camera.move(new Vector3f(0, 1, 0), moveSpeed);
        }
        if (GLFW.glfwGetKey(windowHandle, GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS) {
            camera.move(new Vector3f(0, -1, 0), moveSpeed);
        }
    }
}