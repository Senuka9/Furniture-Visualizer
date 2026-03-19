package com.teamname.furniviz.storage;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Design Storage - CRUD operations for user designs
 * 
 * Manages persistence of designs with:
 * - Save/Load/Update/Delete operations
 * - Multi-user filtering (by userId)
 * - Search and filtering capabilities
 * - In-memory HashMap storage (MongoDB integration ready)
 */
public class DesignStorage {
    
    private static final Map<String, DesignModel> designStore = new HashMap<>();
    
    public DesignStorage() {
        System.out.println("[INFO] Design Storage initialized");
    }
    
    // ========== CREATE OPERATION ==========
    
    /**
     * Save a new design
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
            designStore.put(design.getDesignId(), design);
            System.out.println("[OK] Design saved: " + design.getDesignId() + 
                             " (User: " + design.getUserId() + ", Name: " + design.getDesignName() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error saving design: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ========== READ OPERATIONS ==========
    
    /**
     * Get all designs for a specific user
     * IMPORTANT: Always filter by userId for multi-user support
     * 
     * @param userId The owner's user ID
     * @return List of designs belonging to the user
     */
    public List<DesignModel> getDesignsByUserId(String userId) {
        try {
            List<DesignModel> userDesigns = designStore.values().stream()
                    .filter(design -> design.getUserId().equals(userId))
                    .collect(Collectors.toList());
            System.out.println("[OK] Retrieved " + userDesigns.size() + 
                             " designs for user: " + userId);
            return userDesigns;
        } catch (Exception e) {
            System.err.println("[ERROR] Error retrieving designs: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get a specific design by ID
     * 
     * @param designId The design ID
     * @return The design, or null if not found
     */
    public DesignModel getDesignById(String designId) {
        try {
            DesignModel design = designStore.get(designId);
            if (design == null) {
                System.out.println("[WARNING] Design not found: " + designId);
            } else {
                System.out.println("[OK] Retrieved design: " + designId);
            }
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
            List<DesignModel> allDesigns = new ArrayList<>(designStore.values());
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
            return designStore.values().stream()
                    .filter(d -> d.getUserId().equals(userId))
                    .filter(d -> d.getDesignName().toLowerCase()
                            .contains(searchTerm.toLowerCase()))
                    .collect(Collectors.toList());
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
            return designStore.values().stream()
                    .filter(d -> d.getUserId().equals(userId))
                    .filter(d -> d.getTags().contains(tag))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("[ERROR] Error filtering by tag: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // ========== UPDATE OPERATION ==========
    
    /**
     * Update an existing design
     * 
     * @param design The updated design
     * @return true if updated successfully, false otherwise
     */
    public boolean updateDesign(DesignModel design) {
        try {
            if (!designStore.containsKey(design.getDesignId())) {
                System.err.println("[ERROR] Design not found: " + design.getDesignId());
                return false;
            }
            designStore.put(design.getDesignId(), design);
            System.out.println("[OK] Design updated: " + design.getDesignId() + 
                             " (Name: " + design.getDesignName() + ")");
            return true;
        } catch (Exception e) {
            System.err.println("[ERROR] Error updating design: " + e.getMessage());
            return false;
        }
    }
    
    // ========== DELETE OPERATION ==========
    
    /**
     * Delete a design by ID
     * 
     * @param designId The design ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDesign(String designId) {
        try {
            if (!designStore.containsKey(designId)) {
                System.err.println("[ERROR] Design not found: " + designId);
                return false;
            }
            designStore.remove(designId);
            System.out.println("[OK] Design deleted: " + designId);
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
        return designStore.containsKey(designId);
    }
    
    /**
     * Get count of designs for a user
     * 
     * @param userId The owner's user ID
     * @return Number of designs the user has
     */
    public int getDesignCountForUser(String userId) {
        return (int) designStore.values().stream()
                .filter(d -> d.getUserId().equals(userId))
                .count();
    }
    
    /**
     * Clear all designs (for testing only!)
     */
    public void clearAllDesigns() {
        designStore.clear();
        System.out.println("[WARNING] All designs cleared!");
    }
}
