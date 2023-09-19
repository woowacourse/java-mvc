package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

public class HandlerExecutionAdapter implements HandlerAdapter {

    private final HandlerExecution handlerExecution;

    public HandlerExecutionAdapter(final Object handlerExecution) {
        this.handlerExecution = (HandlerExecution) handlerExecution;
    }

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handlerExecution.handle(request, response);
    }
}
