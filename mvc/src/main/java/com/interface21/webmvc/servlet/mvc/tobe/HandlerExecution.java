package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;   // 컨트롤러 인스턴스
    private final Method method;    // 메서드

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
        this.method.setAccessible(true);    // 접근 허용
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if(method.getParameterCount() == 2){
            return (ModelAndView) method.invoke(handler, request, response);
        }
        return null;
    }
}
