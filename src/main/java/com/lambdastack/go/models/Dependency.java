package com.lambdastack.go.models;

public class Dependency {

    private String goServerUrl;
    private String pipelineName;
    private String locator;


    public Dependency(String goServerUrl, String pipelineName, String locator) {
        this.goServerUrl = goServerUrl;
        this.pipelineName = pipelineName;
        this.locator = locator;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public String getLocator() {
        return locator;
    }

    public String getGoServerUrl() {
        return goServerUrl;
    }
}
