package com.teamname.furniviz.room;

import com.teamname.furniviz.storage.RoomRepository;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

/**
 * Room Templates Page - Browse, edit, delete saved room templates
 * Displays all saved rooms with CRUD operations
 */
public class RoomTemplatesPage extends JPanel {

    private JList<String> templatesList;
    private DefaultListModel<String> listModel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton addRecommendButton;
    private JButton backButton;
    private JButton refreshButton;
    private JLabel infoLabel;
    private JPanel detailsPanel;
    private RoomPreviewPanel roomPreviewPanel;  // NEW: Room preview visualization
    private Room selectedRoom;
    private List<Room> allRooms;
    private RoomRepository roomRepository;
    private Runnable onBackPressed;
    private RoomCallback onRoomSelected;

    // Functional interface for room selection callback
    @FunctionalInterface
    public interface RoomCallback {
        void onRoomSelected(Room room);
    }

    public RoomTemplatesPage(Runnable onBackPressed, RoomCallback onRoomSelected) {
        this.onBackPressed = onBackPressed;
        this.onRoomSelected = onRoomSelected;
        this.roomRepository = new RoomRepository();
        initComponents();
        initUI();
        loadTemplates();
    }

    private void initComponents() {
        listModel = new DefaultListModel<>();
        templatesList = new JList<>(listModel);
        templatesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        templatesList.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        editButton = new JButton("[E] Edit");
        deleteButton = new JButton("[D] Delete");
        addRecommendButton = new JButton("[+] Add Recommendation");
        backButton = new JButton("<- Back");
        refreshButton = new JButton("[R] Refresh");
        infoLabel = new JLabel("No room selected");
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ============ NORTH: Title & Back Button ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Room Templates");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));
        topPanel.add(title, BorderLayout.CENTER);

        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(new Color(33, 33, 33));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        topPanel.add(backButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: Split Panel ============
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(new Color(245, 245, 245));
        splitPane.setDividerLocation(300);

        // LEFT: Room Templates List
        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        // RIGHT: Details Panel
        detailsPanel = createDetailsPanel();
        splitPane.setRightComponent(detailsPanel);

        add(splitPane, BorderLayout.CENTER);

        // ============ SOUTH: Button Panel ============
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setupListeners();
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(245, 245, 245));

        JLabel listLabel = new JLabel("Saved Room Templates");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(listLabel, BorderLayout.NORTH);

        templatesList.setBackground(Color.WHITE);
        templatesList.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        JScrollPane scrollPane = new JScrollPane(templatesList);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // LEFT: Room Details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(255, 255, 255));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        detailsPanel.setPreferredSize(new Dimension(300, 400));

        JLabel detailsTitle = new JLabel("Room Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        detailsPanel.add(detailsTitle);
        detailsPanel.add(Box.createVerticalStrut(10));

        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoLabel.setVerticalAlignment(SwingConstants.TOP);
        detailsPanel.add(infoLabel);

        detailsPanel.add(Box.createVerticalGlue());

        // RIGHT: Room Preview
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setBackground(new Color(255, 255, 255));
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        previewPanel.setPreferredSize(new Dimension(350, 400));

        JLabel previewTitle = new JLabel("Room Preview");
        previewTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        previewPanel.add(previewTitle);
        previewPanel.add(Box.createVerticalStrut(10));

        // Create the room preview visualization panel
        roomPreviewPanel = new RoomPreviewPanel();
        roomPreviewPanel.setPreferredSize(new Dimension(320, 300));
        roomPreviewPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        roomPreviewPanel.setBackground(Color.WHITE);
        previewPanel.add(roomPreviewPanel);

        previewPanel.add(Box.createVerticalGlue());

        // Add both panels to main panel
        mainPanel.add(detailsPanel, BorderLayout.WEST);
        mainPanel.add(previewPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setBackground(new Color(245, 245, 245));

        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editButton.setPreferredSize(new Dimension(150, 40));
        editButton.setBackground(new Color(70, 130, 180));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setBorderPainted(false);
        editButton.setEnabled(false);

        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteButton.setPreferredSize(new Dimension(150, 40));
        deleteButton.setBackground(new Color(244, 67, 54));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setEnabled(false);

        addRecommendButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addRecommendButton.setPreferredSize(new Dimension(180, 40));
        addRecommendButton.setBackground(new Color(255, 152, 0));
        addRecommendButton.setForeground(Color.WHITE);
        addRecommendButton.setFocusPainted(false);
        addRecommendButton.setBorderPainted(false);
        addRecommendButton.setEnabled(false);

        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        refreshButton.setPreferredSize(new Dimension(150, 40));
        refreshButton.setBackground(new Color(76, 175, 80));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);

        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(addRecommendButton);
        panel.add(refreshButton);

        return panel;
    }

    private void setupListeners() {
        backButton.addActionListener(e -> {
            if (onBackPressed != null) {
                onBackPressed.run();
            }
        });

        refreshButton.addActionListener(e -> loadTemplates());

        templatesList.addListSelectionListener(e -> {
            int selectedIndex = templatesList.getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < allRooms.size()) {
                selectedRoom = allRooms.get(selectedIndex);
                displayRoomDetails(selectedRoom);
                editButton.setEnabled(true);
                deleteButton.setEnabled(true);
                addRecommendButton.setEnabled(true);
            } else {
                selectedRoom = null;
                infoLabel.setText("No room selected");
                editButton.setEnabled(false);
                deleteButton.setEnabled(false);
                addRecommendButton.setEnabled(false);
            }
        });

        editButton.addActionListener(e -> {
            if (selectedRoom != null) {
                editRoom(selectedRoom);
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedRoom != null) {
                deleteRoom(selectedRoom);
            }
        });

        addRecommendButton.addActionListener(e -> {
            if (selectedRoom != null) {
                addRecommendation(selectedRoom);
            }
        });
    }

