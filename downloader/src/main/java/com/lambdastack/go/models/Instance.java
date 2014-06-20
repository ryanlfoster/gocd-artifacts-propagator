package com.lambdastack.go.models;

import com.google.api.client.util.Key;

public class Instance {

    @Key("stages")
    private Stage[] stages;

    @Key("locator")
    private String locator;

    @Key("counter")
    private int counter;

    @Key("label")
    private String label;

    public Stage[] getStages() {
        return stages;
    }

    public Instance(Stage[] stages) {
        this.stages = stages;
    }

    public Instance() {
    }
}
