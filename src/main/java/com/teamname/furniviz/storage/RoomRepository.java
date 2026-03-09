package com.teamname.furniviz.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.room.RoomShape;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Room Repository - CRUD operations for room templates
 * Uses in-memory storage with HashMap (MongoDB integration optional)
 * Handles serialization/deserialization of Room objects
 */
public class RoomRepository {

    private static final String COLLECTION_NAME = "rooms";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    // In-memory storage
    private static final Map<String, Room> roomStore = new HashMap<>();

    public RoomRepository() {
        System.out.println("Room Repository initialized with in-memory storage");
    }

    /**
     * Save a new room template
     */
    public boolean saveRoom(Room room) {
        try {
            roomStore.put(room.getRoomId(), room);
            System.out.println("[OK] Room saved: " + room.getRoomId());
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error saving room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update an existing room template
     */
    public boolean updateRoom(Room room) {
        try {
            roomStore.put(room.getRoomId(), room);
            System.out.println("[OK] Room updated: " + room.getRoomId());
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error updating room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a room template by ID
     */
    public boolean deleteRoom(String roomId) {
        try {
            roomStore.remove(roomId);
            System.out.println("[OK] Room deleted: " + roomId);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting room: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get all room templates
     */
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        try {
            rooms.addAll(roomStore.values());
            System.out.println("[OK] Retrieved " + rooms.size() + " rooms");
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving rooms: " + e.getMessage());
        }
        return rooms;
    }

    /**
     * Get a specific room by ID
     */
    public Room getRoomById(String roomId) {
        try {
            return roomStore.get(roomId);
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving room: " + e.getMessage());
        }
        return null;
    }

    /**
     * Serialize Room object to JSON string
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

    /**
     * Convert Color to String for storage
     */
    private String colorToString(Color color) {
        return String.format("RGB(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Convert String to Color
     */
    private Color stringToColor(String colorStr) {
        try {
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
     * Get count of all rooms
     */
    public long getRoomCount() {
        return roomStore.size();
    }

    /**
     * Delete all rooms (for testing/reset)
     */
    public void deleteAllRooms() {
        try {
            roomStore.clear();
            System.out.println("[OK] All rooms deleted");
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting all rooms: " + e.getMessage());
        }
    }
}

