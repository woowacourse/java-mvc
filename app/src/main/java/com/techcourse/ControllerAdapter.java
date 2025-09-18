package com.techcourse;

import com.interface21.webmvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerAdapter implements HandlerAdapter {

    @Override
    public void doHandle(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        final var viewName = ((com.interface21.webmvc.servlet.mvc.asis.Controller) handler).execute(request, response);
        move(viewName, request, response);
    }

    @Override
    public boolean isProcessable(Object handler) {
        return handler instanceof Controller;
    }

    private void move(
            final String viewName,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
