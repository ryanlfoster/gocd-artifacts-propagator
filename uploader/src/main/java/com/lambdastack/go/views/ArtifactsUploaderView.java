package com.lambdastack.go.views;

import com.thoughtworks.go.plugin.api.task.TaskView;
import org.apache.commons.io.IOUtils;

public class ArtifactsUploaderView implements TaskView {
    @Override
    public String displayValue() {
        return "ArtifactsPropagator - Uploader";
    }

    @Override
    public String template() {
        try {
            return IOUtils.toString(getClass().getResourceAsStream("/views/downloader.template.html"), "UTF-8");
        } catch (Exception e) {
            return "Failed to find template: " + e.getMessage();
        }
    }
}
