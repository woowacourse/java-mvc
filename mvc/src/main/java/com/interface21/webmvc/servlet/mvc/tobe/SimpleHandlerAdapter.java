package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.Model;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);
        JspView jspView = new JspView(viewName);
        Model model = new Model(request);
        return new ModelAndView(model, jspView);
    }
}
