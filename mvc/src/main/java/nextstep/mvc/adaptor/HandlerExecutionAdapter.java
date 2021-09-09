package nextstep.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.handler.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Throwable {
        if (handler instanceof HandlerExecution) {
            HandlerExecution handlerExecution = (HandlerExecution) handler;
            return handlerExecution.handle(request, response);
        }
        throw new MvcComponentException("해당 HandlerAdaptor가 처리할 수 있는 handler가 아닙니다.");
    }
}
