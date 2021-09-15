package nextstep.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.TextView;
import nextstep.web.support.FileType;

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

        changeView(modelAndView, handledValue);
        return modelAndView;
    }

    private void changeView(ModelAndView modelAndView, Object handledValue) {
        if (handledValue instanceof String) {
            changeViewWithStringValue(modelAndView, (String) handledValue);
            return;
        }
        if (handledValue instanceof ModelAndView) {
            modelAndView.changeView(new JsonView());
        }
    }

    private void changeViewWithStringValue(ModelAndView modelAndView, String handledValue) {
        if (FileType.matches(handledValue)) {
            modelAndView.changeView(new JspView(handledValue));
            return;
        }
        modelAndView.changeView(new TextView(handledValue));
    }
}
