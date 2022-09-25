package nextstep.mvc.controller.tobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionHandlerAdaptor implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionHandlerAdaptor.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
        final Object handler) {
        final HandlerExecution execution = (HandlerExecution)handler;
        return execution.handle(request, response);
    }
}
