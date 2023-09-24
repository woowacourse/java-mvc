package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response) {
        return ((HandlerExecution) handler).handle(request, response);
    }

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof HandlerExecution;
    }
}
