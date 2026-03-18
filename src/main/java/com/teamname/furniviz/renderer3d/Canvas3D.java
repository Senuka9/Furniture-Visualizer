package com.teamname.furniviz.renderer3d;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Canvas3D - Swing JPanel that renders 3D graphics
 *
 * Responsibilities:
 * - Manage render loop (timer-based painting)
 * - Handle mouse and keyboard input
 * - Track frame rate
 * - Delegate actual rendering to Renderer3D
 *
 * This panel acts as the 3D view surface within Swing.
 * It uses a timer to continuously render and update the display.
 */
public class Canvas3D extends JPanel {

    private Renderer3D renderer;
    private Timer renderTimer;
    private BufferedImage backBuffer;
    private Graphics2D bufferGraphics;

    // Performance tracking
    private long lastFrameTime = 0;
    private int frameCount = 0;
    private float fps = 0;
    private long fpsUpdateTime = 0;

    // Input state
    private boolean[] keysPressed = new boolean[256];
    private int lastMouseX = 0;
    private int lastMouseY = 0;
    private boolean isMousePressed = false;

    /**
     * Constructor - Initialize the 3D canvas
     */
    public Canvas3D(Renderer3D renderer) {
        this.renderer = renderer;

        // Setup panel properties
        setBackground(new Color(50, 50, 50));  // Dark gray background
        setPreferredSize(new Dimension(1024, 768));
        setDoubleBuffered(false);  // We handle buffering manually

        // Setup input listeners
        setupMouseListeners();
        setupKeyboardListeners();

        // Setup render loop
        renderTimer = new Timer(16, e -> repaint());  // ~60 FPS (16ms per frame)
        renderTimer.start();

        // Setup focus
        setFocusable(true);
    }

    /**
     * Setup mouse event handlers
     */
    private void setupMouseListeners() {
        MouseListener ml = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isMousePressed = true;
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                Canvas3D.this.requestFocus();  // Ensure focus for keyboard input
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isMousePressed = false;
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Scroll wheel = zoom
                float zoomFactor = 1.1f;
                if (e.getWheelRotation() > 0) {
                    renderer.getCamera().zoom(1.0f / zoomFactor);  // Zoom out
                } else {
                    renderer.getCamera().zoom(zoomFactor);  // Zoom in
                }
            }
        };

        addMouseListener(ml);
        addMouseWheelListener((MouseWheelListener) ml);

        // Mouse motion for camera rotation
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isMousePressed) {
                    int deltaX = e.getX() - lastMouseX;
                    int deltaY = e.getY() - lastMouseY;

                    // Convert pixel movement to camera rotation (delta values, not accumulated)
                    float deltaRotationY = deltaX * 0.3f;  // Horizontal rotation (reduced sensitivity)
                    float deltaRotationX = deltaY * 0.3f;  // Vertical rotation (reduced sensitivity)

                    // Apply camera orbit with delta values only
                    renderer.getCamera().orbit(deltaRotationY, deltaRotationX);

                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
            }
        });
    }

    /**
     * Setup keyboard event handlers
     */
    private void setupKeyboardListeners() {
        KeyListener kl = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() < 256) {
                    keysPressed[e.getKeyCode()] = true;
                }

                // Handle special keys
                handleKeyboardInput(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() < 256) {
                    keysPressed[e.getKeyCode()] = false;
                }
            }
        };

        addKeyListener(kl);
    }

    /**
     * Handle special keyboard inputs for camera control
     */
    private void handleKeyboardInput(KeyEvent e) {
        Camera camera = renderer.getCamera();

        switch (e.getKeyCode()) {
            case KeyEvent.VK_1:
                // Preset: Isometric
                camera.setIsometric();
                break;
            case KeyEvent.VK_2:
                // Preset: Top-down
                camera.setTopDown();
                break;
            case KeyEvent.VK_3:
                // Preset: Front view
                camera.setFront();
                break;
            case KeyEvent.VK_4:
                // Preset: Left side
                camera.setLeftSide();
                break;
            case KeyEvent.VK_SPACE:
                // Reset to isometric
                camera.setIsometric();
                break;
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS:
                // Zoom in
                camera.zoom(1.1f);
                break;
            case KeyEvent.VK_MINUS:
                // Zoom out
                camera.zoom(0.9f);
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                // Pan up
                camera.pan(0, 0.5f);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                // Pan down
                camera.pan(0, -0.5f);
                break;
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                // Pan left
                camera.pan(-0.5f, 0);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                // Pan right
                camera.pan(0.5f, 0);
                break;
        }
    }

    /**
     * Render the 3D scene
     * Called by the render timer ~60 times per second
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Get canvas dimensions
        int width = getWidth();
        int height = getHeight();

        // Create or recreate back buffer if size changed
        if (backBuffer == null || backBuffer.getWidth() != width || backBuffer.getHeight() != height) {
            backBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bufferGraphics = backBuffer.createGraphics();
        }

        // Clear the buffer
        bufferGraphics.setColor(getBackground());
        bufferGraphics.fillRect(0, 0, width, height);

        // Render the 3D scene
        if (renderer != null) {
            try {
                renderer.render(bufferGraphics, width, height);
            } catch (Exception e) {
                // Graceful error handling
                bufferGraphics.setColor(Color.RED);
                bufferGraphics.setFont(new Font("Arial", Font.PLAIN, 12));
                bufferGraphics.drawString("Rendering error: " + e.getMessage(), 10, 20);
            }
        }

        // Draw FPS counter
        drawFPSCounter(bufferGraphics);

        // Copy back buffer to screen
        g2d.drawImage(backBuffer, 0, 0, null);

        // Update FPS
        updateFPS();
    }

    /**
     * Draw FPS counter on screen
     */
    private void drawFPSCounter(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        g.drawString(String.format("FPS: %.1f", fps), 10, 20);
        g.drawString(String.format("Objects: %d", renderer.getSceneGraph().getAllNodes().size()), 10, 35);
    }

    /**
     * Update FPS calculation
     */
    private void updateFPS() {
        long currentTime = System.currentTimeMillis();

        if (lastFrameTime == 0) {
            lastFrameTime = currentTime;
        }

        long elapsed = currentTime - lastFrameTime;
        lastFrameTime = currentTime;

        frameCount++;

        // Update FPS once per second
        if (currentTime - fpsUpdateTime >= 1000) {
            fps = frameCount * 1000.0f / (currentTime - fpsUpdateTime);
            frameCount = 0;
            fpsUpdateTime = currentTime;
        }
    }

    /**
     * Cleanup resources when canvas is destroyed
     */
    public void cleanup() {
        if (renderTimer != null) {
            renderTimer.stop();
        }
        if (bufferGraphics != null) {
            bufferGraphics.dispose();
        }
    }

    /**
     * Get the renderer
     */
    public Renderer3D getRenderer() {
        return renderer;
    }

    /**
     * Get current FPS
     */
    public float getFPS() {
        return fps;
    }
}
