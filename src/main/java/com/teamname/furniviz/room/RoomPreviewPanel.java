package com.teamname.furniviz.room;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class RoomPreviewPanel extends JPanel {

    private double roomWidth;
    private double roomLength;
    private RoomShape roomShape;
    private Color wallColor;

    private static final int PADDING = 60; // Space around edges for labels

    public RoomPreviewPanel() {
        this.roomWidth = 0;
        this.roomLength = 0;
        this.roomShape = RoomShape.RECTANGLE;
        this.wallColor = Color.WHITE;
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    }

    public void setRoomData(double width, double length, RoomShape shape) {
        this.roomWidth = width;
        this.roomLength = length;
        this.roomShape = shape;
        repaint();
    }

    public void setWallColor(Color color) {
        this.wallColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable high-quality rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        // If no dimensions, show placeholder
        if (roomWidth == 0 || roomLength == 0) {
            drawPlaceholder(g2d);
            return;
        }

        // Calculate available space for room
        int availableWidth = getWidth() - (2 * PADDING);
        int availableHeight = getHeight() - (2 * PADDING);

        // Auto-scale to fit room while maintaining aspect ratio
        double scaleX = availableWidth / roomWidth;
        double scaleY = availableHeight / roomLength;
        double scale = Math.min(scaleX, scaleY);

        // Calculate scaled dimensions
        int scaledWidth = (int) (roomWidth * scale);
        int scaledLength = (int) (roomLength * scale);

        // Center the room in the available space
        int startX = PADDING + (availableWidth - scaledWidth) / 2;
        int startY = PADDING + (availableHeight - scaledLength) / 2;

        // Draw grid background
        drawGridBackground(g2d, startX, startY, scaledWidth, scaledLength);

        // Draw room based on shape
        drawRoom(g2d, startX, startY, scaledWidth, scaledLength, scale);

        // Draw dimension labels on edges
        drawDimensionLabels(g2d, startX, startY, scaledWidth, scaledLength);

        // Draw measurements scale info at bottom
        drawScaleInfo(g2d, scale);
    }

    private void drawPlaceholder(Graphics2D g2d) {
        // Placeholder text
        String text = "Enter room dimensions to preview";
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        g2d.setFont(font);
        g2d.setColor(new Color(150, 150, 150));

        FontMetrics fm = g2d.getFontMetrics(font);
        int textX = (getWidth() - fm.stringWidth(text)) / 2;
        int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

        g2d.drawString(text, textX, textY);
    }

    private void drawGridBackground(Graphics2D g2d, int x, int y, int width, int height) {
        // Light grid pattern
        g2d.setColor(new Color(230, 230, 230));
        g2d.setStroke(new BasicStroke(0.5f));

        int gridSize = 20;
        for (int i = x; i <= x + width; i += gridSize) {
            g2d.drawLine(i, y, i, y + height);
        }
        for (int j = y; j <= y + height; j += gridSize) {
            g2d.drawLine(x, j, x + width, j);
        }
    }

    private void drawRoom(Graphics2D g2d, int x, int y, int width, int height, double scale) {
        switch (roomShape) {
            case RECTANGLE:
            case SQUARE:
                drawRectangleRoom(g2d, x, y, width, height);
                break;
            case L_SHAPE:
                drawLShapeRoom(g2d, x, y, width, height);
                break;
        }
    }

    private void drawRectangleRoom(Graphics2D g2d, int x, int y, int width, int height) {
        // Fill with wall color
        g2d.setColor(wallColor);
        g2d.fillRect(x, y, width, height);

        // Draw border with shadow effect
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(x, y, width, height);

        // Draw shadow on right and bottom edges
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(x + width + 2, y + 3, x + width + 2, y + height + 2);
        g2d.drawLine(x + 3, y + height + 2, x + width + 2, y + height + 2);
    }

    private void drawLShapeRoom(Graphics2D g2d, int x, int y, int width, int height) {
        int quarterW = width / 4;
        int quarterH = height / 4;

        // Define L-shape polygon
        int[] xPoints = {
            x,
            x + width,
            x + width,
            x + quarterW,
            x + quarterW,
            x
        };
        int[] yPoints = {
            y,
            y,
            y + quarterH,
            y + quarterH,
            y + height,
            y + height
        };

        // Fill with wall color
        g2d.setColor(wallColor);
        g2d.fillPolygon(xPoints, yPoints, 6);

        // Draw border
        g2d.setColor(new Color(100, 100, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawPolygon(xPoints, yPoints, 6);

        // Draw shadow
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(xPoints[1] + 2, yPoints[1] + 3, xPoints[2] + 2, yPoints[2] + 2);
        g2d.drawLine(xPoints[3] + 2, yPoints[3] + 2, xPoints[4] + 2, yPoints[4] + 2);
    }

    private void drawDimensionLabels(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2d.setColor(new Color(40, 40, 40));

        // Width label - top center
        String widthText = String.format("%.1f m", roomWidth);
        FontMetrics fm = g2d.getFontMetrics();
        int widthTextX = x + (width - fm.stringWidth(widthText)) / 2;
        int widthTextY = y - 15;
        
        // Draw white background for text
        int padding = 4;
        g2d.setColor(Color.WHITE);
        g2d.fillRect(widthTextX - padding, widthTextY - fm.getAscent() - padding,
                fm.stringWidth(widthText) + 2 * padding, fm.getHeight() + 2 * padding);
        g2d.setColor(new Color(40, 40, 40));
        g2d.drawString(widthText, widthTextX, widthTextY);

        // Length label - left center
        String lengthText = String.format("%.1f m", roomLength);
        int lengthTextX = x - fm.stringWidth(lengthText) - 15;
        int lengthTextY = y + (height + fm.getAscent()) / 2;
        
        // Draw white background for text
        g2d.setColor(Color.WHITE);
        g2d.fillRect(lengthTextX - padding, lengthTextY - fm.getAscent() - padding,
                fm.stringWidth(lengthText) + 2 * padding, fm.getHeight() + 2 * padding);
        g2d.setColor(new Color(40, 40, 40));
        g2d.drawString(lengthText, lengthTextX, lengthTextY);

        // Draw dimension lines with arrows
        drawDimensionLine(g2d, x - 40, y - 10, x - 40, y + height + 10, false);
        drawDimensionLine(g2d, x - 10, y + height + 40, x + width + 10, y + height + 40, true);
    }

    private void drawDimensionLine(Graphics2D g2d, int x1, int y1, int x2, int y2, boolean horizontal) {
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(1));

        // Draw line
        g2d.drawLine(x1, y1, x2, y2);

        // Draw end caps
        int capSize = 5;
        if (horizontal) {
            g2d.drawLine(x1, y1 - capSize, x1, y1 + capSize);
            g2d.drawLine(x2, y2 - capSize, x2, y2 + capSize);
        } else {
            g2d.drawLine(x1 - capSize, y1, x1 + capSize, y1);
            g2d.drawLine(x2 - capSize, y2, x2 + capSize, y2);
        }
    }

    private void drawScaleInfo(Graphics2D g2d, double scale) {
        String scaleText = String.format("Scale: 1 unit = %.1f pixels", scale);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.setColor(new Color(120, 120, 120));

        FontMetrics fm = g2d.getFontMetrics();
        int infoX = 10;
        int infoY = getHeight() - 8;

        g2d.drawString(scaleText, infoX, infoY);
    }
}
