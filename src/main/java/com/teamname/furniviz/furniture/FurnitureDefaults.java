package com.teamname.furniviz.furniture;

import java.util.ArrayList;
import java.util.List;

public class FurnitureDefaults {
    public static List<FurnitureItem> getDefaultFurniture() {
        List<FurnitureItem> items = new ArrayList<>();

        // Chairs
        items.add(new FurnitureItem("Wooden Chair", FurnitureType.CHAIR, "/images/wooden_chair.png", 1.0, 0.9, 1.0, "Wood", "A classic wooden chair."));
        items.add(new FurnitureItem("Office Chair", FurnitureType.CHAIR, "/images/office_chair.png", 1.2, 1.2, 1.2, "Plastic/Metal", "Ergonomic office chair."));

        // Tables
        items.add(new FurnitureItem("Dining Table", FurnitureType.TABLE, "/images/dining_table.png", 3.0, 0.75, 1.8, "Wood", "Large dining table for 6 people."));
        items.add(new FurnitureItem("Coffee Table", FurnitureType.TABLE, "/images/coffee_table.png", 2.0, 0.45, 1.2, "Glass/Wood", "Modern coffee table."));

        // Sofas
        items.add(new FurnitureItem("3-Seater Sofa", FurnitureType.SOFA, "/images/sofa.png", 4.0, 0.85, 1.8, "Fabric", "Comfortable 3-seater sofa."));

        // Beds
        items.add(new FurnitureItem("Queen Bed", FurnitureType.BED, "/images/bed.png", 3.2, 0.8, 4.0, "Wood", "Queen size bed frame."));

        // Desks
        items.add(new FurnitureItem("Office Desk", FurnitureType.DESK, "/images/desk.png", 2.4, 0.75, 1.2, "Wood", "Spacious office desk."));

        // Cabinets
        items.add(new FurnitureItem("Bookshelf", FurnitureType.CABINET, "/images/bookshelf.png", 1.6, 1.8, 0.6, "Wood", "Tall bookshelf."));

        // Lamps
        items.add(new FurnitureItem("Table Lamp", FurnitureType.LAMP, "/images/lamp.png", 0.6, 0.6, 0.6, "Metal", "Modern table lamp."));

        return items;
    }
}
