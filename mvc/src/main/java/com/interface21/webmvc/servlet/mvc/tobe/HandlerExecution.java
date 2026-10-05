package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object target;

    public HandlerExecution(Method method, Object target) {
        validateReturnType(method);
        this.method = method;
        this.target = target;
    }

    private void validateReturnType(final Method method) {
        if (!ModelAndView.class.isAssignableFrom(method.getReturnType())) {
            throw new IllegalStateException(String.format(
                    "핸들러 메서드는 ModelAndView를 반환해야 합니다: %s#%s (반환 타입: %s)",
                    method.getDeclaringClass().getName(), method.getName(), method.getReturnType().getName()));
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object invoked = method.invoke(target, request, response);
        return (ModelAndView) invoked;
    }
}
