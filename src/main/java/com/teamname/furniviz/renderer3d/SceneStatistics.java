package com.teamname.furniviz.renderer3d;

/**
 * SceneStatistics - Tracks rendering performance and scene metrics
 *
 * Useful for:
 * - Performance profiling
 * - Identifying bottlenecks
 * - Debug information display
 */
public class SceneStatistics {
    private int totalVertices = 0;
    private int totalFaces = 0;
    private int visibleFaces = 0;
    private int renderedFaces = 0;
    private int furnitureCount = 0;
    private long renderTimeMs = 0;
    private float fps = 0;
    private int cachedMeshes = 0;
    private float cacheHitRate = 0;

    // Tracking variables
    private long frameStartTime = 0;
    private int frameCount = 0;
    private long fpsUpdateTime = 0;

    /**
     * Start frame timing
     */
    public void startFrame() {
        frameStartTime = System.currentTimeMillis();
        totalVertices = 0;
        totalFaces = 0;
        visibleFaces = 0;
        renderedFaces = 0;
    }

    /**
     * End frame timing and update FPS
     */
    public void endFrame() {
        renderTimeMs = System.currentTimeMillis() - frameStartTime;
        frameCount++;

        long now = System.currentTimeMillis();
        if (now - fpsUpdateTime >= 1000) {
            fps = frameCount;
            frameCount = 0;
            fpsUpdateTime = now;
        }
    }

    /**
     * Get comprehensive statistics string
     */
    public String getStatisticsString() {
        return String.format(
            "FPS: %.1f | Render: %dms | Verts: %d | Faces: %d/%d | Furniture: %d | Cache: %d (%.1f%%)",
            fps, renderTimeMs, totalVertices, renderedFaces, totalFaces, 
            furnitureCount, cachedMeshes, cacheHitRate
        );
    }

    /**
     * Get detailed statistics for debugging
     */
    public String getDetailedStatistics() {
        return String.format(
            "=== Scene Statistics ===\n" +
            "FPS: %.1f\n" +
            "Frame Time: %dms\n" +
            "Total Vertices: %d\n" +
            "Total Faces: %d\n" +
            "Visible Faces: %d\n" +
            "Rendered Faces: %d (%.1f%%)\n" +
            "Furniture Items: %d\n" +
            "Cached Meshes: %d\n" +
            "Cache Hit Rate: %.1f%%",
            fps, renderTimeMs, totalVertices, totalFaces, visibleFaces,
            renderedFaces, (float)renderedFaces/totalFaces*100,
            furnitureCount, cachedMeshes, cacheHitRate
        );
    }

    // Setters for tracking
    public void setTotalVertices(int count) { this.totalVertices = count; }
    public void setTotalFaces(int count) { this.totalFaces = count; }
    public void setVisibleFaces(int count) { this.visibleFaces = count; }
    public void setRenderedFaces(int count) { this.renderedFaces = count; }
    public void setFurnitureCount(int count) { this.furnitureCount = count; }
    public void setCachedMeshes(int count) { this.cachedMeshes = count; }
    public void setCacheHitRate(float rate) { this.cacheHitRate = rate; }

    // Getters
    public int getTotalVertices() { return totalVertices; }
    public int getTotalFaces() { return totalFaces; }
    public int getVisibleFaces() { return visibleFaces; }
    public int getRenderedFaces() { return renderedFaces; }
    public int getFurnitureCount() { return furnitureCount; }
    public long getRenderTimeMs() { return renderTimeMs; }
    public float getFPS() { return fps; }
    public int getCachedMeshes() { return cachedMeshes; }
    public float getCacheHitRate() { return cacheHitRate; }
}

