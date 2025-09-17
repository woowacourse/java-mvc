package com.interface21.webmvc.servlet.mvc.tobe.handler.adaptor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            String viewName = ((Controller) handler).execute(request, response);
            JspView jspView = new JspView(viewName);

            return new ModelAndView(jspView);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean support(Object object) {
        return object instanceof Controller;
    }
}
