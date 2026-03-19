package com.teamname.furniviz.editor2d;

import com.teamname.furniviz.app.DesignState;
import com.teamname.furniviz.furniture.*;
import com.teamname.furniviz.room.Room;

import javax.swing.*;
import javax.swing.DefaultListModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.List;

/**
 * Editor2DPanel - Main 2D editor for room design
 *
 * Features:
 * - Display room layout with walls
 * - Render furniture items
 * - Zoom in/out functionality
 * - Square grid system
 * - Room info and controls
 */
public class Editor2DPanel extends JPanel {
    private DesignState designState;
    private Room currentRoom;
    private Runnable onBackCallback;
    private Runnable onGoTo3DCallback;

    // UI Components
    private JLabel roomInfoLabel;
    private JLabel zoomLabel;
    private JButton backButton;
    private JButton view3DButton;
    private JButton addFurnitureButton;
    private JButton zoomInButton;
    private JButton zoomOutButton;
    private JButton zoomResetButton;
    private JButton gridToggleButton;
    private Canvas2D canvas2D;

    public Editor2DPanel(DesignState designState, Runnable onBackCallback, Runnable onGoTo3DCallback) {
        this.designState = designState;
        this.onBackCallback = onBackCallback;
        this.onGoTo3DCallback = onGoTo3DCallback;
        this.currentRoom = designState.getRoom();

        initUI();
        setupListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ============ TOP PANEL: Room Info and Controls ============
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: 2D Canvas ============
        canvas2D = new Canvas2D(designState, currentRoom);
        JScrollPane scrollPane = new JScrollPane(canvas2D);
        scrollPane.setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // ============ BOTTOM PANEL: Info ============
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Left: Room Info
        roomInfoLabel = new JLabel("Room: Loading...");
        roomInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (currentRoom != null) {
            roomInfoLabel.setText(String.format("Room: %s (%.1f m × %.1f m)",
                currentRoom.getName(),
                currentRoom.getWidth(),
                currentRoom.getLength()));
        }
        panel.add(roomInfoLabel, BorderLayout.WEST);

        // Center: Zoom Controls
        JPanel zoomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        zoomPanel.setBackground(new Color(240, 240, 240));

        zoomOutButton = new JButton("- Zoom Out");
        zoomOutButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        zoomOutButton.setPreferredSize(new Dimension(90, 35));
        zoomOutButton.setBackground(new Color(255, 152, 0));
        zoomOutButton.setForeground(Color.WHITE);
        zoomOutButton.setFocusPainted(false);
        zoomOutButton.setBorderPainted(false);
        zoomOutButton.addActionListener(e -> canvas2D.zoomOut());
        zoomPanel.add(zoomOutButton);

        zoomLabel = new JLabel("100%");
        zoomLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        zoomLabel.setPreferredSize(new Dimension(50, 35));
        zoomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        zoomPanel.add(zoomLabel);

        zoomInButton = new JButton("+ Zoom In");
        zoomInButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        zoomInButton.setPreferredSize(new Dimension(90, 35));
        zoomInButton.setBackground(new Color(76, 175, 80));
        zoomInButton.setForeground(Color.WHITE);
        zoomInButton.setFocusPainted(false);
        zoomInButton.setBorderPainted(false);
        zoomInButton.addActionListener(e -> canvas2D.zoomIn());
        zoomPanel.add(zoomInButton);

        zoomResetButton = new JButton("Reset");
        zoomResetButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        zoomResetButton.setPreferredSize(new Dimension(70, 35));
        zoomResetButton.setBackground(new Color(158, 158, 158));
        zoomResetButton.setForeground(Color.WHITE);
        zoomResetButton.setFocusPainted(false);
        zoomResetButton.setBorderPainted(false);
        zoomResetButton.addActionListener(e -> canvas2D.resetZoom());
        zoomPanel.add(zoomResetButton);

        gridToggleButton = new JButton("🔲 Grid: ON");
        gridToggleButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        gridToggleButton.setPreferredSize(new Dimension(100, 35));
        gridToggleButton.setBackground(new Color(76, 175, 80));
        gridToggleButton.setForeground(Color.WHITE);
        gridToggleButton.setFocusPainted(false);
        gridToggleButton.setBorderPainted(false);
        gridToggleButton.addActionListener(e -> toggleGrid());
        zoomPanel.add(gridToggleButton);

        panel.add(zoomPanel, BorderLayout.CENTER);

        // Right: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(240, 240, 240));

        backButton = new JButton("<- Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });
        buttonPanel.add(backButton);

        addFurnitureButton = new JButton("+ Add Furniture");
        addFurnitureButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        addFurnitureButton.setPreferredSize(new Dimension(130, 35));
        addFurnitureButton.setBackground(new Color(76, 175, 80));
        addFurnitureButton.setForeground(Color.WHITE);
        addFurnitureButton.setFocusPainted(false);
        addFurnitureButton.setBorderPainted(false);
        addFurnitureButton.addActionListener(e -> handleAddFurniture());
        buttonPanel.add(addFurnitureButton);

        view3DButton = new JButton("View 3D");
        view3DButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        view3DButton.setPreferredSize(new Dimension(100, 35));
        view3DButton.setBackground(new Color(33, 150, 243));
        view3DButton.setForeground(Color.WHITE);
        view3DButton.setFocusPainted(false);
        view3DButton.setBorderPainted(false);
        view3DButton.addActionListener(e -> {
            if (onGoTo3DCallback != null) {
                onGoTo3DCallback.run();
            }
        });
        buttonPanel.add(view3DButton);

        // Save Design Button
        JButton saveDesignButton = new JButton("SAVE DESIGN");
        saveDesignButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        saveDesignButton.setPreferredSize(new Dimension(130, 35));
        saveDesignButton.setBackground(new Color(156, 39, 176));
        saveDesignButton.setForeground(Color.WHITE);
        saveDesignButton.setFocusPainted(false);
        saveDesignButton.setBorderPainted(false);
        saveDesignButton.addActionListener(e -> saveCurrentDesign());
        buttonPanel.add(saveDesignButton);

        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel infoLabel = new JLabel("💡 Tip: Use Zoom buttons to adjust view • Scroll wheel also works • Grid shows 1m × 1m squares");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        panel.add(infoLabel);

        return panel;
    }

    private void setupListeners() {
        // Listen to DesignState changes to update furniture display
        designState.addListener(state -> {
            canvas2D.repaint();
        });
    }

    private void handleAddFurniture() {
        // Create a dialog to show furniture list
        JDialog furnitureDialog = new JDialog((java.awt.Window) SwingUtilities.getWindowAncestor(this), "Add Furniture to Room", Dialog.ModalityType.APPLICATION_MODAL);
        furnitureDialog.setSize(600, 500);
        furnitureDialog.setLocationRelativeTo(this);
        furnitureDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Create furniture list panel
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogPanel.setBackground(new Color(245, 245, 245));

        // Title
        JLabel titleLabel = new JLabel("Select Furniture to Add");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dialogPanel.add(titleLabel, BorderLayout.NORTH);

        // Furniture list
        FurnitureController furnitureController = new FurnitureController();
        java.util.List<FurniturePreset> furnitureItems = furnitureController.getFurniturePresets();

        DefaultListModel<FurniturePreset> listModel = new DefaultListModel<>();
        for (FurniturePreset item : furnitureItems) {
            listModel.addElement(item);
        }

        JList<FurniturePreset> furnitureList = new JList<>(listModel);
        furnitureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        furnitureList.setFixedCellHeight(70);
        furnitureList.setCellRenderer(new FurniturePresetListCellRenderer());

        JScrollPane scrollPane = new JScrollPane(furnitureList);
        scrollPane.setBackground(new Color(245, 245, 245));
        dialogPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(new Color(200, 200, 200));
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.addActionListener(e -> furnitureDialog.dispose());
        buttonPanel.add(cancelButton);

        JButton addButton = new JButton("Add to Room");
        addButton.setPreferredSize(new Dimension(130, 35));
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.addActionListener(e -> {
            FurniturePreset selected = furnitureList.getSelectedValue();
            if (selected != null) {
                // Convert FurniturePreset to FurnitureItem and add to room
                FurnitureItem item = new FurnitureItem(
                    selected.getName(),
                    selected.getType(),
                    selected.getIconPath(),
                    selected.getWidth(),
                    selected.getHeight(),
                    selected.getDepth(),
                    selected.getMaterial(),
                    selected.getDescription()
                );

                // Calculate safe position using DesignState's placement strategy
                double[] position = designState.calculatePositionForNewItem(item);
                item.setPosition(position[0], position[1]);
                item.setColor(selected.getDefaultColor());

                // Add to design state
                designState.addFurnitureItem(item);
                furnitureDialog.dispose();
                canvas2D.repaint();
            } else {
                JOptionPane.showMessageDialog(furnitureDialog, "Please select a furniture item first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        buttonPanel.add(addButton);

        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        furnitureDialog.add(dialogPanel);
        furnitureDialog.setVisible(true);
    }

    private void saveCurrentDesign() {
        // Get design name from user
        String designName = JOptionPane.showInputDialog(
                this,
                "Enter a name for this design:",
                "Save Design",
                JOptionPane.PLAIN_MESSAGE);
        
        if (designName == null || designName.trim().isEmpty()) {
            return; // User cancelled
        }
        
        try {
            // Get current data from DesignState
            Room room = designState.getRoom();
            java.util.List<FurnitureItem> items = designState.getFurnitureItems();
            
            if (room == null) {
                JOptionPane.showMessageDialog(this, "No room to save!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get user ID from Session
            String userId = com.teamname.furniviz.auth.Session.getInstance()
                    .getCurrentUser().getUserId();
            
            // Create Design object
            com.teamname.furniviz.storage.DesignModel design = 
                    new com.teamname.furniviz.storage.DesignModel(
                    userId,
                    designName,
                    room,
                    items
            );
            
            // Save to storage
            com.teamname.furniviz.storage.DesignStorage storage = 
                    new com.teamname.furniviz.storage.DesignStorage();
            if (storage.saveDesign(design)) {
                JOptionPane.showMessageDialog(this, 
                        "✓ Design '" + designName + "' saved successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error saving design",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Custom cell renderer for furniture list (same as in FurnitureLibraryPanel)
    private static class FurniturePresetListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof FurniturePreset) {
                FurniturePreset item = (FurniturePreset) value;
                setText(item.getName());
                // Load small image with fallback
                try {
                    java.net.URL url = getClass().getResource(item.getIconPath());
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(image));
                    } else {
                        setIcon(createPlaceholderIcon());
                    }
                } catch (Exception e) {
                    setIcon(createPlaceholderIcon());
                }
            }
            return this;
        }

        private ImageIcon createPlaceholderIcon() {
            java.awt.image.BufferedImage placeholder = new java.awt.image.BufferedImage(50, 50, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = placeholder.createGraphics();
            g2d.setColor(new Color(200, 200, 200));
            g2d.fillRect(0, 0, 50, 50);
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new java.awt.BasicStroke(1));
            g2d.drawRect(1, 1, 48, 48);
            g2d.dispose();
            return new ImageIcon(placeholder);
        }
    }

    public void setRoom(Room room) {
        this.currentRoom = room;
        designState.setRoom(room);
        if (room != null) {
            roomInfoLabel.setText(String.format("Room: %s (%.1f m × %.1f m)",
                room.getName(),
                room.getWidth(),
                room.getLength()));
            canvas2D.setRoom(room);
        }
        canvas2D.repaint();
    }

    public void updateZoomLabel(int zoomPercent) {
        zoomLabel.setText(zoomPercent + "%");
    }

    private void toggleGrid() {
        canvas2D.toggleGrid();
        boolean gridVisible = canvas2D.isGridVisible();
        gridToggleButton.setText(gridVisible ? "🔲 Grid: ON" : "🔲 Grid: OFF");
        gridToggleButton.setBackground(gridVisible ? new Color(76, 175, 80) : new Color(200, 0, 0));
    }

    /**
     * Inner class: Canvas2D - The actual 2D drawing surface with zoom support
     */
    private class Canvas2D extends JPanel {
        private DesignState designState;
        private Room room;
        private double zoomLevel = 1.0;  // 1.0 = 100% but will be scaled to look like 50%
        private boolean gridVisible = true;  // Grid toggle
        private static final double ZOOM_STEP = 0.1;  // 10% per zoom
        private static final double MIN_ZOOM = 0.25;   // 25% minimum (very small)
        private static final double MAX_ZOOM = 3.0;   // 300% maximum (very large)
        private static final double BASE_PIXELS_PER_METER = 100.0;  // Make furniture appear large by default
        private static final int GRID_SQUARE_SIZE = 1; // 1 meter per grid square
        private static final int OFFSET_X = 50;
        private static final int OFFSET_Y = 50;

        // ============ SELECTION STATE ============
        private FurnitureItem selectedItem = null;  // Currently selected furniture item
        private static final Color SELECTION_COLOR = new Color(0, 100, 200);  // Blue highlight
        private static final int SELECTION_BORDER_WIDTH = 3;  // Thick border for selected items
        private static final int NORMAL_BORDER_WIDTH = 1;  // Normal border for unselected items

        // Drag state
        private FurnitureItem draggedItem = null;
        private int dragOffsetX = 0;
        private int dragOffsetY = 0;

        // ============ RESIZE/ROTATE STATE ============
        private enum HandleType { MOVE, RESIZE_TL, RESIZE_TR, RESIZE_BL, RESIZE_BR, ROTATE }
        private HandleType activeHandle = null;
        private static final int HANDLE_SIZE = 8;
        private static final int ROTATE_HANDLE_DISTANCE = 30;

        public Canvas2D(DesignState designState, Room room) {
            this.designState = designState;
            this.room = room;

            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(2000, 1500));

            // Mouse wheel zoom
            addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.getWheelRotation() < 0) {
                        zoomIn();  // Scroll up = zoom in
                    } else {
                        zoomOut();  // Scroll down = zoom out
                    }
                }
            });

            // Mouse interaction for drag and drop
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        handleRightClick(e);
                    } else {
                        handleMousePressed(e);
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        handleRightClick(e);
                    } else {
                        handleMouseReleased(e);
                    }
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!e.isPopupTrigger()) {
                        handleCanvasClick(e);
                    }
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    handleMouseDragged(e);
                }
                @Override
                public void mouseMoved(MouseEvent e) {
                    handleMouseMoved(e);
                }
            });
        }

        public void setRoom(Room room) {
            this.room = room;
        }

        public void zoomIn() {
            if (zoomLevel < MAX_ZOOM) {
                zoomLevel += ZOOM_STEP;
                if (zoomLevel > MAX_ZOOM) {
                    zoomLevel = MAX_ZOOM;
                }
                updateDisplay();
            }
        }

        public void zoomOut() {
            if (zoomLevel > MIN_ZOOM) {
                zoomLevel -= ZOOM_STEP;
                if (zoomLevel < MIN_ZOOM) {
                    zoomLevel = MIN_ZOOM;
                }
                updateDisplay();
            }
        }

        public void resetZoom() {
            zoomLevel = 1.0;  // Reset to 100% (default smaller size)
            updateDisplay();
        }

        public void toggleGrid() {
            gridVisible = !gridVisible;
            repaint();
        }

        public boolean isGridVisible() {
            return gridVisible;
        }

        // ============ SELECTION METHODS ============
        /**
         * Get the currently selected furniture item
         * @return The selected FurnitureItem or null if nothing selected
         */
        public FurnitureItem getSelectedItem() {
            return selectedItem;
        }

        /**
         * Set the selected furniture item and refresh display
         * @param item The FurnitureItem to select, or null to deselect
         */
        public void setSelectedItem(FurnitureItem item) {
            this.selectedItem = item;
            repaint();  // Refresh to show selection highlight
        }

        /**
         * Deselect current item
         */
        public void clearSelection() {
            this.selectedItem = null;
            repaint();
        }

        private void updateDisplay() {
            Editor2DPanel.this.updateZoomLabel((int) (zoomLevel * 100));
            repaint();
        }

        // ============ HANDLE DETECTION ============
        /**
         * Get the handle at the given screen coordinates
         */
        private HandleType getHandleAtPosition(int screenX, int screenY, FurnitureItem item, double pixelsPerMeter) {
            if (item == null) return null;

            int offsetX = OFFSET_X;
            int offsetY = OFFSET_Y;
            int itemPixelX = offsetX + (int) (item.getX() * pixelsPerMeter);
            int itemPixelY = offsetY + (int) (item.getY() * pixelsPerMeter);
            int itemWidth = (int) (item.getWidth() * pixelsPerMeter);
            int itemLength = (int) (item.getDepth() * pixelsPerMeter);

            int centerX = itemPixelX + itemWidth / 2;
            int centerY = itemPixelY + itemLength / 2;

            // Convert screen coords to canvas coords
            int canvasX = (int) (screenX / zoomLevel);
            int canvasY = (int) (screenY / zoomLevel);

            // Rotate click position back to item's local space
            double rotationRadians = Math.toRadians(item.getRotation());
            double cos = Math.cos(-rotationRadians);
            double sin = Math.sin(-rotationRadians);
            double dx = canvasX - centerX;
            double dy = canvasY - centerY;
            double localX = dx * cos - dy * sin;
            double localY = dx * sin + dy * cos;

            // Check corners for resize handles
            int halfW = itemWidth / 2;
            int halfH = itemLength / 2;

            if (Math.abs(localX + halfW) < HANDLE_SIZE && Math.abs(localY + halfH) < HANDLE_SIZE) {
                return HandleType.RESIZE_TL;
            }
            if (Math.abs(localX - halfW) < HANDLE_SIZE && Math.abs(localY + halfH) < HANDLE_SIZE) {
                return HandleType.RESIZE_TR;
            }
            if (Math.abs(localX + halfW) < HANDLE_SIZE && Math.abs(localY - halfH) < HANDLE_SIZE) {
                return HandleType.RESIZE_BL;
            }
            if (Math.abs(localX - halfW) < HANDLE_SIZE && Math.abs(localY - halfH) < HANDLE_SIZE) {
                return HandleType.RESIZE_BR;
            }

            // Check rotate handle (above top center)
            if (Math.abs(localX) < HANDLE_SIZE && (localY + halfH + ROTATE_HANDLE_DISTANCE) < HANDLE_SIZE) {
                return HandleType.ROTATE;
            }

            // Check if in main area for move
            if (Math.abs(localX) < halfW && Math.abs(localY) < halfH) {
                return HandleType.MOVE;
            }

            return null;
        }

        private void handleMouseMoved(MouseEvent e) {
            if (selectedItem == null) return;

            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;
            HandleType handle = getHandleAtPosition(e.getX(), e.getY(), selectedItem, pixelsPerMeter);

            // Change cursor based on handle type
            if (handle == HandleType.ROTATE) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else if (handle == HandleType.RESIZE_TL || handle == HandleType.RESIZE_BR) {
                setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
            } else if (handle == HandleType.RESIZE_TR || handle == HandleType.RESIZE_BL) {
                setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
            } else if (handle == HandleType.MOVE) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }

        private void handleCanvasClick(MouseEvent e) {
            // Convert screen coordinates to canvas coordinates (accounting for zoom)
            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;

            // Get click position in screen coordinates
            int clickX = e.getX();
            int clickY = e.getY();

            // Convert screen coordinates to canvas coordinates (undo zoom transformation)
            // The canvas is scaled by zoomLevel, so we need to divide by it
            int canvasX = (int) (clickX / zoomLevel);
            int canvasY = (int) (clickY / zoomLevel);

            // Get all furniture items
            List<FurnitureItem> items = designState.getFurnitureItems();

            // Loop through items in REVERSE order (so items drawn last are checked first)
            // This ensures items on top are selected first if they overlap
            for (int i = items.size() - 1; i >= 0; i--) {
                FurnitureItem item = items.get(i);

                // Check if click is inside this item's bounding box
                if (isClickInsideFurniture(canvasX, canvasY, item, pixelsPerMeter)) {
                    // Found the item - select it
                    setSelectedItem(item);
                    return;  // Don't check other items
                }
            }

            // Click didn't hit any item - deselect current selection
            clearSelection();
        }

        /**
         * Check if a click position is inside a furniture item's bounding box
         * Takes into account rotation for accurate click detection
         *
         * @param clickX X coordinate of click (canvas space)
         * @param clickY Y coordinate of click (canvas space)
         * @param item The furniture item to test
         * @param pixelsPerMeter Current scale factor
         * @return true if click is inside the item's rotated rectangle
         */
        private boolean isClickInsideFurniture(int clickX, int clickY, FurnitureItem item, double pixelsPerMeter) {
            int offsetX = OFFSET_X;
            int offsetY = OFFSET_Y;

            // Convert item position from meters to pixels
            int itemX = offsetX + (int) (item.getX() * pixelsPerMeter);
            int itemY = offsetY + (int) (item.getY() * pixelsPerMeter);
            int itemWidth = (int) (item.getWidth() * pixelsPerMeter);
            int itemLength = (int) (item.getDepth() * pixelsPerMeter);

            // Center of the item
            int centerX = itemX + itemWidth / 2;
            int centerY = itemY + itemLength / 2;

            // Vector from center to click point
            int dx = clickX - centerX;
            int dy = clickY - centerY;

            // Get rotation angle
            double rotationDegrees = item.getRotation();
            double rotationRadians = Math.toRadians(rotationDegrees);

            // Rotate the click point back to item's local coordinate system
            // This makes the check simpler - we just check axis-aligned rectangle
            double cos = Math.cos(-rotationRadians);  // Negative to rotate back
            double sin = Math.sin(-rotationRadians);

            double rotatedX = dx * cos - dy * sin;
            double rotatedY = dx * sin + dy * cos;

            // Check if rotated point is inside the axis-aligned rectangle
            // (centered at origin in local space, so half-widths are limits)
            double halfWidth = itemWidth / 2.0;
            double halfLength = itemLength / 2.0;

            return (Math.abs(rotatedX) <= halfWidth && Math.abs(rotatedY) <= halfLength);
        }

        private void handleRightClick(MouseEvent e) {
            // Convert screen coordinates to canvas coordinates (accounting for zoom)
            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;
            int canvasX = (int) (e.getX() / zoomLevel);
            int canvasY = (int) (e.getY() / zoomLevel);

            // Find furniture item at click point
            List<FurnitureItem> items = designState.getFurnitureItems();
            for (int i = items.size() - 1; i >= 0; i--) {
                FurnitureItem item = items.get(i);
                if (isClickInsideFurniture(canvasX, canvasY, item, pixelsPerMeter)) {
                    // Found the item - select it and show context menu
                    setSelectedItem(item);
                    showContextMenu(e, item);
                    return;
                }
            }

            // No item clicked - just deselect
            clearSelection();
        }

        private void showContextMenu(MouseEvent e, FurnitureItem item) {
            JPopupMenu menu = new JPopupMenu();

            // Delete option
            JMenuItem deleteItem = new JMenuItem("Delete");
            deleteItem.addActionListener(ae -> {
                designState.removeFurnitureItem(item);
                clearSelection();
                repaint();
            });
            menu.add(deleteItem);

            // Duplicate option
            JMenuItem duplicateItem = new JMenuItem("Duplicate");
            duplicateItem.addActionListener(ae -> {
                duplicateFurnitureItem(item);
                repaint();
            });
            menu.add(duplicateItem);

            // Show the menu at the mouse position
            menu.show(this, e.getX(), e.getY());
        }

        private void duplicateFurnitureItem(FurnitureItem item) {
            // Create a new item with the same properties
            FurnitureItem newItem = new FurnitureItem(
                item.getName() + " Copy",
                item.getType(),
                item.getImagePath(),
                item.getWidth(),
                item.getHeight(),
                item.getDepth(),
                item.getMaterial(),
                item.getDescription()
            );

            // Copy position, rotation, and color with slight offset
            newItem.setPosition(item.getX() + 0.2, item.getY() + 0.2);
            newItem.setRotation(item.getRotation());
            newItem.setColor(item.getColor());

            // Add the new item to the design state
            designState.addFurnitureItem(newItem);

            // Select the new item
            setSelectedItem(newItem);
        }

        private void handleMousePressed(MouseEvent e) {
            if (selectedItem == null) {
                handleCanvasClick(e);
                return;
            }

            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;
            activeHandle = getHandleAtPosition(e.getX(), e.getY(), selectedItem, pixelsPerMeter);

            if (activeHandle != null) {
                draggedItem = selectedItem;
            }
        }

        private void handleMouseDragged(MouseEvent e) {
            if (draggedItem == null || room == null) return;

            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;
            int mouseX = (int) (e.getX() / zoomLevel);
            int mouseY = (int) (e.getY() / zoomLevel);

            int offsetX = OFFSET_X;
            int offsetY = OFFSET_Y;
            int itemPixelX = offsetX + (int) (draggedItem.getX() * pixelsPerMeter);
            int itemPixelY = offsetY + (int) (draggedItem.getY() * pixelsPerMeter);
            int itemWidth = (int) (draggedItem.getWidth() * pixelsPerMeter);
            int itemLength = (int) (draggedItem.getDepth() * pixelsPerMeter);
            int centerX = itemPixelX + itemWidth / 2;
            int centerY = itemPixelY + itemLength / 2;

            switch (activeHandle) {
                case MOVE:
                    handleMove(mouseX, mouseY, pixelsPerMeter);
                    break;
                case RESIZE_TL:
                case RESIZE_TR:
                case RESIZE_BL:
                case RESIZE_BR:
                    handleResize(mouseX, mouseY, pixelsPerMeter, itemWidth, itemLength);
                    break;
                case ROTATE:
                    handleRotate(mouseX, mouseY, centerX, centerY);
                    break;
            }

            repaint();
        }

        private void handleMove(int mouseX, int mouseY, double pixelsPerMeter) {
            int offsetX = OFFSET_X;
            int offsetY = OFFSET_Y;
            int newItemPixelX = mouseX - (int) (draggedItem.getWidth() * pixelsPerMeter) / 2;
            int newItemPixelY = mouseY - (int) (draggedItem.getDepth() * pixelsPerMeter) / 2;
            double newX = (newItemPixelX - offsetX) / pixelsPerMeter;
            double newY = (newItemPixelY - offsetY) / pixelsPerMeter;

            newX = Math.max(0, Math.min(newX, room.getWidth() - draggedItem.getWidth()));
            newY = Math.max(0, Math.min(newY, room.getLength() - draggedItem.getDepth()));

            draggedItem.setPosition(newX, newY);
        }

        private void handleResize(int mouseX, int mouseY, double pixelsPerMeter, int itemWidth, int itemLength) {
            int offsetX = OFFSET_X;
            int offsetY = OFFSET_Y;
            int itemPixelX = offsetX + (int) (draggedItem.getX() * pixelsPerMeter);
            int itemPixelY = offsetY + (int) (draggedItem.getY() * pixelsPerMeter);
            int centerX = itemPixelX + itemWidth / 2;
            int centerY = itemPixelY + itemLength / 2;

            // Get current dimensions in pixels
            double halfPixelWidth = itemWidth / 2.0;
            double halfPixelLength = itemLength / 2.0;

            // Convert mouse position to local coordinates (relative to item center, unrotated)
            double dx = mouseX - centerX;
            double dy = mouseY - centerY;
            double rotationRadians = Math.toRadians(draggedItem.getRotation());
            double cos = Math.cos(-rotationRadians);
            double sin = Math.sin(-rotationRadians);
            double localX = dx * cos - dy * sin;
            double localY = dx * sin + dy * cos;

            // Clamp local coordinates to prevent weird behavior at extreme angles
            double minDimension = 0.3;
            double minPixels = minDimension * pixelsPerMeter;

            // Calculate new dimensions based on which handle is being dragged
            double newWidth = draggedItem.getWidth();
            double newDepth = draggedItem.getDepth();

            if (activeHandle == HandleType.RESIZE_BR) {
                // Bottom-right: drag extends width and depth
                double newHalfPixelWidth = Math.max(minPixels, localX);
                double newHalfPixelLength = Math.max(minPixels, localY);
                newWidth = (newHalfPixelWidth * 2) / pixelsPerMeter;
                newDepth = (newHalfPixelLength * 2) / pixelsPerMeter;
            }
            else if (activeHandle == HandleType.RESIZE_BL) {
                // Bottom-left: drag extends width (opposite) and depth
                double newHalfPixelWidth = Math.max(minPixels, -localX);
                double newHalfPixelLength = Math.max(minPixels, localY);
                newWidth = (newHalfPixelWidth * 2) / pixelsPerMeter;
                newDepth = (newHalfPixelLength * 2) / pixelsPerMeter;
            }
            else if (activeHandle == HandleType.RESIZE_TR) {
                // Top-right: drag extends width and depth (opposite)
                double newHalfPixelWidth = Math.max(minPixels, localX);
                double newHalfPixelLength = Math.max(minPixels, -localY);
                newWidth = (newHalfPixelWidth * 2) / pixelsPerMeter;
                newDepth = (newHalfPixelLength * 2) / pixelsPerMeter;
            }
            else if (activeHandle == HandleType.RESIZE_TL) {
                // Top-left: drag extends width (opposite) and depth (opposite)
                double newHalfPixelWidth = Math.max(minPixels, -localX);
                double newHalfPixelLength = Math.max(minPixels, -localY);
                newWidth = (newHalfPixelWidth * 2) / pixelsPerMeter;
                newDepth = (newHalfPixelLength * 2) / pixelsPerMeter;
            }

            // Enforce room boundaries - furniture cannot exceed room dimensions
            newWidth = Math.min(newWidth, room.getWidth());
            newDepth = Math.min(newDepth, room.getLength());

            // Apply the new dimensions
            draggedItem.setWidth(newWidth);
            draggedItem.setDepth(newDepth);
        }

        private void handleRotate(int mouseX, int mouseY, int centerX, int centerY) {
            double dx = mouseX - centerX;
            double dy = mouseY - centerY;
            double angle = Math.atan2(dy, dx);
            double degrees = Math.toDegrees(angle) - 90;

            degrees = ((degrees % 360) + 360) % 360;
            draggedItem.setRotation(degrees);
        }

        private void handleMouseReleased(MouseEvent e) {
            draggedItem = null;
            activeHandle = null;
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

            // Calculate pixels per meter for current zoom level
            double pixelsPerMeter = BASE_PIXELS_PER_METER * zoomLevel;

            // Apply zoom transformation for all elements (grid, room, furniture)
            AffineTransform zoomTransform = AffineTransform.getScaleInstance(zoomLevel, zoomLevel);
            g2d.transform(zoomTransform);

            // Draw room floor first (background)
            drawRoom(g2d, pixelsPerMeter);

            // Draw grid on top of room floor but behind furniture
            drawGrid(g2d, pixelsPerMeter);

            // Draw furniture items on top
            drawFurniture(g2d, pixelsPerMeter);
        }

        private void drawGrid(Graphics2D g2d, double pixelsPerMeter) {
            if (!gridVisible) {
                return;  // Don't draw grid if hidden
            }

            int offsetX = OFFSET_X;  // 50
            int offsetY = OFFSET_Y;  // 50

            // Calculate room dimensions in pixels
            int roomPixelWidth = (int) (room.getWidth() * pixelsPerMeter);
            int roomPixelLength = (int) (room.getLength() * pixelsPerMeter);

            // Calculate grid size in pixels
            int gridPixelSize = (int) (GRID_SQUARE_SIZE * pixelsPerMeter);

            // Only draw grid if it makes sense at this zoom level
            if (gridPixelSize < 2) {
                return; // Grid too small to see
            }

            // Draw main grid squares (1 meter = 1 square) - ONLY INSIDE THE ROOM
            g2d.setColor(new Color(200, 200, 200, 150));  // Light gray with semi-transparency
            g2d.setStroke(new BasicStroke(0.5f / (float)zoomLevel));  // Scale stroke width with zoom

            // Vertical lines
            for (int x = offsetX; x <= offsetX + roomPixelWidth; x += gridPixelSize) {
                g2d.drawLine(x, offsetY, x, offsetY + roomPixelLength);
            }
            // Horizontal lines
            for (int y = offsetY; y <= offsetY + roomPixelLength; y += gridPixelSize) {
                g2d.drawLine(offsetX, y, offsetX + roomPixelWidth, y);
            }

            // Draw sub-grid (0.5 meter divisions) - lighter shade
            g2d.setColor(new Color(220, 220, 220, 100));  // Even lighter with less opacity
            g2d.setStroke(new BasicStroke(0.3f / (float)zoomLevel));  // Thinner stroke for sub-grid
            int subGridSize = gridPixelSize / 2;

            // Vertical sub-grid lines
            for (int x = offsetX + subGridSize; x < offsetX + roomPixelWidth; x += gridPixelSize) {
                g2d.drawLine(x, offsetY, x, offsetY + roomPixelLength);
            }
            // Horizontal sub-grid lines
            for (int y = offsetY + subGridSize; y < offsetY + roomPixelLength; y += gridPixelSize) {
                g2d.drawLine(offsetX, y, offsetX + roomPixelWidth, y);
            }
        }

        private void drawRoom(Graphics2D g2d, double pixelsPerMeter) {
            int width = (int) (room.getWidth() * pixelsPerMeter);
            int length = (int) (room.getLength() * pixelsPerMeter);
            int offsetX = 50;
            int offsetY = 50;

            // Room floor - light background
            g2d.setColor(new Color(245, 245, 245));
            g2d.fillRect(offsetX, offsetY, width, length);

            // Room walls - thick border
            g2d.setColor(new Color(0, 0, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(offsetX, offsetY, width, length);

            // Room dimensions text
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.drawString(String.format("%.1f m", room.getWidth()), offsetX + width / 2 - 15, offsetY - 10);
            g2d.drawString(String.format("%.1f m", room.getLength()), offsetX - 40, offsetY + length / 2 + 5);
        }

        private void drawFurniture(Graphics2D g2d, double pixelsPerMeter) {
            List<FurnitureItem> items = designState.getFurnitureItems();

            for (FurnitureItem item : items) {
                drawFurnitureItem(g2d, item, pixelsPerMeter);
            }

            // Draw item count
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2d.drawString(String.format("Furniture items: %d", items.size()), 60, 30);
        }

        private void drawFurnitureItem(Graphics2D g2d, FurnitureItem item, double pixelsPerMeter) {
            int offsetX = 50;
            int offsetY = 50;

            // Convert position to pixels
            int x = offsetX + (int) (item.getX() * pixelsPerMeter);
            int y = offsetY + (int) (item.getY() * pixelsPerMeter);
            int itemWidth = (int) (item.getWidth() * pixelsPerMeter);
            int itemLength = (int) (item.getDepth() * pixelsPerMeter);

            // Save original transform
            AffineTransform original = g2d.getTransform();

            // Translate to center of item
            int centerX = x + itemWidth / 2;
            int centerY = y + itemLength / 2;
            g2d.translate(centerX, centerY);

            // Rotate if needed
            double rotation = item.getRotation();
            if (rotation != 0) {
                g2d.rotate(Math.toRadians(rotation));
            }

            // Draw furniture rectangle (centered at origin now)
            g2d.setColor(item.getColor());
            g2d.fillRect(-itemWidth / 2, -itemLength / 2, itemWidth, itemLength);

            // Draw border - highlight if selected
            boolean isSelected = (selectedItem != null && selectedItem == item);
            if (isSelected) {
                g2d.setColor(SELECTION_COLOR);  // Blue for selected
                g2d.setStroke(new BasicStroke(SELECTION_BORDER_WIDTH));
            } else {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(NORMAL_BORDER_WIDTH));
            }
            g2d.drawRect(-itemWidth / 2, -itemLength / 2, itemWidth, itemLength);

            // Draw resize and rotate handles if selected
            if (isSelected) {
                drawResizeHandles(g2d, itemWidth, itemLength);
                drawRotateHandle(g2d, itemWidth, itemLength);
            }

            // Draw rotation indicator (arrow)
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(0, 0, itemWidth / 3, 0);

            // Draw arrowhead
            int[] arrowX = {itemWidth / 3, itemWidth / 3 - 5, itemWidth / 3 - 3};
            int[] arrowY = {0, -3, 3};
            g2d.fillPolygon(arrowX, arrowY, 3);

            // Restore transform
            g2d.setTransform(original);

            // Draw label (outside the rotated area)
            g2d.setColor(isSelected ? SELECTION_COLOR : Color.BLACK);  // Color label based on selection
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
            g2d.drawString(item.getName(), x + 5, y - 5);

            // Draw dimensions
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 8));
            g2d.drawString(String.format("%.1f×%.1f", item.getWidth(), item.getDepth()), x + 5, y + 15);
        }

        private void drawResizeHandles(Graphics2D g2d, int itemWidth, int itemLength) {
            g2d.setColor(new Color(0, 150, 255));
            int halfW = itemWidth / 2;
            int halfH = itemLength / 2;

            g2d.fillRect(-halfW - HANDLE_SIZE / 2, -halfH - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
            g2d.fillRect(halfW - HANDLE_SIZE / 2, -halfH - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
            g2d.fillRect(-halfW - HANDLE_SIZE / 2, halfH - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
            g2d.fillRect(halfW - HANDLE_SIZE / 2, halfH - HANDLE_SIZE / 2, HANDLE_SIZE, HANDLE_SIZE);
        }

        private void drawRotateHandle(Graphics2D g2d, int itemWidth, int itemLength) {
            g2d.setColor(new Color(255, 100, 0));
            int halfH = itemLength / 2;
            g2d.fillOval(-HANDLE_SIZE / 2, -(halfH + ROTATE_HANDLE_DISTANCE + HANDLE_SIZE / 2), HANDLE_SIZE, HANDLE_SIZE);
            g2d.setColor(Color.BLACK);
            g2d.drawLine(0, -halfH, 0, -(halfH + ROTATE_HANDLE_DISTANCE));
        }
    }
}
