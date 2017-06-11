package com.lambdastack.go.core;

import com.lambdastack.go.RestClient;
import com.lambdastack.go.models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DependencyResolverTest {

    DependencyResolver dependencyResolver;
    HashMap taskExecutionContext;
    HashMap environmentVariables;
    Map<String, String> environmentVariablesMap;

    @Before
    public void setUp() {
        dependencyResolver = mock(DependencyResolver.class);
        taskExecutionContext = new HashMap();
        environmentVariables = new HashMap();


        when(dependencyResolver.getTaskExecutionContext()).thenReturn(taskExecutionContext);
        environmentVariablesMap = new HashMap<String, String>();
        environmentVariablesMap.put("GO_SERVER_URL", "https://10.0.0.1:8154/go/");
        environmentVariablesMap.put("GO_PIPELINE_NAME", "C");
        environmentVariablesMap.put("GO_PIPELINE_COUNTER", "7");
        environmentVariablesMap.put("GO_STAGE_NAME", "Build");
        environmentVariablesMap.put("GO_STAGE_COUNTER", "1");

        taskExecutionContext.put("environmentVariables", environmentVariablesMap);
    }

    @Test
    public void shouldFetchValueStreamMapGivenEnvironmentVariablesAreGiven() throws Exception {
        String valueStreamMapURL = "https://10.0.0.1:8154/go/pipelines/value_stream_map/C/7.json";
        RestClient restClient = mock(RestClient.class);
        ValueStreamMap valueStreamMap = mock(ValueStreamMap.class);
        when(dependencyResolver.constructValueStreamMapUrl()).thenReturn(valueStreamMapURL);
        when(dependencyResolver.fetchValueStreamMap()).thenCallRealMethod();
        when(dependencyResolver.getRestClient()).thenReturn(restClient);
        when(restClient.getValueStreamMapFromGoServer(valueStreamMapURL)).thenReturn(valueStreamMap);

        assertEquals(valueStreamMap, dependencyResolver.fetchValueStreamMap());
    }

    @Test
    public void shouldConstructValueStreamMapURL() throws Exception {

        when(dependencyResolver.constructValueStreamMapUrl()).thenCallRealMethod();

        String actualValueStreamMapURL = "http://artifacts-propagator:Helpdesk@10.0.0.1:8153/go/pipelines/value_stream_map/C/7.json";
        assertEquals(actualValueStreamMapURL, dependencyResolver.constructValueStreamMapUrl());
    }

    @Test
    public void shouldResolveDependenciesFromValueStreamMap() throws Exception {

        String[] level1Parents = {};
        String[] level2Parents = {};

        String[] level1Dependents = {"D"};
        String[] level2Dependents = {"D"};

        Stage[] level1Stages = {new Stage("/go/pipelines/A/4/defaultStage/2", "defaultStage", "Passed")};
        Stage[] level2Stages = {new Stage("/go/pipelines/B/5/defaultStage/10", "defaultStage", "Passed")};

        Instance[] level1Instances = {
                new Instance(level1Stages)
        };
        Instance[] level2Instances = {
                new Instance(level2Stages)
        };

        Node[] nodes = {
                new Node("A", "PIPELINE",
                        "/go/tab/pipeline/history/A",
                        level1Parents, 1,
                        level1Instances, "A", level1Dependents),
                new Node("B", "PIPELINE",
                        "/go/tab/pipeline/history/B",
                        level2Parents, 1,
                        level2Instances, "B", level2Dependents)

        };


        ValueStreamMap valueStreamMap = mock(ValueStreamMap.class);
        ValueStreamMapTraversalEngine valueStreamMapTraversalEngine = mock(ValueStreamMapTraversalEngine.class);

        when(dependencyResolver.getTaskExecutionContext()).thenReturn(taskExecutionContext);
        when(dependencyResolver.fetchValueStreamMap()).thenReturn(valueStreamMap);
        when(dependencyResolver.getValueStreamMapTraversalEngine(valueStreamMap)).thenReturn(valueStreamMapTraversalEngine);
        when(valueStreamMapTraversalEngine.getUpstreamPipelines()).thenReturn(Arrays.asList(nodes));
        when(dependencyResolver.resolveDependencies()).thenCallRealMethod();

        Dependencies actualDependencies = dependencyResolver.resolveDependencies();

        assertNotNull(actualDependencies);
        assertEquals(2, actualDependencies.getDependencyList().size());
        assertTrue(checkDependencyExists(actualDependencies, "http://10.0.0.1:8153", "A", "/go/pipelines/A/4/defaultStage/2"));
        assertTrue(checkDependencyExists(actualDependencies, "http://10.0.0.1:8153", "B", "/go/pipelines/B/5/defaultStage/10"));
    }

    private boolean checkDependencyExists(Dependencies actualDependencies, String goServerUrl, String pipelineName, String artifactURL) {
        for (Dependency dependency : actualDependencies.getDependencyList()) {
            if (dependency.getGoServerUrl().equals(goServerUrl)
                    && dependency.getPipelineName().equals(pipelineName)
                    && dependency.getLocator().equals(artifactURL)) {
                return true;
            }
        }
        return false;
    }
}