package com.teamname.furniviz.storage;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;

/**
 * MongoDB Connection Manager
 * Singleton pattern for managing MongoDB connections
 * 
 * Prerequisites: MongoDB must be running on localhost:27017
 * 
 * Setup Options:
 * 1. Local: Download from https://www.mongodb.com/try/download/community
 * 2. Docker: docker run -d -p 27017:27017 --name mongodb mongo:latest
 * 3. Atlas: https://www.mongodb.com/cloud/atlas (cloud-hosted)
 */
public class MongoDBConnection {

    private static MongoDBConnection instance;
    private MongoClient client;
    private MongoDatabase database;
    private boolean isConnected = false;

    // Configuration
    private static final String MONGO_URI = System.getenv("MONGO_URI") != null 
        ? System.getenv("MONGO_URI") 
        : "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "furniture_visualizer";
    private static final int CONNECTION_TIMEOUT_MS = 5000;

    private MongoDBConnection() {
        initializeConnection();
    }

    /**
     * Initialize MongoDB connection
     */
    private void initializeConnection() {
        try {
            System.out.println("[INFO] Connecting to MongoDB: " + MONGO_URI);
            
            // Create MongoDB client
            this.client = MongoClients.create(MONGO_URI);
            
            // Get database (creates if doesn't exist)
            this.database = client.getDatabase(DATABASE_NAME);
            
            // Test connection by running a command
            Document pingCommand = new Document("ping", 1);
            database.runCommand(pingCommand);
            
            this.isConnected = true;
            System.out.println("[OK] MongoDB connection established successfully");
            System.out.println("[OK] Database: " + DATABASE_NAME);
            
            // Ensure required collections exist
            ensureCollectionsExist();
            
        } catch (MongoException e) {
            System.err.println("[ERROR] Failed to connect to MongoDB: " + e.getMessage());
            System.err.println("[ERROR] MongoDB must be running on " + MONGO_URI);
            System.err.println("[INFO] To start MongoDB with Docker:");
            System.err.println("       docker run -d -p 27017:27017 --name mongodb mongo:latest");
            this.isConnected = false;
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error during MongoDB connection: " + e.getMessage());
            e.printStackTrace();
            this.isConnected = false;
        }
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
        if (!isConnected) {
            return false;
        }
        
        try {
            // Verify connection is still alive with a ping
            Document pingCommand = new Document("ping", 1);
            database.runCommand(pingCommand);
            return true;
        } catch (Exception e) {
            System.err.println("[WARNING] MongoDB connection lost: " + e.getMessage());
            isConnected = false;
            return false;
        }
    }

    /**
     * Get a collection from the database
     * Creates collection if it doesn't exist
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        if (!isConnected()) {
            throw new RuntimeException("MongoDB is not connected");
        }
        return database.getCollection(collectionName);
    }

    /**
     * Get the database instance
     */
    public MongoDatabase getDatabase() {
        if (!isConnected()) {
            throw new RuntimeException("MongoDB is not connected");
        }
        return database;
    }

    /**
     * Create users collection with indexes if it doesn't exist
     */
    public void ensureCollectionsExist() {
        if (!isConnected()) {
            System.err.println("[ERROR] Cannot create collections - not connected to MongoDB");
            return;
        }

        try {
            // Get users collection
            MongoCollection<Document> usersCollection = database.getCollection("users");
            
            // Create unique index on email field
            try {
                usersCollection.createIndex(
                    new Document("email", 1),
                    new IndexOptions().unique(true)
                );
                System.out.println("[OK] Users collection ready with unique email index");
            } catch (Exception e) {
                // Index might already exist, which is fine
                System.out.println("[INFO] Users collection verified");
            }

            // Get rooms collection
            MongoCollection<Document> roomsCollection = database.getCollection("rooms");
            System.out.println("[OK] Rooms collection ready");

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to ensure collections exist: " + e.getMessage());
        }
    }

    /**
     * Close MongoDB connection
     */
    public void close() {
        if (client != null) {
            try {
                client.close();
                isConnected = false;
                System.out.println("[OK] MongoDB connection closed");
            } catch (Exception e) {
                System.err.println("[ERROR] Error closing MongoDB connection: " + e.getMessage());
            }
        }
    }

    /**
     * Reconnect to MongoDB (useful if connection was lost)
     */
    public boolean reconnect() {
        System.out.println("[INFO] Attempting to reconnect to MongoDB...");
        close();
        initializeConnection();
        return isConnected();
    }

    /**
     * Get connection status details
     */
    public String getConnectionStatus() {
        if (isConnected()) {
            return "✓ Connected to " + MONGO_URI + " (Database: " + DATABASE_NAME + ")";
        } else {
            return "✗ Disconnected from " + MONGO_URI;
        }
    }
}

