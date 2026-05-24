# Sonic Client

⚡ A fast, feature-rich Minecraft-style client built with Java and LWJGL.

## Features
- ✅ 3D block-based world rendering with textures
- ✅ Player movement with gravity and jumping
- ✅ Block placement and destruction
- ✅ Procedural terrain generation
- ✅ Particle effects
- ✅ Physics system
- ✅ First-person camera with smooth mouse control
- ✅ HUD with stats and crosshair
- ✅ Day/night cycle
- ✅ Optimized chunk rendering

## Prerequisites
- Java 16 or higher
- Maven 3.6+

## Installation & Running

```bash
# Clone the repository
git clone https://github.com/manvithreddya2-ui/First-project.git
cd First-project

# Build the project
mvn clean package

# Run the client
mvn exec:java@run
```

## Controls
- **W/A/S/D** - Move forward/left/backward/right
- **Mouse** - Look around
- **Space** - Jump
- **Left Shift** - Move down / Sprint (hold)
- **Left Click** - Break block
- **Right Click** - Place block
- **F3** - Toggle debug info
- **F5** - Toggle particle effects
- **ESC** - Exit

## Project Structure
```
src/main/java/com/minecraftclient/
├── Client.java                 # Main entry point & game loop
├── render/
│   ├── Renderer.java          # Core rendering engine
│   ├── Camera.java            # First-person camera
│   ├── TextureManager.java    # Texture loading & management
│   └── Shader.java            # OpenGL shader handling
├── world/
│   ├── World.java             # World management
│   ├── Chunk.java             # 16x256x16 chunks
│   ├── Block.java             # Block types & properties
│   └── TerrainGenerator.java  # Procedural generation
├── input/
│   └── InputHandler.java      # Keyboard & mouse input
├── physics/
│   └── PhysicsEngine.java     # Gravity, collisions, movement
├── particles/
│   └── ParticleSystem.java    # Particle effects
└── ui/
    └── HUD.java               # On-screen display
```

## Key Components

### Rendering
- OpenGL 3.2+ with core profile
- Frustum culling for performance
- Batch rendering for blocks
- Texture atlas support

### Physics
- Gravity and velocity system
- Collision detection
- Jump mechanics
- Movement acceleration

### World Generation
- Perlin noise-based terrain
- Multiple biome support
- Tree generation
- Ore distribution

## Performance
- Render distance: 10 chunks
- Optimized chunk mesh generation
- GPU instancing for blocks
- Efficient culling system

## License
MIT