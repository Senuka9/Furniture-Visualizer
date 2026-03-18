package com.teamname.furniviz.renderer3d;

import com.teamname.furniviz.app.DesignState;
import com.teamname.furniviz.furniture.FurnitureItem;
import com.teamname.furniviz.room.Room;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.List;

/**
 * Renderer3D - Main 3D scene manager and rendering engine
 *
 * Responsibilities:
 * - Manage 3D scene graph
 * - Create and update room and furniture geometry
 * - Handle lighting and materials
 * - Maintain camera and rendering state
 * - Provide interface for rendering to canvas
 */
public class Renderer3D {

    // Reference to shared design state
    private DesignState designState;

    // Scene components
    private Camera camera;
    private SceneGraph sceneGraph;
    private LightingManager lighting;

    // Render state
    private boolean showGrid = true;
    private boolean showNormals = false;
    private RenderMode renderMode = RenderMode.SOLID;

    // Performance tracking
    private long lastUpdateTime = 0;
    private int frameCount = 0;
    private float fps = 0;

    /**
     * Render mode - how to render the scene
     */
    public enum RenderMode {
        WIREFRAME,      // Show triangle edges only
        SOLID,          // Show filled triangles
        SHADED          // Show with lighting
    }

    /**
     * Constructor
     * @param designState The shared application state
     */
    public Renderer3D(DesignState designState) {
        this.designState = designState;
        this.camera = new Camera();
        this.sceneGraph = new SceneGraph();
        this.lighting = new LightingManager();

        // Initialize scene with room and furniture
        rebuildScene();
    }

    // ============================================================================
    // SCENE MANAGEMENT
    // ============================================================================

    /**
     * Rebuild entire scene from DesignState
     * Called when room or major properties change
     */
    public void rebuildScene() {
        sceneGraph.clear();

        // Get current room
        Room room = designState.getRoom();
        if (room == null) {
            return;  // No room to render
        }

        // Create room geometry
        createRoomGeometry(room);

        // Create furniture geometry
        List<FurnitureItem> items = designState.getFurnitureItems();
        for (FurnitureItem item : items) {
            createFurnitureGeometry(item);
        }
    }

    /**
     * Update scene based on DesignState changes
     * More efficient than rebuilding entire scene
     */
    public void update() {
        // Check for furniture changes
        List<FurnitureItem> items = designState.getFurnitureItems();

        // Update existing furniture nodes
        for (int i = 0; i < items.size(); i++) {
            FurnitureItem item = items.get(i);
            SceneNode furnitureNode = sceneGraph.getFurnitureNode(i);

            if (furnitureNode != null) {
                // Update position and rotation
                furnitureNode.setPosition(
                    (float) item.getX(),
                    (float) item.getHeight() / 2,
                    (float) item.getY()
                );
                furnitureNode.setRotation(
                    0,  // X rotation
                    Projection.convertRotation(item.getRotation()),  // Y rotation
                    0   // Z rotation
                );
            }
        }

        // Track FPS
        updateFPS();
    }

