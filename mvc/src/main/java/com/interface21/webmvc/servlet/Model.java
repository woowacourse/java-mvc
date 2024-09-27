package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, Object> attributes;

    public Model() {
        this.attributes = new HashMap<>();
    }

    public void addObject(final String attributeName, final Object attributeValue) {
        attributes.put(attributeName, attributeValue);
    }

    public Object getObject(final String attributeName) {
        return attributes.get(attributeName);
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
