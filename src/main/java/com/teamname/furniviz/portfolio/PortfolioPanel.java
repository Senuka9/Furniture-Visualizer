package com.teamname.furniviz.portfolio;

import com.teamname.furniviz.auth.Session;
import com.teamname.furniviz.storage.DesignModel;
import com.teamname.furniviz.storage.DesignStorage;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;

/**
 * Portfolio Panel - Gallery view of user's saved designs
 * 
 * Features:
 * - Grid layout showing all user's saved designs (3 columns)
 * - Design cards with thumbnail, name, info, and action buttons
 * - Search designs by name
 * - Sort designs (date newest, date oldest, name A-Z, name Z-A)
 * - Edit/Delete buttons
 * - Design count display
 */
public class PortfolioPanel extends JPanel {
    
    private DesignStorage designStorage;
    private String currentUserId;
    private List<DesignModel> userDesigns;
    private JPanel gridPanel;
    private JTextField searchField;
    private JComboBox<String> sortCombo;
    private JLabel countLabel;
    private Runnable onBackCallback;
    private Runnable onLoadDesign;
    
    public PortfolioPanel(Runnable onBackCallback, Runnable onLoadDesign) {
        this.onBackCallback = onBackCallback;
        this.onLoadDesign = onLoadDesign;
        this.designStorage = new DesignStorage();
        this.currentUserId = Session.getInstance().getCurrentUser().getUserId();
        
        initUI();
        loadUserDesigns();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ============ NORTH: Title and Back Button ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        
        JLabel title = new JLabel("Portfolio - My Designs");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(33, 33, 33));
        topPanel.add(title, BorderLayout.WEST);
        
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(new Color(33, 33, 33));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> onBackCallback.run());
        topPanel.add(backButton, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // ============ BEFORE_FIRST_LINE: Filter/Search Bar ============
        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.BEFORE_FIRST_LINE);
        
        // ============ CENTER: Grid of Design Cards ============
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 3, 15, 15));  // 3 columns
        gridPanel.setBackground(new Color(245, 245, 245));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        scrollPane.setViewportView(gridPanel);
        add(scrollPane, BorderLayout.CENTER);
        
        // ============ SOUTH: Info ============
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 245, 245));
        countLabel = new JLabel("Designs: 0");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        bottomPanel.add(countLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Search field
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(searchLabel);
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchField.setText("Search by name...");
        panel.add(searchField);
        
        // Search button
        JButton searchButton = new JButton("SEARCH");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        searchButton.setBackground(new Color(76, 175, 80));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setBorderPainted(false);
        searchButton.addActionListener(e -> performSearch());
        panel.add(searchButton);
        
        // Sort combo
        panel.add(new JLabel("Sort by:"));
        sortCombo = new JComboBox<>(new String[]{
                "Date (Newest)",
                "Date (Oldest)",
                "Name (A-Z)",
                "Name (Z-A)"
        });
        sortCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sortCombo.addActionListener(e -> loadUserDesigns());
        panel.add(sortCombo);
        
        // Refresh button
        JButton refreshButton = new JButton("REFRESH");
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        refreshButton.setBackground(new Color(158, 158, 158));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> loadUserDesigns());
        panel.add(refreshButton);
        
        return panel;
    }
    
    private void loadUserDesigns() {
        // Get designs from storage
        userDesigns = designStorage.getDesignsByUserId(currentUserId);
        
        // Sort based on selection
        sortDesigns();
        
        // Render grid
        renderGrid();
        
        // Update count
        countLabel.setText("Designs: " + userDesigns.size());
    }
    
    private void sortDesigns() {
        String sortBy = (String) sortCombo.getSelectedItem();
        
        if ("Date (Newest)".equals(sortBy)) {
            userDesigns.sort((a, b) -> b.getLastModifiedAt()
                    .compareTo(a.getLastModifiedAt()));
        } else if ("Date (Oldest)".equals(sortBy)) {
            userDesigns.sort((a, b) -> a.getLastModifiedAt()
                    .compareTo(b.getLastModifiedAt()));
        } else if ("Name (A-Z)".equals(sortBy)) {
            userDesigns.sort((a, b) -> a.getDesignName()
                    .compareTo(b.getDesignName()));
        } else if ("Name (Z-A)".equals(sortBy)) {
            userDesigns.sort((a, b) -> b.getDesignName()
                    .compareTo(a.getDesignName()));
        }
    }
    
    private void renderGrid() {
        gridPanel.removeAll();
        
        if (userDesigns.isEmpty()) {
            JLabel emptyLabel = new JLabel("No designs yet. Create one!");
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            emptyLabel.setForeground(new Color(150, 150, 150));
            gridPanel.add(emptyLabel);
        } else {
            for (DesignModel design : userDesigns) {
                JPanel card = createDesignCard(design);
                gridPanel.add(card);
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }
    
    private JPanel createDesignCard(DesignModel design) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Thumbnail
        JLabel thumbnailLabel = new JLabel();
        thumbnailLabel.setPreferredSize(new Dimension(200, 150));
        thumbnailLabel.setMaximumSize(new Dimension(200, 150));
        thumbnailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        if (design.getThumbnail() != null) {
            try {
                ImageIcon icon = new ImageIcon(design.getThumbnail());
                Image scaled = icon.getImage().getScaledInstance(200, 150, 
                        Image.SCALE_SMOOTH);
                thumbnailLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                thumbnailLabel.setText("No Image");
                thumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } else {
            thumbnailLabel.setText("No Preview");
            thumbnailLabel.setHorizontalAlignment(SwingConstants.CENTER);
            thumbnailLabel.setBackground(new Color(220, 220, 220));
            thumbnailLabel.setOpaque(true);
        }
        card.add(thumbnailLabel);
        card.add(Box.createVerticalStrut(10));
        
        // Design Name
        JLabel nameLabel = new JLabel(design.getDesignName());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(nameLabel);
        
        // Room Info
        if (design.getRoom() != null) {
            String roomInfo = String.format("Room: %.1f × %.1f m",
                    design.getRoom().getWidth(),
                    design.getRoom().getLength());
            JLabel roomLabel = new JLabel(roomInfo);
            roomLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            roomLabel.setForeground(new Color(100, 100, 100));
            roomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(roomLabel);
        }
        
        // Furniture Count
        int itemCount = design.getFurnitureItems() != null ? 
                design.getFurnitureItems().size() : 0;
        JLabel countLbl = new JLabel(itemCount + " furniture items");
        countLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        countLbl.setForeground(new Color(100, 100, 100));
        countLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(countLbl);
        
        // Date
        String dateStr = design.getCreatedAt().toLocalDate().toString();
        JLabel dateLabel = new JLabel("Created: " + dateStr);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        dateLabel.setForeground(new Color(150, 150, 150));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(dateLabel);
        
        card.add(Box.createVerticalGlue());
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton editBtn = new JButton("EDIT");
        editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        editBtn.setPreferredSize(new Dimension(60, 30));
        editBtn.setBackground(new Color(76, 175, 80));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.setBorderPainted(false);
        editBtn.addActionListener(e -> loadDesign(design));
        buttonPanel.add(editBtn);
        
        JButton exportBtn = new JButton("EXPORT");
        exportBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        exportBtn.setPreferredSize(new Dimension(70, 30));
        exportBtn.setBackground(new Color(33, 150, 243));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFocusPainted(false);
        exportBtn.setBorderPainted(false);
        exportBtn.addActionListener(e -> exportDesign(design));
        buttonPanel.add(exportBtn);
        
        JButton deleteBtn = new JButton("DELETE");
        deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        deleteBtn.setPreferredSize(new Dimension(70, 30));
        deleteBtn.setBackground(new Color(244, 67, 54));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorderPainted(false);
        deleteBtn.addActionListener(e -> deleteDesign(design));
        buttonPanel.add(deleteBtn);
        
        card.add(buttonPanel);
        
        return card;
    }
    
    private void performSearch() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty() || "Search by name...".equals(searchTerm)) {
            loadUserDesigns();
        } else {
            userDesigns = designStorage.searchByName(currentUserId, searchTerm);
            sortDesigns();
            renderGrid();
            countLabel.setText("Found: " + userDesigns.size());
        }
    }
    
    private void loadDesign(DesignModel design) {
        try {
            // Load the design data into DesignState for editing
            if (design.getRoom() != null) {
                // Get the DesignState from somewhere accessible
                // For now, we'll need to pass a callback or navigate directly
                
                // TODO: Need to get DesignState reference to populate it
                // This is a limitation - we need DesignState passed to PortfolioPanel
                
                JOptionPane.showMessageDialog(this, 
                        "Design: " + design.getDesignName() + 
                        "\nRoom: " + design.getRoom().getWidth() + "m x " + design.getRoom().getLength() + "m" +
                        "\nItems: " + design.getFurnitureItems().size() +
                        "\n\nNote: Click Edit from Portfolio to load for editing",
                        "Design Details", 
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Error: Design has no room data", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error loading design: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void exportDesign(DesignModel design) {
        try {
            // Create a simple JSON export of the design
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"designName\": \"").append(design.getDesignName()).append("\",\n");
            json.append("  \"designId\": \"").append(design.getDesignId()).append("\",\n");
            json.append("  \"createdAt\": \"").append(design.getCreatedAt()).append("\",\n");
            
            if (design.getRoom() != null) {
                json.append("  \"room\": {\n");
                json.append("    \"name\": \"").append(design.getRoom().getName()).append("\",\n");
                json.append("    \"width\": ").append(design.getRoom().getWidth()).append(",\n");
                json.append("    \"length\": ").append(design.getRoom().getLength()).append("\n");
                json.append("  },\n");
            }
            
            int itemCount = design.getFurnitureItems() != null ? design.getFurnitureItems().size() : 0;
            json.append("  \"furnitureCount\": ").append(itemCount).append("\n");
            json.append("}\n");
            
            // Show export options
            int result = JOptionPane.showConfirmDialog(this,
                    "Export design as JSON?\n\nDesign: " + design.getDesignName() +
                    "\nRoom: " + (design.getRoom() != null ? design.getRoom().getWidth() + "m x " + design.getRoom().getLength() + "m" : "N/A") +
                    "\nItems: " + itemCount +
                    "\n\nClick YES to copy JSON to details",
                    "Export Design",
                    JOptionPane.YES_NO_OPTION);
            
            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                        "Design exported successfully!\n\nJSON Data:\n\n" + json.toString(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                    "Error exporting design: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void deleteDesign(DesignModel design) {
        int response = JOptionPane.showConfirmDialog(this,
                "Delete design: " + design.getDesignName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            if (designStorage.deleteDesign(design.getDesignId())) {
                JOptionPane.showMessageDialog(this, "Design deleted successfully!");
                loadUserDesigns();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting design", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
