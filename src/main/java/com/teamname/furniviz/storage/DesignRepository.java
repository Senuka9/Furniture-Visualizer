package com.teamname.furniviz.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.teamname.furniviz.furniture.FurnitureItem;
import com.teamname.furniviz.furniture.FurnitureType;
import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.room.RoomShape;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Design Repository - MongoDB-backed CRUD operations for user designs
 * 
 * Replaces in-memory HashMap with permanent MongoDB storage
 * Manages persistence of designs with:
 * - Save/Load/Update/Delete operations
 * - Multi-user filtering (by userId)
 * - Search and filtering capabilities
 * - Full serialization/deserialization support
 */
public class DesignRepository {
    
    private static final String COLLECTION_NAME = "designs";
    private MongoCollection<Document> designsCollection;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public DesignRepository() {
        MongoDBConnection connection = MongoDBConnection.getInstance();
        this.designsCollection = connection.getCollection(COLLECTION_NAME);
        ensureCollectionsExist();
    }
    
    /**
     * Ensure designs collection exists with proper indexes
     */
    public void ensureCollectionsExist() {
        try {
            // Create unique index on designId field
            designsCollection.createIndex(
                    new Document("designId", 1),
                    new IndexOptions().unique(true)
            );
            System.out.println("[OK] Designs collection ready with unique designId index");
            
            // Create index on userId for faster queries
            try {
                designsCollection.createIndex(new Document("userId", 1));
                System.out.println("[OK] Designs collection indexed on userId");
            } catch (Exception e) {
                // Index might already exist
            }
        } catch (Exception e) {
            System.out.println("[INFO] Designs collection/indexes already exist");
        }
    }
    
    // ========== CREATE OPERATION ==========
    
