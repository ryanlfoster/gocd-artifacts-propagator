package com.lambdastack.go.models;

import com.google.api.client.util.Key;

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
}
