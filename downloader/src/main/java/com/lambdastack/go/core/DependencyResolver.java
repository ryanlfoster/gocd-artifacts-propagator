package com.lambdastack.go.core;

import com.lambdastack.go.RestClient;
import com.lambdastack.go.exceptions.GoEnvironmentVariableNotFoundException;
import com.lambdastack.go.models.Dependencies;
import com.lambdastack.go.models.ValueStreamMap;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DependencyResolver {

    public static final String VALUE_STREAM_MAP_RESOURCE = "pipelines/value_stream_map/";

    public static TaskExecutionContext taskExecutionContext;

    public DependencyResolver(TaskExecutionContext taskExecutionContext) {
        this.taskExecutionContext = taskExecutionContext;
    }

    public Dependencies resolveDependencies() throws Exception {
        return new Dependencies(getEnvironmentVariable("GO_SERVER_URL"),
                                getValueStreamMapTraversalEngine(fetchValueStreamMap()).getUpstreamPipelines(),
                                getEnvironmentVariable("GO_PIPELINE_NAME")
                                );
    }

    public static void logMessage(String message) throws Exception {
        if(taskExecutionContext!=null){
            taskExecutionContext.console().printLine(message);
        }
    }

    protected ValueStreamMap fetchValueStreamMap() throws Exception {
        return getRestClient().getValueStreamMapFromGoServer(constructValueStreamMapUrl());
    }

    protected String constructValueStreamMapUrl() throws Exception {
        return getEnvironmentVariable("GO_SERVER_URL") +
                VALUE_STREAM_MAP_RESOURCE +
                getEnvironmentVariable("GO_PIPELINE_NAME") +
                "/" +
                getEnvironmentVariable("GO_PIPELINE_COUNTER") + ".json";
    }

    private String getEnvironmentVariable(String key) throws Exception {
        Map<String, String> environmentMap = getEnvironmentFromTaskExecutionContext();
        String valueForGivenKey = environmentMap.get(key);
        if(valueForGivenKey == null || valueForGivenKey.isEmpty()) {
            throw new GoEnvironmentVariableNotFoundException("Environment variable " + key + " has not value set");
        }

        return valueForGivenKey;
    }

    private Map<String, String> getEnvironmentFromTaskExecutionContext() throws MalformedURLException {
        Map<String, String> environmentMap = getTaskExecutionContext().environment().asMap();
        Map<String, String> mutableEnvironmentMap = new HashMap<String, String>();
        for(String key : environmentMap.keySet()) {
            mutableEnvironmentMap.put(key, environmentMap.get(key));
        }
        URL goServerURL = new URL(environmentMap.get("GO_SERVER_URL"));
        mutableEnvironmentMap.put("GO_SERVER_URL", "http://artifacts-propagator:Helpdesk@" + goServerURL.getHost() + ":8153/go/");
        return mutableEnvironmentMap;
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