    /**
     * Create 3D geometry for room (floor, walls, ceiling)
     */
    private void createRoomGeometry(Room room) {
        double width = room.getWidth();
        double length = room.getLength();
        Color wallColor = room.getWallColor();

        // Floor (XZ plane at y=0)
        MeshFactory.Mesh floorMesh = MeshFactory.createPlane(width, length, Color.LIGHT_GRAY);
        SceneNode floorNode = new SceneNode("Floor");
        floorNode.setMesh(floorMesh);
        floorNode.setPosition(0, 0, 0);
        sceneGraph.addNode(floorNode);

        // Walls - use boxes for simplicity
        float wallHeight = 3.0f;  // Standard wall height
        float wallThickness = 0.1f;

        // Front wall (Z = length/2)
        createWall("FrontWall", (float)width, wallHeight, wallThickness,
                   0, wallHeight/2, (float)length/2, wallColor);

        // Back wall (Z = -length/2)
        createWall("BackWall", (float)width, wallHeight, wallThickness,
                   0, wallHeight/2, (float)-length/2, wallColor);

        // Left wall (X = -width/2)
        createWall("LeftWall", wallThickness, wallHeight, (float)length,
                   (float)-width/2, wallHeight/2, 0, wallColor);

        // Right wall (X = width/2)
        createWall("RightWall", wallThickness, wallHeight, (float)length,
                   (float)width/2, wallHeight/2, 0, wallColor);

        // Ceiling (optional)
        MeshFactory.Mesh ceilingMesh = MeshFactory.createPlane(width, length, Color.WHITE);
        SceneNode ceilingNode = new SceneNode("Ceiling");
        ceilingNode.setMesh(ceilingMesh);
        ceilingNode.setPosition(0, wallHeight, 0);
        sceneGraph.addNode(ceilingNode);
    }

    /**
     * Helper method to create a wall
     */
    private void createWall(String name, float width, float height, float depth,
                           float x, float y, float z, Color color) {
        MeshFactory.Mesh wallMesh = MeshFactory.createBox(width, height, depth, color);
        SceneNode wallNode = new SceneNode(name);
        wallNode.setMesh(wallMesh);
        wallNode.setPosition(x, y, z);
        sceneGraph.addNode(wallNode);
    }

    /**
     * Create 3D geometry for furniture item
     */
    private void createFurnitureGeometry(FurnitureItem item) {
        // Create box geometry based on furniture dimensions
        MeshFactory.Mesh furnitureMesh = MeshFactory.createBox(
            item.getWidth(),
            item.getHeight(),
            item.getDepth(),
            item.getColor()
        );

        // Create scene node
        SceneNode furnitureNode = new SceneNode(item.getName());
        furnitureNode.setMesh(furnitureMesh);

        // Set position (x, y in 2D → x, z in 3D, offset by height/2)
        furnitureNode.setPosition(
            (float) item.getX(),
            (float) item.getHeight() / 2,
            (float) item.getY()
        );

        // Set rotation (2D rotation → Y-axis rotation in 3D)
        furnitureNode.setRotation(
            0,  // X rotation (pitch)
            Projection.convertRotation(item.getRotation()),  // Y rotation (yaw)
            0   // Z rotation (roll)
        );

        sceneGraph.addFurnitureNode(furnitureNode);
    }

    // ============================================================================
    // RENDERING INTERFACE
    // ============================================================================

    /**
     * Render the 3D scene to a Graphics2D context
     * This is called by Canvas3D for Swing rendering
     */
    public void render(Graphics2D g, int width, int height) {
        if (g == null) return;

        // Clear background
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, 0, width, height);

