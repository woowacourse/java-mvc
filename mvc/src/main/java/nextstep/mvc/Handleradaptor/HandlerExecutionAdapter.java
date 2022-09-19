package nextstep.mvc.Handleradaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final HandlerExecution handlerExecution = (HandlerExecution) handler;

        Object result = handlerExecution.handle(request, response);

        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }

        return (ModelAndView) result;
    }
}
