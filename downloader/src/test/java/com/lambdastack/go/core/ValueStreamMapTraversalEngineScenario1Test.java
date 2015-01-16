package com.lambdastack.go.core;

import com.lambdastack.go.models.Instance;
import com.lambdastack.go.models.Node;
import com.lambdastack.go.models.ValueStreamLevel;
import com.lambdastack.go.models.ValueStreamMap;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lambdastack.go.TestUtils.checkNodeAvailable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValueStreamMapTraversalEngineScenario1Test {

    ValueStreamMap valueStreamMap;
    ValueStreamLevel valueStreamLevel1;
    ValueStreamLevel valueStreamLevel2;
    ValueStreamLevel valueStreamLevel3;
    ValueStreamLevel valueStreamLevel4;
    ValueStreamLevel valueStreamLevel5;

    ValueStreamMapTraversalEngine valueStreamMapTraversalEngine;

    @Before
    public void setUp() throws Exception {

        valueStreamMapTraversalEngine = mock(ValueStreamMapTraversalEngine.class);
        valueStreamMap = mock(ValueStreamMap.class);
        valueStreamLevel1 = mock(ValueStreamLevel.class);
        valueStreamLevel2 = mock(ValueStreamLevel.class);
        valueStreamLevel3 = mock(ValueStreamLevel.class);
        valueStreamLevel4 = mock(ValueStreamLevel.class);
        valueStreamLevel5 = mock(ValueStreamLevel.class);

        String[] level1Parents = {};
        String[] level2Parents = {};
        String[] level3Parents = {};
        String[] level4Parents = {"A", "B", "C"};
        String[] level5Parents = {"D"};

        String[] level1Dependents1 = {"A"};
        String[] level1Dependents2 = {"D"};

        String[] level2Dependents1 = {"B"};
        String[] level2Dependents2 = {"D"};

        String[] level3Dependents1 = {"C"};
        String[] level3Dependents2 = {"D"};

        String[] level4Dependents1 = {"D"};
        String[] level4Dependents2 = {"E"};

        String[] level5Dependents1 = {"D"};
        String[] level5Dependents2 = {"E"};


        Instance[] level1Instances = {};
        Instance[] level2Instances = {};
        Instance[] level3Instances = {};
        Instance[] level4Instances = {};
        Instance[] level5Instances = {};


        Node[] level1Nodes = {
            new Node("git://something", "GIT","", null, 1, null, "6c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff", level1Dependents1),
            new Node("A","PIPELINE",
                    "/go/tab/pipeline/history/A",
                    level1Parents, 1,
                    level1Instances, "A", level1Dependents2)
        };

        Node[] level2Nodes = {
            new Node("git://something2", "GIT","", null, 1, null, "7c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c33c09156028670284ff", level2Dependents1),
            new Node("B","PIPELINE",
                "/go/tab/pipeline/history/B",
                level2Parents, 1,
                level2Instances, "B", level2Dependents2)
        };

        Node[] level3Nodes = {
            new Node("git://something3", "GIT","", null, 1, null, "7c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c3335342523270284ff", level3Dependents1),
            new Node("C","PIPELINE",
                    "/go/tab/pipeline/history/C",
                    level3Parents, 1,
                    level3Instances, "C", level3Dependents2)
        };

        Node[] level4Nodes = {
            new Node("git://something4", "GIT","", null, 1, null, "2c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c3335342523270284ff", level4Dependents1),
            new Node("D","PIPELINE",
                    "/go/tab/pipeline/history/D",
                    level4Parents, 1,
                    level4Instances, "D", level4Dependents2)
        };

        Node[] level5Nodes = {
            new Node("git://something5", "GIT","", null, 1, null, "1c72ca325837dfc0a0f1bd49eb4aa6f9b05322135711c3335342523270284ff", level5Dependents1),
            new Node("E","PIPELINE",
                    "/go/tab/pipeline/history/E",
                    level5Parents, 1,
                    level5Instances, "E", level5Dependents2)
        };

        when(valueStreamLevel1.getNodes()).thenReturn(level1Nodes);
        when(valueStreamLevel2.getNodes()).thenReturn(level2Nodes);
        when(valueStreamLevel3.getNodes()).thenReturn(level3Nodes);
        when(valueStreamLevel4.getNodes()).thenReturn(level4Nodes);
        when(valueStreamLevel5.getNodes()).thenReturn(level5Nodes);

        ValueStreamLevel[] valueStreamLevels = {valueStreamLevel1, valueStreamLevel2, valueStreamLevel3, valueStreamLevel4, valueStreamLevel5};
        when(valueStreamMap.getValueStreamLevels()).thenReturn(valueStreamLevels);
        valueStreamMapTraversalEngine = new ValueStreamMapTraversalEngine(valueStreamMap);
    }

    @Test
    public void shouldGetUpstreamPipelinesAfterApplyingFilters1() throws Exception {
        // Scenario 1:

        // A ---+
        //      |
        // B ---> ----> D ---> E
        //      |
        // C ---+

        // Current pipeline is D
        // It should return A, B, C as the upstream pipelines

        when(valueStreamMap.getCurrentPipeline()).thenReturn("D");
        List<String> expectedNodes = Arrays.asList("A", "B", "C", "D");

        List<Node> upstreamPipelines = valueStreamMapTraversalEngine.getUpstreamPipelines();
        assertEquals(expectedNodes.size(), upstreamPipelines.size());

        assertTrue(checkNodeAvailable(upstreamPipelines, expectedNodes));
    }

    @Test
    public void shouldGetUpstreamPipelinesAfterApplyingFilters2() throws Exception {
        // Scenario 2:

        // A ---+
        //      |
        // B ---> ----> D ---> E
        //      |
        // C ---+

        // Current pipeline is B
        // It should return empty list as the upstream pipelines

        when(valueStreamMap.getCurrentPipeline()).thenReturn("B");

        List<String> expectedNodes = Arrays.asList("B");

        List<Node> upstreamPipelines = valueStreamMapTraversalEngine.getUpstreamPipelines();
        assertEquals(expectedNodes.size(), upstreamPipelines.size());
        assertTrue(checkNodeAvailable(upstreamPipelines, expectedNodes));
    }

    @Test
    public void shouldGetUpstreamPipelinesAfterApplyingFilters3() throws Exception {
        // Scenario 3:

        // A ---+
        //      |
        // B ---> ----> D ---> E
        //      |
        // C ---+

        // Current pipeline is E
        // It should return A,B,C,D as the upstream pipelines

        when(valueStreamMap.getCurrentPipeline()).thenReturn("E");
        List<String> expectedNodes = Arrays.asList("A", "B", "C", "D", "E");

        List<Node> upstreamPipelines = valueStreamMapTraversalEngine.getUpstreamPipelines();
        assertEquals(expectedNodes.size(), upstreamPipelines.size());
        assertTrue(checkNodeAvailable(upstreamPipelines, expectedNodes));
    }
}

