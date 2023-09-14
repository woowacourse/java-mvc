package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object instance = clazz.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
