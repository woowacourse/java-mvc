package com.techcourse.air.mvc.core.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.techcourse.air.mvc.core.controller.asis.Controller;
import com.techcourse.air.mvc.core.view.ModelAndView;

public class SimpleControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        return new ModelAndView(viewName);
    }
}
