package com.teamname.furniviz.furniture;

import java.util.List;

public class FurnitureController {
    private List<FurnitureItem> furnitureItems;

    public FurnitureController() {
        this.furnitureItems = FurnitureDefaults.getDefaultFurniture();
    }

    public List<FurnitureItem> getFurnitureItems() {
        return furnitureItems;
    }

    public FurnitureItem getFurnitureItemByName(String name) {
        return furnitureItems.stream()
                .filter(item -> item.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
