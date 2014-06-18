package com.lambdastack.go.executors;

import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;

public class ArtifactsDownloaderExecutor implements TaskExecutor {

    @Override
    public ExecutionResult execute(TaskConfig taskConfig, TaskExecutionContext taskExecutionContext) {
        taskExecutionContext.environment().writeTo(taskExecutionContext.console());
        return ExecutionResult.success("'Hello World!' from ArtifactsDownloaderExecutor");
    }
}
