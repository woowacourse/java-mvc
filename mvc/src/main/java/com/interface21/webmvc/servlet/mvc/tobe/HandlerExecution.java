package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object target;
    private final Method method;

    public HandlerExecution(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // 리플렉션으로 컨트롤러 메서드 실행
        Object result = method.invoke(target, request, response);

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        throw new IllegalStateException("Controller 메서드가 ModelAndView를 반환하지 않았습니다." + method.getName());
    }
}
