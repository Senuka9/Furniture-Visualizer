package com.teamname.furniviz.renderer3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SceneGraph - Manages the hierarchy of all 3D objects in the scene
 *
 * Organizes all scene nodes and provides efficient access
 */
public class SceneGraph {
    private List<SceneNode> rootNodes = new ArrayList<>();
    private Map<String, SceneNode> nodeMap = new HashMap<>();
    private List<SceneNode> furnitureNodes = new ArrayList<>();

    /**
     * Add a node to the scene (as root node)
     */
    public void addNode(SceneNode node) {
        if (!nodeMap.containsKey(node.getName())) {
            rootNodes.add(node);
            nodeMap.put(node.getName(), node);
        }
    }

    /**
     * Add a furniture node
     */
    public void addFurnitureNode(SceneNode node) {
        addNode(node);
        furnitureNodes.add(node);
    }

    /**
     * Get node by name
     */
    public SceneNode getNode(String name) {
        return nodeMap.get(name);
    }

    /**
     * Get furniture node by index
     */
    public SceneNode getFurnitureNode(int index) {
        if (index >= 0 && index < furnitureNodes.size()) {
            return furnitureNodes.get(index);
        }
        return null;
    }

    /**
     * Get all root nodes
     */
    public List<SceneNode> getRootNodes() {
        return new ArrayList<>(rootNodes);
    }

    /**
     * Get all nodes (root + children)
     */
    public List<SceneNode> getAllNodes() {
        List<SceneNode> all = new ArrayList<>(rootNodes);
        for (SceneNode root : rootNodes) {
            addChildrenRecursive(root, all);
        }
        return all;
    }

    /**
     * Get all furniture nodes
     */
    public List<SceneNode> getFurnitureNodes() {
        return new ArrayList<>(furnitureNodes);
    }

    /**
     * Helper: recursively add children
     */
    private void addChildrenRecursive(SceneNode node, List<SceneNode> list) {
        for (SceneNode child : node.getChildren()) {
            list.add(child);
            addChildrenRecursive(child, list);
        }
    }

    /**
     * Remove node by name
     */
    public void removeNode(String name) {
        SceneNode node = nodeMap.remove(name);
        if (node != null) {
            rootNodes.remove(node);
            furnitureNodes.remove(node);
        }
    }

    /**
     * Clear entire scene
     */
    public void clear() {
        rootNodes.clear();
        nodeMap.clear();
        furnitureNodes.clear();
    }

    /**
     * Get number of nodes
     */
    public int nodeCount() {
        return nodeMap.size();
    }

    /**
     * Get number of furniture nodes
     */
    public int furnitureCount() {
        return furnitureNodes.size();
    }
}

