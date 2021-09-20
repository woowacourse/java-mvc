package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        final ModelAndView modelAndView = new ModelAndView();
        final Object handledValue = handlerExecution.handle(request, response, modelAndView);
        return resolveModelAndView(modelAndView, handledValue);
    }

    private ModelAndView resolveModelAndView(ModelAndView modelAndView, Object handledValue) {
        if (handledValue instanceof String) {
            modelAndView.changeViewName((String) handledValue);
        }
        return modelAndView;
    }
}
