package com.teamname.furniviz.renderer3d;

/**
 * RenderingOptimizations - Contains techniques to improve rendering performance
 *
 * Techniques:
 * 1. Frustum Culling - Don't render objects outside camera view
 * 2. Backface Culling - Don't render faces facing away from camera
 * 3. Level of Detail (LOD) - Use simpler meshes for distant objects
 * 4. Draw Call Batching - Combine similar renders into single call
 * 5. Viewport Optimization - Reduced resolution for distant objects
 */
public class RenderingOptimizations {

    /**
     * Calculate screen-space size of object
     * Used for LOD decisions
     */
    public static float calculateScreenSpaceSize(
            Projection.Vector3f objectPosition,
            float objectRadius,
            Projection.Vector3f cameraPosition,
            float fov) {

        // Distance from camera
        float distance = cameraPosition.distance(objectPosition);
        if (distance <= 0) return 0;

        // Convert FOV to radians and calculate screen space size
        float fovRad = (float) Math.toRadians(fov);
        return (objectRadius * 2) / (float) Math.tan(fovRad / 2) / distance;
    }

    /**
     * Get LOD level based on screen space size
     * Higher = more distant = simpler mesh
     */
    public static int getLODLevel(float screenSpaceSize) {
        if (screenSpaceSize > 0.5f) return 0;      // LOD 0: Full detail
        if (screenSpaceSize > 0.2f) return 1;      // LOD 1: Medium detail
        if (screenSpaceSize > 0.05f) return 2;     // LOD 2: Low detail
        return 3;                                  // LOD 3: Very low detail (billboard)
    }

    /**
     * Check if object is in camera frustum
     * Frustum is the pyramid of vision from the camera
     */
    public static boolean isInFrustum(
            Projection.Vector3f objectPosition,
            float objectRadius,
            Projection.Vector3f cameraPos,
            Projection.Vector3f cameraTarget,
            float fov,
            float aspectRatio,
            float nearPlane,
            float farPlane) {

        // Distance from camera
        Projection.Vector3f toObject = objectPosition.subtract(cameraPos);
        float distance = toObject.length();

        // Check near/far planes
        if (distance - objectRadius > farPlane || distance + objectRadius < nearPlane) {
            return false;
        }

        // Check if within FOV cone
        Projection.Vector3f cameraDir = cameraTarget.subtract(cameraPos).normalize();
        float cosHalfFOV = (float) Math.cos(Math.toRadians(fov / 2));
        float dotProduct = toObject.normalize().dot(cameraDir);

        if (dotProduct < cosHalfFOV) {
            return false;  // Outside FOV cone
        }

        return true;
    }

    /**
     * Calculate approximate pixel footprint for prioritization
     */
    public static float calculatePixelFootprint(
            Projection.Vector3f objectCenter,
            float objectSize,
            Projection.Vector3f cameraPos,
            int screenWidth) {

        float distance = objectCenter.distance(cameraPos);
        if (distance <= 0) return Float.MAX_VALUE;

        return (objectSize * screenWidth) / distance;
    }

    /**
     * Estimate vertex count for object
     */
    public static int estimateVertexCount(
            double width, double height, double depth,
            String meshType) {

        switch (meshType.toUpperCase()) {
            case "BOX":
                return 8;  // 8 corners
            case "SPHERE":
                return 162;  // 9x18 segments = 162 vertices
            case "CYLINDER":
                return 34;   // 16 around + 2 centers
            case "TABLE":
            case "DESK":
                return 12;   // Simplified
            case "SOFA":
                return 20;   // Multiple cushions
            case "CHAIR":
                return 14;   // Seat + back
            case "BED":
                return 8;    // Simple box
            default:
                return 8;
        }
    }

    /**
     * Estimate face count for object
     */
    public static int estimateFaceCount(
            double width, double height, double depth,
            String meshType) {

        switch (meshType.toUpperCase()) {
            case "BOX":
                return 12;  // 2 per side × 6 sides
            case "SPHERE":
                return 324; // 9×18×2 triangles
            case "CYLINDER":
                return 64;  // Side + 2 caps
            case "TABLE":
            case "DESK":
                return 24;  // Simplified
            case "SOFA":
                return 40;  // Multiple surfaces
            case "CHAIR":
                return 28;  // Seat + back + legs
            case "BED":
                return 12;  // Simple box
            default:
                return 12;
        }
    }

    /**
     * Get recommended update frequency based on change rate
     * Returns milliseconds between updates
     */
    public static int getUpdateFrequency(float fps, boolean hasChanges) {
        if (hasChanges) {
            return 16;  // Update every frame if changed
        }

        if (fps > 50) {
            return 33;  // Update every other frame if high FPS
        } else if (fps > 30) {
            return 50;  // Update every 3rd frame
        } else {
            return 100; // Update less frequently if low FPS
        }
    }

    /**
     * Check if scene is static (no changes)
     */
    public static boolean isSceneStatic(long timeSinceLastChange) {
        return timeSinceLastChange > 2000;  // No changes for 2 seconds
    }

    /**
     * Get memory usage string for debugging
     */
    public static String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory();

        return String.format(
            "Memory: %d/%d MB (Max: %d MB)",
            usedMemory / 1024 / 1024,
            totalMemory / 1024 / 1024,
            maxMemory / 1024 / 1024
        );
    }
}

