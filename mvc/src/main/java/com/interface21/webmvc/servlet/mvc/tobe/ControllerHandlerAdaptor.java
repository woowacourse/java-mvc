package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdaptor implements HandlerAdaptor {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }
}
