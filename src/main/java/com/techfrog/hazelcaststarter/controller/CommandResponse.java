package com.techfrog.hazelcaststarter.controller;

public class CommandResponse {
    private String value;

    public CommandResponse(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
