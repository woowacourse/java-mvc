package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    private static final String JSP_EXTENSION = ".jsp";

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception {
        String viewName = getViewName((Controller)handler, request, response);
        return new ModelAndView(new JspView(viewName));
    }

    private String getViewName(final Controller handler, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception {
        String viewName = handler.execute(request, response);
        int extensionIndex = viewName.lastIndexOf(".");
        String originExtension = viewName.substring((extensionIndex));
        if (originExtension.equals(JSP_EXTENSION)) {
            return viewName;
        }
        String baseName = viewName.substring(0, extensionIndex);
        return String.format("%s.%s", baseName, JSP_EXTENSION);
    }
}
