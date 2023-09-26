package webmvc.org.springframework.web.servlet.adaptor;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class AnnotationHandlerAdaptor implements HandlerAdaptor {
    @Override
    public boolean supports(final Object handler) {
        return ((HandlerExecution) handler).getController().getClass().isAnnotationPresent(Controller.class);
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
