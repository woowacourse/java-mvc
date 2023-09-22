package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotaionHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
