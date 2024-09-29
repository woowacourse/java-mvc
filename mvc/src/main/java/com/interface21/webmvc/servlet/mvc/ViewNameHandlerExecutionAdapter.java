package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ViewNameHandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.getReturnType() == String.class;
        }

        return false;
    }

    @Override
    public ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        String viewName = (String) handlerExecution.handle(request, response);
        View view = new JspView(viewName);

        return new ModelAndView(view);
    }
}
