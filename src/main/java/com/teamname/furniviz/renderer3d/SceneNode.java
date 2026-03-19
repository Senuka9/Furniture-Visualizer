package com.teamname.furniviz.renderer3d;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SceneNode - Represents a 3D object in the scene (room, furniture, lights, etc.)
 *
 * Contains:
 * - Mesh (3D geometry)
 * - Transform (position, rotation, scale)
 * - Material properties
 */
public class SceneNode {
    private String name;
    private MeshFactory.Mesh mesh;

    // Transform
    private Projection.Vector3f position = new Projection.Vector3f(0, 0, 0);
    private Projection.Vector3f rotation = new Projection.Vector3f(0, 0, 0);  // Euler angles in radians
    private Projection.Vector3f scale = new Projection.Vector3f(1, 1, 1);

    // Material
    private Color color = Color.WHITE;
    private float shininess = 0.5f;

    // Parent/children hierarchy
    private SceneNode parent;
    private List<SceneNode> children = new ArrayList<>();

    /**
     * Constructor
     */
    public SceneNode(String name) {
        this.name = name;
    }

    // ============================================================================
    // MESH AND RENDERING
    // ============================================================================

    public MeshFactory.Mesh getMesh() { return mesh; }
    public void setMesh(MeshFactory.Mesh m) { this.mesh = m; }

    public Color getColor() { return color; }
    public void setColor(Color c) { this.color = c; }

    public float getShininess() { return shininess; }
    public void setShininess(float s) { this.shininess = Math.max(0, Math.min(1, s)); }

    // ============================================================================
    // TRANSFORM
    // ============================================================================

    public Projection.Vector3f getPosition() { return new Projection.Vector3f(position); }
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    public void setPosition(Projection.Vector3f p) {
        position.x = p.x;
        position.y = p.y;
        position.z = p.z;
    }

    public Projection.Vector3f getRotation() { return new Projection.Vector3f(rotation); }
    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public Projection.Vector3f getScale() { return new Projection.Vector3f(scale); }
    public void setScale(float x, float y, float z) {
        scale.x = x;
        scale.y = y;
        scale.z = z;
    }

    /**
     * Get world transformation matrix (combines scale, rotation, translation)
     */
    public Projection.Matrix4f getWorldMatrix() {
        // Scale
        Projection.Matrix4f matrix = Projection.Matrix4f.scale(scale.x, scale.y, scale.z);

        // Rotation (order: X, Y, Z)
        Projection.Matrix4f rotX = Projection.Matrix4f.rotationX(rotation.x);
        Projection.Matrix4f rotY = Projection.Matrix4f.rotationY(rotation.y);
        Projection.Matrix4f rotZ = Projection.Matrix4f.rotationZ(rotation.z);

        matrix = rotX.multiply(matrix);
        matrix = rotY.multiply(matrix);
        matrix = rotZ.multiply(matrix);

        // Translation
        Projection.Matrix4f trans = Projection.Matrix4f.translation(position.x, position.y, position.z);
        matrix = trans.multiply(matrix);

        return matrix;
    }

    // ============================================================================
    // HIERARCHY
    // ============================================================================

    public String getName() { return name; }

    public SceneNode getParent() { return parent; }
    public void setParent(SceneNode p) { this.parent = p; }

    public List<SceneNode> getChildren() { return new ArrayList<>(children); }

    public void addChild(SceneNode child) {
        if (!children.contains(child)) {
            children.add(child);
            child.parent = this;
        }
    }

    public void removeChild(SceneNode child) {
        children.remove(child);
        child.parent = null;
    }

    @Override
    public String toString() {
        return String.format("%s[pos=(%f,%f,%f)]", name, position.x, position.y, position.z);
    }
}

