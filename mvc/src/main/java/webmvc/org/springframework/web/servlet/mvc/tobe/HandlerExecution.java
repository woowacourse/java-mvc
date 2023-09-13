package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Class<?> clazz;

    public HandlerExecution(final Method method, final Class<?> clazz){
        this.method = method;
        this.clazz = clazz;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object object = clazz.getDeclaredConstructor().newInstance();
        return (ModelAndView) method.invoke(object, request, response);
    }
}
