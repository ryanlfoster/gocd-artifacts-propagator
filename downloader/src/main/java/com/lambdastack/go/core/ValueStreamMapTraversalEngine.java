package com.lambdastack.go.core;

import com.lambdastack.go.models.Node;
import com.lambdastack.go.models.ValueStreamLevel;
import com.lambdastack.go.models.ValueStreamMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValueStreamMapTraversalEngine {
    private ValueStreamMap valueStreamMap;

    public ValueStreamMapTraversalEngine(ValueStreamMap valueStreamMap) {
        this.valueStreamMap = valueStreamMap;
    }

    public List<Node> getUpstreamPipelines() {
        Set<String> pipelinesToLook = constructInitalPipelineSeed();
        Set<Node> upstreamPipelines = new HashSet<Node>();

        while(!pipelinesToLook.isEmpty()) {
            Set<String> pipelineToLookForInNextCycle = new HashSet<String>();
            for(String nodeName: pipelinesToLook) {
                Node node = getNodeFromValueStreamMap(nodeName);
                if(node.getNodeType().equals("PIPELINE")){
                    upstreamPipelines.add(node);
                }

                if(node.getNodeType().equals("PIPELINE")) {
                    pipelineToLookForInNextCycle.addAll(new HashSet<String>(Arrays.asList(node.getParents())));
                }
            }
            pipelinesToLook = pipelineToLookForInNextCycle;
        }
        return new ArrayList(upstreamPipelines);
    }

    private Set<String> constructInitalPipelineSeed() {
        return new HashSet<String>(Arrays.asList(valueStreamMap.getCurrentPipeline()));
    }

    private Node getNodeFromValueStreamMap(String nodeName) {

        for(ValueStreamLevel level : valueStreamMap.getValueStreamLevels()) {
            for(Node node : level.getNodes()) {
                if(node.getId().equals(nodeName)) {
                    return node;
                }
            }
        }
        throw new NullPointerException("No node found with name "+ nodeName);
    }
}
