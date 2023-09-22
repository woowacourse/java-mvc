package webmvc.org.springframework.web.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class HandlerExecutionAdapter implements Adapter{
    @Override
    public boolean isPossibleToHandle(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
