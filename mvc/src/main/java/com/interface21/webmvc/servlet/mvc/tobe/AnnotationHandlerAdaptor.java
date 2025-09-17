package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.HandlerAdaptor;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @RequestMapping 애노테이션이 붙은 핸들러(HandlerExecution)를 실행할 수 있도록 지원
 * DispatcherServlet이 찾은 핸들러를 실제 실행시킨다.
 */
public class AnnotationHandlerAdaptor implements HandlerAdaptor {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerExecution);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