    /**
     * Save a new design to MongoDB
     * 
     * @param design The design to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveDesign(DesignModel design) {
        try {
            if (design == null || design.getDesignId() == null) {
                System.err.println("[ERROR] Cannot save null design or design without ID");
                return false;
            }
            
            Document designDoc = designToDocument(design);
            designsCollection.insertOne(designDoc);
            System.out.println("[OK] Design saved to MongoDB: " + design.getDesignId() + 
                             " (User: " + design.getUserId() + ", Name: " + design.getDesignName() + ")");
            return true;
        } catch (com.mongodb.MongoWriteException e) {
            if (e.getError().getCategory().toString().contains("DUPLICATE")) {
                System.err.println("[ERROR] Design already exists: " + design.getDesignId());
            } else {
                System.err.println("[ERROR] Error saving design: " + e.getMessage());
            }
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Error saving design: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ========== READ OPERATIONS ==========
    
    /**
     * Get all designs for a specific user from MongoDB
     * 
     * @param userId The owner's user ID
     * @return List of designs belonging to the user
     */
    public List<DesignModel> getDesignsByUserId(String userId) {
        try {
            Bson filter = Filters.eq("userId", userId);
            List<DesignModel> userDesigns = new ArrayList<>();
            
            for (Document doc : designsCollection.find(filter)) {
                DesignModel design = documentToDesign(doc);
                if (design != null) {
                    userDesigns.add(design);
                }
            }
            
            System.out.println("[OK] Retrieved " + userDesigns.size() + 
                             " designs for user: " + userId);
            return userDesigns;
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving designs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get a specific design by ID from MongoDB
     * 
     * @param designId The design ID
     * @return The design, or null if not found
     */
    public DesignModel getDesignById(String designId) {
        try {
            Bson filter = Filters.eq("designId", designId);
            Document doc = designsCollection.find(filter).first();
            
            if (doc == null) {
                System.out.println("[WARNING] Design not found: " + designId);
                return null;
            }
            
            DesignModel design = documentToDesign(doc);
            System.out.println("[OK] Retrieved design: " + designId);
            return design;
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving design: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get all designs (admin only - use with caution!)
     * 
     * @return List of all designs
     */
    public List<DesignModel> getAllDesigns() {
        try {
            List<DesignModel> allDesigns = new ArrayList<>();
            
            for (Document doc : designsCollection.find()) {
                DesignModel design = documentToDesign(doc);
                if (design != null) {
                    allDesigns.add(design);
                }
            }
            
            System.out.println("[OK] Retrieved all " + allDesigns.size() + " designs");
            return allDesigns;
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving all designs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Search designs by name for a specific user
     * 
     * @param userId The owner's user ID
     * @param searchTerm The search term (case-insensitive)
     * @return List of matching designs
     */
    public List<DesignModel> searchByName(String userId, String searchTerm) {
        try {
            Bson filter = Filters.and(
                    Filters.eq("userId", userId),
                    Filters.regex("designName", searchTerm, "i")  // Case-insensitive regex
            );
            
            List<DesignModel> results = new ArrayList<>();
            for (Document doc : designsCollection.find(filter)) {
                DesignModel design = documentToDesign(doc);
                if (design != null) {
                    results.add(design);
                }
            }
            
            System.out.println("[OK] Found " + results.size() + " designs matching: " + searchTerm);
            return results;
        } catch (Exception e) {
            System.err.println("[ERROR] Error searching designs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Filter designs by tags
     * 
     * @param userId The owner's user ID
     * @param tag The tag to filter by
     * @return List of designs with the tag
     */
    public List<DesignModel> filterByTag(String userId, String tag) {
        try {
            Bson filter = Filters.and(
                    Filters.eq("userId", userId),
                    Filters.regex("tags", tag, "i")  // Case-insensitive regex
            );
            
            List<DesignModel> results = new ArrayList<>();
            for (Document doc : designsCollection.find(filter)) {
                DesignModel design = documentToDesign(doc);
                if (design != null) {
                    results.add(design);
                }
            }
            
            return results;
        } catch (Exception e) {
            System.err.println("[ERROR] Error filtering by tag: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ========== UPDATE OPERATION ==========
    
    /**
     * Update an existing design in MongoDB
     * 
     * @param design The updated design
     * @return true if updated successfully, false otherwise
     */
    public boolean updateDesign(DesignModel design) {
        try {
            if (!designExists(design.getDesignId())) {
                System.err.println("[ERROR] Design not found: " + design.getDesignId());
                return false;
            }
            
            Bson filter = Filters.eq("designId", design.getDesignId());
            Document designDoc = designToDocument(design);
            designsCollection.replaceOne(filter, designDoc);
            
            System.out.println("[OK] Design updated in MongoDB: " + design.getDesignId() + 
                             " (Name: " + design.getDesignName() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error updating design: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ========== DELETE OPERATION ==========
    
    /**
     * Delete a design from MongoDB by ID
     * 
     * @param designId The design ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDesign(String designId) {
        try {
            if (!designExists(designId)) {
                System.err.println("[ERROR] Design not found: " + designId);
                return false;
            }
            
            Bson filter = Filters.eq("designId", designId);
            designsCollection.deleteOne(filter);
            
            System.out.println("[OK] Design deleted from MongoDB: " + designId);
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting design: " + e.getMessage());
            return false;
        }
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Check if a design exists
     * 
     * @param designId The design ID
     * @return true if exists, false otherwise
     */
    public boolean designExists(String designId) {
        try {
            Bson filter = Filters.eq("designId", designId);
            return designsCollection.countDocuments(filter) > 0;
        } catch (Exception e) {
            System.err.println("[ERROR] Error checking design existence: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get count of designs for a user
     * 
     * @param userId The owner's user ID
     * @return Number of designs the user has
     */
    public int getDesignCountForUser(String userId) {
        try {
            Bson filter = Filters.eq("userId", userId);
            return (int) designsCollection.countDocuments(filter);
        } catch (Exception e) {
            System.err.println("[ERROR] Error counting designs: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Delete all designs for a specific user (use with caution!)
     * 
     * @param userId The user ID
     * @return Number of designs deleted
     */
    public int deleteAllDesignsForUser(String userId) {
        try {
            Bson filter = Filters.eq("userId", userId);
            long deletedCount = designsCollection.deleteMany(filter).getDeletedCount();
            System.out.println("[WARNING] Deleted " + deletedCount + " designs for user: " + userId);
            return (int) deletedCount;
        } catch (Exception e) {
            System.err.println("[ERROR] Error deleting user designs: " + e.getMessage());
            return 0;
        }
    }
    
    // ========== SERIALIZATION METHODS ==========
    
    /**
     * Convert DesignModel to MongoDB Document
     */
    private Document designToDocument(DesignModel design) {
        try {
            Document doc = new Document()
                    .append("_id", design.getDesignId())
                    .append("designId", design.getDesignId())
                    .append("userId", design.getUserId())
                    .append("designName", design.getDesignName())
                    .append("createdAt", convertToDate(design.getCreatedAt()))
                    .append("lastModifiedAt", convertToDate(design.getLastModifiedAt()))
                    .append("tags", design.getTags())
                    .append("notes", design.getNotes())
                    .append("thumbnail", design.getThumbnail());
            
            // Serialize room
            if (design.getRoom() != null) {
                doc.append("room", roomToDocument(design.getRoom()));
            }
            
            // Serialize furniture items
            if (design.getFurnitureItems() != null && !design.getFurnitureItems().isEmpty()) {
                List<Document> furnitureDocuments = design.getFurnitureItems().stream()
                        .map(this::furnitureToDocument)
                        .collect(Collectors.toList());
                doc.append("furnitureItems", furnitureDocuments);
            } else {
                doc.append("furnitureItems", new ArrayList<>());
            }
            
            return doc;
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting design to document: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert MongoDB Document to DesignModel
     */
    private DesignModel documentToDesign(Document doc) {
        try {
            String designId = doc.getString("designId");
            String userId = doc.getString("userId");
            String designName = doc.getString("designName");
            String tags = doc.getString("tags");
            String notes = doc.getString("notes");
            byte[] thumbnail = doc.getObjectId("thumbnail") != null ? 
                    doc.get("thumbnail", byte[].class) : null;
            
            LocalDateTime createdAt = convertFromDate(doc.getDate("createdAt"));
            LocalDateTime lastModifiedAt = convertFromDate(doc.getDate("lastModifiedAt"));
            
            // Deserialize room
            Room room = null;
            Document roomDoc = (Document) doc.get("room");
            if (roomDoc != null) {
                room = documentToRoom(roomDoc);
            }
            
            // Deserialize furniture items
            List<FurnitureItem> furnitureItems = new ArrayList<>();
            List<?> furnitureDocsList = doc.getList("furnitureItems", Document.class);
            if (furnitureDocsList != null) {
                for (Object item : furnitureDocsList) {
                    if (item instanceof Document) {
                        FurnitureItem furniture = documentToFurniture((Document) item);
                        if (furniture != null) {
                            furnitureItems.add(furniture);
                        }
                    }
                }
            }
            
            return new DesignModel(designId, userId, designName, room, 
                    furnitureItems, createdAt, lastModifiedAt, tags, notes, thumbnail);
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting document to design: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convert Room to MongoDB Document
     */
    private Document roomToDocument(Room room) {
        try {
            return new Document()
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
     * Convert MongoDB Document to Room
     */
    private Room documentToRoom(Document doc) {
        try {
            String name = doc.getString("name");
            double width = doc.getDouble("width");
            double length = doc.getDouble("length");
            String shapeStr = doc.getString("shape");
            String colorStr = doc.getString("wallColor");
            
            RoomShape shape = RoomShape.valueOf(shapeStr);
            java.awt.Color color = stringToColor(colorStr);
            
            return new Room(name, width, length, shape, color);
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting document to room: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convert FurnitureItem to MongoDB Document
     */
    private Document furnitureToDocument(FurnitureItem furniture) {
        try {
            return new Document()
                    .append("name", furniture.getName())
                    .append("type", furniture.getType().toString())
                    .append("imagePath", furniture.getImagePath())
                    .append("width", furniture.getWidth())
                    .append("height", furniture.getHeight())
                    .append("depth", furniture.getDepth())
                    .append("material", furniture.getMaterial())
                    .append("description", furniture.getDescription())
                    .append("x", furniture.getX())
                    .append("y", furniture.getY())
                    .append("rotation", furniture.getRotation())
                    .append("color", colorToString(furniture.getColor()));
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting furniture to document: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Convert MongoDB Document to FurnitureItem
     */
    private FurnitureItem documentToFurniture(Document doc) {
        try {
            String name = doc.getString("name");
            String typeStr = doc.getString("type");
            String imagePath = doc.getString("imagePath");
            double width = doc.getDouble("width");
            double height = doc.getDouble("height");
            double depth = doc.getDouble("depth");
            String material = doc.getString("material");
            String description = doc.getString("description");
            
            FurnitureType type = FurnitureType.valueOf(typeStr);
            FurnitureItem item = new FurnitureItem(name, type, imagePath, width, 
                    height, depth, material, description);
            
            // Set position and rotation
            item.setX(doc.getDouble("x"));
            item.setY(doc.getDouble("y"));
            item.setRotation(doc.getDouble("rotation"));
            
            // Set color
            String colorStr = doc.getString("color");
            item.setColor(stringToColor(colorStr));
            
            return item;
        } catch (Exception e) {
            System.err.println("[ERROR] Error converting document to furniture: " + e.getMessage());
            return null;
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
     * Convert java.util.Date to LocalDateTime
     */
    private LocalDateTime convertFromDate(Date date) {
        if (date == null) return LocalDateTime.now();
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    
    /**
     * Convert Color to String for storage
     */
    private String colorToString(java.awt.Color color) {
        if (color == null) return "RGB(150,150,150)";
        return String.format("RGB(%d,%d,%d)", color.getRed(), color.getGreen(), color.getBlue());
    }
    
    /**
     * Convert String to Color
     */
    private java.awt.Color stringToColor(String colorStr) {
        try {
            if (colorStr == null || colorStr.isEmpty()) {
                return new java.awt.Color(150, 150, 150);
            }
            // Parse "RGB(r,g,b)" format
            colorStr = colorStr.replace("RGB(", "").replace(")", "");
            String[] parts = colorStr.split(",");
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());
            return new java.awt.Color(r, g, b);
        } catch (Exception e) {
            return new java.awt.Color(150, 150, 150);
        }
    }
}


