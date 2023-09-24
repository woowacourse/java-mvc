package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerExecution implements HandlerExecution{

    private final Object handler;
    private final Method method;

    public AnnotationHandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
