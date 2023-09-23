package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.supports.mapping.HandlerExecution;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
