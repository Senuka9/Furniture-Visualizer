package com.teamname.furniviz.app;

import com.teamname.furniviz.room.RoomFormPanel;
import com.teamname.furniviz.room.RoomController;
import com.teamname.furniviz.room.RoomTemplatesPage;

import javax.swing.*;
import java.awt.*;

public class Navigator extends JPanel {

    private CardLayout layout;
    private DesignState designState;
    private RoomFormPanel roomFormPanel;
    private RoomTemplatesPage roomTemplatesPage;

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
                () -> showRoom()
        );
        add(roomTemplatesPage, "ROOMS");
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

        // Temporary actions
        roomButton.addActionListener(e -> showRoom());
        roomsButton.addActionListener(e -> showRooms());
        furnitureButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "Furniture module not implemented yet"));
        editor2DButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "2D Editor not implemented yet"));
        view3DButton.addActionListener(e -> JOptionPane.showMessageDialog(panel, "3D View not implemented yet"));
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

    // Later you will add:
    // show2D()
    // show3D()
    // showPortfolio()
}
