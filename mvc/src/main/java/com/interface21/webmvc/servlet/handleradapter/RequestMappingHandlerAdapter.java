package com.interface21.webmvc.servlet.handleradapter;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestMappingHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler.getClass().getAnnotation(RequestMapping.class) != null;
    }

    @Override
    public ModelAndView handle(
            HttpServletRequest request,
            HttpServletResponse response, Object handler
    ) throws Exception
    {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
