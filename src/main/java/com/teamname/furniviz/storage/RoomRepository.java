package com.teamname.furniviz.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.room.RoomShape;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Room Repository - MongoDB-backed CRUD operations for room templates
 * 
 * Replaces in-memory HashMap with permanent MongoDB storage
 * Handles serialization/deserialization of Room objects
 */
public class RoomRepository {

    private static final String COLLECTION_NAME = "rooms";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private MongoCollection<Document> roomsCollection;

    public RoomRepository() {
        MongoDBConnection connection = MongoDBConnection.getInstance();
        this.roomsCollection = connection.getCollection(COLLECTION_NAME);
        ensureCollectionsExist();
    }

    /**
     * Ensure rooms collection exists with proper indexes
     */
    public void ensureCollectionsExist() {
        try {
            // Create unique index on roomId field
            roomsCollection.createIndex(
                    new Document("roomId", 1),
                    new IndexOptions().unique(true)
            );
            System.out.println("[OK] Rooms collection ready with unique roomId index");
        } catch (Exception e) {
            System.out.println("[INFO] Rooms collection/indexes already exist");
        }
    }

    /**
     * Save a new room template to MongoDB
     */
    public boolean saveRoom(Room room) {
        try {
            if (room == null || room.getRoomId() == null) {
                System.err.println("[ERROR] Cannot save null room or room without ID");
                return false;
            }

            Document roomDoc = roomToDocument(room);
            roomsCollection.insertOne(roomDoc);
            System.out.println("[OK] Room saved to MongoDB: " + room.getRoomId() + " (" + room.getName() + ")");
            return true;
        } catch (com.mongodb.MongoWriteException e) {
            if (e.getError().getCategory().toString().contains("DUPLICATE")) {
                System.err.println("[ERROR] Room already exists: " + room.getRoomId());
            } else {
                System.err.println("[ERROR] Error saving room: " + e.getMessage());
            }
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Error saving room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update an existing room template in MongoDB
     */
    public boolean updateRoom(Room room) {
        try {
            if (!roomExists(room.getRoomId())) {
                System.err.println("[ERROR] Room not found: " + room.getRoomId());
                return false;
            }

            Bson filter = Filters.eq("roomId", room.getRoomId());
            Document roomDoc = roomToDocument(room);
            roomsCollection.replaceOne(filter, roomDoc);

            System.out.println("[OK] Room updated in MongoDB: " + room.getRoomId());
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a room template by ID from MongoDB
     */
    public boolean deleteRoom(String roomId) {
        try {
            if (!roomExists(roomId)) {
                System.err.println("[ERROR] Room not found: " + roomId);
                return false;
            }

            Bson filter = Filters.eq("roomId", roomId);
            roomsCollection.deleteOne(filter);

            System.out.println("[OK] Room deleted from MongoDB: " + roomId);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all room templates from MongoDB
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try {
            for (Document doc : roomsCollection.find()) {
                Room room = documentToRoom(doc);
                if (room != null) {
                    rooms.add(room);
                }
            }
            System.out.println("[OK] Retrieved " + rooms.size() + " rooms from MongoDB");
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving rooms: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Get a specific room by ID from MongoDB
     */
    public Room getRoomById(String roomId) {
        try {
            Bson filter = Filters.eq("roomId", roomId);
            Document doc = roomsCollection.find(filter).first();

            if (doc == null) {
                System.out.println("[WARNING] Room not found: " + roomId);
                return null;
            }

            Room room = documentToRoom(doc);
            System.out.println("[OK] Retrieved room: " + roomId);
            return room;
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving room: " + e.getMessage());
            return null;
        }
    }

    /**
     * Serialize Room object to MongoDB Document
     */
    private Document roomToDocument(Room room) {
        try {
            return new Document()
                    .append("_id", room.getRoomId())
                    .append("roomId", room.getRoomId())
                    .append("name", room.getName())
                    .append("width", room.getWidth())
                    .append("length", room.getLength())
                    .append("shape", room.getShape().toString())
                    .append("wallColor", colorToString(room.getWallColor()))
                    .append("status", room.getStatus().toString())
                    .append("createdAt", convertToDate(room.getCreatedAt()))
                    .append("lastModifiedAt", convertToDate(room.getLastModifiedAt()));
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting room to document: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deserialize MongoDB Document to Room object
     */
    private Room documentToRoom(Document doc) {
        try {
            String name = doc.getString("name");
            double width = doc.getDouble("width");
            double length = doc.getDouble("length");
            String shapeStr = doc.getString("shape");
            String colorStr = doc.getString("wallColor");

            RoomShape shape = RoomShape.valueOf(shapeStr);
            Color color = stringToColor(colorStr);

            Room room = new Room(name, width, length, shape, color);
            room.setStatus(Room.RoomStatus.valueOf(doc.getString("status")));

            return room;
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting document to room: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert Color to String for storage
     */
    private String colorToString(Color color) {
        if (color == null) return "RGB(255,255,255)";
        return String.format("RGB(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Convert String to Color
     */
    private Color stringToColor(String colorStr) {
        try {
            if (colorStr == null || colorStr.isEmpty()) {
                return Color.WHITE;
            }
            // Parse "RGB(r,g,b)" format
            colorStr = colorStr.replace("RGB(", "").replace(")", "");
            String[] parts = colorStr.split(",");
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());
            return new Color(r, g, b);
        } catch (Exception e) {
            return Color.WHITE;
        }
    }

    /**
     * Convert LocalDateTime to java.util.Date for MongoDB
     */
    private Date convertToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Check if a room exists
     */
    public boolean roomExists(String roomId) {
        try {
            Bson filter = Filters.eq("roomId", roomId);
            return roomsCollection.countDocuments(filter) > 0;
        } catch (Exception e) {
            System.err.println("[ERROR] Error checking room existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get count of all rooms
     */
    public long getRoomCount() {
        try {
            return roomsCollection.countDocuments();
        } catch (Exception e) {
            System.err.println("[ERROR] Error counting rooms: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Delete all rooms (for testing/reset)
     */
    public void deleteAllRooms() {
        try {
            roomsCollection.deleteMany(new Document());
            System.out.println("[OK] All rooms deleted from MongoDB");
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting all rooms: " + e.getMessage());
        }
    }

    /**
     * Serialize Room object to JSON string (for export/backup)
     */
    public String roomToJson(Room room) {
        return gson.toJson(room);
    }

    /**
     * Deserialize JSON string to Room object
     */
    public Room jsonToRoom(String json) {
        try {
            return gson.fromJson(json, Room.class);
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting JSON to room: " + e.getMessage());
            return null;
        }
    }
}
