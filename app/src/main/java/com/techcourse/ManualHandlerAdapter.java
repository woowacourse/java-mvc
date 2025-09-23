package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {
        final String view = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(view));
    }

    @Override
    public boolean canHandle(Object handler) {
        return handler instanceof Controller;
    }
}
