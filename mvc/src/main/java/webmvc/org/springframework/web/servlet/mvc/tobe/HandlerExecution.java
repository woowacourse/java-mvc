package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Constructor<?> constructor;

    public HandlerExecution(Method method, Constructor<?> constructor) {
        this.method = method;
        this.constructor = constructor;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(constructor.newInstance(), request, response);
    }
}
