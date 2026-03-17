package com.teamname.furniviz.editor2d;

import com.teamname.furniviz.app.DesignState;
import com.teamname.furniviz.furniture.FurnitureItem;
import com.teamname.furniviz.room.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Canvas2D - 2D top-down view renderer for the room editor
 *
 * Displays:
 * - Room layout (walls and floor)
 * - All furniture items in the room
 * - Furniture labels and dimensions
 *
 * Data is read from shared DesignState.furnitureItems
 * No separate 2D-only data structures are created.
 *
 * Architecture: Renders furniture as it will be used by 3D renderer
 * - Uses x, y for position (in meters)
 * - Uses width, depth for dimensions (in meters, converted to pixels)
 * - Uses color for fill
 * - Type/name for labels
 * - Height and type are preserved for 3D renderer
 */
public class Canvas2D extends JPanel {
    private DesignState designState;
    private Room room;

    // Rendering constants
    private static final double PIXELS_PER_METER = 100.0;  // 100 pixels = 1 meter
    private static final int ROOM_OFFSET_X = 50;  // Left padding
    private static final int ROOM_OFFSET_Y = 50;  // Top padding
    private static final Color ROOM_FLOOR_COLOR = new Color(245, 245, 245);
    private static final Color ROOM_WALL_COLOR = new Color(0, 0, 0);
    private static final int WALL_THICKNESS = 3;

    // Grid overlay settings
    private boolean showGrid = true;  // Toggle for grid visibility
    private static final double GRID_SPACING_METERS = 0.5;  // Grid lines every 0.5 meters
    private static final Color GRID_COLOR = new Color(200, 200, 200, 100);  // Light gray with transparency

    // Snap-to-grid settings
    private boolean snapToGrid = false;  // Toggle for snap-to-grid behavior

    // Drag state tracking
    private FurnitureItem draggedItem = null;  // Item currently being dragged
    private int dragOffsetX = 0;               // Offset from item origin to mouse
    private int dragOffsetY = 0;               // Offset from item origin to mouse

