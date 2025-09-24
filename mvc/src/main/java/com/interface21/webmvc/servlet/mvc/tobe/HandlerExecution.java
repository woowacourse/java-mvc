package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    // 찾은 메서드를 실행해서 Model 데이터들과 View가 담긴 객체로 만든다.
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;

        modelAndView = (ModelAndView) this.method.invoke(controller, request, response);
        return modelAndView;
    }
}
