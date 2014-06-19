package com.lambdastack.go.models;

import java.util.List;

public class Dependencies {

    List<Dependency> dependencyList;

    public Dependencies(List<Node> upstreamPipelines) {

    }

    public boolean fetchArtifacts() throws Exception{
        return true;
    }
}
