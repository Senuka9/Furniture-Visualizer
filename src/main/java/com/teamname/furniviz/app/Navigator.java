package com.teamname.furniviz.app;

import com.teamname.furniviz.room.RoomFormPanel;
import com.teamname.furniviz.room.RoomController;
import com.teamname.furniviz.room.RoomTemplatesPage;
import com.teamname.furniviz.furniture.FurnitureLibraryPanel;
import com.teamname.furniviz.editor2d.Editor2DPanel;
import com.teamname.furniviz.editor2d.RoomSelectorPanel;

import javax.swing.*;
import java.awt.*;

public class Navigator extends JPanel {

    private CardLayout layout;
    private DesignState designState;
    private RoomFormPanel roomFormPanel;
    private RoomTemplatesPage roomTemplatesPage;
    private FurnitureLibraryPanel furnitureLibraryPanel;
    private Editor2DPanel editor2DPanel;
    private RoomSelectorPanel roomSelectorPanel;

    public Navigator() {
        layout = new CardLayout();
        setLayout(layout);

        // Create shared DesignState
        this.designState = new DesignState();

        // Add HOME panel
        JPanel homePanel = createHomePanel();
        add(homePanel, "HOME");

        // Create and add ROOM panel with back button callback
        RoomController roomController = new RoomController(designState);
        this.roomFormPanel = new RoomFormPanel(roomController, () -> showHome(), () -> showRooms());
        add(roomFormPanel, "ROOM");

        // Create and add ROOMS panel for viewing all templates
        this.roomTemplatesPage = new RoomTemplatesPage(
                () -> showHome(),
                room -> showEditor2D(room)  // When room selected, go to 2D editor
        );
        add(roomTemplatesPage, "ROOMS");

        // Create and add FURNITURE panel
        this.furnitureLibraryPanel = new FurnitureLibraryPanel(
                designState,
                () -> showHome(),
                () -> show2D()
        );
        add(furnitureLibraryPanel, "FURNITURE");

        // Create and add 2D EDITOR panel
        this.editor2DPanel = new Editor2DPanel(
                designState,
                () -> showHome(),
                () -> show3D()
        );
        add(editor2DPanel, "EDITOR_2D");

        // Create and add ROOM SELECTOR panel for 2D editor
        this.roomSelectorPanel = new RoomSelectorPanel(
                room -> showEditor2D(room),
                () -> showHome()
        );
        add(roomSelectorPanel, "ROOM_SELECTOR_2D");
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // NORTH: Title
        JLabel title = new JLabel("Furniture Visualizer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(title, BorderLayout.NORTH);

        // CENTER: Button panel only
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setBackground(new Color(245, 245, 245));

        JButton roomButton = new JButton("Create Room");
        JButton roomsButton = new JButton("Rooms");
        JButton furnitureButton = new JButton("Furniture Library");
        JButton editor2DButton = new JButton("2D Editor");
        JButton view3DButton = new JButton("3D View");
        JButton portfolioButton = new JButton("Portfolio");

        panel.add(roomButton);
        panel.add(roomsButton);
        panel.add(furnitureButton);
        panel.add(editor2DButton);
        panel.add(view3DButton);
        panel.add(portfolioButton);

        // Button actions
        roomButton.addActionListener(e -> showRoom());
        roomsButton.addActionListener(e -> showRooms());
        furnitureButton.addActionListener(e -> showFurniture());
        editor2DButton.addActionListener(e -> showRoomSelector());
        view3DButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "3D View will be implemented by Member 5"));
        portfolioButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Portfolio not implemented yet"));

        return panel;
    }

    public DesignState getDesignState() {
        return designState;
    }

    public void showHome() {
        layout.show(this, "HOME");
    }

    public void showRoom() {
        layout.show(this, "ROOM");
    }

    public void showRooms() {
        roomTemplatesPage.refresh();
        layout.show(this, "ROOMS");
    }

    public void showFurniture() {
        layout.show(this, "FURNITURE");
    }

    public void showRoomSelector() {
        roomSelectorPanel.refresh();  // Refresh list before showing
        layout.show(this, "ROOM_SELECTOR_2D");
    }

    public void show2D() {
        layout.show(this, "EDITOR_2D");
    }

    public void showEditor2D(com.teamname.furniviz.room.Room room) {
        designState.setRoom(room);
        editor2DPanel.setRoom(room);
        layout.show(this, "EDITOR_2D");
    }

    public void show3D() {
        JOptionPane.showMessageDialog(this, "3D View will be implemented by Member 5");
    }
}
