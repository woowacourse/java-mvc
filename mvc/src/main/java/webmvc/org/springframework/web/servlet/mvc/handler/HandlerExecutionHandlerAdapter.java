package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private final HandlerExecution handlerExecution;

    public HandlerExecutionHandlerAdapter(final Object handler) {
        this.handlerExecution = (HandlerExecution) handler;
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final ModelAndView modelAndView = handlerExecution.handle(request, response);
        return modelAndView.getView().getName();
    }
}