        // If no room, display message
        if (designState.getRoom() == null) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("Load a room to view in 3D", width / 2 - 100, height / 2);
            return;
        }

        // Set anti-aliasing for better quality
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Render room geometry
        renderRoomGeometry(g, width, height);

        // Render furniture geometry
        renderFurnitureGeometry(g, width, height);

        // Draw statistics
        drawStatistics(g, width, height);
    }

    /**
     * Render room (floor, walls, ceiling)
     */
    private void renderRoomGeometry(Graphics2D g, int width, int height) {
        Room room = designState.getRoom();
        if (room == null) return;

        float roomWidth = (float) room.getWidth();
        float roomLength = (float) room.getLength();
        float wallHeight = 2.5f;

        // Draw floor
        Projection.Vector3f[] floorCorners = new Projection.Vector3f[]{
            new Projection.Vector3f(0, 0, 0),
            new Projection.Vector3f(roomWidth, 0, 0),
            new Projection.Vector3f(roomWidth, 0, roomLength),
            new Projection.Vector3f(0, 0, roomLength)
        };

        Point[] floorScreenCoords = DrawingUtils.projectPoints(floorCorners, camera, width, height);
        if (floorScreenCoords != null && floorScreenCoords.length >= 3) {
            Color floorColor = new Color(180, 180, 180);  // Light gray
            DrawingUtils.drawFilledPolygon(g, floorScreenCoords, floorColor, Color.BLACK);
        }

        // Draw front wall (Z = 0)
        Projection.Vector3f[] frontWallCorners = new Projection.Vector3f[]{
            new Projection.Vector3f(0, 0, 0),
            new Projection.Vector3f(roomWidth, 0, 0),
            new Projection.Vector3f(roomWidth, wallHeight, 0),
            new Projection.Vector3f(0, wallHeight, 0)
        };

        Point[] frontWallScreenCoords = DrawingUtils.projectPoints(frontWallCorners, camera, width, height);
        if (frontWallScreenCoords != null && frontWallScreenCoords.length >= 3) {
            Color wallColor = room.getWallColor() != null ? room.getWallColor() : new Color(220, 220, 220);
            DrawingUtils.drawFilledPolygon(g, frontWallScreenCoords, wallColor, Color.BLACK);
        }

        // Draw left wall (X = 0)
        Projection.Vector3f[] leftWallCorners = new Projection.Vector3f[]{
            new Projection.Vector3f(0, 0, 0),
            new Projection.Vector3f(0, 0, roomLength),
            new Projection.Vector3f(0, wallHeight, roomLength),
            new Projection.Vector3f(0, wallHeight, 0)
        };

        Point[] leftWallScreenCoords = DrawingUtils.projectPoints(leftWallCorners, camera, width, height);
        if (leftWallScreenCoords != null && leftWallScreenCoords.length >= 3) {
            Color wallColor = room.getWallColor() != null ? room.getWallColor() : new Color(200, 200, 200);
            DrawingUtils.drawFilledPolygon(g, leftWallScreenCoords, wallColor, Color.BLACK);
        }
    }

    /**
     * Render all furniture as 3D boxes
     */
    private void renderFurnitureGeometry(Graphics2D g, int width, int height) {
        for (FurnitureItem item : designState.getFurnitureItems()) {
            renderFurnitureItem(g, item, width, height);
        }
    }

    /**
     * Render a single furniture item as a 3D box
     */
    private void renderFurnitureItem(Graphics2D g, FurnitureItem item, int width, int height) {
        float itemX = (float) item.getX();
        float itemY = (float) item.getY();
        float itemW = (float) item.getWidth();
        float itemH = (float) item.getHeight();
        float itemD = (float) item.getDepth();
        float itemRotation = (float) item.getRotation();

        // Create box vertices
        Projection.Vector3f[] boxVertices = DrawingUtils.createBoxVertices(
            itemX + itemW / 2,
            itemH / 2,
            itemY + itemD / 2,
            itemW, itemH, itemD
        );

        // Project vertices to 2D
        Point[] screenVertices = DrawingUtils.projectPoints(boxVertices, camera, width, height);
        if (screenVertices == null || screenVertices.length < 8) {
            return;  // Can't render
        }

        // Get box faces
        int[][] faces = DrawingUtils.getBoxFaces();

        // Draw each visible face
        Color itemColor = item.getColor() != null ? item.getColor() : Color.BLUE;

        for (int faceIdx = 0; faceIdx < faces.length; faceIdx++) {
            int[] faceIndices = faces[faceIdx];

            // Get face vertices
            Point[] faceScreenCoords = new Point[faceIndices.length];
            for (int i = 0; i < faceIndices.length; i++) {
                faceScreenCoords[i] = screenVertices[faceIndices[i]];
            }

            // Apply simple lighting based on face normal
            Projection.Vector3f[] faceWorldCoords = new Projection.Vector3f[faceIndices.length];
            for (int i = 0; i < faceIndices.length; i++) {
                faceWorldCoords[i] = boxVertices[faceIndices[i]];
            }

            Projection.Vector3f faceNormal = DrawingUtils.calculateNormal(
                faceWorldCoords[0], faceWorldCoords[1], faceWorldCoords[2]);

            // Light direction (from above and behind)
            Projection.Vector3f lightDirection = new Projection.Vector3f(1, 1, -1).normalize();

            float lighting = DrawingUtils.calculateLighting(faceNormal, lightDirection, 0.3f);
            Color shadedColor = DrawingUtils.applyLighting(itemColor, lighting);

            // Draw face
            DrawingUtils.drawFilledPolygon(g, faceScreenCoords, shadedColor, Color.BLACK);
        }
    }

    /**
     * Draw debug statistics on screen
     */
    private void drawStatistics(Graphics2D g, int width, int height) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int y = 20;
        int lineHeight = 18; // Increased for better spacing

        Room room = designState.getRoom();
        if (room != null) {
            g.drawString("Room: " + room.getWidth() + "m x " + room.getLength() + "m", 10, y);
            y += lineHeight;
        }

        g.drawString("Furniture: " + designState.getFurnitureItems().size(), 10, y);
        y += lineHeight;

        g.drawString("Camera: " + camera.getPosition().toString(), 10, y);
    }

    /**
     * Get the scene graph
     */
    public SceneGraph getSceneGraph() {
        return sceneGraph;
    }

    // ============================================================================
    // SCENE NODES AND MATRICES
    // ============================================================================
    public List<SceneNode> getSceneNodes() {
        return sceneGraph.getAllNodes();
    }

    /**
     * Get a specific scene node by name
     */
    public SceneNode getSceneNode(String name) {
        return sceneGraph.getNode(name);
    }

    /**
     * Get transform matrices for rendering
     */
    public Projection.Matrix4f getWorldMatrix(SceneNode node) {
        return node.getWorldMatrix();
    }

    public Projection.Matrix4f getViewMatrix() {
        return camera.getViewMatrix();
    }

    public Projection.Matrix4f getProjectionMatrix(float aspectRatio) {
        return camera.getPerspectiveMatrix(aspectRatio);
    }

    // ============================================================================
    // CAMERA CONTROLS
    // ============================================================================

    public Camera getCamera() { return camera; }

    public void setCamera(Camera cam) { this.camera = cam; }

    public void orbitCamera(float deltaY, float deltaX) {
        camera.orbit(deltaY, deltaX);
    }

    public void panCamera(float deltaX, float deltaZ) {
        camera.pan(deltaX, deltaZ);
    }

    public void zoomCamera(float delta) {
        if (delta > 0) {
            camera.zoomIn();
        } else {
            camera.zoomOut();
        }
    }

    // ============================================================================
    // RENDERING STATE
    // ============================================================================

    public RenderMode getRenderMode() { return renderMode; }
    public void setRenderMode(RenderMode mode) { this.renderMode = mode; }

    public boolean isGridShown() { return showGrid; }
    public void setShowGrid(boolean show) { this.showGrid = show; }

    public boolean areNormalsShown() { return showNormals; }
    public void setShowNormals(boolean show) { this.showNormals = show; }

    // ============================================================================
    // LIGHTING
    // ============================================================================

    public LightingManager getLighting() { return lighting; }

    // ============================================================================
    // PERFORMANCE TRACKING
    // ============================================================================

    private void updateFPS() {
        long currentTime = System.currentTimeMillis();
        frameCount++;

        if (currentTime - lastUpdateTime >= 1000) {  // Every second
            fps = frameCount;
            frameCount = 0;
            lastUpdateTime = currentTime;
        }
    }

    public float getFPS() { return fps; }

    @Override
    public String toString() {
        return String.format("Renderer3D[camera=%s, nodes=%d, fps=%.1f]",
            camera, sceneGraph.nodeCount(), fps);
    }
}
