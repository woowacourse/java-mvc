package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;
    private final Object handler;

    public HandlerExecution(Method method) {
        try {
            this.method = method;
            this.handler = method.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception exception) {
            throw new IllegalArgumentException("핸들러를 초기화할 수 없습니다.", exception);
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
