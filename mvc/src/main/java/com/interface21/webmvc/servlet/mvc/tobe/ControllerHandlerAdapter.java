package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView adapt(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        final View view = new JspView(viewName);

        return new ModelAndView(view);
    }
}
