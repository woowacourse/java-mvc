package com.interface21.webmvc.servlet.mvc.handler;

import java.lang.reflect.Method;
import com.interface21.context.SimpleApplicationContext;
import com.interface21.webmvc.servlet.mvc.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(Method method) {
        this.method = method;
        this.instance = createInstance(method);
    }

    private Object createInstance(Method method) {
        SimpleApplicationContext applicationContext = SimpleApplicationContext.getInstance();
        return applicationContext.getBean(method.getDeclaringClass());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
