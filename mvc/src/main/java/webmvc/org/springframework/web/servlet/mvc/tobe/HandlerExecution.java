package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method handlerMethod;

    public HandlerExecution(Object handler, Method handlerMethod) {
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(HttpServletRequest request,
                               HttpServletResponse response) throws Exception {
        return (ModelAndView) handlerMethod.invoke(handler, request, response);
    }
}
