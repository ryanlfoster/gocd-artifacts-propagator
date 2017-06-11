package com.lambdastack.go.executors;

import com.lambdastack.go.ArtifactsUploader;
import com.lambdastack.go.core.ScriptRunner;
import com.thoughtworks.go.plugin.api.response.execution.ExecutionResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArtifactsUploaderExecutor {

    private static Map map;

    public ExecutionResult execute(Map context) {
        this.map = context;
        try {
            String serverUrl = getEnvironmentVariable(context, "GO_SERVER_URL");
            String pipeline = getEnvironmentVariable(context, "GO_PIPELINE_NAME");
            String pipelineCounter = getEnvironmentVariable(context, "GO_PIPELINE_COUNTER");
            String stage = getEnvironmentVariable(context, "GO_STAGE_NAME");
            String stageCounter = getEnvironmentVariable(context, "GO_STAGE_COUNTER");
            String job = getEnvironmentVariable(context, "GO_JOB_NAME");
            String workingDir = System.getProperty("user.dir") + "/pipelines/" + pipeline;
            String artifactURL = serverUrl + "files/" + pipeline + "/" + pipelineCounter + "/" + stage + "/" + stageCounter + "/" + job;
            basicSetup(workingDir);
            ScriptRunner.execute(workingDir, ".upload_artifacts.sh", Arrays.asList(artifactURL));
            return ExecutionResult.success("Artifacts uploaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            ArtifactsUploader.log(e.getMessage());
            ArtifactsUploader.log(stackTraceToString(e));
            return ExecutionResult.failure("Uploading artifacts failed", e);
        }
    }

    private void basicSetup(String workingDir) throws IOException {
        URL artifactUploadingShellScriptURL = getClass().getResource("/upload_artifacts.sh");
        File dest = new File(workingDir + "/.upload_artifacts.sh");
        FileUtils.copyURLToFile(artifactUploadingShellScriptURL, dest);
    }

    private String getEnvironmentVariable(Map context, String variable) throws Exception {
        return this.constructEnvironmentVariable(context).get(variable);
    }

    private Map<String, String> constructEnvironmentVariable(Map context) throws Exception {
        Map<String, String> environmentMap = (Map<String, String>) context.get("environmentVariables");
        Map<String, String> mutableEnvironmentMap = new HashMap<String, String>();
        for (String key : environmentMap.keySet()) {
            mutableEnvironmentMap.put(key, environmentMap.get(key));
        }
        URL goServerURL = new URL(environmentMap.get("GO_SERVER_URL"));
        mutableEnvironmentMap.put("GO_SERVER_URL", "http://artifacts-propagator:Helpdesk@" + goServerURL.getHost() + ":8153/go/");
        return mutableEnvironmentMap;
    }

    public String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