    private void loadTemplates() {
        listModel.clear();
        allRooms = roomRepository.getAllRooms();

        if (allRooms.isEmpty()) {
            listModel.addElement("No rooms created yet");
            infoLabel.setText("Create your first room to see it here");
        } else {
            for (int i = 0; i < allRooms.size(); i++) {
                Room room = allRooms.get(i);
                String displayName = String.format("Room %d: %.1fx%.1f (%s)",
                        i + 1, room.getWidth(), room.getLength(), room.getShape());
                listModel.addElement(displayName);
            }
        }

        templatesList.repaint();
    }

    private void displayRoomDetails(Room room) {
        StringBuilder details = new StringBuilder();
        details.append("<html>");
        details.append("<b>Width:</b> ").append(String.format("%.2f", room.getWidth())).append(" units<br>");
        details.append("<b>Length:</b> ").append(String.format("%.2f", room.getLength())).append(" units<br>");
        details.append("<b>Area:</b> ").append(String.format("%.2f", room.getArea())).append(" m²<br>");
        details.append("<b>Shape:</b> ").append(room.getShape()).append("<br>");
        details.append("<b>Status:</b> ").append(room.getStatus()).append("<br>");
        details.append("<b>Created:</b> ").append(room.getCreatedAt()).append("<br>");
        details.append("<b>Modified:</b> ").append(room.getLastModifiedAt()).append("<br>");

        Color wallColor = room.getWallColor();
        details.append("<b>Wall Color:</b> RGB(")
                .append(wallColor.getRed()).append(",")
                .append(wallColor.getGreen()).append(",")
                .append(wallColor.getBlue()).append(")<br>");

        details.append("</html>");
        infoLabel.setText(details.toString());

        // NEW: Update the room preview panel to show visual representation
        if (roomPreviewPanel != null) {
            roomPreviewPanel.setRoomData(room.getWidth(), room.getLength(), room.getShape());
            roomPreviewPanel.setWallColor(room.getWallColor());
            roomPreviewPanel.repaint();
        }
    }

    private void editRoom(Room room) {
        if (onRoomSelected != null) {
            onRoomSelected.onRoomSelected(room);
        }
    }

    private void deleteRoom(Room room) {
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this room template?",
                "Delete Room",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            if (roomRepository.deleteRoom(room.getRoomId())) {
                JOptionPane.showMessageDialog(
                        this,
                        "Room deleted successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                loadTemplates();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to delete room. Check MongoDB connection.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void addRecommendation(Room room) {
        String recommendation = JOptionPane.showInputDialog(
                this,
                "Add a recommendation for this room:",
                "Room Recommendation"
        );

        if (recommendation != null && !recommendation.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Recommendation added: " + recommendation,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            // TODO: Store recommendation in MongoDB with the room
        }
    }

    /**
     * Refresh the templates list
     */
    public void refresh() {
        loadTemplates();
    }
}

