package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof Controller controller) {
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } else {
            return null;
        }
    }
}
