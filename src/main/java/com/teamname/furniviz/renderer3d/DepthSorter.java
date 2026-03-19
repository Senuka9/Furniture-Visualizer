package com.teamname.furniviz.renderer3d;

import java.awt.Point;
import java.util.*;

/**
 * DepthSorter - Implements painter's algorithm for proper face rendering order
 *
 * Problem: Without depth sorting, faces render in random order causing z-fighting
 * Solution: Sort faces by depth (distance from camera) before rendering
 *
 * When faces are rendered back-to-front, closer faces appear on top of distant ones
 */
public class DepthSorter {

    /**
     * Represents a triangular face with depth information
     */
    public static class DepthFace implements Comparable<DepthFace> {
        public Point[] screenPoints;      // 2D projected screen coordinates
        public java.awt.Color fillColor;
        public java.awt.Color strokeColor;
        public float averageDepth;        // Average z-distance from camera

        public DepthFace(Point[] screenPoints, java.awt.Color fillColor, 
                        java.awt.Color strokeColor, float averageDepth) {
            this.screenPoints = screenPoints;
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.averageDepth = averageDepth;
        }

        /**
         * Compare faces by depth (for sorting)
         * Returns faces sorted from BACK to FRONT (painter's algorithm)
         * Larger depth = farther away = should render first
         */
        @Override
        public int compareTo(DepthFace other) {
            // Sort in descending order (farthest first)
            return Float.compare(other.averageDepth, this.averageDepth);
        }
    }

    /**
     * Sort faces for proper rendering order
     */
    public static List<DepthFace> sortFacesByDepth(List<DepthFace> faces) {
        if (faces == null || faces.isEmpty()) {
            return faces;
        }

        // Create a copy and sort
        List<DepthFace> sorted = new ArrayList<>(faces);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Calculate average depth of a face (distance from camera)
     * The face is defined by vertices in camera space
     */
    public static float calculateFaceDepth(Projection.Vector3f[] vertices) {
        if (vertices == null || vertices.length == 0) {
            return Float.MAX_VALUE;
        }

        float sumZ = 0;
        for (Projection.Vector3f v : vertices) {
            sumZ += v.z;
        }
        return sumZ / vertices.length;
    }

    /**
     * Calculate face depth for a triangle
     */
    public static float calculateTriangleDepth(
            Projection.Vector3f v1,
            Projection.Vector3f v2,
            Projection.Vector3f v3) {
        return (v1.z + v2.z + v3.z) / 3.0f;
    }

    /**
     * Calculate face center for sorting (alternative method)
     * Some systems use center instead of average
     */
    public static Projection.Vector3f calculateFaceCenter(Projection.Vector3f[] vertices) {
        if (vertices == null || vertices.length == 0) {
            return new Projection.Vector3f(0, 0, 0);
        }

        float sumX = 0, sumY = 0, sumZ = 0;
        for (Projection.Vector3f v : vertices) {
            sumX += v.x;
            sumY += v.y;
            sumZ += v.z;
        }

        int count = vertices.length;
        return new Projection.Vector3f(sumX / count, sumY / count, sumZ / count);
    }

    /**
     * Check if face is front-facing (normal points toward camera)
     * If not front-facing, it's backfacing and shouldn't be rendered
     */
    public static boolean isFrontFacing(
            Projection.Vector3f v1,
            Projection.Vector3f v2,
            Projection.Vector3f v3,
            Projection.Vector3f cameraPos) {

        // Calculate triangle normal
        Projection.Vector3f edge1 = v2.subtract(v1);
        Projection.Vector3f edge2 = v3.subtract(v1);
        Projection.Vector3f normal = edge1.cross(edge2);

        // Vector from triangle to camera
        Projection.Vector3f toCamera = cameraPos.subtract(v1);

        // If normal and toCamera point in same direction, face is front-facing
        return normal.dot(toCamera) > 0;
    }

    /**
     * Remove backfacing faces (optimization)
     * Faces that face away from camera don't need to be rendered
     */
    public static List<DepthFace> removeBackfacingFaces(
            List<DepthFace> faces,
            Projection.Vector3f cameraPos) {

        if (faces == null || faces.isEmpty()) {
            return faces;
        }

        List<DepthFace> frontFacing = new ArrayList<>();

        for (DepthFace face : faces) {
            if (face.screenPoints != null && face.screenPoints.length >= 3) {
                // Simple heuristic: if face area is positive, it's front-facing
                // (after projection, front-facing faces have positive area)
                double area = calculatePolygonArea(face.screenPoints);
                if (area > 0) {
                    frontFacing.add(face);
                }
            }
        }

        return frontFacing;
    }

    /**
     * Calculate 2D polygon area using Shoelace formula
     * Positive area = counter-clockwise = front-facing
     * Negative area = clockwise = back-facing
     */
    private static double calculatePolygonArea(Point[] points) {
        if (points == null || points.length < 3) {
            return 0;
        }

        double area = 0;
        for (int i = 0; i < points.length; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % points.length];
            area += (double) p1.x * p2.y - (double) p2.x * p1.y;
        }

        return area / 2.0;
    }

    /**
     * Apply frustum culling - remove faces outside view frustum
     * This prevents rendering of geometry that's off-screen
     */
    public static List<DepthFace> applyCulling(
            List<DepthFace> faces,
            int screenWidth,
            int screenHeight) {

        if (faces == null) {
            return faces;
        }

        List<DepthFace> culled = new ArrayList<>();
        final int margin = 100;  // Allow some off-screen rendering

        for (DepthFace face : faces) {
            if (isInFrustum(face.screenPoints, screenWidth, screenHeight, margin)) {
                culled.add(face);
            }
        }

        return culled;
    }

    /**
     * Check if polygon is visible in screen bounds
     */
    private static boolean isInFrustum(Point[] points, int screenWidth, 
                                      int screenHeight, int margin) {
        if (points == null || points.length == 0) {
            return false;
        }

        for (Point p : points) {
            if (p.x >= -margin && p.x < screenWidth + margin &&
                p.y >= -margin && p.y < screenHeight + margin) {
                return true;
            }
        }

        return false;
    }
}

