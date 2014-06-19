package com.lambdastack.go.core;

import com.lambdastack.go.RestClient;
import com.lambdastack.go.exceptions.GoEnvironmentVariableNotFoundException;
import com.lambdastack.go.models.ValueStreamMap;
import com.thoughtworks.go.plugin.api.task.EnvironmentVariables;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DependencyResolverTest {

    DependencyResolver dependencyResolver;
    TaskExecutionContext taskExecutionContext;
    EnvironmentVariables environmentVariables;
    Map<String, String> environmentVariablesMap;

    @Before
    public void setUp() {
        dependencyResolver = mock(DependencyResolver.class);
        taskExecutionContext = mock(TaskExecutionContext.class);
        environmentVariables = mock(EnvironmentVariables.class);

        when(dependencyResolver.getTaskExecutionContext()).thenReturn(taskExecutionContext);

        environmentVariablesMap = new HashMap<String, String>();
        environmentVariablesMap.put("GO_SERVER_URL","https://10.0.0.1:8154/go/");
        environmentVariablesMap.put("GO_PIPELINE_NAME", "C");
        environmentVariablesMap.put("GO_PIPELINE_COUNTER", "7");
        environmentVariablesMap.put("GO_STAGE_NAME", "Build");
        environmentVariablesMap.put("GO_STAGE_COUNTER", "1");

        when(taskExecutionContext.environment()).thenReturn(environmentVariables);
        when(environmentVariables.asMap()).thenReturn(environmentVariablesMap);
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
    public void shouldConstructValueStreamMapURL() throws GoEnvironmentVariableNotFoundException {

        when(dependencyResolver.constructValueStreamMapUrl()).thenCallRealMethod();

        String actualValueStreamMapURL = "https://10.0.0.1:8154/go/pipelines/value_stream_map/C/7.json";
        assertEquals(actualValueStreamMapURL, dependencyResolver.constructValueStreamMapUrl());
    }

}