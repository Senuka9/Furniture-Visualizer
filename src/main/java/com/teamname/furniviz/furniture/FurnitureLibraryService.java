package com.teamname.furniviz.furniture;

import com.teamname.furniviz.app.DesignState;

/**
 * FurnitureLibraryService - Integration layer for furniture module
 *
 * Responsibilities:
 * - Track currently selected furniture item
 * - Communicate with DesignState for 2D/3D integration
 * - Handle furniture placement workflows
 * - Notify listeners when furniture selection changes
 */
public class FurnitureLibraryService {

    private FurnitureItem selectedFurniture;
    private DesignState designState;
    private FurnitureSelectionListener selectionListener;

    public interface FurnitureSelectionListener {
        void onFurnitureSelected(FurnitureItem item);
        void onAddToRoom(FurnitureItem item);
        void onView3D(FurnitureItem item);
    }

    public FurnitureLibraryService(DesignState designState) {
        this.designState = designState;
        this.selectedFurniture = null;
    }

    /**
     * Set the currently selected furniture item
     * Notifies listeners of the selection change
     */
    public void selectFurniture(FurnitureItem item) {
        this.selectedFurniture = item;
        if (selectionListener != null) {
            selectionListener.onFurnitureSelected(item);
        }
    }

    /**
     * Get the currently selected furniture item
     */
    public FurnitureItem getSelectedFurniture() {
        return selectedFurniture;
    }

    /**
     * Add selected furniture to the current room design
     * This should trigger a navigation to the 2D editor with furniture pre-selected
     */
    public void addSelectedToRoom() {
        if (selectedFurniture != null && selectionListener != null) {
            selectionListener.onAddToRoom(selectedFurniture);
        }
    }

    /**
     * View selected furniture in 3D
     * This should trigger a navigation to the 3D viewer
     */
    public void view3D() {
        if (selectedFurniture != null && selectionListener != null) {
            selectionListener.onView3D(selectedFurniture);
        }
    }

    /**
     * Register a listener for furniture selection events
     */
    public void setSelectionListener(FurnitureSelectionListener listener) {
        this.selectionListener = listener;
    }

    /**
     * Get the DesignState for coordination with other modules
     */
    public DesignState getDesignState() {
        return designState;
    }
}

