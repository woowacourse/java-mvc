package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlerAdapterImpl implements HandlerAdapter {

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        if (handler != null) {
            if (handler instanceof HandlerExecution) {
                final var modelAndView = ((HandlerExecution) handler).handle(request, response);
                modelAndView.getView().render(modelAndView.getModel(), request, response);
            } else if (handler instanceof Controller) {
                final var viewName = ((Controller) handler).execute(request, response);
                move(viewName, request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    private void move(
            final String viewName,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final var view = new JspView(viewName);
        view.render(Map.of(), request, response);
    }
}
