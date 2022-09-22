package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
