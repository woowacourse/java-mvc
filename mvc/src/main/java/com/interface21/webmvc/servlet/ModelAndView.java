package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private View view;
    private final Map<String, Object> model;

    //    public ModelAndView(final View view) {
    public ModelAndView(String viewName) {
        this.view =  new JspView(viewName);
        this.model = new HashMap<>();
    }

    public ModelAndView() {
        this.view = new JsonView();
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
}
