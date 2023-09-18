package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private final Object handler;

    public AnnotationHandlerAdapter(Object handler) {
        this.handler = handler;
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
