package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response){
        final String viewName = ((Controller) handler).execute(request, response);
        final View view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
