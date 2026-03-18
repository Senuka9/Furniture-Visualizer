package com.teamname.furniviz.furniture;

import java.util.List;

/**
 * FurnitureController - Manages furniture preset data
 *
 * This controller provides access to the library of furniture presets
 * and handles preset-related operations like filtering and searching.
 */
public class FurnitureController {
    private List<FurniturePreset> furniturePresets;

    public FurnitureController() {
        this.furniturePresets = FurniturePresetLibrary.getAllPresets();
    }

    public List<FurniturePreset> getFurniturePresets() {
        return furniturePresets;
    }

    public FurniturePreset getFurniturePresetByName(String name) {
        return FurniturePresetLibrary.getPresetByName(name);
    }

    public List<FurniturePreset> getFurniturePresetsByType(FurnitureType type) {
        return FurniturePresetLibrary.getPresetsByType(type);
    }
}
