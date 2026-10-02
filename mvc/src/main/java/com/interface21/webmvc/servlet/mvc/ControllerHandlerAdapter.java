package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class ControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        try {
            Controller controller = (Controller) handler;
            final var viewName = controller.execute(request, response);

            return new ModelAndView(new JspView(viewName));
        } catch (Throwable e) {
            throw new ServletException(e.getMessage());
        }
    }
}
