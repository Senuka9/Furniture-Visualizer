package com.teamname.furniviz.app;

import com.teamname.furniviz.room.Room;
import java.util.ArrayList;
import java.util.List;

/**
 * DesignState - Central state holder for the furniture visualizer
 *
 * Manages:
 * - Current room configuration
 * - Furniture items (future)
 * - 2D/3D view settings (future)
 *
 * This class serves as the single source of truth for all design data
 * and notifies listeners when any design element changes.
 */
public class DesignState {

    private Room room;
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
