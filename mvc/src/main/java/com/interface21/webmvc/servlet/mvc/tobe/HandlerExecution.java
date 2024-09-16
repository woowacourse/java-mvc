package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ApplicationContext applicationContext = SimpleApplicationContext.getInstance();
        Object instance = applicationContext.getBean(method.getDeclaringClass());

        return (ModelAndView) method.invoke(instance, request, response);
    }
}
