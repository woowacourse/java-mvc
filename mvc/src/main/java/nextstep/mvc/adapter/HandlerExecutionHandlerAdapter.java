package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
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
        if (handledValue instanceof String) {
            return changeViewWithStringValue(modelAndView, (String) handledValue);
        }
        if (handledValue instanceof ModelAndView) {
            modelAndView.changeView(new JsonView());
            return modelAndView;
        }
        return modelAndView;
    }

    private ModelAndView changeViewWithStringValue(ModelAndView modelAndView, String handledValue) {
        if (handledValue.contains(".")) {
            modelAndView.changeView(new JspView(handledValue));
            return modelAndView;
        }
        modelAndView.changeView(new JsonView(handledValue));
        return modelAndView;
    }
}
