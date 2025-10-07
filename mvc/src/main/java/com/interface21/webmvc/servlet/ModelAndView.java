package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Object view;
    private final Map<String, Object> model;

    public ModelAndView(String viewName) {
        this.view = viewName;
        this.model = new HashMap<>();
    }

    public ModelAndView(String viewName, Map<String, Object> model) {
        this.view = viewName;
        this.model = new HashMap<>(model);
    }

    public ModelAndView(View view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public boolean isReference() {
        return (this.view instanceof String);
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(this.model);
    }

    public void addObject(String attributeName, Object attributeValue) {
        this.model.put(attributeName, attributeValue);
    }

    public Object getObject(String key) {
        return this.model.get(key);
    }
}
