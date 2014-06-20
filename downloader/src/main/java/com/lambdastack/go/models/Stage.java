package com.lambdastack.go.models;

import com.google.api.client.util.Key;

public class Stage {
    @Key("locator")
    private String locator;

    @Key("name")
    private String name;

    @Key("status")
    private String status;

    public String getLocator() {
        return locator;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Stage(String locator, String name, String status) {
        this.locator = locator;
        this.name = name;
        this.status = status;
    }

    public Stage() {
    }
}
