package com.teamname.furniviz.renderer3d;

/**
 * Camera - Manages the viewpoint and camera transformations for 3D rendering
 *
 * Handles:
 * - Camera position (eye), look-at target, up vector
 * - Camera presets (isometric, top-down, front, left side)
 * - Zoom in/out functionality
 * - Pan and rotate operations
 * - View and projection matrix generation
 */
public class Camera {

    // Current camera state
    private Projection.Vector3f position;     // Eye position (where camera is)
    private Projection.Vector3f target;       // Look-at target (where camera looks at)
    private Projection.Vector3f up;           // Up vector (orientation)

    // Camera parameters
    private float zoom = 1.0f;                // Zoom level (1.0 = 100%)
    private float nearPlane = 0.1f;           // Near clipping plane
    private float farPlane = 1000.0f;         // Far clipping plane
    private float fov = 45.0f;                // Field of view (degrees)

    // Camera state for orbiting
    private float orbitDistance;              // Distance from target
    private float orbitAngleY;                // Horizontal rotation (degrees)
    private float orbitAngleX;                // Vertical rotation (degrees, clamped)

    /**
     * Default constructor - initializes to isometric view
     */
    public Camera() {
        setIsometric();
    }

    // ============================================================================
    // CAMERA PRESETS
    // ============================================================================

    /**
     * Isometric view preset
     * Best for viewing furniture in a room
     * Position: (10, 8, 10) looking at (0, 0, 0)
     */
    public void setIsometric() {
        // Isometric angle: approximately 45 degrees around X and Y
        position = new Projection.Vector3f(10, 8, 10);
        target = new Projection.Vector3f(0, 0, 0);
        up = new Projection.Vector3f(0, 1, 0);  // Y-axis is up

        // Calculate orbit parameters
        orbitDistance = position.distance(target);
        orbitAngleY = (float) Math.atan2(position.z, position.x) * 180 / (float)Math.PI;
        orbitAngleX = (float) Math.asin(position.y / orbitDistance) * 180 / (float)Math.PI;
    }

    /**
     * Top-down orthographic view
     * Best for comparing with 2D editor
     * Position: directly above room, looking straight down
     */
    public void setTopDown() {
        position = new Projection.Vector3f(0, 20, 0);  // High above
        target = new Projection.Vector3f(0, 0, 0);
        up = new Projection.Vector3f(0, 0, -1);  // Z-axis points "up" in 2D sense

        orbitDistance = 20;
        orbitAngleY = 0;
        orbitAngleX = 90;
    }

    /**
     * Front view - looking at room from front (positive Z direction)
     */
    public void setFront() {
        position = new Projection.Vector3f(0, 5, 15);  // In front, eye level
        target = new Projection.Vector3f(0, 2, 0);     // Look at center
        up = new Projection.Vector3f(0, 1, 0);

        orbitDistance = position.distance(target);
        orbitAngleY = 0;
        orbitAngleX = (float) Math.asin(position.y / orbitDistance) * 180 / (float)Math.PI;
    }

    /**
     * Left side view - looking at room from left (negative X direction)
     */
    public void setLeftSide() {
        position = new Projection.Vector3f(-15, 5, 0);  // To the left, eye level
        target = new Projection.Vector3f(0, 2, 0);      // Look at center
        up = new Projection.Vector3f(0, 1, 0);

        orbitDistance = position.distance(target);
        orbitAngleY = 90;
        orbitAngleX = (float) Math.asin(position.y / orbitDistance) * 180 / (float)Math.PI;
    }

    /**
     * Right side view - looking at room from right (positive X direction)
     */
    public void setRightSide() {
        position = new Projection.Vector3f(15, 5, 0);   // To the right, eye level
        target = new Projection.Vector3f(0, 2, 0);      // Look at center
        up = new Projection.Vector3f(0, 1, 0);

        orbitDistance = position.distance(target);
        orbitAngleY = -90;
        orbitAngleX = (float) Math.asin(position.y / orbitDistance) * 180 / (float)Math.PI;
    }

    // ============================================================================
    // CAMERA CONTROLS
    // ============================================================================

    /**
     * Zoom in by increasing field of view (or scaling projection)
     */
    public void zoomIn() {
        zoom = Math.min(zoom + 0.1f, 3.0f);  // Max 3x zoom
    }

    /**
     * Zoom out by decreasing field of view
     */
    public void zoomOut() {
        zoom = Math.max(zoom - 0.1f, 0.3f);  // Min 0.3x zoom
    }

    /**
     * Reset zoom to normal (1.0)
     */
    public void resetZoom() {
        zoom = 1.0f;
    }

    /**
     * Zoom by multiplying current zoom by a factor
     * @param factor Multiplier for zoom (e.g., 1.1f for zoom in, 0.9f for zoom out)
     */
    public void zoom(float factor) {
        zoom = Math.max(0.3f, Math.min(3.0f, zoom * factor));
    }

