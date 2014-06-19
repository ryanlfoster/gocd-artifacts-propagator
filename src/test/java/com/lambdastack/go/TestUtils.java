package com.lambdastack.go;

import com.lambdastack.go.models.Node;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {
    public static boolean checkNodeAvailable(List<Node> nodes, List<String> wantedNodes) {
        List<String> actualNodes = new ArrayList<String>();
        for(Node node : nodes) {
            actualNodes.add(node.getId());
        }
        return actualNodes.containsAll(wantedNodes) && wantedNodes.containsAll(actualNodes);
    }
}