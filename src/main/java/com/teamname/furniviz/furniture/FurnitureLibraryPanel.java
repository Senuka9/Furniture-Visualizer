package com.teamname.furniviz.furniture;

import com.teamname.furniviz.app.DesignState;
import javax.swing.*;
import javax.swing.DefaultListModel;
import java.awt.*;
import java.util.List;

public class FurnitureLibraryPanel extends JPanel {
    private FurnitureController controller;
    private JList<FurniturePreset> furnitureList;
    private DefaultListModel<FurniturePreset> listModel;
    private PropertiesPanel propertiesPanel;
    private Runnable backCallback;
    private Runnable goTo2DEditorCallback;
    private DesignState designState;
    private JComboBox<String> categoryFilter;

    public FurnitureLibraryPanel(DesignState designState, Runnable backCallback, Runnable goTo2DEditorCallback) {
        this.designState = designState;
        this.backCallback = backCallback;
        this.goTo2DEditorCallback = goTo2DEditorCallback;
        this.controller = new FurnitureController();

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ============ NORTH: Title & Back Button ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        JLabel title = new JLabel("Furniture Library");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));
        topPanel.add(title, BorderLayout.CENTER);

        JButton backButton = new JButton("<- Back to Home");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(140, 40));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(new Color(33, 33, 33));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> backCallback.run());
        topPanel.add(backButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: Split Pane with Left & Right ============
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBackground(new Color(245, 245, 245));

        // LEFT: List of furniture with filter
        JPanel leftPanel = createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        // RIGHT: Properties panel
        propertiesPanel = new PropertiesPanel(designState, goTo2DEditorCallback);
        splitPane.setRightComponent(propertiesPanel);

        splitPane.setDividerLocation(320);
        splitPane.setResizeWeight(0.3);
        add(splitPane, BorderLayout.CENTER);

        // Select first item by default
        List<FurniturePreset> items = controller.getFurniturePresets();
        if (!items.isEmpty()) {
            furnitureList.setSelectedIndex(0);
        }
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(new Color(245, 245, 245));
        filterPanel.add(new JLabel("Category:"));

        categoryFilter = new JComboBox<>(new String[]{
            "All", "Chairs", "Tables", "Sofas", "Beds", "Desks", "Cabinets", "Lamps"
        });
        categoryFilter.addActionListener(e -> filterFurnitureList());
        filterPanel.add(categoryFilter);

        panel.add(filterPanel, BorderLayout.NORTH);

        // List
        List<FurniturePreset> items = controller.getFurniturePresets();
        listModel = new DefaultListModel<>();
        for (FurniturePreset item : items) {
            listModel.addElement(item);
        }
        furnitureList = new JList<>(listModel);
        furnitureList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        furnitureList.setFixedCellHeight(70);
        furnitureList.setCellRenderer(new FurniturePresetListCellRenderer());
        furnitureList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                FurniturePreset selected = furnitureList.getSelectedValue();
                propertiesPanel.displayPreset(selected);
            }
        });

        JScrollPane listScrollPane = new JScrollPane(furnitureList);
        panel.add(listScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void filterFurnitureList() {
        String selectedCategory = (String) categoryFilter.getSelectedItem();
        List<FurniturePreset> allItems = controller.getFurniturePresets();

        listModel.clear();

        if (selectedCategory.equals("All")) {
            for (FurniturePreset item : allItems) {
                listModel.addElement(item);
            }
        } else {
            FurnitureType typeToFilter = getCategoryType(selectedCategory);
            for (FurniturePreset item : allItems) {
                if (item.getType() == typeToFilter) {
                    listModel.addElement(item);
                }
            }
        }
    }

    private FurnitureType getCategoryType(String categoryName) {
        switch (categoryName) {
            case "Chairs": return FurnitureType.CHAIR;
            case "Tables": return FurnitureType.TABLE;
            case "Sofas": return FurnitureType.SOFA;
            case "Beds": return FurnitureType.BED;
            case "Desks": return FurnitureType.DESK;
            case "Cabinets": return FurnitureType.CABINET;
            case "Lamps": return FurnitureType.LAMP;
            default: return FurnitureType.OTHER;
        }
    }

    // Custom cell renderer for the list to show images and names
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
}
