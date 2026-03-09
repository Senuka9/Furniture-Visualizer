package com.teamname.furniviz.app;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {

    public HomePanel(Navigator navigator) {

        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Furniture Visualizer", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        add(title, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 1, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 50, 300));

        JButton roomButton = new JButton("Create Room");
        JButton furnitureButton = new JButton("Furniture Library");
        JButton editor2DButton = new JButton("2D Editor");
        JButton view3DButton = new JButton("3D View");
        JButton portfolioButton = new JButton("Portfolio");

        buttonPanel.add(roomButton);
        buttonPanel.add(furnitureButton);
        buttonPanel.add(editor2DButton);
        buttonPanel.add(view3DButton);
        buttonPanel.add(portfolioButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Temporary actions
        roomButton.addActionListener(e -> 
            navigator.showRoom()
        );

        furnitureButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Furniture module not implemented yet")
        );

        editor2DButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "2D Editor not implemented yet")
        );

        view3DButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "3D View not implemented yet")
        );

        portfolioButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Portfolio not implemented yet")
        );
    }
}
