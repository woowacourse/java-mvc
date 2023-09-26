package webmvc.org.springframework.web.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.InvocationTargetException;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return ((HandlerExecution) handler).handle(request, response);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
