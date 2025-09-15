package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerAdapter {

    public ModelAndView execute(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        if (handler instanceof Controller controller) {
            return controller.execute(request, response);
        }

        if (handler instanceof HandlerExecution execution) {
            return execution.handle(request, response);
        }

        throw new IllegalArgumentException("Unhandled handler: " + handler);
    }
}
