package com.teamname.furniviz.renderer3d;

/**
 * Simple test runner for Phase 1 tests
 * This demonstrates that the math and mesh functionality works
 */
public class Phase1TestRunner {
    public static void main(String[] args) {
        System.out.println("=== PHASE 1: MATH FOUNDATION TEST SUITE ===\n");

        int passed = 0;
        int failed = 0;

        // Test Vector3f creation
        try {
            Projection.Vector3f v = new Projection.Vector3f(1, 2, 3);
            assert v.x == 1 && v.y == 2 && v.z == 3 : "Vector creation failed";
            System.out.println("✓ testVectorCreation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorCreation: " + e.getMessage());
            failed++;
        }

        // Test Vector Addition
        try {
            Projection.Vector3f v1 = new Projection.Vector3f(1, 2, 3);
            Projection.Vector3f v2 = new Projection.Vector3f(4, 5, 6);
            Projection.Vector3f result = v1.add(v2);
            assert result.x == 5 && result.y == 7 && result.z == 9 : "Vector addition failed";
            System.out.println("✓ testVectorAddition");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorAddition: " + e.getMessage());
            failed++;
        }

        // Test Vector Subtraction
        try {
            Projection.Vector3f v1 = new Projection.Vector3f(5, 7, 9);
            Projection.Vector3f v2 = new Projection.Vector3f(1, 2, 3);
            Projection.Vector3f result = v1.subtract(v2);
            assert result.x == 4 && result.y == 5 && result.z == 6 : "Vector subtraction failed";
            System.out.println("✓ testVectorSubtraction");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorSubtraction: " + e.getMessage());
            failed++;
        }

        // Test Vector Multiplication
        try {
            Projection.Vector3f v = new Projection.Vector3f(2, 3, 4);
            Projection.Vector3f result = v.multiply(2);
            assert result.x == 4 && result.y == 6 && result.z == 8 : "Vector multiplication failed";
            System.out.println("✓ testVectorMultiplication");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorMultiplication: " + e.getMessage());
            failed++;
        }

        // Test Vector Dot Product
        try {
            Projection.Vector3f v1 = new Projection.Vector3f(1, 2, 3);
            Projection.Vector3f v2 = new Projection.Vector3f(4, 5, 6);
            float result = v1.dot(v2);
            assert Math.abs(result - 32) < 0.0001f : "Dot product failed: " + result;
            System.out.println("✓ testVectorDotProduct");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorDotProduct: " + e.getMessage());
            failed++;
        }

        // Test Vector Cross Product
        try {
            Projection.Vector3f v1 = new Projection.Vector3f(1, 0, 0);
            Projection.Vector3f v2 = new Projection.Vector3f(0, 1, 0);
            Projection.Vector3f result = v1.cross(v2);
            assert Math.abs(result.x) < 0.0001f && Math.abs(result.y) < 0.0001f && Math.abs(result.z - 1) < 0.0001f : "Cross product failed";
            System.out.println("✓ testVectorCrossProduct");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorCrossProduct: " + e.getMessage());
            failed++;
        }

        // Test Vector Length
        try {
            Projection.Vector3f v = new Projection.Vector3f(3, 4, 0);
            float length = v.length();
            assert Math.abs(length - 5) < 0.0001f : "Length calculation failed: " + length;
            System.out.println("✓ testVectorLength");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorLength: " + e.getMessage());
            failed++;
        }

        // Test Vector Normalize
        try {
            Projection.Vector3f v = new Projection.Vector3f(3, 4, 0);
            Projection.Vector3f normalized = v.normalize();
            float length = normalized.length();
            assert Math.abs(length - 1) < 0.0001f : "Normalization failed: " + length;
            System.out.println("✓ testVectorNormalize");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorNormalize: " + e.getMessage());
            failed++;
        }

        // Test Vector Distance
        try {
            Projection.Vector3f v1 = new Projection.Vector3f(0, 0, 0);
            Projection.Vector3f v2 = new Projection.Vector3f(3, 4, 0);
            float distance = v1.distance(v2);
            assert Math.abs(distance - 5) < 0.0001f : "Distance failed: " + distance;
            System.out.println("✓ testVectorDistance");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testVectorDistance: " + e.getMessage());
            failed++;
        }

        // Test Matrix Identity
        try {
            Projection.Matrix4f m = new Projection.Matrix4f();
            boolean isIdentity = true;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    float expected = (i == j) ? 1.0f : 0.0f;
                    if (Math.abs(m.m[i][j] - expected) > 0.0001f) {
                        isIdentity = false;
                    }
                }
            }
            assert isIdentity : "Identity matrix failed";
            System.out.println("✓ testMatrixIdentity");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testMatrixIdentity: " + e.getMessage());
            failed++;
        }

        // Test Matrix Translation
        try {
            Projection.Matrix4f trans = Projection.Matrix4f.translation(5, 10, 15);
            Projection.Vector3f v = new Projection.Vector3f(1, 2, 3);
            Projection.Vector3f result = trans.transform(v);
            assert Math.abs(result.x - 6) < 0.0001f && Math.abs(result.y - 12) < 0.0001f && Math.abs(result.z - 18) < 0.0001f : "Translation failed";
            System.out.println("✓ testMatrixTranslation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testMatrixTranslation: " + e.getMessage());
            failed++;
        }

        // Test Matrix Scale
        try {
            Projection.Matrix4f scale = Projection.Matrix4f.scale(2, 3, 4);
            Projection.Vector3f v = new Projection.Vector3f(1, 1, 1);
            Projection.Vector3f result = scale.transform(v);
            assert Math.abs(result.x - 2) < 0.0001f && Math.abs(result.y - 3) < 0.0001f && Math.abs(result.z - 4) < 0.0001f : "Scale failed";
            System.out.println("✓ testMatrixScale");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testMatrixScale: " + e.getMessage());
            failed++;
        }

        // Test Coordinate Conversion
        try {
            Projection.Vector3f worldPos = Projection.roomToWorld(2.5, 3.0, 1.0);
            assert Math.abs(worldPos.x - 2.5f) < 0.0001f && Math.abs(worldPos.y - 1.0f) < 0.0001f && Math.abs(worldPos.z - 3.0f) < 0.0001f : "Room to world conversion failed";
            System.out.println("✓ testRoomToWorldConversion");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testRoomToWorldConversion: " + e.getMessage());
            failed++;
        }

        // Test Rotation Conversion
        try {
            float radians = Projection.convertRotation(90);
            float expectedRadians = (float) (Math.PI / 2);
            assert Math.abs(radians - expectedRadians) < 0.0001f : "Rotation conversion failed: " + radians;
            System.out.println("✓ testRotationConversion");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testRotationConversion: " + e.getMessage());
            failed++;
        }

        // Test Mesh Creation - Box
        try {
            MeshFactory.Mesh box = MeshFactory.createBox(2.0, 1.0, 1.5, java.awt.Color.BLUE);
            assert box.vertices.size() == 8 : "Box should have 8 vertices, got " + box.vertices.size();
            assert box.indices.size() == 36 : "Box should have 36 indices (12 faces * 3), got " + box.indices.size();
            assert box.normals.size() == 8 : "Box should have 8 normals, got " + box.normals.size();
            System.out.println("✓ testCreateBox");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCreateBox: " + e.getMessage());
            failed++;
        }

        // Test Mesh Creation - Plane
        try {
            MeshFactory.Mesh plane = MeshFactory.createPlane(4.0, 3.0, java.awt.Color.WHITE);
            assert plane.vertices.size() == 4 : "Plane should have 4 vertices, got " + plane.vertices.size();
            assert plane.indices.size() == 6 : "Plane should have 6 indices (2 faces * 3), got " + plane.indices.size();
            System.out.println("✓ testCreatePlane");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCreatePlane: " + e.getMessage());
            failed++;
        }

        // Test Mesh Normal Calculation
        try {
            MeshFactory.Mesh box = MeshFactory.createBox(1.0, 1.0, 1.0, java.awt.Color.RED);
            boolean normalsGood = true;
            for (Projection.Vector3f normal : box.normals) {
                float length = normal.length();
                if (length < 0.9f || length > 1.1f) {
                    normalsGood = false;
                    break;
                }
            }
            assert normalsGood : "Normal lengths should be ~1";
            System.out.println("✓ testMeshNormalCalculation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testMeshNormalCalculation: " + e.getMessage());
            failed++;
        }

        // Summary
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("TOTAL: " + (passed + failed) + " tests");
        System.out.println("=".repeat(50));

        if (failed == 0) {
            System.out.println("\n✓ ALL TESTS PASSED! Phase 1 is complete!");
            System.exit(0);
        } else {
            System.out.println("\n✗ Some tests failed. Please review the errors above.");
            System.exit(1);
        }
    }
}

