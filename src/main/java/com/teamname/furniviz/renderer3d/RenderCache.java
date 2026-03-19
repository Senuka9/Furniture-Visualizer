package com.teamname.furniviz.renderer3d;

import com.teamname.furniviz.furniture.FurnitureItem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * RenderCache - Caches generated meshes to avoid redundant computation
 *
 * Benefits:
 * - Reduced CPU overhead when scene doesn't change
 * - Faster frame rendering
 * - Scalable to many furniture items
 *
 * The cache automatically invalidates entries when furniture properties change
 */
public class RenderCache {

    /**
     * Cache entry metadata
     */
    private static class CacheEntry {
        MeshFactory.Mesh mesh;
        long lastUsed;
        
        CacheEntry(MeshFactory.Mesh mesh) {
            this.mesh = mesh;
            this.lastUsed = System.currentTimeMillis();
        }
    }

    // Cache storage: key = furniture hash, value = cached mesh
    private Map<String, CacheEntry> meshCache = new HashMap<>();
    
    // Cache statistics
    private int cacheHits = 0;
    private int cacheMisses = 0;
    private int totalCacheSize = 0;
    private static final int MAX_CACHE_SIZE = 500;  // Maximum entries

    /**
     * Generate cache key for a furniture item
     * Key includes all properties that affect mesh generation
     */
    private String generateCacheKey(String furnitureType, double width, double height, 
                                   double depth, Color color) {
        return String.format("%s_%.2f_%.2f_%.2f_%d_%d_%d",
            furnitureType.toUpperCase(),
            width, height, depth,
            color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Generate cache key from FurnitureItem
     */
    private String generateCacheKey(FurnitureItem item) {
        return generateCacheKey(
            item.getType().toString(),
            item.getWidth(),
            item.getHeight(),
            item.getDepth(),
            item.getColor()
        );
    }

    /**
     * Get or create furniture mesh with caching
     * 
     * @param item The furniture item
     * @return Cached or newly created mesh
     */
    public MeshFactory.Mesh getFurnitureMesh(FurnitureItem item) {
        String key = generateCacheKey(item);
        
        // Check if in cache
        if (meshCache.containsKey(key)) {
            CacheEntry entry = meshCache.get(key);
            entry.lastUsed = System.currentTimeMillis();
            cacheHits++;
            return entry.mesh;
        }
        
        // Not in cache, create new mesh
        cacheMisses++;
        MeshFactory.Mesh mesh = MeshFactory.createFurnitureByType(
            item.getType().toString(),
            item.getWidth(),
            item.getHeight(),
            item.getDepth(),
            item.getColor()
        );
        
        // Add to cache
        addToCache(key, mesh);
        return mesh;
    }

    /**
     * Get or create box mesh with caching
     */
    public MeshFactory.Mesh getBoxMesh(double width, double height, double depth, Color color) {
        String key = generateCacheKey("BOX", width, height, depth, color);
        
        if (meshCache.containsKey(key)) {
            CacheEntry entry = meshCache.get(key);
            entry.lastUsed = System.currentTimeMillis();
            cacheHits++;
            return entry.mesh;
        }
        
        cacheMisses++;
        MeshFactory.Mesh mesh = MeshFactory.createBox(width, height, depth, color);
        addToCache(key, mesh);
        return mesh;
    }

    /**
     * Get or create plane mesh with caching
     */
    public MeshFactory.Mesh getPlaneMesh(double width, double depth, Color color) {
        String key = String.format("PLANE_%.2f_%.2f_%d_%d_%d",
            width, depth, color.getRed(), color.getGreen(), color.getBlue());
        
        if (meshCache.containsKey(key)) {
            CacheEntry entry = meshCache.get(key);
            entry.lastUsed = System.currentTimeMillis();
            cacheHits++;
            return entry.mesh;
        }
        
        cacheMisses++;
        MeshFactory.Mesh mesh = MeshFactory.createPlane(width, depth, color);
        addToCache(key, mesh);
        return mesh;
    }

    /**
     * Add mesh to cache, with eviction if necessary
     */
    private void addToCache(String key, MeshFactory.Mesh mesh) {
        // Evict least recently used entry if cache is full
        if (totalCacheSize >= MAX_CACHE_SIZE) {
            evictLRUEntry();
        }
        
        meshCache.put(key, new CacheEntry(mesh));
        totalCacheSize++;
    }

    /**
     * Evict least recently used entry
     */
    private void evictLRUEntry() {
        String lruKey = null;
        long oldestTime = Long.MAX_VALUE;
        
        for (String key : meshCache.keySet()) {
            long time = meshCache.get(key).lastUsed;
            if (time < oldestTime) {
                oldestTime = time;
                lruKey = key;
            }
        }
        
        if (lruKey != null) {
            meshCache.remove(lruKey);
            totalCacheSize--;
        }
    }

    /**
     * Clear entire cache
     */
    public void clear() {
        meshCache.clear();
        totalCacheSize = 0;
        cacheHits = 0;
        cacheMisses = 0;
    }

    /**
     * Get cache hit rate as percentage
     */
    public float getCacheHitRate() {
        int total = cacheHits + cacheMisses;
        if (total == 0) return 0;
        return (float) cacheHits / total * 100;
    }

    /**
     * Get cache statistics string for debugging
     */
    public String getStatistics() {
        int total = cacheHits + cacheMisses;
        float hitRate = total > 0 ? (float) cacheHits / total * 100 : 0;
        return String.format("Cache: %d/%d hits (%.1f%%) | Size: %d/%d",
            cacheHits, total, hitRate, totalCacheSize, MAX_CACHE_SIZE);
    }

    public int getCacheSize() { return totalCacheSize; }
    public int getCacheHits() { return cacheHits; }
    public int getCacheMisses() { return cacheMisses; }
}

