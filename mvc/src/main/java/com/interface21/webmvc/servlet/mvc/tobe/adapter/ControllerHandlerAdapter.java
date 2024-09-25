package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ControllerHandlerAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        log.info("[Adapter 실행] - ControllerHandlerAdapter");

        Controller controller = (Controller) handler;
        final String viewPath = controller.execute(request, response);
        final JspView view = new JspView(viewPath);
        return new ModelAndView(view);
    }
}
