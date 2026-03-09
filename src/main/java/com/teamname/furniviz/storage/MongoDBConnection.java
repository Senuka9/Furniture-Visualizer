package com.teamname.furniviz.storage;

/**
 * MongoDB Connection Manager (Stub Implementation)
 * Placeholder for MongoDB integration
 * Currently operates in offline mode
 *
 * Prerequisites: MongoDB must be running on localhost:27017 (for future use)
 * For Docker setup: docker run -d -p 27017:27017 --name mongodb mongo:latest
 */
public class MongoDBConnection {

    private static MongoDBConnection instance;
    private boolean isConnected = false;

    private MongoDBConnection() {
        // Stub - MongoDB not initialized
        System.out.println("[INFO] MongoDB Connection initialized in stub mode");
        isConnected = false;
    }

    /**
     * Get singleton instance of MongoDB connection
     */
    public static synchronized MongoDBConnection getInstance() {
        if (instance == null) {
            instance = new MongoDBConnection();
        }
        return instance;
    }

    /**
     * Check if connection is active
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Close MongoDB connection
     */
    public void close() {
        isConnected = false;
        System.out.println("[OK] MongoDB connection closed (stub mode)");
    }

    /**
     * Create rooms collection if it doesn't exist
     */
    public void ensureCollectionsExist() {
        // Stub implementation
        System.out.println("[INFO] Collection operations skipped (stub mode)");
    }
}

