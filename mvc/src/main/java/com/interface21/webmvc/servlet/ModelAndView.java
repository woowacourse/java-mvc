package com.interface21.webmvc.servlet;

import java.util.Map;

public class ModelAndView {

    private final String viewName;
    private final Model model;

    public ModelAndView(final String viewName) {
        this.viewName = viewName;
        this.model = new Model();
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.addObject(attributeName, attributeValue);
        return this;
    }

    public Object getObject(final String attributeName) {
        return model.getObject(attributeName);
    }

    public Map<String, Object> getModel() {
        return model.getAttributes();
    }

    public String getViewName() {
        return viewName;
    }
}
