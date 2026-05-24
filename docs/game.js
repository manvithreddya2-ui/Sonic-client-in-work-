// Three.js Game Engine for Sonic Client Browser Edition
let scene, camera, renderer;
let player = { position: { x: 0, y: 20, z: 0 }, velocity: { x: 0, y: 0, z: 0 } };
let blocks = new Map();
let keys = {};
let mouseDown = { left: false, right: false };
let stats = { blocksBroken: 0, blocksPlaced: 0, fps: 0 };
let lastTime = Date.now();
let frameCount = 0;
let selectedBlock = 1; // 1 = dirt, 2 = stone, 3 = grass

// Initialize Game
function init() {
    // Scene Setup
    scene = new THREE.Scene();
    scene.background = new THREE.Color(0x87ceeb);
    scene.fog = new THREE.Fog(0x87ceeb, 100, 500);

    // Camera Setup
    camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
    camera.position.set(0, 20, 0);
    player.position = { x: 0, y: 20, z: 0 };

    // Renderer Setup
    renderer = new THREE.WebGLRenderer({ antialias: true });
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.shadowMap.enabled = true;
    document.getElementById('gameContainer').appendChild(renderer.domElement);

    // Lighting
    const sunlight = new THREE.DirectionalLight(0xffffff, 1);
    sunlight.position.set(50, 50, 50);
    sunlight.castShadow = true;
    sunlight.shadow.mapSize.width = 2048;
    sunlight.shadow.mapSize.height = 2048;
    scene.add(sunlight);

    const ambientLight = new THREE.AmbientLight(0xffffff, 0.6);
    scene.add(ambientLight);

    // Generate World
    generateWorld();

    // Event Listeners
    document.addEventListener('keydown', onKeyDown);
    document.addEventListener('keyup', onKeyUp);
    document.addEventListener('mousemove', onMouseMove);
    document.addEventListener('mousedown', onMouseDown);
    document.addEventListener('mouseup', onMouseUp);
    window.addEventListener('resize', onWindowResize);

    // Lock Pointer
    document.getElementById('gameContainer').addEventListener('click', () => {
        document.getElementById('gameContainer').requestPointerLock();
    });

    // Remove Loading Screen
    document.getElementById('loading').style.display = 'none';

    // Start Game Loop
    animate();
}

// Generate Procedural World
function generateWorld() {
    for (let x = -20; x < 20; x++) {
        for (let z = -20; z < 20; z++) {
            let height = Math.floor(Math.sin(x * 0.1) * 3 + Math.cos(z * 0.1) * 3 + 10);
            
            for (let y = 0; y < height; y++) {
                let type = y === height - 1 ? 3 : y === height - 2 ? 1 : 2; // grass, dirt, stone
                createBlock(x, y, z, type);
            }
        }
    }
}

// Create a Block
function createBlock(x, y, z, type) {
    const geometry = new THREE.BoxGeometry(1, 1, 1);
    
    // Block colors
    const colors = {
        1: 0x8b7355, // dirt
        2: 0x808080, // stone
        3: 0x228b22  // grass
    };

    const material = new THREE.MeshPhongMaterial({ color: colors[type] || 0xffffff });
    const block = new THREE.Mesh(geometry, material);
    block.position.set(x + 0.5, y + 0.5, z + 0.5);
    block.castShadow = true;
    block.receiveShadow = true;
    block.userData = { x, y, z, type };

    scene.add(block);
    blocks.set(`${x},${y},${z}`, block);
}

// Input Handling
function onKeyDown(e) {
    keys[e.key.toLowerCase()] = true;

    if (e.key === 'F1') {
        const controls = document.getElementById('controls');
        controls.style.display = controls.style.display === 'none' ? 'block' : 'none';
    }

    if (e.key === ' ') {
        e.preventDefault();
        if (player.onGround) {
            player.velocity.y = 15;
            player.onGround = false;
        }
    }
}

function onKeyUp(e) {
    keys[e.key.toLowerCase()] = false;
}

let mouseRotation = { x: 0, y: 0 };

