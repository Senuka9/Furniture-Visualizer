package com.teamname.furniviz.common.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * ImageGenerator - Creates placeholder furniture images for development
 * Can be run standalone to generate all required furniture images
 */
public class ImageGenerator {

    private static final String OUTPUT_DIR = "src/main/resources/images";
    private static final int THUMBNAIL_SIZE = 50;
    private static final int PREVIEW_SIZE = 200;

    public static void main(String[] args) {
        File outputDir = new File(OUTPUT_DIR);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        System.out.println("Generating furniture placeholder images...");

        // Generate images for each furniture type
        generateFurnitureImage("wooden_chair", Color.decode("#8B4513"), "Chair");
        generateFurnitureImage("office_chair", Color.decode("#333333"), "Chair");
        generateFurnitureImage("dining_table", Color.decode("#A0522D"), "Table");
        generateFurnitureImage("coffee_table", Color.decode("#D3D3D3"), "Table");
        generateFurnitureImage("sofa", Color.decode("#696969"), "Sofa");
        generateFurnitureImage("bed", Color.decode("#F5DEB3"), "Bed");
        generateFurnitureImage("desk", Color.decode("#8B7355"), "Desk");
        generateFurnitureImage("bookshelf", Color.decode("#654321"), "Cabinet");
        generateFurnitureImage("lamp", Color.decode("#FFD700"), "Lamp");

        System.out.println("✓ All furniture images generated successfully!");
    }

    private static void generateFurnitureImage(String name, Color color, String type) {
        // Generate both thumbnail and preview sizes
        BufferedImage thumbnail = createFurnitureImage(THUMBNAIL_SIZE, color, type.substring(0, 1));
        BufferedImage preview = createFurnitureImage(PREVIEW_SIZE, color, type);

        try {
            String thumbPath = OUTPUT_DIR + "/" + name + "_thumb.png";
            String previewPath = OUTPUT_DIR + "/" + name + ".png";

            ImageIO.write(thumbnail, "PNG", new File(thumbPath));
            ImageIO.write(preview, "PNG", new File(previewPath));

            System.out.println("  ✓ Generated: " + name);
        } catch (IOException e) {
            System.err.println("  ✗ Error generating " + name + ": " + e.getMessage());
        }
    }

    private static BufferedImage createFurnitureImage(int size, Color furnitureColor, String label) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Background
        g2d.setColor(new Color(245, 245, 245));
        g2d.fillRect(0, 0, size, size);

        // Border
        g2d.setColor(new Color(200, 200, 200));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(2, 2, size - 4, size - 4);

        // Furniture shape (simple representation)
        g2d.setColor(furnitureColor);
        int padding = size / 6;
        g2d.fillRoundRect(padding, padding, size - 2 * padding, size - 2 * padding, 5, 5);

        // Label for larger images
        if (size > 100) {
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, Math.max(10, size / 10)));
            FontMetrics fm = g2d.getFontMetrics();
            int textX = (size - fm.stringWidth(label)) / 2;
            int textY = (size + fm.getAscent()) / 2;
            g2d.drawString(label, textX, textY);
        }

        g2d.dispose();
        return image;
    }
}

