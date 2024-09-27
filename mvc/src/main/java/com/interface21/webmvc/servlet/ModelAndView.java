package com.interface21.webmvc.servlet;

import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Model model;

    public ModelAndView(final View view) {
        this(new Model(), view);
    }

    public ModelAndView(final Model model, final View view) {
        this.model = model;
        this.view = view;
    }

    public ModelAndView addObject(final String attributeName, final Object attributeValue) {
        model.addAttribute(attributeName, attributeValue);
        return this;
    }

    public Object getObject(final String attributeName) {
        return model.getAttribute(attributeName);
    }

    public Map<String, Object> getModel() {
        return model.getModel();
    }

    public View getView() {
        return view;
    }
}
