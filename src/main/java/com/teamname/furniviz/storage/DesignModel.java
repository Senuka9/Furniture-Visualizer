package com.teamname.furniviz.storage;

import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.furniture.FurnitureItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Design Model - Represents a saved room design
 * 
 * A Design consists of:
 * - Room: The physical container (dimensions, shape, color)
 * - FurnitureItems: List of furniture placed in the room
 * - Metadata: Name, timestamps, tags, notes
 * 
 * Each design belongs to one user (multi-user support)
 */
public class DesignModel {
    
    private String designId;           // Unique identifier (UUID)
    private String userId;              // Owner of the design
    private String designName;          // User-friendly name
    private Room room;                  // The room layout
    private List<FurnitureItem> furnitureItems; // Furniture placed in room
    private LocalDateTime createdAt;    // When created
    private LocalDateTime lastModifiedAt; // When last modified
    private String tags;                // Comma-separated tags for organization
    private String notes;               // User notes about the design
    private byte[] thumbnail;           // Thumbnail preview image (optional)
    
    // ========== CONSTRUCTORS ==========
    
    /**
     * Constructor for creating a new design
     * Generates new UUID and sets timestamps
     */
    public DesignModel(String userId, String designName, Room room, 
                       List<FurnitureItem> furnitureItems) {
        this.designId = UUID.randomUUID().toString();
        this.userId = userId;
        this.designName = designName;
        this.room = room;
        this.furnitureItems = furnitureItems;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = LocalDateTime.now();
        this.tags = "";
        this.notes = "";
        this.thumbnail = null;
    }
    
    /**
     * Constructor for loading from storage
     * Used when deserializing from database
     */
    public DesignModel(String designId, String userId, String designName,
                       Room room, List<FurnitureItem> furnitureItems,
                       LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                       String tags, String notes, byte[] thumbnail) {
        this.designId = designId;
        this.userId = userId;
        this.designName = designName;
        this.room = room;
        this.furnitureItems = furnitureItems;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.tags = tags != null ? tags : "";
        this.notes = notes != null ? notes : "";
        this.thumbnail = thumbnail;
    }
    
    // ========== GETTERS ==========
    
    public String getDesignId() {
        return designId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getDesignName() {
        return designName;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public List<FurnitureItem> getFurnitureItems() {
        return furnitureItems;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
    
    public String getTags() {
        return tags;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public byte[] getThumbnail() {
        return thumbnail;
    }
    
    // ========== SETTERS ==========
    
    public void setDesignName(String designName) {
        this.designName = designName;
        updateModificationTime();
    }
    
    public void setRoom(Room room) {
        this.room = room;
        updateModificationTime();
    }
    
    public void setFurnitureItems(List<FurnitureItem> furnitureItems) {
        this.furnitureItems = furnitureItems;
        updateModificationTime();
    }
    
    public void setTags(String tags) {
        this.tags = tags != null ? tags : "";
        updateModificationTime();
    }
    
    public void setNotes(String notes) {
        this.notes = notes != null ? notes : "";
        updateModificationTime();
    }
    
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
        updateModificationTime();
    }
    
    // ========== HELPER METHODS ==========
    
    /**
     * Update the last modified timestamp
     * Called whenever any property changes
     */
    private void updateModificationTime() {
        this.lastModifiedAt = LocalDateTime.now();
    }
    
    /**
     * Get design info for display
     */
    @Override
    public String toString() {
        return "DesignModel{" +
                "designId='" + designId + '\'' +
                ", designName='" + designName + '\'' +
                ", userId='" + userId + '\'' +
                ", furnitureCount=" + (furnitureItems != null ? furnitureItems.size() : 0) +
                ", createdAt=" + createdAt +
                ", lastModifiedAt=" + lastModifiedAt +
                '}';
    }
}
