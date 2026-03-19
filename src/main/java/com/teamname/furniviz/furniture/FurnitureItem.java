package com.teamname.furniviz.furniture;

import java.awt.Color;

/**
 * FurnitureItem - Represents a furniture instance placed in a room
 *
 * This is a concrete instance of furniture in the room design.
 * It includes position data (x, y) and can be transformed/moved.
 * Data-driven: the 3D renderer uses the type and dimensions to generate
 * the 3D shape, not PNG images.
 */
public class FurnitureItem {
    private String name;
    private FurnitureType type;
    private String imagePath; // Path to 2D preview image
    private double width;     // Width in meters
    private double height;    // Height in meters
    private double depth;     // Depth in meters
    private String material;
    private String description;
    private double x;         // X position in room (meters)
    private double y;         // Y position in room (meters)
    private double rotation;  // Rotation in degrees (0-360)
    private Color color;      // Color for 3D rendering

    public FurnitureItem(String name, FurnitureType type, String imagePath, double width, double height, double depth, String material, String description) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.material = material;
        this.description = description;
        this.x = 0.0;
        this.y = 0.0;
        this.rotation = 0.0;
        this.color = new Color(150, 150, 150); // Default gray
    }

    // ============ Getters ============
    public String getName() { return name; }
    public FurnitureType getType() { return type; }
    public String getImagePath() { return imagePath; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDepth() { return depth; }
    public String getMaterial() { return material; }
    public String getDescription() { return description; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getRotation() { return rotation; }
    public Color getColor() { return color; }

    // ============ Setters for position and transformation ============
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setRotation(double degrees) {
        this.rotation = degrees % 360;
    }

    public void setWidth(double width) {
        this.width = Math.max(0.3, width); // Minimum 0.3 meters
    }

    public void setDepth(double depth) {
        this.depth = Math.max(0.3, depth); // Minimum 0.3 meters
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name;
    }
}
