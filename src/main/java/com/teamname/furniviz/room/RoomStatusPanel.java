package com.teamname.furniviz.room;

import javax.swing.*;
import java.awt.*;

/**
 * RoomStatusPanel - Displays the current room status and saved rooms
 * This panel shows where the "draft rooms" are displayed and managed
 */
public class RoomStatusPanel extends JPanel {

    private JLabel roomNameLabel;
    private JLabel roomDimensionsLabel;
    private JLabel roomStatusLabel;
    private JLabel roomAreaLabel;
    private JPanel roomPreviewContainer;
    private RoomPreviewPanel roomPreview;
    private Room currentRoom;

    public RoomStatusPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(250, 250, 250));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // ============ TOP: Status Info ============
        JPanel statusPanel = createStatusPanel();
        add(statusPanel, BorderLayout.NORTH);

        // ============ CENTER: Room Preview ============
        roomPreviewContainer = new JPanel(new BorderLayout());
        roomPreviewContainer.setBackground(new Color(255, 255, 255));
        roomPreviewContainer.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        roomPreview = new RoomPreviewPanel();
        roomPreview.setPreferredSize(new Dimension(400, 300));
        roomPreviewContainer.add(roomPreview, BorderLayout.CENTER);

        add(roomPreviewContainer, BorderLayout.CENTER);

        // ============ BOTTOM: Actions ============
        JPanel actionPanel = createActionPanel();
        add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(250, 250, 250));

        // Title
        JLabel title = new JLabel("Current Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(33, 33, 33));
        panel.add(title);
        panel.add(Box.createVerticalStrut(8));

        // Room Name
        roomNameLabel = new JLabel("Room: Not Created");
        roomNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomNameLabel.setForeground(new Color(66, 66, 66));
        panel.add(roomNameLabel);

        // Dimensions
        roomDimensionsLabel = new JLabel("Dimensions: — × —");
        roomDimensionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomDimensionsLabel.setForeground(new Color(66, 66, 66));
        panel.add(roomDimensionsLabel);

        // Area
        roomAreaLabel = new JLabel("Area: —");
        roomAreaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomAreaLabel.setForeground(new Color(66, 66, 66));
        panel.add(roomAreaLabel);

        // Status
        roomStatusLabel = new JLabel("Status: No Room");
        roomStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        roomStatusLabel.setForeground(new Color(150, 150, 150));
        panel.add(roomStatusLabel);

        return panel;
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(new Color(250, 250, 250));

        JLabel infoLabel = new JLabel("Room data is live updated from the Room Form");
        infoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        infoLabel.setForeground(new Color(120, 120, 120));

        panel.add(infoLabel);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    /**
     * Update the room display with new room data
     * @param room The room to display (null to clear)
     */
    public void updateRoom(Room room) {
        this.currentRoom = room;

        if (room == null || !room.isValidForRendering()) {
            // No room or invalid room
            roomNameLabel.setText("Room: Not Created");
            roomDimensionsLabel.setText("Dimensions: — × —");
            roomAreaLabel.setText("Area: —");
            roomStatusLabel.setText("Status: No Room");
            roomStatusLabel.setForeground(new Color(150, 150, 150));
            roomPreview.setRoomData(0, 0, RoomShape.RECTANGLE);
            roomPreview.setWallColor(new Color(240, 240, 240));
        } else {
            // Display room details
            roomNameLabel.setText("Room: " + room.getRoomId());
            roomDimensionsLabel.setText(String.format("Dimensions: %.1f × %.1f", room.getWidth(), room.getLength()));
            roomAreaLabel.setText(String.format("Area: %.2f m²", room.getArea()));

            // Status with color
            String statusText = "Status: " + room.getStatus().toString();
            Color statusColor = getStatusColor(room.getStatus());
            roomStatusLabel.setText(statusText);
            roomStatusLabel.setForeground(statusColor);

            // Update preview
            roomPreview.setRoomData(room.getWidth(), room.getLength(), room.getShape());
            roomPreview.setWallColor(room.getWallColor());
        }

        repaint();
    }

    /**
     * Get color for room status
     */
    private Color getStatusColor(Room.RoomStatus status) {
        switch (status) {
            case SAVED:
                return new Color(76, 175, 80); // Green
            case MODIFIED:
                return new Color(255, 152, 0); // Orange
            case DRAFT:
            default:
                return new Color(255, 193, 7); // Yellow
        }
    }

    /**
     * Get the current room being displayed
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }
}

