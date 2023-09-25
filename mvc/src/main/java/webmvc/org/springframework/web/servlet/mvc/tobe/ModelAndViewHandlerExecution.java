package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class ModelAndViewHandlerExecution implements HandlerExecution {

    private final Object instance;
    private final Method method;

    public ModelAndViewHandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
