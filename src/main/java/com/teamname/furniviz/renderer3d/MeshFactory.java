package com.teamname.furniviz.renderer3d;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * MeshFactory - Creates 3D mesh geometry from furniture data
 *
 * A mesh is:
 * - Vertices: 3D points that define corners
 * - Faces: triangles connecting vertices
 * - Normals: directions perpendicular to faces (for lighting)
 * - Color: RGB color to fill with
 */
public class MeshFactory {

    /**
     * Mesh - Data structure for 3D geometry
     *
     * Contains all info needed to render a 3D object:
     * - vertices: List of 3D points
     * - indices: Which vertices form triangles (faces)
     * - normals: Direction perpendicular to each face (for lighting)
     * - color: Color to fill with
     * - position: Where to place in world (optional, for convenience)
     * - rotation: Rotation in radians (optional, for convenience)
     */
    public static class Mesh {
        public List<Projection.Vector3f> vertices = new ArrayList<>();
        public List<Integer> indices = new ArrayList<>();
        public List<Projection.Vector3f> normals = new ArrayList<>();
        public Color color;
        public Projection.Vector3f position = new Projection.Vector3f(0, 0, 0);
        public float rotation = 0;

        public Mesh(Color color) {
            this.color = color;
        }

        /**
         * Add a vertex (point) to the mesh
         * Returns the index of this vertex for use in faces
         */
        public int addVertex(Projection.Vector3f v) {
            int index = vertices.size();
            vertices.add(v);
            return index;
        }

        /**
         * Add a triangular face using vertex indices
         * Example: face(0, 1, 2) creates triangle from vertices 0, 1, 2
         */
        public void addFace(int v1, int v2, int v3) {
            indices.add(v1);
            indices.add(v2);
            indices.add(v3);
        }

        /**
         * Calculate normals for all faces
         * Normals are used for lighting (determines brightness of surface)
         */
        public void calculateNormals() {
            // Initialize empty normals for all vertices
            while (normals.size() < vertices.size()) {
                normals.add(new Projection.Vector3f(0, 0, 0));
            }

            // For each face, calculate its normal
            for (int i = 0; i < indices.size(); i += 3) {
                Projection.Vector3f v1 = vertices.get(indices.get(i));
                Projection.Vector3f v2 = vertices.get(indices.get(i + 1));
                Projection.Vector3f v3 = vertices.get(indices.get(i + 2));

                // Calculate edge vectors
                Projection.Vector3f edge1 = v2.subtract(v1);
                Projection.Vector3f edge2 = v3.subtract(v1);

                // Normal is perpendicular to both edges
                Projection.Vector3f faceNormal = edge1.cross(edge2).normalize();

                // Add this normal to all three vertices of the face
                normals.set(indices.get(i), normals.get(indices.get(i)).add(faceNormal));
                normals.set(indices.get(i + 1), normals.get(indices.get(i + 1)).add(faceNormal));
                normals.set(indices.get(i + 2), normals.get(indices.get(i + 2)).add(faceNormal));
            }

            // Normalize all vertex normals
            for (int i = 0; i < normals.size(); i++) {
                normals.set(i, normals.get(i).normalize());
            }
        }

        @Override
        public String toString() {
            return String.format("Mesh(%d vertices, %d faces, color=%s)",
                    vertices.size(), indices.size() / 3, color);
        }
    }

    // ============================================================================
    // MESH GENERATORS
    // ============================================================================

    /**
     * Create a box (cuboid) mesh
     *
     * Used for: furniture (desks, tables, beds, etc.)
     *
     * @param width X dimension
     * @param height Y dimension (vertical)
     * @param depth Z dimension
     * @param color Mesh color
     * @return Mesh representing the box
     */
    public static Mesh createBox(double width, double height, double depth, Color color) {
        Mesh mesh = new Mesh(color);

        // Convert to float and divide by 2 (center around origin)
        float w = (float) width / 2;
        float h = (float) height / 2;
        float d = (float) depth / 2;

        // Create 8 vertices for corners of the box
        // Front face
        int v0 = mesh.addVertex(new Projection.Vector3f(-w, -h, d));  // Front-bottom-left
        int v1 = mesh.addVertex(new Projection.Vector3f(w, -h, d));   // Front-bottom-right
        int v2 = mesh.addVertex(new Projection.Vector3f(w, h, d));    // Front-top-right
        int v3 = mesh.addVertex(new Projection.Vector3f(-w, h, d));   // Front-top-left

        // Back face
        int v4 = mesh.addVertex(new Projection.Vector3f(-w, -h, -d)); // Back-bottom-left
        int v5 = mesh.addVertex(new Projection.Vector3f(w, -h, -d));  // Back-bottom-right
        int v6 = mesh.addVertex(new Projection.Vector3f(w, h, -d));   // Back-top-right
        int v7 = mesh.addVertex(new Projection.Vector3f(-w, h, -d));  // Back-top-left

        // Create 6 faces (2 triangles per face)
        // Front face
        mesh.addFace(v0, v2, v1);
        mesh.addFace(v0, v3, v2);

        // Back face
        mesh.addFace(v5, v7, v6);
        mesh.addFace(v5, v4, v7);

        // Top face
        mesh.addFace(v3, v6, v7);
        mesh.addFace(v3, v2, v6);

        // Bottom face
        mesh.addFace(v4, v1, v5);
        mesh.addFace(v4, v0, v1);

        // Left face
        mesh.addFace(v4, v3, v7);
        mesh.addFace(v4, v0, v3);

        // Right face
        mesh.addFace(v1, v6, v5);
        mesh.addFace(v1, v2, v6);

        // Calculate lighting normals
        mesh.calculateNormals();

        return mesh;
    }

    /**
     * Create a plane (flat rectangle)
     *
     * Used for: room floor, walls, ceilings
     */
    public static Mesh createPlane(double width, double height, Color color) {
        Mesh mesh = new Mesh(color);

        float w = (float) width / 2;
        float h = (float) height / 2;

        int v0 = mesh.addVertex(new Projection.Vector3f(-w, 0, -h));
        int v1 = mesh.addVertex(new Projection.Vector3f(w, 0, -h));
        int v2 = mesh.addVertex(new Projection.Vector3f(w, 0, h));
        int v3 = mesh.addVertex(new Projection.Vector3f(-w, 0, h));

        mesh.addFace(v0, v2, v1);
        mesh.addFace(v0, v3, v2);

        mesh.calculateNormals();

        return mesh;
    }

    /**
     * Create a cylinder (for round furniture)
     *
     * Used for: round tables, chairs, stools, lamps
     * (Implemented in Phase 2)
     */
    public static Mesh createCylinder(double radius, double height, int segments, Color color) {
        // TODO: Implement in Phase 2
        throw new UnsupportedOperationException("Cylinder generation not yet implemented");
    }
}
