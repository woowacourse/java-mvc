package webmvc.org.springframework.web.servlet.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerExecution;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object controller) {
        return controller instanceof HandlerExecution;
    }

    @Override
    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) {
        return ((HandlerExecution) handler).handle(request, response);
    }
}
