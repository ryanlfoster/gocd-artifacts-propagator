package com.lambdastack.go.core;

import com.lambdastack.go.RestClient;
import com.lambdastack.go.exceptions.GoEnvironmentVariableNotFoundException;
import com.lambdastack.go.models.Dependencies;
import com.lambdastack.go.models.ValueStreamMap;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;

import java.util.Map;

public class DependencyResolver {

    public static final String VALUE_STREAM_MAP_RESOURCE = "pipelines/value_stream_map/";

    private TaskExecutionContext taskExecutionContext;

    public DependencyResolver(TaskExecutionContext taskExecutionContext) {
        this.taskExecutionContext = taskExecutionContext;
    }

    public Dependencies resolveDependencies() throws Exception {
        return new Dependencies(getEnvironmentVariable("GO_SERVER_URL"),
                                getValueStreamMapTraversalEngine(fetchValueStreamMap()).getUpstreamPipelines());
    }

    protected ValueStreamMap fetchValueStreamMap() throws Exception {
        return getRestClient().getValueStreamMapFromGoServer(constructValueStreamMapUrl());
    }

    protected String constructValueStreamMapUrl() throws GoEnvironmentVariableNotFoundException {
        return getEnvironmentVariable("GO_SERVER_URL") +
                VALUE_STREAM_MAP_RESOURCE +
                getEnvironmentVariable("GO_PIPELINE_NAME") +
                "/" +
                getEnvironmentVariable("GO_PIPELINE_COUNTER") + ".json";
    }

    private String getEnvironmentVariable(String key) throws GoEnvironmentVariableNotFoundException {
        Map<String, String> environmentMap = getEnvironmentFromTaskExecutionContext();
        String valueForGivenKey = environmentMap.get(key);
        if(valueForGivenKey == null || valueForGivenKey.isEmpty()) {
            throw new GoEnvironmentVariableNotFoundException("Environment variable " + key + " has not value set");
        }

        return valueForGivenKey;
    }

    private Map<String, String> getEnvironmentFromTaskExecutionContext() {
        return getTaskExecutionContext().environment().asMap();
    }

    protected RestClient getRestClient() {
        return new RestClient();
    }

    protected TaskExecutionContext getTaskExecutionContext() {
        return taskExecutionContext;
    }

    protected ValueStreamMapTraversalEngine getValueStreamMapTraversalEngine(ValueStreamMap valueStreamMap) {
        return new ValueStreamMapTraversalEngine(valueStreamMap);
    }
}
