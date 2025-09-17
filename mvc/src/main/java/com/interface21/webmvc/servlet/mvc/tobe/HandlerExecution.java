package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    // 찾은 메서드를 실행해서 Model 데이터들과 View가 담긴 객체로 만든다.
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ModelAndView modelAndView;

        modelAndView = (ModelAndView) this.method.invoke(this.clazz.newInstance(), request, response);
        return modelAndView;
    }
}
