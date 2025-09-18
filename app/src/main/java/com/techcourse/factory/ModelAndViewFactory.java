package com.techcourse.factory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class ModelAndViewFactory {

    private ModelAndViewFactory() {
    }

    public static ModelAndView createModelAndViewByViewName(HttpServletRequest request, String viewName) {
        JspView jspView = new JspView(viewName);
        Map<String, Object> model = buildModel(request);
        return new ModelAndView(jspView, model);
    }

    private static Map<String, Object> buildModel(final HttpServletRequest request) {
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
