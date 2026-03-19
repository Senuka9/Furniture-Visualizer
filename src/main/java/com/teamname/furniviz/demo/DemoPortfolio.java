package com.teamname.furniviz.demo;

import com.teamname.furniviz.storage.*;
import com.teamname.furniviz.room.*;
import com.teamname.furniviz.furniture.*;
import java.awt.Color;
import java.util.*;

/**
 * Demo Portfolio - Test CRUD operations for Design Storage
 * 
 * Tests:
 * - Save designs
 * - Retrieve designs by user ID
 * - Search designs by name
 * - Update designs
 * - Delete designs
 * - Multi-user filtering
 */
public class DemoPortfolio {
    
    public static void main(String[] args) {
        System.out.println("\n===============================================");
        System.out.println("        PORTFOLIO SYSTEM - DEMO TEST           ");
        System.out.println("===============================================\n");
        
        // Setup
        String userId1 = "user-1";
        String userId2 = "user-2";
        DesignStorage storage = new DesignStorage();
        
        // ========== TEST 1: CREATE ROOMS ==========
        System.out.println("TEST 1: Creating test rooms...");
        Room livingRoom = new Room("Living Room", 5.0, 4.0, 
                RoomShape.RECTANGLE, Color.WHITE);
        Room bedroom = new Room("Bedroom", 4.0, 3.5, 
                RoomShape.RECTANGLE, new Color(230, 220, 200));
        Room kitchen = new Room("Kitchen", 3.5, 3.0, 
                RoomShape.RECTANGLE, new Color(255, 250, 240));
        System.out.println("✓ 3 rooms created\n");
        
        // ========== TEST 2: CREATE FURNITURE ==========
        System.out.println("TEST 2: Creating furniture items...");
        FurnitureItem sofa = new FurnitureItem("Sofa", 
                FurnitureType.SOFA, "", 2.0, 0.9, 1.0, "Fabric", "3-seater");
        sofa.setPosition(1.0, 1.0);
        
        FurnitureItem table = new FurnitureItem("Coffee Table",
                FurnitureType.TABLE, "", 1.5, 0.4, 1.0, "Wood", "Oak");
        table.setPosition(3.0, 1.5);
        
        FurnitureItem bed = new FurnitureItem("Queen Bed",
                FurnitureType.BED, "", 1.6, 0.3, 2.0, "Wood", "Oak frame");
        bed.setPosition(1.0, 0.5);
        
        FurnitureItem lamp = new FurnitureItem("Table Lamp",
                FurnitureType.LAMP, "", 0.4, 0.6, 0.4, "Metal", "LED");
        lamp.setPosition(4.0, 0.5);
        System.out.println("✓ 4 furniture items created\n");
        
        // ========== TEST 3: SAVE DESIGNS ==========
        System.out.println("TEST 3: Saving designs...");
        
        // User 1 designs
        DesignModel design1 = new DesignModel(userId1, "Cozy Living Room",
                livingRoom, Arrays.asList(sofa, table));
        design1.setTags("living room, cozy");
        design1.setNotes("My favorite living room design");
        
        DesignModel design2 = new DesignModel(userId1, "Master Bedroom",
                bedroom, Collections.singletonList(bed));
        design2.setTags("bedroom, master");
        
        // User 2 designs
        DesignModel design3 = new DesignModel(userId2, "Modern Kitchen",
                kitchen, Collections.singletonList(lamp));
        design3.setTags("kitchen, modern");
        
        boolean saved1 = storage.saveDesign(design1);
        boolean saved2 = storage.saveDesign(design2);
        boolean saved3 = storage.saveDesign(design3);
        
        if (saved1 && saved2 && saved3) {
            System.out.println("✓ All designs saved successfully\n");
        } else {
            System.out.println("✗ Error saving designs\n");
            return;
        }
        
        // ========== TEST 4: RETRIEVE BY USER ID ==========
        System.out.println("TEST 4: Retrieving designs by user ID...");
        List<DesignModel> user1Designs = storage.getDesignsByUserId(userId1);
        List<DesignModel> user2Designs = storage.getDesignsByUserId(userId2);
        
        System.out.println("User 1 has " + user1Designs.size() + " designs:");
        for (DesignModel d : user1Designs) {
            System.out.println("  - " + d.getDesignName() + 
                             " (" + d.getFurnitureItems().size() + " items)");
        }
        
        System.out.println("\nUser 2 has " + user2Designs.size() + " designs:");
        for (DesignModel d : user2Designs) {
            System.out.println("  - " + d.getDesignName() + 
                             " (" + d.getFurnitureItems().size() + " items)");
        }
        
        if (user1Designs.size() == 2 && user2Designs.size() == 1) {
            System.out.println("✓ Multi-user filtering works correctly\n");
        } else {
            System.out.println("✗ Multi-user filtering failed\n");
            return;
        }
        
        // ========== TEST 5: GET BY ID ==========
        System.out.println("TEST 5: Retrieving design by ID...");
        DesignModel retrieved = storage.getDesignById(design1.getDesignId());
        if (retrieved != null && retrieved.getDesignName().equals("Cozy Living Room")) {
            System.out.println("✓ Retrieved design: " + retrieved.getDesignName() + "\n");
        } else {
            System.out.println("✗ Failed to retrieve design\n");
            return;
        }
        
        // ========== TEST 6: SEARCH BY NAME ==========
        System.out.println("TEST 6: Searching designs by name...");
        List<DesignModel> searchResults = storage.searchByName(userId1, "Living");
        System.out.println("Found " + searchResults.size() + " design(s) matching 'Living':");
        for (DesignModel d : searchResults) {
            System.out.println("  - " + d.getDesignName());
        }
        
        if (searchResults.size() == 1 && searchResults.get(0).getDesignName().contains("Living")) {
            System.out.println("✓ Search works correctly\n");
        } else {
            System.out.println("✗ Search failed\n");
            return;
        }
        
        // ========== TEST 7: UPDATE DESIGN ==========
        System.out.println("TEST 7: Updating design...");
        design1.setDesignName("Updated Living Room Design");
        design1.setTags("living room, updated, modern");
        
        boolean updated = storage.updateDesign(design1);
        if (updated) {
            DesignModel updatedDesign = storage.getDesignById(design1.getDesignId());
            if (updatedDesign.getDesignName().equals("Updated Living Room Design")) {
                System.out.println("✓ Design updated: " + updatedDesign.getDesignName() + "\n");
            } else {
                System.out.println("✗ Update verification failed\n");
                return;
            }
        } else {
            System.out.println("✗ Failed to update design\n");
            return;
        }
        
        // ========== TEST 8: DELETE DESIGN ==========
        System.out.println("TEST 8: Deleting design...");
        String designToDelete = design3.getDesignId();
        boolean deleted = storage.deleteDesign(designToDelete);
        if (deleted) {
            DesignModel deletedDesign = storage.getDesignById(designToDelete);
            if (deletedDesign == null) {
                System.out.println("✓ Design deleted successfully\n");
            } else {
                System.out.println("✗ Design still exists after deletion\n");
                return;
            }
        } else {
            System.out.println("✗ Failed to delete design\n");
            return;
        }
        
        // ========== TEST 9: COUNT AND VERIFY ==========
        System.out.println("TEST 9: Final verification...");
        int count1 = storage.getDesignCountForUser(userId1);
        int count2 = storage.getDesignCountForUser(userId2);
        List<DesignModel> all = storage.getAllDesigns();
        
        System.out.println("User 1 designs: " + count1);
        System.out.println("User 2 designs: " + count2);
        System.out.println("Total designs: " + all.size());
        
        if (count1 == 2 && count2 == 0 && all.size() == 2) {
            System.out.println("✓ Final verification successful\n");
        } else {
            System.out.println("✗ Final verification failed\n");
            return;
        }
        
        // ========== RESULTS ==========
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║       ✓ ALL TESTS PASSED! ✓            ║");
        System.out.println("╚════════════════════════════════════════╝\n");
        
        System.out.println("Summary:");
        System.out.println("✓ Save/Load operations work");
        System.out.println("✓ Multi-user filtering works");
        System.out.println("✓ Search functionality works");
        System.out.println("✓ Update operations work");
        System.out.println("✓ Delete operations work");
        System.out.println("✓ All CRUD operations verified\n");
    }
}


