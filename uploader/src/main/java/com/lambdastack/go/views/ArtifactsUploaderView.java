package com.lambdastack.go.views;

import org.apache.commons.io.IOUtils;

public class ArtifactsUploaderView {
    public String displayValue() {
        return "ArtifactsPropagator - Uploader";
    }

    public String template() {
        try {
            return IOUtils.toString(getClass().getResourceAsStream("/views/downloader.template.html"), "UTF-8");
        } catch (Exception e) {
            return "Failed to find template: " + e.getMessage();
        }
    }
}
