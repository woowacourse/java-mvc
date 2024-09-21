package com.interface21.webmvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private final Map<String, Object> model;

    public Model() {
        this.model = new HashMap<>();
    }

    public Model(final HttpServletRequest request) {
        Map<String, Object> model = new HashMap<>();
        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            model.put(name, request.getAttribute(name));
        }
        this.model = model;
    }

    public void addAttribute(String name, Object value) {
        model.put(name, value);
    }

    public Object getAttribute(String name) {
        return model.get(name);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }
}
