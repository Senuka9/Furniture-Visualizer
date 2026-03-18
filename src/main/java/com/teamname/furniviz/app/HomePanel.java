package com.teamname.furniviz.app;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(Navigator navigator) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Title
        JLabel title = new JLabel("Furniture Visualizer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        title.setForeground(new Color(33, 33, 33));

        add(title, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton roomButton = new JButton("Create Room");
        JButton roomsButton = new JButton("Rooms");
        JButton furnitureButton = new JButton("Furniture Library");
        JButton editor2DButton = new JButton("2D Editor");
        JButton view3DButton = new JButton("3D View");
        JButton portfolioButton = new JButton("Portfolio");

        // Style buttons
        for (JButton btn : new JButton[]{roomButton, roomsButton, furnitureButton, editor2DButton, view3DButton, portfolioButton}) {
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBackground(new Color(76, 175, 80));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
        }

        buttonPanel.add(roomButton);
        buttonPanel.add(roomsButton);
        buttonPanel.add(furnitureButton);
        buttonPanel.add(editor2DButton);
        buttonPanel.add(view3DButton);
        buttonPanel.add(portfolioButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Actions
        roomButton.addActionListener(e -> navigator.showRoom());
        roomsButton.addActionListener(e -> navigator.showRooms());
        furnitureButton.addActionListener(e -> navigator.showFurniture());
        editor2DButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "2D Editor not implemented yet"));
        view3DButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "3D View not implemented yet"));
        portfolioButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Portfolio not implemented yet"));
    }
}
