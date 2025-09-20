package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.HandlerAdapter;
import com.interface21.webmvc.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(final Object handler) {
        return handler instanceof NotFoundHandler;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request, final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        return new ModelAndView(new RedirectView("/404.jsp"));
    }
}
