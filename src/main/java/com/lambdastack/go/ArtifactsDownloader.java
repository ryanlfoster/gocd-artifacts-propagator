package com.lambdastack.go;

import com.lambdastack.go.executors.ArtifactsDownloaderExecutor;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;
import org.apache.commons.io.IOUtils;

import com.lambdastack.go.views.ArtifactsDownloaderView;

@Extension
public class ArtifactsDownloader implements Task {

    @Override
    public TaskConfig config() {
        TaskConfig config = new TaskConfig();
        return config;
    }

    @Override
    public TaskExecutor executor() {
        return new ArtifactsDownloaderExecutor();
    }

    @Override
    public TaskView view() {
        return new ArtifactsDownloaderView();
    }

    @Override
    public ValidationResult validate(TaskConfig taskConfig) {
        return new ValidationResult(); // No errors added to it.
    }
}