function onMouseMove(e) {
    if (document.pointerLockElement === document.getElementById('gameContainer')) {
        mouseRotation.x += e.movementX * 0.002;
        mouseRotation.y += e.movementY * 0.002;

        if (mouseRotation.y > Math.PI / 2) mouseRotation.y = Math.PI / 2;
        if (mouseRotation.y < -Math.PI / 2) mouseRotation.y = -Math.PI / 2;

        camera.rotation.order = 'YXZ';
        camera.rotation.y = mouseRotation.x;
        camera.rotation.x = mouseRotation.y;
    }
}

function onMouseDown(e) {
    if (e.button === 0) mouseDown.left = true;
    if (e.button === 2) mouseDown.right = true;
}

function onMouseUp(e) {
    if (e.button === 0) mouseDown.left = false;
    if (e.button === 2) mouseDown.right = false;
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

// Game Update Loop
function updatePlayer() {
    const speed = keys['shift'] ? 0.5 : 0.25;
    const direction = new THREE.Vector3();

    if (keys['w']) direction.z -= speed;
    if (keys['s']) direction.z += speed;
    if (keys['a']) direction.x -= speed;
    if (keys['d']) direction.x += speed;

    // Rotate direction based on camera
    direction.applyAxisAngle(new THREE.Vector3(0, 1, 0), mouseRotation.x);

    player.velocity.x = direction.x;
    player.velocity.z = direction.z;

    // Gravity
    player.velocity.y -= 0.5;
    if (player.velocity.y < -50) player.velocity.y = -50;

    // Update position
    player.position.x += player.velocity.x;
    player.position.y += player.velocity.y;
    player.position.z += player.velocity.z;

    // Collision detection
    const blockKey = `${Math.floor(player.position.x)},${Math.floor(player.position.y)},${Math.floor(player.position.z)}`;
    if (blocks.has(blockKey)) {
        player.position.y = Math.floor(player.position.y) + 1;
        player.velocity.y = 0;
        player.onGround = true;
    } else {
        player.onGround = false;
    }

    camera.position.copy(player.position);
    camera.position.y += 1.6; // Eye height
}

// Raycast for Block Interaction
function getBlockAtCursor() {
    const raycaster = new THREE.Raycaster();
    raycaster.setFromCamera(new THREE.Vector2(0, 0), camera);

    const blockObjects = Array.from(blocks.values());
    const intersects = raycaster.intersectObjects(blockObjects);

    if (intersects.length > 0) {
        return intersects[0].object;
    }
    return null;
}

// Block Breaking and Placing
function updateBlockInteraction() {
    const block = getBlockAtCursor();

    if (mouseDown.left && block) {
        // Break block
        scene.remove(block);
        const key = `${block.userData.x},${block.userData.y},${block.userData.z}`;
        blocks.delete(key);
        stats.blocksBroken++;
        mouseDown.left = false;
    }

    if (mouseDown.right && block) {
        // Place block next to it
        const newPos = new THREE.Vector3(
            block.position.x + (Math.random() > 0.5 ? 1 : -1),
            block.position.y,
            block.position.z
        );
        createBlock(Math.floor(newPos.x), Math.floor(newPos.y), Math.floor(newPos.z), selectedBlock);
        stats.blocksPlaced++;
        mouseDown.right = false;
    }
}

// Update HUD
function updateHUD() {
    document.getElementById('fps').textContent = stats.fps;
    document.getElementById('pos').textContent = 
        `${Math.floor(player.position.x)}, ${Math.floor(player.position.y)}, ${Math.floor(player.position.z)}`;
    document.getElementById('blocksBroken').textContent = stats.blocksBroken;
    document.getElementById('blocksPlaced').textContent = stats.blocksPlaced;
}

// Calculate FPS
function updateFPS() {
    frameCount++;
    const now = Date.now();
    if (now - lastTime >= 1000) {
        stats.fps = frameCount;
        frameCount = 0;
        lastTime = now;
    }
}

// Main Animation Loop
function animate() {
    requestAnimationFrame(animate);

    updatePlayer();
    updateBlockInteraction();
    updateHUD();
    updateFPS();

    renderer.render(scene, camera);
}

// Start the game when page loads
window.addEventListener('load', init);