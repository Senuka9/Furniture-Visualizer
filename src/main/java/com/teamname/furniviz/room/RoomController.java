package com.teamname.furniviz.room;

import com.teamname.furniviz.app.DesignState;
import com.teamname.furniviz.storage.RoomRepository;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomController - Handles room creation, validation, editing, and state management
 *
 * Responsibilities:
 * - Room validation and business rules
 * - Room creation and editing workflows
 * - State management delegation to DesignState
 * - Simple undo/redo history (last 5 states)
 * - Integration contract exposure for 2D/3D modules
 */
public class RoomController {

    // Constants for room size constraints
    private static final double MIN_ROOM_SIZE = 1.0;
    private static final double MAX_ROOM_SIZE = 500.0;
    private static final int HISTORY_SIZE = 5;

    private DesignState designState;
    private Color selectedColor = Color.WHITE;
    private List<Room> roomHistory;
    private Room previousRoom; // For simple undo
    private RoomRepository roomRepository; // NEW: Database persistence

    public RoomController(DesignState designState) {
        this.designState = designState;
        this.roomHistory = new ArrayList<>();
        this.roomRepository = new RoomRepository(); // NEW: Initialize repository
    }

    public void setSelectedColor(Color color) {
        this.selectedColor = color;
    }

    /**
     * Validate room dimensions with detailed error messages
     * 
     * @param width Room width in units
     * @param length Room length in units
     * @return null if valid, error message string if invalid
     */
    public String validate(double width, double length) {
        // Check for minimum size
        if (width < MIN_ROOM_SIZE) {
            return String.format("Width must be at least %.1f unit", MIN_ROOM_SIZE);
        }
        if (length < MIN_ROOM_SIZE) {
            return String.format("Length must be at least %.1f unit", MIN_ROOM_SIZE);
        }

        // Check for maximum size
        if (width > MAX_ROOM_SIZE) {
            return String.format("Width exceeds maximum of %.0f units", MAX_ROOM_SIZE);
        }
        if (length > MAX_ROOM_SIZE) {
            return String.format("Length exceeds maximum of %.0f units", MAX_ROOM_SIZE);
        }

        return null; // Valid
    }

    /**
     * Check if a room is valid without throwing exceptions
     * 
     * @param width Room width in units
     * @param length Room length in units
     * @return true if room dimensions are valid
     */
    public boolean isValidRoom(double width, double length) {
        return validate(width, length) == null;
    }

    /**
     * Calculate the area of a room
     * 
     * @param width Room width in units
     * @param length Room length in units
     * @return Area in square units
     */
    public double calculateArea(double width, double length) {
        if (!isValidRoom(width, length)) {
            return 0.0;
        }
        return width * length;
    }

    /**
     * Create a new room with validation and name
     *
     * @param name Room name
     * @param width Room width in units
     * @param length Room length in units
     * @param shape Room shape (RECTANGLE, SQUARE, L_SHAPE)
     * @return The created Room object
     * @throws IllegalArgumentException if dimensions are invalid
     */
    public Room createRoom(String name, double width, double length, RoomShape shape) {
        String error = validate(width, length);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Room room = new Room(name, width, length, shape, selectedColor);
        room.setStatus(Room.RoomStatus.SAVED);

        // Store previous state for undo
        savePreviousState();

        // Delegate state management to DesignState
        designState.setRoom(room);
        designState.notifyListeners();

        // NEW: Save to MongoDB database
        roomRepository.saveRoom(room);

        return room;
    }

    /**
     * Create a new room with validation (uses default name if not provided)
     *
     * @param width Room width in units
     * @param length Room length in units
     * @param shape Room shape (RECTANGLE, SQUARE, L_SHAPE)
     * @return The created Room object
     * @throws IllegalArgumentException if dimensions are invalid
     */
    public Room createRoom(double width, double length, RoomShape shape) {
        String error = validate(width, length);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Room room = new Room(width, length, shape, selectedColor);
        room.setStatus(Room.RoomStatus.SAVED);

        // Store previous state for undo
        savePreviousState();

        // Delegate state management to DesignState
        designState.setRoom(room);
        designState.notifyListeners();

        // NEW: Save to MongoDB database
        roomRepository.saveRoom(room);

        return room;
    }

