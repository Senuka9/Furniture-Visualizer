package com.teamname.furniviz.renderer3d;

import java.awt.Color;

/**
 * LightingManager - Manages lighting in the 3D scene
 *
 * Handles:
 * - Ambient lighting
 * - Directional lights
 * - Light color and intensity
 */
public class LightingManager {

    /**
     * Light - Represents a light source
     */
    public static class Light {
        public Projection.Vector3f position;     // Position in world space
        public Projection.Vector3f direction;    // Direction for directional lights
        public Color color;
        public float intensity;
        public LightType type;

        public enum LightType {
            AMBIENT,        // Uniform light everywhere
            DIRECTIONAL,    // Light from infinite distance (sun)
            POINT,          // Light from a point (bulb)
            SPOT            // Directional light from a point
        }

        public Light(LightType type, Color color, float intensity) {
            this.type = type;
            this.color = color;
            this.intensity = intensity;
            this.position = new Projection.Vector3f(0, 0, 0);
            this.direction = new Projection.Vector3f(0, -1, 0);  // Default: down
        }
    }

    private Light ambientLight;
    private Light directionalLight;

    /**
     * Default constructor - sets up standard lighting
     */
    public LightingManager() {
        // Ambient light: soft white light everywhere
        ambientLight = new Light(
            Light.LightType.AMBIENT,
            Color.WHITE,
            0.5f  // 50% ambient
        );

        // Directional light: sun-like light from top-left
        directionalLight = new Light(
            Light.LightType.DIRECTIONAL,
            Color.WHITE,
            0.8f  // 80% intensity
        );
        directionalLight.direction = new Projection.Vector3f(-1, -1, -1).normalize();
    }

    // ============================================================================
    // GETTERS/SETTERS
    // ============================================================================

    public Light getAmbientLight() { return ambientLight; }
    public Light getDirectionalLight() { return directionalLight; }

    public void setAmbientLight(Color color, float intensity) {
        ambientLight.color = color;
        ambientLight.intensity = Math.max(0, Math.min(1, intensity));
    }

    public void setDirectionalLight(Projection.Vector3f direction, Color color, float intensity) {
        directionalLight.direction = direction.normalize();
        directionalLight.color = color;
        directionalLight.intensity = Math.max(0, Math.min(1, intensity));
    }

    // ============================================================================
    // LIGHTING CALCULATIONS
    // ============================================================================

    /**
     * Calculate lighting intensity at a point given a normal
     * Uses simple Phong-like lighting model
     */
    public float calculateLightIntensity(Projection.Vector3f normal, Projection.Vector3f toLight) {
        // Ensure normal and light direction are normalized
        Projection.Vector3f n = normal.normalize();
        Projection.Vector3f l = toLight.normalize();

        // Lambert's cosine law
        float diffuse = Math.max(0, n.dot(l));

        return diffuse;
    }

    /**
     * Calculate final color given material color and lighting
     */
    public java.awt.Color calculateFinalColor(java.awt.Color materialColor,
                                              Projection.Vector3f normal,
                                              Projection.Vector3f toLight) {
        // Calculate diffuse lighting
        float intensity = calculateLightIntensity(normal, toLight);

        // Clamp intensity
        intensity = Math.max(0.2f, Math.min(1, intensity + ambientLight.intensity));

        // Apply to material color
        int r = (int) (materialColor.getRed() * intensity);
        int g = (int) (materialColor.getGreen() * intensity);
        int b = (int) (materialColor.getBlue() * intensity);

        return new java.awt.Color(r, g, b);
    }

    /**
     * Get shaded color based on surface normal
     * Simplified for 2D rendering
     */
    public java.awt.Color getShadedColor(java.awt.Color baseColor, Projection.Vector3f normal) {
        // Simple shading: brighter if normal faces camera
        Projection.Vector3f cameraDir = new Projection.Vector3f(0, 0, 1).normalize();
        float brightness = Math.max(0.3f, normal.dot(cameraDir));

        int r = (int) (baseColor.getRed() * brightness);
        int g = (int) (baseColor.getGreen() * brightness);
        int b = (int) (baseColor.getBlue() * brightness);

        return new java.awt.Color(
            Math.min(255, r),
            Math.min(255, g),
            Math.min(255, b)
        );
    }
}

