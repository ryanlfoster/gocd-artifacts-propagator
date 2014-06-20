package com.lambdastack.go.models;

import com.lambdastack.go.exceptions.UpstreamPipelineBrokenException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dependencies {

    List<Dependency> dependencyList = new ArrayList<Dependency>();
    String goServerURL;

    public Dependencies(String goServerURL, List<Node> upstreamPipelines) throws Exception {
        URL goServerURLObject = new URL(goServerURL);
        this.goServerURL = goServerURLObject.getProtocol() + "://" + goServerURLObject.getHost() + ":" +
                                goServerURLObject.getPort();
        for(Node node : upstreamPipelines) {
            processNode(node);
        }
    }

    private void processNode(Node node) throws UpstreamPipelineBrokenException {
        for(Instance instance : node.getInstances()){
            for(Stage stage : instance.getStages()){
                if(stage.getStatus() != "Passed"){
                    throw new UpstreamPipelineBrokenException("Upstream " + node.getId() + " is failing");
                }
                dependencyList.add(new Dependency(goServerURL, node.getId(), stage.getLocator()));
            }
        }
    }

    public boolean fetchArtifacts() throws Exception{
        return true;
    }

    public List<Dependency> getDependencyList() {
        return dependencyList;
    }
}
