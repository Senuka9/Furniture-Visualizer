package com.teamname.furniviz.room;

import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Room Model - Represents a room in the furniture visualizer
 * Contains all properties needed for 2D/3D rendering and serialization
 */
public class Room {

    public enum RoomStatus {
        DRAFT,      // Not yet saved
        SAVED,      // Saved to DesignState
        MODIFIED    // Was saved but has been edited
    }

    private String name;
    private double width;
    private double length;
    private RoomShape shape;
    private Color wallColor;
    private RoomStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private String roomId;

    public Room(double width, double length, RoomShape shape, Color wallColor) {
        this(null, width, length, shape, wallColor);
    }

    public Room(String name, double width, double length, RoomShape shape, Color wallColor) {
        this.name = name != null ? name : "Unnamed Room";
        this.width = width;
        this.length = length;
        this.shape = shape;
        this.wallColor = wallColor;
        this.status = RoomStatus.DRAFT;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.roomId = generateRoomId();
    }

    // ========== GETTERS ==========
    public String getName() { return name; }
    public double getWidth() { return width; }
    public double getLength() { return length; }
    public RoomShape getShape() { return shape; }
    public Color getWallColor() { return wallColor; }
    public RoomStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getLastModifiedAt() { return lastModifiedAt; }
    public String getRoomId() { return roomId; }

    // ========== SETTERS ==========
    public void setName(String name) {
        this.name = name != null && !name.trim().isEmpty() ? name : "Unnamed Room";
        updateModificationTime();
    }
    public void setWidth(double width) {
        this.width = width;
        updateModificationTime();
    }

    public void setLength(double length) {
        this.length = length;
        updateModificationTime();
    }

    public void setShape(RoomShape shape) {
        this.shape = shape;
        updateModificationTime();
    }

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
        updateModificationTime();
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
        if (status == RoomStatus.SAVED) {
            this.lastModifiedAt = LocalDateTime.now();
        }
    }

    // ========== HELPER METHODS ==========
    private void updateModificationTime() {
        this.lastModifiedAt = LocalDateTime.now();
        if (this.status == RoomStatus.SAVED) {
            this.status = RoomStatus.MODIFIED;
        }
    }

    private String generateRoomId() {
        return "ROOM_" + System.nanoTime();
    }

    /**
     * Get room area in square units
     * @return width * length
     */
    public double getArea() {
        return width * length;
    }

    /**
     * Check if room is in a valid state for 2D/3D rendering
     * @return true if room has valid dimensions
     */
    public boolean isValidForRendering() {
        return width > 0 && length > 0 && shape != null;
    }

    /**
     * Get room description for UI display
     * @return formatted string with room name, dimensions and status
     */
    public String getDescription() {
        return String.format("%s | %.1f×%.1f %s [%s]", name, width, length, shape.toString(), status.toString());
    }

    /**
     * Reset room to default state (for testing/clearing)
     */
    public void reset() {
        this.width = 0;
        this.length = 0;
        this.shape = RoomShape.RECTANGLE;
        this.wallColor = Color.WHITE;
        this.status = RoomStatus.DRAFT;
    }
}
