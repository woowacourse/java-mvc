package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public Object execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        final HandlerExecution execution = (HandlerExecution) handler;
        return execution.handle(request, response);
    }
}
