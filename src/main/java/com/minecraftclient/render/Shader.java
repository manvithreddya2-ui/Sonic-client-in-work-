package com.minecraftclient.render;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shader {
    private static final Logger logger = LoggerFactory.getLogger(Shader.class);
    private int programID;
    
    public Shader(String vertexSource, String fragmentSource) {
        int vertexShader = compileShader(vertexSource, GL20.GL_VERTEX_SHADER);
        int fragmentShader = compileShader(fragmentSource, GL20.GL_FRAGMENT_SHADER);
        
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShader);
        GL20.glAttachShader(programID, fragmentShader);
        GL20.glLinkProgram(programID);
        
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0) {
            logger.error("Shader linking failed: {}", GL20.glGetProgramInfoLog(programID));
        }
        
        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);
    }
    
    private int compileShader(String source, int type) {
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);
        
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0) {
            logger.error("Shader compilation failed: {}", GL20.glGetShaderInfoLog(shaderID));
        }
        
        return shaderID;
    }
    
    public void use() {
        GL20.glUseProgram(programID);
    }
    
    public void cleanup() {
        GL20.glDeleteProgram(programID);
    }
}