package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.CanNotHandleException;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            return ((HandlerExecution) handler).handle(request, response);
        } catch (Exception e) {
            throw new CanNotHandleException();
        }
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
