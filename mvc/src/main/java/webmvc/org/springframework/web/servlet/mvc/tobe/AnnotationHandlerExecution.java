package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.HandlerExecution;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerExecution implements HandlerExecution {

    private final Object instance;
    private final Method method;

    public AnnotationHandlerExecution(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(instance, request, response);
    }

}
