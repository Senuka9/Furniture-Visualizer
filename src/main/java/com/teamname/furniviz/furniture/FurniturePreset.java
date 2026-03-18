package com.teamname.furniviz.furniture;

import java.awt.Color;

/**
 * FurniturePreset - Represents a preset furniture item in the library
 *
 * This is a "template" or "blueprint" for furniture that users can add to their room.
 * It contains all the metadata needed to display the item in the library and
 * eventually convert it to a FurnitureItem when placed in the room.
 *
 * Unlike FurnitureItem which is a placed instance in the room, FurniturePreset
 * is library data that can be reused multiple times.
 */
public class FurniturePreset {
    private String name;
    private FurnitureType type;
    private double width;
    private double depth;
    private double height;
    private String material;
    private String description;
    private String iconPath;           // Path to thumbnail image (50x50px or larger)
    private String previewPath;        // Path to preview image (200x200px)
    private Color defaultColor;        // Default color for 3D rendering

    public FurniturePreset(
            String name,
            FurnitureType type,
            double width,
            double depth,
            double height,
            String material,
            String description,
            String iconPath,
            String previewPath,
            Color defaultColor) {
        this.name = name;
        this.type = type;
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.material = material;
        this.description = description;
        this.iconPath = iconPath;
        this.previewPath = previewPath;
        this.defaultColor = defaultColor;
    }

    // ============ Getters ============
    public String getName() { return name; }
    public FurnitureType getType() { return type; }
    public double getWidth() { return width; }
    public double getDepth() { return depth; }
    public double getHeight() { return height; }
    public String getMaterial() { return material; }
    public String getDescription() { return description; }
    public String getIconPath() { return iconPath; }
    public String getPreviewPath() { return previewPath; }
    public Color getDefaultColor() { return defaultColor; }

    /**
     * Convert this preset to a FurnitureItem for placing in the room
     * @param x X position in room (in meters)
     * @param y Y position in room (in meters)
     * @return A new FurnitureItem instance based on this preset
     */
    public FurnitureItem createFurnitureItem(double x, double y) {
        FurnitureItem item = new FurnitureItem(
            this.name,
            this.type,
            this.previewPath,  // Use preview path for the item's display image
            this.width,
            this.height,
            this.depth,
            this.material,
            this.description
        );
        // Note: x, y positioning will be handled by FurnitureItem when needed
        return item;
    }

    @Override
    public String toString() {
        return name;
    }
}

