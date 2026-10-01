package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

// 선택된 컨트롤러 메서드 실행
public class HandlerExecution {
    private final Object  controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // (캐스팅) 실행메소드.invoke(객체, 매개변수 ... )
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
