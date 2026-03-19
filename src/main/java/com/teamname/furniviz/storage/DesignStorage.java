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
 * - Permanent MongoDB storage (replaces in-memory HashMap)
 */
public class DesignStorage {
    
    private DesignRepository designRepository;
    
    public DesignStorage() {
        this.designRepository = new DesignRepository();
        System.out.println("[INFO] Design Storage initialized with MongoDB backend");
    }
    
    // ========== CREATE OPERATION ==========
    
    /**
     * Save a new design
     * 
     * @param design The design to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveDesign(DesignModel design) {
        return designRepository.saveDesign(design);
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
        return designRepository.getDesignsByUserId(userId);
    }
    
    /**
     * Get a specific design by ID
     * 
     * @param designId The design ID
     * @return The design, or null if not found
     */
    public DesignModel getDesignById(String designId) {
        return designRepository.getDesignById(designId);
    }
    
    /**
     * Get all designs (admin only - use with caution!)
     * 
     * @return List of all designs
     */
    public List<DesignModel> getAllDesigns() {
        return designRepository.getAllDesigns();
    }
    
    /**
     * Search designs by name for a specific user
     * 
     * @param userId The owner's user ID
     * @param searchTerm The search term (case-insensitive)
     * @return List of matching designs
     */
    public List<DesignModel> searchByName(String userId, String searchTerm) {
        return designRepository.searchByName(userId, searchTerm);
    }
    
    /**
     * Filter designs by tags
     * 
     * @param userId The owner's user ID
     * @param tag The tag to filter by
     * @return List of designs with the tag
     */
    public List<DesignModel> filterByTag(String userId, String tag) {
        return designRepository.filterByTag(userId, tag);
    }
    
    // ========== UPDATE OPERATION ==========
    
    /**
     * Update an existing design
     * 
     * @param design The updated design
     * @return true if updated successfully, false otherwise
     */
    public boolean updateDesign(DesignModel design) {
        return designRepository.updateDesign(design);
    }
    
    // ========== DELETE OPERATION ==========
    
    /**
     * Delete a design by ID
     * 
     * @param designId The design ID to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDesign(String designId) {
        return designRepository.deleteDesign(designId);
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * Check if a design exists
     * 
     * @param designId The design ID
     * @return true if exists, false otherwise
     */
    public boolean designExists(String designId) {
        return designRepository.designExists(designId);
    }
    
    /**
     * Get count of designs for a user
     * 
     * @param userId The owner's user ID
     * @return Number of designs the user has
     */
    public int getDesignCountForUser(String userId) {
        return designRepository.getDesignCountForUser(userId);
    }
    
    /**
     * Clear all designs for a user (for testing only!)
     */
    public void clearAllDesignsForUser(String userId) {
        designRepository.deleteAllDesignsForUser(userId);
        System.out.println("[WARNING] All designs cleared for user: " + userId);
    }
}
