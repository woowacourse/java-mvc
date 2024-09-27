package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Controller controller = (Controller) handler;
        String viewName = controller.execute(request, response);

        return new ModelAndView(createView(viewName));
    }

    private JspView createView(String viewName) {
        return new JspView(viewName);
    }
}
