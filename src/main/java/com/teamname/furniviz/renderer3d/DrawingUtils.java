package com.teamname.furniviz.renderer3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DrawingUtils - Utility class for 3D to 2D projection and drawing
 *
 * Provides helper methods for:
 * - Projecting 3D points to 2D screen coordinates
 * - Drawing 3D shapes (boxes, planes, etc.)
 * - Applying lighting and colors
 * - Depth sorting for proper rendering
 */
public class DrawingUtils {

    /**
     * Project a 3D point to 2D screen coordinates using perspective projection
     *
     * @param point3D The 3D point to project
     * @param camera The camera providing transformation matrices
     * @param screenWidth Screen width in pixels
     * @param screenHeight Screen height in pixels
     * @return 2D point in screen coordinates, or null if behind camera
     */
    public static Point project3Dto2D(
            Projection.Vector3f point3D,
            Camera camera,
            int screenWidth,
            int screenHeight) {

        if (point3D == null || camera == null) {
            return null;
        }

        // Get camera transformation matrices
        Projection.Matrix4f viewMatrix = camera.getViewMatrix();
        Projection.Matrix4f projectionMatrix = camera.getPerspectiveMatrix((float)screenWidth / screenHeight);

        // Transform point from world space to camera space
        Projection.Vector3f cameraSpace = transformPoint(point3D, viewMatrix);

        // Check if point is behind camera
        if (cameraSpace.z >= 0) {
            return null;  // Point is behind camera, don't draw
        }

        // Apply perspective divide
        float perspectiveDivide = -cameraSpace.z;  // Negative because we look down -Z
        if (perspectiveDivide <= 0.1f) {
            return null;  // Too close to camera
        }

        float screenX = centerX(screenWidth) + (cameraSpace.x / perspectiveDivide) * getScale(screenWidth);
        float screenY = centerY(screenHeight) - (cameraSpace.y / perspectiveDivide) * getScale(screenHeight);

        // Clamp to screen bounds
        if (screenX < -1000 || screenX > screenWidth + 1000 ||
            screenY < -1000 || screenY > screenHeight + 1000) {
            return null;  // Point is way off screen
        }

        return new Point((int)screenX, (int)screenY);
    }

    /**
     * Project multiple 3D points to 2D screen coordinates
     */
    public static Point[] projectPoints(
            Projection.Vector3f[] points3D,
            Camera camera,
            int screenWidth,
            int screenHeight) {

        if (points3D == null) return null;

        List<Point> projectedPoints = new ArrayList<>();
        for (Projection.Vector3f point : points3D) {
            Point screenPoint = project3Dto2D(point, camera, screenWidth, screenHeight);
            if (screenPoint != null) {
                projectedPoints.add(screenPoint);
            }
        }

        return projectedPoints.toArray(new Point[0]);
    }

    /**
     * Draw a filled polygon on the screen
     */
    public static void drawFilledPolygon(
            Graphics2D g,
            Point[] vertices,
            Color fillColor,
            Color strokeColor) {

        if (vertices == null || vertices.length < 3) {
            return;  // Need at least 3 vertices for a polygon
        }

        // Extract x and y coordinates
        int[] xPoints = new int[vertices.length];
        int[] yPoints = new int[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            xPoints[i] = vertices[i].x;
            yPoints[i] = vertices[i].y;
        }

        // Fill polygon
        if (fillColor != null) {
            g.setColor(fillColor);
            g.fillPolygon(xPoints, yPoints, vertices.length);
        }

        // Draw outline
        if (strokeColor != null) {
            g.setColor(strokeColor);
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(xPoints, yPoints, vertices.length);
        }
    }

    /**
     * Draw a filled rectangle (using polygon)
     */
    public static void drawRectangle(
            Graphics2D g,
            Point p1, Point p2, Point p3, Point p4,
            Color fillColor,
            Color strokeColor) {

        Point[] vertices = {p1, p2, p3, p4};
        drawFilledPolygon(g, vertices, fillColor, strokeColor);
    }

    /**
     * Calculate lighting intensity for a surface based on normal and light direction
     *
     * @param normal Surface normal vector
     * @param lightDirection Direction towards light source
     * @param ambientLight Ambient light intensity (0.0 to 1.0)
     * @return Lighting intensity (0.0 to 1.0)
     */
    public static float calculateLighting(
            Projection.Vector3f normal,
            Projection.Vector3f lightDirection,
            float ambientLight) {

        if (normal == null || lightDirection == null) {
            return ambientLight;
        }

        // Normalize vectors
        Projection.Vector3f normalizedNormal = normal.normalize();
        Projection.Vector3f normalizedLight = lightDirection.normalize();

        // Calculate dot product (cosine of angle)
        float dotProduct = normalizedNormal.dot(normalizedLight);

        // Clamp to 0-1 range
        dotProduct = Math.max(0, Math.min(1, dotProduct));

        // Combine ambient and directional lighting
        float directionalIntensity = 0.7f;  // Strength of directional light
        float totalLighting = ambientLight + (dotProduct * directionalIntensity);

        return Math.min(1.0f, totalLighting);
    }

    /**
     * Apply lighting to a color
     */
    public static Color applyLighting(Color baseColor, float lightIntensity) {
        if (baseColor == null) {
            return Color.GRAY;
        }

        // Clamp intensity to 0-1
        lightIntensity = Math.max(0, Math.min(1, lightIntensity));

        // Apply lighting to RGB
        int r = (int)(baseColor.getRed() * lightIntensity);
        int g = (int)(baseColor.getGreen() * lightIntensity);
        int b = (int)(baseColor.getBlue() * lightIntensity);

        return new Color(r, g, b);
    }

