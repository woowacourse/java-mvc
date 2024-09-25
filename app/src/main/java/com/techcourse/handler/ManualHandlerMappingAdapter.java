package com.techcourse.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.HandlerMappingAdapter;

public class ManualHandlerMappingAdapter implements HandlerMappingAdapter {

    private final ManualHandlerMapping handlerMapping;

    public ManualHandlerMappingAdapter() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        this.handlerMapping = manualHandlerMapping;
    }

    @Override
    public ModelAndView adapt(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Controller handler = handlerMapping.getHandler(request.getRequestURI());
        final String viewName = handler.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
