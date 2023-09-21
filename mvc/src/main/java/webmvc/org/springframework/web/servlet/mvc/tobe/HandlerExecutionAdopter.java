package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecutionAdopter implements HandlerAdopter {
    @Override
    public boolean isSupport(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
