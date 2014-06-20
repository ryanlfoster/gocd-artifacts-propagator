package com.lambdastack.go.models;

import com.lambdastack.go.core.ScriptRunner;
import com.lambdastack.go.exceptions.UpstreamPipelineBrokenException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dependencies {

    List<Dependency> dependencyList = new ArrayList<Dependency>();
    String goServerURL;
    String currentPipelineName;
    String currentPipelineWorkingDir;

    public Dependencies(String goServerURL, List<Node> upstreamPipelines, String currentPipelineName) throws Exception {
        URL goServerURLObject = new URL(goServerURL);
        this.goServerURL = goServerURLObject.getProtocol() + "://" + goServerURLObject.getHost() + ":" +
                                goServerURLObject.getPort();
        this.currentPipelineName = currentPipelineName;
        this.currentPipelineWorkingDir = System.getProperty("user.dir") + "/pipelines/" + currentPipelineName;
        for(Node node : upstreamPipelines) {
            processNode(node);
        }
    }

    private void processNode(Node node) throws UpstreamPipelineBrokenException {
        for(Instance instance : node.getInstances()){
            for(Stage stage : instance.getStages()){
                if(!stage.getStatus().equals("Passed")){
                    throw new UpstreamPipelineBrokenException("Upstream " + node.getId() + " is failing");
                }
                dependencyList.add(new Dependency(goServerURL, node.getId(), stage.getLocator()));
            }
        }
    }

    public boolean fetchArtifacts() throws Exception{
        basicSetup();
        PrintWriter writer = new PrintWriter(this.currentPipelineWorkingDir + "/.artifacts_to_be_fetched");
        for(Dependency dependency : dependencyList) {
            for(String artifactURL : dependency.fetchArtifact()) {
                writer.println(artifactURL);
            }
        }
        writer.close();
        ScriptRunner.execute(this.currentPipelineWorkingDir, ".fetch_artifacts.sh", new ArrayList<String>());
        return true;
    }

    private void basicSetup() throws IOException {
        URL artifactFetchingShellScriptURL = getClass().getResource("/fetch_artifacts.sh");
        File dest = new File(this.currentPipelineWorkingDir + "/.fetch_artifacts.sh");
        FileUtils.copyURLToFile(artifactFetchingShellScriptURL, dest);
    }

    public List<Dependency> getDependencyList() {
        return dependencyList;
    }
}
