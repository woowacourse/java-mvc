package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(final Class<?> controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Constructor<?> constructor = controller.getDeclaredConstructor();
        final Object instance = constructor.newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
