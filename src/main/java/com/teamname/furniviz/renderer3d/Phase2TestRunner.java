package com.teamname.furniviz.renderer3d;

/**
 * Phase 2 Test Runner - Tests Camera, Renderer3D, and scene graph components
 */
public class Phase2TestRunner {
    public static void main(String[] args) {
        System.out.println("=== PHASE 2: RENDERING ENGINE TEST SUITE ===\n");

        int passed = 0;
        int failed = 0;

        // Test 1: Camera Creation
        try {
            Camera cam = new Camera();
            assert cam != null : "Camera creation failed";
            System.out.println("✓ testCameraCreation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraCreation: " + e.getMessage());
            failed++;
        }

        // Test 2: Camera Isometric Preset
        try {
            Camera cam = new Camera();
            cam.setIsometric();
            Projection.Vector3f pos = cam.getPosition();
            assert Math.abs(pos.x - 10) < 0.1f : "Isometric X position wrong";
            System.out.println("✓ testCameraIsometricPreset");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraIsometricPreset: " + e.getMessage());
            failed++;
        }

        // Test 3: Camera Top-Down Preset
        try {
            Camera cam = new Camera();
            cam.setTopDown();
            Projection.Vector3f pos = cam.getPosition();
            assert Math.abs(pos.y - 20) < 0.1f : "Top-down Y position wrong";
            System.out.println("✓ testCameraTopDownPreset");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraTopDownPreset: " + e.getMessage());
            failed++;
        }

        // Test 4: Camera Zoom
        try {
            Camera cam = new Camera();
            assert Math.abs(cam.getZoom() - 1.0f) < 0.01f : "Initial zoom should be 1.0";
            cam.zoomIn();
            assert cam.getZoom() > 1.0f : "Zoom in should increase zoom";
            cam.zoomOut();
            assert Math.abs(cam.getZoom() - 1.0f) < 0.2f : "Zoom out should decrease zoom";
            System.out.println("✓ testCameraZoom");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraZoom: " + e.getMessage());
            failed++;
        }

        // Test 5: Camera Orbit
        try {
            Camera cam = new Camera();
            cam.setIsometric();
            Projection.Vector3f posBefore = cam.getPosition();
            cam.orbit(45, 0);  // Rotate 45 degrees horizontally
            Projection.Vector3f posAfter = cam.getPosition();
            assert posBefore.distance(posAfter) > 0.1f : "Orbit should change camera position";
            System.out.println("✓ testCameraOrbit");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraOrbit: " + e.getMessage());
            failed++;
        }

        // Test 6: Camera View Matrix
        try {
            Camera cam = new Camera();
            cam.setIsometric();
            Projection.Matrix4f viewMatrix = cam.getViewMatrix();
            assert viewMatrix != null : "View matrix should not be null";
            assert viewMatrix.m[3][3] == 1.0f : "View matrix should be valid";
            System.out.println("✓ testCameraViewMatrix");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraViewMatrix: " + e.getMessage());
            failed++;
        }

        // Test 7: Camera Projection Matrix
        try {
            Camera cam = new Camera();
            Projection.Matrix4f projMatrix = cam.getPerspectiveMatrix(1.33f);
            assert projMatrix != null : "Projection matrix should not be null";
            assert projMatrix.m[3][2] == -1.0f : "Projection matrix should be perspective";
            System.out.println("✓ testCameraProjectionMatrix");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testCameraProjectionMatrix: " + e.getMessage());
            failed++;
        }

        // Test 8: SceneNode Creation
        try {
            SceneNode node = new SceneNode("TestNode");
            assert "TestNode".equals(node.getName()) : "Node name should match";
            System.out.println("✓ testSceneNodeCreation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneNodeCreation: " + e.getMessage());
            failed++;
        }

        // Test 9: SceneNode Transform
        try {
            SceneNode node = new SceneNode("TestNode");
            node.setPosition(1, 2, 3);
            Projection.Vector3f pos = node.getPosition();
            assert Math.abs(pos.x - 1) < 0.01f : "X position wrong";
            assert Math.abs(pos.y - 2) < 0.01f : "Y position wrong";
            assert Math.abs(pos.z - 3) < 0.01f : "Z position wrong";
            System.out.println("✓ testSceneNodeTransform");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneNodeTransform: " + e.getMessage());
            failed++;
        }

        // Test 10: SceneNode World Matrix
        try {
            SceneNode node = new SceneNode("TestNode");
            node.setPosition(5, 5, 5);
            Projection.Matrix4f worldMatrix = node.getWorldMatrix();
            assert worldMatrix != null : "World matrix should not be null";
            // Test that translation is applied
            Projection.Vector3f testVec = new Projection.Vector3f(0, 0, 0);
            Projection.Vector3f transformed = worldMatrix.transform(testVec);
            assert Math.abs(transformed.x - 5) < 0.1f : "Translation not applied";
            System.out.println("✓ testSceneNodeWorldMatrix");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneNodeWorldMatrix: " + e.getMessage());
            failed++;
        }

        // Test 11: SceneGraph Creation
        try {
            SceneGraph graph = new SceneGraph();
            assert graph.nodeCount() == 0 : "New graph should be empty";
            System.out.println("✓ testSceneGraphCreation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneGraphCreation: " + e.getMessage());
            failed++;
        }

        // Test 12: SceneGraph Add Node
        try {
            SceneGraph graph = new SceneGraph();
            SceneNode node = new SceneNode("TestNode");
            graph.addNode(node);
            assert graph.nodeCount() == 1 : "Graph should contain 1 node";
            assert graph.getNode("TestNode") != null : "Node should be retrievable";
            System.out.println("✓ testSceneGraphAddNode");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneGraphAddNode: " + e.getMessage());
            failed++;
        }

        // Test 13: SceneGraph Furniture Nodes
        try {
            SceneGraph graph = new SceneGraph();
            SceneNode furNode = new SceneNode("Furniture1");
            graph.addFurnitureNode(furNode);
            assert graph.furnitureCount() == 1 : "Graph should have 1 furniture node";
            assert graph.getFurnitureNode(0) != null : "Furniture node should be retrievable";
            System.out.println("✓ testSceneGraphFurnitureNodes");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testSceneGraphFurnitureNodes: " + e.getMessage());
            failed++;
        }

        // Test 14: LightingManager Creation
        try {
            LightingManager lighting = new LightingManager();
            assert lighting != null : "Lighting manager should be created";
            assert lighting.getAmbientLight() != null : "Ambient light should exist";
            assert lighting.getDirectionalLight() != null : "Directional light should exist";
            System.out.println("✓ testLightingManagerCreation");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testLightingManagerCreation: " + e.getMessage());
            failed++;
        }

        // Test 15: LightingManager Light Calculations
        try {
            LightingManager lighting = new LightingManager();
            Projection.Vector3f normal = new Projection.Vector3f(0, 1, 0).normalize();
            Projection.Vector3f toLight = new Projection.Vector3f(1, 1, 0).normalize();
            float intensity = lighting.calculateLightIntensity(normal, toLight);
            assert intensity >= 0 && intensity <= 1 : "Light intensity should be 0-1";
            System.out.println("✓ testLightingCalculations");
            passed++;
        } catch (Exception e) {
            System.out.println("✗ testLightingCalculations: " + e.getMessage());
            failed++;
        }

        // Summary
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST RESULTS: " + passed + " passed, " + failed + " failed");
        System.out.println("TOTAL: " + (passed + failed) + " tests");
        System.out.println("=".repeat(50));

        if (failed == 0) {
            System.out.println("\n✓ ALL TESTS PASSED! Phase 2 Foundation Complete!");
            System.exit(0);
        } else {
            System.out.println("\n✗ Some tests failed. Please review the errors above.");
            System.exit(1);
        }
    }
}

