package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return HandlerExecution.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView handle(
            Object handler,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(httpServletRequest, httpServletResponse);
    }
}
