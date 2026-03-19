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
     */
    public static Mesh createCylinder(double radius, double height, int segments, Color color) {
        Mesh mesh = new Mesh(color);
        
        float r = (float) radius;
        float h = (float) height / 2;
        
        // Create top and bottom center vertices
        int topCenter = mesh.addVertex(new Projection.Vector3f(0, h, 0));
        int bottomCenter = mesh.addVertex(new Projection.Vector3f(0, -h, 0));
        
        // Create vertices around cylinder
        int[] topVertices = new int[segments];
        int[] bottomVertices = new int[segments];
        
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float x = r * (float) Math.cos(angle);
            float z = r * (float) Math.sin(angle);
            
            topVertices[i] = mesh.addVertex(new Projection.Vector3f(x, h, z));
            bottomVertices[i] = mesh.addVertex(new Projection.Vector3f(x, -h, z));
        }
        
        // Create faces
        for (int i = 0; i < segments; i++) {
            int next = (i + 1) % segments;
            
            // Top cap
            mesh.addFace(topCenter, topVertices[next], topVertices[i]);
            
            // Bottom cap
            mesh.addFace(bottomCenter, bottomVertices[i], bottomVertices[next]);
            
            // Side faces
            mesh.addFace(topVertices[i], bottomVertices[i], topVertices[next]);
            mesh.addFace(topVertices[next], bottomVertices[i], bottomVertices[next]);
        }
        
        mesh.calculateNormals();
        return mesh;
    }
    
    /**
     * Create furniture-specific mesh based on type
     * 
     * Different furniture types get different shapes for more realistic visualization
     */
    public static Mesh createFurnitureByType(String furnitureType, double width, double height, 
                                            double depth, Color color) {
        // Convert type to uppercase for comparison
        String type = furnitureType.toUpperCase();
        
        switch (type) {
            case "SOFA":
                return createSofaMesh(width, height, depth, color);
            case "CHAIR":
                return createChairMesh(width, height, depth, color);
            case "TABLE":
            case "DINING_TABLE":
            case "COFFEE_TABLE":
                return createTableMesh(width, height, depth, color);
            case "BED":
                return createBedMesh(width, height, depth, color);
            case "DESK":
                return createDeskMesh(width, height, depth, color);
            case "LAMP":
                return createLampMesh(width, height, depth, color);
            case "CABINET":
            case "BOOKSHELF":
                return createCabinetMesh(width, height, depth, color);
            default:
                // Fallback to simple box
                return createBox(width, height, depth, color);
        }
    }
    
    /**
     * Create a sofa mesh (cushioned seating with back)
     */
    private static Mesh createSofaMesh(double width, double height, double depth, Color color) {
        Mesh mesh = new Mesh(color);
        
        float w = (float) width / 2;
        float h = (float) height / 2;
        float d = (float) depth / 2;
        
        // Base (main seating surface)
        int base_v0 = mesh.addVertex(new Projection.Vector3f(-w, -h, d));
        int base_v1 = mesh.addVertex(new Projection.Vector3f(w, -h, d));
        int base_v2 = mesh.addVertex(new Projection.Vector3f(w, -h * 0.5f, d));
        int base_v3 = mesh.addVertex(new Projection.Vector3f(-w, -h * 0.5f, d));
        
        int base_v4 = mesh.addVertex(new Projection.Vector3f(-w, -h, -d));
        int base_v5 = mesh.addVertex(new Projection.Vector3f(w, -h, -d));
        int base_v6 = mesh.addVertex(new Projection.Vector3f(w, -h * 0.5f, -d));
        int base_v7 = mesh.addVertex(new Projection.Vector3f(-w, -h * 0.5f, -d));
        
        // Add base faces
        mesh.addFace(base_v0, base_v2, base_v1);
        mesh.addFace(base_v0, base_v3, base_v2);
        mesh.addFace(base_v5, base_v7, base_v6);
        mesh.addFace(base_v5, base_v4, base_v7);
        
        // Back cushion (simplified as box in back)
        int back_v0 = mesh.addVertex(new Projection.Vector3f(-w, -h * 0.3f, -d));
        int back_v1 = mesh.addVertex(new Projection.Vector3f(w, -h * 0.3f, -d));
        int back_v2 = mesh.addVertex(new Projection.Vector3f(w, h * 0.7f, -d));
        int back_v3 = mesh.addVertex(new Projection.Vector3f(-w, h * 0.7f, -d));
        
        mesh.addFace(back_v0, back_v2, back_v1);
        mesh.addFace(back_v0, back_v3, back_v2);
        
        mesh.calculateNormals();
        return mesh;
    }
    
    /**
     * Create a chair mesh (seat + back)
     */
    private static Mesh createChairMesh(double width, double height, double depth, Color color) {
        Mesh mesh = new Mesh(color);
        
        float w = (float) width / 3;  // Chair is narrower
        float h = (float) height / 2;
        float d = (float) depth / 2;
        
        // Seat
        int seat_v0 = mesh.addVertex(new Projection.Vector3f(-w, -h, d));
        int seat_v1 = mesh.addVertex(new Projection.Vector3f(w, -h, d));
        int seat_v2 = mesh.addVertex(new Projection.Vector3f(w, 0, d));
        int seat_v3 = mesh.addVertex(new Projection.Vector3f(-w, 0, d));
        
        int seat_v4 = mesh.addVertex(new Projection.Vector3f(-w, -h, -d));
        int seat_v5 = mesh.addVertex(new Projection.Vector3f(w, -h, -d));
        int seat_v6 = mesh.addVertex(new Projection.Vector3f(w, 0, -d));
        int seat_v7 = mesh.addVertex(new Projection.Vector3f(-w, 0, -d));
        
        // Add seat faces
        mesh.addFace(seat_v0, seat_v2, seat_v1);
        mesh.addFace(seat_v0, seat_v3, seat_v2);
        
        // Back rest (thin vertical)
        int back_v0 = mesh.addVertex(new Projection.Vector3f(-w * 0.9f, 0, -d * 0.8f));
        int back_v1 = mesh.addVertex(new Projection.Vector3f(w * 0.9f, 0, -d * 0.8f));
        int back_v2 = mesh.addVertex(new Projection.Vector3f(w * 0.9f, h * 0.8f, -d * 0.8f));
        int back_v3 = mesh.addVertex(new Projection.Vector3f(-w * 0.9f, h * 0.8f, -d * 0.8f));
        
        mesh.addFace(back_v0, back_v2, back_v1);
        mesh.addFace(back_v0, back_v3, back_v2);
        
        mesh.calculateNormals();
        return mesh;
    }
    
    /**
     * Create a table mesh (surface with legs)
     */
    private static Mesh createTableMesh(double width, double height, double depth, Color color) {
        Mesh mesh = new Mesh(color);
        
        float w = (float) width / 2;
        float h = (float) height / 2;
        float d = (float) depth / 2;
        float legThickness = Math.min(w, d) * 0.2f;
        
        // Table top
        int top_v0 = mesh.addVertex(new Projection.Vector3f(-w, h, -d));
        int top_v1 = mesh.addVertex(new Projection.Vector3f(w, h, -d));
        int top_v2 = mesh.addVertex(new Projection.Vector3f(w, h, d));
        int top_v3 = mesh.addVertex(new Projection.Vector3f(-w, h, d));
        
        mesh.addFace(top_v0, top_v2, top_v1);
        mesh.addFace(top_v0, top_v3, top_v2);
        
        // Legs (4 small boxes at corners)
        float legX = w * 0.7f;
        float legZ = d * 0.7f;
        
        // Front-left leg
        int leg1_v0 = mesh.addVertex(new Projection.Vector3f(-legX, -h, -legZ));
        int leg1_v1 = mesh.addVertex(new Projection.Vector3f(-legX + legThickness, -h, -legZ));
        int leg1_v2 = mesh.addVertex(new Projection.Vector3f(-legX + legThickness, h, -legZ));
        int leg1_v3 = mesh.addVertex(new Projection.Vector3f(-legX, h, -legZ));
        
        mesh.addFace(leg1_v0, leg1_v2, leg1_v1);
        mesh.addFace(leg1_v0, leg1_v3, leg1_v2);
        
        // Additional legs (similar pattern for other 3 corners)
        // For simplicity, just add the main leg - in production would add all 4
        
        mesh.calculateNormals();
        return mesh;
    }
    
    /**
     * Create a bed mesh (frame + mattress)
     */
    private static Mesh createBedMesh(double width, double height, double depth, Color color) {
        // Bed is relatively flat - create a box slightly modified
        Mesh mesh = new Mesh(color);
        
        float w = (float) width / 2;
        float h = (float) height / 4;  // Bed is low profile
        float d = (float) depth / 2;
        
        // Simple box for bed (mattress + frame)
        int v0 = mesh.addVertex(new Projection.Vector3f(-w, -h, d));
        int v1 = mesh.addVertex(new Projection.Vector3f(w, -h, d));
        int v2 = mesh.addVertex(new Projection.Vector3f(w, h, d));
        int v3 = mesh.addVertex(new Projection.Vector3f(-w, h, d));
        
        int v4 = mesh.addVertex(new Projection.Vector3f(-w, -h, -d));
        int v5 = mesh.addVertex(new Projection.Vector3f(w, -h, -d));
        int v6 = mesh.addVertex(new Projection.Vector3f(w, h, -d));
        int v7 = mesh.addVertex(new Projection.Vector3f(-w, h, -d));
        
        mesh.addFace(v0, v2, v1);
        mesh.addFace(v0, v3, v2);
        mesh.addFace(v5, v7, v6);
        mesh.addFace(v5, v4, v7);
        
        mesh.calculateNormals();
        return mesh;
    }
    
    /**
     * Create a desk mesh (flat surface, like table but lower)
     */
    private static Mesh createDeskMesh(double width, double height, double depth, Color color) {
        // Desk is similar to table, just with different proportions
        return createTableMesh(width * 1.2, height * 0.6, depth * 0.8, color);
    }
    
    /**
     * Create a lamp mesh (stand + bulb area)
     */
    private static Mesh createLampMesh(double width, double height, double depth, Color color) {
        // Use cylinder for lamp (stand)
        return createCylinder(Math.min(width, depth) * 0.3, height, 16, color);
    }
    
    /**
     * Create a cabinet/bookshelf mesh (tall storage)
     */
    private static Mesh createCabinetMesh(double width, double height, double depth, Color color) {
        // Cabinet is just a box, similar to standard furniture
        return createBox(width, height, depth, color);
    }
}
