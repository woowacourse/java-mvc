package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

// "나중에 이 메서드를 실행하려고 객체 + 메서드를 묶어서 보관하는 객체"
//어떤 컨트롤러 객체의 어떤 메서드를 실행해야 하는지 기억했다가 실행한다.
public class HandlerExecution {
    private final Object controller;
    private final Method method;

    public HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response);
    }
}
