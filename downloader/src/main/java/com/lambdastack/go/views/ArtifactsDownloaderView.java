package com.lambdastack.go.views;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

public class ArtifactsDownloaderView {
    public String displayValue() {
        return "ArtifactsPropagator - Downloader";
    }

    public String template() throws IOException {
        return IOUtils.toString(getClass().getResourceAsStream("/views/downloader.template.html"), "UTF-8");
    }
}