    public Canvas2D(DesignState designState, Room room) {
        this.designState = designState;
        this.room = room;

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200, 900));

        // Add mouse listener for click, press, and drag handling
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        // Add mouse motion listener for drag handling
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });

        // Add key listener for keyboard interactions (e.g., delete)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });

        // Enable focus to receive key events
        setFocusable(true);
        requestFocusInWindow();
    }

    /**
     * Set the current room to display
     */
    public void setRoom(Room room) {
        this.room = room;
        repaint();
    }

    /**
     * Set whether the grid overlay should be shown
     *
     * @param showGrid true to show the grid, false to hide it
     */
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

    /**
     * Set whether snap-to-grid is enabled for dragging furniture
     *
     * @param snapToGrid true to enable snap-to-grid, false to disable
     */
    public void setSnapToGrid(boolean snapToGrid) {
        this.snapToGrid = snapToGrid;
    }

    /**
     * Handle mouse pressed events - start drag operation or show context menu
     *
     * When the mouse is pressed:
     * 1. Detect which furniture item is clicked (if any)
     * 2. Select the item in DesignState
     * 3. If left-click: Calculate the offset from the item's origin to the mouse position and start drag
     * 4. If right-click: Show context menu
     */
    private void handleMousePressed(MouseEvent e) {
        if (room == null) {
            return;
        }

        int clickX = e.getX();
        int clickY = e.getY();

        // Find furniture item at click point
        FurnitureItem hitItem = findFurnitureAtPoint(clickX, clickY);

        if (hitItem != null) {
            // Found an item - select it
            designState.setSelectedFurnitureItem(hitItem);

            if (e.getButton() == MouseEvent.BUTTON1) {
                // Left-click: start drag
                draggedItem = hitItem;

                // Calculate drag offset (distance from item origin to click point in pixels)
                int itemPixelX = ROOM_OFFSET_X + (int) (hitItem.getX() * PIXELS_PER_METER);
                int itemPixelY = ROOM_OFFSET_Y + (int) (hitItem.getY() * PIXELS_PER_METER);

                dragOffsetX = clickX - itemPixelX;
                dragOffsetY = clickY - itemPixelY;
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                // Right-click: show context menu
                showContextMenu(e, hitItem);
            }

            repaint();
        } else {
            // Click didn't hit any item - clear selection
            designState.clearSelection();
            draggedItem = null;
            repaint();
        }
    }

    /**
     * Handle mouse dragged events - move selected furniture
     *
     * While dragging:
     * 1. Update the selected item's x and y position in real time
     * 2. Convert pixel coordinates back to meters for DesignState
     * 3. Repaint the canvas to show live movement
     */
    private void handleMouseDragged(MouseEvent e) {
        if (draggedItem == null || room == null) {
            return;
        }

        int currentMouseX = e.getX();
        int currentMouseY = e.getY();

        // Calculate new item position by subtracting the drag offset
        int newItemPixelX = currentMouseX - dragOffsetX;
        int newItemPixelY = currentMouseY - dragOffsetY;

        // Convert pixel position back to meters (accounting for room offset)
        double newX = (newItemPixelX - ROOM_OFFSET_X) / PIXELS_PER_METER;
        double newY = (newItemPixelY - ROOM_OFFSET_Y) / PIXELS_PER_METER;

        // Snap-to-grid adjustment (if enabled)
        if (snapToGrid) {
            newX = Math.round(newX / GRID_SPACING_METERS) * GRID_SPACING_METERS;
            newY = Math.round(newY / GRID_SPACING_METERS) * GRID_SPACING_METERS;
        }

        // Clamp position to room boundaries (optional but prevents off-room placement)
        newX = Math.max(0, Math.min(newX, room.getWidth() - draggedItem.getWidth()));
        newY = Math.max(0, Math.min(newY, room.getLength() - draggedItem.getDepth()));

        // Update the item's position in DesignState
        draggedItem.setPosition(newX, newY);

        // Repaint to show live movement
        repaint();
    }

    /**
     * Handle mouse released events - finalize drag operation
     *
     * When the mouse is released:
     * 1. Clear the drag state
     * 2. Final position is already saved in the item via handleMouseDragged
     * 3. Repaint to ensure clean state
     */
    private void handleMouseReleased(MouseEvent e) {
        draggedItem = null;
        dragOffsetX = 0;
        dragOffsetY = 0;
        repaint();
    }

    /**
     * Handle key pressed events - keyboard interactions
     *
     * Supports:
     * - Delete: Remove the selected furniture item
     * - R: Rotate selected item by 45 degrees
     * - C: Duplicate selected item
     * - Arrow keys: Nudge the selected item
     */
    private void handleKeyPressed(KeyEvent e) {
        if (designState.getSelectedFurnitureItem() == null) {
            return;  // No item selected, ignore key events
        }

        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_DELETE:
                // Delete key - remove the selected item
                designState.removeFurnitureItem(designState.getSelectedFurnitureItem());
                designState.clearSelection();
                repaint();
                break;

            case KeyEvent.VK_R:
                // R key - rotate selected item by 45 degrees
                FurnitureItem selected = designState.getSelectedFurnitureItem();
                selected.setRotation(selected.getRotation() + 45);
                repaint();
                break;

            case KeyEvent.VK_C:
                // C key - duplicate selected item
                duplicateSelectedItem();
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_UP:
            case KeyEvent.VK_DOWN:
                // Arrow keys - nudge the selected item
                nudgeSelectedItem(keyCode);
                break;
        }
    }

    /**
     * Nudge the selected furniture item by a small increment
     *
     * Used for fine-tuning item placement with arrow keys.
     *
     * @param keyCode The key code of the arrow key pressed
     */
    private void nudgeSelectedItem(int keyCode) {
        FurnitureItem selectedItem = designState.getSelectedFurnitureItem();

        // Determine nudge distance (in pixels)
        int nudgeDistance = 5;

        // Convert nudge distance to meters
        double nudgeX = (keyCode == KeyEvent.VK_RIGHT ? nudgeDistance : -nudgeDistance) / PIXELS_PER_METER;
        double nudgeY = (keyCode == KeyEvent.VK_DOWN ? nudgeDistance : -nudgeDistance) / PIXELS_PER_METER;

        // Update item's position
        selectedItem.setPosition(selectedItem.getX() + nudgeX, selectedItem.getY() + nudgeY);

        // Repaint to show updated position
        repaint();
    }

    /**
     * Duplicate the currently selected furniture item
     *
     * Creates a copy of the item with the same properties and adds it to the room.
     * The new item is offset slightly to avoid exact overlap with the original.
     */
    private void duplicateSelectedItem() {
        FurnitureItem selectedItem = designState.getSelectedFurnitureItem();

        if (selectedItem == null) {
            return;  // Nothing to duplicate
        }

        // Create a new item with the same properties
        FurnitureItem newItem = new FurnitureItem(
            selectedItem.getName() + " Copy",
            selectedItem.getType(),
            selectedItem.getImagePath(),
            selectedItem.getWidth(),
            selectedItem.getHeight(),
            selectedItem.getDepth(),
            selectedItem.getMaterial(),
            selectedItem.getDescription()
        );

        // Copy position, rotation, and color
        newItem.setPosition(selectedItem.getX() + 0.1, selectedItem.getY());  // Offset by 10 cm
        newItem.setRotation(selectedItem.getRotation());
        newItem.setColor(selectedItem.getColor());

        // Add the new item to the design state
        designState.addFurnitureItem(newItem);

        // Select the new item
        designState.setSelectedFurnitureItem(newItem);

        // Repaint to show the new item
        repaint();
    }

    /**
     * Find the furniture item at the given point in pixels
     *
     * This is the main helper method for hit detection. It searches through all furniture
     * items from top to bottom (reverse order) to find which item, if any, contains the
     * given point. This method is reusable for other interactions like drag-and-drop.
     *
     * Assumptions:
     * - Rectangles are axis-aligned (rotation property is ignored for now)
     * - Items are rendered in list order, so later items appear on top
     * - Therefore, reverse iteration ensures topmost item is selected on overlap
     *
     * @param mouseX X coordinate in pixels (from mouse event)
     * @param mouseY Y coordinate in pixels (from mouse event)
     * @return The FurnitureItem at the point, or null if no item was hit
     */
    public FurnitureItem findFurnitureAtPoint(int mouseX, int mouseY) {
        // Get all furniture items from DesignState
        List<FurnitureItem> items = designState.getFurnitureItems();

        // Loop through items in reverse order (topmost items first)
        // This ensures that if items overlap, the topmost one is found first
        for (int i = items.size() - 1; i >= 0; i--) {
            FurnitureItem item = items.get(i);

            // Check if click is inside this item's bounding box
            if (isPointInsideFurniture(mouseX, mouseY, item)) {
                return item;  // Found the item - return it
            }
        }

        // No item was hit at this point
        return null;
    }

    /**
     * Check if a point (in pixels) is inside a furniture item's bounding box
     *
     * Helper method used by findFurnitureAtPoint(). Accounts for rotation by calculating
     * the axis-aligned bounding box of the rotated rectangle.
     *
     * Coordinate conversion:
     * - Item position/dimensions are stored in meters (x, y, width, depth)
     * - Mouse coordinates are in pixels
     * - This method converts item bounds to pixels for comparison
     *
     * @param pixelX X coordinate in pixels
     * @param pixelY Y coordinate in pixels
     * @param item The FurnitureItem to test against
     * @return true if the point is inside the item's rotated bounding box
     */
    private boolean isPointInsideFurniture(int pixelX, int pixelY, FurnitureItem item) {
        // ============ STEP 1: Convert item position to pixels ============
        int itemPixelX = ROOM_OFFSET_X + (int) (item.getX() * PIXELS_PER_METER);
        int itemPixelY = ROOM_OFFSET_Y + (int) (item.getY() * PIXELS_PER_METER);

        // ============ STEP 2: Calculate rotated bounding box ============
        double theta = Math.toRadians(item.getRotation());
        double cosTheta = Math.abs(Math.cos(theta));
        double sinTheta = Math.abs(Math.sin(theta));

        double halfWidth = (item.getWidth() * PIXELS_PER_METER / 2.0) * cosTheta +
                          (item.getDepth() * PIXELS_PER_METER / 2.0) * sinTheta;
        double halfHeight = (item.getWidth() * PIXELS_PER_METER / 2.0) * sinTheta +
                           (item.getDepth() * PIXELS_PER_METER / 2.0) * cosTheta;

        double boundingWidth = 2 * halfWidth;
        double boundingHeight = 2 * halfHeight;

        // ============ STEP 3: Check if point is within bounding box ============
        return pixelX >= (itemPixelX - halfWidth) &&
               pixelX <= (itemPixelX + halfWidth) &&
               pixelY >= (itemPixelY - halfHeight) &&
               pixelY <= (itemPixelY + halfHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (room == null) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
            g2d.drawString("No room selected", 50, 50);
            return;
        }

        // Draw room
        drawRoom(g2d);

        // Draw grid overlay if enabled (behind furniture)
        if (showGrid) {
            drawGrid(g2d);
        }

        // Draw all furniture items
        drawFurnitureItems(g2d);

        // Draw item count indicator
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.drawString(String.format("Furniture items: %d", designState.getFurnitureItems().size()), 60, 30);
    }

    /**
     * Draw the room layout (floor and walls)
     */
    private void drawRoom(Graphics2D g2d) {
        // Convert room dimensions from meters to pixels
        int roomWidth = (int) (room.getWidth() * PIXELS_PER_METER);
        int roomLength = (int) (room.getLength() * PIXELS_PER_METER);

        // Draw floor
        g2d.setColor(ROOM_FLOOR_COLOR);
        g2d.fillRect(ROOM_OFFSET_X, ROOM_OFFSET_Y, roomWidth, roomLength);

        // Draw walls
        g2d.setColor(ROOM_WALL_COLOR);
        g2d.setStroke(new BasicStroke(WALL_THICKNESS));
        g2d.drawRect(ROOM_OFFSET_X, ROOM_OFFSET_Y, roomWidth, roomLength);

        // Draw room dimensions labels
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2d.drawString(String.format("%.1f m", room.getWidth()),
                      ROOM_OFFSET_X + roomWidth / 2 - 20, ROOM_OFFSET_Y - 10);
        g2d.drawString(String.format("%.1f m", room.getLength()),
                      ROOM_OFFSET_X - 45, ROOM_OFFSET_Y + roomLength / 2);
    }


    /**
     * Draw all furniture items from DesignState
     */
    private void drawFurnitureItems(Graphics2D g2d) {
        List<FurnitureItem> items = designState.getFurnitureItems();

        for (FurnitureItem item : items) {
            drawFurnitureItem(g2d, item);
        }
    }

    /**
     * Draw a single furniture item in top-down view
     *
     * This method is self-contained and handles all drawing for one item:
     * - Converts position and dimensions from meters to pixels
     * - Applies rotation around the item's center
     * - Draws the colored rectangle
     * - Draws the border
     * - Draws the label and dimensions
     *
     * @param g2d Graphics2D context
     * @param item The FurnitureItem to draw
     */
    private void drawFurnitureItem(Graphics2D g2d, FurnitureItem item) {
        // ============ STEP 1: Convert meters to pixels ============
        int pixelX = ROOM_OFFSET_X + (int) (item.getX() * PIXELS_PER_METER);
        int pixelY = ROOM_OFFSET_Y + (int) (item.getY() * PIXELS_PER_METER);
        int pixelWidth = (int) (item.getWidth() * PIXELS_PER_METER);
        int pixelLength = (int) (item.getDepth() * PIXELS_PER_METER);

        // ============ STEP 2: Save current transform ============
        AffineTransform originalTransform = g2d.getTransform();

        // ============ STEP 3: Apply rotation around item center ============
        double centerX = pixelX + pixelWidth / 2.0;
        double centerY = pixelY + pixelLength / 2.0;
        g2d.rotate(Math.toRadians(item.getRotation()), centerX, centerY);

        // ============ STEP 4: Draw furniture - try image first, fallback to color ============
        boolean imageDrawn = false;
        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            try {
                java.net.URL url = getClass().getResource(item.getImagePath());
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    Image image = icon.getImage().getScaledInstance(pixelWidth, pixelLength, Image.SCALE_SMOOTH);
                    g2d.drawImage(image, pixelX, pixelY, null);
                    imageDrawn = true;
                }
            } catch (Exception e) {
                // Fallback to color
            }
        }
        if (!imageDrawn) {
            g2d.setColor(item.getColor());
            g2d.fillRect(pixelX, pixelY, pixelWidth, pixelLength);
        }

        // ============ STEP 5: Draw border - highlight if selected ============
        boolean isSelected = (item == designState.getSelectedFurnitureItem());
        if (isSelected) {
            g2d.setColor(new Color(0, 100, 200));  // Blue for selected
            g2d.setStroke(new BasicStroke(3));      // Thicker border
        } else {
            g2d.setColor(Color.BLACK);              // Black for unselected
            g2d.setStroke(new BasicStroke(1));      // Normal border
        }
        g2d.drawRect(pixelX, pixelY, pixelWidth, pixelLength);

        // ============ STEP 6: Restore original transform ============
        g2d.setTransform(originalTransform);

        // ============ STEP 7: Draw label and dimensions (without rotation) ============
        drawItemLabel(g2d, item, pixelX, pixelY, pixelWidth, pixelLength);
    }

    /**
     * Draw furniture item label/name inside or below the rectangle
     *
     * Smart placement:
     * - Centers text inside rectangle if it fits
     * - Places text below rectangle if too large
     * - Always shows dimensions below
     */
    private void drawItemLabel(Graphics2D g2d, FurnitureItem item,
                              int pixelX, int pixelY, int pixelWidth, int pixelLength) {
        // Get item label text (use name, fallback to type)
        String labelText = item.getName();
        if (labelText == null || labelText.isEmpty()) {
            labelText = item.getType().toString();
        }

        // ============ LABEL TEXT ============
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(labelText);
        int textHeight = fm.getAscent();

        // Try to center text inside rectangle if it fits
        if (textWidth < pixelWidth - 4 && textHeight < pixelLength - 4) {
            // Text fits inside - draw centered
            int textX = pixelX + (pixelWidth - textWidth) / 2;
            int textY = pixelY + ((pixelLength - textHeight) / 2) + fm.getAscent();
            g2d.drawString(labelText, textX, textY);
        } else {
            // Text doesn't fit - draw below rectangle
            int textX = pixelX + 2;
            int textY = pixelY + pixelLength + textHeight + 2;
            g2d.drawString(labelText, textX, textY);
        }

        // ============ DIMENSION TEXT ============
        String dimensionText = String.format("%.1f×%.1f m", item.getWidth(), item.getDepth());
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8));
        g2d.setColor(Color.GRAY);

        int dimTextX = pixelX + 2;
        int dimTextY = pixelY + pixelLength + textHeight + 14;
        g2d.drawString(dimensionText, dimTextX, dimTextY);
    }

    /**
     * Show a context menu for the selected furniture item
     *
     * Displays a popup menu with options to delete, rotate, or duplicate the item.
     *
     * @param e The MouseEvent that triggered the menu
     * @param item The FurnitureItem to show the menu for
     */
    private void showContextMenu(MouseEvent e, FurnitureItem item) {
        JPopupMenu menu = new JPopupMenu();

        // Delete option
        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.addActionListener(ae -> {
            designState.removeFurnitureItem(item);
            designState.clearSelection();
            repaint();
        });
        menu.add(deleteItem);

        // Rotate 90 degrees option
        JMenuItem rotateItem = new JMenuItem("Rotate 90 degrees");
        rotateItem.addActionListener(ae -> {
            item.setRotation(item.getRotation() + 90);
            repaint();
        });
        menu.add(rotateItem);

        // Duplicate option
        JMenuItem duplicateItem = new JMenuItem("Duplicate");
        duplicateItem.addActionListener(ae -> duplicateSelectedItem());
        menu.add(duplicateItem);

        // Show the menu at the mouse position
        menu.show(this, e.getX(), e.getY());
    }

    /**
     * Draw the grid overlay
     *
     * This method draws a grid on the canvas to assist with alignment and placement.
     * The grid is drawn in the background, behind all other elements.
     *
     * - Grid lines are drawn at regular intervals (GRID_SPACING_METERS) in both X and Y directions.
     * - The color and transparency of the grid lines are controlled by GRID_COLOR.
     *
     * @param g2d The Graphics2D context to draw on
     */
    private void drawGrid(Graphics2D g2d) {
        // Set grid color
        g2d.setColor(GRID_COLOR);

        // Convert grid spacing from meters to pixels
        double gridSpacingPixels = GRID_SPACING_METERS * PIXELS_PER_METER;

        // ============ STEP 1: Draw vertical grid lines ============
        for (double x = ROOM_OFFSET_X; x < getWidth(); x += gridSpacingPixels) {
            g2d.drawLine((int) x, ROOM_OFFSET_Y, (int) x, ROOM_OFFSET_Y + (int) (room.getLength() * PIXELS_PER_METER));
        }

        // ============ STEP 2: Draw horizontal grid lines ============
        for (double y = ROOM_OFFSET_Y; y < getHeight(); y += gridSpacingPixels) {
            g2d.drawLine(ROOM_OFFSET_X, (int) y, ROOM_OFFSET_X + (int) (room.getWidth() * PIXELS_PER_METER), (int) y);
        }
    }
}
