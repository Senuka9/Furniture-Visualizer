package com.teamname.furniviz.renderer3d;

import com.teamname.furniviz.app.DesignState;

import javax.swing.*;
import java.awt.*;

/**
 * View3DPanel - Main 3D view UI panel
 *
 * Responsibilities:
 * - Create and manage the 3D canvas
 * - Provide camera preset controls
 * - Display status information
 * - Handle integration with Navigator
 *
 * This panel is shown in Navigator's CardLayout when user clicks "View 3D" button
 */
public class View3DPanel extends JPanel {

    private DesignState designState;
    private Renderer3D renderer3D;
    private Canvas3D canvas3D;
    private Runnable onBackCallback;

    /**
     * Constructor
     * @param designState Shared design state (room + furniture)
     * @param onBackCallback Callback when user clicks "Back to 2D"
     */
    public View3DPanel(DesignState designState, Runnable onBackCallback) {
        this.designState = designState;
        this.onBackCallback = onBackCallback;

        // Setup layout
        setLayout(new BorderLayout(5, 5));
        setBackground(new Color(50, 50, 50));

        // Create top control panel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Create and initialize renderer
        this.renderer3D = new Renderer3D(designState);

        // Create canvas for 3D rendering
        this.canvas3D = new Canvas3D(renderer3D);
        add(canvas3D, BorderLayout.CENTER);

        // Create bottom status panel
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Create top control panel with camera presets and buttons
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(100, 100, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Title label
        JLabel titleLabel = new JLabel("3D View - Camera Presets:");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(titleLabel);

        // Camera preset buttons
        JButton isometricBtn = new JButton("Isometric");
        isometricBtn.addActionListener(e -> {
            renderer3D.getCamera().setIsometric();
            canvas3D.repaint();
        });
        panel.add(isometricBtn);

        JButton topDownBtn = new JButton("Top-Down");
        topDownBtn.addActionListener(e -> {
            renderer3D.getCamera().setTopDown();
            canvas3D.repaint();
        });
        panel.add(topDownBtn);

        JButton frontBtn = new JButton("Front");
        frontBtn.addActionListener(e -> {
            renderer3D.getCamera().setFront();
            canvas3D.repaint();
        });
        panel.add(frontBtn);

        JButton leftBtn = new JButton("Left Side");
        leftBtn.addActionListener(e -> {
            renderer3D.getCamera().setLeftSide();
            canvas3D.repaint();
        });
        panel.add(leftBtn);

        // Separator
        panel.add(Box.createHorizontalGlue());

        // Back to 2D button
        JButton backBtn = new JButton("Back to 2D");
        backBtn.setBackground(new Color(200, 100, 100));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            cleanup();
            if (onBackCallback != null) {
                onBackCallback.run();
            }
        });
        panel.add(backBtn);

        return panel;
    }

    /**
     * Create bottom status panel
     */
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(new Color(100, 100, 100));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // FPS label
        JLabel fpsLabel = new JLabel("FPS: 0");
        fpsLabel.setForeground(Color.WHITE);
        fpsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(fpsLabel);

        // Furniture count label
        JLabel furnitureLabel = new JLabel("Furniture Items: 0");
        furnitureLabel.setForeground(Color.WHITE);
        furnitureLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(furnitureLabel);

        // Update labels periodically
        Timer updateTimer = new Timer(500, e -> {
            if (canvas3D != null) {
                fpsLabel.setText(String.format("FPS: %.1f", canvas3D.getFPS()));
                furnitureLabel.setText("Furniture Items: " + designState.getFurnitureItems().size());
            }
        });
        updateTimer.start();

        // Help text
        panel.add(Box.createHorizontalGlue());
        JLabel helpLabel = new JLabel("Drag to rotate | Scroll to zoom | 1-4: camera presets | Space: reset");
        helpLabel.setForeground(Color.LIGHT_GRAY);
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        panel.add(helpLabel);

        return panel;
    }

    /**
     * Refresh the 3D view (call this when returning from 2D editor)
     */
    public void refresh() {
        if (renderer3D != null) {
            renderer3D.update();
        }
    }

    /**
     * Cleanup resources before switching panels
     */
    private void cleanup() {
        if (canvas3D != null) {
            canvas3D.cleanup();
        }
    }

    /**
     * Get the renderer (for debugging/testing)
     */
    public Renderer3D getRenderer() {
        return renderer3D;
    }

    /**
     * Get the canvas (for debugging/testing)
     */
    public Canvas3D getCanvas() {
        return canvas3D;
    }
}
