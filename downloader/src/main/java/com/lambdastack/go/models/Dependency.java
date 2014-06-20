package com.lambdastack.go.models;

import com.lambdastack.go.RestClient;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> fetchArtifact() throws Exception{
        List<String> jobURLs = new RestClient().getStageFeed(goServerUrl, locator);
        List<String> artifactsURL = new ArrayList<String>();
        for(String jobUrl : jobURLs) {
            artifactsURL.add(new RestClient().getArtifactURLsFromJobFeed(jobUrl)+"/artifacts.zip");
        }
        return artifactsURL;
    }


}
