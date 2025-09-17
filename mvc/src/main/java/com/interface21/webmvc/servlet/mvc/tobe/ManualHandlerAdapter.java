package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final String execute = ((Controller) handler).execute(request, response);
        final var view = new JspView(execute);
        view.render(buildModel(request), request, response);
    }

    private HashMap<String, Object> buildModel(final HttpServletRequest request) {
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
