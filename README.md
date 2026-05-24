# Minecraft Client

A lightweight Minecraft client implementation built with Java and LWJGL.

## Features
- 3D world rendering
- Player movement and camera control
- Block placement and destruction
- Multiplayer support (basic)

## Prerequisites
- Java 16 or higher
- Maven 3.6+

## Setup

```bash
# Clone the repository
git clone https://github.com/manvithreddya2-ui/First-project.git
cd First-project

# Build the project
mvn clean package

# Run the client
mvn exec:java@run
```

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   └── com/minecraftclient/
│   │       ├── Client.java          # Main entry point
│   │       ├── world/
│   │       │   ├── World.java
│   │       │   ├── Chunk.java
│   │       │   └── Block.java
│   │       ├── render/
│   │       │   ├── Renderer.java
│   │       │   └── Camera.java
│   │       ├── input/
│   │       │   └── InputHandler.java
│   │       └── network/
│   │           └── NetworkManager.java
│   └── resources/
│       └── shaders/
├── pom.xml
└── README.md
```

## Controls
- **W/A/S/D** - Move forward/left/backward/right
- **Space** - Jump
- **Left Click** - Break block
- **Right Click** - Place block
- **ESC** - Exit

## License
MIT