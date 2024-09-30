package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        this.view = view;
        this.model = Collections.emptyMap();
    }

    public ModelAndView(final View view, final Map<String, Object> model) {
        this.view = view;
        this.model = model;
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