    /**
     * Calculate surface normal from 3 vertices
     */
    public static Projection.Vector3f calculateNormal(
            Projection.Vector3f p1,
            Projection.Vector3f p2,
            Projection.Vector3f p3) {

        if (p1 == null || p2 == null || p3 == null) {
            return new Projection.Vector3f(0, 0, 1);  // Default normal
        }

        // Vectors from p1 to p2 and p1 to p3
        Projection.Vector3f u = p2.subtract(p1);
        Projection.Vector3f v = p3.subtract(p1);

        // Cross product: u × v
        Projection.Vector3f normal = u.cross(v);

        // Normalize
        return normal.normalize();
    }

    /**
     * Create a box (cube) mesh with specified dimensions
     * Returns array of 8 corner vertices
     */
    public static Projection.Vector3f[] createBoxVertices(
            float centerX,
            float centerY,
            float centerZ,
            float width,
            float height,
            float depth) {

        float halfW = width / 2;
        float halfH = height / 2;
        float halfD = depth / 2;

        return new Projection.Vector3f[]{
            // Bottom face (y = centerY - halfH)
            new Projection.Vector3f(centerX - halfW, centerY - halfH, centerZ - halfD),  // 0: BLF
            new Projection.Vector3f(centerX + halfW, centerY - halfH, centerZ - halfD),  // 1: BRF
            new Projection.Vector3f(centerX + halfW, centerY - halfH, centerZ + halfD),  // 2: BRB
            new Projection.Vector3f(centerX - halfW, centerY - halfH, centerZ + halfD),  // 3: BLB

            // Top face (y = centerY + halfH)
            new Projection.Vector3f(centerX - halfW, centerY + halfH, centerZ - halfD),  // 4: TLF
            new Projection.Vector3f(centerX + halfW, centerY + halfH, centerZ - halfD),  // 5: TRF
            new Projection.Vector3f(centerX + halfW, centerY + halfH, centerZ + halfD),  // 6: TRB
            new Projection.Vector3f(centerX - halfW, centerY + halfH, centerZ + halfD),  // 7: TLB
        };
    }

    /**
     * Get 6 faces of a box (each face is 4 vertices)
     * Returns array of 6 face quad indices
     */
    public static int[][] getBoxFaces() {
        return new int[][]{
            {0, 1, 2, 3},  // Bottom
            {4, 7, 6, 5},  // Top
            {0, 4, 5, 1},  // Front
            {3, 2, 6, 7},  // Back
            {0, 3, 7, 4},  // Left
            {1, 5, 6, 2},  // Right
        };
    }

    /**
     * Get face normals for a box
     */
    public static Projection.Vector3f[] getBoxNormals() {
        return new Projection.Vector3f[]{
            new Projection.Vector3f(0, -1, 0),  // Bottom: negative Y
            new Projection.Vector3f(0, 1, 0),   // Top: positive Y
            new Projection.Vector3f(0, 0, -1),  // Front: negative Z
            new Projection.Vector3f(0, 0, 1),   // Back: positive Z
            new Projection.Vector3f(-1, 0, 0),  // Left: negative X
            new Projection.Vector3f(1, 0, 0),   // Right: positive X
        };
    }

    /**
     * Create a plane (rectangle) mesh
     * Returns 4 corner vertices
     */
    public static Projection.Vector3f[] createPlaneVertices(
            float centerX,
            float centerY,
            float centerZ,
            float width,
            float length,
            float rotationY) {

        float halfW = width / 2;
        float halfL = length / 2;

        // Rotation around Y axis
        float cos = (float)Math.cos(Math.toRadians(rotationY));
        float sin = (float)Math.sin(Math.toRadians(rotationY));

        return new Projection.Vector3f[]{
            rotatePoint(new Projection.Vector3f(centerX - halfW, centerY, centerZ - halfL), centerX, centerZ, cos, sin),
            rotatePoint(new Projection.Vector3f(centerX + halfW, centerY, centerZ - halfL), centerX, centerZ, cos, sin),
            rotatePoint(new Projection.Vector3f(centerX + halfW, centerY, centerZ + halfL), centerX, centerZ, cos, sin),
            rotatePoint(new Projection.Vector3f(centerX - halfW, centerY, centerZ + halfL), centerX, centerZ, cos, sin),
        };
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    private static Projection.Vector3f transformPoint(
            Projection.Vector3f point,
            Projection.Matrix4f matrix) {

        if (point == null || matrix == null) {
            return point;
        }

        // Apply matrix transformation to the point
        return matrix.transform(point);
    }

    private static int centerX(int screenWidth) {
        return screenWidth / 2;
    }

    private static int centerY(int screenHeight) {
        return screenHeight / 2;
    }

    private static float getScale(int screenDimension) {
        return screenDimension / 6.0f;  // Adjust for viewport
    }

    private static Projection.Vector3f rotatePoint(
            Projection.Vector3f point,
            float centerX,
            float centerZ,
            float cos,
            float sin) {

        float x = point.x - centerX;
        float z = point.z - centerZ;

        float rotatedX = x * cos - z * sin;
        float rotatedZ = x * sin + z * cos;

        return new Projection.Vector3f(rotatedX + centerX, point.y, rotatedZ + centerZ);
    }
}
