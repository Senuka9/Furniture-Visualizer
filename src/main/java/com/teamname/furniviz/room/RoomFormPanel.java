package com.teamname.furniviz.room;

import javax.swing.*;
import java.awt.*;

public class RoomFormPanel extends JPanel {

    private JTextField nameField;
    private JTextField widthField;
    private JTextField lengthField;
    private JComboBox<RoomShape> shapeBox;
    private JButton colorButton;
    private JButton saveButton;
    private JButton resetButton;
    private JButton backButton;
    private JButton roomsButton; // NEW: Button to go to Rooms page
    private JLabel errorLabel;
    private JLabel areaLabel;
    private JLabel shapeInfoLabel;
    private JLabel colorInfoLabel;
    private JLabel statusLabel;
    private RoomPreviewPanel previewPanel;
    private RoomController controller;
    private Color selectedColor;
    private Runnable onRoomsPressed; // NEW: Callback for Rooms button
    private boolean isEditMode = false;
    private Room editingRoom = null;

    public RoomFormPanel(RoomController controller) {
        this(controller, null, null);
    }

    public RoomFormPanel(RoomController controller, Runnable onBackPressed) {
        this(controller, onBackPressed, null);
    }

    public RoomFormPanel(RoomController controller, Runnable onBackPressed, Runnable onRoomsPressed) {
        this.controller = controller;
        this.selectedColor = Color.WHITE;
        this.onRoomsPressed = onRoomsPressed;
        initUI(onBackPressed);
    }

    public void setEditMode(Room room) {
        this.isEditMode = true;
        this.editingRoom = room;
        populateFields(room);
        // Update title
        JLabel title = new JLabel("Edit Room");
        // Title update will be handled through repaint
    }

    private void initUI(Runnable onBackPressed) {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(245, 245, 245));

        // ============ NORTH: Title & Back Button ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Create Your Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(33, 33, 33));
        topPanel.add(title, BorderLayout.CENTER);

