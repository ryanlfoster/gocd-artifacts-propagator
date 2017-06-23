package com.lambdastack.go.executors;

import com.lambdastack.go.core.DependencyResolver;

import java.util.Map;


public class ArtifactsDownloaderExecutor {
    public void execute(Map context) throws Exception {
        new DependencyResolver(context).resolveDependencies().fetchArtifacts();
    }
}

