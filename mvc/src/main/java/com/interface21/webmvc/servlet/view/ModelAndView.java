package com.interface21.webmvc.servlet.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public void render(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            this.view.render(this.model, httpServletRequest, httpServletResponse);
        } catch (Exception exception) {
            throw new RuntimeException("View 렌더링 도중 오류가 발생하였습니다.");
        }
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
