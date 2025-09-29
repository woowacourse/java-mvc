package com.interface21.webmvc.servlet;

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

    public Object getObject(final String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public void render(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            view.render(model, request, response);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("%s %s 요청에 대한 응답을 렌더링하는데 실패했습니다.", request.getRequestURI(),
                            request.getMethod()));
        }
    }
}