    /**
     * Create or update a room with a specific wall color
     * 
     * @param width Room width in units
     * @param length Room length in units
     * @param shape Room shape
     * @param wallColor Room wall color
     * @throws IllegalArgumentException if dimensions are invalid
     */
    public void createOrUpdateRoom(double width, double length, RoomShape shape, Color wallColor) {
        String error = validate(width, length);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        Room room = new Room(width, length, shape, wallColor);
        room.setStatus(Room.RoomStatus.SAVED);

        // Store previous state for undo
        savePreviousState();

        // Delegate state management to DesignState
        designState.setRoom(room);
        designState.notifyListeners();
    }

    /**
     * Update an existing room's properties including name
     *
     * @param room The room to update
     * @param name The new room name
     * @param width The new width
     * @param length The new length
     * @param shape The new shape
     * @param wallColor The new wall color
     * @throws IllegalArgumentException if dimensions are invalid
     */
    public void updateRoom(Room room, String name, double width, double length, RoomShape shape, Color wallColor) {
        String error = validate(width, length);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Store previous state for undo
        savePreviousState();

        // Update room properties
        room.setName(name);
        room.setWidth(width);
        room.setLength(length);
        room.setShape(shape);
        room.setWallColor(wallColor);
        room.setStatus(Room.RoomStatus.SAVED);

        // Notify listeners
        designState.notifyListeners();

        // NEW: Save updated room to MongoDB
        roomRepository.updateRoom(room);
    }

    /**
     * Update an existing room's properties
     *
     * @param room The room to update
     * @throws IllegalArgumentException if dimensions are invalid
     */
    public void updateRoom(Room room, double width, double length, RoomShape shape, Color wallColor) {
        String error = validate(width, length);
        if (error != null) {
            throw new IllegalArgumentException(error);
        }

        // Store previous state for undo
        savePreviousState();

        // Update room properties
        room.setWidth(width);
        room.setLength(length);
        room.setShape(shape);
        room.setWallColor(wallColor);
        room.setStatus(Room.RoomStatus.SAVED);

        // Notify listeners
        designState.notifyListeners();

        // NEW: Save updated room to MongoDB
        roomRepository.updateRoom(room);
    }

    /**
     * Delete the current room
     */
    public void deleteRoom() {
        if (designState.getRoom() != null) {
            Room room = designState.getRoom();
            savePreviousState();
            designState.setRoom(null);
            designState.notifyListeners();

            // NEW: Delete from MongoDB
            roomRepository.deleteRoom(room.getRoomId());
        }
    }

    /**
     * Clear/reset the current room (set to null)
     */
    public void clearRoom() {
        designState.setRoom(null);
        designState.notifyListeners();
    }

    /**
     * Undo to previous room state
     * @return true if undo was successful, false if no previous state
     */
    public boolean undo() {
        if (previousRoom != null) {
            designState.setRoom(previousRoom);
            previousRoom = designState.getRoom();
            designState.notifyListeners();
            return true;
        }
        return false;
    }

    /**
     * Get minimum allowed room size
     * @return Minimum room size in units
     */
    public static double getMinRoomSize() {
        return MIN_ROOM_SIZE;
    }

    /**
     * Get maximum allowed room size
     * @return Maximum room size in units
     */
    public static double getMaxRoomSize() {
        return MAX_ROOM_SIZE;
    }

    /**
     * Save current room state to history for undo support
     */
    private void savePreviousState() {
        Room current = designState.getRoom();
        if (current != null) {
            previousRoom = current;
        }
    }

    // ========== INTEGRATION CONTRACTS FOR 2D/3D MODULES ==========
    /**
     * Get room boundaries for 2D editor (MEMBER 4's integration point)
     * @return array [minX, minY, maxX, maxY] representing room bounds
     */
    public double[] getRoomBounds() {
        Room room = designState.getRoom();
        if (room == null || !room.isValidForRendering()) {
            return new double[]{0, 0, 0, 0};
        }
        return new double[]{0, 0, room.getWidth(), room.getLength()};
    }

    /**
     * Get room geometry for 3D renderer (MEMBER 5's integration point)
     * Provides the room's 3D representation data
     * @return Room object with all necessary geometry data
     */
    public Room getRoomGeometry() {
        return designState.getRoom();
    }

    /**
     * Check if a room exists and is ready for rendering
     * @return true if valid room exists in DesignState
     */
    public boolean hasValidRoom() {
        Room room = designState.getRoom();
        return room != null && room.isValidForRendering();
    }
}
