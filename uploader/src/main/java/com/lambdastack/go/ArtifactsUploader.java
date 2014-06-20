package com.lambdastack.go;

import com.lambdastack.go.executors.ArtifactsUploaderExecutor;
import com.lambdastack.go.views.ArtifactsUploaderView;
import com.thoughtworks.go.plugin.api.annotation.Extension;
import com.thoughtworks.go.plugin.api.response.validation.ValidationResult;
import com.thoughtworks.go.plugin.api.task.Task;
import com.thoughtworks.go.plugin.api.task.TaskConfig;
import com.thoughtworks.go.plugin.api.task.TaskExecutor;
import com.thoughtworks.go.plugin.api.task.TaskView;

@Extension
public class ArtifactsUploader implements Task {
    @Override
    public TaskConfig config() {
        TaskConfig config = new TaskConfig();
        return config;
    }

    @Override
    public TaskExecutor executor() {
        return new ArtifactsUploaderExecutor();
    }


    @Override
    public TaskView view() {
        return new ArtifactsUploaderView(); // TODO: If needed create a separate view
    }

    @Override
    public ValidationResult validate(TaskConfig taskConfig) {
        return new ValidationResult(); // No errors added to it.
    }
}
