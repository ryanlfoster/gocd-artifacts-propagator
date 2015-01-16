package com.lambdastack.go.models;

import com.google.api.client.util.Key;

import java.util.Arrays;

public class Node {

    @Key("name")
    private String name;

    @Key("node_type")
    private String nodeType;

    @Key("locator")
    private String locator;

    @Key("parents")
    private String[] parents;

    @Key("depth")
    private int depth;

    @Key("instances")
    private Instance[] instances;

    @Key("id")
    private String id;

    @Key("dependents")
    private String[] dependents;

    public Node(String name,
                String nodeType,
                String locator,
                String[] parents,
                int depth,
                Instance[] instances,
                String id,
                String[] dependents) {
        this.name = name;
        this.nodeType = nodeType;
        this.locator = locator;
        this.parents = parents;
        this.depth = depth;
        this.instances = instances;
        this.id = id;
        this.dependents = dependents;
    }

    public Node() {
    }

    public String getName() {
        return name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public String getLocator() {
        return locator;
    }

    public String[] getParents() {
        return parents;
    }

    public int getDepth() {
        return depth;
    }

    public Instance[] getInstances() {
        return instances;
    }

    public String getId() {
        return id;
    }

    public String[] getDependents() {
        return dependents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (!id.equals(node.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