    /**
     * Rotate camera around target (orbit)
     *
     * @param deltaAngleY Change in horizontal rotation (degrees)
     * @param deltaAngleX Change in vertical rotation (degrees)
     */
    public void orbit(float deltaAngleY, float deltaAngleX) {
        orbitAngleY += deltaAngleY;
        orbitAngleX = Math.max(-89, Math.min(89, orbitAngleX + deltaAngleX));  // Clamp vertical

        updatePositionFromOrbit();
    }

    /**
     * Pan camera (move along X-Z plane relative to view direction)
     *
     * @param deltaX Movement in X
     * @param deltaZ Movement in Z
     */
    public void pan(float deltaX, float deltaZ) {
        target.x += deltaX;
        target.z += deltaZ;

        updatePositionFromOrbit();
    }

    /**
     * Update position based on orbit parameters
     */
    private void updatePositionFromOrbit() {
        float rad_x = (float) Math.toRadians(orbitAngleX);
        float rad_y = (float) Math.toRadians(orbitAngleY);

        float radius_xz = orbitDistance * (float) Math.cos(rad_x);

        position.x = target.x + radius_xz * (float) Math.sin(rad_y);
        position.y = target.y + orbitDistance * (float) Math.sin(rad_x);
        position.z = target.z + radius_xz * (float) Math.cos(rad_y);
    }

    // ============================================================================
    // VIEW MATRIX GENERATION
    // ============================================================================

    /**
     * Create view matrix (transforms world coordinates to camera coordinates)
     * Using right-handed coordinate system: X = right, Y = up, Z = back
     */
    public Projection.Matrix4f getViewMatrix() {
        // Forward vector (from eye to target)
        Projection.Vector3f forward = target.subtract(position).normalize();

        // Right vector (perpendicular to forward and up)
        Projection.Vector3f right = forward.cross(up).normalize();

        // Recalculate up vector (perpendicular to forward and right)
        Projection.Vector3f actualUp = right.cross(forward).normalize();

        // Create view matrix
        Projection.Matrix4f view = new Projection.Matrix4f();
        view.m[0][0] = right.x;        view.m[0][1] = right.y;        view.m[0][2] = right.z;        view.m[0][3] = -right.dot(position);
        view.m[1][0] = actualUp.x;     view.m[1][1] = actualUp.y;     view.m[1][2] = actualUp.z;     view.m[1][3] = -actualUp.dot(position);
        view.m[2][0] = -forward.x;     view.m[2][1] = -forward.y;     view.m[2][2] = -forward.z;     view.m[2][3] = forward.dot(position);
        view.m[3][0] = 0;              view.m[3][1] = 0;              view.m[3][2] = 0;              view.m[3][3] = 1;

        return view;
    }

    /**
     * Create perspective projection matrix
     * Uses zoom level to scale field of view
     */
    public Projection.Matrix4f getPerspectiveMatrix(float aspect) {
        float fovRadians = (float) Math.toRadians(fov / zoom);  // Zoom affects FOV
        return Projection.Matrix4f.perspective(fovRadians, aspect, nearPlane, farPlane);
    }

    /**
     * Create orthographic projection matrix (for UI or 2D-like views)
     */
    public Projection.Matrix4f getOrthographicMatrix(float width, float height) {
        float w = width / zoom / 2;
        float h = height / zoom / 2;
        return Projection.Matrix4f.orthographic(-w, w, -h, h, nearPlane, farPlane);
    }

    // ============================================================================
    // GETTERS AND SETTERS
    // ============================================================================

    public Projection.Vector3f getPosition() { return new Projection.Vector3f(position); }
    public Projection.Vector3f getTarget() { return new Projection.Vector3f(target); }
    public Projection.Vector3f getUp() { return new Projection.Vector3f(up); }

    public void setPosition(Projection.Vector3f pos) {
        this.position = new Projection.Vector3f(pos);
        recalculateOrbit();
    }
    public void setTarget(Projection.Vector3f tgt) {
        this.target = new Projection.Vector3f(tgt);
        recalculateOrbit();
    }

    public float getZoom() { return zoom; }
    public void setZoom(float z) { zoom = Math.max(0.3f, Math.min(3.0f, z)); }

    public float getFov() { return fov; }
    public void setFov(float f) { fov = f; }

    /**
     * Recalculate orbit parameters from current position
     * Used when position is set directly
     */
    private void recalculateOrbit() {
        Projection.Vector3f diff = position.subtract(target);
        orbitDistance = diff.length();
        if (orbitDistance > 0.01f) {
            orbitAngleY = (float) Math.atan2(diff.z, diff.x) * 180 / (float)Math.PI;
            orbitAngleX = (float) Math.asin(diff.y / orbitDistance) * 180 / (float)Math.PI;
        }
    }

    @Override
    public String toString() {
        return String.format("Camera[pos=%s, target=%s, zoom=%.2f]",
            Projection.format(position), Projection.format(target), zoom);
    }
}
