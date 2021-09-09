package nextstep.mvc.controller.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.handler.mapping.HandlerExecution;
import nextstep.mvc.handler.adapter.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
