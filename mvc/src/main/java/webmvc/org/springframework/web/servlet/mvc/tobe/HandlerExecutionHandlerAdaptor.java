package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdaptor;

public class HandlerExecutionHandlerAdaptor implements HandlerAdaptor {

    private final HandlerExecution handlerExecution;

    public HandlerExecutionHandlerAdaptor(final HandlerExecution handlerExecution) {
        this.handlerExecution = handlerExecution;
    }

    @Override
    public boolean supports(final Object target) {
        return target.getClass().isAssignableFrom(HandlerExecution.class);
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handlerExecution.handle(request, response);
    }
}
