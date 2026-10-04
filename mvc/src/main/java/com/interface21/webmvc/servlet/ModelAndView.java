package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(final View view) {
        validateView(view);
        this.view = view;
        this.model = new HashMap<>();
    }

    private void validateView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("View는 null일 수 없습니다.");
        }
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
