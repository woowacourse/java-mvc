package com.interface21.webmvc.servlet.mvc.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter extends AbstractHandlerAdapter<HandlerExecution> {
    @Override
    protected ModelAndView handleInternal(
            final HandlerExecution handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        return handler.handle(request, response);
    }
}