        backButton = new JButton("<- Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(new Color(33, 33, 33));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        if (onBackPressed != null) {
            backButton.addActionListener(e -> onBackPressed.run());
        }
        topPanel.add(backButton, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));

        add(topPanel, BorderLayout.NORTH);

        // ============ CENTER: Form (LEFT) & Preview (RIGHT) ============
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // LEFT SIDE: Form Panel
        JPanel formPanel = createFormPanel();
        centerPanel.add(formPanel, BorderLayout.WEST);

        // RIGHT SIDE: Preview Panel + Info Card
        JPanel rightPanel = createRightPanel();
        centerPanel.add(rightPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        setupListeners();
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(300, 400));

        // Room Name (NEW FEATURE)
        JLabel nameLabel = new JLabel("Room Name (Optional)");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(new Color(66, 66, 66));
        panel.add(nameLabel);
        panel.add(Box.createVerticalStrut(5));

        nameField = new JTextField("Unnamed Room");
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nameField.setPreferredSize(new Dimension(260, 35));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(15));

        // Width
        JLabel widthLabel = new JLabel("Width (units)");
        widthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        widthLabel.setForeground(new Color(66, 66, 66));
        panel.add(widthLabel);
        panel.add(Box.createVerticalStrut(5));

        widthField = new JTextField();
        widthField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        widthField.setPreferredSize(new Dimension(260, 35));
        widthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        widthField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(widthField);
        panel.add(Box.createVerticalStrut(15));

        // Length
        JLabel lengthLabel = new JLabel("Length (units)");
        lengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lengthLabel.setForeground(new Color(66, 66, 66));
        panel.add(lengthLabel);
        panel.add(Box.createVerticalStrut(5));

        lengthField = new JTextField();
        lengthField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lengthField.setPreferredSize(new Dimension(260, 35));
        lengthField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        lengthField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(lengthField);
        panel.add(Box.createVerticalStrut(15));

        // Shape
        JLabel shapeLabel = new JLabel("Room Shape");
        shapeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        shapeLabel.setForeground(new Color(66, 66, 66));
        panel.add(shapeLabel);
        panel.add(Box.createVerticalStrut(5));

        shapeBox = new JComboBox<>(RoomShape.values());
        shapeBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        shapeBox.setPreferredSize(new Dimension(260, 35));
        shapeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(shapeBox);
        panel.add(Box.createVerticalStrut(15));

        // Color
        JLabel colorLabel = new JLabel("Wall Color");
        colorLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        colorLabel.setForeground(new Color(66, 66, 66));
        panel.add(colorLabel);
        panel.add(Box.createVerticalStrut(5));

        colorButton = new JButton("Choose Color");
        colorButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        colorButton.setPreferredSize(new Dimension(260, 35));
        colorButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        colorButton.setBackground(new Color(70, 130, 180));
        colorButton.setForeground(Color.WHITE);
        colorButton.setFocusPainted(false);
        colorButton.setBorderPainted(false);
        panel.add(colorButton);
        panel.add(Box.createVerticalStrut(20));

        // Error Label
        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);
        panel.add(Box.createVerticalStrut(10));

        // Save Button
        saveButton = new JButton("Save Room");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveButton.setPreferredSize(new Dimension(260, 45));
        saveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        saveButton.setBackground(new Color(76, 175, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveButton.setEnabled(false); // Disabled until form is valid
        panel.add(saveButton);
        panel.add(Box.createVerticalStrut(10));

        // Reset Button
        resetButton = new JButton("Reset Form");
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        resetButton.setPreferredSize(new Dimension(260, 40));
        resetButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        resetButton.setBackground(new Color(158, 158, 158));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(resetButton);
        panel.add(Box.createVerticalStrut(10));

        // Rooms Button - Navigate to Rooms page
        roomsButton = new JButton("📋 My Rooms");
        roomsButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roomsButton.setPreferredSize(new Dimension(260, 40));
        roomsButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        roomsButton.setBackground(new Color(103, 58, 183));
        roomsButton.setForeground(Color.WHITE);
        roomsButton.setFocusPainted(false);
        roomsButton.setBorderPainted(false);
        roomsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.add(roomsButton);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(new Color(245, 245, 245));

        // Preview Panel
        previewPanel = new RoomPreviewPanel();
        previewPanel.setPreferredSize(new Dimension(350, 300));
        previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.add(previewPanel, BorderLayout.CENTER);

        // Room Info Card
        JPanel infoCard = createInfoCard();
        panel.add(infoCard, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createInfoCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(255, 255, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(350, 160));

        // Title
        JLabel cardTitle = new JLabel("Room Information");
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cardTitle.setForeground(new Color(33, 33, 33));
        card.add(cardTitle);
        card.add(Box.createVerticalStrut(10));

        // Status Label (NEW)
        statusLabel = new JLabel("Status: Draft");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        statusLabel.setForeground(new Color(255, 193, 7));
        card.add(statusLabel);

        // Area
        areaLabel = new JLabel("Area: — m²");
        areaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        areaLabel.setForeground(new Color(66, 66, 66));
        card.add(areaLabel);

        // Shape
        shapeInfoLabel = new JLabel("Shape: —");
        shapeInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        shapeInfoLabel.setForeground(new Color(66, 66, 66));
        card.add(shapeInfoLabel);

        // Color
        colorInfoLabel = new JLabel("Color: RGB(255, 255, 255)");
        colorInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        colorInfoLabel.setForeground(new Color(66, 66, 66));
        card.add(colorInfoLabel);

        return card;
    }

    private void setupListeners() {
        // Width field - real-time validation + preview update
        widthField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            
            private void validateAndUpdate() {
                performRealTimeValidation();
                updatePreview();
                updateInfoCard();
            }
        });

        // Length field - real-time validation + preview update
        lengthField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                validateAndUpdate();
            }
            
            private void validateAndUpdate() {
                performRealTimeValidation();
                updatePreview();
                updateInfoCard();
            }
        });

        // Shape box - live preview update
        shapeBox.addActionListener(e -> {
            updatePreview();
            updateInfoCard();
        });

        // Color button
        colorButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(
                    this,
                    "Choose Wall Color",
                    selectedColor
            );
            if (selected != null) {
                selectedColor = selected;
                previewPanel.setWallColor(selected);
                controller.setSelectedColor(selected);
                updateInfoCard();
            }
        });

        // Save button
        saveButton.addActionListener(e -> {
            try {
                String roomName = nameField.getText().trim();
                if (roomName.isEmpty()) {
                    roomName = "Unnamed Room";
                }
                double width = Double.parseDouble(widthField.getText());
                double length = Double.parseDouble(lengthField.getText());
                RoomShape shape = (RoomShape) shapeBox.getSelectedItem();

                if (isEditMode && editingRoom != null) {
                    // Update existing room
                    controller.updateRoom(editingRoom, roomName, width, length, shape, selectedColor);
                    errorLabel.setText("[OK] Room updated successfully!");
                } else {
                    // Create new room
                    controller.createRoom(roomName, width, length, shape);
                    errorLabel.setText("[OK] Room created successfully!");
                }

                errorLabel.setForeground(new Color(76, 175, 80));
                updatePreview();
                updateInfoCard();

            } catch (NumberFormatException ex) {
                errorLabel.setText("[ERROR] Please enter valid numbers.");
                errorLabel.setForeground(Color.RED);
                saveButton.setEnabled(false);
            } catch (IllegalArgumentException ex) {
                errorLabel.setText("[ERROR] " + ex.getMessage());
                errorLabel.setForeground(Color.RED);
                saveButton.setEnabled(false);
            }
        });

        // Reset button
        resetButton.addActionListener(e -> {
            nameField.setText("Unnamed Room");
            widthField.setText("");
            lengthField.setText("");
            shapeBox.setSelectedIndex(0);
            selectedColor = Color.WHITE;
            previewPanel.setWallColor(Color.WHITE);
            colorButton.setBackground(new Color(70, 130, 180));
            errorLabel.setText("");
            controller.setSelectedColor(Color.WHITE);
            previewPanel.setRoomData(0, 0, RoomShape.RECTANGLE);
            previewPanel.repaint();
            updateInfoCard();
            saveButton.setEnabled(false);
            isEditMode = false;
        });

        // Rooms button - Navigate to My Rooms page
        roomsButton.addActionListener(e -> {
            if (onRoomsPressed != null) {
                onRoomsPressed.run();
            }
        });
    }

    /**
     * Real-time validation for width and length fields
     * Shows error immediately and disables Save button if invalid
     */
    private void performRealTimeValidation() {
        String widthText = widthField.getText().trim();
        String lengthText = lengthField.getText().trim();

        // Check if fields are empty
        if (widthText.isEmpty() || lengthText.isEmpty()) {
            errorLabel.setText("");
            saveButton.setEnabled(false);
            return;
        }

        // Try to parse as numbers
        double width = 0;
        double length = 0;
        boolean widthValid = true;
        boolean lengthValid = true;

        try {
            width = Double.parseDouble(widthText);
        } catch (NumberFormatException e) {
            widthValid = false;
        }

        try {
            length = Double.parseDouble(lengthText);
        } catch (NumberFormatException e) {
            lengthValid = false;
        }

        // Show error if either is not a valid number
        if (!widthValid || !lengthValid) {
            errorLabel.setText("[ERROR] Please enter valid numbers");
            errorLabel.setForeground(Color.RED);
            saveButton.setEnabled(false);
            return;
        }

        // Validate width
        if (width <= 0) {
            errorLabel.setText("[ERROR] Width must be greater than 0");
            errorLabel.setForeground(Color.RED);
            saveButton.setEnabled(false);
            return;
        }

        if (width > 100) {
            errorLabel.setText("[ERROR] Width must be 100 or less");
            errorLabel.setForeground(Color.RED);
            saveButton.setEnabled(false);
            return;
        }

        // Validate length
        if (length <= 0) {
            errorLabel.setText("[ERROR] Length must be greater than 0");
            errorLabel.setForeground(Color.RED);
            saveButton.setEnabled(false);
            return;
        }

        if (length > 100) {
            errorLabel.setText("[ERROR] Length must be 100 or less");
            errorLabel.setForeground(Color.RED);
            saveButton.setEnabled(false);
            return;
        }

        // All validations passed - enable Save button and clear error
        errorLabel.setText("");
        saveButton.setEnabled(true);
    }

    private void updatePreview() {
        try {
            double width = Double.parseDouble(widthField.getText());
            double length = Double.parseDouble(lengthField.getText());
            RoomShape shape = (RoomShape) shapeBox.getSelectedItem();

            previewPanel.setRoomData(width, length, shape);
            previewPanel.repaint();
        } catch (NumberFormatException ex) {
            // Silently ignore if fields are invalid
        }
    }

    private void updateInfoCard() {
        try {
            double width = Double.parseDouble(widthField.getText());
            double length = Double.parseDouble(lengthField.getText());
            double area = width * length;
            RoomShape shape = (RoomShape) shapeBox.getSelectedItem();

            areaLabel.setText(String.format("Area: %.2f m²", area));
            shapeInfoLabel.setText("Shape: " + (shape != null ? shape.toString() : "—"));

            int r = selectedColor.getRed();
            int g = selectedColor.getGreen();
            int b = selectedColor.getBlue();
            colorInfoLabel.setText(String.format("Color: RGB(%d, %d, %d)", r, g, b));

            // Update status label
            Room currentRoom = controller.getRoomGeometry();
            if (currentRoom != null) {
                Room.RoomStatus status = currentRoom.getStatus();
                String statusText = "Status: " + status.toString();
                Color statusColor = getStatusColor(status);
                statusLabel.setText(statusText);
                statusLabel.setForeground(statusColor);
            } else {
                statusLabel.setText("Status: Draft");
                statusLabel.setForeground(new Color(255, 193, 7));
            }
        } catch (NumberFormatException ex) {
            areaLabel.setText("Area: — m²");
            shapeInfoLabel.setText("Shape: —");
            colorInfoLabel.setText("Color: RGB(255, 255, 255)");
            statusLabel.setText("Status: Draft");
            statusLabel.setForeground(new Color(255, 193, 7));
        }
    }

    /**
     * Get color for room status display
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
     * Reset form to empty state
     */
    private void resetForm() {
        nameField.setText("Unnamed Room");
        widthField.setText("");
        lengthField.setText("");
        shapeBox.setSelectedIndex(0);
        selectedColor = Color.WHITE;
        previewPanel.setWallColor(Color.WHITE);
        colorButton.setBackground(new Color(70, 130, 180));
        errorLabel.setText("");
        controller.setSelectedColor(Color.WHITE);
        previewPanel.setRoomData(0, 0, RoomShape.RECTANGLE);
        previewPanel.repaint();
        updateInfoCard();
        saveButton.setEnabled(false);
        isEditMode = false;
        editingRoom = null;
    }

    // Helper methods to populate fields
    public void populateFields(Room room) {
        if (room != null) {
            nameField.setText(room.getName() != null ? room.getName() : "Unnamed Room");
            widthField.setText(String.valueOf(room.getWidth()));
            lengthField.setText(String.valueOf(room.getLength()));
            shapeBox.setSelectedItem(room.getShape());
            selectedColor = room.getWallColor();
            previewPanel.setWallColor(selectedColor);
            updatePreview();
            updateInfoCard();
        }
    }

    // Getter for selected color
    public Color getSelectedColor() {
        return selectedColor;
    }

    // Getter for form values
    public Room getRoomFromForm() throws NumberFormatException {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            name = "Unnamed Room";
        }
        double width = Double.parseDouble(widthField.getText());
        double length = Double.parseDouble(lengthField.getText());
        RoomShape shape = (RoomShape) shapeBox.getSelectedItem();
        return new Room(name, width, length, shape, selectedColor);
    }

    // Clear error message
    public void clearError() {
        errorLabel.setText("");
    }

    // Show error message
    public void showError(String message) {
        errorLabel.setText("[ERROR] " + message);
        errorLabel.setForeground(Color.RED);
    }

    /**
     * Get current room from controller
     */
    public Room getCurrentRoom() {
        return controller.getRoomGeometry();
    }
}
