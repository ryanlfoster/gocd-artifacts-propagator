package com.lambdastack.go.models;

import org.junit.Before;
import org.junit.Test;

public class DependencyTest {

    Dependency dependency;

    @Before
    public void setUp() {
        dependency = new Dependency("https://10.0.0.1:8154", "A", "/go/pipelines/A/4/defaultStage/2");
    }

    @Test
    public void shouldFetchArtifacts() throws Exception {
//        dependency.fetchArtifact();
    }

}