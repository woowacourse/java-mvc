package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final String viewName;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        this(view, null);
    }

    public ModelAndView(final String viewName) {
        this(null, viewName);
    }

    private ModelAndView(final View view, final String viewName) {
        this.view = view;
        this.viewName = viewName;
        this.model = new HashMap<>();
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
        return view;
    }

    public String getViewName() {
        return viewName;
    }
}
