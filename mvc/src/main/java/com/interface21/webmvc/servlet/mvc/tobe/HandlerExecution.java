package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        final Enumeration<String> attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            final String name = attributes.nextElement();
            final Object value = request.getAttribute(name);
            modelAndView.addObject(name, value);
        }

        return modelAndView;
    }
}
