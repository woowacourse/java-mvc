package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        this.view = view;
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

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final ModelAndView that = (ModelAndView) object;
        return Objects.equals(view, that.view) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(view, model);
    }
}
