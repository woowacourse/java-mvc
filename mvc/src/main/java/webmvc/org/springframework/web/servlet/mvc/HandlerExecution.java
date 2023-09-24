package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (ModelAndView) method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), request, response);
    }
}
