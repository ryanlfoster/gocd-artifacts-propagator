package com.lambdastack.go.models;

import com.google.api.client.util.Key;

public class ValueStreamLevel {
    @Key("nodes")
    Node[] nodes;

    public Node[] getNodes() {
        return nodes;
    }
}
