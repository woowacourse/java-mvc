package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
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
                               HttpServletResponse response) {
        try {
            return (ModelAndView) handlerMethod.invoke(handler, request, response);
        } catch (InvocationTargetException |
                 IllegalAccessException e) {
            throw new IllegalArgumentException("Annotation Handler 를 handle 하는 도중 예외가 발생했습니다.", e);
        }
    }
}
