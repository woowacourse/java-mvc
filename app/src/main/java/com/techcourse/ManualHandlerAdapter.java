package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ManualHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String viewName = ((Controller) handler).execute(request, response);
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = buildModel(request);
        return new ModelAndView(jspView, model);
    }

    private Map<String, Object> buildModel(final HttpServletRequest request) {
        final var model = new HashMap<String, Object>();
        final Enumeration<String> attributeNames = request.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            final String attributeName = attributeNames.nextElement();
            final Object attributeValue = request.getAttribute(attributeName);
            model.put(attributeName, attributeValue);
        }
        return model;
    }
}
