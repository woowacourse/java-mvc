package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object instance;
    private final Method handlerMethod;

    public HandlerExecution(final Object instance, final Method handlerMethod) {
        this.instance = instance;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(instance, request, response);
    }
}
