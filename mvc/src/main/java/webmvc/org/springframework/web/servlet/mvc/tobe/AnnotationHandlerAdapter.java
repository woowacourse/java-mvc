package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }

    @Override
    public boolean isSupported(final Object handler) {
        return handler instanceof HandlerExecution;
    }
}
