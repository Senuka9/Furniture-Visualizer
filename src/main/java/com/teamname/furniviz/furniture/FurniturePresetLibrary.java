package com.teamname.furniviz.furniture;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * FurniturePresetLibrary - Central repository of all furniture presets
 *
 * This class provides the complete library of furniture items that users
 * can add to their room. Each preset is a template with metadata for display
 * and creation of actual FurnitureItems.
 */
public class FurniturePresetLibrary {

    /**
     * Get all available furniture presets
     */
    public static List<FurniturePreset> getAllPresets() {
        List<FurniturePreset> presets = new ArrayList<>();

        // ============ CHAIRS ============
        presets.add(new FurniturePreset(
            "Wooden Chair",
            FurnitureType.CHAIR,
            1.0, 1.0, 0.9,  // width, depth, height (in meters)
            "Wood",
            "A classic wooden chair with natural finish.",
            "/images/wooden_chair.png",      // icon
            "/images/wooden_chair.png",      // preview
            new Color(139, 69, 19)           // brown
        ));

        presets.add(new FurniturePreset(
            "Office Chair",
            FurnitureType.CHAIR,
            1.2, 1.2, 1.2,
            "Plastic/Metal",
            "Ergonomic office chair with adjustable height.",
            "/images/office_chair.png",
            "/images/office_chair.png",
            new Color(64, 64, 64)            // dark gray
        ));

        // ============ TABLES ============
        presets.add(new FurniturePreset(
            "Dining Table",
            FurnitureType.TABLE,
            3.0, 1.8, 0.75,
            "Wood",
            "Large dining table for 6 people.",
            "/images/dining_table.png",
            "/images/dining_table.png",
            new Color(160, 82, 45)           // dark brown
        ));

        presets.add(new FurniturePreset(
            "Coffee Table",
            FurnitureType.TABLE,
            2.0, 1.2, 0.45,
            "Glass/Wood",
            "Modern coffee table with glass top.",
            "/images/coffee_table.png",
            "/images/coffee_table.png",
            new Color(192, 192, 192)         // light gray (glass)
        ));

        // ============ SOFAS ============
        presets.add(new FurniturePreset(
            "3-Seater Sofa",
            FurnitureType.SOFA,
            4.0, 1.8, 0.85,
            "Fabric",
            "Comfortable 3-seater sofa for relaxing.",
            "/images/sofa.png",
            "/images/sofa.png",
            new Color(101, 67, 33)           // tan
        ));

        // ============ BEDS ============
        presets.add(new FurniturePreset(
            "Queen Bed",
            FurnitureType.BED,
            3.2, 4.0, 0.8,
            "Wood",
            "Queen size bed frame with wooden structure.",
            "/images/bed.png",
            "/images/bed.png",
            new Color(101, 67, 33)           // wood tan
        ));

        // ============ DESKS ============
        presets.add(new FurniturePreset(
            "Office Desk",
            FurnitureType.DESK,
            2.4, 1.2, 0.75,
            "Wood",
            "Spacious office desk for work.",
            "/images/desk.png",
            "/images/desk.png",
            new Color(139, 90, 43)           // medium brown
        ));

        // ============ CABINETS ============
        presets.add(new FurniturePreset(
            "Bookshelf",
            FurnitureType.CABINET,
            1.6, 0.6, 1.8,
            "Wood",
            "Tall bookshelf for storing books and decorations.",
            "/images/bookshelf.png",
            "/images/bookshelf.png",
            new Color(139, 90, 43)           // medium brown
        ));

        // ============ LAMPS ============
        presets.add(new FurniturePreset(
            "Table Lamp",
            FurnitureType.LAMP,
            0.6, 0.6, 0.6,
            "Metal",
            "Modern table lamp for ambient lighting.",
            "/images/lamp.png",
            "/images/lamp.png",
            new Color(255, 215, 0)           // gold
        ));

        return presets;
    }

    /**
     * Get presets filtered by type
     */
    public static List<FurniturePreset> getPresetsByType(FurnitureType type) {
        List<FurniturePreset> filtered = new ArrayList<>();
        for (FurniturePreset preset : getAllPresets()) {
            if (preset.getType() == type) {
                filtered.add(preset);
            }
        }
        return filtered;
    }

    /**
     * Get a preset by name
     */
    public static FurniturePreset getPresetByName(String name) {
        for (FurniturePreset preset : getAllPresets()) {
            if (preset.getName().equals(name)) {
                return preset;
            }
        }
        return null;
    }
}

