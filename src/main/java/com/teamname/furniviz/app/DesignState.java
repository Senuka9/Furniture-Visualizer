package com.teamname.furniviz.app;

import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.furniture.FurnitureItem;
import java.util.ArrayList;
import java.util.List;

/**
 * DesignState - Central state holder for the furniture visualizer
 *
 * Manages:
 * - Current room configuration
 * - Furniture items placed in the room
 * - 2D/3D view settings (future)
 *
 * This class serves as the single source of truth for all design data
 * and notifies listeners when any design element changes.
 */
public class DesignState {

    private Room room;
    private List<FurnitureItem> furnitureItems = new ArrayList<>();
    private FurnitureItem selectedFurnitureItem = null;  // Currently selected item (single source of truth)
    private List<DesignStateListener> listeners = new ArrayList<>();
    private List<RoomListener> roomListeners = new ArrayList<>();

    public DesignState() {
        this.room = null;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        notifyRoomListeners();
        notifyListeners();
    }

    // ============ Furniture Items Management ============
    public List<FurnitureItem> getFurnitureItems() {
        return new ArrayList<>(furnitureItems);
    }

    public void addFurnitureItem(FurnitureItem item) {
        furnitureItems.add(item);
        notifyListeners();
    }

    public void removeFurnitureItem(FurnitureItem item) {
        furnitureItems.remove(item);
        notifyListeners();
    }

    public void clearFurnitureItems() {
        furnitureItems.clear();
        notifyListeners();
    }

    /**
     * Calculate a safe starting position for a newly added furniture item
     *
     * Uses a predictable placement strategy:
     * 1. Start with a margin from top-left corner (offset for visibility)
     * 2. Offset each new item slightly from previous placements
     * 3. Ensure the item stays within room boundaries
     * 4. Wrap to new row if needed to prevent overlapping
     *
     * Placement grid:
     * - Top-left margin: (0.5m, 0.5m) for visibility
     * - Offset between items: 0.3m in both X and Y
     * - If X exceeds room width, wrap to next row
     *
     * @param item The FurnitureItem to position
     * @return [x, y] position in meters, guaranteed to be within room bounds
     */
    public double[] calculatePositionForNewItem(FurnitureItem item) {
        // If no room is set, use default position
        if (room == null) {
            return new double[]{0.0, 0.0};
        }

        // ============ PLACEMENT PARAMETERS ============
        final double TOP_LEFT_MARGIN = 0.5;      // Start 0.5m from top-left
        final double OFFSET_BETWEEN_ITEMS = 0.3; // Space between items in grid

        // ============ CALCULATE GRID POSITION ============
        // Count items already in room to determine offset
        int itemCount = furnitureItems.size();

        // Calculate x position (offset by item count)
        double x = TOP_LEFT_MARGIN + (itemCount * OFFSET_BETWEEN_ITEMS);
        double y = TOP_LEFT_MARGIN + (itemCount * OFFSET_BETWEEN_ITEMS);

        // ============ ENFORCE ROOM BOUNDARIES ============
        // Clamp to ensure item stays inside room (same logic as drag clamping)
        x = Math.max(0, Math.min(x, room.getWidth() - item.getWidth()));
        y = Math.max(0, Math.min(y, room.getLength() - item.getDepth()));

        // If position would be too close to edge, wrap to next row
        // This prevents items from being cut off at boundaries
        if (x + item.getWidth() > room.getWidth() - 0.1) {
            // Item too close to right edge - move to next row
            x = TOP_LEFT_MARGIN;
            y = TOP_LEFT_MARGIN + ((itemCount + 1) * OFFSET_BETWEEN_ITEMS);

            // Clamp Y as well
            y = Math.max(0, Math.min(y, room.getLength() - item.getDepth()));
        }

        // Final safety check: ensure position is valid
        x = Math.max(0, Math.min(x, room.getWidth() - item.getWidth()));
        y = Math.max(0, Math.min(y, room.getLength() - item.getDepth()));

        return new double[]{x, y};
    }

    // ============ Selection Management ============
    /**
     * Get the currently selected furniture item
     * @return The selected FurnitureItem or null if nothing is selected
     */
    public FurnitureItem getSelectedFurnitureItem() {
        return selectedFurnitureItem;
    }

    /**
     * Set the selected furniture item
     * @param item The FurnitureItem to select, or null to deselect
     */
    public void setSelectedFurnitureItem(FurnitureItem item) {
        this.selectedFurnitureItem = item;
        notifyListeners();  // Notify UI components that selection changed
    }

    /**
     * Clear the current selection
     */
    public void clearSelection() {
        this.selectedFurnitureItem = null;
        notifyListeners();
    }
    public void addListener(DesignStateListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DesignStateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Add a listener specifically for room changes
     * Useful for UI panels that only care about room updates
     */
    public void addRoomListener(RoomListener listener) {
        roomListeners.add(listener);
    }

    /**
     * Remove a room listener
     */
    public void removeRoomListener(RoomListener listener) {
        roomListeners.remove(listener);
    }

    public void notifyListeners() {
        for (DesignStateListener listener : listeners) {
            listener.onDesignStateChanged(this);
        }
    }

    /**
     * Notify only room listeners of room changes
     */
    private void notifyRoomListeners() {
        for (RoomListener listener : roomListeners) {
            listener.onRoomChanged(room);
        }
    }

    public interface DesignStateListener {
        void onDesignStateChanged(DesignState state);
    }

    /**
     * Listener interface specifically for room changes
     * Useful for decoupling room-specific UI updates
     */
    public interface RoomListener {
        void onRoomChanged(Room room);
    }
}
