package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method handlerMethod;

    public HandlerExecution(final Method handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Class<?> controller = handlerMethod.getDeclaringClass();
        final Object controllerInstance = controller.getConstructor().newInstance();
        return (ModelAndView) handlerMethod.invoke(controllerInstance, request, response);
    }
}
