package com.millionaire_project.millionaire_project.dto.res;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class DynamicResponse {

    private Map<String, Object> properties = new HashMap<>();

    @JsonAnySetter
    public void set(String name, Object value) {
        properties.put(name, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    // Optional: helper method to get property by name
    public Object get(String key) {
        return properties.get(key);
    }

    @Override
    public String toString() {
        return "DynamicResponse{" +
                "properties=" + properties +
                '}';
    }
}
