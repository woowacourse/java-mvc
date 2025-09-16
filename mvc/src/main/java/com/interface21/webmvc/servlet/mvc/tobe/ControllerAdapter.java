package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) {
        try {
            return doHandle(request, response, handler);
        } catch (final Exception e) {
            throw new IllegalStateException("ControllerAdapter failed to handle request", e);
        }
    }

    private ModelAndView doHandle(final HttpServletRequest request, final HttpServletResponse response,
                                  final Object handler) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        final View view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
