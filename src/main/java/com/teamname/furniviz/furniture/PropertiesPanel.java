package com.teamname.furniviz.furniture;

import com.teamname.furniviz.app.DesignState;
import javax.swing.*;
import java.awt.*;

/**
 * PropertiesPanel - Displays details of a selected furniture preset
 *
 * This panel shows the preview image, specifications, and action buttons
 * for the currently selected furniture preset from the library.
 */
public class PropertiesPanel extends JPanel {
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JLabel typeLabel;
    private JLabel dimensionsLabel;
    private JLabel materialLabel;
    private JTextArea descriptionArea;
    private JButton addToRoomButton;
    private JButton view3DButton;
    private FurniturePreset currentPreset;
    private DesignState designState;
    private Runnable goTo2DEditorCallback;

    public PropertiesPanel(DesignState designState, Runnable goTo2DEditorCallback) {
        this.designState = designState;
        this.goTo2DEditorCallback = goTo2DEditorCallback;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(245, 245, 245));

        // ============ TOP: Image ============
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(220, 220));
        imageLabel.setBackground(new Color(230, 230, 230));
        imageLabel.setOpaque(true);
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        add(imageLabel, BorderLayout.NORTH);

        // ============ CENTER: Details in two panels ============
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 245));

        // Details panel
        JPanel detailsPanel = createDetailsPanel();
        centerPanel.add(detailsPanel, BorderLayout.NORTH);

        // Description panel
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.setBackground(new Color(245, 245, 245));
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descriptionArea.setBackground(new Color(255, 255, 255));
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        descPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        centerPanel.add(descPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // ============ SOUTH: Action Buttons ============
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        addToRoomButton = new JButton("Add to Room");
        addToRoomButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addToRoomButton.setBackground(new Color(76, 175, 80));
        addToRoomButton.setForeground(Color.WHITE);
        addToRoomButton.setFocusPainted(false);
        addToRoomButton.setBorderPainted(false);
        addToRoomButton.addActionListener(e -> handleAddToRoom());
        buttonPanel.add(addToRoomButton);

        view3DButton = new JButton("View 3D");
        view3DButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        view3DButton.setBackground(new Color(33, 150, 243));
        view3DButton.setForeground(Color.WHITE);
        view3DButton.setFocusPainted(false);
        view3DButton.setBorderPainted(false);
        view3DButton.addActionListener(e -> handleView3D());
        buttonPanel.add(view3DButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder("Item Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(nameLabel, gbc);

        // Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        typeLabel = new JLabel();
        panel.add(typeLabel, gbc);

        // Dimensions
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Dimensions:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        dimensionsLabel = new JLabel();
        panel.add(dimensionsLabel, gbc);

        // Material
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Material:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        materialLabel = new JLabel();
        panel.add(materialLabel, gbc);

        return panel;
    }

    /**
     * Display a furniture preset in the properties panel
     */
    public void displayPreset(FurniturePreset preset) {
        this.currentPreset = preset;

        if (preset != null) {
            // Load image - with fallback for missing images
            try {
                java.net.URL url = getClass().getResource(preset.getPreviewPath());
                if (url != null) {
                    ImageIcon icon = new ImageIcon(url);
                    Image image = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                } else {
                    createPlaceholderImage();
                }
            } catch (Exception e) {
                createPlaceholderImage();
            }

            nameLabel.setText(preset.getName());
            typeLabel.setText(preset.getType().toString());
            dimensionsLabel.setText(String.format("%.1f m × %.1f m × %.1f m (W × D × H)",
                preset.getWidth(), preset.getDepth(), preset.getHeight()));
            materialLabel.setText(preset.getMaterial());
            descriptionArea.setText(preset.getDescription());

            addToRoomButton.setEnabled(true);
            view3DButton.setEnabled(true);
        } else {
            clearDisplay();
        }
    }

    private void clearDisplay() {
        imageLabel.setIcon(null);
        nameLabel.setText("");
        typeLabel.setText("");
        dimensionsLabel.setText("");
        materialLabel.setText("");
        descriptionArea.setText("");
        addToRoomButton.setEnabled(false);
        view3DButton.setEnabled(false);
    }

    private void createPlaceholderImage() {
        java.awt.image.BufferedImage placeholder = new java.awt.image.BufferedImage(200, 200, java.awt.image.BufferedImage.TYPE_INT_RGB);
        java.awt.Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, 200, 200);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new java.awt.BasicStroke(2));
        g2d.drawRect(5, 5, 190, 190);
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("No Image Available", 50, 100);
        g2d.dispose();
        imageLabel.setIcon(new ImageIcon(placeholder));
    }

    private void handleAddToRoom() {
        if (currentPreset != null) {
            // Convert preset to furniture item
            FurnitureItem item = currentPreset.createFurnitureItem(0.0, 0.0);

            // Calculate safe position using DesignState's placement strategy
            double[] position = designState.calculatePositionForNewItem(item);
            item.setPosition(position[0], position[1]);

            // Set the default color from preset
            item.setColor(currentPreset.getDefaultColor());

            // Add to design state
            designState.addFurnitureItem(item);

            // Show confirmation
            JOptionPane.showMessageDialog(this,
                "Added \"" + currentPreset.getName() + "\" to room.\nNavigating to 2D editor...",
                "Furniture Added",
                JOptionPane.INFORMATION_MESSAGE);

            // Navigate to 2D editor
            if (goTo2DEditorCallback != null) {
                goTo2DEditorCallback.run();
            }
        }
    }

    private void handleView3D() {
        if (currentPreset != null) {
            JOptionPane.showMessageDialog(this,
                "Viewing \"" + currentPreset.getName() + "\" in 3D.\nThe 3D renderer will use the furniture specifications to generate the shape.",
                "View 3D",
                JOptionPane.INFORMATION_MESSAGE);
            // 3D view navigation will be handled by member 5
        }
    }
}
