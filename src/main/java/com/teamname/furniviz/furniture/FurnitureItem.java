package com.teamname.furniviz.furniture;

public class FurnitureItem {
    private String name;
    private FurnitureType type;
    private String imagePath; // Path to 2D image
    private double width;
    private double height;
    private double depth;
    private String material;
    private String description;

    public FurnitureItem(String name, FurnitureType type, String imagePath, double width, double height, double depth, String material, String description) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.material = material;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public FurnitureType getType() { return type; }
    public String getImagePath() { return imagePath; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getDepth() { return depth; }
    public String getMaterial() { return material; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        return name;
    }
}
