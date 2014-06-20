package com.lambdastack.go.executors;

import com.lambdastack.go.core.ScriptRunner;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutionContext;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArtifactsUploaderExecutor implements TaskExecutor {

    private static TaskExecutionContext taskExecutionContext;

    @Override
    public ExecutionResult execute(TaskConfig taskConfig, TaskExecutionContext taskExecutionContext) {
        this.taskExecutionContext = taskExecutionContext;
        try {
            String serverUrl = getEnvironmentVariable(taskExecutionContext, "GO_SERVER_URL");
            String pipeline = getEnvironmentVariable(taskExecutionContext, "GO_PIPELINE_NAME");
            String pipelineCounter = getEnvironmentVariable(taskExecutionContext, "GO_PIPELINE_COUNTER");
            String stage = getEnvironmentVariable(taskExecutionContext, "GO_STAGE_NAME");
            String stageCounter = getEnvironmentVariable(taskExecutionContext, "GO_STAGE_COUNTER");
            String job = getEnvironmentVariable(taskExecutionContext, "GO_JOB_NAME");
            String workingDir = System.getProperty("user.dir") + "/pipelines/" + pipeline;
            String artifactURL = serverUrl + "files/" + pipeline + "/" + pipelineCounter + "/" + stage + "/" + stageCounter + "/" + job;
            basicSetup(workingDir);
            ScriptRunner.execute(workingDir, ".upload_artifacts.sh", Arrays.asList(artifactURL));
            return ExecutionResult.success("Artifacts uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ExecutionResult.failure("Uploading artifacts failed", e);
        }
    }

    public static void logMessage(String message) {
        taskExecutionContext.console().printLine(message);
    }

    private void basicSetup(String workingDir) throws IOException {
        URL artifactUploadingShellScriptURL = getClass().getResource("/upload_artifacts.sh");
        File dest = new File(workingDir + "/.upload_artifacts.sh");
        FileUtils.copyURLToFile(artifactUploadingShellScriptURL, dest);
    }

    private String getEnvironmentVariable(TaskExecutionContext context, String variable) throws Exception {
        return this.constructEnvironmentVariable(context, variable).get(variable);
    }

    private Map<String, String> constructEnvironmentVariable(TaskExecutionContext context, String variable) throws Exception {
        Map<String, String> environmentMap = context.environment().asMap();
        Map<String, String> mutableEnvironmentMap = new HashMap<String, String>();
        for (String key : environmentMap.keySet()) {
            mutableEnvironmentMap.put(key, environmentMap.get(key));
        }
        URL goServerURL = new URL(environmentMap.get("GO_SERVER_URL"));
        mutableEnvironmentMap.put("GO_SERVER_URL", "http://" + goServerURL.getHost() + ":8153/go/");
        return mutableEnvironmentMap;
    }

}
