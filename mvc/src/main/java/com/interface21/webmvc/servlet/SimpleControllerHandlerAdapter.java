package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Controller controller = (Controller) handler;
        final String result = controller.execute(request, response);

        ModelAndView modelAndView = new ModelAndView(result);
        addAttributes(request, modelAndView);
        return modelAndView;
    }

    private void addAttributes(HttpServletRequest request, ModelAndView modelAndView) {
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (attributeNames == null) {
            return;
        }

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = request.getAttribute(attributeName);
            modelAndView.addObject(attributeName, attributeValue);
        }
    }
}
