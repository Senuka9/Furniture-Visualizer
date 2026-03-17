package com.teamname.furniviz.editor2d;

import com.teamname.furniviz.room.Room;
import com.teamname.furniviz.storage.RoomRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Room Selector Panel - Allows user to select a room before opening 2D editor
 * Displayed when user clicks "2D Editor" button without a room selected
 */
public class RoomSelectorPanel extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> roomList;
    private List<Room> allRooms;
    private RoomRepository roomRepository;
    private RoomSelectionCallback onRoomSelected;
    private Runnable onBackCallback;

    @FunctionalInterface
    public interface RoomSelectionCallback {
        void onRoomSelected(Room room);
    }

    public RoomSelectorPanel(RoomSelectionCallback onRoomSelected, Runnable onBackCallback) {
        this.onRoomSelected = onRoomSelected;
        this.onBackCallback = onBackCallback;
        this.roomRepository = new RoomRepository();

        initUI();
        loadRooms();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ============ NORTH: Title & Back Button ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Select a Room for 2D Editor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(33, 33, 33));
        topPanel.add(title, BorderLayout.CENTER);

        JButton backButton = new JButton("<- Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(new Color(33, 33, 33));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> {
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });
        topPanel.add(backButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: Room List ============
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));

        JLabel listLabel = new JLabel("Available Rooms:");
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        centerPanel.add(listLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        roomList = new JList<>(listModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomList.setBackground(Color.WHITE);
        roomList.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JScrollPane scrollPane = new JScrollPane(roomList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ============ SOUTH: Action Buttons ============
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));

        JButton selectButton = new JButton("Select Room");
        selectButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        selectButton.setPreferredSize(new Dimension(150, 40));
        selectButton.setBackground(new Color(76, 175, 80));
        selectButton.setForeground(Color.WHITE);
        selectButton.setFocusPainted(false);
        selectButton.setBorderPainted(false);
        selectButton.addActionListener(e -> handleRoomSelection());
        bottomPanel.add(selectButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadRooms() {
        try {
            allRooms = roomRepository.getAllRooms();
            listModel.clear();

            if (allRooms.isEmpty()) {
                listModel.addElement("[No rooms created yet]");
            } else {
                for (Room room : allRooms) {
                    String roomDisplay = String.format("%s (%.1f m × %.1f m)",
                        room.getName(),
                        room.getWidth(),
                        room.getLength());
                    listModel.addElement(roomDisplay);
                }
            }
        } catch (Exception e) {
            listModel.clear();
            listModel.addElement("[Error loading rooms]");
        }
    }

    private void handleRoomSelection() {
        int selectedIndex = roomList.getSelectedIndex();

        if (selectedIndex < 0 || allRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select a room first!",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room selectedRoom = allRooms.get(selectedIndex);
        if (onRoomSelected != null) {
            onRoomSelected.onRoomSelected(selectedRoom);
        }
    }

    public void refresh() {
        loadRooms();
    }
}

