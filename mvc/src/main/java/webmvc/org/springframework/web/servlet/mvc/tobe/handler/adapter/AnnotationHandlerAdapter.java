package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper.HandlerExecution;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.execute(request, response);
    }

    @Override
    public boolean isSupported(final Object handler) {
        return handler instanceof HandlerExecution;
    }
}
