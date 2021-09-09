package com.techcourse.air.mvc.core.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final Object view;
    private final Map<String, Object> model;

    public ModelAndView(Object view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public String getViewName() {
        if (view instanceof String) {
            return (String) view;
        }
        return null;
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public Object getView() {
        return view;
    }
}
