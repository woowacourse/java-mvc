package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
