package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Object view;
    private final Map<String, Object> model;

    public ModelAndView(Object view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }

    public ModelAndView(final View view) {
        this(view, new HashMap<>());
    }

    public ModelAndView(final String viewName) {
        this(viewName, new HashMap<>());
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        if (this.view instanceof View) {
            return (View) this.view;
        }
        return null;
    }

    public String getViewName() {
        if (this.view instanceof String) {
            return (String) this.view;
        }
        return null;
    }
}
