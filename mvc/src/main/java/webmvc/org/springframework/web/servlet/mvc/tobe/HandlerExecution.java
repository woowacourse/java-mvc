package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.lang.reflect.Method;

public class HandlerExecution {

<<<<<<< HEAD
    private final Object instance;
    private final Method method;

    public HandlerExecution(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(instance, request, response);
=======
    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(clazz.newInstance(), request, response);
>>>>>>> 6ce849f ([MVC 구현하기 - 1단계] 제이미(임정수) 미션 제출합니다. (#411))
    }
}
