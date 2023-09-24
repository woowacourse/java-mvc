package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerExecution implements HandlerExecution{

    private final Object handler;
    private final Method method;

    public AnnotationHandlerExecution(Class<?> clazz, Method method) {
        this.handler = instantiateController(clazz);
        this.method = method;
    }

    private Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InstantiateControllerException();
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        return (ModelAndView) method.invoke(handler, request, response);
    }
}
