package com.techcourse.handler;

import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.HandlerAdapter;

public class ManualHandlerAdapter implements HandlerAdapter {

    private final ManualHandlerMapping handlerMapping;

    public ManualHandlerAdapter() {
        final ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        this.handlerMapping = manualHandlerMapping;
    }

    @Override
    public ModelAndView adapt(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Controller controller = handlerMapping.getHandler(request.getRequestURI());
        final String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    @Override
    public boolean support(final HttpServletRequest request) {
        final Controller handler = handlerMapping.getHandler(request.getRequestURI());
        return !Objects.isNull(handler);
    }
}
