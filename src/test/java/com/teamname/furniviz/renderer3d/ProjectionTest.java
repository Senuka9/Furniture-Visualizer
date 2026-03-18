package com.teamname.furniviz.renderer3d;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectionTest {

    private static final float EPSILON = 0.0001f;

    // ============================================================================
    // VECTOR TESTS
    // ============================================================================

    @Test
    public void testVectorCreation() {
        Projection.Vector3f v = new Projection.Vector3f(1, 2, 3);
        assertEquals(1, v.x);
        assertEquals(2, v.y);
        assertEquals(3, v.z);
    }

    @Test
    public void testVectorAddition() {
        Projection.Vector3f v1 = new Projection.Vector3f(1, 2, 3);
        Projection.Vector3f v2 = new Projection.Vector3f(4, 5, 6);
        Projection.Vector3f result = v1.add(v2);

        assertEquals(5, result.x, EPSILON);
        assertEquals(7, result.y, EPSILON);
        assertEquals(9, result.z, EPSILON);
    }

    @Test
    public void testVectorSubtraction() {
        Projection.Vector3f v1 = new Projection.Vector3f(5, 7, 9);
        Projection.Vector3f v2 = new Projection.Vector3f(1, 2, 3);
        Projection.Vector3f result = v1.subtract(v2);

        assertEquals(4, result.x, EPSILON);
        assertEquals(5, result.y, EPSILON);
        assertEquals(6, result.z, EPSILON);
    }

    @Test
    public void testVectorMultiplication() {
        Projection.Vector3f v = new Projection.Vector3f(2, 3, 4);
        Projection.Vector3f result = v.multiply(2);

        assertEquals(4, result.x, EPSILON);
        assertEquals(6, result.y, EPSILON);
        assertEquals(8, result.z, EPSILON);
    }

    @Test
    public void testVectorDotProduct() {
        Projection.Vector3f v1 = new Projection.Vector3f(1, 2, 3);
        Projection.Vector3f v2 = new Projection.Vector3f(4, 5, 6);
        float result = v1.dot(v2);

        // (1*4) + (2*5) + (3*6) = 4 + 10 + 18 = 32
        assertEquals(32, result, EPSILON);
    }

    @Test
    public void testVectorCrossProduct() {
        Projection.Vector3f v1 = new Projection.Vector3f(1, 0, 0);
        Projection.Vector3f v2 = new Projection.Vector3f(0, 1, 0);
        Projection.Vector3f result = v1.cross(v2);

        // (0*0 - 0*1, 0*0 - 1*0, 1*1 - 0*0) = (0, 0, 1)
        assertEquals(0, result.x, EPSILON);
        assertEquals(0, result.y, EPSILON);
        assertEquals(1, result.z, EPSILON);
    }

    @Test
    public void testVectorLength() {
        Projection.Vector3f v = new Projection.Vector3f(3, 4, 0);
        float length = v.length();

        // sqrt(9 + 16 + 0) = sqrt(25) = 5
        assertEquals(5, length, EPSILON);
    }

    @Test
    public void testVectorNormalize() {
        Projection.Vector3f v = new Projection.Vector3f(3, 4, 0);
        Projection.Vector3f normalized = v.normalize();
        float length = normalized.length();

        // Should be length 1
        assertEquals(1, length, EPSILON);
        assertEquals(0.6f, normalized.x, EPSILON);
        assertEquals(0.8f, normalized.y, EPSILON);
        assertEquals(0, normalized.z, EPSILON);
    }

    @Test
    public void testVectorDistance() {
        Projection.Vector3f v1 = new Projection.Vector3f(0, 0, 0);
        Projection.Vector3f v2 = new Projection.Vector3f(3, 4, 0);
        float distance = v1.distance(v2);

        // Distance formula: sqrt((3-0)^2 + (4-0)^2 + (0-0)^2) = 5
        assertEquals(5, distance, EPSILON);
    }

    // ============================================================================
    // MATRIX TESTS
    // ============================================================================

    @Test
    public void testMatrixIdentity() {
        Projection.Matrix4f m = new Projection.Matrix4f();

        // Identity matrix should have 1 on diagonal, 0 elsewhere
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float expected = (i == j) ? 1.0f : 0.0f;
                assertEquals(expected, m.m[i][j], EPSILON);
            }
        }
    }

    @Test
    public void testMatrixTranslation() {
        Projection.Matrix4f trans = Projection.Matrix4f.translation(5, 10, 15);
        Projection.Vector3f v = new Projection.Vector3f(1, 2, 3);
        Projection.Vector3f result = trans.transform(v);

        // Translation adds to coordinates
        assertEquals(6, result.x, EPSILON);      // 1 + 5
        assertEquals(12, result.y, EPSILON);     // 2 + 10
        assertEquals(18, result.z, EPSILON);     // 3 + 15
    }

    @Test
    public void testMatrixScale() {
        Projection.Matrix4f scale = Projection.Matrix4f.scale(2, 3, 4);
        Projection.Vector3f v = new Projection.Vector3f(1, 1, 1);
        Projection.Vector3f result = scale.transform(v);

        assertEquals(2, result.x, EPSILON);
        assertEquals(3, result.y, EPSILON);
        assertEquals(4, result.z, EPSILON);
    }

    @Test
    public void testMatrixRotationZ() {
        // 90 degree rotation around Z axis
        float angle90Degrees = (float) (Math.PI / 2);
        Projection.Matrix4f rotZ = Projection.Matrix4f.rotationZ(angle90Degrees);

        Projection.Vector3f v = new Projection.Vector3f(1, 0, 0);
        Projection.Vector3f result = rotZ.transform(v);

        // (1, 0, 0) rotated 90° around Z becomes approximately (0, 1, 0)
        assertEquals(0, result.x, EPSILON);
        assertEquals(1, result.y, EPSILON);
        assertEquals(0, result.z, EPSILON);
    }

    @Test
    public void testMatrixMultiplication() {
        // Create translate + rotate
        Projection.Matrix4f trans = Projection.Matrix4f.translation(5, 0, 0);
        Projection.Matrix4f scale = Projection.Matrix4f.scale(2, 2, 2);

        // Combine: first scale, then translate
        Projection.Matrix4f combined = trans.multiply(scale);

        Projection.Vector3f v = new Projection.Vector3f(1, 1, 1);
        Projection.Vector3f result = combined.transform(v);

        // Scale by 2: (1,1,1) -> (2,2,2)
        // Translate by 5: (2,2,2) -> (7,2,2)
        assertEquals(7, result.x, EPSILON);
        assertEquals(2, result.y, EPSILON);
        assertEquals(2, result.z, EPSILON);
    }

    // ============================================================================
    // COORDINATE CONVERSION TESTS
    // ============================================================================

    @Test
    public void testRoomToWorldConversion() {
        // Furniture at room position (2.5, 3.0) with height 1.0
        Projection.Vector3f worldPos = Projection.roomToWorld(2.5, 3.0, 1.0);

        // Should convert to world space
        assertEquals(2.5f, worldPos.x, EPSILON);  // X stays same
        assertEquals(1.0f, worldPos.y, EPSILON);  // Y becomes height
        assertEquals(3.0f, worldPos.z, EPSILON);  // Room Y becomes Z
    }

    @Test
    public void testRotationConversion() {
        // 90 degrees in 2D
        float radians = Projection.convertRotation(90);

        // Should convert to radians (approximately PI/2 = 1.5708)
        float expectedRadians = (float) (Math.PI / 2);
        assertEquals(expectedRadians, radians, EPSILON);
    }
}

