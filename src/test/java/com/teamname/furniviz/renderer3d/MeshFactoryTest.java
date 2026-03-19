package com.teamname.furniviz.renderer3d;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class MeshFactoryTest {

    @Test
    public void testCreateBox() {
        MeshFactory.Mesh box = MeshFactory.createBox(2.0, 1.0, 1.5, Color.BLUE);

        // Box should have 8 vertices (corners)
        assertEquals(8, box.vertices.size());

        // Box should have 12 faces (2 triangles per side × 6 sides)
        assertEquals(36, box.indices.size()); // 12 faces × 3 indices per face

        // Color should be correct
        assertEquals(Color.BLUE, box.color);

        // Should have calculated normals
        assertEquals(8, box.normals.size());
    }

    @Test
    public void testCreatePlane() {
        MeshFactory.Mesh plane = MeshFactory.createPlane(4.0, 3.0, Color.WHITE);

        // Plane should have 4 vertices (corners)
        assertEquals(4, plane.vertices.size());

        // Plane should have 2 faces (triangles)
        assertEquals(6, plane.indices.size()); // 2 faces × 3 indices per face

        // Color should be correct
        assertEquals(Color.WHITE, plane.color);
    }

    @Test
    public void testMeshNormalCalculation() {
        MeshFactory.Mesh box = MeshFactory.createBox(1.0, 1.0, 1.0, Color.RED);

        // All normals should be calculated
        for (Projection.Vector3f normal : box.normals) {
            // Normal length should be approximately 1 (normalized)
            float length = normal.length();
            assertTrue(length > 0.9f && length < 1.1f,
                      "Normal length should be ~1, was " + length);
        }
    }

    @Test
    public void testMeshAddVertex() {
        MeshFactory.Mesh mesh = new MeshFactory.Mesh(Color.BLACK);

        int idx0 = mesh.addVertex(new Projection.Vector3f(0, 0, 0));
        int idx1 = mesh.addVertex(new Projection.Vector3f(1, 0, 0));
        int idx2 = mesh.addVertex(new Projection.Vector3f(0, 1, 0));

        assertEquals(0, idx0);
        assertEquals(1, idx1);
        assertEquals(2, idx2);
        assertEquals(3, mesh.vertices.size());
    }

    @Test
    public void testMeshAddFace() {
        MeshFactory.Mesh mesh = new MeshFactory.Mesh(Color.BLACK);

        mesh.addVertex(new Projection.Vector3f(0, 0, 0));
        mesh.addVertex(new Projection.Vector3f(1, 0, 0));
        mesh.addVertex(new Projection.Vector3f(0, 1, 0));

        mesh.addFace(0, 1, 2);

        // Should have 3 indices (1 triangle)
        assertEquals(3, mesh.indices.size());
    }
}

