package com.interface21.webmvc.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private final View view;
    private final Map<String, Object> model;

    public ModelAndView(View view) {
        if (view == null) {
            throw new IllegalArgumentException("뷰 객체는 null일 수 없습니다.");
        }
        this.view = view;
        this.model = new HashMap<>();
    }

    public ModelAndView(View view, Map<String, Object> model) {
        if (view == null) {
            throw new IllegalArgumentException("뷰 객체는 null일 수 없습니다.");
        }
        this.view = view;
        this.model = new HashMap<>(model);
    }

    public View getView() {
        return this.view;
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
