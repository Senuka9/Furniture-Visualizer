package com.teamname.furniviz.renderer3d;

import java.awt.Color;

/**
 * Material - Defines how a surface looks and interacts with light
 *
 * Properties:
 * - Diffuse: Base color (responds to diffuse lighting)
 * - Specular: Highlight color (for shiny surfaces)
 * - Ambient: Color in shadow (prevents complete darkness)
 * - Shininess: How reflective/smooth the surface is (1-128)
 *
 * Higher shininess = more mirror-like
 * Lower shininess = more matte
 */
public class Material {
    public Color diffuse;      // Base color
    public Color specular;     // Highlight color (usually white or bright)
    public Color ambient;      // Shadow color (usually dark version of diffuse)
    public float shininess;    // Surface smoothness (1-128)

    /**
     * Create a material with all properties
     */
    public Material(Color diffuse, Color specular, Color ambient, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.ambient = ambient;
        this.shininess = Math.max(1, Math.min(128, shininess));  // Clamp to 1-128
    }

    /**
     * Create a basic matte material
     * Specular and ambient are automatically derived from diffuse
     */
    public Material(Color diffuse) {
        this.diffuse = diffuse;
        this.shininess = 32;  // Medium shine
        
        // Specular: bright white for highlights
        this.specular = Color.WHITE;
        
        // Ambient: darker version of diffuse
        int r = Math.max(20, (int) (diffuse.getRed() * 0.3f));
        int g = Math.max(20, (int) (diffuse.getGreen() * 0.3f));
        int b = Math.max(20, (int) (diffuse.getBlue() * 0.3f));
        this.ambient = new Color(r, g, b);
    }

    /**
     * Create a shiny material (like polished wood or metal)
     */
    public static Material createShiny(Color diffuse) {
        int r = Math.max(50, (int) (diffuse.getRed() * 0.5f));
        int g = Math.max(50, (int) (diffuse.getGreen() * 0.5f));
        int b = Math.max(50, (int) (diffuse.getBlue() * 0.5f));
        Color ambient = new Color(r, g, b);
        return new Material(diffuse, Color.WHITE, ambient, 96);  // Very shiny
    }

    /**
     * Create a matte material (like fabric or paper)
     */
    public static Material createMatte(Color diffuse) {
        Color ambient = new Color(
            Math.max(10, (int) (diffuse.getRed() * 0.2f)),
            Math.max(10, (int) (diffuse.getGreen() * 0.2f)),
            Math.max(10, (int) (diffuse.getBlue() * 0.2f))
        );
        return new Material(diffuse, new Color(100, 100, 100), ambient, 16);  // Low shine
    }

    /**
     * Create a metal material
     */
    public static Material createMetal(Color diffuse) {
        Color specular = new Color(
            Math.min(255, diffuse.getRed() + 80),
            Math.min(255, diffuse.getGreen() + 80),
            Math.min(255, diffuse.getBlue() + 80)
        );
        Color ambient = new Color(
            Math.max(30, (int) (diffuse.getRed() * 0.4f)),
            Math.max(30, (int) (diffuse.getGreen() * 0.4f)),
            Math.max(30, (int) (diffuse.getBlue() * 0.4f))
        );
        return new Material(diffuse, specular, ambient, 112);  // Very reflective
    }

    /**
     * Get material for a furniture type
     */
    public static Material getMaterialForFurniture(String furnitureType, Color baseColor) {
        String type = furnitureType.toUpperCase();
        
        switch (type) {
            case "SOFA":
            case "CHAIR":
            case "OFFICE_CHAIR":
                return Material.createMatte(baseColor);  // Fabric
                
            case "DESK":
            case "TABLE":
            case "DINING_TABLE":
            case "COFFEE_TABLE":
                return Material.createShiny(baseColor);  // Polished wood
                
            case "BED":
                return Material.createMatte(baseColor);  // Fabric/mattress
                
            case "LAMP":
                return Material.createShiny(baseColor);  // Smooth finish
                
            case "CABINET":
            case "BOOKSHELF":
                return Material.createShiny(baseColor);  // Polished wood/material
                
            default:
                return new Material(baseColor);  // Basic material
        }
    }

    /**
     * Apply lighting to this material
     * Uses Phong lighting model with specular highlights
     */
    public Color applyPhongLighting(
            Projection.Vector3f normal,
            Projection.Vector3f toLight,
            Projection.Vector3f toCamera,
            float ambientIntensity,
            float diffuseIntensity,
            float specularIntensity) {

        if (normal == null || toLight == null || toCamera == null) {
            return diffuse;
        }

        normal = normal.normalize();
        toLight = toLight.normalize();
        toCamera = toCamera.normalize();

        // Ambient component
        float ambient = ambientIntensity;

        // Diffuse component (Lambert's cosine law)
        float diffuse = Math.max(0, normal.dot(toLight)) * diffuseIntensity;

        // Specular component (Phong model)
        Projection.Vector3f reflect = reflect(toLight, normal);
        float specular = 0;
        if (diffuse > 0) {  // Only calculate specular if diffuse > 0
            float spec = Math.max(0, reflect.dot(toCamera));
            specular = (float) Math.pow(spec, this.shininess / 32.0f) * specularIntensity;
        }

        // Combine components
        float r = ambient * this.ambient.getRed() +
                  diffuse * this.diffuse.getRed() +
                  specular * this.specular.getRed();
        float g = ambient * this.ambient.getGreen() +
                  diffuse * this.diffuse.getGreen() +
                  specular * this.specular.getGreen();
        float b = ambient * this.ambient.getBlue() +
                  diffuse * this.diffuse.getBlue() +
                  specular * this.specular.getBlue();

        // Clamp values to 0-255
        int r255 = Math.min(255, (int) r);
        int g255 = Math.min(255, (int) g);
        int b255 = Math.min(255, (int) b);

        return new Color(r255, g255, b255);
    }

    /**
     * Calculate reflection vector
     * Given light direction and surface normal, calculate reflected direction
     */
    private Projection.Vector3f reflect(Projection.Vector3f v, Projection.Vector3f n) {
        float dot = v.dot(n);
        return v.subtract(n.multiply(2 * dot));
    }

    @Override
    public String toString() {
        return String.format("Material(diffuse=%s, shininess=%.0f)", diffuse, shininess);
    }
}

