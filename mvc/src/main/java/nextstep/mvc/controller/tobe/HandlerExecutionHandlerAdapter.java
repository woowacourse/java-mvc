package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        final var handlerExecution = (HandlerExecution) handler;
        final var result = handlerExecution.handle(request, response);
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }
        return new ModelAndView(new JspView(result.toString()));
    }
}
