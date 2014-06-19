package com.lambdastack.go.models;

import com.google.api.client.util.Key;

public class Stage {
    @Key("locator")
    private String locator;

    @Key("name")
    private String name;

    @Key("status")
    private String status;
}
